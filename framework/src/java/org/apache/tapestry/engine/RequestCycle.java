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

package org.apache.tapestry.engine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.impl.ErrorLogImpl;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.record.PageRecorderImpl;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Provides the logic for processing a single request cycle. Provides access to the
 * {@link IEngine engine}and the {@link RequestContext}.
 * 
 * @author Howard Lewis Ship
 */

public class RequestCycle implements IRequestCycle
{
    private static final Log LOG = LogFactory.getLog(RequestCycle.class);

    private IPage _page;

    private IEngine _engine;

    private IEngineService _service;

    private RequestContext _requestContext;

    private IMonitor _monitor;

    private HttpServletResponse _response;

    /** @since 3.1 */

    private PropertyPersistenceStrategySource _strategySource;

    /** @since 3.1 */

    private IPageSource _pageSource;

    /**
     * Contains parameters extracted from the request context, plus any decoded by any
     * {@link ServiceEncoder}s.
     * 
     * @since 3.1
     */

    private QueryParameterMap _parameters;

    /**
     * A mapping of pages loaded during the current request cycle. Key is the page name, value is
     * the {@link IPage}instance.
     */

    private Map _loadedPages;

    /**
     * A mapping of page recorders for the current request cycle. Key is the page name, value is the
     * {@link IPageRecorder}instance.
     */

    private Map _pageRecorders;

    private boolean _rewinding = false;

    private Map _attributes;

    private int _actionId;

    private int _targetActionId;

    private IComponent _targetComponent;

    /** @since 2.0.3 * */

    private Object[] _serviceParameters;

    /** @since 3.1 */

    private ErrorLog _log;

    /**
     * Standard constructor used to render a response page.
     */

    public RequestCycle(IEngine engine, RequestContext requestContext,
            QueryParameterMap parameters, IEngineService service,
            IPageSource pageSource,
            PropertyPersistenceStrategySource strategySource, ErrorHandler errorHandler,
            IMonitor monitor)
    {
        _engine = engine;
        _requestContext = requestContext;
        _parameters = parameters;
        _service = service;
        _pageSource = pageSource;
        _strategySource = strategySource;
        _log = new ErrorLogImpl(errorHandler, LOG);
        _monitor = monitor;
    }

    /**
     * Alternate constructor used <b>only for testing purposes </b>.
     * 
     * @since 3.1
     */
    public RequestCycle()
    {
    }

    /**
     * Called at the end of the request cycle (i.e., after all responses have been sent back to the
     * client), to release all pages loaded during the request cycle.
     */

    public void cleanup()
    {
        if (_loadedPages == null)
            return;

        Iterator i = _loadedPages.values().iterator();

        while (i.hasNext())
        {
            IPage page = (IPage) i.next();

            _pageSource.releasePage(page);
        }

        _loadedPages = null;
        _pageRecorders = null;

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
     * Gets the page from the engines's {@link IPageSource}.
     */

    public IPage getPage(String name)
    {
        IPage result = null;

        if (name == null)
            throw new NullPointerException(Tapestry.getMessage("RequestCycle.invalid-null-name"));

        if (_loadedPages != null)
            result = (IPage) _loadedPages.get(name);

        if (result == null)
        {
            _monitor.pageLoadBegin(name);

            result = _pageSource.getPage(this, name, _monitor);

            result.setRequestCycle(this);

            // Get the recorder that will eventually observe and record
            // changes to persistent properties of the page.

            IPageRecorder recorder = getPageRecorder(name);

            // Have it rollback the page to the prior state. Note that
            // the page has a null observer at this time (which keeps
            // these changes from being sent to the page recorder).

            recorder.rollback(result);

            // Now, have the page use the recorder for any future
            // property changes.

            result.setChangeObserver(recorder);

            _monitor.pageLoadEnd(name);

            if (_loadedPages == null)
                _loadedPages = new HashMap();

            _loadedPages.put(name, result);
        }

        return result;
    }

    /**
     * Returns the page recorder for the named page. Starting with Tapestry 3.1, page recorders are
     * shortlived objects managed exclusively by the request cycle.
     */

    protected IPageRecorder getPageRecorder(String name)
    {
        if (_pageRecorders == null)
            _pageRecorders = new HashMap();

        IPageRecorder result = (IPageRecorder) _pageRecorders.get(name);

        if (result == null)
        {
            result = new PageRecorderImpl(name, this, _strategySource, _log);
            _pageRecorders.put(name, result);
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

        // Woops. Mismatch.

        throw new StaleLinkException(component, Integer.toHexString(_targetActionId),
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
     * Renders the page by invoking {@link IPage#renderPage(IMarkupWriter, IRequestCycle)}. This
     * clears all attributes.
     */

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
     * Rewinds an individual form by invoking {@link IForm#rewind(IMarkupWriter, IRequestCycle)}.
     * <p>
     * The process is expected to end with a {@link RenderRewoundException}. If the entire page is
     * renderred without this exception being thrown, it means that the target action id was not
     * valid, and a {@link ApplicationRuntimeException}is thrown.
     * <p>
     * This clears all attributes.
     * 
     * @since 1.0.2
     */

    public void rewindForm(IForm form, String targetActionId)
    {
        IPage page = form.getPage();
        String pageName = page.getPageName();

        _rewinding = true;

        _monitor.pageRewindBegin(pageName);

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

            throw new StaleLinkException(Tapestry.format("RequestCycle.form-rewind-failure", form
                    .getExtendedId()), form);
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

            throw new ApplicationRuntimeException(ex.getMessage(), page, null, ex);
        }
        finally
        {
            _actionId = 0;
            _targetActionId = 0;
            _targetComponent = null;

            page.endPageRender();

            _monitor.pageRewindEnd(pageName);

            _rewinding = false;
        }
    }

    /**
     * Rewinds the page by invoking {@link IPage#renderPage(IMarkupWriter, IRequestCycle)}.
     * <p>
     * The process is expected to end with a {@link RenderRewoundException}. If the entire page is
     * renderred without this exception being thrown, it means that the target action id was not
     * valid, and a {@link ApplicationRuntimeException}is thrown.
     * <p>
     * This clears all attributes.
     */

    public void rewindPage(String targetActionId, IComponent targetComponent)
    {
        String pageName = _page.getPageName();

        _rewinding = true;

        _monitor.pageRewindBegin(pageName);

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

            throw new ApplicationRuntimeException(ex.getMessage(), _page, null, ex);
        }
        finally
        {

            _actionId = 0;
            _targetActionId = 0;
            _targetComponent = null;

            _monitor.pageRewindEnd(pageName);

            _rewinding = false;
        }

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
     * Invokes {@link IPageRecorder#commit()}on each page recorder loaded during the request cycle
     * (even recorders marked for discard).
     */

    public void commitPageChanges()
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Committing page changes");

        if (_pageRecorders == null || _pageRecorders.isEmpty())
            return;

        Iterator i = _pageRecorders.values().iterator();

        while (i.hasNext())
        {
            IPageRecorder recorder = (IPageRecorder) i.next();

            recorder.commit();
        }
    }

    /**
     * As of 3.1, just a synonym for {@link #forgetPage(String)}.
     * 
     * @since 2.0.2
     */

    public void discardPage(String name)
    {
        forgetPage(name);
    }

    /** @since 2.0.3 * */

    public Object[] getServiceParameters()
    {
        return _serviceParameters;
    }

    /** @since 2.0.3 * */

    public void setServiceParameters(Object[] serviceParameters)
    {
        _serviceParameters = serviceParameters;
    }

    /** @since 3.0 * */

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

        Tapestry
                .checkMethodInvocation(Tapestry.ABSTRACTPAGE_VALIDATE_METHOD_ID, "validate()", page);

        setPage(page);
    }

    /** @since 3.1 */
    public String getParameter(String name)
    {
        return _parameters.getParameterValue(name);
    }

    /** @since 3.1 */
    public String[] getParameters(String name)
    {
        return _parameters.getParameterValues(name);
    }

    /**
     * @since 3.0
     */
    public String toString()
    {
        ToStringBuilder b = new ToStringBuilder(this);

        b.append("rewinding", _rewinding);

        if (_service != null)
            b.append("service", _service.getName());

        b.append("serviceParameters", _serviceParameters);

        if (_loadedPages != null)
            b.append("loadedPages", _loadedPages.keySet());

        b.append("attributes", _attributes);
        b.append("targetActionId", _targetActionId);
        b.append("targetComponent", _targetComponent);

        return b.toString();
    }

    /** @since 3.1 */

    public String getAbsoluteURL(String partialURL)
    {
        String contextPath = _requestContext.getContextPath();

        return _requestContext.getAbsoluteURL(contextPath + partialURL);
    }

    /** @since 3.1 */

    public void forgetPage(String pageName)
    {
        Defense.notNull(pageName, "pageName");

        _strategySource.discardAllStoredChanged(pageName, this);
    }

}