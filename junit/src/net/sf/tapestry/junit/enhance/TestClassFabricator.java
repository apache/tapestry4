package net.sf.tapestry.junit.enhance;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.DefaultResourceResolver;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.enhance.ClassFabricator;
import net.sf.tapestry.enhance.EnhanceClassLoader;
import net.sf.tapestry.enhance.MethodFabricator;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.html.BasePage;
import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.util.prop.OgnlUtils;
import org.apache.bcel.Constants;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.generic.InstructionConstants;
import org.apache.bcel.generic.InstructionFactory;
import org.apache.bcel.generic.InstructionList;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.Type;

/**
 *  Tests the classes used by the 
 *  {@link net.sf.tapestry.enhance.DefaultComponentClassEnhancer} to
 *  construct new classes.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 **/

public class TestClassFabricator extends TapestryTestCase
{

    public TestClassFabricator(String name)
    {
        super(name);
    }

    /**
     *  Test the ability to create a class that implements a read/write property.
     * 
     **/

    public void testCreateProperty() throws Exception
    {
        String className = "net.sf.tapestry.junit.enhance.PropertyHolder";
        String fieldName = "_name";

        ClassFabricator cf = new ClassFabricator(className, "java.lang.Object");

        InstructionFactory f = cf.getInstructionFactory();

        cf.addDefaultConstructor();
        cf.addField(Type.STRING, fieldName);

        MethodFabricator mf = cf.createMethod(Constants.ACC_PUBLIC, Type.VOID, "setName");
        int nameArgIndex = mf.addArgument(Type.STRING, "name");

        InstructionList il = mf.getInstructionList();

        il.append(f.createThis());
        il.append(f.createLoad(Type.STRING, nameArgIndex));
        il.append(f.createPutField(className, fieldName, Type.STRING));
        il.append(InstructionConstants.RETURN);

        mf.commit();

        mf = cf.createMethod(Constants.ACC_PUBLIC, Type.STRING, "getName");
        il = mf.getInstructionList();

        il.append(f.createThis());
        il.append(f.createGetField(className, fieldName, Type.STRING));
        il.append(f.createReturn(Type.STRING));

        mf.commit();

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        Class holderClass = cl.defineClass(jc);

        Object holder = holderClass.newInstance();

        String value = "abraxis";

        OgnlUtils.set("name", resolver, holder, value);

        Object actual = OgnlUtils.get("name", resolver, holder);

        assertEquals("Holder name property.", value, actual);
    }

    /**
     *  Test ability to create a subclass of an existing class
     *  and implement an interface.
     * 
     **/

    public void testAddInterface() throws Exception
    {
        ClassFabricator cf =
            new ClassFabricator("net.sf.tapestry.junit.enhance.Foobar", BasePage.class.getName());

        cf.addDefaultConstructor();
        cf.addInterface(PageDetachListener.class);

        MethodFabricator mf = cf.createMethod("pageDetached");

        mf.addArgument(new ObjectType(PageEvent.class.getName()), "event");

        InstructionList il = mf.getInstructionList();

        il.append(InstructionConstants.RETURN);

        mf.commit();

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        Class holderClass = cl.defineClass(jc);

        Object instance = holderClass.newInstance();

        assertEquals("Implements interface", true, instance instanceof PageDetachListener);
    }

    public void testFailureAddArgumentsAfterLocals()
    {
        ClassFabricator cf = new ClassFabricator("Foo", "java.lang.Object");

        MethodFabricator mf = cf.createMethod("bar");

        mf.addArgument(Type.STRING, "param1");
        mf.addLocalVariable(Type.BOOLEAN, "local1");

        try
        {
            mf.addArgument(Type.LONG, "param2");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            checkException(
                ex,
                "No more arguments may be added once any local variables are added.");
        }
    }

    public void testCreateFailure()
    {
        ClassFabricator cf =
            new ClassFabricator("net.sf.tapestry.junit.enhance.Invalid", "java.lang.Boolean");

        JavaClass jc = cf.commit();

        IResourceResolver resolver = new DefaultResourceResolver();

        EnhanceClassLoader cl = new EnhanceClassLoader(resolver.getClassLoader());

        try
        {
            cl.defineClass(jc);

            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "Cannot inherit from final class");
        }

    }
}