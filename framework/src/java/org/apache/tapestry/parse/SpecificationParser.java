// Copyright 2004 The Apache Software Foundation
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultErrorHandler;
import org.apache.hivemind.parse.AbstractParser;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.bean.IBeanInitializer;
import org.apache.tapestry.spec.AssetType;
import org.apache.tapestry.spec.BeanLifecycle;
import org.apache.tapestry.spec.BindingType;
import org.apache.tapestry.spec.Direction;
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
import org.apache.tapestry.spec.SpecFactory;
import org.apache.tapestry.util.IPropertyHolder;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.util.xml.DocumentParseException;
import org.apache.tapestry.util.xml.InvalidStringException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Parses the different types of Tapestry specifications.
 * <p>
 * Not threadsafe; it is the callers responsibility to ensure thread safety.
 * 
 * @author Howard Lewis Ship
 */
public class SpecificationParser extends AbstractParser implements ISpecificationParser
{
    /**
     * Perl5 pattern for asset names. Letter, followed by letter, number or underscore. Also allows
     * the special "$template" value.
     * 
     * @since 2.2
     */

    public static final String ASSET_NAME_PATTERN = "(\\$template)|("
            + Tapestry.SIMPLE_PROPERTY_NAME_PATTERN + ")";

    /**
     * Perl5 pattern for helper bean names. Letter, followed by letter, number or underscore.
     * 
     * @since 2.2
     */

    public static final String BEAN_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern for component alias. Letter, followed by letter, number, or underscore. This is
     * used to validate component types registered in the application or library specifications.
     * 
     * @since 2.2
     */

    public static final String COMPONENT_ALIAS_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern for component ids. Letter, followed by letter, number or underscore.
     * 
     * @since 2.2
     */

    public static final String COMPONENT_ID_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern for component types. Component types are an optional namespace prefix followed
     * by a normal identifier.
     * 
     * @since 2.2
     */

    public static final String COMPONENT_TYPE_PATTERN = "^(_?[a-zA-Z]\\w*:)?[a-zA-Z_](\\w)*$";

    /**
     * We can share a single map for all the XML attribute to object conversions, since the keys are
     * unique.
     */

    private final Map CONVERSION_MAP = new HashMap();

    /**
     * Like modified property name, but allows periods in the name as well.
     * 
     * @since 2.2
     */

    public static final String EXTENDED_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z](\\w|-|\\.)*$";

    /**
     * Per5 pattern for extension names. Letter followed by letter, number, dash, period or
     * underscore.
     * 
     * @since 2.2
     */

    public static final String EXTENSION_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern for library ids. Letter followed by letter, number or underscore.
     * 
     * @since 2.2
     */

    public static final String LIBRARY_ID_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /** @since 3.1 */
    private final Log _log;

    /** @since 3.1 */
    private final ErrorHandler _errorHandler;

    /**
     * Set to true if parsing the 3.1 DTD.
     * 
     * @since 3.1
     */

    private boolean _DTD_3_1;

    /**
     * Perl5 pattern for page names. Letter followed by letter, number, dash, underscore or period.
     * 
     * @since 2.2
     */

    public static final String PAGE_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern that parameter names must conform to. Letter, followed by letter, number or
     * underscore.
     * 
     * @since 2.2
     */

    public static final String PARAMETER_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern that property names (that can be connected to parameters) must conform to.
     * Letter, followed by letter, number or underscore.
     * 
     * @since 2.2
     */

    public static final String PROPERTY_NAME_PATTERN = Tapestry.SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     * Perl5 pattern for service names. Letter followed by letter, number, dash, underscore or
     * period.
     * 
     * @since 2.2
     * @deprecated As of release 3.1, the &lt;service&gt; element (in 3.0 DTDs) is no longer
     *             supported.
     */

    public static final String SERVICE_NAME_PATTERN = EXTENDED_PROPERTY_NAME_PATTERN;

    private static final int STATE_ALLOW_DESCRIPTION = 2000;

    private static final int STATE_ALLOW_PROPERTY = 2001;

    private static final int STATE_APPLICATION_SPECIFICATION_INITIAL = 1002;

    private static final int STATE_BEAN = 4;

    /** Very different between 3.0 and 3.1 DTD */

    private static final int STATE_BINDING_3_0 = 7;

    /** @since 3.1 */

    private static final int STATE_BINDING = 100;

    private static final int STATE_COMPONENT = 6;

    private static final int STATE_COMPONENT_SPECIFICATION = 1;

    private static final int STATE_COMPONENT_SPECIFICATION_INITIAL = 1000;

    private static final int STATE_CONFIGURE = 14;

    private static final int STATE_DESCRIPTION = 2;

    private static final int STATE_EXTENSION = 13;

    private static final int STATE_LIBRARY_SPECIFICATION = 12;

    private static final int STATE_LIBRARY_SPECIFICATION_INITIAL = 1003;

    private static final int STATE_LISTENER_BINDING = 8;

    private static final int STATE_NO_CONTENT = 3000;

    private static final int STATE_PAGE_SPECIFICATION = 11;

    private static final int STATE_PAGE_SPECIFICATION_INITIAL = 1001;

    private static final int STATE_META = 3;

    private static final int STATE_PROPERTY_SPECIFICATION = 10;

    private static final int STATE_SET_PROPERTY = 5;

    /** 3.0 DTD only */
    private static final int STATE_STATIC_BINDING = 9;

    /** @since 3.0 */

    public static final String TAPESTRY_DTD_3_0_PUBLIC_ID = "-//Apache Software Foundation//Tapestry Specification 3.0//EN";

    /** @since 3.1 */

    public static final String TAPESTRY_DTD_3_1_PUBLIC_ID = "-//Apache Software Foundation//Tapestry Specification 3.1//EN";

    /**
     * The attributes of the current element, as a map (string keyed on string).
     */

    private Map _attributes;

    /**
     * The name of the current element.
     */

    private String _elementName;

    /** @since 1.0.9 */

    private final SpecFactory _factory;

    private RegexpMatcher _matcher = new RegexpMatcher();

    private SAXParser _parser;

    private SAXParserFactory _parserFactory = SAXParserFactory.newInstance();

    /**
     * @since 3.0
     */

    private final ClassResolver _resolver;

    /**
     * The root object parsed: a component or page specification, a library specification, or an
     * application specification.
     */
    private Object _rootObject;

    // Identify all the different acceptible values.
    // We continue to sneak by with a single map because
    // there aren't conflicts; when we have 'foo' meaning
    // different things in different places in the DTD, we'll
    // need multiple maps.

    {

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

        CONVERSION_MAP.put("in", Direction.IN);
        CONVERSION_MAP.put("form", Direction.FORM);
        CONVERSION_MAP.put("custom", Direction.CUSTOM);
        CONVERSION_MAP.put("auto", Direction.AUTO);

        _parserFactory.setNamespaceAware(false);
        _parserFactory.setValidating(true);
    }

    /**
     * This constructor is a convienience used by some tests.
     */
    public SpecificationParser(ClassResolver resolver)
    {
        this(resolver, new SpecFactory());
    }

    /**
     * Create a new instance with resolver and a provided SpecFactory (used by Spindle).
     * 
     * @deprecated to be removed in release 3.2
     */
    public SpecificationParser(ClassResolver resolver, SpecFactory factory)
    {
        this(new DefaultErrorHandler(), LogFactory.getLog(SpecificationParser.class), resolver,
                factory);
    }

    /**
     * The full constructor, used within Tapestry.
     */
    public SpecificationParser(ErrorHandler errorHandler, Log log, ClassResolver resolver,
            SpecFactory factory)
    {
        _errorHandler = errorHandler;
        _log = log;
        _resolver = resolver;
        _factory = factory;
    }

    protected void begin(String elementName, Map attributes)
    {
        _elementName = elementName;
        _attributes = attributes;

        switch (getState())
        {
            case STATE_COMPONENT_SPECIFICATION_INITIAL:

                beginComponentSpecificationInitial();
                break;

            case STATE_PAGE_SPECIFICATION_INITIAL:

                beginPageSpecificationInitial();
                break;

            case STATE_APPLICATION_SPECIFICATION_INITIAL:

                beginApplicationSpecificationInitial();
                break;

            case STATE_LIBRARY_SPECIFICATION_INITIAL:

                beginLibrarySpecificationInitial();
                break;

            case STATE_COMPONENT_SPECIFICATION:

                beginComponentSpecification();
                break;

            case STATE_PAGE_SPECIFICATION:

                beginPageSpecification();
                break;

            case STATE_ALLOW_DESCRIPTION:

                beginAllowDescription();
                break;

            case STATE_ALLOW_PROPERTY:

                allowMetaData();
                break;

            case STATE_BEAN:

                beginBean();
                break;

            case STATE_COMPONENT:

                beginComponent();
                break;

            case STATE_LIBRARY_SPECIFICATION:

                beginLibrarySpecification();
                break;

            case STATE_EXTENSION:

                beginExtension();
                break;

            default:

                unexpectedElement(_elementName);
        }
    }

    /**
     * Special state for a number of specification types that can support the &lt;description&gt;
     * element.
     */

    private void beginAllowDescription()
    {
        if (_elementName.equals("description"))
        {
            enterDescription();
            return;
        }

        unexpectedElement(_elementName);
    }

    /**
     * Special state for a number of elements that can support the nested &lt;meta&gt; meta data
     * element (&lt;property&gt; in 3.0 DTD).
     */

    private void allowMetaData()
    {
        if (_DTD_3_1)
        {
            if (_elementName.equals("meta"))
            {
                enterMeta();
                return;
            }
        }
        else if (_elementName.equals("property"))
        {
            enterProperty_3_0();
            return;
        }

        unexpectedElement(_elementName);
    }

    private void beginApplicationSpecificationInitial()
    {
        expectElement("application");

        String name = getAttribute("name");
        String engineClassName = getAttribute("engine-class");

        IApplicationSpecification as = _factory.createApplicationSpecification();

        as.setName(name);

        if (Tapestry.isNonBlank(engineClassName))
            as.setEngineClassName(engineClassName);

        _rootObject = as;

        push(_elementName, as, STATE_LIBRARY_SPECIFICATION);
    }

    private void beginBean()
    {
        if (_elementName.equals("set-property"))
        {
            enterSetProperty();
            return;
        }

        if (_elementName.equals("set-message-property"))
        {
            enterSetMessage();
            return;
        }

        if (_elementName.equals("description"))
        {
            enterDescription();
            return;
        }

        allowMetaData();
    }

    private void beginComponent()
    {
        if (_elementName.equals("binding"))
        {
            enterBinding();
            return;
        }

        // 3.0 DTD only
        if (_elementName.equals("static-binding"))
        {
            enterStaticBinding();
            return;
        }

        // 3.0 DTD only
        if (_elementName.equals("message-binding"))
        {
            enterMessageBinding();
            return;
        }

        // 3.0 DTD only
        if (_elementName.equals("inherited-binding"))
        {
            enterInheritedBinding();
            return;
        }

        if (_elementName.equals("listener-binding"))
        {
            enterListenerBinding();
            return;
        }

        allowMetaData();
    }

    private void beginComponentSpecification()
    {
        if (_elementName.equals("reserved-parameter"))
        {
            enterReservedParameter();
            return;
        }

        if (_elementName.equals("parameter"))
        {
            enterParameter();
            return;
        }

        // The remainder are common to both <component-specification> and
        // <page-specification>

        beginPageSpecification();
    }

    private void beginComponentSpecificationInitial()
    {
        expectElement("component-specification");

        IComponentSpecification cs = _factory.createComponentSpecification();

        cs.setAllowBody(getBooleanAttribute("allow-body", true));
        cs.setAllowInformalParameters(getBooleanAttribute("allow-informal-parameters", true));

        String className = getAttribute("class");

        if (className != null)
            cs.setComponentClassName(className);

        cs.setSpecificationLocation(getResource());

        _rootObject = cs;

        push(_elementName, cs, STATE_COMPONENT_SPECIFICATION);
    }

    private void beginExtension()
    {
        if (_elementName.equals("configure"))
        {
            enterConfigure();
            return;
        }

        allowMetaData();
    }

    private void beginLibrarySpecification()
    {
        if (_elementName.equals("description"))
        {
            enterDescription();
            return;
        }

        if (_elementName.equals("page"))
        {
            enterPage();
            return;
        }

        if (_elementName.equals("component-type"))
        {
            enterComponentType();
            return;
        }

        // Holdover from the 3.0 DTD, now ignored.

        if (_elementName.equals("service"))
        {
            enterService();
            return;
        }

        if (_elementName.equals("library"))
        {
            enterLibrary();
            return;
        }

        if (_elementName.equals("extension"))
        {
            enterExtension();
            return;
        }

        allowMetaData();
    }

    private void beginLibrarySpecificationInitial()
    {
        expectElement("library-specification");

        ILibrarySpecification ls = _factory.createLibrarySpecification();

        _rootObject = ls;

        push(_elementName, ls, STATE_LIBRARY_SPECIFICATION);
    }

    private void beginPageSpecification()
    {
        if (_elementName.equals("component"))
        {
            enterComponent();
            return;
        }

        if (_elementName.equals("bean"))
        {
            enterBean();
            return;
        }

        if (_elementName.equals("property-specification"))
        {
            enterPropertySpecification();
            return;
        }

        if (_elementName.equals("context-asset"))
        {
            enterContextAsset();
            return;
        }

        if (_elementName.equals("private-asset"))
        {
            enterPrivateAsset();
            return;
        }

        if (_elementName.equals("external-asset"))
        {
            enterExternalAsset();
            return;

        }

        if (_elementName.equals("description"))
        {
            enterDescription();
            return;
        }

        allowMetaData();
    }

    private void beginPageSpecificationInitial()
    {
        expectElement("page-specification");

        IComponentSpecification cs = _factory.createComponentSpecification();

        String className = getAttribute("class");

        if (className != null)
            cs.setComponentClassName(className);

        cs.setSpecificationLocation(getResource());
        cs.setPageSpecification(true);

        _rootObject = cs;

        push(_elementName, cs, STATE_PAGE_SPECIFICATION);
    }

    /**
     * Close a stream (if not null), ignoring any errors.
     */
    private void close(InputStream stream)
    {
        try
        {
            if (stream != null)
                stream.close();
        }
        catch (IOException ex)
        {
            // ignore
        }
    }

    private void copyBindings(String sourceComponentId, IComponentSpecification cs,
            IContainedComponent target)
    {
        IContainedComponent source = cs.getComponent(sourceComponentId);
        if (source == null)
            throw new DocumentParseException(ParseMessages.unableToCopy(sourceComponentId),
                    getLocation(), null);

        Iterator i = source.getBindingNames().iterator();
        while (i.hasNext())
        {
            String bindingName = (String) i.next();
            IBindingSpecification binding = source.getBinding(bindingName);
            target.setBinding(bindingName, binding);
        }

        target.setType(source.getType());
    }

    protected void end(String elementName)
    {
        _elementName = elementName;

        switch (getState())
        {
            case STATE_DESCRIPTION:

                endDescription();
                break;

            case STATE_META:

                endProperty();
                break;

            case STATE_SET_PROPERTY:

                endSetProperty();
                break;

            case STATE_BINDING_3_0:

                endBinding_3_0();
                break;

            case STATE_BINDING:

                endBinding();
                break;

            case STATE_LISTENER_BINDING:

                endListenerBinding();
                break;

            case STATE_STATIC_BINDING:

                endStaticBinding();
                break;

            case STATE_PROPERTY_SPECIFICATION:

                endPropertySpecification();
                break;

            case STATE_LIBRARY_SPECIFICATION:

                endLibrarySpecification();
                break;

            case STATE_CONFIGURE:

                endConfigure();
                break;

            default:
                break;
        }

        // Pop the top element of the stack and continue processing from there.

        pop();
    }

    private void endBinding_3_0()
    {
        BindingSetter bs = (BindingSetter) peekObject();

        String expression = getExtendedValue(bs.getValue(), "expression", true);

        IBindingSpecification spec = _factory.createBindingSpecification();

        spec.setType(BindingType.PREFIXED);
        spec.setValue("ognl:" + expression);

        bs.apply(spec);
    }

    private void endConfigure()
    {
        ExtensionConfigurationSetter setter = (ExtensionConfigurationSetter) peekObject();

        String finalValue = getExtendedValue(setter.getValue(), "value", true);

        setter.apply(finalValue);
    }

    private void endDescription()
    {
        DescriptionSetter setter = (DescriptionSetter) peekObject();

        String description = peekContent();

        setter.apply(description);
    }

    private void endLibrarySpecification()
    {
        ILibrarySpecification spec = (ILibrarySpecification) peekObject();

        spec.setResourceResolver(_resolver);
        spec.setSpecificationLocation(getResource());

        spec.instantiateImmediateExtensions();
    }

    private void endListenerBinding()
    {
        BindingSetter bs = (BindingSetter) peekObject();

        IListenerBindingSpecification lbs = _factory.createListenerBindingSpecification();

        lbs.setLanguage(bs.getValue());

        // Do we need a check for no body content?

        lbs.setValue(peekContent());
        lbs.setLocation(getLocation());

        bs.apply(lbs);
    }

    private void endProperty()
    {
        PropertyValueSetter pvs = (PropertyValueSetter) peekObject();

        String finalValue = getExtendedValue(pvs.getPropertyValue(), "value", true);

        pvs.applyValue(finalValue);
    }

    private void endPropertySpecification()
    {
        IPropertySpecification ps = (IPropertySpecification) peekObject();

        String initialValue = getExtendedValue(ps.getInitialValue(), "initial-value", false);

        ps.setInitialValue(initialValue);
    }

    private void endSetProperty()
    {
        BeanSetPropertySetter bs = (BeanSetPropertySetter) peekObject();

        String finalValue = getExtendedValue(bs.getExpression(), "expression", true);

        bs.applyExpression(finalValue);
    }

    private void endStaticBinding()
    {
        BindingSetter bs = (BindingSetter) peekObject();

        String literalValue = getExtendedValue(bs.getValue(), "value", true);

        IBindingSpecification spec = _factory.createBindingSpecification();

        spec.setType(BindingType.PREFIXED);
        spec.setValue("literal:" + literalValue);

        bs.apply(spec);
    }

    private void enterAsset(String pathAttributeName, AssetType type)
    {
        String name = getValidatedAttribute("name", ASSET_NAME_PATTERN, "invalid-asset-name");
        String path = getAttribute(pathAttributeName);

        IAssetSpecification ia = _factory.createAssetSpecification();

        ia.setType(type);
        ia.setPath(path);

        IComponentSpecification cs = (IComponentSpecification) peekObject();

        cs.addAsset(name, ia);

        push(_elementName, ia, STATE_ALLOW_PROPERTY);
    }

    private void enterBean()
    {
        String name = getValidatedAttribute("name", BEAN_NAME_PATTERN, "invalid-bean-name");
        String className = getAttribute("class");
        BeanLifecycle lifecycle = (BeanLifecycle) getConvertedAttribute(
                "lifecycle",
                BeanLifecycle.REQUEST);

        IBeanSpecification bs = _factory.createBeanSpecification();

        bs.setClassName(className);
        bs.setLifecycle(lifecycle);

        IComponentSpecification cs = (IComponentSpecification) peekObject();

        cs.addBeanSpecification(name, bs);

        push(_elementName, bs, STATE_BEAN);
    }

    private void enterBinding()
    {
        if (!_DTD_3_1)
        {
            enterBinding_3_0();
            return;
        }

        // 3.1 stuff

        String name = getValidatedAttribute(
                "name",
                PARAMETER_NAME_PATTERN,
                "invalid-parameter-name");
        String value = getAttribute("value");

        IContainedComponent cc = (IContainedComponent) peekObject();

        BindingSetter bs = new BindingSetter(cc, name, value);

        push(_elementName, bs, STATE_BINDING, false);
    }

    private void endBinding()
    {
        BindingSetter bs = (BindingSetter) peekObject();

        String value = getExtendedValue(bs.getValue(), "value", true);

        IBindingSpecification spec = _factory.createBindingSpecification();

        spec.setType(BindingType.PREFIXED);
        spec.setValue(value);

        bs.apply(spec);
    }

    /**
     * Handles a binding in a 3.0 DTD.
     */

    private void enterBinding_3_0()
    {
        String name = getAttribute("name");
        String expression = getAttribute("expression");

        IContainedComponent cc = (IContainedComponent) peekObject();

        BindingSetter bs = new BindingSetter(cc, name, expression);

        push(_elementName, bs, STATE_BINDING_3_0, false);
    }

    private void enterComponent()
    {
        String id = getValidatedAttribute("id", COMPONENT_ID_PATTERN, "invalid-component-id");

        String type = getValidatedAttribute(
                "type",
                COMPONENT_TYPE_PATTERN,
                "invalid-component-type");
        String copyOf = getAttribute("copy-of");
        boolean inherit = getBooleanAttribute("inherit-informal-parameters", false);

        // Check that either copy-of or type, but not both

        boolean hasCopyOf = Tapestry.isNonBlank(copyOf);

        if (hasCopyOf)
        {
            if (Tapestry.isNonBlank(type))
                throw new DocumentParseException(ParseMessages.bothTypeAndCopyOf(id),
                        getLocation(), null);
        }
        else
        {
            if (Tapestry.isBlank(type))
                throw new DocumentParseException(ParseMessages.missingTypeOrCopyOf(id),
                        getLocation(), null);
        }

        IContainedComponent cc = _factory.createContainedComponent();
        cc.setType(type);
        cc.setCopyOf(copyOf);
        cc.setInheritInformalParameters(inherit);

        IComponentSpecification cs = (IComponentSpecification) peekObject();

        cs.addComponent(id, cc);

        if (hasCopyOf)
            copyBindings(copyOf, cs, cc);

        push(_elementName, cc, STATE_COMPONENT);
    }

    private void enterComponentType()
    {
        String type = getValidatedAttribute(
                "type",
                COMPONENT_ALIAS_PATTERN,
                "invalid-component-type");
        String path = getAttribute("specification-path");

        ILibrarySpecification ls = (ILibrarySpecification) peekObject();

        ls.setComponentSpecificationPath(type, path);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterConfigure()
    {
        String propertyName = getValidatedAttribute(
                "property-name",
                PROPERTY_NAME_PATTERN,
                "invalid-property-name");

        String value = getAttribute("value");

        IExtensionSpecification es = (IExtensionSpecification) peekObject();

        ExtensionConfigurationSetter setter = new ExtensionConfigurationSetter(es, propertyName,
                value);

        push(_elementName, setter, STATE_CONFIGURE, false);
    }

    private void enterContextAsset()
    {
        enterAsset("path", AssetType.CONTEXT);
    }

    private void enterDescription()
    {
        push(_elementName, new DescriptionSetter(peekObject()), STATE_DESCRIPTION, false);
    }

    private void enterExtension()
    {
        String name = getValidatedAttribute(
                "name",
                EXTENSION_NAME_PATTERN,
                "invalid-extension-name");

        boolean immediate = getBooleanAttribute("immediate", false);
        String className = getAttribute("class");

        IExtensionSpecification es = _factory.createExtensionSpecification();

        es.setClassName(className);
        es.setImmediate(immediate);

        ILibrarySpecification ls = (ILibrarySpecification) peekObject();

        ls.addExtensionSpecification(name, es);

        push(_elementName, es, STATE_EXTENSION);
    }

    private void enterExternalAsset()
    {
        enterAsset("URL", AssetType.EXTERNAL);
    }

    private void enterInheritedBinding()
    {
        String name = getAttribute("name");
        String parameterName = getAttribute("parameter-name");

        IBindingSpecification bs = _factory.createBindingSpecification();
        bs.setType(BindingType.INHERITED);
        bs.setValue(parameterName);

        IContainedComponent cc = (IContainedComponent) peekObject();

        cc.setBinding(name, bs);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterLibrary()
    {
        String libraryId = getValidatedAttribute("id", LIBRARY_ID_PATTERN, "invalid-library-id");
        String path = getAttribute("specification-path");

        if (libraryId.equals(INamespace.FRAMEWORK_NAMESPACE))
            throw new DocumentParseException(ParseMessages
                    .frameworkLibraryIdIsReserved(INamespace.FRAMEWORK_NAMESPACE), getLocation(),
                    null);

        ILibrarySpecification ls = (ILibrarySpecification) peekObject();

        ls.setLibrarySpecificationPath(libraryId, path);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterListenerBinding()
    {
        String name = getAttribute("name");
        String language = getAttribute("language");

        IContainedComponent cc = (IContainedComponent) peekObject();
        BindingSetter bs = new BindingSetter(cc, name, language);

        push(_elementName, bs, STATE_LISTENER_BINDING, false);
    }

    private void enterMessageBinding()
    {
        String name = getAttribute("name");
        String key = getAttribute("key");

        IBindingSpecification bs = _factory.createBindingSpecification();
        bs.setType(BindingType.PREFIXED);
        bs.setValue("message:" + key);
        bs.setLocation(getLocation());

        IContainedComponent cc = (IContainedComponent) peekObject();

        cc.setBinding(name, bs);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterPage()
    {
        String name = getValidatedAttribute("name", PAGE_NAME_PATTERN, "invalid-page-name");
        String path = getAttribute("specification-path");

        ILibrarySpecification ls = (ILibrarySpecification) peekObject();

        ls.setPageSpecificationPath(name, path);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterParameter()
    {
        IParameterSpecification ps = _factory.createParameterSpecification();

        String name = getValidatedAttribute(
                "name",
                PARAMETER_NAME_PATTERN,
                "invalid-parameter-name");

        String propertyName = getValidatedAttribute(
                "property-name",
                PROPERTY_NAME_PATTERN,
                "invalid-property-name");

        if (propertyName == null)
            propertyName = name;

        ps.setPropertyName(propertyName);

        ps.setRequired(getBooleanAttribute("required", false));
        ps.setDefaultValue(getAttribute("default-value"));
        ps.setDirection((Direction) getConvertedAttribute("direction", Direction.CUSTOM));

        String type = getAttribute("type"); // Current, 3.0+ DTD

        if (type != null)
            ps.setType(type);

        IComponentSpecification cs = (IComponentSpecification) peekObject();

        cs.addParameter(name, ps);

        push(_elementName, ps, STATE_ALLOW_DESCRIPTION);
    }

    private void enterPrivateAsset()
    {
        enterAsset("resource-path", AssetType.PRIVATE);
    }

    private void enterMeta()
    {
        String key = getAttribute("key");
        String value = getAttribute("value");

        // Value may be null, in which case the value is set from the element content

        IPropertyHolder ph = (IPropertyHolder) peekObject();

        push(_elementName, new PropertyValueSetter(ph, key, value), STATE_META, false);
    }

    private void enterProperty_3_0()
    {
        String name = getAttribute("name");
        String value = getAttribute("value");

        // Value may be null, in which case the value is set from the element content

        IPropertyHolder ph = (IPropertyHolder) peekObject();

        push(_elementName, new PropertyValueSetter(ph, name, value), STATE_META, false);
    }

    private void enterPropertySpecification()
    {
        String name = getValidatedAttribute("name", PROPERTY_NAME_PATTERN, "invalid-property-name");
        String type = getAttribute("type");
        boolean persistent = getBooleanAttribute("persistent", false);
        String initialValue = getAttribute("initial-value");

        IPropertySpecification ps = _factory.createPropertySpecification();
        ps.setName(name);

        if (Tapestry.isNonBlank(type))
            ps.setType(type);

        ps.setPersistent(persistent);
        ps.setInitialValue(initialValue);

        IComponentSpecification cs = (IComponentSpecification) peekObject();
        cs.addPropertySpecification(ps);

        push(_elementName, ps, STATE_PROPERTY_SPECIFICATION, false);
    }

    private void enterReservedParameter()
    {
        String name = getAttribute("name");
        IComponentSpecification cs = (IComponentSpecification) peekObject();

        cs.addReservedParameterName(name);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterService()
    {
        _errorHandler.error(_log, ParseMessages.serviceElementNotSupported(), getLocation(), null);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterSetMessage()
    {
        String name = getAttribute("name");
        String key = getAttribute("key");

        IBeanInitializer bi = _factory.createMessageBeanInitializer();

        PropertyUtils.write(bi, "propertyName", name);
        PropertyUtils.write(bi, "key", key);

        bi.setLocation(getLocation());

        IBeanSpecification bs = (IBeanSpecification) peekObject();

        bs.addInitializer(bi);

        push(_elementName, null, STATE_NO_CONTENT);
    }

    private void enterSetProperty()
    {
        String name = getAttribute("name");
        String expression = getAttribute("expression");

        IBeanInitializer bi = _factory.createExpressionBeanInitializer();

        PropertyUtils.write(bi, "propertyName", name);

        IBeanSpecification bs = (IBeanSpecification) peekObject();

        push(_elementName, new BeanSetPropertySetter(bs, bi, expression), STATE_SET_PROPERTY, false);
    }

    private void enterStaticBinding()
    {
        String name = getAttribute("name");
        String expression = getAttribute("value");

        IContainedComponent cc = (IContainedComponent) peekObject();

        BindingSetter bs = new BindingSetter(cc, name, expression);

        push(_elementName, bs, STATE_STATIC_BINDING, false);
    }

    private void expectElement(String elementName)
    {
        if (_elementName.equals(elementName))
            return;

        throw new DocumentParseException(ParseMessages.incorrectDocumentType(
                _elementName,
                elementName), getLocation(), null);

    }

    private String getAttribute(String name)
    {
        return (String) _attributes.get(name);
    }

    private boolean getBooleanAttribute(String name, boolean defaultValue)
    {
        String value = getAttribute(name);

        if (value == null)
            return defaultValue;

        return value.equals("yes");
    }

    private Object getConvertedAttribute(String name, Object defaultValue)
    {
        String key = getAttribute(name);

        if (key == null)
            return defaultValue;

        return CONVERSION_MAP.get(key);
    }

    private InputSource getDTDInputSource(String name)
    {
        InputStream stream = getClass().getResourceAsStream(name);

        return new InputSource(stream);
    }

    private String getExtendedValue(String attributeValue, String attributeName, boolean required)
    {
        String contentValue = peekContent();

        boolean asAttribute = Tapestry.isNonBlank(attributeValue);
        boolean asContent = Tapestry.isNonBlank(contentValue);

        if (asAttribute && asContent)
        {
            throw new DocumentParseException(ParseMessages.noAttributeAndBody(
                    attributeName,
                    _elementName), getLocation(), null);
        }

        if (required && !(asAttribute || asContent))
        {
            throw new DocumentParseException(ParseMessages.requiredExtendedAttribute(
                    _elementName,
                    attributeName), getLocation(), null);
        }

        if (asAttribute)
            return attributeValue;

        return contentValue;
    }

    private String getValidatedAttribute(String name, String pattern, String errorKey)
    {
        String value = getAttribute(name);

        if (value == null)
            return null;

        if (_matcher.matches(pattern, value))
            return value;

        throw new InvalidStringException(ParseMessages.invalidAttribute(errorKey, value), value,
                getLocation());
    }

    protected void initializeParser(Resource resource, int startState)
    {
        super.initializeParser(resource, startState);

        _rootObject = null;
        _attributes = new HashMap();
    }

    public IApplicationSpecification parseApplicationSpecification(Resource resource)
    {
        initializeParser(resource, STATE_APPLICATION_SPECIFICATION_INITIAL);

        try
        {
            parseDocument();

            return (IApplicationSpecification) _rootObject;
        }
        finally
        {
            resetParser();
        }
    }

    public IComponentSpecification parseComponentSpecification(Resource resource)
    {
        initializeParser(resource, STATE_COMPONENT_SPECIFICATION_INITIAL);

        try
        {
            parseDocument();

            return (IComponentSpecification) _rootObject;
        }
        finally
        {
            resetParser();
        }
    }

    private void parseDocument()
    {
        InputStream stream = null;

        Resource resource = getResource();

        boolean success = false;

        try
        {
            if (_parser == null)
                _parser = _parserFactory.newSAXParser();

            URL resourceURL = resource.getResourceURL();

            if (resourceURL == null)
                throw new DocumentParseException(ParseMessages.missingResource(resource), resource);

            InputStream rawStream = resourceURL.openStream();
            stream = new BufferedInputStream(rawStream);

            _parser.parse(stream, this, resourceURL.toExternalForm());

            stream.close();
            stream = null;

            success = true;
        }
        catch (Exception ex)
        {
            _parser = null;

            throw new DocumentParseException(ParseMessages.errorReadingResource(resource, ex),
                    resource, ex);
        }
        finally
        {
            if (!success)
                _parser = null;

            close(stream);
        }
    }

    public ILibrarySpecification parseLibrarySpecification(Resource resource)
    {
        initializeParser(resource, STATE_LIBRARY_SPECIFICATION_INITIAL);

        try
        {
            parseDocument();

            return (ILibrarySpecification) _rootObject;
        }
        finally
        {
            resetParser();
        }
    }

    public IComponentSpecification parsePageSpecification(Resource resource)
    {
        initializeParser(resource, STATE_PAGE_SPECIFICATION_INITIAL);

        try
        {
            parseDocument();

            return (IComponentSpecification) _rootObject;
        }
        finally
        {
            resetParser();
        }
    }

    protected String peekContent()
    {
        String content = super.peekContent();

        if (content == null)
            return null;

        return content.trim();
    }

    protected void resetParser()
    {
        _rootObject = null;
        _DTD_3_1 = false;

        _attributes.clear();
    }

    /**
     * Resolved an external entity, which is assumed to be the doctype. Might need a check to ensure
     * that specs without a doctype fail.
     */
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException
    {
        if (TAPESTRY_DTD_3_1_PUBLIC_ID.equals(publicId))
        {
            _DTD_3_1 = true;
            return getDTDInputSource("Tapestry_3_1.dtd");
        }

        if (TAPESTRY_DTD_3_0_PUBLIC_ID.equals(publicId))
            return getDTDInputSource("Tapestry_3_0.dtd");

        throw new DocumentParseException(ParseMessages.unknownPublicId(getResource(), publicId),
                getResource());
    }
}