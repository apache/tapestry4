package net.sf.tapestry.junit.parse;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 *  Tests the specification parser (which reads page and component
 *  specifications).  Came into being somewhat late, so it just
 *  tests new features for the meantime.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.0.4
 *
 **/

public class SpecificationParserTest extends TapestryTestCase
{

    public SpecificationParserTest(String name)
    {
        super(name);
    }

    /**
     *  Tests that the parser can handle a specification
     *  that includes a &lt;string-binding&gt; element.
     * 
     **/

    public void testStringBinding() throws Exception
    {
        ComponentSpecification spec = parseComponent("TestStringBinding.jwc");

        BindingSpecification bs = spec.getComponent("hello").getBinding("value");

        assertEquals("type", BindingType.STRING, bs.getType());
        assertEquals("key", "label.hello", bs.getValue());
    }

    /**
     *  Test valid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testValidParameterName() throws Exception
    {
        ComponentSpecification spec = parseComponent("ValidParameterName.jwc");

        ParameterSpecification ps = spec.getParameter("valid");

        assertNotNull("Parameter specification.", ps);
    }

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidParameterName() throws Exception
    {
        try
        {
            parseComponent("InvalidParameterName.jwc");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in-valid");
            checkException(ex, "Parameter");
        }
    }

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidComponentId() throws Exception
    {
        try
        {
            parseComponent("InvalidComponentId.jwc");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "component id");
        }
    }

    /**
     *  Test invalid library id in a library specification.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidLibraryId() throws Exception
    {
        try
        {
            parseLib("InvalidLibraryId.library");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "library id");
        }
    }

    /**
     *  Parse a valid library.
     * 
     *  @since 2.2
     * 
     **/

    public void testValidLibrary() throws Exception
    {
        ILibrarySpecification spec = parseLib("ValidLibrary.library");

        checkList("serviceNames", new String[] { "service1", "service2" }, spec.getServiceNames());

        checkList("pageNames", new String[] { "FirstPage", "SecondPage" }, spec.getPageNames());

        checkList("componentAliases", new String[] { "FirstComponent", "SecondComponent" }, spec.getComponentAliases());

        checkList("libraryIds", new String[] { "lib1", "lib2" }, spec.getLibraryIds());
    }

    /**
     *  Test invalid parameter name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidAssetName() throws Exception
    {
        try
        {
            parseComponent("InvalidAssetName.jwc");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in.valid");
            checkException(ex, "asset name");
        }
    }

    /**
     *  Test invalid page name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidPageName() throws Exception
    {
        try
        {
            parseApp("InvalidPageName.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in$valid");
            checkException(ex, "page name");
        }
    }

    /**
     *  Test invalid service name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidServiceName() throws Exception
    {
        try
        {
            parseApp("InvalidServiceName.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "in$valid");
            checkException(ex, "service");
        }
    }

    /**
     *  Test invalid service name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidComponentAlias() throws Exception
    {
        try
        {
            parseApp("InvalidComponentAlias.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Invalid$Component");
            checkException(ex, "alias");
        }
    }

    /**
     *  Test invalid extension name.
     * 
     *  @since 2.2
     * 
     **/

    public void testInvalidExtensionName() throws Exception
    {
        try
        {
            parseApp("InvalidExtensionName.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Invalid$Extension");
            checkException(ex, "extension name");
        }
    }

    /** 
     *  Test case where the document does not have a DOCTYPE
     * 
     *  @since 2.2
     * 
     **/

    public void testMissingDoctype() throws Exception
    {
        try
        {
            parseApp("MissingDoctype.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Valid documents must have a <!DOCTYPE");
        }
    }

    /**
     *  Test case where the public id of the document is not known.
     * 
     **/

    public void testInvalidPublicId() throws Exception
    {
        try
        {
            parseApp("InvalidPublicId.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "has an unexpected public id");
        }
    }

    /**
     *  Test an an application specification can omit
     *  the name and engine-class attributes.
     * 
     *  @since 2.4
     * 
     **/

    public void testNulledApplication() throws Exception
    {
        IApplicationSpecification spec = parseApp("NulledApplication.application");

        assertNull(spec.getEngineClassName());
        assertNull(spec.getName());
    }

    /**
     *  Test new DTD 1.4 syntax for declaring components.
     * 
     *  @since 2.4
     * 
     **/

    public void testComponentType() throws Exception
    {
        IApplicationSpecification spec = parseApp("ComponentType.application");

        assertEquals("/path/Fred.jwc", spec.getComponentSpecificationPath("Fred"));
    }

    /**
     *  Test omitting the class name from a component specification
     *  (new, in DTD 1.4).
     * 
     **/

    public void testNulledComponent() throws Exception
    {
        ComponentSpecification spec = parseComponent("NulledComponent.jwc");

        assertNull(spec.getComponentClassName());
    }

    /**
     *  Test omitting the class name from a component specification
     *  (new, in DTD 1.4).
     * 
     **/

    public void testNulledPage() throws Exception
    {
        ComponentSpecification spec = parsePage("NulledPage.page");

        assertNull(spec.getComponentClassName());
    }

    /**
     *  Test the value attribute for the property element
     *  (which is new in DTD 1.4).
     * 
     *  @since 2.4
     * 
     **/

    public void testPropertyValue() throws Exception
    {
        ComponentSpecification spec = parsePage("PropertyValue.page");

        assertEquals("rubble", spec.getProperty("barney"));
        assertEquals("flintstone", spec.getProperty("wilma"));
    }

    /**
     *  Tests the new (in DTD 1.4) value attribute on static-binding
     *  element.
     * 
     *  @since 2.4
     * 
     **/

    public void testStaticBindingValue() throws Exception
    {
        ComponentSpecification spec = parsePage("StaticBindingValue.page");

        ContainedComponent c = spec.getComponent("c");

        assertEquals("flintstone", c.getBinding("fred").getValue());
        assertEquals("rubble", c.getBinding("barney").getValue());
        assertEquals("hudson", c.getBinding("rock").getValue());
    }
}