package net.sf.tapestry.junit.spec;

import net.sf.tapestry.IMonitor;
import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.ExtensionSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;

/**
 *  Tests related to {@link net.sf.tapestry.spec.ApplicationSpecification}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestApplicationSpecification extends TapestryTestCase
{

    public TestApplicationSpecification(String name)
    {
        super(name);
    }

    public void testBasicExtension() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        TestBean extension = (TestBean) spec.getExtension("testBean");

        assertEquals("booleanProperty", true, extension.getBooleanProperty());
        assertEquals("intProperty", 18, extension.getIntProperty());
        assertEquals("longProperty", 383838L, extension.getLongProperty());
        assertEquals("doubleProperty", -3.14, extension.getDoubleProperty(), 0.0);
        assertEquals("stringProperty", "Tapestry: Java Web Components", extension.getStringProperty());
    }

    public void testExtensionType() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        TestBean extension = (TestBean) spec.getExtension("testBean", Object.class);

        assertNotNull(extension);
    }

    public void testExtensionTypeFailClass() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", Number.class);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            checkException(ex, "does not inherit from class java.lang.Number");
        }

    }

    public void testExtensionTypeFailInterface() throws Exception
    {
        IApplicationSpecification spec = parseApp("BasicExtension.application");

        try
        {
            spec.getExtension("testBean", IMonitor.class);
            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            checkException(ex, "does not implement interface net.sf.tapestry.IMonitor");
        }

    }

    public void testExtensionProperty() throws Exception
    {
        IApplicationSpecification a = parseApp("ExtensionProperty.application");

        ExtensionSpecification e = a.getExtensionSpecification("testBean");

        assertEquals("Property fred.", "flintstone", e.getProperty("fred"));
    }

    public void testImmediateExtension() throws Exception
    {
        assertEquals("instanceCount", 0, ImmediateExtension.getInstanceCount());

        parseApp("ImmediateExtension.application");

        assertEquals("instanceCount", 1, ImmediateExtension.getInstanceCount());
    }
}
