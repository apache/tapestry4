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

package org.apache.tapestry;

import java.io.OutputStream;
import java.util.EventListener;
import java.util.Locale;

import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.event.PageValidateListener;
import org.apache.tapestry.util.StringSplitter;

/**
 *  Abstract base class implementing the {@link IPage} interface.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship, David Solis
 *  @since 0.2.9
 * 
 **/

public abstract class AbstractPage extends BaseComponent implements IPage
{
    private static final Log LOG = LogFactory.getLog(AbstractPage.class);

    /**
     *  Object to be notified when a observered property changes.  Observered
     *  properties are the ones that will be persisted between request cycles.
     *  Unobserved properties are reconstructed.
     *
     **/

    private ChangeObserver _changeObserver;

    /**
     *  The {@link IEngine} the page is currently attached to.
     *
     **/

    private IEngine _engine;

    /**
     *  The visit object, if any, for the application.  Set inside
     *  {@link #attach(IEngine)} and cleared
     *  by {@link #detach()}.
     *
     **/

    private Object _visit;

    /**
     *  The qualified name of the page, which may be prefixed by the
     *  namespace.
     * 
     *  @since 2.3
     * 
     **/

    private String _pageName;

    /**
     *  Set when the page is attached to the engine.
     *
     **/

    private IRequestCycle _requestCycle;

    /**
     *  The locale of the page, initially determined from the {@link IEngine engine}.
     *
     **/

    private Locale _locale;

    /**
     *  A list of listeners for the page.
     *  @see PageRenderListener
     *  @see PageDetachListener
     *
     *  @since 1.0.5
     **/

    private EventListenerList _listenerList;
    
    
    /**
     *  The output encoding to be used when rendering this page.
     *  This value is cached from the engine.
     *
     *  @since 3.0
     **/
    private String _outputEncoding;

    /**
     *  Standard constructor; invokes {@link #initialize()}
     *  to configure initial values for properties
     *  of the page.
     * 
     *  @since 2.2
     * 
     **/

    public AbstractPage()
    {
        initialize();
    }

    /**
     *  Implemented in subclasses to provide a particular kind of
     *  response writer (and therefore, a particular kind of
     *  content).
     *
     **/

    abstract public IMarkupWriter getResponseWriter(OutputStream out);

    /**
     *  Prepares the page to be returned to the pool.
     *  <ul>
     *  <li>Clears the changeObserved property
     *	<li>Invokes {@link PageDetachListener#pageDetached(PageEvent)} on all listeners
     *  <li>Invokes {@link #initialize()} to clear/reset any properties	
     * <li>Clears the engine, visit and requestCycle properties
     *	</ul>
     *
     *  <p>Subclasses may override this method, but must invoke this
     *  implementation (usually, last).
     *
     **/

    public void detach()
    {
    	Tapestry.addMethodInvocation(Tapestry.ABSTRACTPAGE_DETACH_METHOD_ID);
    	
        // Do this first,so that any changes to persistent properties do not
        // cause errors.

        _changeObserver = null;

        firePageDetached();

        initialize();

        _engine = null;
        _visit = null;
        _requestCycle = null;
    }

    /**
     *  Method invoked from the constructor, and from
     *  {@link #detach()} to (re-)initialize properties
     *  of the page.  This is most useful when
     *  properties have non-null initial values.
     * 
     *  <p>Subclasses may override this implementation
     *  (which is empty).
     * 
     *  @since 2.2
     * 
     **/

    protected void initialize()
    {
        // Does nothing.
    }

    public IEngine getEngine()
    {
        return _engine;
    }

    public ChangeObserver getChangeObserver()
    {
        return _changeObserver;
    }

    /**
     *  Returns the name of the page.
     *
     **/

    public String getExtendedId()
    {
        return _pageName;
    }

    /**
     *  Pages always return null for idPath.
     *
     **/

    public String getIdPath()
    {
        return null;
    }

    /**
     *  Returns the locale for the page, which may be null if the
     *  locale is not known (null corresponds to the "default locale").
     *
     **/

    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale value)
    {
        if (_locale != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("AbstractPage.attempt-to-change-locale"));

        _locale = value;
    }

    public IComponent getNestedComponent(String path)
    {
        StringSplitter splitter;
        IComponent current;
        String[] elements;
        int i;

        if (path == null)
            return this;

        splitter = new StringSplitter('.');
        current = this;

        elements = splitter.splitToArray(path);
        for (i = 0; i < elements.length; i++)
        {
            current = current.getComponent(elements[i]);
        }

        return current;

    }

    /**
     *  Called by the {@link IEngine engine} to attach the page
     *  to itself.  Does
     *  <em>not</em> change the locale, but since a page is selected
     *  from the {@link org.apache.tapestry.engine.IPageSource} pool based on its
     *  locale matching the engine's locale, they should match
     *  anyway.
     *
     **/

    public void attach(IEngine value)
    {
        if (_engine != null)
            LOG.error(this +" attach(" + value + "), but engine = " + _engine);

        _engine = value;
    }

    /**
     *
     * <ul>
     *  <li>Invokes {@link PageRenderListener#pageBeginRender(PageEvent)}
     *  <li>Invokes {@link #beginResponse(IMarkupWriter, IRequestCycle)}
     *  <li>Invokes {@link IRequestCycle#commitPageChanges()} (if not rewinding)
     *  <li>Invokes {@link #render(IMarkupWriter, IRequestCycle)}
     *  <li>Invokes {@link PageRenderListener#pageEndRender(PageEvent)} (this occurs
     *  even if a previous step throws an exception)
     *
     **/

    public void renderPage(IMarkupWriter writer, IRequestCycle cycle)
    {
        try
        {
            firePageBeginRender();

            beginResponse(writer, cycle);

            if (!cycle.isRewinding())
                cycle.commitPageChanges();

            render(writer, cycle);
        }
        finally
        {
            firePageEndRender();
        }
    }

    public void setChangeObserver(ChangeObserver value)
    {
        _changeObserver = value;
    }

    /** @since 3.0 **/

    public void setPageName(String pageName)
    {
        if (_pageName != null)
            throw new ApplicationRuntimeException(
                Tapestry.getMessage("AbstractPage.attempt-to-change-name"));

        _pageName = pageName;
    }

    /**
     *  By default, pages are not protected and this method does nothing.
     *
     **/

    public void validate(IRequestCycle cycle)
    {
        Tapestry.addMethodInvocation(Tapestry.ABSTRACTPAGE_VALIDATE_METHOD_ID);

        firePageValidate();
    }

    /**
     *  Does nothing, subclasses may override as needed.
     *
     * @deprecated To be removed in 3.1.  Implement 
     * {@link PageRenderListener} instead.
     *
     **/

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle)
    {
    }

    public IRequestCycle getRequestCycle()
    {
        return _requestCycle;
    }

    public void setRequestCycle(IRequestCycle value)
    {
        _requestCycle = value;
    }

    /**
     *  Returns the visit object obtained from the engine via
     *  {@link IEngine#getVisit(IRequestCycle)}.
     *
     **/

    public Object getVisit()
    {
        if (_visit == null)
            _visit = _engine.getVisit(_requestCycle);

        return _visit;
    }

    /**
     *  Convienience methods, simply invokes
     *  {@link IEngine#getGlobal()}.
     * 
     *  @since 2.3
     * 
     **/

    public Object getGlobal()
    {
        return _engine.getGlobal();
    }

    public void addPageDetachListener(PageDetachListener listener)
    {
        addListener(PageDetachListener.class, listener);
    }

    private void addListener(Class listenerClass, EventListener listener)
    {
        if (_listenerList == null)
            _listenerList = new EventListenerList();

        _listenerList.add(listenerClass, listener);
    }

    /**
     *  @since 2.1-beta-2
     * 
     **/

    private void removeListener(Class listenerClass, EventListener listener)
    {
        if (_listenerList != null)
            _listenerList.remove(listenerClass, listener);
    }

    public void addPageRenderListener(PageRenderListener listener)
    {
        addListener(PageRenderListener.class, listener);
    }

    /**
     *  @since 1.0.5
     *
     **/

    protected void firePageDetached()
    {
        if (_listenerList == null)
            return;

        PageEvent event = null;
        Object[] listeners = _listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == PageDetachListener.class)
            {
                PageDetachListener l = (PageDetachListener) listeners[i + 1];

                if (event == null)
                    event = new PageEvent(this, _requestCycle);

                l.pageDetached(event);
            }
        }
    }

    /**
     *  @since 1.0.5
     *
     **/

    protected void firePageBeginRender()
    {
        if (_listenerList == null)
            return;

        PageEvent event = null;
        Object[] listeners = _listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == PageRenderListener.class)
            {
                PageRenderListener l = (PageRenderListener) listeners[i + 1];

                if (event == null)
                    event = new PageEvent(this, _requestCycle);

                l.pageBeginRender(event);
            }
        }
    }

    /**
     *  @since 1.0.5
     *
     **/

    protected void firePageEndRender()
    {
        if (_listenerList == null)
            return;

        PageEvent event = null;
        Object[] listeners = _listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == PageRenderListener.class)
            {
                PageRenderListener l = (PageRenderListener) listeners[i + 1];

                if (event == null)
                    event = new PageEvent(this, _requestCycle);

                l.pageEndRender(event);
            }
        }
    }

    /**
     *  @since 2.1-beta-2
     * 
     **/

    public void removePageDetachListener(PageDetachListener listener)
    {
        removeListener(PageDetachListener.class, listener);
    }

    public void removePageRenderListener(PageRenderListener listener)
    {
        removeListener(PageRenderListener.class, listener);
    }

    /** @since 2.2 **/

    public void beginPageRender()
    {
        firePageBeginRender();
    }

    /** @since 2.2 **/

    public void endPageRender()
    {
        firePageEndRender();
    }

    /** @since 3.0 **/

    public String getPageName()
    {
        return _pageName;
    }

    public void addPageValidateListener(PageValidateListener listener)
    {
        addListener(PageValidateListener.class, listener);
    }

    public void removePageValidateListener(PageValidateListener listener)
    {
        removeListener(PageValidateListener.class, listener);
    }

    protected void firePageValidate()
    {
        if (_listenerList == null)
            return;

        PageEvent event = null;
        Object[] listeners = _listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == PageValidateListener.class)
            {
                PageValidateListener l = (PageValidateListener) listeners[i + 1];

                if (event == null)
                    event = new PageEvent(this, _requestCycle);

                l.pageValidate(event);
            }
        }
    }

    /**
     *  Returns the output encoding to be used when rendering this page.
     *  This value is usually cached from the Engine.
     * 
     *  @since 3.0
     **/
    protected String getOutputEncoding()
    {
        if (_outputEncoding == null)
            _outputEncoding = getEngine().getOutputEncoding();
        
        return _outputEncoding;
    }
}