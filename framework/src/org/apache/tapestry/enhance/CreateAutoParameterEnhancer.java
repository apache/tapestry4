package org.apache.tapestry.enhance;

import org.apache.bcel.Constants;
import org.apache.bcel.generic.BranchInstruction;
import org.apache.bcel.generic.GOTO;
import org.apache.bcel.generic.IFNONNULL;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionHandle;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.PUSH;
import org.apache.bcel.generic.ReferenceType;
import org.apache.bcel.generic.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.Location;
import org.apache.tapestry.Tapestry;

/**
 *  Creates a synthetic property for a
 *  {@link org.apache.tapestry.spec.Direction#AUTO}
 *  parameter.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class CreateAutoParameterEnhancer implements IEnhancer
{
    private static final Log LOG = LogFactory.getLog(CreateAutoParameterEnhancer.class);

    private ComponentClassFactory _factory;
    private String _propertyName;
    private String _parameterName;
    private Type _type;
    private String _typeClassName;
    private String _readMethodName;
    private Location _location;

    private Type _bindingType;
    private Type _classType;

    public CreateAutoParameterEnhancer(
        ComponentClassFactory factory,
        String propertyName,
        String parameterName,
        Type type,
        String typeClassName,
        String readMethodName,
        Location location)
    {
        _factory = factory;
        _propertyName = propertyName;
        _parameterName = parameterName;
        _type = type;
        _typeClassName = typeClassName;
        _readMethodName = readMethodName;
        _location = location;

        _bindingType = factory.getObjectType(IBinding.class.getName());
        _classType = factory.getObjectType(Class.class.getName());
    }

    public void performEnhancement(ComponentClassFactory factory)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Creating auto property: " + _propertyName);

        ClassFabricator cf = factory.getClassFabricator();

        String readBindingMethodName =
            _factory.buildMethodName(
                "get",
                _parameterName + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX);

        createReadMethod(cf, readBindingMethodName);
        createWriteMethod(cf, readBindingMethodName);
    }

    private String createParameterTypeField(ClassFabricator cf)
    {
        String fieldName = "$type$" + _parameterName;

        cf.addField(Constants.ACC_PRIVATE | Constants.ACC_STATIC, _classType, fieldName);

        MethodFabricator mf = cf.getStaticInitializerMethod();
        InstructionFactory factory = cf.getInstructionFactory();
        String className = cf.getClassName();
        Type throwableType = _factory.getObjectType(Throwable.class.getName());

        mf.append(factory.createGetStatic(className, fieldName, _classType));

        BranchInstruction ifNotNullBI = new IFNONNULL(null);

        mf.append(ifNotNullBI);

        // Invoke Class.forName and store ther result.
        // Concern: will the class be visible to the right class loader?
        // May need to use alternate forName() and pass Thread's context class loader.

		mf.append(new PUSH(cf.getConstantPool(), _typeClassName));
        InstructionHandle tryStart =
            mf.append(
                factory.createInvoke(
                    "java.lang.Class",
                    "forName",
                    _classType,
                    new Type[] { Type.STRING },
                    Constants.INVOKESTATIC));
        mf.append(factory.createPutStatic(className, fieldName, _classType));

        GOTO jumpOut = new GOTO(null);

        InstructionHandle tryEnd = mf.append(jumpOut);

        String exceptionClassName = ApplicationRuntimeException.class.getName();

        InstructionHandle catchHandle = mf.append(factory.createNew(exceptionClassName));

		// This stuff can make my head spin, so let's map it out a little.
		// CCE = ClassCastException, ARE = ApplicationRuntimeException

		// Stack: CCE, ARE --> ARE, CCE, ARE
		
		mf.append(InstructionConstants.DUP_X1);
		
		// Stack: ARE, CCE, ARE -> ARE, ARE, CCE
		
        mf.append(InstructionConstants.SWAP);

        mf.append(
            factory.createInvoke(
                exceptionClassName,
                Constants.CONSTRUCTOR_NAME,
                Type.VOID,
                new Type[] { throwableType },
                Constants.INVOKESPECIAL));

        mf.append(InstructionConstants.ATHROW);

        mf.addExceptionHandler(
            tryStart,
            tryEnd,
            catchHandle,
            new ObjectType(ClassNotFoundException.class.getName()));

        InstructionHandle end = mf.append(InstructionConstants.NOP);

        ifNotNullBI.setTarget(end);
        jumpOut.setTarget(end);

        return fieldName;
    }

    private void createReadMethod(ClassFabricator cf, String readBindingMethodName)
    {
        String methodName =
            _readMethodName == null
                ? _factory.buildMethodName("get", _propertyName)
                : _readMethodName;

        if (LOG.isDebugEnabled())
            LOG.debug("Creating method: " + methodName);

		Type[] noArgs = new Type[] { };

        MethodFabricator mf = cf.createMethod(Constants.ACC_PUBLIC, _type, methodName);

        InstructionFactory factory = cf.getInstructionFactory();

        mf.append(factory.createThis());
        mf.append(
            factory.createInvoke(
                cf.getClassName(),
                readBindingMethodName,
                _bindingType,
                noArgs,
                Constants.INVOKEVIRTUAL));

        String accessMethodName = null;

        if (isBoolean(_type))
            accessMethodName = "getBoolean";
        else
            if (isInt(_type))
                accessMethodName = "getInt";
            else
                if (isDouble(_type))
                    accessMethodName = "getDouble";
                else
                    if (isString(_type))
                        accessMethodName = "getString";

        if (accessMethodName != null)
        {
            // The binding object is on top of the stack
            mf.append(
                factory.createInvoke(
                    IBinding.class.getName(),
                    accessMethodName,
                    _type,
                    noArgs,
                    Constants.INVOKEINTERFACE));
        }
        else
        {
            // Type is either an object type or an array type

            // To invoke getObject(parameterName, type) we need the type.
            // We mimic what Java compiler does; create a private static field
            // to store the type, and add a static initializer that invokes Class.forName.

            String fieldName = createParameterTypeField(cf);

            mf.append(new PUSH(cf.getConstantPool(), _parameterName));
            mf.append(factory.createGetStatic(cf.getClassName(), fieldName, _classType));

            mf.append(
                factory.createInvoke(
                    IBinding.class.getName(),
                    "getObject",
                    Type.OBJECT,
                    new Type[] { Type.STRING, _classType },
                    Constants.INVOKEINTERFACE));

            // ReferenceType is superclass to ObjectType and ArrayType

            mf.append(factory.createCheckCast((ReferenceType) _type));
        }

        mf.append(factory.createReturn(_type));

        mf.commit();
    }

    private void createWriteMethod(ClassFabricator cf, String readBindingMethodName)
    {
        String methodName = _factory.buildMethodName("set", _propertyName);

        if (LOG.isDebugEnabled())
            LOG.debug("Creating method: " + methodName);

        MethodFabricator mf = cf.createMethod(methodName);
        mf.addArgument(_type, _propertyName);

        InstructionFactory factory = cf.getInstructionFactory();

        String updateMethodName = null;
        Type argumentType = _type;

        if (isBoolean(_type))
            updateMethodName = "setBoolean";
        else
            if (isInt(_type))
                updateMethodName = "setInt";
            else
                if (isDouble(_type))
                    updateMethodName = "setDouble";
                else
                    if (isString(_type))
                        updateMethodName = "setString";
                    else
                    {
                        updateMethodName = "setObject";
                        argumentType = Type.OBJECT;
                    }

        // Get the binding

        mf.append(factory.createThis());
        mf.append(
            factory.createInvoke(
                cf.getClassName(),
                readBindingMethodName,
                _bindingType,
                new Type[] { },
                Constants.INVOKEVIRTUAL));

        // Push the parameter value (remember,
        // parameter 0 is "this")

        mf.append(factory.createLoad(_type, 1));

        // Invoke the update method

        mf.append(
            factory.createInvoke(
                IBinding.class.getName(),
                updateMethodName,
                Type.VOID,
                new Type[] { argumentType },
                Constants.INVOKEINTERFACE));

        mf.append(InstructionConstants.RETURN);
        mf.commit();
    }

    private boolean isBoolean(Type t)
    {
        return t.getType() == Constants.T_BOOLEAN;
    }

    private boolean isDouble(Type t)
    {
        return t.getType() == Constants.T_DOUBLE;
    }

    private boolean isInt(Type t)
    {
        return t.getType() == Constants.T_INT;
    }

    private boolean isString(Type t)
    {
        return Type.STRING.equals(t);
    }
}
