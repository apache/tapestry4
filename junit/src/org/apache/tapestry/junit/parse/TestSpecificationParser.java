/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.junit.parse;

import java.util.Map;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.BindingSpecification;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.ContainedComponent;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.ListenerBindingSpecification;
import org.apache.tapestry.spec.ParameterSpecification;
import org.apache.tapestry.spec.PropertySpecification;
import org.apache.tapestry.util.xml.DocumentParseException;

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

public class TestSpecificationParser extends TapestryTestCase
{

    public TestSpecificationParser(String name)
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

        checkList(
            "componentAliases",
            new String[] { "FirstComponent", "SecondComponent" },
            spec.getComponentAliases());

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
            checkException(ex, "type");
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

    public void testAttributeAndBody() throws Exception
    {
        try
        {
            parsePage("AttributeAndBody.page");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(
                ex,
                "It is not valid to specify a value for attribute 'value' of <static-binding> and provide a value in the body of the element.");
        }
    }

    /**
     *  Tests the new (in DTD 1.4) value attribute on a configure element.
     * 
     *  @since 2.4
     * 
     **/

    public void testConfigureValue() throws Exception
    {
        ILibrarySpecification spec = parseLib("ConfigureValue.library");

        Map map = (Map) spec.getExtension("map", Map.class);

        assertEquals("flintstone", map.get("fred"));
    }

    /**
     *  Tests the new &lt;listener-binding&gt; element in the 1.4 DTD.
     * 
     *  @since 2.4
     * 
     **/

    public void testListenerBinding() throws Exception
    {
        ComponentSpecification spec = parsePage("ListenerBinding.page");

        ContainedComponent c = spec.getComponent("c");

        ListenerBindingSpecification lbs = (ListenerBindingSpecification) c.getBinding("listener");

        String expectedScript =
            buildExpectedScript(
                new String[] {
                    "if page.isFormInputValid():",
                    "  cycle.page = \"Results\"",
                    "else:",
                    "  page.message = \"Please fix errors before continuing.\";" });

        assertEquals("jython", lbs.getLanguage());
        assertEquals(expectedScript, lbs.getScript());
    }

    private String buildExpectedScript(String[] lines)
    {
        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < lines.length; i++)
        {
            if (i > 0)
                buffer.append("\n");

            buffer.append(lines[i]);
        }

        return buffer.toString();
    }

    /** @since 2.4 **/

    public void testPropertySpecifications() throws Exception
    {
        ComponentSpecification spec = parsePage("PropertySpecifications.page");

        checkList(
            "propertySpecificationNames",
            new String[] { "bool", "init", "persist" },
            spec.getPropertySpecificationNames());

        PropertySpecification ps = spec.getPropertySpecification("bool");
        assertEquals("name", "bool", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());
        assertEquals("type", "boolean", ps.getType());
        assertNull("initialValue", ps.getInitialValue());

        ps = spec.getPropertySpecification("init");
        assertEquals("name", "init", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());
        assertEquals("type", "java.lang.Object", ps.getType());
        assertEquals("initialValue", "pageName", ps.getInitialValue());

        ps = spec.getPropertySpecification("persist");
        assertEquals("name", "persist", ps.getName());
        assertEquals("persistent", true, ps.isPersistent());
        assertEquals("type", "java.lang.Object", ps.getType());
        assertNull("initialValue", ps.getInitialValue());

        ps = spec.getPropertySpecification("unknown");

        assertNull("Unknown PropertySpecification", ps);
    }

    /** @since 2.4 **/

    public void testDuplicatePropertySpecification() throws Exception
    {
        try
        {
            parsePage("DuplicatePropertySpecification.page");

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
            checkException(ex, "already contains property specification for property 'bool'");
        }
    }

    /** @since 2.4 **/

    public void testMissingRequiredExtendedAttribute() throws Exception
    {
        try
        {
            parsePage("MissingRequiredExtendedAttribute.page");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(
                ex,
                "Element <binding> does not specify a value for attribute 'expression', or contain a body value.");
        }
    }
}