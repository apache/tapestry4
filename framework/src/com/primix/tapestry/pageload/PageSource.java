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
 *
 *  <p>In addition, this class acts as a cache of serveral common
 *  objects:
 *  <ul>
 *  <li>{@link FieldBinding}
 *  <li>{@link StaticBinding}
 *  <li>{@link ExternalAsset}
 *  <li>{@link ContextAsset}
 *  <li><{@link PrivateAsset}
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

public class PageSource implements IPageSource, IRenderDescription
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

	private static final String PAGE_LOADER_KEY = "PageLoader";

	public PageSource(IResourceResolver resolver)
	{
		this.resolver = resolver;

		pool = new Pool();
	}

	public IResourceResolver getResourceResolver()
	{
		return resolver;
	}

	/**
	 *  Builds a key for a named page in the application's current locale.
	 *
	 */

	protected MultiKey buildKey(IEngine engine, String pageName)
	{
		Object[] keys;

		keys = new Object[] { pageName, engine.getLocale()};

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

		keys = new Object[] { page.getName(), page.getLocale()};

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
		IPage result = (IPage) pool.retrieve(key);

		if (result == null)
		{
			if (monitor != null)
				monitor.pageCreateBegin(pageName);

			PageSpecification specification =
				engine.getSpecification().getPageSpecification(pageName);

			if (specification == null)
				throw new ApplicationRuntimeException(
					Tapestry.getString("PageLoader.no-such-page", pageName));

			PageLoader loader = retrievePageLoader();

			result =
				loader.loadPage(pageName, engine, specification.getSpecificationPath());

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
		PageLoader result = (PageLoader) pool.retrieve(PAGE_LOADER_KEY);

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

		pool.store(buildKey(page), page);
	}

	/**
	 *  Invoked (during testing primarily) to release the entire pool
	 *  of pages, and the caches of bindings and assets.
	 *
	 */

	public void reset()
	{
		synchronized (this)
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

	public IBinding getFieldBinding(String fieldName)
	{
		IBinding result = null;

		if (fieldBindings == null)
		{
			synchronized (this)
			{
				if (fieldBindings == null)
					fieldBindings = new HashMap(MAP_SIZE);
			}
		}

		synchronized (fieldBindings)
		{
			result = (IBinding) fieldBindings.get(fieldName);

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

	public IBinding getStaticBinding(String value)
	{
		IBinding result = null;

		if (staticBindings == null)
		{
			synchronized (this)
			{
				if (staticBindings == null)
					staticBindings = new HashMap(MAP_SIZE);
			}
		}

		synchronized (staticBindings)
		{
			result = (IBinding) staticBindings.get(value);

			if (result == null)
			{
				result = new StaticBinding(value);

				staticBindings.put(value, result);
			}
		}

		return result;
	}

	public IAsset getExternalAsset(String URL)
	{
		IAsset result = null;

		if (externalAssets == null)
		{
			synchronized (this)
			{
				if (externalAssets == null)
					externalAssets = new HashMap(MAP_SIZE);
			}
		}

		synchronized (externalAssets)
		{
			result = (IAsset) externalAssets.get(URL);

			if (result == null)
			{
				result = new ExternalAsset(URL);
				externalAssets.put(URL, result);
			}
		}

		return result;
	}

	public IAsset getContextAsset(String assetPath)
	{
		IAsset result = null;

		if (contextAssets == null)
		{
			synchronized (this)
			{
				if (contextAssets == null)
					contextAssets = new HashMap(MAP_SIZE);
			}
		}

		synchronized (contextAssets)
		{
			result = (IAsset) contextAssets.get(assetPath);

			if (result == null)
			{
				result = new ContextAsset(assetPath);
				contextAssets.put(assetPath, result);
			}
		}

		return result;

	}

	public IAsset getPrivateAsset(String resourcePath)
	{
		IAsset result = null;

		if (privateAssets == null)
		{
			synchronized (this)
			{
				if (privateAssets == null)
					privateAssets = new HashMap(MAP_SIZE);
			}
		}

		synchronized (privateAssets)
		{
			result = (IAsset) privateAssets.get(resourcePath);

			if (result == null)
			{
				result = new PrivateAsset(resourcePath);
				privateAssets.put(resourcePath, result);
			}
		}

		return result;
	}

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("PageSource@");
		buffer.append(Integer.toHexString(hashCode()));
		buffer.append('[');

		if (pool != null)
		{
			buffer.append("pool=");
			buffer.append(pool);
		}

		extend(buffer, fieldBindings, "field bindings");
		extend(buffer, staticBindings, "static bindings");
		extend(buffer, externalAssets, "external assets");
		extend(buffer, contextAssets, "context assets");
		extend(buffer, privateAssets, "private assets");

		int lastChar = buffer.length() - 1;

		buffer.append(']');

		return buffer.toString();
	}

	private void extend(StringBuffer buffer, Map map, String label)
	{
		if (map == null)
			return;

		int count;

		synchronized (map)
		{
			count = map.size();
		}

		if (count == 0)
			return;

		char ch = buffer.charAt(buffer.length() - 1);
		if (ch != ' ' && ch != '[')
			buffer.append(", ");

		buffer.append(count);
		buffer.append(" cached ");
		buffer.append(label);
	}

	/** @since 1.0.6 **/

	public void renderDescription(IResponseWriter writer)
	{
		writer.print("PageSource");
		writer.begin("ul");

		if (pool != null)
		{
			writer.begin("li");
			writer.print("pool = ");
			pool.renderDescription(writer);
			writer.end();
		}

		describe(writer, fieldBindings, "field bindings");
		describe(writer, staticBindings, "static bindings");
		describe(writer, externalAssets, "external assets");
		describe(writer, contextAssets, "context assets");
		describe(writer, privateAssets, "private assets");

		writer.end(); // <ul>
	}

	/** @since 1.0.6 **/

	private void describe(IResponseWriter writer, Map map, String label)
	{
		if (map == null)
			return;

		synchronized (map)
		{
			Set entrySet = map.entrySet();
			int count = entrySet.size();

			if (count > 0)
			{
				writer.begin("li");
				writer.print(" ");

				writer.print(count);
				writer.print(" cached ");
				writer.print(label);

				writer.begin("ul");

				Iterator i = map.entrySet().iterator();

				while (i.hasNext())
				{
					Map.Entry e = (Map.Entry) i.next();

					writer.begin("li");
					writer.print(e.getKey().toString());
					writer.println();
					writer.end();
				}

				writer.end(); // <ul>
				writer.end(); // <li>

			}
		}
	}
}