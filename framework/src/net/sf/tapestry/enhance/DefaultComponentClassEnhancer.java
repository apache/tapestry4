package net.sf.tapestry.enhance;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.IComponentClassEnhancer;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.PropertySpecification;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.BasicType;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Default implementation of {@link net.sf.tapestry.IComponentClassEnhancer}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class DefaultComponentClassEnhancer implements IComponentClassEnhancer
{
    private static final Log LOG = LogFactory.getLog(DefaultComponentClassEnhancer.class);

    /**
     *  UID used to generate new class names.
     * 
     **/

    private int _uid = 0;

    /**
     *  Map of Class, keyed on ComponentSpecification.
     * 
     **/

    private Map _cachedClasses = new HashMap();
    private IResourceResolver _resolver;
    private EnhanceClassLoader _classLoader;

    /**
     *  Map of type (as Class), keyed on type name. 
     * 
     **/

    private Map _typeMap = new HashMap();

    /**
     *  Map of type (as Type), keyed on type name.
     * 
     **/

    private Map _objectTypeMap = new HashMap();

    public DefaultComponentClassEnhancer(IResourceResolver resolver)
    {
        _resolver = resolver;
        _classLoader = new EnhanceClassLoader(resolver.getClassLoader());

        initializeTypeMaps();
    }

    public synchronized void reset()
    {
        _cachedClasses.clear();
        initializeTypeMaps();
    }

    private void initializeTypeMaps()
    {
        _typeMap.clear();
        _objectTypeMap.clear();

        recordType("boolean", boolean.class, Type.BOOLEAN);
        recordType("short", short.class, Type.SHORT);
        recordType("int", int.class, Type.INT);
        recordType("long", long.class, Type.LONG);
        recordType("float", float.class, Type.FLOAT);
        recordType("double", double.class, Type.DOUBLE);
        recordType("char", char.class, Type.CHAR);
        recordType("byte", byte.class, Type.BYTE);
    }

    private void recordType(String name, Class type, Type objectType)
    {
        _typeMap.put(name, type);
        _objectTypeMap.put(name, objectType);
    }

    public synchronized Class getEnhancedClass(
        ComponentSpecification specification,
        String className)
    {
        Class result = (Class) _cachedClasses.get(specification);

        if (result == null)
        {
            result = constructComponentClass(specification, className);

            _cachedClasses.put(specification, result);
        }

        return result;
    }

    /**
     *  Returns the class to be used for the component, which is either
     *  the class with the given name, or an enhanced subclass.
     * 
     **/

    protected Class constructComponentClass(ComponentSpecification specification, String className)
    {
        Class result = _resolver.findClass(className);

        if (needsEnhancement(specification))
            result = createEnhancedSubclass(result, specification);

        return result;
    }

    /**
     *  Examines the specification, identifies if any enhancements will be needed.
     *  This implementation looks for the presence of any
     *  {@link net.sf.tapestry.spec.PropertySpecification}s.
     * 
     **/

    protected boolean needsEnhancement(ComponentSpecification specification)
    {
        return !specification.getPropertySpecificationNames().isEmpty();
    }

    private Class createEnhancedSubclass(Class startClass, ComponentSpecification specification)
    {
        if (LOG.isDebugEnabled())
            LOG.debug(
                "Enhancing class "
                    + startClass.getName()
                    + " for "
                    + specification.getSpecificationLocation());

        Class result = null;

        String startClassName = startClass.getName();
        String enhancedName = startClassName + "$Enhance_" + _uid++;

        ClassFabricator cf = new ClassFabricator(enhancedName, startClassName);

        cf.addDefaultConstructor();

        List names = specification.getPropertySpecificationNames();
        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            PropertySpecification ps = specification.getPropertySpecification(name);

            createProperty(cf, startClass, ps);
        }

        JavaClass jc = cf.commit();

        result = _classLoader.defineClass(jc);

        if (LOG.isDebugEnabled())
            LOG.debug("Finished creating enhanced class " + result.getName());

        return result;
    }

    /**
     *  Invoked to create the specified property.  Checks that the superclass provides
     *  either abstract accessors or none at all.  Creates the file, creates 
     *  the accessors, creates initialization code.
     * 
     **/

    protected void createProperty(ClassFabricator cf, Class startClass, PropertySpecification ps)
    {
        String propertyName = ps.getName();

        if (LOG.isDebugEnabled())
            LOG.debug("Establishing property " + propertyName);

        String type = ps.getType();
        Class propertyType = convertPropertyType(type);

        checkAccessors(startClass, propertyName, propertyType);

        String fieldName = "_$" + propertyName;

        Type fieldType = getObjectType(type);

        cf.addField(fieldType, fieldName);

        createAccessor(cf, fieldType, fieldName, propertyName);
        createMutator(cf, fieldType, fieldName, propertyName, ps.isPersistent());
    }

    protected void createAccessor(
        ClassFabricator cf,
        Type fieldType,
        String fieldName,
        String propertyName)
    {
        String methodName = buildMethodName("get", propertyName);

        MethodFabricator mf = cf.createMethod(Constants.ACC_PUBLIC, fieldType, methodName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = cf.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createGetField(cf.getClassName(), fieldName, fieldType));
        il.append(factory.createReturn(fieldType));

        mf.commit();
    }

    protected void createMutator(
        ClassFabricator cf,
        Type fieldType,
        String fieldName,
        String propertyName,
        boolean isPersistent)
    {
        String methodName = buildMethodName("set", propertyName);

        MethodFabricator mf = cf.createMethod(methodName);
        mf.addArgument(fieldType, propertyName);

        InstructionList il = mf.getInstructionList();
        InstructionFactory factory = cf.getInstructionFactory();

        il.append(factory.createThis());
        il.append(factory.createLoad(fieldType, 1));
        il.append(factory.createPutField(cf.getClassName(), fieldName, fieldType));

        // Persistent properties must invoke fireObservedChange

        if (isPersistent)
        {
            il.append(factory.createThis());
            il.append(new PUSH(cf.getConstantPool(), propertyName));
            il.append(factory.createLoad(fieldType, 1));

            Type argumentType = convertToArgumentType(fieldType);

            il.append(
                factory.createInvoke(
                    cf.getClassName(),
                    "fireObservedChange",
                    Type.VOID,
                    new Type[] { Type.STRING, argumentType },
                    Constants.INVOKEVIRTUAL));
        }

        il.append(InstructionConstants.RETURN);

        mf.commit();
    }

    /**
     *  Given an arbitrary type, figures out the correct
     *  argument type (for fireObservedChange()) to use.
     * 
     **/

    protected Type convertToArgumentType(Type type)
    {
        if (type instanceof BasicType)
            return type;

        return Type.OBJECT;
    }

    protected String buildMethodName(String prefix, String propertyName)
    {
        StringBuffer result = new StringBuffer(prefix);

        char ch = propertyName.charAt(0);

        result.append(Character.toUpperCase(ch));

        result.append(propertyName.substring(1));

        return result.toString();
    }

    protected Class convertPropertyType(String type)
    {
        Class result = (Class) _typeMap.get(type);

        if (result == null)
        {
            result = _resolver.findClass(type);

            _typeMap.put(type, result);
        }

        return result;
    }

    protected Type getObjectType(String type)
    {
        Type result = (Type) _objectTypeMap.get(type);

        if (result == null)
        {
            result = new ObjectType(type);
            _objectTypeMap.put(type, result);
        }

        return result;
    }

    protected void checkAccessors(Class startClass, String propertyName, Class propertyType)
    {
        BeanInfo info = null;

        try
        {
            info = Introspector.getBeanInfo(startClass);

        }
        catch (IntrospectionException ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "DefaultComponentClassEnhancer.unable-to-introspect-class",
                    startClass.getName()),
                ex);
        }

        PropertyDescriptor[] descriptors = info.getPropertyDescriptors();

        for (int i = 0; i < descriptors.length; i++)
        {
            PropertyDescriptor d = descriptors[i];

            if (!d.getName().equals(propertyName))
                continue;

            if (!d.getPropertyType().equals(propertyType))
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "DefaultComponentClassEnhancer.property-type-mismatch",
                        new Object[] {
                            startClass.getName(),
                            propertyName,
                            d.getPropertyType().getName(),
                            propertyType.getName()}));

            Method m = d.getReadMethod();

            if (m != null && !Modifier.isAbstract(m.getModifiers()))
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "DefaultComponentClassEnhancer.non-abstract-read",
                        m.getDeclaringClass().getName(),
                        propertyName));

            m = d.getWriteMethod();

            if (m != null && !Modifier.isAbstract(m.getModifiers()))
                throw new ApplicationRuntimeException(
                    Tapestry.getString(
                        "DefaultComponentClassEnhancer.non-abstract-write",
                        m.getDeclaringClass().getName(),
                        propertyName));
            return;
        }
    }
}