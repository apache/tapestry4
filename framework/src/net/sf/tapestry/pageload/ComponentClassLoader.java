package net.sf.tapestry.pageload;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ParameterSpecification;
import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.Constant;
import org.apache.bcel.classfile.ConstantClass;
import org.apache.bcel.classfile.ConstantFieldref;
import org.apache.bcel.classfile.ConstantNameAndType;
import org.apache.bcel.classfile.ConstantPool;
import org.apache.bcel.classfile.ConstantString;
import org.apache.bcel.classfile.ConstantUtf8;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.generic.FieldInstruction;
import org.apache.bcel.generic.Instruction;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.LDC;
import org.apache.bcel.generic.TargetLostException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author mindbridge
 *
 */
public class ComponentClassLoader extends ClassLoader
{
	private static final Log LOG =
		LogFactory.getLog(ComponentClassLoader.class);

	private static final String TEMPLATE_CLASS_NAME =
		EnhancedComponentTemplate.class.getName();

	private static final String TEMPLATE_VALUE_FIELD =
		"_$templateParameterValue";
	private static final String TEMPLATE_CACHED_FIELD =
		"_$templateParameterCached";
	private static final String TEMPLATE_RESET_METHOD = "resetParametersCache";
	private static final String TEMPLATE_GET_METHOD = "get$TemplateParameter";
	private static final String TEMPLATE_SET_METHOD = "set$TemplateParameter";
	private static final String TEMPLATE_PROPERTY_NAME = "$TemplateParameter";

	private static final String CONTEXT_CONSTANTS_KEY = "constants";
	private static final String CONTEXT_FIELDS_KEY = "fields";
	private static final String CONTEXT_METHODS_KEY = "methods";
	private static final String CONTEXT_VALUE_FIELD_KEY = "valueField";
	private static final String CONTEXT_CACHED_FIELD_KEY = "cachedField";
	private static final String CONTEXT_RESET_METHOD_KEY = "resetMethod";
	private static final String CONTEXT_RESET_CODE_KEY = "resetCode";
	private static final String CONTEXT_RESET_TEMPLATE_KEY = "resetTemplate";
	private static final String CONTEXT_GET_METHOD_KEY = "getMethod";
	private static final String CONTEXT_SET_METHOD_KEY = "setMethod";

	private IResourceResolver _resolver;
	private byte[] _templateClassData;

	public ComponentClassLoader(IResourceResolver resolver)
	{
		setResolver(resolver);
		setTemplateClassData(loadClassData(TEMPLATE_CLASS_NAME));
	}

	/**
	 * @see java.lang.ClassLoader#loadClass(String)
	 */
	public Class loadClass(String name) throws ClassNotFoundException
	{
		//System.out.println("loading class: " + name);
		return getResolver().findClass(name);
	}

	/**
	 * Returns the resolver.
	 * @return IResourceResolver
	 */
	public IResourceResolver getResolver()
	{
		return _resolver;
	}

	/**
	 * Sets the resolver.
	 * @param resolver The resolver to set
	 */
	public void setResolver(IResourceResolver resolver)
	{
		_resolver = resolver;
	}

	/**
	 * Returns the templateClassData.
	 * @return byte[]
	 */
	protected byte[] getTemplateClassData()
	{
		return _templateClassData;
	}

	/**
	 * Sets the templateClassData.
	 * @param templateClassData The templateClassData to set
	 */
	protected void setTemplateClassData(byte[] templateClassData)
	{
		_templateClassData = templateClassData;
	}

	public Class findComponentClass(ComponentSpecification spec)
		throws ComponentClassLoaderException
	{
		String className = spec.getComponentClassName();

		Class originalClass = getResolver().findClass(className);

		// check whether enhancement is necessary
		if (getTemplateClassData() == null)
			return originalClass;

		List cachedParameters = getCachedParameters(spec);
		//if (cachedParameters.isEmpty())
		//    return originalClass;

		// okay, enhance
		Class componentClass =
			generateComponentClass(originalClass, cachedParameters);

		return componentClass;
	}

	protected List getCachedParameters(ComponentSpecification spec)
	{
		ArrayList cachedParameters = new ArrayList();

		List parameterNames = spec.getParameterNames();
		int parameterCount = parameterNames.size();
		for (int i = 0; i < parameterCount; i++)
		{
			ParameterSpecification paramSpec =
				spec.getParameter((String) parameterNames.get(i));
			Direction direction = paramSpec.getDirection();
			if (direction.equals(Direction.CACHED))
				cachedParameters.add(paramSpec);
		}

		return cachedParameters;
	}

	protected Class generateComponentClass(
		Class originalClass,
		List cachedParameters)
		throws ComponentClassLoaderException
	{
		try
		{
			JavaClass javaClass;
			try
			{
				javaClass = loadTemplate(originalClass);
			}
			catch (ClassFormatError e)
			{
				LOG.error("Cannot parse template class", e);
				throw new ComponentClassLoaderException(
					"Cannot parse template class",
					e);
			}

			Map context = createClassContext(javaClass);

			updateCommonTemplateData(javaClass, context, originalClass);

			int cachedParametersCount = cachedParameters.size();
			for (int i = 0; i < cachedParametersCount; i++)
			{
				ParameterSpecification paramSpec =
					(ParameterSpecification) cachedParameters.get(i);
				updateParameterTemplateData(
					javaClass,
					context,
					originalClass,
					paramSpec);
			}

			applyClassContext(javaClass, context);

			System.out.println(javaClass.toString());

			System.out.println("--- constructor:");
			Method initMethod = findClassMethod(javaClass, "<init>");
			InstructionList il =
				new InstructionList(initMethod.getCode().getCode());
			printInstructions(il.getStart());

			ByteArrayOutputStream bouts = new ByteArrayOutputStream();
			javaClass.dump(bouts);
			byte[] classData = bouts.toByteArray();

			Class enhancedClass =
				defineClass(null, classData, 0, classData.length);

			return enhancedClass;
		}
		catch (ClassFormatError e)
		{
			LOG.error("Error in class generation", e);
			throw new ComponentClassLoaderException(
				"Error in class generation",
				e);
		}
		catch (IOException e)
		{
			LOG.error("Cannot load template class", e);
			return null;
		}
	}

	protected JavaClass loadTemplate(Class originalClass)
		throws ClassFormatError, IOException
	{
		ByteArrayInputStream ins =
			new ByteArrayInputStream(getTemplateClassData());
		ClassParser classParser =
			new ClassParser(
				ins,
				fileNameFromClassName(originalClass.getName()));
		return classParser.parse();
	}

	/**
	 * Loads the class file into a byte array.
	 * @param className
	 * @return byte[]
	 */
	protected byte[] loadClassData(String className)
	{
		String classPath = fileNameFromClassName(className);
		try
		{
			URL classUrl = getResolver().getResource(classPath);
			InputStream ins = classUrl.openStream();
			ByteArrayOutputStream outs = new ByteArrayOutputStream();

			int len = 0;
			byte[] buffer = new byte[8192];
			while ((len = ins.read(buffer)) > 0)
				outs.write(buffer, 0, len);

			byte[] classData = outs.toByteArray();
			ins.close();
			outs.close();

			return classData;
		}
		catch (IOException e)
		{
			// NEED TO LOCALIZE MESSAGE!
			LOG.error("Cannot load template class data", e);
			return null;
		}
	}

	protected Map createClassContext(JavaClass javaClass)
		throws ComponentClassLoaderException
	{
		Map context = new HashMap();

		// constants to add
		ConstantPool constantPool = javaClass.getConstantPool();
		Constant[] constants = constantPool.getConstantPool();
		List constantList = Arrays.asList(constants);
		ArrayList constantArrayList = new ArrayList(constantList);
		context.put(CONTEXT_CONSTANTS_KEY, constantArrayList);

		// list of fields to update
		List fieldsList = Arrays.asList(javaClass.getFields());
		context.put(CONTEXT_FIELDS_KEY, new ArrayList(fieldsList));

		// list of methods to update
		ArrayList methodList =
			new ArrayList(Arrays.asList(javaClass.getMethods()));
		context.put(CONTEXT_METHODS_KEY, methodList);

		// field templates to use for creation of new fields
		Field valueField = findClassField(javaClass, TEMPLATE_VALUE_FIELD);
		if (valueField == null)
			throw new ComponentClassLoaderException(
				"Invalid template: cannot find field " + TEMPLATE_VALUE_FIELD);
		context.put(CONTEXT_VALUE_FIELD_KEY, valueField);

		Field cachedField = findClassField(javaClass, TEMPLATE_CACHED_FIELD);
		if (valueField == null)
			throw new ComponentClassLoaderException(
				"Invalid template: cannot find field " + TEMPLATE_CACHED_FIELD);
		context.put(CONTEXT_CACHED_FIELD_KEY, cachedField);

		// reset method body to update
		Method resetMethod = findClassMethod(javaClass, TEMPLATE_RESET_METHOD);
		if (resetMethod == null)
			throw new ComponentClassLoaderException(
				"Invalid template: cannot find method "
					+ TEMPLATE_RESET_METHOD);
		context.put(CONTEXT_RESET_METHOD_KEY, resetMethod);

		InstructionList resetMethodInstructionList =
			new InstructionList(resetMethod.getCode().getCode());
		context.put(CONTEXT_RESET_CODE_KEY, resetMethodInstructionList);

		// reset template
		try
		{
			InstructionList resetMethodTemplateList =
				new InstructionList(resetMethod.getCode().getCode());
			resetMethodTemplateList.delete(resetMethodTemplateList.getEnd());
			context.put(CONTEXT_RESET_TEMPLATE_KEY, resetMethodTemplateList);
		}
		catch (TargetLostException e)
		{
			throw new ComponentClassLoaderException(e);
		}

		// get method template
		Method getMethod = findClassMethod(javaClass, TEMPLATE_GET_METHOD);
		if (getMethod == null)
			throw new ComponentClassLoaderException(
				"Invalid template: cannot find method " + TEMPLATE_GET_METHOD);
		context.put(CONTEXT_GET_METHOD_KEY, getMethod);

		// set method template
		Method setMethod = findClassMethod(javaClass, TEMPLATE_SET_METHOD);
		if (setMethod == null)
			throw new ComponentClassLoaderException(
				"Invalid template: cannot find method " + TEMPLATE_SET_METHOD);
		context.put(CONTEXT_SET_METHOD_KEY, setMethod);

		return context;
	}

	protected void applyClassContext(JavaClass javaClass, Map context)
	{
		// update constants
		ConstantPool constantPool = javaClass.getConstantPool();
		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		int i = 0;
		for (Iterator it = constantList.iterator(); it.hasNext();)
		{
			Object element = (Object) it.next();
			//System.out.println("[" + i + "] " + element);
			i++;
		}
		Constant[] constants =
			(Constant[]) constantList.toArray(
				new Constant[constantList.size()]);
		constantPool.setConstantPool(constants);

		// update fields
		List fieldsList = (List) context.get(CONTEXT_FIELDS_KEY);
		Field[] fields =
			(Field[]) fieldsList.toArray(new Field[fieldsList.size()]);
		javaClass.setFields(fields);

		// update methods
		List methodList = (List) context.get(CONTEXT_METHODS_KEY);
		Method[] methods =
			(Method[]) methodList.toArray(new Method[methodList.size()]);
		javaClass.setMethods(methods);

		/*
		// update reset method
		Method resetMethod = (Method) context.get(CONTEXT_RESET_METHOD_KEY);
		InstructionList resetMethodInstructionList =
			(InstructionList) context.get(CONTEXT_RESET_CODE_KEY);
		byte[] code = resetMethodInstructionList.getByteCode();
		resetMethod.getCode().setCode(code);
		*/

		Method getMethod = findClassMethod(javaClass, TEMPLATE_GET_METHOD);

		InstructionList resetMethodInstructionList =
			new InstructionList(getMethod.getCode().getCode());
		printInstructions(resetMethodInstructionList.getStart());
	}

	protected void updateCommonTemplateData(
		JavaClass javaClass,
		Map context,
		Class originalClass)
	{
		String originalClassName = originalClass.getName();

		// generate a unique class name
		String componentClassName =
			javaClass.getClassName()
				+ "$"
				+ originalClassName.replace('.', '$');

		// do some surgery here

		String convertedClassName = componentClassName.replace('.', '/');
		javaClass.setClassName(componentClassName);
		updateClassConstant(
			context,
			javaClass.getClassNameIndex(),
			convertedClassName);

		String convertedSuperclassName = originalClassName.replace('.', '/');
		javaClass.setSuperclassName(originalClassName);
		updateClassConstant(
			context,
			javaClass.getSuperclassNameIndex(),
			convertedSuperclassName);
	}

	protected void updateClassConstant(
		Map context,
		int index,
		String className)
	{
		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		ConstantClass classConstant = (ConstantClass) constantList.get(index);
		ConstantUtf8 classNameConstant =
			(ConstantUtf8) constantList.get(classConstant.getNameIndex());
		classNameConstant.setBytes(className);
	}

	protected void updateParameterTemplateData(
		JavaClass javaClass,
		Map context,
		Class originalClass,
		ParameterSpecification paramSpec)
		throws ComponentClassLoaderException
	{
		// check if a getter exists
		String propName = paramSpec.getPropertyName();
		String propTypeName = getPropTypeName(originalClass, propName);

		Map fieldMapping = new HashMap();
		Map constantMapping = new HashMap();

		addPropertyFields(
			javaClass,
			context,
			propName,
			propTypeName,
			fieldMapping,
			constantMapping);

		// add field initialization
		addFieldReset(
			javaClass,
			context,
			propName,
			fieldMapping,
			constantMapping);

		// add getter method
		addGetMethod(
			javaClass,
			context,
			propName,
			fieldMapping,
			constantMapping);

		// check if a setter exists
		// add setter method
	}

	protected String getPropTypeName(Class originalClass, String propName)
		throws ComponentClassLoaderException
	{
		java.lang.reflect.Method propMethod =
			findGetMethod(originalClass, propName);

		if (propMethod == null)
			throw new ComponentClassLoaderException(
				"No get method for property " + propName + " has been defined");

		if (propMethod.getModifiers() != (Modifier.PUBLIC | Modifier.ABSTRACT))
			throw new ComponentClassLoaderException(
				"Property "
					+ propName
					+ " is defined as 'cached', but its get method is not 'public abstract'");

		Class returnType = propMethod.getReturnType();
		String propTypeName = returnType.getName();
		if (!returnType.isPrimitive() && !returnType.isArray())
			propTypeName = "L" + propTypeName.replace('.', '/') + ";";

		return propTypeName;
	}

	protected void addPropertyFields(
		JavaClass javaClass,
		Map context,
		String propName,
		String propTypeName,
		Map fieldMapping,
		Map constantMapping)
	{
		// add member variables
		String propValueFieldName = "_$" + propName + "Value";
		Field templateValueField = (Field) context.get(CONTEXT_VALUE_FIELD_KEY);
		int valueFieldId =
			addClassField(
				javaClass,
				context,
				propValueFieldName,
				propTypeName,
				templateValueField);

		String propCachedFieldName = "_$" + propName + "Cached";
		Field templateCachedField =
			(Field) context.get(CONTEXT_CACHED_FIELD_KEY);
		int cachedFieldId =
			addClassField(
				javaClass,
				context,
				propCachedFieldName,
				"Z",
				templateCachedField);

		fieldMapping.put(
			new Integer(templateValueField.getNameIndex()),
			new Integer(valueFieldId));
		fieldMapping.put(
			new Integer(templateCachedField.getNameIndex()),
			new Integer(cachedFieldId));

		int templateParamId =
			findStringConstant(javaClass, TEMPLATE_PROPERTY_NAME);
		int paramId = addStringConstant(context, propName);

		constantMapping.put(new Integer(templateParamId), new Integer(paramId));
	}

	protected String fileNameFromClassName(String className)
	{
		return className.replace('.', '/') + ".class";
	}

	protected java.lang.reflect.Method findGetMethod(
		Class originalClass,
		String propName)
	{
		try
		{
			String getterName = getGetMethodName(propName);
			java.lang.reflect.Method getterMethod =
				originalClass.getMethod(getterName, new Class[0]);

			// another way to do it (but slower)
			//OgnlContext ognlContext = new OgnlContext();
			//Method getterMethod = OgnlRuntime.getGetMethod(ognlContext, originalClass, propName);

			return getterMethod;
		}
		catch (NoSuchMethodException e)
		{
			return null;
		}
	}

	protected String getGetMethodName(String propName)
	{
		String getMethodName =
			"get"
				+ propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		return getMethodName;
	}

	protected String getSetMethodName(String propName)
	{
		String getMethodName =
			"set"
				+ propName.substring(0, 1).toUpperCase()
				+ propName.substring(1);
		return getMethodName;
	}

	protected Field findClassField(JavaClass javaClass, String fieldName)
	{
		ConstantPool constantPool = javaClass.getConstantPool();
		Field[] classFields = javaClass.getFields();
		for (int i = 0; i < classFields.length; i++)
		{
			Field field = classFields[i];
			if (field.getName().equals(fieldName))
				return field;
		}

		return null;
	}

	protected Method findClassMethod(JavaClass javaClass, String methodName)
	{
		Method[] classMethods = javaClass.getMethods();
		for (int i = 0; i < classMethods.length; i++)
		{
			Method method = classMethods[i];
			if (method.getName().equals(methodName))
				return method;
		}

		return null;
	}

	protected int findStringConstant(JavaClass javaClass, String constValue)
	{
		ConstantPool constantPool = javaClass.getConstantPool();
		int len = constantPool.getLength();
		for (int i = 0; i < len; i++)
		{
			Constant constant = constantPool.getConstant(i);
			if (constant instanceof ConstantString)
			{
				ConstantString stringConstant = (ConstantString) constant;
				String constantValue =
					(String) stringConstant.getConstantValue(constantPool);
				if (constValue.equals(constantValue))
					return i;
			}
		}

		return -1;
	}

	protected int addUtfConstant(Map context, String constant)
	{
		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		constantList.add(new ConstantUtf8(constant));
		return constantList.size() - 1;
	}

	protected int addStringConstant(Map context, String constant)
	{
		int utfId = addUtfConstant(context, constant);

		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		constantList.add(new ConstantString(utfId));
		return constantList.size() - 1;
	}

	protected int addNameAndTypeConstant(Map context, String name, String type)
	{
		int nameId = addUtfConstant(context, name);
		int typeId = addUtfConstant(context, type);

		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		constantList.add(new ConstantNameAndType(nameId, typeId));
		return constantList.size() - 1;
	}

	protected int addFieldRefConstant(
		JavaClass javaClass,
		Map context,
		String name,
		String type)
	{
		int nameAndTypeId = addNameAndTypeConstant(context, name, type);

		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		constantList.add(
			new ConstantFieldref(javaClass.getClassNameIndex(), nameAndTypeId));
		return constantList.size() - 1;
	}

	protected int addClassConstant(Map context, String constant)
	{
		int utfId = addUtfConstant(context, constant);

		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);
		constantList.add(new ConstantClass(utfId));
		return constantList.size() - 1;
	}

	protected int addClassField(
		JavaClass javaClass,
		Map context,
		String fieldName,
		String typeName,
		Field templateField)
	{
		List constantList = (List) context.get(CONTEXT_CONSTANTS_KEY);

		int fieldId =
			addFieldRefConstant(javaClass, context, fieldName, typeName);
		ConstantFieldref fieldConst =
			(ConstantFieldref) constantList.get(fieldId);
		ConstantNameAndType nameAndTypeConst =
			(ConstantNameAndType) constantList.get(
				fieldConst.getNameAndTypeIndex());

		int nameId = nameAndTypeConst.getNameIndex();
		int typeId = nameAndTypeConst.getSignatureIndex();

		Field field = new Field(templateField);
		field.setNameIndex(nameId);
		field.setSignatureIndex(typeId);

		List fieldsList = (List) context.get(CONTEXT_FIELDS_KEY);
		fieldsList.add(field);

		return fieldId;
	}

	protected void addFieldReset(
		JavaClass javaClass,
		Map context,
		String propName,
		Map fieldMapping,
		Map constantMapping)
	{
		InstructionList resetMethodTemplateList =
			(InstructionList) context.get(CONTEXT_RESET_TEMPLATE_KEY);
		InstructionList resetList = resetMethodTemplateList.copy();

		modifyTemplateInstructions(
			javaClass,
			resetList,
			fieldMapping,
			constantMapping);

		// Insert before the return
		InstructionList resetMethodList =
			(InstructionList) context.get(CONTEXT_RESET_CODE_KEY);
		resetMethodList.insert(resetMethodList.getEnd(), resetList);
	}

	protected void addMethod(Map context, Method method)
	{
		List methodList = (List) context.get(CONTEXT_METHODS_KEY);
		methodList.add(method);
	}

	protected void addGetMethod(
		JavaClass javaClass,
		Map context,
		String propName,
		Map fieldMapping,
		Map constantMapping)
	{
		Method templateGetMethod = (Method) context.get(CONTEXT_GET_METHOD_KEY);
		InstructionList getTemplateInstructionList =
			new InstructionList(templateGetMethod.getCode().getCode());

		InstructionList getMethodInstructionList =
			getTemplateInstructionList.copy();

		//System.out.println("get method instructions:");            
		//printInstructions(getMethodInstructionList.getStart());

		modifyTemplateInstructions(
			javaClass,
			getMethodInstructionList,
			fieldMapping,
			constantMapping);

		//System.out.println("new method instructions:");            
		//printInstructions(getMethodInstructionList.getStart());

		String getMethodName = getGetMethodName(propName);
		int getMethodNameId = addUtfConstant(context, getMethodName);

		Method propertyGetMethod =
			templateGetMethod.copy(javaClass.getConstantPool());
		propertyGetMethod.setNameIndex(getMethodNameId);
		propertyGetMethod.getCode().setCode(
			getMethodInstructionList.getByteCode());

		addMethod(context, propertyGetMethod);
	}

	protected void modifyTemplateInstructions(
		JavaClass javaClass,
		InstructionList instList,
		Map fieldMapping,
		Map constantMapping)
	{
		ConstantPool constantPool = javaClass.getConstantPool();

		InstructionHandle handle = instList.getStart();
		while (handle != null)
		{
			Instruction inst = handle.getInstruction();

			if (inst instanceof FieldInstruction)
			{
				FieldInstruction fieldInst = (FieldInstruction) inst;
				int fieldId = fieldInst.getIndex();
				ConstantFieldref fieldConst =
					(ConstantFieldref) constantPool.getConstant(fieldId);
				ConstantNameAndType nameAndTypeConst =
					(ConstantNameAndType) constantPool.getConstant(
						fieldConst.getNameAndTypeIndex());

				Integer newFieldId =
					(Integer) fieldMapping.get(
						new Integer(nameAndTypeConst.getNameIndex()));
				if (newFieldId != null)
					fieldInst.setIndex(newFieldId.intValue());
			}

			if (inst instanceof LDC)
			{
				LDC ldcInstruction = (LDC) inst;
				Integer newConstId =
					(Integer) constantMapping.get(
						new Integer(ldcInstruction.getIndex()));
				if (newConstId != null)
					ldcInstruction.setIndex(newConstId.intValue());
			}

			handle = handle.getNext();
		}

	}

	protected void printInstructions(InstructionHandle handle)
	{
		while (handle != null)
		{
			Instruction inst = handle.getInstruction();
			System.out.println(inst);
			handle = handle.getNext();
		}
	}

	public static void main(String[] arrArgs)
	{
		try
		{
			IResourceResolver res = new DefaultResourceResolver();
			ComponentClassLoader loader = new ComponentClassLoader(res);

			String resPath =
				"net/sf/tapestry/contrib/table/components/TableView.jwc";
			SpecificationParser parser = new SpecificationParser();
			ComponentSpecification spec =
				parser.parseComponentSpecification(
					res.getResource(resPath).openStream(),
					resPath);

			//Class newClass = loader.findComponentClass(spec);
			//TableView testObj = (TableView) newClass.newInstance();

			System.out.println("Done");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
