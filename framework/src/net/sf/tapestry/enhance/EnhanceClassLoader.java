package net.sf.tapestry.enhance;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.Tapestry;

import org.apache.bcel.classfile.JavaClass;

/**
 *  A class loader that can be used to create new classes 
 *  as needed.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class EnhanceClassLoader extends ClassLoader
{

    public EnhanceClassLoader(ClassLoader parentClassLoader)
    {
        super(parentClassLoader);
    }

    /**
     *  Defines the new class.
     * 
     *  @throws ApplicationRuntimeException if defining the class fails.
     * 
     **/

    public Class defineClass(JavaClass enhancedClass)
    {
        byte[] byteCode = enhancedClass.getBytes();

        try
        {
            return defineClass(enhancedClass.getClassName(), byteCode, 0, byteCode.length);
        }
        catch (Throwable ex)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString(
                    "EnhancedClassLoader.unable-to-define-class",
                    enhancedClass,
                    ex.getMessage()),
                ex);
        }
    }
}
