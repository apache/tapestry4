/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry.parse;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.util.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import com.primix.tapestry.util.xml.*;

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
 *  </ul>
 *  </td>
 *  </tr>
 *  </table>
 *
 *  @version $Id$
 *  @author Howard Ship
 */


public class SpecificationParser
	extends AbstractDocumentParser
{
	public static final String TAPESTRY_DTD_1_0_PUBLIC_ID = "-//Primix Solutions//Tapestry Specification 1.0//EN";
	public static final String TAPESTRY_DTD_1_1_PUBLIC_ID = "-//Howard Ship//Tapestry Specification 1.1//EN";
	
	private static final Map booleanMap;
	
	// Identify all the different acceptible values.
	
	static
	{
		booleanMap = new HashMap(13);
		
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
	}
	

	public SpecificationParser()
	{
		register(TAPESTRY_DTD_1_0_PUBLIC_ID, "Tapestry_1_0.dtd");
		register(TAPESTRY_DTD_1_1_PUBLIC_ID, "Tapestry_1_1.dtd");
	}

	/**
	 *  Parses an input stream containing a component specification and assembles
	 *  a {@link ComponentSpecification} from it.
	 *
	 *  @throws DocumentParseException if the input stream cannot be fully
	 *  parsed or contains invalid data.
	 *
	 */
	
	public ComponentSpecification parseComponentSpecification(InputStream input, 
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
	
	public ApplicationSpecification parseApplicationSpecification(InputStream input,
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
		if (document.getDoctype().getPublicId().equals(TAPESTRY_DTD_1_0_PUBLIC_ID))
			return convertComponentSpecification_1(document);
		
		return convertComponentSpecification_2(document);
	}
	

	private ApplicationSpecification convertApplicationSpecification(Document document)
		throws DocumentParseException
	{
		if (document.getDoctype().getPublicId().equals(TAPESTRY_DTD_1_0_PUBLIC_ID))
			return convertApplicationSpecification_1(document);
		
		return convertApplicationSpecification_2(document);
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
		
		result = new ComponentSpecification();
		
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
		
		specification = new ApplicationSpecification();
		
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
		
		page = new PageSpecification();
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertComponentAlias_1(ApplicationSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		String alias = null;
		String path = null;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertParameters_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertParameter_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		ParameterSpecification parameter;
		
		parameter = new ParameterSpecification();
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertComponents_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "component"))
			{
				convertComponent_1(specification, child);
				continue;
			}
		}
	}
	
	private void convertComponent_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		ContainedComponent contained;
		Node child;
		String id = null;
		
		contained = new ContainedComponent();
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertBinding_1(ContainedComponent contained, Node node,
			BindingType type, String innerElementName)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		String value = null;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
		
		contained.setBinding(name, new BindingSpecification(type, value));	
	}
	
	private void convertAssets_1(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		Node child;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			// <internal-asset> has been deprecated, replaced with
			// <context-asset>
			
			if (isElement(child, "internal-asset") ||
					isElement(child, "context-asset"))
			{
				convertAsset_1(specification, child, AssetType.CONTEXT, "path");
				continue;
			}
			
			if (isElement(child, "external-asset"))
			{
				convertAsset_1(specification, child,  AssetType.EXTERNAL, "URL");
				continue;
			}
			
			if (isElement(child, "private-asset"))
			{
				convertAsset_1(specification, child, AssetType.PRIVATE, "resource-path");
				continue;
			}
		}
	}
	
	private void convertAsset_1(ComponentSpecification specification, Node node,
			AssetType type, String innerElementName)
		throws DocumentParseException
	{
		Node child;
		String name = null;
		String path = null;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
		
		specification.addAsset(name, new AssetSpecification(type, path));
	}
	
	private void convertProperties_1(IPropertyHolder holder, Node node)
		throws DocumentParseException
	{
		Node child;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	
	private boolean getBooleanValue(Node node)
		throws DocumentParseException
	{
		String key;
		Boolean value;
		
		key = getValue(node).toLowerCase();
		
		value = (Boolean)booleanMap.get(key);
		
		if (value == null)
			throw new DocumentParseException(
				key + " can't be converted to boolean (in element " +
					getNodePath(node.getParentNode()) + ").",
				getResourcePath(), null);
		
		return value.booleanValue();
	}
	
	
	private boolean getBooleanAttribute(Node node, String attributeName)
	{
		String attributeValue = getAttribute(node, attributeName);
		
		return attributeValue.equals("yes");
	}
	
	private ApplicationSpecification convertApplicationSpecification_2(Document document)
		throws DocumentParseException
	{
		Element root;
		Node node;
		ApplicationSpecification specification;
		
		specification = new ApplicationSpecification();
		
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
		}
		
		return specification;	
	}
	
	private void convertPage_2(ApplicationSpecification specification, Node node)
	{
		String name = getAttribute(node, "name");
		String specificationPath = getAttribute(node, "specification-path");
		
		PageSpecification page = new PageSpecification(specificationPath);
		
		specification.setPageSpecification(name, page);
	}
	
	private void convertComponentAlias_2(ApplicationSpecification specification, Node node)
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
		ComponentSpecification specification = new ComponentSpecification();
		Element root = document.getDocumentElement();
		
		specification.setAllowBody(
				getBooleanAttribute(root, "allow-body"));
		specification.setAllowInformalParameters(
			getBooleanAttribute(root, "allow-informal-parameters"));
		specification.setComponentClassName(getAttribute(root, "class"));
		
		for (Node node = root.getFirstChild(); node != null ; node = node.getNextSibling())
		{
			if (isElement(node, "parameter"))
			{
				convertParameter_2(specification, node);
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
		}
	
		return specification;
	}
	
	private void convertParameter_2(ComponentSpecification specification, Node node)
	{
		ParameterSpecification param = new ParameterSpecification();
		
		String name = getAttribute(node, "name");
		param.setType(getAttribute(node, "java-type"));
		param.setRequired(getBooleanAttribute(node, "required"));
		
		specification.addParameter(name, param);
	}
	
	private void convertComponent_2(ComponentSpecification specification, Node node)
		throws DocumentParseException
	{
		String id = getAttribute(node, "id");
		String type = getAttribute(node, "type");
		String copyOf = getAttribute(node, "copy-of");
		ContainedComponent c;
		
		if (type != null && copyOf != null)
			throw new DocumentParseException(
					"Contained component " + id + 
					" contains both type and copy-of attributes.",
				getResourcePath(), null);
	
		if (copyOf != null)
			c = copyExistingComponent(specification, copyOf);
		else
		{
			if (type == null)
				throw new DocumentParseException(
					"Contained component " + id + 
						" does not specify attribute type or copy-of.",
					getResourcePath(), null);
			
			c = new ContainedComponent();
			c.setType(type);
		}
		
		for (Node child = node.getFirstChild(); child != null; child = child.getNextSibling())
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
	
	private void convertBinding_2(ContainedComponent component, Node node,
			BindingType type, String attributeName)
	{
		String name = getAttribute(node, "name");
		String value = getAttribute(node, attributeName);
		BindingSpecification binding = new BindingSpecification(type, value);
		
		component.setBinding(name, binding);
	}
	
	private void convertStaticBinding_2(ContainedComponent component, Node node)
	{
		String name = getAttribute(node, "name");
		String value = getValue(node);
		BindingSpecification binding = new BindingSpecification(BindingType.STATIC, value);
		
		component.setBinding(name, binding);
	}
	
	private ContainedComponent copyExistingComponent(ComponentSpecification spec, String id)
		throws DocumentParseException
	{
		ContainedComponent c = spec.getComponent(id);
		if (c == null)
			throw new DocumentParseException(
				"Unable to copy component " + id + ", which does not exist.",
				getResourcePath(), null);
		
		ContainedComponent result = new ContainedComponent();
		
		result.setType(c.getType());
		
		Iterator i = c.getBindingNames().iterator();
		while (i.hasNext())
		{
			String name = (String)i.next();
			BindingSpecification binding = c.getBinding(name);
			result.setBinding(name, binding);
		}
		
		return result;
	}
	
	private void convertAsset_2(ComponentSpecification specification, Node node,
			AssetType type, String attributeName)
	{
		String name = getAttribute(node, "name");
		String value = getAttribute(node, attributeName);
		AssetSpecification asset = new AssetSpecification(type, value);
		
		specification.addAsset(name, asset);
	}
}

