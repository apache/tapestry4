/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package net.sf.tapestry.parse;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import net.sf.tapestry.util.xml.AbstractDocumentParser;
import net.sf.tapestry.util.xml.DocumentParseException;

import net.sf.tapestry.*;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.bean.*;
import net.sf.tapestry.bean.IBeanInitializer;
import net.sf.tapestry.spec.*;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.spec.AssetSpecification;
import net.sf.tapestry.spec.AssetType;
import net.sf.tapestry.spec.BeanLifecycle;
import net.sf.tapestry.spec.BeanSpecification;
import net.sf.tapestry.spec.BindingSpecification;
import net.sf.tapestry.spec.BindingType;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;
import net.sf.tapestry.spec.PageSpecification;
import net.sf.tapestry.spec.ParameterSpecification;
import net.sf.tapestry.spec.SpecFactory;
import net.sf.tapestry.util.*;
import net.sf.tapestry.util.IPropertyHolder;

/**
 *  Used to parse an application or component specification into a
 *  {@link ApplicationSpecification} or {@link ComponentSpecification}.
 *  This is all somewhat temporary; parsing will be revised to use
 *  Adelard (Java XML Binding) once it is available.
 *
 *  <p>This class supports the 1.1 DTD (introduced in release 1.0.1)
 *  as well as the "old" 1.0 DTD.
 *
 *  <table border=1
 *	<tr>
 *	  <th>Version</th> <th>PUBLIC ID</th> <th>SYSTEM ID</th> <th>Description</th>
 *  </tr>
 *  <tr valign=top>
 *	  <td>1.0</td>
 *	  <td><code>-//Primix Solutions//Tapestry Specification 1.0//EN</code></td>
 *    <td><code>http://tapestry.sourceforge.net/dtd/Tapestry_1_0.dtd</code></td>
 *    <td>Original, overly verbose version.</td>
 *   </tr>
 *  <tr valign=top>
 *    <td>1.1</th>
 *    <td><code>-//Howard Ship//Tapestry Specification 1.1//EN</code>
 *    <td><code>http://tapestry.sf.net/dtd/Tapestry_1_1.dtd</code></td>
 *   <td>Streamlined version of 1.0 (makes use of XML attributes
 *  instead of nested elements), also:
 *  <ul>
 *  <li>Adds &lt;description&gt; element
 *  <li>Adds copy-of attribute to &lt;component&gt;
 *  <li>Support for helper beans
 *  </ul>
 *  </td>
 *  </tr>
 *  </table>
 *
 *  @version $Id$
 *  @author Howard Ship
 * 
 **/

public class SpecificationParser extends AbstractDocumentParser
{
	public static final String TAPESTRY_DTD_1_0_PUBLIC_ID =
		"-//Primix Solutions//Tapestry Specification 1.0//EN";
	public static final String TAPESTRY_DTD_1_1_PUBLIC_ID =
		"-//Howard Ship//Tapestry Specification 1.1//EN";

	private static final Map booleanMap;
	private static final Map lifecycleMap;
	private static final Map converterMap;

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
			Object result = booleanMap.get(value.toLowerCase());

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
		booleanMap = new HashMap();

		booleanMap.put("true", Boolean.TRUE);
		booleanMap.put("t", Boolean.TRUE);
		booleanMap.put("1", Boolean.TRUE);
		booleanMap.put("y", Boolean.TRUE);
		booleanMap.put("yes", Boolean.TRUE);
		booleanMap.put("on", Boolean.TRUE);

		booleanMap.put("false", Boolean.FALSE);
		booleanMap.put("f", Boolean.FALSE);
		booleanMap.put("0", Boolean.FALSE);
		booleanMap.put("off", Boolean.FALSE);
		booleanMap.put("no", Boolean.FALSE);
		booleanMap.put("n", Boolean.FALSE);

		lifecycleMap = new HashMap();
		lifecycleMap.put("none", BeanLifecycle.NONE);
		lifecycleMap.put("request", BeanLifecycle.REQUEST);
		lifecycleMap.put("page", BeanLifecycle.PAGE);

		converterMap = new HashMap();
		converterMap.put("boolean", new BooleanConverter());
		converterMap.put("int", new IntConverter());
		converterMap.put("double", new DoubleConverter());
		converterMap.put("String", new StringConverter());
	}

	public SpecificationParser()
	{
		register(TAPESTRY_DTD_1_0_PUBLIC_ID, "Tapestry_1_0.dtd");
		register(TAPESTRY_DTD_1_1_PUBLIC_ID, "Tapestry_1_1.dtd");
		factory = new SpecFactory();
	}

	/**
	 *  Parses an input stream containing a component specification and assembles
	 *  a {@link ComponentSpecification} from it.
	 *
	 *  @throws DocumentParseException if the input stream cannot be fully
	 *  parsed or contains invalid data.
	 *
	 */

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
	 */

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

	private ComponentSpecification convertComponentSpecification(Document document)
		throws DocumentParseException
	{
		String publicId = document.getDoctype().getPublicId();

		if (publicId.equals(TAPESTRY_DTD_1_0_PUBLIC_ID))
			return convertComponentSpecification_1(document);

		if (publicId.equals(TAPESTRY_DTD_1_1_PUBLIC_ID))
			return convertComponentSpecification_2(document);

		throw new DocumentParseException(
			Tapestry.getString(
				"SpecificationParser.unexpected-component-public-id",
				publicId),
			getResourcePath());
	}

	private ApplicationSpecification convertApplicationSpecification(Document document)
		throws DocumentParseException
	{
		String publicId = document.getDoctype().getPublicId();

		if (publicId.equals(TAPESTRY_DTD_1_0_PUBLIC_ID))
			return convertApplicationSpecification_1(document);

		if (publicId.equals(TAPESTRY_DTD_1_1_PUBLIC_ID))
			return convertApplicationSpecification_2(document);

		throw new DocumentParseException(
			Tapestry.getString(
				"SpecificationParser.unexpected-application-public-id",
				publicId),
			getResourcePath());

	}

	//	 All the methods with the suffix "_1" parse the first version of the DTD
	//	 (version 1.0), all the methods with suffix "_2" parse the
	//	 second version (version 1.1).

	private ComponentSpecification convertComponentSpecification_1(Document document)
		throws DocumentParseException
	{
		Element root;
		Node node;
		ComponentSpecification result;

		result = factory.createComponentSpecification();

		root = document.getDocumentElement();

		for (node = root.getFirstChild(); node != null; node = node.getNextSibling())
		{
			if (isElement(node, "class"))
			{
				result.setComponentClassName(getValue(node));
				continue;
			}

			if (isElement(node, "allow-body"))
			{
				result.setAllowBody(getBooleanValue(node));
				continue;
			}

			if (isElement(node, "parameters"))
			{
				convertParameters_1(result, node);
				continue;
			}

			if (isElement(node, "components"))
			{
				convertComponents_1(result, node);
				continue;
			}

			if (isElement(node, "assets"))
			{
				convertAssets_1(result, node);
				continue;
			}

			if (isElement(node, "properties"))
			{
				convertProperties_1(result, node);
				continue;
			}

		}

		return result;
	}

	private ApplicationSpecification convertApplicationSpecification_1(Document document)
		throws DocumentParseException
	{
		Element root;
		Node node;
		ApplicationSpecification specification;

		specification = factory.createApplicationSpecification();

		root = document.getDocumentElement();

		for (node = root.getFirstChild(); node != null; node = node.getNextSibling())
		{
			if (isElement(node, "name"))
			{
				specification.setName(getValue(node));
				continue;
			}

			if (isElement(node, "engine-class"))
			{
				specification.setEngineClassName(getValue(node));
				continue;
			}

			if (isElement(node, "page"))
			{
				convertPage_1(specification, node);
				continue;
			}

			if (isElement(node, "component-alias"))
			{
				convertComponentAlias_1(specification, node);
				continue;
			}

			if (isElement(node, "properties"))
			{
				convertProperties_1(specification, node);
				continue;
			}
		}

		return specification;
	}

	private void convertPage_1(ApplicationSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		PageSpecification page;

		page = factory.createPageSpecification();

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "name"))
			{
				name = getValue(child);
				continue;
			}

			if (isElement(child, "specification-path"))
			{
				page.setSpecificationPath(getValue(child));
				continue;
			}

		}

		specification.setPageSpecification(name, page);
	}

	private void convertComponentAlias_1(
		ApplicationSpecification specification,
		Node node)
		throws DocumentParseException
	{
		Node child;
		String alias = null;
		String path = null;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "alias"))
			{
				alias = getValue(child);
				continue;
			}

			if (isElement(child, "specification-path"))
			{
				path = getValue(child);
				continue;
			}
		}

		specification.setComponentAlias(alias, path);

	}

	private void convertParameters_1(
		ComponentSpecification specification,
		Node node)
		throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "allow-informal-parameters"))
			{
				specification.setAllowInformalParameters(getBooleanValue(child));
				continue;
			}

			if (isElement(child, "parameter"))
			{
				convertParameter_1(specification, child);
				continue;
			}
		}
	}

	private void convertParameter_1(
		ComponentSpecification specification,
		Node node)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		ParameterSpecification parameter;

		parameter = factory.createParameterSpecification();

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "name"))
			{
				name = getValue(child);
				continue;
			}

			if (isElement(child, "java-type"))
			{
				parameter.setType(getValue(child));
				continue;
			}

			if (isElement(child, "required"))
			{
				parameter.setRequired(getBooleanValue(child));
				continue;
			}
		}

		specification.addParameter(name, parameter);

	}

	private void convertComponents_1(
		ComponentSpecification specification,
		Node node)
		throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "component"))
			{
				convertComponent_1(specification, child);
				continue;
			}
		}
	}

	private void convertComponent_1(
		ComponentSpecification specification,
		Node node)
		throws DocumentParseException
	{
		ContainedComponent contained;
		Node child;
		String id = null;

		contained = factory.createContainedComponent();

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "id"))
			{
				id = getId(child);
				continue;
			}

			if (isElement(child, "type"))
			{
				contained.setType(getValue(child));
				continue;
			}

			if (isElement(child, "bindings"))
			{
				convertBindings_1(contained, child);
				continue;
			}

		}

		specification.addComponent(id, contained);
	}

	private void convertBindings_1(ContainedComponent contained, Node node)
		throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "binding"))
			{
				convertBinding_1(contained, child, BindingType.DYNAMIC, "property-path");
				continue;
			}

			if (isElement(child, "static-binding"))
			{
				convertBinding_1(contained, child, BindingType.STATIC, "value");
				continue;
			}

			if (isElement(child, "inherited-binding"))
			{
				convertBinding_1(contained, child, BindingType.INHERITED, "parameter-name");
				continue;
			}

			if (isElement(child, "field-binding"))
			{
				convertBinding_1(contained, child, BindingType.FIELD, "field-name");
				continue;
			}
		}
	}

	private void convertBinding_1(
		ContainedComponent contained,
		Node node,
		BindingType type,
		String innerElementName)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		String value = null;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "name"))
			{
				name = getValue(child);
				continue;
			}

			if (isElement(child, innerElementName))
			{
				value = getValue(child);
				continue;
			}
		}

		contained.setBinding(name, factory.createBindingSpecification(type, value));
	}

	private void convertAssets_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			// <internal-asset> has been deprecated, replaced with
			// <context-asset>

			if (isElement(child, "internal-asset") || isElement(child, "context-asset"))
			{
				convertAsset_1(specification, child, AssetType.CONTEXT, "path");
				continue;
			}

			if (isElement(child, "external-asset"))
			{
				convertAsset_1(specification, child, AssetType.EXTERNAL, "URL");
				continue;
			}

			if (isElement(child, "private-asset"))
			{
				convertAsset_1(specification, child, AssetType.PRIVATE, "resource-path");
				continue;
			}
		}
	}

	private void convertAsset_1(
		ComponentSpecification specification,
		Node node,
		AssetType type,
		String innerElementName)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		String path = null;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "name"))
			{
				name = getValue(child);
				continue;
			}

			if (isElement(child, innerElementName))
			{
				path = getValue(child);
				continue;
			}
		}

		specification.addAsset(name, factory.createAssetSpecification(type, path));
	}

	private void convertProperties_1(IPropertyHolder holder, Node node)
		throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "property"))
			{
				convertProperty_1(holder, child);
				continue;
			}
		}
	}

	private void convertProperty_1(IPropertyHolder holder, Node node)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		String value = null;

		for (child = node.getFirstChild();
			child != null;
			child = child.getNextSibling())
		{
			if (isElement(child, "name"))
			{
				name = getValue(child);
				continue;
			}

			if (isElement(child, "value"))
			{
				value = getValue(child);
				continue;
			}
		}

		holder.setProperty(name, value);
	}

	private boolean getBooleanValue(Node node) throws DocumentParseException
	{
		String key;
		Boolean value;

		key = getValue(node).toLowerCase();

		value = (Boolean) booleanMap.get(key);

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

		return attributeValue != null && attributeValue.equals("yes");
	}

	private ApplicationSpecification convertApplicationSpecification_2(Document document)
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
				convertPage_2(specification, node);
				continue;
			}

			if (isElement(node, "component-alias"))
			{
				convertComponentAlias_2(specification, node);
				continue;
			}

			if (isElement(node, "property"))
			{
				convertProperty_2(specification, node);
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

	private void convertPage_2(ApplicationSpecification specification, Node node)
	{
		String name = getAttribute(node, "name");
		String specificationPath = getAttribute(node, "specification-path");

		PageSpecification page = factory.createPageSpecification(specificationPath);

		specification.setPageSpecification(name, page);
	}

	private void convertComponentAlias_2(
		ApplicationSpecification specification,
		Node node)
	{
		String type = getAttribute(node, "type");
		String path = getAttribute(node, "specification-path");

		specification.setComponentAlias(type, path);
	}

	private void convertProperty_2(IPropertyHolder holder, Node node)
	{
		String name = getAttribute(node, "name");
		String value = getValue(node);

		holder.setProperty(name, value);
	}

	private ComponentSpecification convertComponentSpecification_2(Document document)
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
				convertParameter_2(specification, node);
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
				convertComponent_2(specification, node);
				continue;
			}

			if (isElement(node, "external-asset"))
			{
				convertAsset_2(specification, node, AssetType.EXTERNAL, "URL");
				continue;
			}

			if (isElement(node, "context-asset"))
			{
				convertAsset_2(specification, node, AssetType.CONTEXT, "path");
				continue;
			}

			if (isElement(node, "private-asset"))
			{
				convertAsset_2(specification, node, AssetType.PRIVATE, "resource-path");
				continue;
			}

			if (isElement(node, "property"))
			{
				convertProperty_2(specification, node);
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

	private void convertParameter_2(
		ComponentSpecification specification,
		Node node)
		throws DocumentParseException
	{
		ParameterSpecification param = factory.createParameterSpecification();

		String name = getAttribute(node, "name");
		param.setType(getAttribute(node, "java-type"));
		param.setRequired(getBooleanAttribute(node, "required"));

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
	 */

	private void convertBean(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		String name = getAttribute(node, "name");
		String className = getAttribute(node, "class");
		String lifecycleString = getAttribute(node, "lifecycle");

		BeanLifecycle lifecycle = (BeanLifecycle) lifecycleMap.get(lifecycleString);

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

		IConverter converter = (IConverter) converterMap.get(type);

		if (converter == null)
			throw new DocumentParseException(
				"Unknown <static-value> type: '" + type + "'.");

		Object staticValue = converter.convert(value);

		IBeanInitializer iz =
			factory.createStaticBeanInitializer(propertyName, staticValue);

		spec.addInitializer(iz);
	}

	private void convertComponent_2(
		ComponentSpecification specification,
		Node node)
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
				convertBinding_2(c, child, BindingType.DYNAMIC, "property-path");
				continue;
			}

			if (isElement(child, "field-binding"))
			{
				convertBinding_2(c, child, BindingType.FIELD, "field-name");
				continue;
			}

			if (isElement(child, "inherited-binding"))
			{
				convertBinding_2(c, child, BindingType.INHERITED, "parameter-name");
				continue;
			}

			if (isElement(child, "static-binding"))
			{
				convertStaticBinding_2(c, child);
				continue;
			}
		}

		specification.addComponent(id, c);
	}

	private void convertBinding_2(
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

	private void convertStaticBinding_2(ContainedComponent component, Node node)
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

	private void convertAsset_2(
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
	 */

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