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
import com.primix.tapestry.script.*;
import com.primix.tapestry.util.pool.*;
import com.primix.tapestry.util.xml.*;
import java.util.*;
import java.net.*;
import java.io.*;

/**
 *  Provides basic access to scripts available on the classpath.  Scripts are cached in
 *  memory once parsed.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.2
 */


public class DefaultScriptSource
	implements IScriptSource
{
	private IResourceResolver resolver;
	
	private static final String PARSER_KEY = "parser";
	
	private Pool parserPool = new Pool();
	private Map cache;
	
	private static final int MAP_SIZE = 17;
	
	public DefaultScriptSource(IResourceResolver resolver)
	{
		this.resolver = resolver;
	}
	
	public void reset()
	{
		parserPool.clear();
		cache = null;
	}
	
	public IScript getScript(String resourcePath)
		throws ResourceUnavailableException
	{
		IScript result;
		
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
			result = (IScript)cache.get(resourcePath);
		}
		
		if (result != null)
			return result;
		
		result = parse(resourcePath);
		
		// There's a small window if reset() is invoked on a very busy system where
		// cache could be null here.
		
		synchronized(cache)
		{
			cache.put(resourcePath, result);
		}
		
		return result;
	}
	
	private IScript parse(String resourcePath)
		throws ResourceUnavailableException
	{
		ScriptParser parser = (ScriptParser)parserPool.retrieve(PARSER_KEY);
		InputStream stream = null;
		
		if (parser == null)
			parser = new ScriptParser();
		
		try
		{
			URL url = resolver.getResource(resourcePath);

			stream = url.openStream();
			
			IScript result = parser.parse(stream, resourcePath);
			
			stream.close();
			
			return result;
		}
		catch (DocumentParseException ex)
		{
			throw new ResourceUnavailableException("Unable to parse script " + resourcePath + ".", ex);
		}
		catch (IOException ex)
		{
			throw new ResourceUnavailableException("Unable to read script " + resourcePath + ".", ex);
		}
		finally
		{
			Tapestry.close(stream);
			
			parserPool.store(PARSER_KEY, parser);
		}
	}
	
}

