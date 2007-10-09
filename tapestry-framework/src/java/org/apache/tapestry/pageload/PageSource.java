// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.commons.pool.BaseKeyedPoolableObjectFactory;
import org.apache.commons.pool.impl.GenericKeyedObjectPool;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.events.RegistryShutdownListener;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.event.ReportStatusEvent;
import org.apache.tapestry.event.ReportStatusListener;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.internal.pageload.PageKey;
import org.apache.tapestry.resolver.PageSpecificationResolver;

/**
 * A source for pages for a particular application. Each application should have its own
 * <code>PageSource</code>, storing it into the {@link javax.servlet.ServletContext}using a
 * unique key (usually built from the application name).
 * <p>
 * The <code>PageSource</code> acts as a pool for {@link IPage}instances. Pages are retrieved
 * from the pool using {@link #getPage(IRequestCycle, String)}and are later returned to
 * the pool using {@link #releasePage(IPage)}.
 * <p>
 * TBD: Pooled pages stay forever. Need a strategy for cleaning up the pool, tracking which pages
 * have been in the pool the longest, etc.
 *
 * @author Howard Lewis Ship
 */

public class PageSource extends BaseKeyedPoolableObjectFactory implements IPageSource, ResetEventListener, ReportStatusListener, RegistryShutdownListener {
    
    /** set by container. */
    private ClassResolver _classResolver;

    /** @since 4.0 */
    private PageSpecificationResolver _pageSpecificationResolver;

    /** @since 4.0 */

    private IPageLoader _loader;

    private IPropertySource _propertySource;

    private String _serviceId;

    /**
     * Thread safe reference to current request.
     */
    private IRequestCycle _cycle;

    static final long MINUTE = 1000 * 60;
    
    /**
     * The pool of {@link IPage}s. The key is a {@link org.apache.tapestry.util.MultiKey}, 
     * built from the page name and the page locale.
     */
    GenericKeyedObjectPool _pool;

    public void initializeService()
    {
        _pool = new GenericKeyedObjectPool(this);

        _pool.setMaxActive(Integer.parseInt(_propertySource.getPropertyValue("org.apache.tapestry.page-pool-max-active")));
        _pool.setMaxIdle(Integer.parseInt(_propertySource.getPropertyValue("org.apache.tapestry.page-pool-max-idle")));

        _pool.setMinIdle(Integer.parseInt(_propertySource.getPropertyValue("org.apache.tapestry.page-pool-min-idle")));
        
        _pool.setMinEvictableIdleTimeMillis(MINUTE * Long.parseLong(_propertySource.getPropertyValue("org.apache.tapestry.page-pool-evict-idle-page-minutes")));
        _pool.setTimeBetweenEvictionRunsMillis(MINUTE * Long.parseLong(_propertySource.getPropertyValue("org.apache.tapestry.page-pool-evict-thread-sleep-minutes")));
        
        _pool.setTestWhileIdle(false);
        _pool.setTestOnBorrow(false);
        _pool.setTestOnReturn(false);
    }

    public void registryDidShutdown()
    {
        try
        {
            _pool.close();
        } catch (Exception e) {
            // ignore
        }
    }

    public ClassResolver getClassResolver()
    {
        return _classResolver;
    }

    /**
     * Builds a key for a named page in the application's current locale.
     *
     * @param engine
     *          The current engine servicing this request.
     * @param pageName
     *          The name of the page to build key for.
     *
     * @return The unique key for ths specified page and current {@link java.util.Locale}. 
     */

    protected PageKey buildKey(IEngine engine, String pageName)
    {
        return new PageKey(pageName, engine.getLocale());
    }

    /**
     * Builds a key from an existing page, using the page's name and locale. This is used when
     * storing a page into the pool.
     *
     * @param page
     *          The page to build the key for.
     *
     * @return The unique key for the specified page instance.
     */

    protected PageKey buildKey(IPage page)
    {
        return new PageKey(page.getPageName(), page.getLocale());
    }

    public Object makeObject(Object key)
      throws Exception
    {
        PageKey pageKey = (PageKey) key;

        _pageSpecificationResolver.resolve(_cycle, pageKey.getPageName());

        // The loader is responsible for invoking attach(),
        // and for firing events to PageAttachListeners

        return _loader.loadPage(_pageSpecificationResolver.getSimplePageName(),
                                _pageSpecificationResolver.getNamespace(),
                                _cycle,
                                _pageSpecificationResolver.getSpecification());
    }

    /**
     * Gets the page from a pool, or otherwise loads the page. This operation is threadsafe.
     */

    public IPage getPage(IRequestCycle cycle, String pageName)
    {

        IEngine engine = cycle.getEngine();
        Object key = buildKey(engine, pageName);

        IPage result;

        // lock our page specific key lock first
        // This is only a temporary measure until a more robust
        // page pool implementation can be created.

        try
        {
            result = (IPage) _pool.borrowObject(key);
            
        } catch (Exception ex)
        {
            if (RuntimeException.class.isInstance(ex))
                throw (RuntimeException)ex;
            else
                throw new ApplicationRuntimeException(PageloadMessages.errorPagePoolGet(key), ex);
        }


        if (result.getEngine() == null)
        {
            // This call will also fire events to any PageAttachListeners
            
            result.attach(engine, cycle);
        }

        return result;
    }

    /**
     * Returns the page to the appropriate pool. Invokes {@link IPage#detach()}.
     */

    public void releasePage(IPage page)
    {
        Tapestry.clearMethodInvocations();

        page.detach();

        Tapestry.checkMethodInvocation(Tapestry.ABSTRACTPAGE_DETACH_METHOD_ID, "detach()", page);

        PageKey key = buildKey(page);

        try
        {
            _pool.returnObject(key, page);

        } catch (Exception ex)
        {
            if (RuntimeException.class.isInstance(ex))
                throw (RuntimeException)ex;
            else
                throw new ApplicationRuntimeException(PageloadMessages.errorPagePoolGet(key), ex);
        }        
    }

    public void resetEventDidOccur()
    {
        _pool.clear();
    }

    public void reportStatus(ReportStatusEvent event)
    {
        event.title(_serviceId);

        event.section("Page Pool");

        event.property("active", _pool.getNumActive());
        event.property("idle", _pool.getNumIdle());
    }

    public void setServiceId(String serviceId)
    {
        _serviceId = serviceId;
    }

    public void setRequestCycle(IRequestCycle cycle)
    {
        _cycle = cycle;
    }

    /** @since 4.0 */

    public void setClassResolver(ClassResolver resolver)
    {
        _classResolver = resolver;
    }

    /** @since 4.0 */

    public void setPageSpecificationResolver(PageSpecificationResolver resolver)
    {
        _pageSpecificationResolver = resolver;
    }

    /** @since 4.0 */

    public void setLoader(IPageLoader loader)
    {
        _loader = loader;
    }

    public void setPropertySource(IPropertySource propertySource)
    {
        _propertySource = propertySource;
    }
}
