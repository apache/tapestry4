//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.parse;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Rule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.spec.AssetType;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.Direction;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IExtensionSpecification;
import org.apache.tapestry.spec.ILibrarySpecification;
import org.apache.tapestry.spec.SpecFactory;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

/**
 *  Used to parse an application or component specification into a
 *  {@link org.apache.tapestry.spec.ApplicationSpecification} or {@link IComponentSpecification}.
 *
 *
 *  <table border=1
 *	<tr>
 *	  <th>Version</th> <th>PUBLIC ID</th> <th>SYSTEM ID</th> <th>Description</th>
 *  </tr>
 *
 * 
 *  <tr valign="top">
 *  <td>1.3</td>
 *  <td><code>-//Howard Lewis Ship//Tapestry Specification 1.3//EN</code></td>
 * <td><code>http://tapestry.sf.net/dtd/Tapestry_1_3.dtd</code></td>
 *  <td>
 *  Version of specification introduced in release 2.2.
 * </td>
 * </tr>
 *
 *  <tr valign="top">
 *  <td>3.0</td>
 *  <td><code>-//Howard Lewis Ship//Tapestry Specification 3.0//EN</code></td>
 * <td><code>http://tapestry.sf.net/dtd/Tapestry_3_0.dtd</code></td>
 *  <td>
 *  Version of specification introduced in release 3.0.
 *  <br/>
 *  Note: Future DTD versions will track Tapestry release numbers.
 * </td>
 * </tr>
 * 
 * 
 *  </table>
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class SpecificationParser
{
    private static final Log LOG = LogFactory.getLog(SpecificationParser.class);

    /** @since 2.2 **/

    public static final String TAPESTRY_DTD_1_3_PUBLIC_ID =
        "-//Howard Lewis Ship//Tapestry Specification 1.3//EN";

    /** @since 3.0 **/

    public static final String TAPESTRY_DTD_3_0_PUBLIC_ID =
        "-//Apache Software Foundation//Tapestry Specification 3.0//EN";

    /**
     *  Like modified property name, but allows periods in the name as
     *  well.
     * 
     *  @since 2.2
     * 
     **/

    public static final String EXTENDED_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z](\\w|-|\\.)*$";

    /**
     *  Perl5 pattern that parameter names must conform to.  
     *  Letter, followed by letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String PARAMETER_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern that property names (that can be connected to
     *  parameters) must conform to.  
     *  Letter, followed by letter, number or underscore.
     *  
     * 
     *  @since 2.2
     * 
     **/

    public static final String PROPERTY_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for page names.  Letter
     *  followed by letter, number, dash, underscore or period.
     * 
     *  @since 2.2
     * 
     **/

    public static final String PAGE_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for component alias. 
     *  Letter, followed by letter, number, or underscore.
     *  This is used to validate component types registered
     *  in the application or library specifications.
     * 
     *  @since 2.2
     * 
     **/

    public static final String COMPONENT_ALIAS_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for helper bean names.  
     *  Letter, followed by letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String BEAN_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for component ids.  Letter, followed by
     *  letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String COMPONENT_ID_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for asset names.  Letter, followed by
     *  letter, number or underscore.  Also allows
     *  the special "$template" value.
     * 
     *  @since 2.2
     * 
     **/

    public static final String ASSET_NAME_PATTERN =
        "(\\$template)|(" + Tapestry.SIMPLE_PROPERTY_NAME_PATTERN + ")";

    /**
     *  Perl5 pattern for service names.  Letter
     *  followed by letter, number, dash, underscore or period.
     * 
     *  @since 2.2
     * 
     **/

    public static final String SERVICE_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for library ids.  Letter followed
     *  by letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String LIBRARY_ID_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Per5 pattern for extension names.  Letter followed
     *  by letter, number, dash, period or underscore. 
     * 
     *  @since 2.2
     * 
     **/

    public static final String EXTENSION_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for component types.  Component types are an optional
     *  namespace prefix followed by a normal identifier.
     * 
     *  @since 2.2
     **/

    public static final String COMPONENT_TYPE_PATTERN = "^(_?[a-zA-Z]\\w*:)?[a-zA-Z_](\\w)*$";

    /**
     *  We can share a single map for all the XML attribute to object conversions,
     *  since the keys are unique.
     * 
     **/

    private static final Map CONVERSION_MAP = new HashMap();

    /** @since 1.0.9 **/

    private SpecFactory _factory;

    /** 
     *   Digester used for component specifications.
     * 
     *  @since 3.0 
     * 
     **/

    private SpecificationDigester _componentDigester;

    /**
     *  Digestger used for page specifications.
     * 
     *  @since 3.0
     * 
     **/

    private SpecificationDigester _pageDigester;

    /**
     *  Digester use for library specifications.
     * 
     *  @since 3.0
     * 
     **/

    private SpecificationDigester _libraryDigester;

    /**
     *  @since 3.0 
     * 
     **/

    private IResourceResolver _resolver;

    private interface IConverter
    {
        public Object convert(String value) throws DocumentParseException;
    }

    private static class BooleanConverter implements IConverter
    
    {
        public Object convert(String value) throws DocumentParseException
        {
            Object result = CONVERSION_MAP.get(value.toLowerCase());

            if (result == null || !(result instanceof Boolean))
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.fail-convert-boolean", value));

            return result;
        }
    }

    private static class IntConverter implements IConverter
    {
        public Object convert(String value) throws DocumentParseException
        {
            try
            {
                return new Integer(value);
            }
            catch (NumberFormatException ex)
            {
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.fail-convert-int", value),
                    ex);
            }
        }
    }

    private static class LongConverter implements IConverter
    {
        public Object convert(String value) throws DocumentParseException
        {
            try
            {
                return new Long(value);
            }
            catch (NumberFormatException ex)
            {
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.fail-convert-long", value),
                    ex);
            }
        }
    }

    private static class DoubleConverter implements IConverter
    {
        public Object convert(String value) throws DocumentParseException
        {
            try
            {
                return new Double(value);
            }
            catch (NumberFormatException ex)
            {
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.fail-convert-double", value),
                    ex);
            }
        }
    }

    private static class StringConverter implements IConverter
    {
        public Object convert(String value)
        {
            return value.trim();
        }
    }

    /** 
     *  Base class for creating locatable objects using the 
     *  {@link SpecFactory}.
     * 
     **/

    private abstract static class SpecFactoryCreateRule extends AbstractSpecificationRule
    {
        /**
         *  Implement in subclass to create correct locatable object.
         * 
         **/

        public abstract ILocationHolder create();

        public void begin(String namespace, String name, Attributes attributes) throws Exception
        {
            ILocationHolder holder = create();

            holder.setLocation(getLocation());

            digester.push(holder);
        }

        public void end(String namespace, String name) throws Exception
        {
            digester.pop();
        }

    }

    private class CreateExpressionBeanInitializerRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createExpressionBeanInitializer();
        }
    }

    private class CreateStringBeanInitializerRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createMessageBeanInitializer();
        }
    }

    private class CreateContainedComponentRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createContainedComponent();
        }
    }

    private class CreateParameterSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createParameterSpecification();
        }
    }

    private class CreateComponentSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createComponentSpecification();
        }
    }

    private class CreateBindingSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createBindingSpecification();
        }
    }

    private class CreateBeanSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createBeanSpecification();
        }
    }

    private class CreateListenerBindingSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createListenerBindingSpecification();
        }
    }

    private class CreateAssetSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createAssetSpecification();
        }
    }

    private class CreatePropertySpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createPropertySpecification();
        }
    }

    private class CreateApplicationSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createApplicationSpecification();
        }
    }

    private class CreateLibrarySpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createLibrarySpecification();
        }
    }

    private class CreateExtensionSpecificationRule extends SpecFactoryCreateRule
    {
        public ILocationHolder create()
        {
            return _factory.createExtensionSpecification();
        }
    }

    private static class ProcessExtensionConfigurationRule extends AbstractSpecificationRule
    {
        private String _value;
        private String _propertyName;
        private IConverter _converter;

        public void begin(String namespace, String name, Attributes attributes) throws Exception
        {
            _propertyName = getValue(attributes, "property-name");
            _value = getValue(attributes, "value");

            String type = getValue(attributes, "type");

            _converter = (IConverter) CONVERSION_MAP.get(type);

            if (_converter == null)
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.unknown-static-value-type", type),
                    getResourceLocation());

        }

        public void body(String namespace, String name, String text) throws Exception
        {
            if (Tapestry.isBlank(text))
                return;

            if (_value != null)
                throw new DocumentParseException(
                    Tapestry.format("SpecificationParser.no-attribute-and-body", "value", name),
                    getResourceLocation());

            _value = text.trim();
        }

        public void end(String namespace, String name) throws Exception
        {
            if (_value == null)
                throw new DocumentParseException(
                    Tapestry.format(
                        "SpecificationParser.required-extended-attribute",
                        name,
                        "value"),
                    getResourceLocation());

            Object objectValue = _converter.convert(_value);

            IExtensionSpecification top = (IExtensionSpecification) digester.peek();

            top.addConfiguration(_propertyName, objectValue);

            _converter = null;
            _value = null;
            _propertyName = null;

        }

    }

    // Identify all the different acceptible values.
    // We continue to sneak by with a single map because
    // there aren't conflicts;  when we have 'foo' meaning
    // different things in different places in the DTD, we'll
    // need multiple maps.

    static {

        CONVERSION_MAP.put("true", Boolean.TRUE);
        CONVERSION_MAP.put("t", Boolean.TRUE);
        CONVERSION_MAP.put("1", Boolean.TRUE);
        CONVERSION_MAP.put("y", Boolean.TRUE);
        CONVERSION_MAP.put("yes", Boolean.TRUE);
        CONVERSION_MAP.put("on", Boolean.TRUE);

        CONVERSION_MAP.put("false", Boolean.FALSE);
        CONVERSION_MAP.put("f", Boolean.FALSE);
        CONVERSION_MAP.put("0", Boolean.FALSE);
        CONVERSION_MAP.put("off", Boolean.FALSE);
        CONVERSION_MAP.put("no", Boolean.FALSE);
        CONVERSION_MAP.put("n", Boolean.FALSE);

        CONVERSION_MAP.put("none", BeanLifecycle.NONE);
        CONVERSION_MAP.put("request", BeanLifecycle.REQUEST);
        CONVERSION_MAP.put("page", BeanLifecycle.PAGE);
        CONVERSION_MAP.put("render", BeanLifecycle.RENDER);

        CONVERSION_MAP.put("boolean", new BooleanConverter());
        CONVERSION_MAP.put("int", new IntConverter());
        CONVERSION_MAP.put("double", new DoubleConverter());
        CONVERSION_MAP.put("String", new StringConverter());
        CONVERSION_MAP.put("long", new LongConverter());

        CONVERSION_MAP.put("in", Direction.IN);
        CONVERSION_MAP.put("form", Direction.FORM);
        CONVERSION_MAP.put("custom", Direction.CUSTOM);
        CONVERSION_MAP.put("auto", Direction.AUTO);
    }

    public SpecificationParser(IResourceResolver resolver)
    {
        _resolver = resolver;
        setFactory(new SpecFactory());
    }

    /**
     *  Parses an input stream containing a page or component specification and assembles
     *  an {@link IComponentSpecification} from it.  
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public IComponentSpecification parseComponentSpecification(IResourceLocation resourceLocation)
        throws DocumentParseException
    {
        if (_componentDigester == null)
            _componentDigester = constructComponentDigester();

        try
        {
            IComponentSpecification result =
                (IComponentSpecification) parse(_componentDigester, resourceLocation);

            result.setSpecificationLocation(resourceLocation);

            return result;
        }
        catch (DocumentParseException ex)
        {
            _componentDigester = null;

            throw ex;
        }
    }

    /**
     *  Parses a resource using a particular digester.
     * 
     *  @since 3.0
     * 
     **/

    protected Object parse(SpecificationDigester digester, IResourceLocation location)
        throws DocumentParseException
    {
        try
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Parsing " + location);

            URL url = location.getResourceURL();

            if (url == null)
                throw new DocumentParseException(
                    Tapestry.format("AbstractDocumentParser.missing-resource", location),
                    location);

            InputSource source = new InputSource(url.toExternalForm());

            digester.setResourceLocation(location);

            Object result = digester.parse(source);

            if (LOG.isDebugEnabled())
                LOG.debug("Result: " + result);

            return result;
        }
        catch (SAXParseException ex)
        {
            throw new DocumentParseException(ex);
        }
        catch (DocumentParseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new DocumentParseException(
                Tapestry.format(
                    "SpecificationParser.error-reading-resource",
                    location,
                    ex.getMessage()),
                location,
                ex);
        }
        finally
        {
            digester.setResourceLocation(null);
        }
    }

    /**
     *  Parses an input stream containing a page specification and assembles
     *  an {@link IComponentSpecification} from it.  
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     * 
     *  @since 2.2
     *
     **/

    public IComponentSpecification parsePageSpecification(IResourceLocation resourceLocation)
        throws DocumentParseException
    {
        if (_pageDigester == null)
            _pageDigester = constructPageDigester();

        try
        {
            IComponentSpecification result =
                (IComponentSpecification) parse(_pageDigester, resourceLocation);

            result.setSpecificationLocation(resourceLocation);

            return result;
        }
        catch (DocumentParseException ex)
        {
            _pageDigester = null;

            throw ex;
        }
    }

    /**
     *  Parses an resource containing an application specification and assembles
     *  an {@link org.apache.tapestry.spec.ApplicationSpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public IApplicationSpecification parseApplicationSpecification(IResourceLocation resourceLocation)
        throws DocumentParseException
    {

        // Use a one-shot digester, because you only parse the app spec
        // once.

        IApplicationSpecification result =
            (IApplicationSpecification) parse(constructApplicationDigester(), resourceLocation);

        result.setResourceResolver(_resolver);
        result.setSpecificationLocation(resourceLocation);
        result.instantiateImmediateExtensions();

        return result;
    }

    /**
     *  Parses an input stream containing a library specification and assembles
     *  a {@link org.apache.tapestry.spec.LibrarySpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     * 
     *  @since 2.2
     *
     **/

    public ILibrarySpecification parseLibrarySpecification(IResourceLocation resourceLocation)
        throws DocumentParseException
    {
        if (_libraryDigester == null)
            _libraryDigester = constructLibraryDigester();

        try
        {
            ILibrarySpecification result =
                (ILibrarySpecification) parse(_libraryDigester, resourceLocation);

            result.setResourceResolver(_resolver);
            result.setSpecificationLocation(resourceLocation);
            result.instantiateImmediateExtensions();

            return result;
        }
        catch (DocumentParseException ex)
        {
            _libraryDigester = null;

            throw ex;
        }
    }

    /**
     *  Sets the SpecFactory which instantiates Tapestry spec objects.
     * 
     *  @since 1.0.9
     **/

    public void setFactory(SpecFactory factory)
    {
        _factory = factory;
    }

    /**
     *  Returns the current SpecFactory which instantiates Tapestry spec objects.
     * 
     *  @since 1.0.9
     * 
     **/

    public SpecFactory getFactory()
    {
        return _factory;
    }

    /**
     *  Constructs a digester, registerring the known DTDs and the
     *  global rules (for &lt;property&gt; and &lt;description&gt;).
     * 
     *  @since 3.0
     * 
     **/

    protected SpecificationDigester constructBaseDigester(String rootElement)
    {
        SpecificationDigester result = new SpecificationDigester();

        // <description>

        result.addBeanPropertySetter("*/description", "description");

        // <property> 

        result.addRule("*/property", new SetMetaPropertyRule());

        result.register(TAPESTRY_DTD_1_3_PUBLIC_ID, getURL("Tapestry_1_3.dtd"));
        result.register(TAPESTRY_DTD_3_0_PUBLIC_ID, getURL("Tapestry_3_0.dtd"));

        result.addDocumentRule(
            new ValidatePublicIdRule(
                new String[] { TAPESTRY_DTD_1_3_PUBLIC_ID, TAPESTRY_DTD_3_0_PUBLIC_ID },
                rootElement));

        result.setValidating(true);

        return result;

    }

    /**
     *  Constructs a digester configued to parse application specifications.
     * 
     *  @since 3.0
     * 
     **/

    protected SpecificationDigester constructApplicationDigester()
    {
        SpecificationDigester result = constructBaseDigester("application");

        String pattern = "application";

        result.addRule(pattern, new CreateApplicationSpecificationRule());
        result.addSetLimitedProperties(
            pattern,
            new String[] { "name", "engine-class" },
            new String[] { "name", "engineClassName" });
        result.addRule(pattern, new SetPublicIdRule());

        configureLibraryCommon(result, "application");

        return result;
    }

    /**
     *  Constructs a digester configured to parse library specifications.
     * 
     *  @since 3.0
     * 
     **/

    protected SpecificationDigester constructLibraryDigester()
    {
        SpecificationDigester result = constructBaseDigester("library-specification");

        String pattern = "library-specification";

        result.addRule(pattern, new CreateLibrarySpecificationRule());
        result.addRule(pattern, new SetPublicIdRule());

        // Has no attributes

        configureLibraryCommon(result, "library-specification");

        return result;
    }

    /**
     *  Configures a digester to parse the common elements of
     *  a &lt;application&gt; or &lt;library-specification&gt;.
     * 
     *  @since 3.0
     * 
     **/

    protected void configureLibraryCommon(SpecificationDigester digester, String rootElementName)
    {
        String pattern = rootElementName + "/page";

        // <page>

        digester.addValidate(
            pattern,
            "name",
            PAGE_NAME_PATTERN,
            "SpecificationParser.invalid-page-name");
        digester.addCallMethod(pattern, "setPageSpecificationPath", 2);
        digester.addCallParam(pattern, 0, "name");
        digester.addCallParam(pattern, 1, "specification-path");

        // <component-type>

        pattern = rootElementName + "/component-type";
        digester.addValidate(
            pattern,
            "type",
            COMPONENT_ALIAS_PATTERN,
            "SpecificationParser.invalid-component-type");
        digester.addCallMethod(pattern, "setComponentSpecificationPath", 2);
        digester.addCallParam(pattern, 0, "type");
        digester.addCallParam(pattern, 1, "specification-path");

        // <component-alias>
        // From 1.3 DTD, replaced with <component-type> in 3.0 DTD

        pattern = rootElementName + "/component-alias";
        digester.addValidate(
            pattern,
            "type",
            COMPONENT_ALIAS_PATTERN,
            "SpecificationParser.invalid-component-type");
        digester.addCallMethod(pattern, "setComponentSpecificationPath", 2);
        digester.addCallParam(pattern, 0, "type");
        digester.addCallParam(pattern, 1, "specification-path");

        // <service>

        pattern = rootElementName + "/service";

        digester.addValidate(
            pattern,
            "name",
            SERVICE_NAME_PATTERN,
            "SpecificationParser.invalid-service-name");
        digester.addCallMethod(pattern, "setServiceClassName", 2);
        digester.addCallParam(pattern, 0, "name");
        digester.addCallParam(pattern, 1, "class");

        // <library>

        pattern = rootElementName + "/library";

        digester.addValidate(
            pattern,
            "id",
            LIBRARY_ID_PATTERN,
            "SpecificationParser.invalid-library-id");
        digester.addRule(pattern, new DisallowFrameworkNamespaceRule());
        digester.addCallMethod(pattern, "setLibrarySpecificationPath", 2);
        digester.addCallParam(pattern, 0, "id");
        digester.addCallParam(pattern, 1, "specification-path");

        // <extension>

        pattern = rootElementName + "/extension";

        digester.addRule(pattern, new CreateExtensionSpecificationRule());
        digester.addValidate(
            pattern,
            "name",
            EXTENSION_NAME_PATTERN,
            "SpecificationParser.invalid-extension-name");
        digester.addSetBooleanProperty(pattern, "immediate", "immediate");
        digester.addSetLimitedProperties(pattern, "class", "className");
        digester.addConnectChild(pattern, "addExtensionSpecification", "name");

        // <configure> within <extension>

        pattern = rootElementName + "/extension/configure";
        digester.addValidate(
            pattern,
            "property-name",
            PROPERTY_NAME_PATTERN,
            "SpecificationParser.invalid-property-name");
        digester.addRule(pattern, new ProcessExtensionConfigurationRule());

    }

    /**
     *  Returns a digester configured to parse page specifications.
     * 
     *  @since 3.0
     * 
     **/

    protected SpecificationDigester constructPageDigester()
    {
        SpecificationDigester result = constructBaseDigester("page-specification");

        // <page-specification>

        String pattern = "page-specification";

        result.addRule(pattern, new CreateComponentSpecificationRule());
        result.addRule(pattern, new SetPublicIdRule());
        result.addInitializeProperty(pattern, "pageSpecification", Boolean.TRUE);
        result.addInitializeProperty(pattern, "allowBody", Boolean.TRUE);
        result.addInitializeProperty(pattern, "allowInformalParameters", Boolean.FALSE);
        result.addSetLimitedProperties(pattern, "class", "componentClassName");

        configureCommon(result, "page-specification");

        return result;
    }

    /**
     *  Returns a digester configured to parse component specifications.
     * 
     **/

    protected SpecificationDigester constructComponentDigester()
    {
        SpecificationDigester result = constructBaseDigester("component-specification");

        // <component-specification>

        String pattern = "component-specification";

        result.addRule(pattern, new CreateComponentSpecificationRule());
        result.addRule(pattern, new SetPublicIdRule());

        result.addSetBooleanProperty(pattern, "allow-body", "allowBody");
        result.addSetBooleanProperty(
            pattern,
            "allow-informal-parameters",
            "allowInformalParameters");
        result.addSetLimitedProperties(pattern, "class", "componentClassName");

        // TODO: publicId

        // <parameter>

        pattern = "component-specification/parameter";

        result.addRule(pattern, new CreateParameterSpecificationRule());
        result.addValidate(
            pattern,
            "name",
            PARAMETER_NAME_PATTERN,
            "SpecificationParser.invalid-parameter-name");

        result.addValidate(
            pattern,
            "property-name",
            PROPERTY_NAME_PATTERN,
            "SpecificationParser.invalid-property-name");

        // We use a slight kludge to set the default propertyName from the 
        // name attribute.  If the spec includes a property-name attribute, that
        // will overwrite the default property name).
        // Remember that digester rule order counts!

        result.addSetLimitedProperties(pattern, "name", "propertyName");

        // java-type is a holdover from the 1.3 DTD and will eventually be removed.

        result.addSetLimitedProperties(
            pattern,
            new String[] { "property-name", "type", "java-type", "default-value" },
            new String[] { "propertyName", "type", "type", "defaultValue" });

        result.addSetBooleanProperty(pattern, "required", "required");

        result.addSetConvertedProperty(pattern, CONVERSION_MAP, "direction", "direction");

        result.addConnectChild(pattern, "addParameter", "name");

        // <reserved-parameter>

        pattern = "component-specification/reserved-parameter";

        result.addCallMethod(pattern, "addReservedParameterName", 1);
        result.addCallParam(pattern, 0, "name");

        configureCommon(result, "component-specification");

        return result;
    }

    /**
     *  Configure the common elements shared by both &lt;page-specification&gt;
     *  and &lt;component-specification&gt;.
     * 
     **/

    protected void configureCommon(SpecificationDigester digester, String rootElementName)
    {
        // <bean>

        String pattern = rootElementName + "/bean";

        digester.addRule(pattern, new CreateBeanSpecificationRule());
        digester.addValidate(
            pattern,
            "name",
            BEAN_NAME_PATTERN,
            "SpecificationParser.invalid-bean-name");
        digester.addSetConvertedProperty(pattern, CONVERSION_MAP, "lifecycle", "lifecycle");
        digester.addSetLimitedProperties(pattern, "class", "className");
        digester.addConnectChild(pattern, "addBeanSpecification", "name");

        // <set-property> inside <bean>

        pattern = rootElementName + "/bean/set-property";

        digester.addRule(pattern, new CreateExpressionBeanInitializerRule());
        digester.addSetLimitedProperties(pattern, "name", "propertyName");
        digester.addSetExtendedProperty(pattern, "expression", "expression", true);
        digester.addSetNext(pattern, "addInitializer");

        // <set-string-property> inside <bean>
        // This is for compatibility with the 1.3 DTD

        pattern = rootElementName + "/bean/set-string-property";

        digester.addRule(pattern, new CreateStringBeanInitializerRule());
        digester.addSetLimitedProperties(
            pattern,
            new String[] { "name", "key" },
            new String[] { "propertyName", "key" });

        digester.addSetNext(pattern, "addInitializer");

        // It's now set-message-property in the 3.0 DTD

        pattern = rootElementName + "/bean/set-message-property";

        digester.addRule(pattern, new CreateStringBeanInitializerRule());
        digester.addSetLimitedProperties(
            pattern,
            new String[] { "name", "key" },
            new String[] { "propertyName", "key" });

        digester.addSetNext(pattern, "addInitializer");

        // <component>

        pattern = rootElementName + "/component";

        digester.addRule(pattern, new CreateContainedComponentRule());
        digester.addValidate(
            pattern,
            "id",
            COMPONENT_ID_PATTERN,
            "SpecificationParser.invalid-component-id");
        digester.addValidate(
            pattern,
            "type",
            COMPONENT_TYPE_PATTERN,
            "SpecificationParser.invalid-component-type");
        digester.addSetLimitedProperties(pattern, "type", "type");
        digester.addRule(pattern, new ComponentCopyOfRule());
        digester.addConnectChild(pattern, "addComponent", "id");
        digester.addSetBooleanProperty(
            pattern,
            "inherit-informal-parameters",
            "inheritInformalParameters");

        // <binding> inside <component>

        pattern = rootElementName + "/component/binding";

        Rule createBindingSpecificationRule = new CreateBindingSpecificationRule();

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.DYNAMIC);
        digester.addSetExtendedProperty(pattern, "expression", "value", true);
        digester.addConnectChild(pattern, "setBinding", "name");

        // <field-binding> inside <component>
        // For compatibility with 1.3 DTD only, removed in 3.0 DTD

        pattern = rootElementName + "/component/field-binding";

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.FIELD);
        digester.addSetExtendedProperty(pattern, "field-name", "value", true);
        digester.addConnectChild(pattern, "setBinding", "name");

        // <inherited-binding> inside <component>

        pattern = rootElementName + "/component/inherited-binding";

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.INHERITED);
        digester.addSetLimitedProperties(pattern, "parameter-name", "value");
        digester.addConnectChild(pattern, "setBinding", "name");

        // <static-binding> inside <component>

        pattern = rootElementName + "/component/static-binding";

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.STATIC);
        digester.addSetExtendedProperty(pattern, "value", "value", true);
        digester.addConnectChild(pattern, "setBinding", "name");

        // <string-binding> inside <component>
        // Maintained just for 1.3 DTD compatibility

        pattern = rootElementName + "/component/string-binding";

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.STRING);
        digester.addSetLimitedProperties(pattern, "key", "value");
        digester.addConnectChild(pattern, "setBinding", "name");

        // Renamed to <message-binding> in the 3.0 DTD

        pattern = rootElementName + "/component/message-binding";

        digester.addRule(pattern, createBindingSpecificationRule);
        digester.addInitializeProperty(pattern, "type", BindingType.STRING);
        digester.addSetLimitedProperties(pattern, "key", "value");
        digester.addConnectChild(pattern, "setBinding", "name");

        // <listener-binding> inside <component>

        pattern = rootElementName + "/component/listener-binding";

        digester.addRule(pattern, new CreateListenerBindingSpecificationRule());
        digester.addSetLimitedProperties(pattern, "language", "language");
        digester.addBody(pattern, "value");
        digester.addConnectChild(pattern, "setBinding", "name");

        // <external-asset>

        pattern = rootElementName + "/external-asset";

        Rule createAssetSpecificationRule = new CreateAssetSpecificationRule();

        digester.addRule(pattern, createAssetSpecificationRule);
        digester.addInitializeProperty(pattern, "type", AssetType.EXTERNAL);
        digester.addValidate(
            pattern,
            "name",
            ASSET_NAME_PATTERN,
            "SpecificationParser.invalid-asset-name");
        digester.addSetLimitedProperties(pattern, "URL", "path");
        digester.addConnectChild(pattern, "addAsset", "name");

        // <context-asset>

        pattern = rootElementName + "/context-asset";

        digester.addRule(pattern, createAssetSpecificationRule);
        digester.addInitializeProperty(pattern, "type", AssetType.CONTEXT);
        digester.addValidate(
            pattern,
            "name",
            ASSET_NAME_PATTERN,
            "SpecificationParser.invalid-asset-name");

        // TODO: $template$

        digester.addSetLimitedProperties(pattern, "path", "path");
        digester.addConnectChild(pattern, "addAsset", "name");

        // <private-asset>

        pattern = rootElementName + "/private-asset";

        digester.addRule(pattern, createAssetSpecificationRule);
        digester.addInitializeProperty(pattern, "type", AssetType.PRIVATE);
        digester.addValidate(
            pattern,
            "name",
            ASSET_NAME_PATTERN,
            "SpecificationParser.invalid-asset-name");

        // TODO: $template$

        digester.addSetLimitedProperties(pattern, "resource-path", "path");
        digester.addConnectChild(pattern, "addAsset", "name");

        // <property-specification>

        pattern = rootElementName + "/property-specification";

        digester.addRule(pattern, new CreatePropertySpecificationRule());
        digester.addValidate(
            pattern,
            "name",
            PROPERTY_NAME_PATTERN,
            "SpecificationParser.invalid-property-name");
        digester.addSetLimitedProperties(
            pattern,
            new String[] { "name", "type" },
            new String[] { "name", "type" });
        digester.addSetBooleanProperty(pattern, "persistent", "persistent");
        digester.addSetExtendedProperty(pattern, "initial-value", "initialValue", false);
        digester.addSetNext(pattern, "addPropertySpecification");
    }

    private String getURL(String resource)
    {
        return getClass().getResource(resource).toExternalForm();
    }
}