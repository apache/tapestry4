//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.AssetSpecification;
import net.sf.tapestry.spec.AssetType;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;
import net.sf.tapestry.spec.Direction;
import net.sf.tapestry.spec.ExtensionSpecification;
import net.sf.tapestry.spec.IApplicationSpecification;
import net.sf.tapestry.spec.ILibrarySpecification;
import net.sf.tapestry.spec.LibrarySpecification;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.spec.SpecFactory;
import net.sf.tapestry.util.IPropertyHolder;
import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 *  Used to parse an application or component specification into a
 *  {@link ApplicationSpecification} or {@link ComponentSpecification}.
 *  This may someday be revised to use
 *  Java XML Binding (once JAXB is available).
 *
 *  <p>This class supports the 1.1 DTD (introduced in release 1.0.1)
 *  as well as the "old" 1.0 DTD.
 *
 *  <table border=1
 *	<tr>
 *	  <th>Version</th> <th>PUBLIC ID</th> <th>SYSTEM ID</th> <th>Description</th>
 *  </tr>

 *  <tr valign=top>
 *    <td>1.1</th>
 *    <td><code>-//Howard Ship//Tapestry Specification 1.1//EN</code>
 *    <td><code>http://tapestry.sf.net/dtd/Tapestry_1_1.dtd</code></td>
 *   <td>Streamlined version of (now defunct) 1.0 (makes use of XML attributes
 *  instead of nested elements), also:
 *  <ul>
 *  <li>Adds &lt;description&gt; element
 *  <li>Adds copy-of attribute to &lt;component&gt;
 *  <li>Support for helper beans
 *  </ul>
 *  </td>
 *  </tr>
 * <tr valign="top">
 * 	<td>1.2</td>
 *  <td><code>-//Howard Lewis Ship//Tapestry Specification 1.2//EN</code></td>
 * <td><code>http://tapestry.sf.net/dtd/Tapestry_1_2.dtd</code></td>
 *  <td>Adds property-name attribute to element &lt;parameter&gt;
 * </td>
 * </tr>
 * 
 *  <tr valign="top">
 *  <td>1.3</td>
 *  <td><code>-//Howard Lewis Ship//Tapestry Specification 1.3//EN</code></td>
 * <td><code>http://tapestry.sf.net/dtd/Tapestry_1_3.dtd</code></td>
 *  <td>
 *   <ul>
 *      <li>Splits the &lt;specification&gt; element into &lt;component-specification&gt;
 *  and &lt;page-specification&gt; (where &lt;page-specification&gt; doesn't allow
 *  attributes and elements related to declaring parameters).
 *      <li>Adds the &lt;library&gt; and &lt;extension&gt; elements
 *      <li>Adds the &lt;library-specification&gt; root element
 *      <li>Adds &lt;property&gt; to many other elements
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

public class SpecificationParser extends AbstractDocumentParser
{

    public static final String TAPESTRY_DTD_1_1_PUBLIC_ID = "-//Howard Ship//Tapestry Specification 1.1//EN";
    public static final String TAPESTRY_DTD_1_2_PUBLIC_ID = "-//Howard Lewis Ship//Tapestry Specification 1.2//EN";

    /** @since 2.2 **/

    public static final String TAPESTRY_DTD_1_3_PUBLIC_ID = "-//Howard Lewis Ship//Tapestry Specification 1.3//EN";

    /**
     *  Simple property names match Java variable names; a leading letter
     *  (or underscore), followed by letters, numbers and underscores.
     * 
     *  @since 2.2
     * 
     **/

    private static final String SIMPLE_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z]\\w*$";


    /**
     *  Like modified property name, but allows periods in the name as
     *  well.
     * 
     *  @since 2.2
     * 
     **/

    private static final String EXTENDED_PROPERTY_NAME_PATTERN = "^_?[a-zA-Z](\\w|-|\\.)*$";

    /**
     *  Perl5 pattern that parameter names must conform to.  
     *  Letter, followed by letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String PARAMETER_NAME_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern that property names (that can be connected to
     *  parameters) must conform to.  
     *  Letter, followed by letter, number or underscore.
     *  
     * 
     *  @since 2.2
     * 
     **/

    public static final String PROPERTY_NAME_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

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
     * 
     *  @since 2.2
     * 
     **/

    public static final String COMPONENT_ALIAS_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for helper bean names.  
     *  Letter, followed by letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String BEAN_NAME_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for component ids.  Letter, followed by
     *  letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String COMPONENT_ID_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

    /**
     *  Perl5 pattern for asset names.  Letter, followed by
     *  letter, number or underscore.
     * 
     *  @since 2.2
     * 
     **/

    public static final String ASSET_NAME_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

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

    public static final String LIBRARY_ID_PATTERN = SIMPLE_PROPERTY_NAME_PATTERN;

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
     *  Flag set at the start of the parse to indicate that it is a version 3 (i.e. 1.3)
     *  or better input document.  Some validations and rules trigger off of that.
     * 
     *  @since 2.2
     * 
     **/

    private boolean _version3 = false;

    /**
     *  We can share a single map for all the XML attribute to object conversions,
     *  since the keys are unique.
     * 
     **/

    private static final Map _conversionMap = new HashMap();

    /** @since 1.0.9 **/

    private SpecFactory _factory;

    /** 
     * 
     *  Compiler used to convert pattern strings into {@link Pattern}
     *  instances.
     * 
     *  @since 2.2 
     * 
     **/

    private PatternCompiler _patternCompiler;

    /** 
     * 
     *  Matcher used to match patterns against input strings.
     * 
     *  @since 2.2 
     * 
     **/

    private PatternMatcher _matcher;

    /** 
     * 
     *  Map of compiled {@link Pattern}s, keyed on pattern
     *  string.  Patterns are lazily compiled as needed.
     * 
     *  @since 2.2 
     * 
     **/

    private Map _compiledPatterns;

    private interface IConverter
    {
        public Object convert(String value) throws DocumentParseException;
    }

    private static class BooleanConverter implements IConverter
    
    {
        public Object convert(String value) throws DocumentParseException
        {
            Object result = _conversionMap.get(value.toLowerCase());

            if (result == null || !(result instanceof Boolean))
                throw new DocumentParseException(Tapestry.getString("SpecificationParser.fail-convert-boolean", value));

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
                throw new DocumentParseException(Tapestry.getString("SpecificationParser.fail-convert-int", value), ex);
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
                    Tapestry.getString("SpecificationParser.fail-convert-long", value),
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
                    Tapestry.getString("SpecificationParser.fail-convert-double", value),
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

    // Identify all the different acceptible values.
    // We continue to sneak by with a single map because
    // there aren't conflicts;  when we have 'foo' meaning
    // different things in different places in the DTD, we'll
    // need two maps.

    static {

        _conversionMap.put("true", Boolean.TRUE);
        _conversionMap.put("t", Boolean.TRUE);
        _conversionMap.put("1", Boolean.TRUE);
        _conversionMap.put("y", Boolean.TRUE);
        _conversionMap.put("yes", Boolean.TRUE);
        _conversionMap.put("on", Boolean.TRUE);

        _conversionMap.put("false", Boolean.FALSE);
        _conversionMap.put("f", Boolean.FALSE);
        _conversionMap.put("0", Boolean.FALSE);
        _conversionMap.put("off", Boolean.FALSE);
        _conversionMap.put("no", Boolean.FALSE);
        _conversionMap.put("n", Boolean.FALSE);

        _conversionMap.put("none", BeanLifecycle.NONE);
        _conversionMap.put("request", BeanLifecycle.REQUEST);
        _conversionMap.put("page", BeanLifecycle.PAGE);
        _conversionMap.put("render", BeanLifecycle.RENDER);

        _conversionMap.put("boolean", new BooleanConverter());
        _conversionMap.put("int", new IntConverter());
        _conversionMap.put("double", new DoubleConverter());
        _conversionMap.put("String", new StringConverter());
        _conversionMap.put("long", new LongConverter());

        _conversionMap.put("in", Direction.IN);
        _conversionMap.put("form", Direction.FORM);
        _conversionMap.put("custom", Direction.CUSTOM);
    }

    public SpecificationParser()
    {
        register(TAPESTRY_DTD_1_1_PUBLIC_ID, "Tapestry_1_1.dtd");
        register(TAPESTRY_DTD_1_2_PUBLIC_ID, "Tapestry_1_2.dtd");
        register(TAPESTRY_DTD_1_3_PUBLIC_ID, "Tapestry_1_3.dtd");
        _factory = new SpecFactory();
    }

    /**
     *  Parses an input stream containing a page or component specification and assembles
     *  a {@link ComponentSpecification} from it.  For the 1.3 DTD, the
     *  root element must be "component-specification" (for earlier DTDs, "specification"
     *  is used).
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public ComponentSpecification parseComponentSpecification(InputStream input, String resourcePath)
        throws DocumentParseException
    {
        Document document;

        try
        {
            document = parse(new InputSource(input), resourcePath, null);

            _version3 = checkVersion3(document);

            String rootElementName = _version3 ? "component-specification" : "specification";

            validateRootElement(document, rootElementName, resourcePath);

            return convertComponentSpecification(document, false);
        }
        finally
        {
            setResourcePath(null);
        }
    }

    /**
     *  Parses an input stream containing a page specification and assembles
     *  a {@link ComponentSpecification} from it.  For the 1.3 DTD, the
     *  root element must be "page-specification" (for earlier DTDs, "specification"
     *  is used).
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     * 
     *  @since 2.2
     *
     **/

    public ComponentSpecification parsePageSpecification(InputStream input, String resourcePath)
        throws DocumentParseException
    {
        try
        {
            Document document = parse(new InputSource(input), resourcePath, null);

            _version3 = checkVersion3(document);

            String rootElementName = _version3 ? "page-specification" : "specification";

            validateRootElement(document, rootElementName, resourcePath);

            ComponentSpecification result = convertComponentSpecification(document, true);

            result.setAllowBody(true);
            result.setAllowInformalParameters(false);
            result.setPageSpecification(true);

            return result;
        }
        finally
        {
            setResourcePath(null);
        }
    }

    /**
     *  Parses an input stream containing an application specification and assembles
     *  an {@link ApplicationSpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public IApplicationSpecification parseApplicationSpecification(
        InputStream input,
        String resourcePath,
        IResourceResolver resolver)
        throws DocumentParseException
    {
        Document document;

        try
        {
            document = parse(new InputSource(input), resourcePath, "application");

            _version3 = checkVersion3(document);

            return convertApplicationSpecification(document, resolver);
        }
        finally
        {
            setResourcePath(null);
        }
    }

    /**
     *  Parses an input stream containing a library specification and assembles
     *  a {@link LibrarySpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     * 
     *  @since 2.2
     *
     **/

    public ILibrarySpecification parseLibrarySpecification(
        InputStream input,
        String resourcePath,
        IResourceResolver resolver)
        throws DocumentParseException
    {
        Document document;

        try
        {
            document = parse(new InputSource(input), resourcePath, "library-specification");

            _version3 = true;

            return convertLibrarySpecification(document, resolver);
        }
        finally
        {
            setResourcePath(null);
        }
    }

    private boolean getBooleanValue(Node node) throws DocumentParseException
    {
        String key = getValue(node).toLowerCase();

        Boolean value = (Boolean) _conversionMap.get(key);

        if (value == null)
            throw new DocumentParseException(
                Tapestry.getString(
                    "SpecificationParser.unable-to-convert-node-to-boolean",
                    key,
                    getNodePath(node.getParentNode())),
                getResourcePath());

        return value.booleanValue();
    }

    private boolean getBooleanAttribute(Node node, String attributeName)
    {
        String attributeValue = getAttribute(node, attributeName);

        return attributeValue.equals("yes");
    }

    private IApplicationSpecification convertApplicationSpecification(Document document, IResourceResolver resolver)
        throws DocumentParseException
    {
        IApplicationSpecification specification = _factory.createApplicationSpecification();

        Element root = document.getDocumentElement();

        specification.setName(getAttribute(root, "name"));
        specification.setEngineClassName(getAttribute(root, "engine-class"));

        processLibrarySpecification(document, specification, resolver);

        return specification;
    }

    /** @since 2.2 **/

    private ILibrarySpecification convertLibrarySpecification(Document document, IResourceResolver resolver)
        throws DocumentParseException
    {
        ILibrarySpecification specification = _factory.createLibrarySpecification();

        processLibrarySpecification(document, specification, resolver);

        return specification;
    }

    /**
     *  Processes the embedded elements inside a LibrarySpecification.
     * 
     *  @since 2.2
     * 
     **/

    private void processLibrarySpecification(
        Document document,
        ILibrarySpecification specification,
        IResourceResolver resolver)
        throws DocumentParseException
    {
        specification.setPublicId(document.getDoctype().getPublicId());

        specification.setResourceResolver(resolver);

        Element root = document.getDocumentElement();

        for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling())
        {
            if (isElement(node, "page"))
            {
                convertPage(specification, node);
                continue;
            }

            if (isElement(node, "component-alias"))
            {
                convertComponentAlias(specification, node);
                continue;
            }

            if (isElement(node, "property"))
            {
                convertProperty(specification, node);
                continue;
            }

            if (isElement(node, "service"))
            {
                convertService(specification, node);
                continue;
            }

            if (isElement(node, "description"))
            {
                specification.setDescription(getValue(node));
                continue;
            }

            if (isElement(node, "library"))
            {
                convertLibrary(specification, node);
                continue;
            }

            if (isElement(node, "extension"))
            {
                convertExtension(specification, node);
                continue;
            }
        }

        specification.instantiateImmediateExtensions();
    }

    /**  @since 2.2 **/

    private void convertLibrary(ILibrarySpecification specification, Node node) throws DocumentParseException
    {
        String id = getAttribute(node, "id");

        validate(id, LIBRARY_ID_PATTERN, "SpecificationParser.invalid-library-id");

        if (id.equals(INamespace.FRAMEWORK_NAMESPACE))
            throw new DocumentParseException(
                Tapestry.getString(
                    "SpecificationParser.framework-library-id-is-reserved",
                    INamespace.FRAMEWORK_NAMESPACE),
                getResourcePath());

        String specificationPath = getAttribute(node, "specification-path");

        specification.setLibrarySpecificationPath(id, specificationPath);
    }

    private void convertPage(ILibrarySpecification specification, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        validate(name, PAGE_NAME_PATTERN, "SpecificationParser.invalid-page-name");

        String specificationPath = getAttribute(node, "specification-path");

        specification.setPageSpecificationPath(name, specificationPath);
    }

    private void convertComponentAlias(ILibrarySpecification specification, Node node) throws DocumentParseException
    {
        String type = getAttribute(node, "type");

        validate(type, COMPONENT_ALIAS_PATTERN, "SpecificationParser.invalid-component-alias");

        String path = getAttribute(node, "specification-path");

        specification.setComponentSpecificationPath(type, path);
    }

    private void convertProperty(IPropertyHolder holder, Node node)
    {
        String name = getAttribute(node, "name");
        String value = getValue(node);

        holder.setProperty(name, value);
    }

    private ComponentSpecification convertComponentSpecification(Document document, boolean isPage)
        throws DocumentParseException
    {
        ComponentSpecification specification = _factory.createComponentSpecification();
        Element root = document.getDocumentElement();

        specification.setPublicId(document.getDoctype().getPublicId());

        // Only components specify these two attributes.  For pages they either can't be specified
        // (1.3 DTD) or are now ignored (1.1 and 1.2 DTD).

        if (!isPage)
        {
            specification.setAllowBody(getBooleanAttribute(root, "allow-body"));
            specification.setAllowInformalParameters(getBooleanAttribute(root, "allow-informal-parameters"));
        }

        specification.setComponentClassName(getAttribute(root, "class"));

        for (Node node = root.getFirstChild(); node != null; node = node.getNextSibling())
        {
            if (isElement(node, "parameter"))
            {
                if (isPage)
                    throw new DocumentParseException(
                        Tapestry.getString("SpecificationParser.not-allowed-for-page", "parameter"),
                        getResourcePath());

                convertParameter(specification, node);
                continue;
            }

            if (isElement(node, "reserved-parameter"))
            {
                if (isPage)
                    throw new DocumentParseException(
                        Tapestry.getString("SpecificationParser.not-allowed-for-page", "reserved-parameter"),
                        getResourcePath());

                convertReservedParameter(specification, node);
                continue;
            }

            if (isElement(node, "bean"))
            {
                convertBean(specification, node);
                continue;
            }

            if (isElement(node, "component"))
            {
                convertComponent(specification, node);
                continue;
            }

            if (isElement(node, "external-asset"))
            {
                convertAsset(specification, node, AssetType.EXTERNAL, "URL");
                continue;
            }

            if (isElement(node, "context-asset"))
            {
                convertAsset(specification, node, AssetType.CONTEXT, "path");
                continue;
            }

            if (isElement(node, "private-asset"))
            {
                convertAsset(specification, node, AssetType.PRIVATE, "resource-path");
                continue;
            }

            if (isElement(node, "property"))
            {
                convertProperty(specification, node);
                continue;
            }

            if (isElement(node, "description"))
            {
                specification.setDescription(getValue(node));
                continue;
            }
        }

        return specification;
    }

    private void convertParameter(ComponentSpecification specification, Node node) throws DocumentParseException
    {
        ParameterSpecification param = _factory.createParameterSpecification();

        String name = getAttribute(node, "name");

        validate(name, PARAMETER_NAME_PATTERN, "SpecificationParser.invalid-parameter-name");

        param.setType(getAttribute(node, "java-type"));
        param.setRequired(getBooleanAttribute(node, "required"));

        String propertyName = getAttribute(node, "property-name");

        // If not specified (or a 1.0 DTD, in which case the property-name
        // attribute doesn't exist), use the name of the parameter.

        if (propertyName == null)
        {
            propertyName = name;

            validate(propertyName, PROPERTY_NAME_PATTERN, "SpecificationParser.invalid-property-name");
        }

        param.setPropertyName(propertyName);

        String direction = getAttribute(node, "direction");

        if (direction != null)
            param.setDirection((Direction) _conversionMap.get(direction));

        specification.addParameter(name, param);

        Node child = node.getFirstChild();
        if (child != null && isElement(child, "description"))
        {
            param.setDescription(getValue(child));
        }
    }

    /**
     *  @since 1.0.4
     *
     **/

    private void convertBean(ComponentSpecification specification, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        validate(name, BEAN_NAME_PATTERN, "SpecificationParser.invalid-bean-name");

        String className = getAttribute(node, "class");
        String lifecycleString = getAttribute(node, "lifecycle");

        BeanLifecycle lifecycle = (BeanLifecycle) _conversionMap.get(lifecycleString);

        BeanSpecification bspec = _factory.createBeanSpecification(className, lifecycle);

        specification.addBeanSpecification(name, bspec);

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "description"))
            {
                bspec.setDescription(getValue(child));
                continue;
            }

            if (isElement(child, "property"))
            {
                convertProperty(bspec, child);
                continue;
            }

            if (isElement(child, "set-property"))
            {
                if (_version3)
                    convertSetProperty(bspec, child);
                else
                    convertSetProperty_2(bspec, child);
                continue;
            }
            
            if (isElement(child, "set-string-property"))
            {
                convertSetStringProperty(bspec, child);
                continue;
            }
        }
    }

    /** @since 1.0.5 **/

    private void convertSetProperty_2(BeanSpecification spec, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        // <set-property> contains either <static-value>, <field-value> or <property-value>

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "static-value"))
            {
                convertStaticValue(spec, name, child);
                continue;
            }

            if (isElement(child, "field-value"))
            {
                convertFieldValue(spec, name, child);
                continue;
            }
            
            if (isElement(child, "property-value"))
            {
                convertPropertyValue(spec, name, child);
                continue;
            }
        }
    }

    /**
     *  This is a new simplified version structured around OGNL, in the 1.3 DTD.
     * 
     *  @since 2.2
     * 
     **/
    
    
    private void convertSetProperty(BeanSpecification spec, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");
        String expression = getAttribute(node, "expression");
        
        IBeanInitializer iz = _factory.createExpressionBeanInitializer(name, expression);
        
        spec.addInitializer(iz);
    }

    /**
     *  String properties in the 1.3 DTD are handled a little differently.
     * 
     *  @since 2.2
     * 
     **/
    
    private void convertSetStringProperty(BeanSpecification spec, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");
        String key = getAttribute(node, "key");
        
        IBeanInitializer iz = _factory.createStringBeanInitializer(name, key);
        
        spec.addInitializer(iz);
    }
    

    /** @since 1.0.8 **/

    private void convertFieldValue(BeanSpecification spec, String propertyName, Node node)
    {
        String fieldName = getAttribute(node, "field-name");
        IBeanInitializer iz = _factory.createFieldBeanInitializer(propertyName, fieldName);

        spec.addInitializer(iz);
    }

    /** 
     * 
     *  For backwards compatibility to 1.2 DTD.
     * 
     *  @since 1.0.5
     * 
     **/

    private void convertPropertyValue(BeanSpecification spec, String propertyName, Node node)
    {
        String propertyPath = getAttribute(node, "property-path");
        IBeanInitializer iz = _factory.createPropertyBeanInitializer(propertyName, propertyPath);

        spec.addInitializer(iz);
    }

    /**
     *  @since 2.2
     * 
     **/
    
    private void convertExpressionValue(BeanSpecification spec, String propertyName, Node node)
    {
        String expression = getAttribute(node, "expression");
        IBeanInitializer iz = _factory.createPropertyBeanInitializer(propertyName, expression);

        spec.addInitializer(iz);
    }


    /** @since 1.0.5 **/

    private void convertStaticValue(BeanSpecification spec, String propertyName, Node node)
        throws DocumentParseException
    {
        String type = getAttribute(node, "type");
        String value = getValue(node);

        IConverter converter = (IConverter) _conversionMap.get(type);

        if (converter == null)
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.unknown-static-value-type", type),
                getResourcePath());

        Object staticValue = converter.convert(value);

        IBeanInitializer iz = _factory.createStaticBeanInitializer(propertyName, staticValue);

        spec.addInitializer(iz);
    }

    private void convertComponent(ComponentSpecification specification, Node node) throws DocumentParseException
    {
        String id = getAttribute(node, "id");

        validate(id, COMPONENT_ID_PATTERN, "SpecificationParser.invalid-component-id");

        String type = getAttribute(node, "type");
        String copyOf = getAttribute(node, "copy-of");
        ContainedComponent c;

        if (type != null && copyOf != null)
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.both-type-and-copy-of", id),
                getResourcePath());

        if (copyOf != null)
            c = copyExistingComponent(specification, copyOf);
        else
        {
            if (type == null)
                throw new DocumentParseException(
                    Tapestry.getString("SpecificationParser.missing-type-or-copy-of", id),
                    getResourcePath());

            // In prior versions, its more free-form, because you can specify the path to
            // a component as well.  In version 3, you must use an alias and define it
            // in a library.
            
            if (_version3)
                validate(type, COMPONENT_TYPE_PATTERN, "SpecificationParser.invalid-component-type");

            c = _factory.createContainedComponent();
            c.setType(type);
        }

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "binding"))
            {
                // property-path in 1.2 DTD is expression in 1.3 DTD.
                
                convertBinding(c, child, BindingType.DYNAMIC,
                _version3 ? "expression" : "property-path");
                continue;
            }

            if (isElement(child, "field-binding"))
            {
                convertBinding(c, child, BindingType.FIELD, "field-name");
                continue;
            }

            if (isElement(child, "inherited-binding"))
            {
                convertBinding(c, child, BindingType.INHERITED, "parameter-name");
                continue;
            }

            if (isElement(child, "static-binding"))
            {
                convertStaticBinding(c, child);
                continue;
            }

            // <string-binding> added in release 2.0.4

            if (isElement(child, "string-binding"))
            {
                convertBinding(c, child, BindingType.STRING, "key");
                continue;
            }

            if (isElement(child, "property"))
            {
                convertProperty(c, child);
                continue;
            }
        }

        specification.addComponent(id, c);
    }

    private void convertBinding(ContainedComponent component, Node node, BindingType type, String attributeName)
    {
        String name = getAttribute(node, "name");
        String value = getAttribute(node, attributeName);
        BindingSpecification binding = _factory.createBindingSpecification(type, value);

        component.setBinding(name, binding);
    }

    private void convertStaticBinding(ContainedComponent component, Node node)
    {
        String name = getAttribute(node, "name");
        String value = getValue(node);
        BindingSpecification binding = _factory.createBindingSpecification(BindingType.STATIC, value);

        component.setBinding(name, binding);
    }

    private ContainedComponent copyExistingComponent(ComponentSpecification spec, String id)
        throws DocumentParseException
    {
        ContainedComponent c = spec.getComponent(id);
        if (c == null)
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.unable-to-copy", id),
                getResourcePath());

        ContainedComponent result = _factory.createContainedComponent();

        result.setType(c.getType());
        result.setCopyOf(id);

        Iterator i = c.getBindingNames().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();
            BindingSpecification binding = c.getBinding(name);
            result.setBinding(name, binding);
        }

        return result;
    }

    private void convertAsset(ComponentSpecification specification, Node node, AssetType type, String attributeName)
        throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        // As a special case, allow the exact value through (even though
        // it is not, technically, a valid asset name).

        if (!name.equals(ITemplateSource.TEMPLATE_ASSET_NAME))
            validate(name, ASSET_NAME_PATTERN, "SpecificationParser.invalid-asset-name");

        String value = getAttribute(node, attributeName);
        AssetSpecification asset = _factory.createAssetSpecification(type, value);

        specification.addAsset(name, asset);

        processPropertiesInNode(asset, node);
    }

    /**
     *  Used in several places where an elements only possible children are
     *  &lt;property&gt; elements.
     * 
     **/

    private void processPropertiesInNode(IPropertyHolder holder, Node node) throws DocumentParseException
    {
        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "property"))
            {
                convertProperty(holder, child);
                continue;
            }
        }
    }

    /**
     *  @since 1.0.5
     *
     **/

    private void convertReservedParameter(ComponentSpecification spec, Node node)
    {
        String name = getAttribute(node, "name");

        spec.addReservedParameterName(name);
    }

    /**
     *  @since 1.0.9
     * 
     **/

    private void convertService(ILibrarySpecification spec, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        validate(name, SERVICE_NAME_PATTERN, "SpecificationParser.invalid-service-name");

        String className = getAttribute(node, "class");

        spec.setServiceClassName(name, className);
    }

    /**
     *  Sets the SpecFactory which instantiates Tapestry spec objects.
     * 
     *  @since 1.0.9
     **/

    public void setFactory(SpecFactory factory)
    {
        this._factory = factory;
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
     *  Validates that the input value matches against the specified
     *  Perl5 pattern.  If valid, the method simply returns.
     *  If not a match, then an error message is generated (using the
     *  errorKey and the input value) and a
     *  {@link DocumentParseException} is thrown.
     * 
     *  @since 2.2
     * 
     **/

    private void validate(String value, String pattern, String errorKey) throws DocumentParseException
    {
        if (_compiledPatterns == null)
            _compiledPatterns = new HashMap();

        Pattern compiled = (Pattern) _compiledPatterns.get(pattern);

        if (compiled == null)
        {
            compiled = compilePattern(pattern);

            _compiledPatterns.put(pattern, compiled);
        }

        if (_matcher == null)
            _matcher = new Perl5Matcher();

        if (_matcher.matches(value, compiled))
            return;

        throw new DocumentParseException(Tapestry.getString(errorKey, value), getResourcePath());
    }

    /** 
     * 
     *  Returns a pattern compiled for single line matching
     * 
     *  @since 2.2 
     * 
     **/

    private Pattern compilePattern(String pattern)
    {
        if (_patternCompiler == null)
            _patternCompiler = new Perl5Compiler();

        try
        {
            return _patternCompiler.compile(pattern, Perl5Compiler.SINGLELINE_MASK);
        }
        catch (MalformedPatternException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    /** @since 2.2 **/

    private void convertExtension(ILibrarySpecification specification, Node node) throws DocumentParseException
    {
        String name = getAttribute(node, "name");
        String className = getAttribute(node, "class");
        boolean immediate = getBooleanAttribute(node, "immediate");

        validate(name, EXTENSION_NAME_PATTERN, "SpecificationParser.invalid-extension-name");

        ExtensionSpecification exSpec = _factory.createExtensionSpecification();

        exSpec.setClassName(className);
        exSpec.setImmediate(immediate);

        specification.addExtensionSpecification(name, exSpec);

        for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if (isElement(child, "configure"))
            {
                convertConfigure(exSpec, child);
                continue;
            }

            if (isElement(child, "property"))
            {
                convertProperty(exSpec, child);
                continue;
            }
        }
    }

    /** @since 2.2 **/

    private void convertConfigure(ExtensionSpecification spec, Node node) throws DocumentParseException
    {
        String propertyName = getAttribute(node, "property-name");
        String type = getAttribute(node, "type");
        String value = getValue(node);

        validate(propertyName, PROPERTY_NAME_PATTERN, "SpecificationParser.invalid-property-name");

        IConverter converter = (IConverter) _conversionMap.get(type);

        if (converter == null)
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.unknown-static-value-type", type),
                getResourcePath());

        Object objectValue = converter.convert(value);

        spec.addConfiguration(propertyName, objectValue);
    }

    /**
     *  Returns true if the DOCTYPE for the document is the 1.3 version of
     *  the specification (or better).  This triggers some additional validation
     *  rules.  When a later version is added, the check here will need to
     *  be amended.
     *   
     *  @since 2.2
     * 
     **/

    private boolean checkVersion3(Document document)
    {
        String publicId = document.getDoctype().getPublicId();

        return publicId.equals(TAPESTRY_DTD_1_3_PUBLIC_ID);
    }

}