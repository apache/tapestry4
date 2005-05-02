// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.junit.parse;

import java.util.List;

import org.apache.hivemind.Locatable;
import org.apache.tapestry.bean.BindingBeanInitializer;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IAssetSpecification;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IBindingSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.spec.IExtensionSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.IListenerBindingSpecification;
import org.apache.tapestry.spec.IParameterSpecification;
import org.apache.tapestry.spec.IPropertySpecification;
import org.apache.tapestry.spec.InjectSpecification;
import org.apache.tapestry.spec.InjectStateSpecification;
import org.apache.tapestry.spec.ListenerBindingSpecification;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 * Tests the specification parser (which reads page and component specifications). Came into being
 * somewhat late, so it just tests new features for the meantime.
 * 
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class TestSpecificationParser extends TapestryTestCase
{

    private void checkLine(Locatable locatable, int line)
    {
        assertEquals("Line", line, locatable.getLocation().getLineNumber());
    }

    /**
     * Test new (in 3.0) &lt;message-binding&gt; element.
     */

    public void tesMessageBinding() throws Exception
    {
        IComponentSpecification spec = parseComponent("TestMessageBinding.jwc");

        IBindingSpecification bs = spec.getComponent("hello").getBinding("value");

        assertEquals("type", BindingType.PREFIXED, bs.getType());
        assertEquals("key", "message:label.hello", bs.getValue());

        checkLine(bs, 25);
    }

    /**
     * Tests the new style &lt;binding&gt; element in 4.0 DTD.
     */

    public void testBinding40() throws Exception
    {
        IComponentSpecification spec = parseComponent("Binding40.jwc");
        IContainedComponent cc = spec.getComponent("component");

        IBindingSpecification bs = cc.getBinding("simple");

        assertEquals(BindingType.PREFIXED, bs.getType());
        assertEquals("message:some-key", bs.getValue());

        bs = cc.getBinding("enclosed");

        assertEquals(BindingType.PREFIXED, bs.getType());
        assertEquals("ognl:zip.zap.zoop", bs.getValue());
    }

    /**
     * Test valid parameter name.
     * 
     * @since 2.2
     */

    public void testValidParameterName() throws Exception
    {
        IComponentSpecification spec = parseComponent("ValidParameterName.jwc");

        IParameterSpecification ps = spec.getParameter("valid");

        assertNotNull("Parameter specification.", ps);
        checkLine(ps, 24);
    }

    /**
     * Test invalid parameter name.
     * 
     * @since 2.2
     */

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
     * Test invalid component id.
     * 
     * @since 2.2
     */

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
     * Test invalid library id in a library specification.
     * 
     * @since 2.2
     */

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
     * Parse a valid library.
     * 
     * @since 2.2
     */

    public void testValidLibrary() throws Exception
    {
        ILibrarySpecification spec = parseLib("ValidLibrary.library");

        checkLine(spec, 24);

        checkList("pageNames", new String[]
        { "FirstPage", "SecondPage" }, spec.getPageNames());

        checkList("componentAliases", new String[]
        { "FirstComponent", "SecondComponent" }, spec.getComponentTypes());

        checkList("libraryIds", new String[]
        { "lib1", "lib2" }, spec.getLibraryIds());

        assertNotNull(spec.getSpecificationLocation());
    }

    /**
     * Test invalid parameter name.
     * 
     * @since 2.2
     */

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
     * Test invalid page name.
     * 
     * @since 2.2
     */

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
     * Test invalid component type ("alias" in older parlance).
     * 
     * @since 2.2
     */

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
     * Test invalid extension name.
     * 
     * @since 2.2
     */

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
     * Test case where the document does not have a DOCTYPE
     * 
     * @since 2.2
     */

    public void testMissingDoctype() throws Exception
    {
        try
        {
            parseApp("MissingDoctype.application");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            // XML parsers tend to generate different exception messages,
            // so make the condition as unspecific as possible
            checkException(ex, "DOCTYPE");
        }
    }

    /**
     * Test case where the public id of the document is not known.
     */

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
     * Test an an application specification can omit the name and engine-class attributes.
     * 
     * @since 3.0
     */

    public void testNulledApplication() throws Exception
    {
        IApplicationSpecification spec = parseApp("NulledApplication.application");

        assertNull(spec.getEngineClassName());
        assertNull(spec.getName());
        checkLine(spec, 25);

        assertNotNull(spec.getSpecificationLocation());
    }

    /**
     * Test new DTD 1.4 syntax for declaring components.
     * 
     * @since 3.0
     */

    public void testComponentType() throws Exception
    {
        IApplicationSpecification spec = parseApp("ComponentType.application");

        assertEquals("/path/Fred.jwc", spec.getComponentSpecificationPath("Fred"));
    }

    /**
     * Test omitting the class name from a component specification (new, in DTD 1.4).
     */

    public void testNulledComponent() throws Exception
    {
        IComponentSpecification spec = parseComponent("NulledComponent.jwc");

        assertNull(spec.getComponentClassName());
        checkLine(spec, 22);

        assertEquals(false, spec.isPageSpecification());
        assertNotNull(spec.getSpecificationLocation());
    }

    /**
     * Test omitting the class name from a component specification (new, in DTD 1.4).
     */

    public void testNulledPage() throws Exception
    {
        IComponentSpecification spec = parsePage("NulledPage.page");

        assertNull(spec.getComponentClassName());
        checkLine(spec, 22);

        assertEquals(true, spec.isPageSpecification());
        assertNotNull(spec.getSpecificationLocation());
    }

    /**
     * Test the value attribute for the property element (which is new in DTD 1.4).
     * 
     * @since 3.0
     */

    public void testPropertyValue() throws Exception
    {
        IComponentSpecification spec = parsePage("PropertyValue.page");

        checkLine(spec, 22);

        assertEquals("rubble", spec.getProperty("barney"));
        assertEquals("flintstone", spec.getProperty("wilma"));
    }

    /**
     * Tests the new (in DTD 1.4) value attribute on static-binding element.
     * 
     * @since 3.0
     */

    public void testStaticBindingValue() throws Exception
    {
        IComponentSpecification spec = parsePage("StaticBindingValue.page");

        checkLine(spec, 22);

        IContainedComponent c = spec.getComponent("c");

        checkLine(c, 24);

        IBindingSpecification b = c.getBinding("fred");
        checkLine(b, 25);

        assertEquals("literal:flintstone", b.getValue());

        b = c.getBinding("barney");
        checkLine(b, 26);

        assertEquals("literal:rubble", b.getValue());

        b = c.getBinding("rock");
        checkLine(b, 27);
        assertEquals("literal:hudson", b.getValue());
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
     * Tests the new (in DTD 1.4) value attribute on a configure element.
     * 
     * @since 3.0
     */

    public void testConfigureValue() throws Exception
    {
        ILibrarySpecification spec = parseLib("ConfigureValue.library");

        checkLine(spec, 22);
        checkLine(spec.getExtensionSpecification("bedrock"), 24);

        Bedrock bedrock = (Bedrock) spec.getExtension("bedrock", Bedrock.class);

        assertEquals("flintstone", bedrock.getFred());
    }

    /**
     * Tests the new &lt;listener-binding&gt; element in the 1.4 DTD.
     * 
     * @since 3.0
     */

    public void testListenerBinding() throws Exception
    {
        IComponentSpecification spec = parsePage("ListenerBinding.page");

        checkLine(spec, 22);
        IContainedComponent c = spec.getComponent("c");

        checkLine(c, 24);

        IListenerBindingSpecification lbs = (ListenerBindingSpecification) c.getBinding("listener");

        checkLine(lbs, 25);

        String expectedScript = buildExpectedScript(new String[]
        { "if page.isFormInputValid():", "  cycle.page = \"Results\"", "else:",
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

    /** @since 3.0 * */

    public void testPropertySpecifications() throws Exception
    {
        IComponentSpecification spec = parsePage("PropertySpecifications.page");

        checkList("propertySpecificationNames", new String[]
        { "bool", "init", "longInitialValue", "persist" }, spec.getPropertySpecificationNames());

        IPropertySpecification ps = spec.getPropertySpecification("bool");
        assertEquals("name", "bool", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());
        assertEquals("type", "boolean", ps.getType());
        assertNull("initialValue", ps.getInitialValue());
        checkLine(ps, 24);

        ps = spec.getPropertySpecification("init");
        assertEquals("name", "init", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());
        assertNull("type", ps.getType());

        // Starting with release 4.0, the initial value is a binding reference
        // with an appropriate prefix. In 3.0 it was always an OGNL expression.

        assertEquals("initialValue", "ognl:pageName", ps.getInitialValue());
        checkLine(ps, 26);

        ps = spec.getPropertySpecification("persist");
        assertEquals("name", "persist", ps.getName());
        assertEquals("persistent", true, ps.isPersistent());
        assertNull("type", ps.getType());
        assertNull("initialValue", ps.getInitialValue());
        checkLine(ps, 25);

        ps = spec.getPropertySpecification("longInitialValue");
        assertEquals("ognl:long.initial.value", ps.getInitialValue());

        ps = spec.getPropertySpecification("unknown");

        assertNull("Unknown PropertySpecification", ps);
    }

    /** @since 3.0 * */

    public void testDuplicatePropertySpecification() throws Exception
    {
        try
        {
            parsePage("DuplicatePropertySpecification.page");

            unreachable();
        }
        catch (DocumentParseException ex)
        {
            checkException(ex, "already contains property specification for property 'bool'");
        }
    }

    /** @since 3.0 * */

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

    /** @since 3.0 * */

    public void testMessageBeanInitializer() throws Exception
    {
        IComponentSpecification spec = parsePage("MessageBeanInitializer.page");

        IBeanSpecification bs = spec.getBeanSpecification("fred");
        checkLine(bs, 24);
        BindingBeanInitializer i = (BindingBeanInitializer) bs.getInitializers().get(0);

        assertEquals("barney", i.getPropertyName());
        assertEquals("message:rubble", i.getBindingReference());
        checkLine(i, 25);
    }

    /**
     * Tests the DTD 3.0 <set-property>element
     * 
     * @since 4.0
     */

    public void testExpressionBeanInitializer() throws Exception
    {
        IComponentSpecification spec = parsePage("ExpressionBeanInitializer_3_0.page");

        IBeanSpecification bs = spec.getBeanSpecification("zebean");

        BindingBeanInitializer i = (BindingBeanInitializer) bs.getInitializers().get(0);

        assertEquals("barney", i.getPropertyName());
        assertEquals("ognl:rubble", i.getBindingReference());

        i = (BindingBeanInitializer) bs.getInitializers().get(1);

        assertEquals("fred", i.getPropertyName());
        assertEquals("ognl:flintstone", i.getBindingReference());
    }

    /** @since 4.0 */

    public void testBeanSet() throws Exception
    {
        IComponentSpecification spec = parsePage("BeanSet.page");
        IBeanSpecification bs = spec.getBeanSpecification("target");

        BindingBeanInitializer i = (BindingBeanInitializer) bs.getInitializers().get(0);

        assertEquals("literal", i.getPropertyName());
        assertEquals("literal-string", i.getBindingReference());

    }

    public void testInheritInformalParameters() throws Exception
    {
        IComponentSpecification spec = parseComponent("TestInheritInformal.jwc");

        IContainedComponent border = spec.getComponent("border");
        assertEquals(border.getInheritInformalParameters(), false);

        IContainedComponent textField = spec.getComponent("textField");
        assertEquals(textField.getInheritInformalParameters(), true);
    }

    /** @since 4.0 */

    public void testConfigureExtension() throws Exception
    {
        IApplicationSpecification spec = parseApp("ConfigureExtension.application");
        IExtensionSpecification es = spec.getExtensionSpecification("my-extension");

        // Note: this is in transition; under 3.0 and earlier, the spec parser was
        // responsible for converting values into object types ... that is now
        // done futher down stream.

        assertEquals("-227", es.getConfiguration().get("long"));
        assertEquals("22.7", es.getConfiguration().get("double"));
        assertEquals("true", es.getConfiguration().get("boolean"));
        assertEquals("An extended string.", es.getConfiguration().get("string"));
    }

    public void testConfigureExtensionProperty() throws Exception
    {
        IApplicationSpecification spec = parseApp("ConfigureExtension.application");
        IExtensionSpecification es = spec.getExtensionSpecification("my-extension");

        assertEquals("my-value", es.getProperty("my-property"));
    }

    /** @since 4.0 */

    public void testComponentProperty() throws Exception
    {
        IComponentSpecification cs = parseComponent("ComponentProperty.jwc");

        IContainedComponent cc = cs.getComponent("body");

        assertEquals("my-value", cc.getProperty("my-property"));
    }

    /** @since 4.0 */

    public void testComponentInjectProperty() throws Exception
    {
        IComponentSpecification cs = parseComponent("ComponentInjectProperty.jwc");

        IContainedComponent cc = cs.getComponent("body");

        assertEquals("myProperty", cc.getPropertyName());

        cc = cs.getComponent("fred");

        assertNull(cc.getPropertyName());
    }

    /** @since 4.0 */

    public void testBeanDescription() throws Exception
    {
        IComponentSpecification cs = parseComponent("BeanDescription.jwc");
        IBeanSpecification bs = cs.getBeanSpecification("mybean");

        assertEquals("Description of mybean.", bs.getDescription());
        assertNotNull(bs.getLocation());
    }

    /** @since 4.0 */

    public void testBeanProperty() throws Exception
    {
        IComponentSpecification cs = parseComponent("BeanDescription.jwc");
        IBeanSpecification bs = cs.getBeanSpecification("mybean");

        assertEquals("myvalue", bs.getProperty("myproperty"));
    }

    /**
     * @since 4.0
     */

    public void testBeanInject() throws Exception
    {
        IComponentSpecification cs = parseComponent("BeanInject.jwc");
        IBeanSpecification bs = cs.getBeanSpecification("bean");
        assertEquals("myProperty", bs.getPropertyName());
    }

    /** @since 4.0 */

    public void testLibraryDescription() throws Exception
    {
        ILibrarySpecification ls = parseLib("LibraryDescription.library");

        assertEquals("Often, these are just placeholders.", ls.getDescription());
    }

    /** @since 4.0 */

    public void testPageDescription() throws Exception
    {
        IComponentSpecification spec = parsePage("PageDescription.page");

        assertEquals("Description of this page.", spec.getDescription());
    }

    /**
     * Excercies the check that the correct root element is used.
     * 
     * @since 4.0
     */

    public void testRootElementMismatch() throws Exception
    {
        try
        {
            parseComponent("NulledPage.page");
            unreachable();
        }
        catch (Exception ex)
        {
            checkException(
                    ex,
                    "Incorrect document type; expected page-specification but received component-specification.");
        }
    }

    /**
     * Checks to make sure that a application or library may not defined a lbirary with id
     * 'framework'.
     * 
     * @since 4.0
     */

    public void testLibraryFrameworkNamespace() throws Exception
    {
        try
        {
            parseLib("LibraryFrameworkNamespace.library");
            unreachable();
        }
        catch (Exception ex)
        {
            checkException(ex, "The library id 'framework' is reserved and may not be used.");
        }
    }

    /**
     * Tests that a &lt;component&gt; element may not have both type and copy-of attributes.
     * 
     * @since 4.0
     */

    public void testComponentWithTypeAndCopyOf() throws Exception
    {
        try
        {
            parseComponent("ComponentWithTypeAndCopyOf.jwc");
            unreachable();
        }
        catch (Exception ex)
        {
            checkException(ex, "Contained component bad contains both type and copy-of attributes.");
        }
    }

    /**
     * Tests that &lt;component&gt; must have either type or copy-of attribute.
     * 
     * @since 4.0
     */

    public void testComponentWithoutType() throws Exception
    {
        try
        {
            parseComponent("ComponentWithoutType.jwc");
            unreachable();
        }
        catch (Exception ex)
        {
            checkException(
                    ex,
                    "Contained component bad does not specify attribute type or copy-of.");
        }
    }

    /**
     * Tests the use of copy-of attribute inside &lt;component&gt;.
     * 
     * @since 4.0
     */

    public void testComponentCopyOf() throws Exception
    {
        IComponentSpecification cs = parseComponent("ComponentCopyOf.jwc");

        IContainedComponent source = cs.getComponent("source");
        IContainedComponent copy = cs.getComponent("copy");
        IContainedComponent override = cs.getComponent("override");

        assertEquals("Insert", source.getType());
        assertEquals("Insert", copy.getType());
        assertEquals("Insert", override.getType());

        IBindingSpecification b = source.getBinding("value");

        assertEquals(BindingType.PREFIXED, b.getType());
        assertEquals("ognl:date", b.getValue());

        assertSame(b, copy.getBinding("value"));

        IBindingSpecification b2 = override.getBinding("value");
        assertEquals("ognl:tomorrow", b2.getValue());

        b = copy.getBinding("foo");

        assertSame(b, override.getBinding("foo"));

        b = copy.getBinding("formatter");

        assertSame(b, override.getBinding("formatter"));
    }

    /**
     * And here's what happens when copy-of doesn't match a known component.
     * 
     * @since 4.0
     */
    public void testComponentBadCopy()
    {
        try
        {
            parseComponent("ComponentBadCopy.jwc");
            unreachable();
        }
        catch (Exception ex)
        {
            checkException(ex, "Unable to copy component missing, which does not exist.");
        }
    }

    /**
     * Check that &lt;service&gt; elements are ignored properly.
     * 
     * @since 4.0
     */

    public void testServiceElement() throws Exception
    {
        interceptLogging("org.apache.tapestry");

        parseLib("ServiceElement.library");

        assertLoggedMessagePattern("The <service> element is no longer supported");
    }

    /** @since 4.0 */
    public void testMeta() throws Exception
    {
        ILibrarySpecification spec = parseLib("Meta.library");

        assertEquals("bar", spec.getProperty("foo"));
        assertEquals("long value", spec.getProperty("long"));
    }

    /** @since 4.0 */
    public void testInject() throws Exception
    {
        IComponentSpecification spec = parseComponent("Inject.jwc");

        List l = spec.getInjectSpecifications();

        assertEquals(2, l.size());

        InjectSpecification i1 = (InjectSpecification) l.get(0);

        assertEquals("fred", i1.getProperty());
        assertEquals("flintstone", i1.getObjectReference());
        assertNotNull(i1.getLocation());

        InjectSpecification i2 = (InjectSpecification) l.get(1);
        assertEquals("barney", i2.getProperty());
        assertEquals("rubble", i2.getObjectReference());
        assertNotNull(i2.getLocation());
    }

    /**
     * Test that the new &lt;property&gt; element (was &lt;property-specification&gt; in release
     * 3.0) works correctly.
     * 
     * @since 4.0
     */

    public void testProperty() throws Exception
    {
        IComponentSpecification spec = parsePage("Property.page");

        checkList("propertySpecificationNames", new String[]
        { "bool", "init", "longInit", "persist" }, spec.getPropertySpecificationNames());

        IPropertySpecification ps = spec.getPropertySpecification("bool");
        assertEquals("name", "bool", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());

        // In a 4.0 DTD, type is always null.
        assertNull("type", ps.getType());

        // Note that no prefix is added. Initial value will be a string literal,
        // or have a prefix and be something else.

        assertNull("initialValue", ps.getInitialValue());
        checkLine(ps, 24);

        ps = spec.getPropertySpecification("init");
        assertEquals("name", "init", ps.getName());
        assertEquals("persistent", false, ps.isPersistent());

        assertEquals("initialValue", "ognl:pageName", ps.getInitialValue());
        checkLine(ps, 26);

        ps = spec.getPropertySpecification("persist");
        assertEquals("name", "persist", ps.getName());
        assertEquals("persistent", true, ps.isPersistent());
        assertNull("initialValue", ps.getInitialValue());
        checkLine(ps, 25);

        ps = spec.getPropertySpecification("longInit");
        assertEquals("message:long-init-key", ps.getInitialValue());

        ps = spec.getPropertySpecification("unknown");

        assertNull("Unknown PropertySpecification", ps);
    }

    /**
     * Tests parameters specification from a 3.0 DTD
     * 
     * @since 4.0
     */

    public void testParameter_3_0() throws Exception
    {
        IComponentSpecification spec = parseComponent("Parameter_3_0.jwc");

        IParameterSpecification ps = spec.getParameter("noDefault");

        assertEquals("noDefault", ps.getPropertyName());
        assertEquals(true, ps.isRequired());
        assertEquals("bar", ps.getType());
        assertNull(ps.getDefaultValue());
        assertNull(ps.getDefaultBindingType());

        ps = spec.getParameter("withDefault");
        assertNull(ps.getType());
        assertEquals(false, ps.isRequired());

        // For 3.0 DTDs, where the default value was always an OGNL expression,
        // the parser will provide the "ognl:" prefix.

        assertEquals("ognl:an.expression", ps.getDefaultValue());

        ps = spec.getParameter("withDescription");
        assertEquals("A parameter with a description.", ps.getDescription());

        ps = spec.getParameter("altName");
        assertEquals("altNameParameter", ps.getPropertyName());

        ps = spec.getParameter("directionIn");
        assertEquals(true, ps.getCache());

        ps = spec.getParameter("directionAuto");
        assertEquals(false, ps.getCache());
    }

    /**
     * Tests the new way default-value is interpreted (as a binding-like value, prefixed to indicate
     * type).
     * 
     * @since 4.0
     */

    public void testParameter() throws Exception
    {
        IComponentSpecification spec = parseComponent("Parameter.jwc");

        IParameterSpecification ps = spec.getParameter("noDefault");

        assertNull(ps.getDefaultValue());
        assertNull(ps.getDefaultBindingType());
        assertEquals(true, ps.getCache());

        ps = spec.getParameter("literalDefault");

        assertEquals("literal-value", ps.getDefaultValue());

        ps = spec.getParameter("expressionDefault");

        assertEquals("ognl:an.expression", ps.getDefaultValue());

        ps = spec.getParameter("defaultBindingType");

        assertEquals("ognl", ps.getDefaultBindingType());

        ps = spec.getParameter("noCache");
        assertEquals(false, ps.getCache());
    }

    /**
     * Tests that assets read using the 3.0 DTD are converted properly into paths with the proper
     * prefix.
     * 
     * @since 4.0
     */
    public void testAssets_3_0() throws Exception
    {
        IComponentSpecification cs = parsePage("Assets_3_0.page");

        IAssetSpecification as = cs.getAsset("mycontext");

        assertEquals("context:path/to/context", as.getPath());

        as = cs.getAsset("myprivate");

        assertEquals("classpath:path/to/private", as.getPath());

        as = cs.getAsset("myexternal");

        assertEquals("http://myexternal/asset", as.getPath());

        assertListsEqual(new String[]
        { "mycontext", "myexternal", "myprivate" }, cs.getAssetNames());
    }

    /** @since 4.0 */

    public void testAssets() throws Exception
    {
        IComponentSpecification cs = parsePage("Assets.page");

        IAssetSpecification as = cs.getAsset("myasset");

        assertEquals("path/to/asset", as.getPath());
        assertEquals("myProperty", as.getPropertyName());
    }

    /** @since 4.0 */

    public void testInjectState() throws Exception
    {
        IComponentSpecification cs = parsePage("InjectState.page");

        List l = cs.getInjectStateSpecifications();

        assertEquals(1, l.size());

        InjectStateSpecification s = (InjectStateSpecification) l.get(0);

        assertEquals("fred", s.getProperty());
        assertEquals("barney", s.getObjectName());
    }
}