package net.sf.tapestry.junit.parse;

import junit.framework.AssertionFailedError;

import net.sf.tapestry.junit.TapestryTestCase;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;
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
     * 
     *  Asserts that the exception message contains the
     *  indicated substring.
     * 
     *  @since 2.2 
     * 
     **/

    private void checkException(DocumentParseException ex, String string)
    {
        if (ex.getMessage().indexOf(string) >= 0)
            return;

        throw new AssertionFailedError("Exception " + ex + " does not contain sub-string '" + string + "'.");
    }

    /**
     *  Tests that the parser can handle a 1.2 specification
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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");

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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");
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

            throw new AssertionFailedError("Should not be able to parse document.");
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "Invalid$Extension");
            checkException(ex, "extension name");
        }
    }

}