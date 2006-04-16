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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.impl.ErrorLogImpl;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.ToStringBuilder;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.RenderRewoundException;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.record.PageRecorderImpl;
import org.apache.tapestry.record.PropertyPersistenceStrategySource;
import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.ResponseBuilder;
import org.apache.tapestry.util.IdAllocator;
import org.apache.tapestry.util.QueryParameterMap;

/**
 * Provides the logic for processing a single request cycle. Provides access to the
 * {@link IEngine engine} and the {@link RequestContext}.
 * 
 * @author Howard Lewis Ship
 */

public class RequestCycle implements IRequestCycle
{
    private static final Log LOG = LogFactory.getLog(RequestCycle.class);
    
    protected ResponseBuilder _responseBuilder;
    
    private IPage _page;

    private IEngine _engine;

    private String _serviceName;

    /** @since 4.0 */

    private PropertyPersistenceStrategySource _strategySource;

    /** @since 4.0 */

    private IPageSource _pageSource;

    /** @since 4.0 */

    private Infrastructure _infrastructure;

    /**
     * Contains parameters extracted from the request context, plus any decoded by any
     * {@link ServiceEncoder}s.
     * 
     * @since 4.0
     */

    private QueryParameterMap _parameters;

    /** @since 4.0 */

    private AbsoluteURLBuilder _absoluteURLBuilder;

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

    private Map _attributes = new HashMap();

    private int _actionId;

    private int _targetActionId;

    private IComponent _targetComponent;

    /** @since 2.0.3 * */

    private Object[] _listenerParameters;

    /** @since 4.0 */

    private ErrorLog _log;

    /** @since 4.0 */

    private IdAllocator _idAllocator = new IdAllocator();

    /**
     * Standard constructor used to render a response page.
     * 
     * @param engine
     *            the current request's engine
     * @param parameters
     *            query parameters (possibly the result of {@link ServiceEncoder}s decoding path
     *            information)
     * @param serviceName
     *            the name of engine service
     * @param environment
     *            additional invariant services and objects needed by each RequestCycle instance
     */

    public RequestCycle(IEngine engine, QueryParameterMap parameters, String serviceName,
            RequestCycleEnvironment environment)
    {
        // Variant from instance to instance

        _engine = engine;
        _parameters = parameters;
        _serviceName = serviceName;

        // Invariant from instance to instance

        _infrastructure = environment.getInfrastructure();
        _pageSource = _infrastructure.getPageSource();
        _strategySource = environment.getStrategySource();
        _absoluteURLBuilder = environment.getAbsoluteURLBuilder();
        _log = new ErrorLogImpl(environment.getErrorHandler(), LOG);

    }

    /**
     * Alternate constructor used <strong>only for testing purposes</strong>.
     * 
     * @since 4.0
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
        return _infrastructure.getServiceMap().getService(_serviceName);
    }

    public String encodeURL(String URL)
    {
        return _infrastructure.getResponse().encodeURL(URL);
    }

    public IEngine getEngine()
    {
        return _engine;
    }

    public Object getAttribute(String name)
    {
        return _attributes.get(name);
    }

    /** @deprecated */
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
        Defense.notNull(name, "name");

        IPage result = null;

        if (_loadedPages != null)
            result = (IPage) _loadedPages.get(name);

        if (result == null)
        {
            result = loadPage(name);

            if (_loadedPages == null)
                _loadedPages = new HashMap();

            _loadedPages.put(name, result);
        }

        return result;
    }

    private IPage loadPage(String name)
    {
        IPage result = _pageSource.getPage(this, name);

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

        return result;

    }

    /**
     * Returns the page recorder for the named page. Starting with Tapestry 4.0, page recorders are
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

    public void setResponseBuilder(ResponseBuilder builder)
    {
        //if (_responseBuilder != null)
          //  throw new IllegalArgumentException("A ResponseBuilder has already been set on this response.");
        _responseBuilder = builder;
    }
    
    public ResponseBuilder getResponseBuilder()
    {
        return _responseBuilder;
    }
    
    public boolean isRewinding()
    {
        return _rewinding;
    }

    public boolean isRewound(IComponent component)
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

        _attributes.remove(name);
    }

    /**
     * Renders the page by invoking {@link IPage#renderPage(ResponseBuilder, IRequestCycle)}. This
     * clears all attributes.
     */

    public void renderPage(ResponseBuilder builder)
    {
        _rewinding = false;
        _actionId = -1;
        _targetActionId = 0;

        try
        {
            _page.renderPage(builder, this);

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
            reset();
        }

    }

    /**
     * Resets all internal state after a render or a rewind.
     */

    private void reset()
    {
        _actionId = 0;
        _targetActionId = 0;
        _attributes.clear();
        _idAllocator.clear();
    }

    /**
     * Rewinds an individual form by invoking {@link IForm#rewind(IMarkupWriter, IRequestCycle)}.
     * <p>
     * The process is expected to end with a {@link RenderRewoundException}. If the entire page is
     * renderred without this exception being thrown, it means that the target action id was not
     * valid, and a {@link ApplicationRuntimeException}&nbsp;is thrown.
     * <p>
     * This clears all attributes.
     * 
     * @since 1.0.2
     */

    public void rewindForm(IForm form)
    {
        IPage page = form.getPage();
        _rewinding = true;

        // Fake things a little for getNextActionId() / isRewound()
        // This used to be more involved (and include service parameters, and a parameter
        // to this method), when the actionId was part of the Form name. That's not longer
        // necessary (no service parameters), and we can fake things here easily enough with
        // fixed actionId of 0.

        _targetActionId = 0;
        _actionId = -1;

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
            page.endPageRender();

            reset();
            _rewinding = false;
        }
    }

    /**
     * Rewinds the page by invoking {@link IPage#renderPage(ResponseBuilder, IRequestCycle)}.
     * <p>
     * The process is expected to end with a {@link RenderRewoundException}. If the entire page is
     * renderred without this exception being thrown, it means that the target action id was not
     * valid, and a {@link ApplicationRuntimeException}is thrown.
     * <p>
     * This clears all attributes.
     * 
     * @deprecated To be removed in 4.1 with no replacement.
     */

    public void rewindPage(String targetActionId, IComponent targetComponent)
    {
        _rewinding = true;

        _actionId = -1;
        
        // Parse the action Id as hex since that's whats generated
        // by getNextActionId()
        _targetActionId = Integer.parseInt(targetActionId, 16);
        _targetComponent = targetComponent;
        
        try
        {
            //TODO: pass in a valid ResponseBuilder!
            _page.renderPage(null, this);
            
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
            _rewinding = false;

            reset();
        }

    }

    public void setAttribute(String name, Object value)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Set attribute " + name + " to " + value);

        _attributes.put(name, value);
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
     * As of 4.0, just a synonym for {@link #forgetPage(String)}.
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
        return getListenerParameters();
    }

    /** @since 2.0.3 * */

    public void setServiceParameters(Object[] serviceParameters)
    {
        setListenerParameters(serviceParameters);
    }

    /** @since 4.0 */
    public Object[] getListenerParameters()
    {
        return _listenerParameters;
    }

    /** @since 4.0 */
    public void setListenerParameters(Object[] parameters)
    {
        _listenerParameters = parameters;
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
        Defense.notNull(page, "page");

        if (LOG.isDebugEnabled())
            LOG.debug("Activating page " + page);

        Tapestry.clearMethodInvocations();

        page.validate(this);

        Tapestry
                .checkMethodInvocation(Tapestry.ABSTRACTPAGE_VALIDATE_METHOD_ID, "validate()", page);

        _page = page;
    }

    /** @since 4.0 */
    public String getParameter(String name)
    {
        return _parameters.getParameterValue(name);
    }

    /** @since 4.0 */
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

        b.append("serviceName", _serviceName);

        b.append("serviceParameters", _listenerParameters);

        if (_loadedPages != null)
            b.append("loadedPages", _loadedPages.keySet());

        b.append("attributes", _attributes);
        b.append("targetActionId", _targetActionId);
        b.append("targetComponent", _targetComponent);

        return b.toString();
    }

    /** @since 4.0 */

    public String getAbsoluteURL(String partialURL)
    {
        String contextPath = _infrastructure.getRequest().getContextPath();

        return _absoluteURLBuilder.constructURL(contextPath + partialURL);
    }

    /** @since 4.0 */

    public void forgetPage(String pageName)
    {
        Defense.notNull(pageName, "pageName");

        _strategySource.discardAllStoredChanged(pageName);
    }

    /** @since 4.0 */

    public Infrastructure getInfrastructure()
    {
        return _infrastructure;
    }

    /** @since 4.0 */

    public String getUniqueId(String baseId)
    {
        return _idAllocator.allocateId(baseId);
    }

    /** @since 4.0 */
    public void sendRedirect(String URL)
    {
        throw new RedirectException(URL);
    }

}
