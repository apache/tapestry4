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
 
/**
 *  Used to parse an application or component specification into a
 *  {@link ApplicationSpecification} or {@link ComponentSpecification}.
 *  This is all somewhat temporary; parsing will be revised to use
 *  Adelard (Java XML Binding) once it is available.
 *
 *  @version $Id$
 *  @author Howard Ship
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
 
public class SpecificationParser
extends AbstractDocumentParser
{
	static private final Map booleanMap;

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


	public SpecificationParser()
	{
		register("-//Primix Solutions//Tapestry Specification 1.0//EN", 
				 "Tapestry_1_0.dtd");
	}

	private ComponentSpecification convertComponentSpecification(Document document)
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
				convertParameters(result, node);
				continue;
			}

			if (isElement(node, "components"))
			{
				convertComponents(result, node);
				continue;
			}

			if (isElement(node, "assets"))
			{
				convertAssets(result, node);
				continue;
			}

			if (isElement(node, "properties"))
			{
				convertProperties(result, node);
				continue;
			}

		}

		return result;
	}

	private ApplicationSpecification convertApplicationSpecification(Document document)
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
				convertPage(specification, node);
				continue;
			}

			if (isElement(node, "component-alias"))
			{
				convertComponentAlias(specification, node);
				continue;
			}

			if (isElement(node, "properties"))
			{
				convertProperties(specification, node);
				continue;
			}
		}

		return specification;
	}

	private void convertPage(ApplicationSpecification specification, Node node)
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

	private void convertComponentAlias(ApplicationSpecification specification, Node node)
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

	private void convertParameters(ComponentSpecification specification, Node node)
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
				convertParameter(specification, child);
				continue;
			}
		}
	}

	private void convertParameter(ComponentSpecification specification, Node node)
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

	private void convertComponents(ComponentSpecification specification, Node node)
	throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "component"))
			{
				convertComponent(specification, child);
				continue;
			}
		}
	}

	private void convertComponent(ComponentSpecification specification, Node node)
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
				convertBindings(contained, child);
				continue;
			}

		}

		specification.addComponent(id, contained);
	}

	private void convertBindings(ContainedComponent contained, Node node)
	throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "binding"))
			{
				convertBinding(contained, child, BindingType.DYNAMIC, "property-path");
				continue;
			}

			if (isElement(child, "static-binding"))
			{
				convertBinding(contained, child, BindingType.STATIC, "value");
				continue;
			}

			if (isElement(child, "inherited-binding"))
			{
				convertBinding(contained, child, BindingType.INHERITED, "parameter-name");
				continue;
			}

			if (isElement(child, "field-binding"))
			{
				convertBinding(contained, child, BindingType.FIELD, "field-name");
				continue;
			}
		}
	}

	private void convertBinding(ContainedComponent contained, Node node,
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

	private void convertAssets(ComponentSpecification specification, Node node)
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
				convertAsset(specification, child, AssetType.CONTEXT, "path");
				continue;
			}

			if (isElement(child, "external-asset"))
			{
				convertAsset(specification, child,  AssetType.EXTERNAL, "URL");
				continue;
			}

			if (isElement(child, "private-asset"))
			{
				convertAsset(specification, child, AssetType.PRIVATE, "resource-path");
				continue;
			}
		}
	}

	private void convertAsset(ComponentSpecification specification, Node node,
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

	private void convertProperties(IPropertyHolder holder, Node node)
	throws DocumentParseException
	{
		Node child;

		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "property"))
			{
				convertProperty(holder, child);
				continue;
			}
		}
	}

	private void convertProperty(IPropertyHolder holder, Node node)
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

}

