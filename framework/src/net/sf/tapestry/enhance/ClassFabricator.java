package net.sf.tapestry.enhance;

import org.apache.bcel.Constants;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.*;

/**
 *  Class used to simplify the generation of new classes as
 *  a wrapper around BCEL.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class ClassFabricator
{
    private ClassGen _classGen;
    private InstructionFactory _instructionFactory;

    /**
     *  Creates a public final class.
     * 
     **/

    public ClassFabricator(String className, String parentClassName)
    {
        this(className, parentClassName, Constants.ACC_FINAL | Constants.ACC_PUBLIC);
    }

    /**
     *  General constructor, creates a new instance using the supplied
     *  parameters.  ACC_SUPER is automatically added to the access flags.
     * 
     **/

    public ClassFabricator(String className, String parentClassName, int accessFlags)
    {
        _classGen =
            new ClassGen(
                className,
                parentClassName,
                "<synthetic>",
                accessFlags | Constants.ACC_SUPER,
                null);
    }

	/**
	 *  Creates a new {@link MethodFabricator}.  Invoke
	 *  {@link MethodFabricator#getInstructionList()}
	 *  to obtain the (initially empty) instruction list
	 *  for the method.
	 * 
	 **/
	
    public MethodFabricator createMethod(int accessFlags, Type returnType, String methodName)
    {
        return new MethodFabricator(_classGen, accessFlags, returnType, methodName);
    }

    /**
     *  Creates a default public method that returns void.
     * 
     **/

    public MethodFabricator createMethod(String methodName)
    {
        return createMethod(Constants.ACC_PUBLIC, Type.VOID, methodName);
    }

    /**
     *  Adds a private instance variable of the given type and name.
     * 
     **/

    public void addField(Type type, String name)
    {
        addField(Constants.ACC_PRIVATE, type, name);
    }

    /**
     *  Adds a field with the given access type, type and name.
     * 
     **/

    public void addField(int accessFlags, Type type, String name)
    {
        FieldGen fg = new FieldGen(accessFlags, type, name, _classGen.getConstantPool());

        Field f = fg.getField();

        _classGen.addField(f);
    }

    /**
     *  Adds a public, no-arguments constructor.
     * 
     **/

    public void addDefaultConstructor()
    {
        _classGen.addEmptyConstructor(Constants.ACC_PUBLIC);
    }


	/**
	 *  Adds an interface to the list of interfaces implemented
	 *  by the class.
	 * 
	 **/
	
	public void addInterface(String interfaceName)
	{
		_classGen.addInterface(interfaceName);
	}
	
	public void addInterface(Class interfaceClass)
	{
		addInterface(interfaceClass.getName());
	}
	
	public InstructionFactory getInstructionFactory()
	{
		if (_instructionFactory == null)
			_instructionFactory = new InstructionFactory(_classGen);
		
		return _instructionFactory;
	}

    /**
     *  Invoked very much last, to create the
     *  new {@link org.apache.bcel.classfile.JavaClass} instance.
     * 
     **/

    public JavaClass commit()
    {
        return _classGen.getJavaClass();
    }
    
    /**
     *  Returns the fully qualified class name.
     * 
     **/
    
    public String getClassName()
    {
    	return _classGen.getClassName();
    }
    
    /**
     *  Returns the mutable constant pool.
     * 
     **/
    
    public ConstantPoolGen getConstantPool()
    {
    	return _classGen.getConstantPool();
    }
}
