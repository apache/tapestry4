/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.pageload;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceResolver;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IMonitor;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.resolver.PageSpecificationResolver;
import org.apache.tapestry.util.MultiKey;
import org.apache.tapestry.util.pool.Pool;

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
 *  @version $Id$
 * 
 **/

public class PageSource implements IPageSource
{
    /**
     *  Key used to find PageLoader instances in the Pool.
     * 
     **/

    private static final String PAGE_LOADER_POOL_KEY = "org.apache.tapestry.PageLoader";

    private IResourceResolver _resolver;

    /**
     *  The pool of {@link PooledPage}s.  The key is a {@link MultiKey},
     *  built from the page name and the page locale.  This is a reference
     *  to a shared pool.
     * 
     *  @see IEngine#getPool()
     *
     **/

    private Pool _pool;

    /**
     *  Used to resolve page names to a namespace, a simple name, and a page specification.
     * 
     *  @since 3.0
     * 
     **/

    private PageSpecificationResolver _pageSpecificationResolver;

    public PageSource(IEngine engine)
    {
        _resolver = engine.getResourceResolver();
        _pool = engine.getPool();
    }

    public IResourceResolver getResourceResolver()
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
        IPage result = (IPage) _pool.retrieve(key);

        if (result == null)
        {
            monitor.pageCreateBegin(pageName);

            if (_pageSpecificationResolver == null)
                _pageSpecificationResolver = new PageSpecificationResolver(cycle);

            _pageSpecificationResolver.resolve(cycle, pageName);

            // Page loader's are not threadsafe, so we create a new
            // one as needed.  However, they would make an excellent
            // candidate for pooling.

            PageLoader loader = getPageLoader(cycle);

            try
            {
                result =
                    loader.loadPage(
                        _pageSpecificationResolver.getSimplePageName(),
                        _pageSpecificationResolver.getNamespace(),
                        cycle,
                        _pageSpecificationResolver.getSpecification());
            }
            finally
            {
                discardPageLoader(loader);
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
        PageLoader result = (PageLoader) _pool.retrieve(PAGE_LOADER_POOL_KEY);

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
     *
     **/

    public synchronized void reset()
    {
        _pool.clear();
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("pool", _pool);
        builder.append("resolver", _resolver);

        return builder.toString();
    }

}