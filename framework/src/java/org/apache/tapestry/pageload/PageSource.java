//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.pageload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.resolver.PageSpecificationResolver;
import org.apache.tapestry.services.ObjectPool;
import org.apache.tapestry.util.MultiKey;

/**
 *  A source for pages for a particular application.  Each application
 *  should have its own <code>PageSource</code>, storing it into the
 *  {@link javax.servlet.ServletContext} using a unique key (usually built from
 *  the application name).
 *
 *  <p>The <code>PageSource</code> acts as a pool for {@link IPage} instances.
 *  Pages are retrieved from the pool using {@link #getPage(IRequestCycle, String, IMonitor)}
 *  and are later returned to the pool using {@link #releasePage(IPage)}.
 *
 *
 *  <p>TBD: Pooled pages stay forever.  Need a strategy for cleaning up the pool,
 *  tracking which pages have been in the pool the longest, etc.  A mechanism
 *  for reporting pool statistics would be useful.
 *
 *  @author Howard Lewis Ship
 * 
 **/

public class PageSource implements IPageSource
{
    /**
     *  Key used to find PageLoader instances in the Pool.
     * 
     **/

    private static final String PAGE_LOADER_POOL_KEY = "org.apache.tapestry.PageLoader";

    /**
     * Key used to find {@link PageSpecificationResolver} instance
     * in the pool.
     */

    private static final String PAGE_SPECIFICATION_RESOLVER_KEY =
        "org.apache.tapestry.PageSpecificationResolver";

    private ClassResolver _resolver;

    /**
     *  The pool of {@link IPage}s.  The key is a {@link MultiKey},
     *  built from the page name and the page locale.  This is a reference
     *  to a shared pool.
     * 
     *  @see IEngine#getPool()
     *
     **/

    private ObjectPool _pool;

    public PageSource(IEngine engine)
    {
        _resolver = engine.getClassResolver();
        _pool = engine.getPool();
    }

    public ClassResolver getResourceResolver()
    {
        return _resolver;
    }

    /**
     *  Builds a key for a named page in the application's current locale.
     *
     **/

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
     **/

    protected MultiKey buildKey(IPage page)
    {
        Object[] keys;

        keys = new Object[] { page.getPageName(), page.getLocale()};

        // Don't make a copy, this array is just for the MultiKey.

        return new MultiKey(keys, false);
    }

    /**
     *  Gets the page from a pool, or otherwise loads the page.  This operation
     *  is threadsafe.
     *
     **/

    public IPage getPage(IRequestCycle cycle, String pageName, IMonitor monitor)
    {
        IEngine engine = cycle.getEngine();
        Object key = buildKey(engine, pageName);
        IPage result = (IPage) _pool.get(key);

        if (result == null)
        {
            monitor.pageCreateBegin(pageName);

            // Resolvers are not threadsafe, so we get one from
            // the pool or create as needed.

            PageSpecificationResolver pageSpecificationResolver =
                getPageSpecificationResolver(cycle);

            pageSpecificationResolver.resolve(cycle, pageName);

            // Likewise PageLoader

            PageLoader loader = getPageLoader(cycle);

            try
            {
                result =
                    loader.loadPage(
                        pageSpecificationResolver.getSimplePageName(),
                        pageSpecificationResolver.getNamespace(),
                        cycle,
                        pageSpecificationResolver.getSpecification());
            }
            finally
            {
                discardPageLoader(loader);
                discardPageSpecificationResolver(pageSpecificationResolver);
            }

            monitor.pageCreateEnd(pageName);
        }
        else
        {
            // The page loader attaches the engine, but a page from
            // the pool needs to be explicitly attached.

            result.attach(engine);
        }

        return result;
    }

    /**
     *  Invoked to obtain an instance of 
     *  {@link PageLoader}.  An instance if aquired from the pool or,
     *  if none are available, created fresh.
     * 
     *  @since 3.0
     * 
     **/

    protected PageLoader getPageLoader(IRequestCycle cycle)
    {
        PageLoader result = (PageLoader) _pool.get(PAGE_LOADER_POOL_KEY);

        if (result == null)
            result = new PageLoader(cycle);

        return result;
    }

    /**
     *  Invoked once the {@link PageLoader} is not
     *  longer needed; it is then returned to the pool.
     * 
     *  @since 3.0
     * 
     **/

    protected void discardPageLoader(PageLoader loader)
    {
        _pool.store(PAGE_LOADER_POOL_KEY, loader);
    }

    /**
     * Invoked to obtain an instance of {@link PageSpecificationResolver}.
     * An instance is acquired form the pool or, if none are available,
     * a new one is instantiated.
     * 
     * @since 3.0
     */

    protected PageSpecificationResolver getPageSpecificationResolver(IRequestCycle cycle)
    {
        PageSpecificationResolver result =
            (PageSpecificationResolver) _pool.get(PAGE_SPECIFICATION_RESOLVER_KEY);

        if (result == null)
            result = new PageSpecificationResolver(cycle);

        return result;
    }

    /**
     * Invoked once the {@link PageSpecificationResolver} is no longer
     * needed, it is returned to the pool.
     * 
     * @since 3.0
     */

    protected void discardPageSpecificationResolver(PageSpecificationResolver resolver)
    {
        _pool.store(PAGE_SPECIFICATION_RESOLVER_KEY, resolver);
    }

    /**
     *  Returns the page to the appropriate pool.  Invokes
     *  {@link IPage#detach()}.
     *
     **/

    public void releasePage(IPage page)
    {
        Tapestry.clearMethodInvocations();

        page.detach();

        Tapestry.checkMethodInvocation(Tapestry.ABSTRACTPAGE_DETACH_METHOD_ID, "detach()", page);

        _pool.store(buildKey(page), page);
    }

    /**
     *  Invoked (during testing primarily) to release the entire pool
     *  of pages, and the caches of bindings and assets.
     *  TODO: Probably remove this!
     */

    public synchronized void reset()
    {
        // _pool.clear();
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("pool", _pool);
        builder.append("resolver", _resolver);

        return builder.toString();
    }

}