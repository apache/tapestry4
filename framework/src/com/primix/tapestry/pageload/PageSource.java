package com.primix.tapestry.pageload;

import javax.servlet.*;
import com.primix.foundation.MultiKey;
import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import java.util.*;
import com.primix.tapestry.binding.*;

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
 *  A source for pages for a particular application.  Each application
 *  should have its own <code>PageSource</code>, storing it into the
 *  <code>ServletContext</code> using a unique key (usually built from
 *  the application name).
 *
 *  <p>The <code>PageSource</code> acts as a pool for {@link IPage} instances.
 *  Pages are retrieved from the pool using {@link #getPage(IApplication, String, IMonitor)}
 *  and are later returned to the pool using {@link #releasePage(IPage)}.
 *
 *  <p>Acts as a cache of {@link FieldBinding}s.  This allows a {@link FieldBinding} to
 * be created once, and used across all components and pages.
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
	private static final int MAP_SIZE = 11;
    private Map fieldBindings;

    private IResourceResolver resolver;

    public PageSource(IResourceResolver resolver)
    {
        this.resolver = resolver;
    }

    public IResourceResolver getResourceResolver()
    {
        return resolver;
    }



	/**
	*  The pool of <code>PooledPage</code>s.  The key is a {@link MultiKey},
	*  built from the application name, the page name, and the page locale.
	*
	*/

	private Map pool;

	/**
	*  Builds a key for a named page in the application's current locale.
	*
	*/

	protected MultiKey buildKey(IApplication application, String pageName)
	{
		Object[] keys;

		keys = new Object[] { pageName,
                              application.getLocale() };

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

	public IPage getPage(IApplication application, String pageName, IMonitor monitor)
	throws PageLoaderException
	{
		Object key;
		PooledPage pooled = null;
		PageLoader loader;
		IPage result = null;
		String resource;
		PageSpecification specification;

		key = buildKey(application, pageName);

		if (pool != null)
		{
			// Need to synchronize for TWO operations:  getting the current
			// PooledPage, then updating the key back to the
			// next int he PoolePage list.

			synchronized(pool)
			{

				pooled = (PooledPage)pool.get(key);

				if (pooled != null)
				{
					if (pooled.next == null)
						pool.remove(key);
					else
						pool.put(key, pooled.next);
				}
			}

			// Not synchronized

			if (pooled != null)
			{
				result = pooled.page;

				// Pooled pages are detached from their original application, and
				// must join the new application.

				result.joinApplication(application);
			}

		}

		if (result == null)
		{
			if (monitor != null)
				monitor.pageCreateBegin(pageName);

			// Ok, need to load the page.  Note that we should also pool the page loader, instead
			// we inneficiently create - use - discard it.

			loader = new PageLoader(this, application);

			specification = application.getSpecification().getPageSpecification(pageName);

			if (specification == null)
				throw new ApplicationRuntimeException(
					"This application does not contain a page named " + pageName + "."); 

			result = loader.loadPage(pageName, specification.getSpecificationPath());

            // Because of support for new style (no arguments) constructor, we must
            // always join the application explicitly.

            result.joinApplication(application);

            // Alas, the page loader is discarded, we should be pooling those as
            // well.

			if (monitor != null)
				monitor.pageCreateEnd(pageName);
		}

		return result;
	}

	/**
	*  Returns the page to the appropriate pool.
	*
	*/

	public void releasePage(IPage page)
	{
		Object key;
		PooledPage next;
		PooledPage pooled;

		key = buildKey(page);

		page.detachFromApplication();

		if (pool == null)
			pool = new HashMap(MAP_SIZE);

		// Lock the pool for TWO operations:  getting the old head of the
		// list (into next), then putting the new head of the list
		// into the pool.

		synchronized(pool)
		{
			next = (PooledPage)pool.get(key);

			pooled = new PooledPage(page, next);

			pool.put(key, pooled);
		}

	}

	/**
	*  Invoked (during testing primarily) to release the entire pool
	*  of pages and bindings.
	*
	*/

	public void reset()
	{
		pool = null;
        fieldBindings = null;
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
            fieldBindings = new HashMap(MAP_SIZE);
        else
            result = (IBinding)fieldBindings.get(fieldName);
 
        if (result == null)
        {
            result = new FieldBinding(resolver, fieldName);

            fieldBindings.put(fieldName, result);
        }

        return result;
    }
}

