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
 *  A wrapper around {@link DocumentBuilder} (itself a wrapper around
 *  some XML parser), this class provides error handling and entity
 *  resolving.
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.10
 */
 
package com.primix.tapestry.util.xml;

import com.primix.tapestry.util.*;
import org.w3c.dom.*;
import java.io.*;
import java.util.*;
import org.xml.sax.*;
import javax.xml.parsers.*; 
import org.apache.log4j.*;
 
public abstract class AbstractDocumentParser
implements ErrorHandler, EntityResolver
{
	private static final Category CAT =
		Category.getInstance(AbstractDocumentParser.class);
		
	private DocumentBuilder builder;
	private String resourcePath;
	private static final int MAP_SIZE = 7;
	
	/**
	 *  Map used to resolve public identifiers to corresponding InputSource.
	 *
	 */
	 
	private Map entities;

	/**
	 *  Invoked by subclasses (usually inside thier constructor) to register
	 *  a public id and corresponding input source.  Generally, the source
	 *  is a wrapper around an input stream to a package resource.
	 *
	 *  @param publicId the public identifier to be registerred, generally
	 *  the publicId of a DTD related to the document being parsed
	 *  @param entityPath the resource path of the entity, typically a DTD
	 *  file.  Relative files names are expected to be stored in the same package
	 *  as the class file, otherwise a leading slash is an absolute pathname
	 *  within the classpath.
	 *
	 */
	 
	protected void register(String publicId, String entityPath)
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Registerring " + publicId + " as " + entityPath);
			
		if (entities == null)
			entities = new HashMap(MAP_SIZE);
		
		entities.put(publicId, entityPath);	
	}
	
	public String getResourcePath()
	{
		return resourcePath;
	}
	
	public void setResourcePath(String value)
	{
		resourcePath = null;
	}
	
	/** 
	 * Invoked by subclasses to parse a document.  Obtains (or re-uses) a
	 *  {@link DocumentBuilder} and parses the document from the {@link InputSource}.
	 *
	 *  @param source source from which to read the document
	 *  @param resourcePath a description of the source, used in errors
	 *  @param rootElementName the expected root element of the {@link Document}
	 *
	 *  @throws DocumentParseException wrapped around {@link SAXParseException} or
	 *  {@link IOException}, or if the root element is wrong.
	 *
	 */
	 
	protected Document parse(InputSource source, String resourcePath, String rootElementName)
	throws DocumentParseException
	{
		Document document;
		Element root;
		boolean error = true;
		
		if (CAT.isDebugEnabled())
			CAT.debug("Parsing " + source + " (" + resourcePath + ") for element " +
				rootElementName);
				
		try
		{
			if (builder == null)
				builder = constructBuilder();

			document = builder.parse(source);

			error = false;
			
			root = document.getDocumentElement();
			if (!root.getTagName().equals(rootElementName))
			{
				throw new DocumentParseException(
					"Incorrect document type; expected " + rootElementName + 
					" but received " + root.getTagName() + ".", resourcePath,
					null);
			}

			return document;
		}
		catch (SAXParseException ex)
		{
			// This constructor captures the line number and column number
			
			throw new DocumentParseException("Unable to parse " + resourcePath + ": "
				+ ex.getMessage(), 
				resourcePath, ex);
		}
		catch (SAXException ex)
		{
			throw new DocumentParseException("Unable to parse " + resourcePath + ": " +
				ex.getMessage(), 
				resourcePath, ex);
		}
		catch (IOException ex)
		{
			throw new DocumentParseException("Error reading " + resourcePath + ": " +
				ex.getMessage(),
				resourcePath, ex);
		}
		catch (ParserConfigurationException ex)
		{
			throw new DocumentParseException("Unable to construct DocumentBuilder: " +
				ex.getMessage(), ex);
		}
		finally
		{
			// If there was an error, discard the builder --- it may be in
			// an unknown and unusable state.
			
			if (error && builder != null)
			{
				CAT.debug("Discarding builder due to parse error.");
				builder = null;	
			}
		}	
	}

	/**
	 *  Throws the exception, which is caught and wrapped
	 *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
	 *
	 */
	 
	public void warning(SAXParseException exception)
	throws SAXException
	{
		throw exception;
	}

	/**
	 *  Throws the exception, which is caught and wrapped
	 *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
	 *
	 */
	 
	public void error(SAXParseException exception)
	throws SAXException
	{
		throw exception;
	}

	/**
	 *  Throws the exception, which is caught and wrapped
	 *  in a {@link DocumentParseException} by {@link #parse(InputSource,String,String)}.
	 *
	 */
	 
	public void fatalError(SAXParseException exception)
	throws SAXException
	{
		throw exception;
	}

	/**
	*  Checks for a previously registered public ID and returns the corresponding
	*  input source.
	*
	*/

	public InputSource resolveEntity(String publicId,
		String systemId)
	throws SAXException, IOException
	{
		if (CAT.isDebugEnabled())
			CAT.debug("Attempting to resolve entity; publicId = " +
				publicId + 
				" systemId = " + systemId);
				
		if (entities == null)
			return null;
		
		String entityPath = (String)entities.get(publicId);
		if (entityPath == null)
			return null;
				
		InputStream stream = getClass().getResourceAsStream(entityPath);
		
		InputSource result = new InputSource(stream);
		
		if (result != null && CAT.isDebugEnabled())
			CAT.debug("Resolved " + publicId + " as " + result + " (for " +
				entityPath + ")");
		
		return result;	
	}


	/**
	 *  Returns true if the node is an element with the specified
	 *  name.
	 *
	 */
	 
	protected boolean isElement(Node node, String elementName)
	throws DocumentParseException
	{
		if (node.getNodeType() != Node.ELEMENT_NODE)
			return false;

		// Cast it to Element

		Element element = (Element)node;

		// Note:  Using Xerces 1.0.3 and deferred DOM loading
		// (which is explicitly turned off), this sometimes
		// throws a NullPointerException.

		return element.getTagName().equals(elementName);

	}	

	/**
	*  Returns the value of an {@link Element} node.  That is, all the {@link Text}
	*  nodes appended together.  Invokes trim() to remove leading and trailing spaces.
	*
	*/

	protected String getValue(Node node)
	{
		String result;
		Node child;
		Text text;
		StringBuffer buffer;

		buffer = new StringBuffer();

		for (child = node.getFirstChild(); child != null; child = child.getNextSibling())
		{
			text = (Text)child;

			buffer.append(text.getData());
		}

		result = buffer.toString().trim();

		return result;
	}

	/**
	*  Returns the value of an {@link Element} node (via {@link #getValue(Node)}),
	*  but then validates that the result is a good identifier (starts with a
	*  letter, contains letters, numbers, dashes, underscore).
	*
	*/

	protected String getId(Node node)
	throws DocumentParseException
	{
		String result = getValue(node);
		char[] array = result.toCharArray();
		char ch;
		boolean fail = false;

		for (int i = 0; i < array.length; i++)
		{
			ch = array[i];

			if (i == 0)
				fail = ! Character.isLetter(ch);
			else
			{
				fail = ! (Character.isLetter(ch) ||
					Character.isDigit(ch) ||
					ch == '-' ||
					ch == '_');
			}

			if (fail)
				throw new DocumentParseException
					(result + " is not a valid identifier (in element " +
					getNodePath(node.getParentNode()) + ").",
					resourcePath, null);
		}

		return result;
	}


	/**
	 *  Returns a 'path' to the given node, which is a list of enclosing
	 *  element names seperated by periods.  The root element name is first,
	 *  and the node's element is last.  This is used when reporting some
	 *  parse errors.
	 *
	 */
	 
	protected String getNodePath(Node node)
	{
		int count = 0;
		int length = 0;
		
		String[] path = new String[10];
		
		while (node != null)
		{
			// Dynamically expand the list before it overflows.
			
			if (count == path.length)
			{
				String newPath[] = new String[count * 2];
				System.arraycopy(path, 0, newPath, 0, count);
				
				path = newPath;			
			}
			
			String nodeName = node.getNodeName();
			
			path[count++] = nodeName;
			node = node.getParentNode();

			length += nodeName.length() + 1;
			
		}

		StringBuffer buffer = new StringBuffer(length);
		boolean addDot = false;

		for (int i = count - 1; i >= 0; i--)
		{

			if (addDot)
				buffer.append('.');

			buffer.append(path[i]);
			
			addDot = true;
		}

		return buffer.toString();
	}		

	/**
	 *  Constructs a new {@link DocumentBuilder} to be used for parsing.
	 *  The builder is used and reused, at least until there is an error
	 *  parsing a document (at which point, it is discarded).
	 *
	 *  <p>This implementation obtains a builder with the following
	 *  characteristics:
	 *  <ul>
	 *  <li>validating
	 *  <li>ignoringElementContentWhitespace
	 *  <li>ignoringComments
	 *  <li>coalescing
	 *  </ul>
	 *
	 *  <p>These characteristics are appropriate to parsing things such
	 *  as Tapestry specifications; subclasses with unusual demands
	 *  may need to override this method.
	 *
	 *  <p>The builder uses this {@link AbstractDocumentParser}
	 *  as the entity resolver and error handler.
	 *
	 */
	 
	protected DocumentBuilder constructBuilder()
	throws ParserConfigurationException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		factory.setValidating(true);
		factory.setIgnoringElementContentWhitespace(true);
		factory.setIgnoringComments(true);
		factory.setCoalescing(true);

		DocumentBuilder result = factory.newDocumentBuilder();

		result.setErrorHandler(this);
		result.setEntityResolver(this);

		if (CAT.isDebugEnabled())
			CAT.debug("Constructed new builder " + result);
			
		return result;
	}

}

