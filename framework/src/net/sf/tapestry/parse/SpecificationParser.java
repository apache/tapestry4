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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

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
import net.sf.tapestry.spec.PageSpecification;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.spec.SpecFactory;
import net.sf.tapestry.util.IPropertyHolder;
import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;

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
 *  </table>
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class SpecificationParser extends AbstractDocumentParser
{

    public static final String TAPESTRY_DTD_1_1_PUBLIC_ID =
        "-//Howard Ship//Tapestry Specification 1.1//EN";
    public static final String TAPESTRY_DTD_1_2_PUBLIC_ID =
        "-//Howard Lewis Ship//Tapestry Specification 1.2//EN";

    /**
     *  We can share a single map for all the XML attribute to object conversions,
     *  since the keys are unique.
     * 
     **/
    
    private static final Map conversionMap = new HashMap();
    
    /** @since 1.0.9 **/

    private SpecFactory factory;

    private interface IConverter
    {
        public Object convert(String value) throws DocumentParseException;
    }

    private static class BooleanConverter implements IConverter
    
    {
        public Object convert(String value) throws DocumentParseException
        {
            Object result = conversionMap.get(value.toLowerCase());

            if (result == null)
                throw new DocumentParseException(
                    Tapestry.getString("SpecificationParser.fail-convert-boolean", value));

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
                    Tapestry.getString("SpecificationParser.fail-convert-int", value),
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

    static {
 
        conversionMap.put("true", Boolean.TRUE);
        conversionMap.put("t", Boolean.TRUE);
        conversionMap.put("1", Boolean.TRUE);
        conversionMap.put("y", Boolean.TRUE);
        conversionMap.put("yes", Boolean.TRUE);
        conversionMap.put("on", Boolean.TRUE);

        conversionMap.put("false", Boolean.FALSE);
        conversionMap.put("f", Boolean.FALSE);
        conversionMap.put("0", Boolean.FALSE);
        conversionMap.put("off", Boolean.FALSE);
        conversionMap.put("no", Boolean.FALSE);
        conversionMap.put("n", Boolean.FALSE);

         conversionMap.put("none", BeanLifecycle.NONE);
        conversionMap.put("request", BeanLifecycle.REQUEST);
        conversionMap.put("page", BeanLifecycle.PAGE);

        conversionMap.put("boolean", new BooleanConverter());
        conversionMap.put("int", new IntConverter());
        conversionMap.put("double", new DoubleConverter());
        conversionMap.put("String", new StringConverter());
        
        conversionMap.put("in", Direction.IN);
        conversionMap.put("out", Direction.OUT);
        conversionMap.put("in-out", Direction.IN_OUT);
        conversionMap.put("custom", Direction.CUSTOM);
    }

    public SpecificationParser()
    {
        register(TAPESTRY_DTD_1_1_PUBLIC_ID, "Tapestry_1_1.dtd");
        register(TAPESTRY_DTD_1_2_PUBLIC_ID, "Tapestry_1_2.dtd");
        factory = new SpecFactory();
    }

    /**
     *  Parses an input stream containing a component specification and assembles
     *  a {@link ComponentSpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public ComponentSpecification parseComponentSpecification(
        InputStream input,
        String resourcePath)
        throws DocumentParseException
    {
        Document document;

        try
        {
            document = parse(new InputSource(input), resourcePath, "specification");

            return convertComponentSpecification(document);
        }
        finally
        {
            setResourcePath(null);
        }
    }

    /**
     *  Parses an input stream containing an application specification and assembles
     *  a {@link ApplicationSpecification} from it.
     *
     *  @throws DocumentParseException if the input stream cannot be fully
     *  parsed or contains invalid data.
     *
     **/

    public ApplicationSpecification parseApplicationSpecification(
        InputStream input,
        String resourcePath)
        throws DocumentParseException
    {
        Document document;

        try
        {
            document = parse(new InputSource(input), resourcePath, "application");

            return convertApplicationSpecification(document);
        }
        finally
        {
            setResourcePath(null);
        }
    }

    private boolean getBooleanValue(Node node) throws DocumentParseException
    {
        String key = getValue(node).toLowerCase();

        Boolean value = (Boolean) conversionMap.get(key);

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

    private ApplicationSpecification convertApplicationSpecification(Document document)
        throws DocumentParseException
    {
        Element root;
        Node node;
        ApplicationSpecification specification;

        specification = factory.createApplicationSpecification();

        root = document.getDocumentElement();

        specification.setName(getAttribute(root, "name"));
        specification.setEngineClassName(getAttribute(root, "engine-class"));

        for (node = root.getFirstChild(); node != null; node = node.getNextSibling())
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
        }

        return specification;
    }

    private void convertPage(ApplicationSpecification specification, Node node)
    {
        String name = getAttribute(node, "name");
        String specificationPath = getAttribute(node, "specification-path");

        PageSpecification page = factory.createPageSpecification(specificationPath);

        specification.setPageSpecification(name, page);
    }

    private void convertComponentAlias(
        ApplicationSpecification specification,
        Node node)
    {
        String type = getAttribute(node, "type");
        String path = getAttribute(node, "specification-path");

        specification.setComponentAlias(type, path);
    }

    private void convertProperty(IPropertyHolder holder, Node node)
    {
        String name = getAttribute(node, "name");
        String value = getValue(node);

        holder.setProperty(name, value);
    }

    private ComponentSpecification convertComponentSpecification(Document document)
        throws DocumentParseException
    {
        ComponentSpecification specification = factory.createComponentSpecification();
        Element root = document.getDocumentElement();

        specification.setAllowBody(getBooleanAttribute(root, "allow-body"));
        specification.setAllowInformalParameters(
            getBooleanAttribute(root, "allow-informal-parameters"));
        specification.setComponentClassName(getAttribute(root, "class"));

        for (Node node = root.getFirstChild();
            node != null;
            node = node.getNextSibling())
        {
            if (isElement(node, "parameter"))
            {
                convertParameter(specification, node);
                continue;
            }

            if (isElement(node, "reserved-parameter"))
            {
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

    private void convertParameter(ComponentSpecification specification, Node node)
        throws DocumentParseException
    {
        ParameterSpecification param = factory.createParameterSpecification();

        String name = getAttribute(node, "name");
        param.setType(getAttribute(node, "java-type"));
        param.setRequired(getBooleanAttribute(node, "required"));

        String propertyName = getAttribute(node, "property-name");

        // If not specified (or a 1.0 DTD, in which case the property-name
        // attribute doesn't exist), use the name of the parameter.

        if (propertyName == null)
            propertyName = name;

        param.setPropertyName(propertyName);

		String direction = getAttribute(node, "direction");
		
		if (direction != null)
			param.setDirection((Direction)conversionMap.get(direction));			

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

    private void convertBean(ComponentSpecification specification, Node node)
        throws DocumentParseException
    {
        String name = getAttribute(node, "name");
        String className = getAttribute(node, "class");
        String lifecycleString = getAttribute(node, "lifecycle");

        BeanLifecycle lifecycle = (BeanLifecycle) conversionMap.get(lifecycleString);

        BeanSpecification bspec = factory.createBeanSpecification(className, lifecycle);

        specification.addBeanSpecification(name, bspec);

        for (Node child = node.getFirstChild();
            child != null;
            child = child.getNextSibling())
        {
            if (isElement(child, "description"))
            {
                bspec.setDescription(getValue(child));
                continue;
            }

            if (isElement(child, "set-property"))
            {
                convertSetProperty(bspec, child);
                continue;
            }
        }
    }

    /** @since 1.0.5 **/

    private void convertSetProperty(BeanSpecification spec, Node node)
        throws DocumentParseException
    {
        String name = getAttribute(node, "name");

        // <set-property> contains either <static-value>, <field-value> or <property-value>

        for (Node child = node.getFirstChild();
            child != null;
            child = child.getNextSibling())
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

    /** @since 1.0.8 **/

    private void convertFieldValue(
        BeanSpecification spec,
        String propertyName,
        Node node)
    {
        String fieldName = getAttribute(node, "field-name");
        IBeanInitializer iz =
            factory.createFieldBeanInitializer(propertyName, fieldName);

        spec.addInitializer(iz);
    }

    /** @since 1.0.5 **/

    private void convertPropertyValue(
        BeanSpecification spec,
        String propertyName,
        Node node)
    {
        String propertyPath = getAttribute(node, "property-path");
        IBeanInitializer iz =
            factory.createPropertyBeanInitializer(propertyName, propertyPath);

        spec.addInitializer(iz);
    }

    /** @since 1.0.5 **/

    private void convertStaticValue(
        BeanSpecification spec,
        String propertyName,
        Node node)
        throws DocumentParseException
    {
        String type = getAttribute(node, "type");
        String value = getValue(node);

        IConverter converter = (IConverter) conversionMap.get(type);

        if (converter == null)
            throw new DocumentParseException(
                "Unknown <static-value> type: '" + type + "'.");

        Object staticValue = converter.convert(value);

        IBeanInitializer iz =
            factory.createStaticBeanInitializer(propertyName, staticValue);

        spec.addInitializer(iz);
    }

    private void convertComponent(ComponentSpecification specification, Node node)
        throws DocumentParseException
    {
        String id = getAttribute(node, "id");
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

            c = factory.createContainedComponent();
            c.setType(type);
        }

        for (Node child = node.getFirstChild();
            child != null;
            child = child.getNextSibling())
        {
            if (isElement(child, "binding"))
            {
                convertBinding(c, child, BindingType.DYNAMIC, "property-path");
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
        }

        specification.addComponent(id, c);
    }

    private void convertBinding(
        ContainedComponent component,
        Node node,
        BindingType type,
        String attributeName)
    {
        String name = getAttribute(node, "name");
        String value = getAttribute(node, attributeName);
        BindingSpecification binding = factory.createBindingSpecification(type, value);

        component.setBinding(name, binding);
    }

    private void convertStaticBinding(ContainedComponent component, Node node)
    {
        String name = getAttribute(node, "name");
        String value = getValue(node);
        BindingSpecification binding =
            factory.createBindingSpecification(BindingType.STATIC, value);

        component.setBinding(name, binding);
    }

    private ContainedComponent copyExistingComponent(
        ComponentSpecification spec,
        String id)
        throws DocumentParseException
    {
        ContainedComponent c = spec.getComponent(id);
        if (c == null)
            throw new DocumentParseException(
                Tapestry.getString("SpecificationParser.unable-to-copy", id),
                getResourcePath());

        ContainedComponent result = factory.createContainedComponent();

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

    private void convertAsset(
        ComponentSpecification specification,
        Node node,
        AssetType type,
        String attributeName)
    {
        String name = getAttribute(node, "name");
        String value = getAttribute(node, attributeName);
        AssetSpecification asset = factory.createAssetSpecification(type, value);

        specification.addAsset(name, asset);
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

    private void convertService(ApplicationSpecification spec, Node node)
    {
        String name = getAttribute(node, "name");
        String className = getAttribute(node, "class");

        spec.addService(name, className);
    }

    /**
     *  Sets the SpecFactory which instantiates Tapestry spec objects.
     * 
     *  @since 1.0.9
     **/

    public void setFactory(SpecFactory factory)
    {
        this.factory = factory;
    }

    /**
     *  Returns the current SpecFactory which instantiates Tapestry spec objects.
     * 
     *  @since 1.0.9
     * 
     **/

    public SpecFactory getFactory()
    {
        return factory;
    }
}