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

package com.primix.tapestry.engine;

import com.primix.tapestry.*;
import com.primix.tapestry.parse.*;
import com.primix.tapestry.util.*;
import com.primix.tapestry.spec.*;
import java.io.*;
import java.util.*;
import java.net.URL;
import org.apache.log4j.*;

/**
 *  Default implementation of {@link ITemplateSource}.  Templates, once parsed,
 *  stay in memory until explicitly cleared.
 *
 *  <p>An instance of this class acts as a singleton shared by all sessions, so it
 *  must be threadsafe.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class DefaultTemplateSource 
implements ITemplateSource
{
	private static final Category CAT =
		Category.getInstance(DefaultTemplateSource.class);
		 
	private static final int MAP_SIZE = 11;

    // Cache of previously retrieved templates.  Key is a multi-key of 
    // specification resource path and locale (local may be null), value
    // is the ComponentTemplate.

	private Map cache;

    // Previously read templates; key is the HTML resource path, value
    // is the ComponentTemplate.

    private Map templates;

	/**
	 *  Number of tokens (each template contains multiple tokens).
	 *
	 */
	
	private int tokenCount;
	
	private static final int BUFFER_SIZE = 2000;

	private IResourceResolver resolver;
	private TemplateParser parser;

	private static class ParserDelegate
		implements ITemplateParserDelegate
	{
		IComponent component;
		
		ParserDelegate(IComponent component)
		{
			this.component = component;
		}
		
		public boolean getKnownComponent(String componentId)
		{
			try
			{
				component.getComponent(componentId);
				
				return true;
			}
			catch (NoSuchComponentException ex)
			{
				return false;
			}
		}
		
		public boolean getAllowBody(String componentId)
		{
			return component.getComponent(componentId).getSpecification().getAllowBody();
		}
	}
	
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
        cache = null;
		templates = null;
		
		tokenCount = 0;
	}

	/**
	*  Reads the template for the component.
	*
	*  <p>Returns null if the template can't be found.
	*/

	public ComponentTemplate getTemplate(IComponent component)
	throws ResourceUnavailableException
	{
	ComponentSpecification specification = component.getSpecification();
		String specificationResourcePath = specification.getSpecificationResourcePath();
       Locale locale = component.getPage().getLocale();

        Object key = new MultiKey(new Object[] 
        { specificationResourcePath, locale
        }, false);

        ComponentTemplate result = searchCache(key);
        if (result != null)
            return result;

        result = findTemplate(specificationResourcePath, component, locale);

        if (result == null)
        {
            StringBuffer buffer = new StringBuffer();
            buffer.append("Could not find template for component ");
            buffer.append(specificationResourcePath);

            if (locale != null)
            {
                buffer.append(" in locale ");
                buffer.append(locale.toString());
            }

            buffer.append('.');

            throw new ResourceUnavailableException(buffer.toString());
        }

        saveToCache(key, result);

		return result;
	}

    private ComponentTemplate searchCache(Object key)
    {
        if (cache == null)
            return null;

        synchronized(cache)
        {
            return (ComponentTemplate)cache.get(key);
        }
    }

    private void saveToCache(Object key, ComponentTemplate template)
    {
        if (cache == null)
        {
            synchronized(this)
            {
                if (cache == null)
                    cache = new HashMap(MAP_SIZE);
            }
        }

        synchronized(cache)
        {
            cache.put(key, template);
        }
    }

    /**
     *  Search for the template corresponding to the resource and the locale.
     *  This may be in the template map already, or may involve reading and
     *  parsing the template.
     *
     */

    private ComponentTemplate findTemplate(String specificationResourcePath, 
			IComponent component,
			Locale locale)
    throws ResourceUnavailableException
    {
        int dotx;
        StringBuffer buffer;
        int rawLength;
        String candidatePath = null;
        String language = null;
        String country = null;
        int start = 2;
        ComponentTemplate result = null;

   		if (CAT.isDebugEnabled())
			CAT.debug("Searching for localized version of template for " + 
				specificationResourcePath + " in locale " + locale.getDisplayName());
				
        // Just easier to lock the template cache once, for the duration.

        if (templates == null)
        {
            synchronized(this)
            {
                if (templates == null)
                    templates = new HashMap(MAP_SIZE);
            }
        }

        dotx = specificationResourcePath.lastIndexOf('.');
        buffer = new StringBuffer (dotx + 20);
        buffer.append(specificationResourcePath.substring(0, dotx));
        rawLength = buffer.length();

        if (locale != null)
        {
            country = locale.getCountry();
            if (country.length() > 0)
                start--;

            // This assumes that you never have the case where there's
            // a null language code and a non-null country code.

            language = locale.getLanguage();
            if (language.length() > 0)
                start--;
        }


        // On pass #0, we use language code and country code
        // On pass #1, we use language code
        // On pass #2, we use neither.
        // We skip pass #0 or #1 depending on whether the language code
        // and/or country code is null.

        synchronized(templates)
        {
            for (int i = start; i < 3; i++)
            {
                buffer.setLength(rawLength);

                if (i < 2)
                {
                    buffer.append('_');
                    buffer.append(language);
                }

                if (i == 0)
                {
                    buffer.append('_');
                    buffer.append(country);
                }

                buffer.append(".html");

                candidatePath = buffer.toString();

                // See if it's been parsed before

                result = (ComponentTemplate)templates.get(candidatePath);
                if (result != null)
                    break;
        
                // Ok, see if it exists.

                result = parseTemplate(candidatePath, component);

                if (result != null)
                {
                    templates.put(candidatePath, result);
                    break;
                }
            }
        }
				
        return result;
    }
    
    /**
     *  Reads the template for the given resource; returns null if the
     *  resource doesn't exist.  Note that this method is only invoked
	 *  from a synchronized block, so there shouldn't be threading
	 *  issues here.
     *
     */

	private ComponentTemplate parseTemplate(String resourceName,
		IComponent component)
	throws ResourceUnavailableException
	{
		TemplateToken[] tokens;

		char[] templateData = readTemplate(resourceName);
        if (templateData == null)
            return null;

		if (parser == null)
			parser = new TemplateParser();

		ITemplateParserDelegate delegate = new ParserDelegate(component);
		
		// Once we have the template data in memory, the parse will always be successful.
		// In the future, the parser may be more complicated and will be able to
		// detect errors in the template data.

		try
		{
			tokens = parser.parse(templateData, delegate, resourceName);
		}
		catch (TemplateParseException ex)
		{
			throw new ResourceUnavailableException(
				"Unable to parse template " + resourceName + ".", ex);
		}

		if (CAT.isDebugEnabled())
			CAT.debug("Parsed " + tokens.length + " tokens from template");
	
		tokenCount += tokens.length;
		
		return new ComponentTemplate(templateData, tokens);
	}

    /**
     *  Reads the template, given the complete path to the
     *  resource.  Returns null if the resource doesn't exist.
     *
     */

	private char[] readTemplate(String resourceName)
	throws ResourceUnavailableException
	{
    	URL url;
		InputStream stream = null;

		url = resolver.getResource(resourceName);
        
		if (url == null)
            return null;

        if (CAT.isDebugEnabled())
        	CAT.debug("Reading template " + resourceName + " from " + url);
        	
		try
		{
			stream = url.openStream();
        
        	return readTemplateStream(stream);
		}
		catch (IOException ex)
		{
			throw new ResourceUnavailableException("Could not read from " + 
				resourceName + ".", ex);
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

    /**
     *  Reads a Stream into memory as an array of characters.
     *
     */

	private char[] readTemplateStream(InputStream stream)
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

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("DefaultTemplateSource@");
		buffer.append(Integer.toHexString(hashCode()));
		
		buffer.append('[');
		
		if (cache != null)
		{
			synchronized(cache)
			{
				buffer.append(cache.keySet());
			}
		}

		
		if (tokenCount > 0)
		{
			buffer.append(", ");
			buffer.append(tokenCount);
			buffer.append(" tokens");
		}
		
		buffer.append(']');
		
		return buffer.toString();
	}
}


