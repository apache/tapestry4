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

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.request.RequestContext;

/**
 *  Provides the logic for processing a single request cycle.  Provides access to
 *  the {@link IEngine engine} and the {@link RequestContext}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class RequestCycle implements IRequestCycle, ChangeObserver
{
    private static final Log LOG = LogFactory.getLog(RequestCycle.class);

    private IPage _page;
    private IEngine _engine;
    private IEngineService _service;

    private RequestContext _requestContext;

    private IMonitor _monitor;

    private HttpServletResponse _response;

    /**
     *  A mapping of pages loaded during the current request cycle.
     *  Key is the page name, value is the {@link IPage} instance.
     *
     **/

    private Map _loadedPages;

    /**
     * A mapping of page recorders for the current request cycle.
     * Key is the page name, value is the {@link IPageRecorder} instance.
     *
     **/

    private Map _loadedRecorders;

    private boolean _rewinding = false;

    private Map _attributes;

    private int _actionId;
    private int _targetActionId;
    private IComponent _targetComponent;

    /** @since 2.0.3 **/

    private Object[] _serviceParameters;

    /**
     *  Standard constructor used to render a response page.
     *
     **/

    public RequestCycle(
        IEngine engine,
        RequestContext requestContext,
        IEngineService service,
        IMonitor monitor)
    {
        _engine = engine;
        _requestContext = requestContext;
        _service = service;
        _monitor = monitor;
    }

    /**
     *  Called at the end of the request cycle (i.e., after all responses have been
     *  sent back to the client), to release all pages loaded during the request cycle.
     *
     **/

    public void cleanup()
    {
        if (_loadedPages == null)
            return;

        IPageSource source = _engine.getPageSource();
        Iterator i = _loadedPages.values().iterator();

        while (i.hasNext())
        {
            IPage page = (IPage) i.next();

            source.releasePage(page);
        }

        _loadedPages = null;
        _loadedRecorders = null;

    }

    public IEngineService getService()
    {
        return _service;
    }

    public String encodeURL(String URL)
    {
        if (_response == null)
            _response = _requestContext.getResponse();

        return _response.encodeURL(URL);
    }

    public IEngine getEngine()
    {
        return _engine;
    }

    public Object getAttribute(String name)
    {
        if (_attributes == null)
            return null;

        return _attributes.get(name);
    }

    public IMonitor getMonitor()
    {
        return _monitor;
    }

    public String getNextActionId()
    {
        return Integer.toHexString(++_actionId);
    }

    public IPage getPage()
    {
        return _page;
    }

    /**
     *  Gets the page from the engines's {@link IPageSource}.
     *
     **/

    public IPage getPage(String name)
    {
        IPage result = null;

        if (name == null)
            throw new NullPointerException(Tapestry.getString("RequestCycle.invalid-null-name"));

        if (_loadedPages != null)
            result = (IPage) _loadedPages.get(name);

        if (result == null)
        {
            _monitor.pageLoadBegin(name);

            IPageSource pageSource = _engine.getPageSource();

            result = pageSource.getPage(this, name, _monitor);

            result.setRequestCycle(this);

            // Get the recorder that will eventually observe and record
            // changes to persistent properties of the page.  If the page
            // has never emitted any page changes, then it will
            // not have a recorder.

            IPageRecorder recorder = getPageRecorder(name);

            if (recorder != null)
            {
                // Have it rollback the page to the prior state.  Note that
                // the page has a null observer at this time.

                recorder.rollback(result);

                // Now, have the page use the recorder for any future
                // property changes.

                result.setChangeObserver(recorder);

                // And, if this recorder observed changes in a prior request cycle
                // (and was locked after committing in that cycle), it's time
                // to unlock.

                recorder.setLocked(false);
            }
            else
            {
                // No page recorder for the page.  We'll observe its
                // changes and create the page recorder dynamically
                // if it emits any.

                result.setChangeObserver(this);
            }

            _monitor.pageLoadEnd(name);

            if (_loadedPages == null)
                _loadedPages = new HashMap();

            _loadedPages.put(name, result);
        }

        return result;
    }

    /**
     *  Returns the page recorder for the named page.  This may come
     *  from the cycle's cache of page recorders or, if not yet encountered
     *  in this request cycle, the {@link IEngine#getPageRecorder(String, IRequestCycle)} is
     *  invoked to get the recorder, if it exists.
     * 
     **/

    protected IPageRecorder getPageRecorder(String name)
    {
        IPageRecorder result = null;

        if (_loadedRecorders != null)
            result = (IPageRecorder) _loadedRecorders.get(name);

        if (result != null)
            return result;

        result = _engine.getPageRecorder(name, this);

        if (result == null)
            return null;

        if (_loadedRecorders == null)
            _loadedRecorders = new HashMap();

        _loadedRecorders.put(name, result);

        return result;
    }

    /** 
     * 
     *  Gets the page recorder from the loadedRecorders cache, or from the engine
     *  (putting it into loadedRecorders).  If the recorder does not yet exist,
     *  it is created.
     * 
     *  @see IEngine#createPageRecorder(String, IRequestCycle)
     *  @since 2.0.3
     * 
     **/

    private IPageRecorder createPageRecorder(String name)
    {
        IPageRecorder result = getPageRecorder(name);

        if (result == null)
        {
            result = _engine.createPageRecorder(name, this);

            if (_loadedRecorders == null)
                _loadedRecorders = new HashMap();

            _loadedRecorders.put(name, result);
        }

        return result;
    }

    public RequestContext getRequestContext()
    {
        return _requestContext;
    }

    public boolean isRewinding()
    {
        return _rewinding;
    }

    public boolean isRewound(IComponent component) throws StaleLinkException
    {
        // If not rewinding ...

        if (!_rewinding)
            return false;

        if (_actionId != _targetActionId)
            return false;

        // OK, we're there, is the page is good order?

        if (component == _targetComponent)
            return true;

        // Woops.  Mismatch.

        throw new StaleLinkException(
            component,
            Integer.toHexString(_targetActionId),
            _targetComponent.getExtendedId());
    }

    public void removeAttribute(String name)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Removing attribute " + name);

        if (_attributes == null)
            return;

        _attributes.remove(name);
    }

    /**
     *  Renders the page by invoking 
     * {@link IPage#renderPage(IMarkupWriter, IRequestCycle)}.  
     *  This clears all attributes.
     *
     **/

    public void renderPage(IMarkupWriter writer)
    {
        String pageName = _page.getPageName();
        _monitor.pageRenderBegin(pageName);

        _rewinding = false;
        _actionId = -1;
        _targetActionId = 0;

        // Forget any attributes from a previous render cycle.

        if (_attributes != null)
            _attributes.clear();

        try
        {
            _page.renderPage(writer, this);

        }
        catch (ApplicationRuntimeException ex)
        {
            // Nothing much to add here.

            throw ex;
        }
        catch (Throwable ex)
        {
            // But wrap other exceptions in a RequestCycleException ... this
            // will ensure that some of the context is available.

            throw new ApplicationRuntimeException(ex.getMessage(), _page, null, ex);
        }
        finally
        {
            _actionId = 0;
            _targetActionId = 0;
        }

        _monitor.pageRenderEnd(pageName);

    }

    /**
     *  Rewinds an individual form by invoking 
     *  {@link IForm#rewind(IMarkupWriter, IRequestCycle)}.
     *
     * <p>The process is expected to end with a {@link RenderRewoundException}.
     * If the entire page is renderred without this exception being thrown, it means
     * that the target action id was not valid, and a 
     * {@link RequestCycleException}
     * is thrown.
     *
     * <p>This clears all attributes.
     *
     *  @since 1.0.2
     **/

    public void rewindForm(IForm form, String targetActionId)
    {
        IPage page = form.getPage();
        String pageName = page.getPageName();

        _monitor.pageRewindBegin(pageName);

        _rewinding = true;

        if (_attributes != null)
            _attributes.clear();

        // Fake things a little for getNextActionId() / isRewound()

        _targetActionId = Integer.parseInt(targetActionId, 16);
        _actionId = _targetActionId - 1;

        _targetComponent = form;

        try
        {
            page.beginPageRender();

            form.rewind(NullWriter.getSharedInstance(), this);

            // Shouldn't get this far, because the form should
            // throw the RenderRewoundException.

            throw new StaleLinkException(
                Tapestry.getString("RequestCycle.form-rewind-failure", form.getExtendedId()),
                form);
        }
        catch (RenderRewoundException ex)
        {
            // This is acceptible and expected.
        }
        catch (ApplicationRuntimeException ex)
        {
            // RequestCycleExceptions don't need to be wrapped.
            throw ex;
        }
        catch (Throwable ex)
        {
            // But wrap other exceptions in a ApplicationRuntimeException ... this
            // will ensure that some of the context is available.

            throw new ApplicationRuntimeException(ex.getMessage(), page, ex);
        }
        finally
        {
            _rewinding = false;
            _actionId = 0;
            _targetActionId = 0;
            _targetComponent = null;

            page.endPageRender();
        }

        _monitor.pageRewindEnd(pageName);

    }

    /**
     *  Rewinds the page by invoking 
     *  {@link IPage#renderPage(IMarkupWriter, IRequestCycle)}.
     *
     * <p>The process is expected to end with a {@link RenderRewoundException}.
     * If the entire page is renderred without this exception being thrown, it means
     * that the target action id was not valid, and a 
     * {@link RequestCycleException}
     * is thrown.
     *
     * <p>This clears all attributes.
     *
     **/

    public void rewindPage(String targetActionId, IComponent targetComponent)
    {
        String pageName = _page.getPageName();

        _monitor.pageRewindBegin(pageName);

        _rewinding = true;

        if (_attributes != null)
            _attributes.clear();

        _actionId = -1;

        // Parse the action Id as hex since that's whats generated
        // by getNextActionId()
        _targetActionId = Integer.parseInt(targetActionId, 16);
        _targetComponent = targetComponent;

        try
        {
            _page.renderPage(NullWriter.getSharedInstance(), this);

            // Shouldn't get this far, because the target component should
            // throw the RenderRewoundException.

            throw new StaleLinkException(_page, targetActionId, targetComponent.getExtendedId());
        }
        catch (RenderRewoundException ex)
        {
            // This is acceptible and expected.
        }
        catch (ApplicationRuntimeException ex)
        {
            // ApplicationRuntimeExceptions don't need to be wrapped.
            throw ex;
        }
        catch (Throwable ex)
        {
            // But wrap other exceptions in a RequestCycleException ... this
            // will ensure that some of the context is available.

            throw new ApplicationRuntimeException(ex.getMessage(), _page, ex);
        }
        finally
        {
            _rewinding = false;
            _actionId = 0;
            _targetActionId = 0;
            _targetComponent = null;
        }

        _monitor.pageRewindEnd(pageName);

    }

    public void setAttribute(String name, Object value)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Set attribute " + name + " to " + value);

        if (_attributes == null)
            _attributes = new HashMap();

        _attributes.put(name, value);
    }

    public void setPage(IPage value)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Set page to " + value);

        _page = value;
    }

    public void setPage(String name)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Set page to " + name);

        _page = getPage(name);
    }

    /**
     *  Invokes {@link IPageRecorder#commit()} on each page recorder loaded
     *  during the request cycle (even recorders marked for discard).
     *
     **/

    public void commitPageChanges()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Committing page changes");

        if (_loadedRecorders == null || _loadedRecorders.isEmpty())
            return;

        Iterator i = _loadedRecorders.values().iterator();

        while (i.hasNext())
        {
            IPageRecorder recorder = (IPageRecorder) i.next();

            recorder.commit();
        }
    }

    /**
     *  For pages without a {@link IPageRecorder page recorder}, 
     *  we're the {@link ChangeObserver change observer}.
     *  If such a page actually emits a change, then
     *  we'll obtain a new page recorder from the
     *  {@link IEngine engine}, set the recorder
     *  as the page's change observer, and forward the event
     *  to the newly created recorder.  In addition, the
     *  new page recorder is remembered so that it will
     *  be committed by {@link #commitPageChanges()}.
     *
     **/

    public void observeChange(ObservedChangeEvent event)
    {
        IPage page = event.getComponent().getPage();
        String pageName = page.getPageName();

        if (LOG.isDebugEnabled())
            LOG.debug("Observed change in page " + pageName + "; creating page recorder.");

        IPageRecorder recorder = createPageRecorder(pageName);

        page.setChangeObserver(recorder);

        recorder.observeChange(event);
    }

    /**
     *  Finds the page and its page recorder, creating the page recorder if necessary.
     *  The page recorder is marked for discard regardless of its current state.
     * 
     *  <p>This may make the application stateful even if the page recorder does
     *  not yet exist.
     * 
     *  <p>The page recorder will be discarded at the end of the current request cycle.
     * 
     *  @since 2.0.2
     * 
     **/

    public void discardPage(String name)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Discarding page " + name);

        IPageRecorder recorder = _engine.getPageRecorder(name, this);

        if (recorder == null)
        {
            _page = getPage(name);

            recorder = createPageRecorder(name);

            _page.setChangeObserver(recorder);
        }

        recorder.markForDiscard();
    }

    /** @since 2.0.3 **/

    public Object[] getServiceParameters()
    {
        return _serviceParameters;
    }

    /** @since 2.0.3 **/

    public void setServiceParameters(Object[] serviceParameters)
    {
        _serviceParameters = serviceParameters;
    }

    /** @since 3.0 **/

    public void activate(String name)
    {
        IPage page = getPage(name);

        activate(page);
    }

    /** @since 3.0 */

    public void activate(IPage page)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Activating page " + page);

        Tapestry.clearMethodInvocations();

        page.validate(this);

        Tapestry.checkMethodInvocation(Tapestry.IPAGE_VALIDATE_METHOD_ID, "validate()", page);

        setPage(page);
    }
}