package com.primix.tapestry.engine;

import java.util.Locale;
import com.primix.tapestry.*;
import com.primix.tapestry.parse.*;
import java.io.*;
import java.util.*;
import java.net.URL;

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
 *  Default implementation of {@link ITemplateSource}.  Templates, once parsed,
 *  stay in memory until explicitly cleared.
 *
 *  <p>An instance of this class acts as a singleton shared by all sessions, so it
 *  must be threadsafe.
 *
 *  <p>TBD:  This implementation ignores <b>locale</b>.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class DefaultTemplateSource 
   implements ITemplateSource
{
	private static final int MAP_SIZE = 11;

	private Map templates;

	private static final int BUFFER_SIZE = 2000;

	private IResourceResolver resolver;
	private TemplateParser parser;

	public DefaultTemplateSource(IResourceResolver resolver)
	{
        this.resolver = resolver;
	}

	/**
	*  Clears the template cache.  This is used during debugging.
	*
	*/

	public void reset()
	{
		templates = null;
	}

	/**
	*  Reads the template for the component.
	*
	*  <p>TBD:  Do a search based on the locale of the page containing the component.
    *
    *  <p>TBD:  Make threadsafe.
	*
	*  <p>Returns null if the template can't be found.
	*/

	public ComponentTemplate getTemplate(IComponent component)
	throws ResourceUnavailableException
	{
		String specificationResourcePath;
		String resourcePath;
		int dotx;
		ComponentTemplate result = null;

		specificationResourcePath = component.getSpecification().
		getSpecificationResourcePath();

		// Strip off the '.jwc' and replace with '.html'

		dotx = specificationResourcePath.lastIndexOf('.');

		resourcePath = specificationResourcePath.substring(0, dotx) + ".html";

		// Need to do a little search right here when we implement
		// localization.

		if (templates != null)
			result = (ComponentTemplate)templates.get(resourcePath);

		if (result != null)
			return result;

		result = parseTemplate(resourcePath);

		if (templates == null)
			templates = new HashMap(MAP_SIZE);

		templates.put(resourcePath, result);

		return result;
	}

	protected ComponentTemplate parseTemplate(String name)
	throws ResourceUnavailableException
	{
		char[] templateData;
		ComponentTemplate result;
		TemplateToken[] tokens;

		templateData = readTemplate(name);

		if (parser == null)
			parser = new TemplateParser();

		// Once we have the template data in memory, the parse will always be successful.
		// In the future, the parser may be more complicated and will be able to
		// detect errors in the template data.

		tokens = parser.parse(templateData);

		result = new ComponentTemplate(templateData, tokens);

		return result;
	}

	protected char[] readTemplate(String resourceName)
	throws ResourceUnavailableException
	{
    	URL url;
		InputStream stream = null;

		url = resolver.getResource(resourceName);
        
		if (url == null)
			throw new ResourceUnavailableException(
			"Resource " + resourceName + " not found.");

		try
		{
			stream = url.openStream();
        
        	return readTemplateStream(stream);
		}
		catch (IOException e)
		{
			throw new ResourceUnavailableException("Could not read from " + 
				resourceName + ".", e);
		}
		finally
		{
			try
			{
				if (stream != null)
                	stream.close();
			}
			catch (IOException e)
			{
				// Ignore it!
			}
		}

	}

	protected char[] readTemplateStream(InputStream stream)
	throws IOException
	{
		InputStreamReader reader;
		StringBuffer buffer;
		int charsRead;
		char[] charBuffer;
		int length;

		charBuffer = new char[BUFFER_SIZE];
		buffer = new StringBuffer();

		reader = new InputStreamReader(stream);

		try
		{
			while (true)
			{
				charsRead = reader.read(charBuffer, 0, BUFFER_SIZE);

				if (charsRead <= 0)
					break;

				buffer.append(charBuffer, 0, charsRead);
			}
		}
		finally
		{
			reader.close();
		}

		// OK, now reuse the charBuffer variable to
		// produce the final result.

		length = buffer.length();

		charBuffer = new char[length];

		// Copy the character out of the StringBuffer and into the
		// array.

		buffer.getChars(0, length, charBuffer, 0);

		return charBuffer; 
	}
}


