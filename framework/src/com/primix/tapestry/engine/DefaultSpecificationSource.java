package com.primix.tapestry.engine;

import java.net.*;
import java.io.*;
import com.primix.tapestry.*;
import com.primix.tapestry.parse.*;
import com.primix.tapestry.spec.*;
import java.util.*;
import org.log4j.*;

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
 *  Default implementation of {@link ISpecificationSource} that
 *  expects to use the normal class loader to locate component
 *  specifications from within the classpath.
 *
 * <p>Caches specifications in memory forever, or until {@link #reset()} is invoked.
 *
 * <p>An instance of this class acts like a singleton and is shared by multiple sessions,
 * so it must be threadsafe.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class DefaultSpecificationSource 
    implements ISpecificationSource
{
	private static final Category CAT = 
		Category.getInstance(DefaultSpecificationSource.class.getName());
	
	private IResourceResolver resolver;
	protected ApplicationSpecification specification;

	private SpecificationParser parser;

	private static final int MAP_SIZE = 23;
	
	/**
	*  Contains previously parsed specification.
	*
	*/

	protected Map cache;

	public DefaultSpecificationSource(IResourceResolver resolver,
		ApplicationSpecification specification)
	{
		this.resolver = resolver;
		this.specification = specification;
	}

	/**
	*  Clears the specification cache.  This is used during debugging.
	*
	*/

	public void reset()
	{
		cache = null;
	}

	/**
	*  Gets a specification.  The type is either a component specification
	*  path, or an alias to a component (registerred in the application
	*  specification).  The former always starts with a slash, the latter
	*  never does.
	*
	*  <p>If an alias (i.e, start with a slash), then the value is passed through
	*  {@link ApplicationSpecification#getComponentAlias(String)} to
	*  get a resource on the classpath that is parsed.
	*
	*/

	public synchronized ComponentSpecification getSpecification(String type)
	throws ResourceUnavailableException
	{
		ComponentSpecification result = null;
		String resourceName;

		if (cache != null)
			result = (ComponentSpecification)cache.get(type);

		if (result == null)
		{
			if (type.startsWith("/"))
				resourceName = type;
			else
			{
				resourceName = specification.getComponentAlias(type);
				
				if (resourceName == null)
					throw new ResourceUnavailableException(
						"Could not find a component matching alias " + type + ".");
			}	

			result = parseSpecification(resourceName);

			if (cache == null)
				cache = new HashMap(MAP_SIZE);

			cache.put(type, result);
			if (resourceName != type)
				cache.put(resourceName, result);
		}

		return result;
	}

	protected ComponentSpecification parseSpecification(String resourcePath)
	throws ResourceUnavailableException
	{
		ComponentSpecification result = null;
		URL URL;
		InputStream inputStream;

		if (CAT.isDebugEnabled())
			CAT.debug("Parsing component specification " + resourcePath);

		URL = resolver.getResource(resourcePath);

		if (URL == null)
		{
			throw new ResourceUnavailableException("Could not locate resource " +
				resourcePath + " in the classpath.");
		}

		try
		{
			inputStream = URL.openStream();
		}
		catch (IOException e)
		{
			throw new ResourceUnavailableException("Could not open specification " +
				resourcePath + ".", e);
		}

		if (parser == null)
			parser = new SpecificationParser();

		try
		{
			result = parser.parseComponentSpecification(inputStream, resourcePath);
		}
		catch (SpecificationParseException e)
		{
			throw new ResourceUnavailableException("Could not parse specification " +
				resourcePath + ".", e);
		}

		result.setSpecificationResourcePath(resourcePath);

		return result;
	}
}

