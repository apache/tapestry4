//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry;

import java.io.OutputStream;
import java.util.EventListener;
import java.util.Locale;

import javax.swing.event.EventListenerList;

import org.apache.log4j.Logger;
import org.apache.log4j.LogManager;

import net.sf.tapestry.event.ChangeObserver;
import net.sf.tapestry.event.PageCleanupListener;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.util.StringSplitter;

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
    private static final Logger LOG = LogManager.getLogger(AbstractPage.class);

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
     *  The name of this page.  This may be read, but not changed, by
     *  subclasses.
     *
     **/

    private String _name;

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
     *  Implemented in subclasses to provide a particular kind of
     *  response writer (and therefore, a particular kind of
     *  content).
     *
     **/

    abstract public IMarkupWriter getResponseWriter(OutputStream out);

    /**
     *  Prepares the page to be returned to the pool.
     *  <ul>
     *	<li>Invokes {@link PageDetachListener#pageDetached(PageEvent)} on all listeners
     *	<li>Clears the engine, visit and changeObserver properties
     *	</ul>
     *
     *  <p>Subclasses may override this method, but must invoke this
     *  implementation (usually, last).
     *
     **/

    public void detach()
    {
        firePageDetached();

        _engine = null;
        _visit = null;
        _changeObserver = null;
        _requestCycle = null;
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
        return _name;
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
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractPage.attempt-to-change-locale"));

        _locale = value;
    }

    public String getName()
    {
        return _name;
    }

    public IPage getPage()
    {
        return this;
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
     *  from the {@link IPageSource} pool based on its
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

    public void renderPage(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        try
        {
            firePageBeginRender();

            beginResponse(writer, cycle);

            if (!cycle.isRewinding())
                cycle.commitPageChanges();

            render(writer, cycle);
        }
        catch (PageRecorderCommitException ex)
        {
            throw new RequestCycleException(ex.getMessage(), null, ex);
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

    public void setName(String value)
    {
        if (_name != null)
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractPage.attempt-to-change-name"));

        _name = value;
    }

    /**
     *  By default, pages are not protected and this method does nothing.
     *
     **/

    public void validate(IRequestCycle cycle) throws RequestCycleException
    {
        // Does nothing.
    }

    /**
     *  Does nothing, subclasses may override as needed.
     *
     *
     **/

    public void beginResponse(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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
     *  Invokes {@link PageCleanupListener#pageCleanup(PageEvent)} on any
     *  listener.
     *
     *  <p>Subclasses may override, but should invoke this implementation.
     *
     **/

    public void cleanupPage()
    {
        firePageCleanup();
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
     *  @seince 2.1-beta-2
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

    public void addPageCleanupListener(PageCleanupListener listener)
    {
        addListener(PageCleanupListener.class, listener);
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
    protected void firePageCleanup()
    {
        if (_listenerList == null)
            return;

        PageEvent event = null;
        Object[] listeners = _listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2)
        {
            if (listeners[i] == PageCleanupListener.class)
            {
                PageCleanupListener l = (PageCleanupListener) listeners[i + 1];

                if (event == null)
                    event = new PageEvent(this, null);

                l.pageCleanup(event);
            }
        }
    }
    
    /**
     *  @since 2.1-beta-2
     * 
     **/
    
    public void removePageCleanupListener(PageCleanupListener listener)
    {
        removeListener(PageCleanupListener.class, listener);
    }

     
    /**
     *  @since 2.1-beta-2
     * 
     **/
    
   public void removePageDetachListener(PageDetachListener listener)
    {
        removeListener(PageDetachListener.class, listener);
    }

    
    /**
     *  @since 2.1-beta-2
     * 
     **/
    
    public void removePageRenderListener(PageRenderListener listener)
    {
        removeListener(PageRenderListener.class, listener);
    }

}