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

package com.primix.tapestry.pageload;

import javax.servlet.*;
import com.primix.tapestry.util.MultiKey;
import com.primix.tapestry.util.pool.*;
import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import java.util.*;
import com.primix.tapestry.binding.*;
import com.primix.tapestry.asset.*;

/**
 *  A source for pages for a particular application.  Each application
 *  should have its own <code>PageSource</code>, storing it into the
 *  {@link ServletContext} using a unique key (usually built from
 *  the application name).
 *
 *  <p>The <code>PageSource</code> acts as a pool for {@link IPage} instances.
 *  Pages are retrieved from the pool using {@link #getPage(IEngine, String, IMonitor)}
 *  and are later returned to the pool using {@link #releasePage(IPage)}.
 *
 *  <p>During development, it is useful to occasionally disable pooling of pages.
 *  This will slow down requests as new page object hierarchies will have to be
 *  created for each request.  However, it will identify a common Tapestry
 *  developer failure:  expecting transient page properties to be available
 *  on subsequent request cycles (this is usually related to
 *  an incomplete implementation of {@link IPage#detach()}).
 *
 *  <p>Setting the JVM system property
 * <code>com.primix.tapestry.disable-page-pool</code>
 *  will turn pooling off (pages will always be discarded at the end of
 *  the request cycle, never recycled in subsequent cycles).
 *
 *  <p>In addition, this class acts as a cache of serveral common
 *  objects:
 *  <ul>
 *  <li>{@link FieldBinding}
 *  <li>{@link StaticBinding}
 *  <li>{@link ExternalAsset}
 *  <li>{@link ContextAsset}
 *  </ul>
 *
 *  <p>This caching allows common objects to be created once, and
 *  used across all components and pages.  Without pooling, objects would often be duplicated.
 *
 *
 * <p>TBD: Pooled pages stay forever.  Need a strategy for cleaning up the pool,
 * tracking which pages have been in the pool the longest, etc.  A mechanism
 * for reporting pool statistics would be useful.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class PageSource 
    implements IPageSource
{
	private Map fieldBindings;
	private Map staticBindings;
	private Map externalAssets;
	private Map contextAssets;
	private Map privateAssets;
	private IResourceResolver resolver;
	private static final int MAP_SIZE = 23;

	/**
	*  The pool of {@link PooledPage}s.  The key is a {@link MultiKey},
	*  built from the application name, the page name, and the page locale.
	*  It is also used to pool {@link PageLoader}s.
	*
	*/

	private Pool pool;
	private boolean poolDisabled;

	private static final String PAGE_LOADER_KEY = "PageLoader";

	public PageSource(IResourceResolver resolver)
	{
		this.resolver = resolver;

		poolDisabled = Boolean.getBoolean("com.primix.tapestry.disable-page-pool");

		pool = new Pool();
	}

	public IResourceResolver getResourceResolver()
	{
		return resolver;
	}

	/**
	*  Returns true if the page pool has been disabled.
	*
	*/

	public boolean isPoolDisabled()
	{
		return poolDisabled;
	}



	/**
	*  Builds a key for a named page in the application's current locale.
	*
	*/

	protected MultiKey buildKey(IEngine engine, String pageName)
	{
		Object[] keys;

		keys = new Object[] { pageName,
                              engine.getLocale() };

		// Don't make a copy, this array is just for the MultiKey.

		return new MultiKey(keys, false);
	}

	/**
	*  Builds a key from an existing page, using the page's name and locale.  This is
	*  used when storing a page into the pool.
	*
	*/

	protected MultiKey buildKey(IPage page)
	{
		Object[] keys;

		keys = new Object[] { page.getName(),
                              page.getLocale() };

		// Don't make a copy, this array is just for the MultiKey.

		return new MultiKey(keys, false);
	}

	/**
	*  Gets the page from a pool, or otherwise loads the page.  This operation
	*  is threadsafe ... it synchronizes on the pool of pages.
	*

	*/

	public IPage getPage(IEngine engine, String pageName, IMonitor monitor)
	throws PageLoaderException
	{
		Object key = buildKey(engine, pageName);
		IPage result = null;

		if (!poolDisabled)
			result = (IPage)pool.retrieve(key);

		if (result == null)
		{
			if (monitor != null)
				monitor.pageCreateBegin(pageName);

			PageSpecification specification = 
				engine.getSpecification().getPageSpecification(pageName);

			if (specification == null)
				throw new ApplicationRuntimeException(
					"This application does not contain a page named " + pageName + "."); 

			PageLoader loader = retrievePageLoader();

			result = loader.loadPage(pageName, engine, specification.getSpecificationPath());

			storePageLoader(loader);

			// Alas, the page loader is discarded, we should be pooling those as
			// well.

			if (monitor != null)
				monitor.pageCreateEnd(pageName);
		}

		// Whether its new or reused, it must join the engine.

		result.attach(engine);

		return result;
	}

	private PageLoader retrievePageLoader()
	{
		PageLoader result = (PageLoader)pool.retrieve(PAGE_LOADER_KEY);
		
		if (result == null)
			result = new PageLoader(this);

		return result;	
	}
	
	private void storePageLoader(PageLoader loader)
	{
		pool.store(PAGE_LOADER_KEY, loader);
	}

	/**
	*  Returns the page to the appropriate pool.
	*
	*/

	public void releasePage(IPage page)
	{
		page.detach();

		if (!poolDisabled)
			pool.store(buildKey(page), page);
	}

	/**
	*  Invoked (during testing primarily) to release the entire pool
	*  of pages, and the caches of bindings and assets.
	*
	*/

	public void reset()
	{
		synchronized(this)
		{
			pool.clear();

			fieldBindings = null;
			staticBindings = null;
			externalAssets = null;
			contextAssets = null;
			privateAssets = null;
		}
	}

	/**
	*  Gets a field binding for the named field (the name includes the class name
	*  and the field).  If no such binding exists, then one is created, otherwise
	*  the existing binding is returned. 
	*
	*/

	public synchronized IBinding getFieldBinding(String fieldName)
	{
		IBinding result = null;

		if (fieldBindings == null)
		{
			synchronized(this)
			{
				if (fieldBindings == null)
					fieldBindings = new HashMap(MAP_SIZE);
			}	
		}

		synchronized(fieldBindings)
		{
			result = (IBinding)fieldBindings.get(fieldName);

			if (result == null)
			{
				result = new FieldBinding(resolver, fieldName);

				fieldBindings.put(fieldName, result);
			}
		}

		return result;
	}

	/**
	*  Like {@link #getFieldBinding(String)}, except for {@link StaticBinding}s.
	*
	*/

	public synchronized IBinding getStaticBinding(String value)
	{
		IBinding result = null;

		if (staticBindings == null)
		{
			synchronized(this)
			{
				if (staticBindings == null)
					staticBindings = new HashMap(MAP_SIZE);
			}
		}
		
		synchronized(staticBindings)
		{
			result = (IBinding)staticBindings.get(value);

			if (result == null)
			{
				result = new StaticBinding(value);

				staticBindings.put(value, result);
			}
		}

		return result;
	}

	public synchronized IAsset getExternalAsset(String URL)
	{
		IAsset result = null;

		if (externalAssets == null)
		{
			synchronized(this)
			{
				if (externalAssets == null)
					externalAssets = new HashMap(MAP_SIZE);
			}
		}
		
		synchronized(externalAssets)
		{
			result = (IAsset)externalAssets.get(URL);

			if (result == null)
			{
				result = new ExternalAsset(URL);
				externalAssets.put(URL, result);
			}
		}

		return result;
	}

	public synchronized IAsset getContextAsset(String assetPath)
	{
		IAsset result = null;

		if (contextAssets == null)
		{
			synchronized(this)
			{
				if (contextAssets == null)
					contextAssets = new HashMap(MAP_SIZE);
			}
		}
		
		synchronized(contextAssets)
		{
			result = (IAsset)contextAssets.get(assetPath);

			if (result == null)
			{
				result = new ContextAsset(assetPath);
				contextAssets.put(assetPath, result);
			}
		}
		
		return result;

	}

	public synchronized IAsset getPrivateAsset(String resourcePath)
	{
		IAsset result = null;

		if (privateAssets == null)
		{
			synchronized(this)
			{
				if (privateAssets == null)
					privateAssets = new HashMap(MAP_SIZE);
			}
		}
		
		synchronized(privateAssets)
		{
			result = (IAsset)privateAssets.get(resourcePath);

			if (result == null)
			{
				result = new PrivateAsset(resourcePath);
				privateAssets.put(resourcePath, result);
			}
		}
		
		return result;
	}
}

