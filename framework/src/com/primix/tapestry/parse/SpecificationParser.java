package com.primix.tapestry.parse;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.*;
import org.apache.xerces.parsers.DOMParser;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
 
/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 
public class SpecificationParser
implements ErrorHandler, EntityResolver
{
	private DOMParser parser;
	
	/**
	 *  Parses an input stream containing a component specification and assembles
	 *  a {@link ComponentSpecification} from it.
	 *
	 *  @throws ResourceUnavailableException if the input stream cannot be fully
	 *  parsed or contains invalid data.
	 *
	 */
	 
	public ComponentSpecification parseComponentSpecification(InputStream input, 
		String resourcePath)
	throws ResourceUnavailableException
	{
		Document document;

		document = parse(input, resourcePath, "specification");
		
		return convertComponentSpecification(document);
	}
	
	/**
	 *  Parses an input stream containing an application specification and assembles
	 *  a {@link ApplicationSpecification} from it.
	 *
	 *  @throws ResourceUnavailableException if the input stream cannot be fully
	 *  parsed or contains invalid data.
	 *
	 */
	
	public ApplicationSpecification parseApplicationSpecification(InputStream input,
		String resourcePath)
	throws ResourceUnavailableException
	{
		Document document;
		
		document = parse(input, resourcePath, "application");
		
		return convertApplicationSpecification(document);
	}
	
	private Document parse(InputStream input, String resourcePath, String rootElementName)
	throws ResourceUnavailableException
	{
		InputSource source;
		Document document;
		Element root;
		try
		{
			if (parser == null)
			{
				parser = new DOMParser();
				parser.setErrorHandler(this);
				parser.setEntityResolver(this);
				
				// Turn on validation.  We use the setFeature() method since
				// it doesn't throw java.lang.Exception (!).
				
				parser.setFeature("http://xml.org/sax/features/validation", true);
				
				// Leave ignorable whitespace (i.e., whitespace around tags) out
				// of the DOM tree.
				
				parser.setFeature("http://apache.org/xml/features/dom/include-ignorable-whitespace", 
					false);
			}
			
			source = new InputSource(input);
			
			parser.parse(source);
			
			document = parser.getDocument();
			
			root = document.getDocumentElement();
			if (!root.getTagName().equals(rootElementName))
			{
				throw new ResourceUnavailableException(
					"Error in " + resourcePath + ": " +
					"Incorrect document type; expected " + rootElementName + 
					" but received " + root.getTagName() + ".");
			}
			
			return document;
		}
		catch (SAXException se)
		{
			parser = null;
			
			throw new ResourceUnavailableException("Unable to parse " + resourcePath + ".", se);
		}
		catch (IOException ioe)
		{
			parser = null;
			
			throw new ResourceUnavailableException("Error reading " + resourcePath + ".", ioe);
		}
	}
	
	public void warning(SAXParseException exception)
             throws SAXException
	{
		parser = null; 
		throw exception;
	}
	
	public void error(SAXParseException exception)
    		throws SAXException
	{
		parser = null;
		throw exception;
	}
	
	public void fatalError(SAXParseException exception)
                throws SAXException
	{
		parser = null;
		throw exception;
	}
		
	/**
	 *  Handles the public reference <code>-//Primix Solutions//Tapestry Specification 1.0//EN</code>
	 *  by resolving it to the input stream of the <code>Tapestry_1_0.dtd</code>
	 *  resource.
	 *
	 */
	 
	public InputSource resolveEntity(String publicId,
                                 String systemId)
                          throws SAXException,
                                 IOException
	{
		
		if (publicId.equals("-//Primix Solutions//Tapestry Specification 1.0//EN"))
		{
			InputStream stream;
			
			stream = getClass().getResourceAsStream("Tapestry_1_0.dtd");
			
			return new InputSource(stream);
		}
		
		// Use default behavior.
		
		return null;
	}
								 
	private ComponentSpecification convertComponentSpecification(Document document)
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
			
			if (isElement(child, "buffer-size"))
			{
				convertBufferSize(page, child);
				continue;
			}
			
			if (isElement(child, "specification-path"))
			{
				page.setSpecificationPath(getValue(child));
				continue;
			}
			
			if (isElement(child, "properties"))
			{
				convertProperties(page, child);
				continue;
			}
		}
		
		specification.setPageSpecification(name, page);
	}
	
	private void convertComponentAlias(ApplicationSpecification specification, Node node)
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
	/**
	*  Sets the buffer size for the page.  This is expressed as a
	*  string.  The string specifies the size of the buffer, either
	*  in bytes or in kilobytes by appending a 'k'.
	*
	*  The exact value accepted is defined by the regular expression:
	*  [0-9]+ *(k|K)
	*
	*  @throws IllegalArgumentException if the string is not formatted acceptibly
	*/

	private void convertBufferSize(PageSpecification page, Node node)
	{
		int bytes = 0;
		char[] digits;
		char digit;
		int i;
		boolean requireDigit = true;
		boolean acceptModifier = false;
		boolean acceptDigit = true;
		boolean invalid = false;
		String value;

		value = getValue(node);
		
		// Leading, trailing whitespace is already trimmed away.

		digits = value.toCharArray();

		for (i = 0; i < digits.length; i++)
		{
			digit = digits[i];

			if (digit >= '0' && digit <= '9')
			{
				if (!acceptDigit)
				{
					invalid = true;
					break;
				}

				bytes = (10 * bytes) + (digit - '0');

				acceptModifier = true;
				requireDigit = false;
				continue;
			}

			acceptDigit = false;

			if (requireDigit)
			{
				invalid = true;
				break;
			}

			// One or more spaces allowed between the base quantity and the modifier

			if (digit == ' ')
			{
				acceptDigit = false;
				continue;
			}

			if (!acceptModifier)
			{
				invalid = true;
				break;
			}


			if (digit == 'k' || digit == 'K')
			{
				bytes *= 1024;

				// Continue loop, but this should be the last letter.

				continue;
			}

			// Unrecognized character

			invalid = true;
			break;
		}


		if (invalid)
			throw new IllegalArgumentException("Invalid buffer size specification: '" +
				value + "'.");

		page.setBufferSize(bytes);
	}

	
	private void convertParameters(ComponentSpecification specification, Node node)
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
	{
		ContainedComponent contained;
		Node child;
		String id = null;
		
		contained = new ContainedComponent();
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "id"))
			{
				id = getValue(child);
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
			
			if (isElement(child, "properties"))
			{
				convertProperties(contained, child);
				continue;
			}
		}
		
		specification.addComponent(id, contained);
	}
	
	private void convertBindings(ContainedComponent contained, Node node)
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
		}
	}
	
	private void convertBinding(ContainedComponent contained, Node node,
		BindingType type, String innerElementName)
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
	{
		Node child;
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			if (isElement(child, "internal-asset"))
			{
				convertAsset(specification, child, AssetType.INTERNAL, "path");
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
	
	private boolean isElement(Node node, String elementName)
	{
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return false;
		
		// Cast it to Element
		
		Element element = (Element)node;
		
		return element.getTagName().equals(elementName);
	}	

	private StringBuffer buffer;
	
	/**
	 *  Returns the value of an {@link Element} node.  That is, all the {@link Text}
	 *  nodes appended together.
	 *
	 */
	 
	private String getValue(Node node)
	{
		String result;
		Node child;
		Text text;
		
		if (buffer == null)
			buffer = new StringBuffer();
		else
			buffer.setLength(0);	
		
		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			text = (Text)child;
			
			buffer.append(text.getData());
		}
		
		result = buffer.toString();
		buffer.setLength(0);
		
		return result;
	}
	
	static private final Map booleanMap;
	
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
		
	private boolean getBooleanValue(Node node)
	{
		String key;
		Boolean value;
		
		key = getValue(node).toLowerCase();
		
		value = (Boolean)booleanMap.get(key);
		
		if (value == null)
			throw new RuntimeException(key + " can't be converted to boolean.");
		
		return value.booleanValue();
	}	
		
}
