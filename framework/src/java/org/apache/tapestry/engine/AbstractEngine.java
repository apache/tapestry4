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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.Constants;
import org.apache.tapestry.IEngine;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.StaleLinkException;
import org.apache.tapestry.StaleSessionException;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.TapestryConstants;
import org.apache.tapestry.listener.ListenerMap;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.request.ResponseOutputStream;
import org.apache.tapestry.services.DataSqueezer;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Basis for building real Tapestry applications. Immediate subclasses provide different strategies
 * for managing page state and other resources between request cycles.
 * <p>
 * Note: much of this description is <em>in transition</em> as part of Tapestry 3.1. All ad-hoc
 * singletons and such are being replaced with HiveMind services.
 * <p>
 * Uses a shared instance of {@link ITemplateSource},{@link ISpecificationSource},
 * {@link IScriptSource}and {@link IComponentMessagesSource}stored as attributes of the
 * {@link ServletContext}(they will be shared by all sessions).
 * <p>
 * An engine is designed to be very lightweight. Particularily, it should <b>never </b> hold
 * references to any {@link IPage}or {@link org.apache.tapestry.IComponent}objects. The entire
 * system is based upon being able to quickly rebuild the state of any page(s).
 * <p>
 * Where possible, instance variables should be transient. They can be restored inside
 * {@link #setupForRequest(RequestContext)}.
 * <p>
 * In practice, a subclass (usually {@link BaseEngine}) is used without subclassing. Instead, a
 * visit object is specified. To facilitate this, the application specification may include a
 * property, <code>org.apache.tapestry.visit-class</code> which is the class name to instantiate
 * when a visit object is first needed. See {@link #createVisit(IRequestCycle)}for more details.
 * <p>
 * Some of the classes' behavior is controlled by JVM system properties (typically only used during
 * development): <table border=1>
 * <tr>
 * <th>Property</th>
 * <th>Description</th>
 * </tr>
 * <tr>
 * <td>org.apache.tapestry.enable-reset-service</td>
 * <td>If true, enabled an additional service, reset, that allow page, specification and template
 * caches to be cleared on demand. See {@link #isResetServiceEnabled()}.</td>
 * </tr>
 * <tr>
 * <td>org.apache.tapestry.disable-caching</td>
 * <td>If true, then the page, specification, template and script caches will be cleared after each
 * request. This slows things down, but ensures that the latest versions of such files are used.
 * Care should be taken that the source directories for the files preceeds any versions of the files
 * available in JARs or WARs.</td>
 * </tr>
 * </table>
 * 
 * @author Howard Lewis Ship
 */

public abstract class AbstractEngine implements IEngine
{
    private static final Log LOG = LogFactory.getLog(AbstractEngine.class);

    /**
     * The link to the world of HiveMind services.
     * 
     * @since 3.1
     */
    private Infrastructure _infrastructure;

    private ListenerMap _listeners;

    /**
     * The name of the application property that will be used to determine the encoding to use when
     * generating the output
     * 
     * @since 3.0
     */

    public static final String OUTPUT_ENCODING_PROPERTY_NAME = "org.apache.tapestry.output-encoding";

    /**
     * The default encoding that will be used when generating the output. It is used if no output
     * encoding property has been specified.
     * 
     * @since 3.0
     */

    public static final String DEFAULT_OUTPUT_ENCODING = "UTF-8";

    /**
     * The curent locale for the engine, which may be changed at any time.
     */

    private Locale _locale;

    /**
     * The name of the application specification property used to specify the class of the visit
     * object.
     */

    public static final String VISIT_CLASS_PROPERTY_NAME = "org.apache.tapestry.visit-class";

    /**
     * If true (set from the JVM system parameter <code>org.apache.tapestry.disable-caching</code>)
     * then the cache of pages, specifications and template will be cleared after each request.
     */

    private static final boolean _disableCaching = Boolean
            .getBoolean("org.apache.tapestry.disable-caching");

    /**
     * The instance of {@link IMonitorFactory}used to create a monitor.
     * 
     * @since 3.0
     */

    private IMonitorFactory _monitorFactory;

    /**
     * Sets the Exception page's exception property, then renders the Exception page.
     * <p>
     * If the render throws an exception, then copious output is sent to <code>System.err</code>
     * and a {@link ServletException}is thrown.
     */

    protected void activateExceptionPage(IRequestCycle cycle, ResponseOutputStream output,
            Throwable cause)
    {
        try
        {
            IPage exceptionPage = cycle.getPage(getExceptionPageName());

            exceptionPage.setProperty("exception", cause);

            cycle.activate(exceptionPage);

            renderResponse(cycle, output);

        }
        catch (Throwable ex)
        {
            // Worst case scenario. The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.

            reportException(
                    Tapestry.getMessage("AbstractEngine.unable-to-process-client-request"),
                    cause);

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.

            reportException(
                    Tapestry.getMessage("AbstractEngine.unable-to-present-exception-page"),
                    ex);

            // And throw the exception.

            throw new ApplicationRuntimeException(ex.getMessage(), ex);
        }
    }

    /**
     * Writes a detailed report of the exception to <code>System.err</code>.
     */

    public void reportException(String reportTitle, Throwable ex)
    {
        _infrastructure.getRequestExceptionReporter().reportRequestException(reportTitle, ex);
    }

    /**
     * Invoked at the end of the request cycle to release any resources specific to the request
     * cycle.
     */

    protected abstract void cleanupAfterRequest(IRequestCycle cycle);

    /**
     * Returns the locale for the engine. This is initially set by the {@link ApplicationServlet}
     * but may be updated by the application.
     */

    public Locale getLocale()
    {
        return _locale;
    }

    /**
     * Returns a service with the given name.
     * 
     * @see Infrastructure#getServiceMap()
     * @see org.apache.tapestry.services.ServiceMap
     */

    public IEngineService getService(String name)
    {
        return _infrastructure.getServiceMap().getService(name);
    }

    /** @see Infrastructure#getApplicationSpecification() */

    public IApplicationSpecification getSpecification()
    {
        return _infrastructure.getApplicationSpecification();
    }

    /** @see Infrastructure#getSpecificationSource() */

    public ISpecificationSource getSpecificationSource()
    {
        return _infrastructure.getSpecificationSource();
    }

    /**
     * Invoked, typically, when an exception occurs while servicing the request. This method resets
     * the output, sets the new page and renders it.
     */

    protected void redirect(String pageName, IRequestCycle cycle, ResponseOutputStream out,
            ApplicationRuntimeException exception) throws IOException, ServletException
    {
        // Discard any output from the previous page.

        out.reset();

        IPage page = cycle.getPage(pageName);

        cycle.activate(page);

        renderResponse(cycle, out);
    }

    /**
     * Delegates to
     * {@link org.apache.tapestry.services.ResponseRenderer#renderResponse(IRequestCycle, ResponseOutputStream)}.
     */

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
            throws ServletException, IOException
    {
        _infrastructure.getResponseRenderer().renderResponse(cycle, output);
    }

    /**
     * Delegate method for the servlet. Services the request.
     */

    public void service(WebRequest request, WebResponse response) throws IOException
    {
        IRequestCycle cycle = null;
        IMonitor monitor = null;
        IEngineService service = null;
        ResponseOutputStream output = null;

        if (_infrastructure == null)
            _infrastructure = (Infrastructure) request.getAttribute(Constants.INFRASTRUCTURE_KEY);

        try
        {
            output = new ResponseOutputStream(response);
        }
        catch (Exception ex)
        {
            reportException(Tapestry.getMessage("AbstractEngine.unable-to-begin-request"), ex);

            throw new ApplicationRuntimeException(ex.getMessage(), ex);
        }

        try
        {
            try
            {
                cycle = _infrastructure.getRequestCycleFactory().newRequestCycle(
                        this,
                        request,
                        response);

                monitor = cycle.getMonitor();
                service = cycle.getService();

                monitor.serviceBegin(service.getName(), _infrastructure.getRequest()
                        .getRequestURI());

                // Invoke the service, which returns true if it may have changed
                // the state of the engine (most do return true).

                service.service(cycle, output);

                return;
            }
            catch (PageRedirectException ex)
            {
                handlePageRedirectException(ex, cycle, output);
            }
            catch (RedirectException ex)
            {
                handleRedirectException(cycle, ex);
            }
            catch (StaleLinkException ex)
            {
                handleStaleLinkException(ex, cycle, output);
            }
            catch (StaleSessionException ex)
            {
                handleStaleSessionException(ex, cycle, output);
            }
        }
        catch (Exception ex)
        {
            monitor.serviceException(ex);

            // Discard any output (if possible). If output has already been sent
            // to
            // the client, then things get dicey. Note that this block
            // gets activated if the StaleLink or StaleSession pages throws
            // any kind of exception.

            // Attempt to switch to the exception page. However, this may itself
            // fail
            // for a number of reasons, in which case a ServletException is
            // thrown.

            output.reset();

            if (LOG.isDebugEnabled())
                LOG.debug("Uncaught exception", ex);

            activateExceptionPage(cycle, output, ex);
        }
        finally
        {
            if (service != null)
                monitor.serviceEnd(service.getName());

            try
            {
                cycle.cleanup();
                _infrastructure.getApplicationStateManager().flush();

                // Closing the buffered output closes the underlying stream as
                // well.

                if (output != null)
                    output.forceFlush();

                cleanupAfterRequest(cycle);
            }
            catch (Exception ex)
            {
                reportException(Tapestry.getMessage("AbstractEngine.exception-during-cleanup"), ex);
            }

            if (_disableCaching)
            {
                try
                {
                    _infrastructure.getResetEventCoordinator().fireResetEvent();
                }
                catch (Exception ex)
                {
                    reportException(Tapestry
                            .getMessage("AbstractEngine.exception-during-cache-clear"), ex);
                }
            }

        }
    }

    /**
     * Handles {@link PageRedirectException}&nbsp;which involves executing
     * {@link IPage#validate(IRequestCycle)}on the target page (of the exception), until either a
     * loop is found, or a page succesfully validates and can be activated.
     * <p>
     * This should generally not be overriden in subclasses.
     * 
     * @since 3.0
     */

    protected void handlePageRedirectException(PageRedirectException ex, IRequestCycle cycle,
            ResponseOutputStream output) throws IOException, ServletException
    {
        List pageNames = new ArrayList();

        String pageName = ex.getTargetPageName();

        while (true)
        {
            if (pageNames.contains(pageName))
            {
                // Add the offending page to pageNames so it shows in the
                // list.

                pageNames.add(pageName);

                StringBuffer buffer = new StringBuffer();
                int count = pageNames.size();

                for (int i = 0; i < count; i++)
                {
                    if (i > 0)
                        buffer.append("; ");

                    buffer.append(pageNames.get(i));
                }

                throw new ApplicationRuntimeException(Tapestry.format(
                        "AbstractEngine.validate-cycle",
                        buffer.toString()));
            }

            // Record that this page has been a target.

            pageNames.add(pageName);

            try
            {
                // Attempt to activate the new page.

                cycle.activate(pageName);

                break;
            }
            catch (PageRedirectException ex2)
            {
                pageName = ex2.getTargetPageName();
            }
        }

        // Discard any output from the previous page.

        output.reset();

        renderResponse(cycle, output);
    }

    /**
     * Invoked by {@link #service(RequestContext)}if a {@link StaleLinkException}is thrown by the
     * {@link IEngineService service}. This implementation sets the message property of the
     * StaleLink page to the message provided in the exception, then invokes
     * {@link #redirect(String, IRequestCycle, ResponseOutputStream, ApplicationRuntimeException)}
     * to render the StaleLink page.
     * <p>
     * Subclasses may overide this method (without invoking this implementation). A common practice
     * is to present an error message on the application's Home page.
     * <p>
     * Alternately, the application may provide its own version of the StaleLink page, overriding
     * the framework's implementation (probably a good idea, because the default page hints at
     * "application errors" and isn't localized). The overriding StaleLink implementation must
     * implement a message property of type String.
     * 
     * @since 0.2.10
     */

    protected void handleStaleLinkException(StaleLinkException ex, IRequestCycle cycle,
            ResponseOutputStream output) throws IOException, ServletException
    {
        String staleLinkPageName = getStaleLinkPageName();
        IPage page = cycle.getPage(staleLinkPageName);

        page.setProperty("message", ex.getMessage());

        redirect(staleLinkPageName, cycle, output, ex);
    }

    /**
     * Invoked by {@link #service(RequestContext)}if a {@link StaleSessionException}is thrown by
     * the {@link IEngineService service}. This implementation invokes
     * {@link #redirect(String, IRequestCycle, ResponseOutputStream, ApplicationRuntimeException)}
     * to render the StaleSession page.
     * <p>
     * Subclasses may overide this method (without invoking this implementation). A common practice
     * is to present an eror message on the application's Home page.
     * 
     * @since 0.2.10
     */

    protected void handleStaleSessionException(StaleSessionException ex, IRequestCycle cycle,
            ResponseOutputStream output) throws IOException, ServletException
    {
        redirect(getStaleSessionPageName(), cycle, output, ex);
    }

    /**
     * Changes the locale for the engine.
     */

    public void setLocale(Locale value)
    {
        Defense.notNull(value, "locale");

        _locale = value;

        // The locale may be set before the engine is initialized with the Infrastructure.

        if (_infrastructure != null)
            _infrastructure.setLocale(value);
    }

    /**
     * @see Infrastructure#getClassResolver()
     */

    public ClassResolver getClassResolver()
    {
        return _infrastructure.getClassResolver();
    }

    /**
     * Generates a description of the instance. Invokes {@link #extendDescription(ToStringBuilder)}
     * to fill in details about the instance.
     * 
     * @see #extendDescription(ToStringBuilder)
     */

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("locale", _locale);

        return builder.toString();
    }

    /**
     * Implemented by subclasses to return the names of the active pages (pages for which recorders
     * exist). May return the empty list, but should not return null.
     */

    abstract public Collection getActivePageNames();

    /**
     * Gets the visit object from the
     * {@link org.apache.tapestry.engine.state.ApplicationStateManager}, creating it if it does not
     * already exist.
     * <p>
     * As of Tapestry 3.1, this will always create the visit object, possibly creating a new session
     * in the process.
     */

    public Object getVisit()
    {
        return _infrastructure.getApplicationStateManager().get("visit");
    }

    public void setVisit(Object visit)
    {
        _infrastructure.getApplicationStateManager().store("visit", visit);
    }

    /**
     * Gets the visit object from the
     * {@link org.apache.tapestry.engine.state.ApplicationStateManager}, which will create it as
     * necessary.
     */

    public Object getVisit(IRequestCycle cycle)
    {
        return getVisit();
    }

    public boolean getHasVisit()
    {
        return _infrastructure.getApplicationStateManager().exists("visit");
    }

    /**
     * Returns the global object for the application. The global object is created at the start of
     * the request ({@link #setupForRequest(RequestContext)}invokes
     * {@link #createGlobal(RequestContext)}if needed), and is stored into the
     * {@link ServletContext}. All instances of the engine for the application share the global
     * object; however, the global object is explicitly <em>not</em> replicated to other servers
     * within a cluster.
     * 
     * @since 2.3
     */

    public Object getGlobal()
    {
        return _infrastructure.getApplicationStateManager().get("global");
    }

    public IScriptSource getScriptSource()
    {
        return _infrastructure.getScriptSource();
    }

    /**
     * Allows subclasses to include listener methods easily.
     * 
     * @since 1.0.2
     */

    public ListenerMap getListeners()
    {
        if (_listeners == null)
            _listeners = new ListenerMap(this);

        return _listeners;
    }

    /**
     * Invoked when a {@link RedirectException}is thrown during the processing of a request.
     * 
     * @throws ApplicationRuntimeException
     *             if an {@link IOException},{@link ServletException}is thrown by the redirect,
     *             or if no {@link RequestDispatcher}can be found for local resource.
     * @since 2.2
     */

    protected void handleRedirectException(IRequestCycle cycle, RedirectException ex)
    {
        String location = ex.getRedirectLocation();

        if (LOG.isDebugEnabled())
            LOG.debug("Redirecting to: " + location);

        _infrastructure.getRequest().forward(location);
    }

    /**
     * @see Infrastructure#getDataSqueezer()
     */

    public DataSqueezer getDataSqueezer()
    {
        return _infrastructure.getDataSqueezer();
    }

    /** @since 2.3 */

    public IPropertySource getPropertySource()
    {
        return _infrastructure.getApplicationPropertySource();
    }

    /** @since 3.0 */

    protected String getExceptionPageName()
    {
        return TapestryConstants.EXCEPTION_PAGE;
    }

    /** @since 3.0 */

    protected String getStaleLinkPageName()
    {
        return TapestryConstants.STALE_LINK_PAGE;
    }

    /** @since 3.0 */

    protected String getStaleSessionPageName()
    {
        return TapestryConstants.STALE_SESSION_PAGE;
    }

    /**
     * The encoding to be used if none has been defined using the output encoding property. Override
     * this method to change the default.
     * 
     * @return the default output encoding
     * @since 3.0
     */
    protected String getDefaultOutputEncoding()
    {
        return DEFAULT_OUTPUT_ENCODING;
    }

    /**
     * Returns the encoding to be used to generate the servlet responses and accept the servlet
     * requests. The encoding is defined using the org.apache.tapestry.output-encoding and is UTF-8
     * by default
     * 
     * @since 3.0
     * @see org.apache.tapestry.IEngine#getOutputEncoding()
     */
    public String getOutputEncoding()
    {
        IPropertySource source = getPropertySource();

        String encoding = source.getPropertyValue(OUTPUT_ENCODING_PROPERTY_NAME);
        if (encoding == null)
            encoding = getDefaultOutputEncoding();

        return encoding;
    }

    /** @since 3.1 */
    public Infrastructure getInfrastructure()
    {
        return _infrastructure;
    }
}