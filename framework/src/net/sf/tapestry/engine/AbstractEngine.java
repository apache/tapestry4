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

package net.sf.tapestry.engine;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.apache.log4j.Category;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.ApplicationServlet;
import net.sf.tapestry.IEngine;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IEngineServiceView;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IMonitor;
import net.sf.tapestry.IPage;
import net.sf.tapestry.IPageRecorder;
import net.sf.tapestry.IPageSource;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceResolver;
import net.sf.tapestry.IScriptSource;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.ITemplateSource;
import net.sf.tapestry.PageRedirectException;
import net.sf.tapestry.RedirectException;
import net.sf.tapestry.RequestContext;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.ResponseOutputStream;
import net.sf.tapestry.StaleLinkException;
import net.sf.tapestry.StaleSessionException;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.listener.ListenerMap;
import net.sf.tapestry.pageload.PageSource;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.util.exception.ExceptionAnalyzer;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Basis for building real Tapestry applications.  Immediate subclasses
 *  provide different strategies for managing page state and other resources
 *  between request cycles.  
 *
 *  Uses a shared instance of
 *  {@link ITemplateSource} and {@link ISpecificationSource}
 *  stored as attributes of the  {@link ServletContext} 
 *  (they will be shared by all sessions).
 *
 *  <p>An application is designed to be very lightweight.
 *  Particularily, it should <b>never</b> hold references to any
 *  {@link IPage} or {@link IComponent} objects.  The entire system is
 *  based upon being able to quickly rebuild the state of any page(s).
 *
 * <p>Where possible, instance variables should be transient.  They
 * can be restored inside {@link #setupForRequest(RequestContext)}.
 *
 *  <p>In practice, a subclass (usually {@link SimpleEngine})
 *  is used without subclassing.  Instead, a 
 *  visit object is specified.  To facilitate this, the application specification
 *  may include a property, <code>net.sf.tapestry.visit-class</code>
 *  which is the class name  to instantiate when a visit object is first needed.  See
 *  {@link #createVisit(IRequestCycle)} for more details.
 *
 * <p>Some of the classes' behavior is controlled by JVM system parameters
 * (typically only used during development):
 *
 * <table border=1>
 * 	<tr> <th>Parameter</th> <th>Description</th> </tr>
 *  <tr> <td>net.sf.tapestry.enable-reset-service</td>
 *		<td>If true, enabled an additional service, reset, that
 *		allow page, specification and template caches to be cleared on demand.
 *  	See {@link #isResetServiceEnabled()}. </td>
 * </tr>
 * <tr>
 *		<td>net.sf.tapestry.disable-caching</td>
 *	<td>If true, then the page, specification, template and script caches
 *  will be cleared after each request. This slows things down,
 *  but ensures that the latest versions of such files are used.
 *  Care should be taken that the source directories for the files
 *  preceeds any versions of the files available in JARs or WARs. </td>
 * </tr>
 * </table>
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class AbstractEngine
    implements IEngine, IEngineServiceView, Externalizable, HttpSessionBindingListener
{
    private static final Category CAT = Category.getInstance(AbstractEngine.class);

    private transient String contextPath;
    private transient String servletPath;
    private transient String clientAddress;
    private transient String sessionId;
    private transient boolean stateful;
    private transient ListenerMap listeners;

    /**
     *  An object used to contain application-specific server side state.
     *
     **/

    private Object visit;

    /**
     *  The curent locale for the engine, which may be changed at any time.
     *
     **/

    private Locale locale;

    /**
     *  Set by {@link #setLocale(Locale)} when the locale is changed;
     *  this allows the locale cookie to be updated.
     *
     **/

    private boolean localeChanged;

    /**
     *  The specification for the application, which
     *  lives in the {@link ServletContext}.  If the
     *  session (and application) moves to a different context (i.e.,
     *  a different JVM), then
     *  we want to reconnect to the specification in the new context.
     *  A check is made on every request
     *  cycle as needed.
     *
     **/

    protected transient ApplicationSpecification specification;

    /**
     *  The source for template data. The template source is stored
     *  in the {@link ServletContext} as a named attribute.
     *  After de-serialization, the application can re-connect to
     *  the template source (or create a new one).
     *
     **/

    protected transient ITemplateSource templateSource;

    /**
     *  The source for component specifications, stored in the
     *  {@link ServletContext} (like {@link #templateSource}).
     *
     **/

    protected transient ISpecificationSource specificationSource;

    /**
     *  The source for parsed scripts, again, stored in the
     * {@link ServletContext}.
     *
     * @since 1.0.2
     **/

    private transient IScriptSource scriptSource;

    /** 
     *  The name of the context attribute for the {@link IScriptSource} instance.
     *  The application's name is appended.
     *
     *  @since 1.0.2
     *
     **/

    protected static final String SCRIPT_SOURCE_NAME = "net.sf.tapestry.DefaultScriptSource";

    private transient Map services;

    private static final int MAP_SIZE = 7;

    /**
     *  The name of the application specification property used to specify the
     *  class of the visit object.
     *
     **/

    public static final String VISIT_CLASS_PROPERTY_NAME = "net.sf.tapestry.visit-class";

    /**
     *  Servlet context attribute name for the default {@link ITemplateSource}
     *  instance.  The application's name is appended.
     *
     **/

    protected static final String TEMPLATE_SOURCE_NAME = "net.sf.tapestry.TemplateSource";

    /**
     *  Servlet context attribute name for the default {@link ISpecificationSource}
     *  instance.  The application's name is appended.
     *
     **/

    protected static final String SPECIFICATION_SOURCE_NAME = "net.sf.tapestry.SpecificationSource";

    /**
     *  Servlet context attribute name for the {@link IPageSource}
     *  instance.  The application's name is appended.
     *
     **/

    protected static final String PAGE_SOURCE_NAME = "net.sf.tapestry.PageSource";

    /**
     *  The source for pages, which acts as a pool, but is capable of
     *  creating pages as needed.  Stored in the
     *  {@link ServletContext}, like {@link #templateSource}.
     *
     **/

    protected transient IPageSource pageSource;

    /**
     *  If true (set from JVM system parameter
     *  <code>net.sf.tapestry.enable-reset-service</code>)
     *  then the reset service will be enabled, allowing
     *  the cache of pages, specifications and template
     *  to be cleared on demand.
     *
     **/

    private static boolean resetServiceEnabled =
        Boolean.getBoolean("net.sf.tapestry.enable-reset-service");

    /**
     * If true (set from the JVM system parameter
     * <code>net.sf.tapestry.disable-caching</code>)
     * then the cache of pages, specifications and template
     * will be cleared after each request.
     *
     **/

    private static boolean disableCaching = Boolean.getBoolean("net.sf.tapestry.disable-caching");

    private transient IResourceResolver resolver;

    /**
     *  Map from service name to service instance.
     * 
     *  @since 1.0.9
     * 
     **/

    private transient Map serviceMap;

    protected static final String SERVICE_MAP_NAME = "net.sf.tapestry.ServiceMap";

    /**
     *  Sets the Exception page's exception property, then renders the Exception page.
     *
     *  <p>If the render throws an exception, then copious output is sent to
     *  <code>System.err</code> and a {@link ServletException} is thrown.
     *
     **/

    protected void activateExceptionPage(
        IRequestCycle cycle,
        ResponseOutputStream output,
        Throwable cause)
        throws ServletException
    {
        try
        {
            IPage exceptionPage = cycle.getPage(EXCEPTION_PAGE);

            PropertyHelper helper = PropertyHelper.forClass(exceptionPage.getClass());

            helper.set(exceptionPage, "exception", cause);

            cycle.setPage(exceptionPage);

            renderResponse(cycle, output);

        }
        catch (Throwable ex)
        {
            // Worst case scenario.  The exception page itself is broken, leaving
            // us with no option but to write the cause to the output.

            reportException(Tapestry.getString("AbstractEngine.unable-to-process-client-request"), cause);

            // Also, write the exception thrown when redendering the exception
            // page, so that can get fixed as well.

            reportException(Tapestry.getString("AbstractEngine.unable-to-present-exception-page"), ex);

            // And throw the exception.

            throw new ServletException(ex.getMessage(), ex);
        }
    }

    /**
     *  Writes a detailed report of the exception to <code>System.err</code>.
     *
     **/

    public void reportException(String reportTitle, Throwable ex)
    {
        CAT.warn(reportTitle, ex);

        System.err.println("\n\n**********************************************************\n\n");

        System.err.println(reportTitle);

        System.err.println(
            "\n\n      Session id: "
                + sessionId
                + "\n  Client address: "
                + clientAddress
                + "\n\nExceptions:\n");

        new ExceptionAnalyzer().reportException(ex, System.err);

        System.err.println("\n**********************************************************\n");

    }

    /**
     *  Invoked at the end of the request cycle to release any resources specific
     *  to the request cycle.
     *
     **/

    protected abstract void cleanupAfterRequest(IRequestCycle cycle);

    /**
     *  Extends the description of the class generated by {@link #toString()}.
     *  If a subclass adds additional instance variables that should be described
     *  in the instance description, it may overide this method.  Subclasses
     *  should invoke this implementation first.  They should append a space
     *  before each value.
     *
     *  @see #toString()
     **/

    public void extendDescription(StringBuffer buffer)
    {
        // In rare cases, toString() may be invoked before
        // the engine has a change to obtain the specification
        // from the servlet.

        if (specification == null)
            buffer.append(Tapestry.getString("AbstractEngine.unknown-specification"));
        else
            buffer.append(specification.getName());
    }

    /**
     *  Returns the locale for the engine.  This is initially set
     *  by the {@link ApplicationServlet} but may be updated
     *  by the application.
     *
     **/

    public Locale getLocale()
    {
        return locale;
    }

    /**
     *  Overriden in subclasses that support monitoring.  Should create and return
     *  an instance of {@link IMonitor} that is appropriate for the request cycle described
     *  by the {@link RequestContext}.  May return null.
     *
     *  <p>The monitor is used to create a {@link RequestCycle}.
     *
     *  <p>This implementation returns null always.  Subclasses may overide without
     *  invoking it.
     *
     *  <p>TBD:  Lifecycle of the monitor ... should there be a commit?
     *
     **/

    public IMonitor getMonitor(RequestContext context)
    {
        return null;
    }

    public IPageSource getPageSource()
    {
        return pageSource;
    }

    /**
     *  Returns a service with the given name.  Services are created by the
     *  first call to {@link #setupForRequest(RequestContext)}.
     **/

    public IEngineService getService(String name)
    {
        IEngineService result = (IEngineService) serviceMap.get(name);

        if (result == null)
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractEngine.unknown-service", name));

        return result;
    }

    public String getServletPath()
    {
        return servletPath;
    }

    /**
     * Returns the context path, the prefix to apply to any URLs so that they
     * are recognized as belonging to the Servlet 2.2 context.
     *
     *  @see ContextAsset
     **/

    public String getContextPath()
    {
        return contextPath;
    }

    /**
     *  Returns the specification, if available, or null otherwise.
     *
     *  <p>To facilitate deployment across multiple servlet containers, the
     *  application is serializable.  However, the reference to the specification
     *  is transient.   When an application instance is deserialized, it reconnects
     *  with the application specification by locating it in the {@link ServletContext}
     *  or parsing it fresh.
     *
     **/

    public ApplicationSpecification getSpecification()
    {
        return specification;
    }

    public ISpecificationSource getSpecificationSource()
    {
        return specificationSource;
    }

    public ITemplateSource getTemplateSource()
    {
        return templateSource;
    }

    /**
     *  Reads the state serialized by {@link #writeExternal(ObjectOutput)}.
     *
     *  <p>This always set the stateful flag.  By default, a deserialized
     *  session is stateful (else, it would not have been serialized).
     **/

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException
    {
        stateful = true;

        String localeName = in.readUTF();
        locale = Tapestry.getLocale(localeName);

        visit = in.readObject();
    }

    /**
     *  Writes the following properties:
     *
     *  <ul>
     *  <li>locale name ({@link Locale#toString()})
     *  <li>visit
     *  </ul>
     *
     **/

    public void writeExternal(ObjectOutput out) throws IOException
    {
        out.writeUTF(locale.toString());
        out.writeObject(visit);
    }

    /**
     *  Invoked, typically, when an exception occurs while servicing the request.
     *  This method resets the output, sets the new page and renders it.
     *
     **/

    protected void redirect(
        String pageName,
        IRequestCycle cycle,
        ResponseOutputStream out,
        RequestCycleException exception)
        throws IOException, RequestCycleException, ServletException
    {
        // Discard any output from the previous page.

        out.reset();

        cycle.setPage(pageName);

        renderResponse(cycle, out);
    }

    public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
        throws RequestCycleException, ServletException, IOException
    {
        IMarkupWriter writer;
        boolean discard = true;
        IPage page;

        if (CAT.isDebugEnabled())
            CAT.debug("Begin render response.");

        // If the locale has changed during this request cycle then
        // do the work to propogate the locale change into
        // subsequent request cycles.

        if (localeChanged)
        {
            localeChanged = false;

            RequestContext context = cycle.getRequestContext();
            ApplicationServlet servlet = context.getServlet();

            servlet.writeLocaleCookie(locale, this, context);
        }

        // Commit all changes and ignore further changes.

        page = cycle.getPage();

        writer = page.getResponseWriter(output);

        output.setContentType(writer.getContentType());

        try
        {
            cycle.renderPage(writer);

            discard = false;
        }
        finally
        {
            // Closing the writer closes its PrintWriter and a whole stack of java.io objects,
            // which tend to stream a lot of output that eventually hits the
            // ResponseOutputStream.  If we are discarding output anyway (due to an exception
            // getting thrown during the render), we can save ourselves some trouble
            // by ignoring it.

            if (discard)
                output.setDiscard(true);

            writer.close();

            if (discard)
                output.setDiscard(false);
        }

    }

    /**
     * Invalidates the session, then redirects the client web browser to
     * the servlet's prefix, starting a new visit.
     *
     * <p>Subclasses should perform their own restart (if necessary, which is
     * rarely) before invoking this implementation.
     *
     **/

    public void restart(IRequestCycle cycle) throws IOException
    {

        RequestContext context = cycle.getRequestContext();

        HttpSession session = context.getSession();

        if (session != null)
        {
            try
            {
                session.invalidate();
            }
            catch (IllegalStateException ex)
            {
                if (CAT.isDebugEnabled())
                    CAT.debug("Exception thrown invalidating HttpSession.", ex);

                // Otherwise, ignore it.
            }
        }

        // Make isStateful() return false, so that the servlet doesn't
        // try to store the engine back into the (now invalid) session.

        stateful = false;

        String url = context.getAbsoluteURL(servletPath);

        context.redirect(url);
    }

    /**
     *  Delegate method for the servlet.  Services the request.
     *
     **/

    public boolean service(RequestContext context) throws ServletException, IOException
    {
        RequestCycle cycle = null;
        ResponseOutputStream output = null;
        IMonitor monitor;

        if (CAT.isInfoEnabled())
            CAT.info("Begin service " + context.getRequest().getRequestURI());

        if (specification == null)
            specification = context.getServlet().getApplicationSpecification();

        // The servlet invokes setLocale() before invoking service().  We want
        // to ignore that setLocale() ... that is, not force a cookie to be
        // written.

        localeChanged = false;

        // Build the resolver around the servlet, since that's guarenteed
        // to be in the application's class loader (which has the broadest
        // possible view).

        if (resolver == null)
            resolver = new ResourceResolver(context.getServlet());

        try
        {
            setupForRequest(context);

            monitor = getMonitor(context);

            cycle = new RequestCycle(this, context, monitor);

            output = new ResponseOutputStream(context.getResponse());
        }
        catch (Exception ex)
        {
            reportException(Tapestry.getString("AbstractEngine.unable-to-begin-request"), ex);

            throw new ServletException(ex.getMessage(), ex);
        }

        IEngineService service = null;

        try
        {
            try
            {
                String serviceName = context.getParameter(IEngineService.SERVICE_QUERY_PARAMETER_NAME);

                if (Tapestry.isNull(serviceName))
                    serviceName = IEngineService.HOME_SERVICE;

                service = getService(serviceName);

                cycle.setService(service);

                if (monitor != null)
                    monitor.serviceBegin(service.getName(), context.getRequest().getRequestURI());

                return service.service(this, cycle, output);
            }
            catch (PageRedirectException ex)
            {
                redirect(ex.getTargetPageName(), cycle, output, ex);
            }
            catch (RedirectException ex)
            {
                redirectOut(cycle, ex);
            }
            catch (StaleLinkException ex)
            {
                handleStaleLinkException(ex, cycle, output);
            }
            catch (StaleSessionException ex)
            {
                handleStaleSessionException(ex, cycle, output);
            }
            finally
            {
                if (monitor != null)
                    monitor.serviceEnd(service.getName());
            }
        }
        catch (Exception ex)
        {
            if (monitor != null)
                monitor.serviceException(ex);

            // Discard any output (if possible).  If output has already been sent to
            // the client, then things get dicey.  Note that this block
            // gets activated if the StaleLink or StaleSession pages throws
            // any kind of exception.

            // Attempt to switch to the exception page.  However, this may itself fail
            // for a number of reasons, in which case a ServletException is thrown.

            output.reset();

            if (CAT.isInfoEnabled())
                CAT.info("Uncaught exception", ex);

            activateExceptionPage(cycle, output, ex);
        }
        finally
        {
            cycle.cleanup();

            // Closing the buffered output closes the underlying stream as well.

            if (output != null)
                output.forceClose();

            cleanupAfterRequest(cycle);

            if (disableCaching)
            {
                try
                {
                    clearCachedData();
                }
                catch (Exception ex)
                {
                    CAT.warn("Exception thrown while clearing caches.", ex);
                }
            }

            if (CAT.isInfoEnabled())
                CAT.info("End service");

        }

        // When in doubt, assume that the request did cause some change
        // to the engine.

        return true;
    }

    /**
     *  Invoked by {@link #service(RequestContext)} if a {@link StaleLinkException}
     *  is thrown by the {@link IEngineService service}.  This implementation
     *  invokes 
     *  {@link #redirect(String, IRequestCycle, ResponseOutputStream, RequestCycleException)}
     *  to render the StaleLink page.
     *
     *  <p>Subclasses may overide this method (without
     *  invoking this implementation).  A common practice
     *  is to present an eror message on the application's
     *  Home page.	
     *
     *  @since 0.2.10
     **/

    protected void handleStaleLinkException(
        StaleLinkException ex,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws IOException, ServletException, RequestCycleException
    {
        redirect(STALE_LINK_PAGE, cycle, output, ex);
    }

    /**
     *  Invoked by {@link #service(RequestContext)} if a {@link StaleSessionException}
     *  is thrown by the {@link IEngineService service}.  This implementation
     *  invokes 
     *  {@link #redirect(String, IRequestCycle, ResponseOutputStream, RequestCycleException)}
     *  to render the StaleSession page.
     *
     *  <p>Subclasses may overide this method (without
     *  invoking this implementation).  A common practice
     *  is to present an eror message on the application's
     *  Home page.	
     *
     *  @since 0.2.10
     **/

    protected void handleStaleSessionException(
        StaleSessionException ex,
        IRequestCycle cycle,
        ResponseOutputStream output)
        throws IOException, ServletException, RequestCycleException
    {
        redirect(STALE_SESSION_PAGE, cycle, output, ex);
    }

    /**
     *  Discards all cached pages, component specifications and templates.
     *
     *  @since 1.0.1
     **/

    public void clearCachedData()
    {
        pageSource.reset();
        specificationSource.reset();
        templateSource.reset();
        scriptSource.reset();
    }

    /**
     *  Changes the locale for the engine.
     *
     **/

    public void setLocale(Locale value)
    {
        if (value == null)
            throw new IllegalArgumentException("May not change engine locale to null.");

        // Because locale changes are expensive (it involves writing a cookie and all that),
        // we're careful not to really change unless there's a true change in value.

        if (!value.equals(locale))
        {
            locale = value;
            localeChanged = true;
        }
    }

    /**
     *  Invoked from {@link #service(RequestContext)} to ensure that the engine's
     *  instance variables are setup.  This allows the application a chance to
     *  restore transient variables that will not have survived deserialization.
     *
     *  Determines the servlet prefix:  this is the base URL used by
     *  {@link IEngineService services} to build URLs.  It consists
     *  of two parts:  the context path and the servlet path.
     *
     * <p>The servlet path is retrieved from {@link HttpServletRequest#getServletPath()}.
     *
     * <p>The context path is retrieved from {@link HttpServletRequest#getContextPath()}.
     *
     * <p>The final path is available via the {@link #getServletPath()} method.
     *
     *  <p>In addition, this method locates and/or creates the:
     *  <ul>
     *  <li>{@link ITemplateSource} 
     *  <li>{@link ISpecificationSource}
     *  <li>{@link IPageSource}
     *  <li>Helper object {@link Pool}
     *  <li>Service {@link Map}
     *  </ul>
     *
     *  <p>Subclasses should invoke this implementation first, then perform their
     *  own setup.
     *
     **/

    protected void setupForRequest(RequestContext context)
    {
        HttpServlet servlet = context.getServlet();
        ServletContext servletContext = servlet.getServletContext();
        HttpServletRequest request = context.getRequest();
        HttpSession session = context.getSession();

        if (session != null)
            sessionId = context.getSession().getId();
        else
            sessionId = null;

        clientAddress = request.getRemoteHost();
        if (clientAddress == null)
            clientAddress = request.getRemoteAddr();

        // servletPath is null, so this means either we're doing the
        // first request in this session, or we're handling a subsequent
        // request in another JVM (i.e. another server in the cluster).
        // In any case, we have to do some late (re-)initialization.

        if (servletPath == null)
        {
            // Get the path *within* the servlet context

            String path = request.getServletPath();

            // Get the context path, which may be the empty string
            // (but won't be null).

            contextPath = request.getContextPath();

            servletPath = contextPath + path;

        }

        String applicationName = specification.getName();

        if (templateSource == null)
        {
            String name = TEMPLATE_SOURCE_NAME + "." + applicationName;

            templateSource = (ITemplateSource) servletContext.getAttribute(name);

            if (templateSource == null)
            {
                templateSource = createTemplateSource();

                servletContext.setAttribute(name, templateSource);
            }
        }

        if (specificationSource == null)
        {
            String name = SPECIFICATION_SOURCE_NAME + "." + applicationName;

            specificationSource = (ISpecificationSource) servletContext.getAttribute(name);

            if (specificationSource == null)
            {
                specificationSource = createSpecificationSource();

                servletContext.setAttribute(name, specificationSource);
            }
        }

        if (pageSource == null)
        {
            String name = PAGE_SOURCE_NAME + "." + applicationName;

            pageSource = (IPageSource) servletContext.getAttribute(name);

            if (pageSource == null)
            {
                pageSource = createPageSource();

                servletContext.setAttribute(name, pageSource);
            }
        }

        if (scriptSource == null)
        {
            String name = SCRIPT_SOURCE_NAME + "." + applicationName;

            scriptSource = (IScriptSource) servletContext.getAttribute(name);

            if (scriptSource == null)
            {
                scriptSource = createScriptSource();

                servletContext.setAttribute(name, scriptSource);
            }
        }

        if (serviceMap == null)
        {
            String name = SERVICE_MAP_NAME + "." + applicationName;

            serviceMap = (Map) servletContext.getAttribute(name);

            if (serviceMap == null)
            {
                serviceMap = createServiceMap();

                servletContext.setAttribute(name, serviceMap);
            }
        }
    }

    /**
     *  Invoked from {@link #setupForRequest(RequestContext)} to provide
     *  an instance of {@link IScriptSource} that will be stored into
     *  the {@link ServletContext}.  Subclasses may override this method
     *  to provide a different implementation.
     * 
     *  
     *  @returns an instance of {@link DefaultScriptSource}
     *  @since 1.0.9
     **/

    protected IScriptSource createScriptSource()
    {
        return new DefaultScriptSource(getResourceResolver());
    }

    /**
     *  Invoked from {@link #setupForRequest(RequestContext)} to provide
     *  an instance of {@link IPageSource} that will be stored into
     *  the {@link ServletContext}.  Subclasses may override this method
     *  to provide a different implementation.
     * 
     *  @returns an instance of {@link PageSource}
     *  @since 1.0.9
     **/

    protected IPageSource createPageSource()
    {
        return new PageSource(getResourceResolver());
    }

    /**
     *  Invoked from {@link #setupForRequest(RequestContext)} to provide
     *  an instance of {@link ISpecificationSource} that will be stored into
     *  the {@link ServletContext}.  Subclasses may override this method
     *  to provide a different implementation.
     * 
     *  @returns an instance of {@link DefaultSpecificationSource}
     *  @since 1.0.9
     **/

    protected ISpecificationSource createSpecificationSource()
    {
        return new DefaultSpecificationSource(getResourceResolver(), specification);
    }

    /**
     *  Invoked from {@link #setupForRequest(RequestContext)} to provide
     *  an instance of {@link ITemplateSource} that will be stored into
     *  the {@link ServletContext}.  Subclasses may override this method
     *  to provide a different implementation.
     * 
     *  @returns an instance of {@link DefaultTemplateSource}
     *  @since 1.0.9
     **/

    protected ITemplateSource createTemplateSource()
    {
        return new DefaultTemplateSource(getResourceResolver());
    }

    /**
     *  Returns an object which can find resources and classes.
     *
     **/

    public IResourceResolver getResourceResolver()
    {
        return resolver;
    }

    /**
     *  Generates a description of the instance.  
     *  Invokes {@link #extendDescription(StringBuffer)}
     *  to fill in details about the instance.
     *
     *  @see #extendDescription(StringBuffer)
     *
     **/

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer(super.toString());

        buffer.append('[');

        extendDescription(buffer);

        buffer.append(']');

        return buffer.toString();
    }

    /**
     *  Invoked when the application object is stored into
     *  the {@link HttpSession}.  This implementation does nothing.
     *
     **/

    public void valueBound(HttpSessionBindingEvent event)
    {
    }

    /**
     *  Invoked when the application object is removed from the {@link HttpSession}.
     *  This occurs when the session times out or is explicitly invalidated
     *  (for example, by the reset or restart services).  Invokes
     *  {@link #cleanupEngine()}.
     *
     **/

    public void valueUnbound(HttpSessionBindingEvent event)
    {
        // Note: there's a possible latent bug here.  If cleaning up the
        // application requires loading any resources (specifically
        // component specifications) and we need a ResourceResolver and
        // the application instance is newly deserialized (i.e., was deserialized
        // so that it could unbound from the HttpSession) ... then 
        // we may trip over a NullPointerException because the resolver
        // will be null.  Don't have a great solution for this!

        cleanupEngine();

    }

    /**
     *  Invoked when the engine is removed from the {@link HttpSession} i.e.,
     *  because the sesssion timed out or was explicitly invalidated.
     *
     *  <p>Locates all active pages (pages which have been activated) and
     *  invokes {@link IPage#cleanupPage()} on them.  This gives 
     *  pages a chance to release any long held resources.  This primarily
     *  exists so that pages that hold references to stateful session EJBs
     *  can remove those EJBs in a timely manner.
     *
     *  <p>Subclasses may overide this method to clean up any engine-held
     *  resources, but should invoke this implementation <em>first</em>.
     **/

    protected void cleanupEngine()
    {
        Iterator i;
        String name;
        IPageSource source;
        IPageRecorder recorder;
        IPage page;

        if (CAT.isInfoEnabled())
            CAT.info(this +" cleanupEngine()");

        source = getPageSource();

        i = getActivePageNames().iterator();

        while (i.hasNext())
        {
            name = (String) i.next();

            try
            {
                page = source.getPage(this, name, null);
                recorder = getPageRecorder(name);

                recorder.rollback(page);

                page.cleanupPage();
            }
            catch (Throwable t)
            {
                reportException(Tapestry.getString("AbstractEngine.unable-to-cleanup-page", name), t);
            }
        }
    }

    /**
     *  Returns true if the reset service is curently enabled.
     *
     **/

    public boolean isResetServiceEnabled()
    {
        return resetServiceEnabled;
    }

    /**
     *  Implemented by subclasses to return the names of the active pages
     *  (pages for which recorders exist).
     *
     **/

    abstract public Collection getActivePageNames();

    /**
     *  Gets the visit object, if it has been created already.
     *
     **/

    public Object getVisit()
    {
        return visit;
    }

    /**
     *  Gets the visit object, invoking {@link #createVisit(IRequestCycle)} to create
     *  it lazily if needed.
     *
     *
     **/

    public Object getVisit(IRequestCycle cycle)
    {
        if (visit == null && cycle != null)
        {
            // Force the creation of the HttpSession

            cycle.getRequestContext().createSession();

            visit = createVisit(cycle);
        }

        return visit;
    }

    public void setVisit(Object value)
    {
        visit = value;
    }

    public boolean getHasVisit()
    {
        return visit != null;
    }

    /**
     *  Invoked to lazily create a new visit object when it is first
     *  referenced (by {@link #getVisit(IRequestCycle)}).  This implementation works
     *  by looking up the name of the class
     *  in the application specification.
     *
     *  <p>Subclasses may want to overide this method if some other means
     *  of instantiating a visit object is required.
     **/

    protected Object createVisit(IRequestCycle cycle)
    {
        String visitClassName;
        Class visitClass;
        Object result = null;

        visitClassName = specification.getProperty(VISIT_CLASS_PROPERTY_NAME);
        if (visitClassName == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractEngine.visit-class-property-not-specified", VISIT_CLASS_PROPERTY_NAME));

        if (CAT.isDebugEnabled())
            CAT.debug("Creating visit object as instance of " + visitClassName);

        visitClass = resolver.findClass(visitClassName);

        try
        {
            result = visitClass.newInstance();
        }
        catch (Throwable t)
        {
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractEngine.unable-to-instantiate-visit", visitClassName),
                t);
        }

        // Now that a visit object exists, we need to force the creation
        // of a HttpSession.

        cycle.getRequestContext().createSession();

        setStateful();

        return result;
    }

    public IScriptSource getScriptSource()
    {
        return scriptSource;
    }

    public boolean isStateful()
    {
        return stateful;
    }

    /**
     *  Invoked by subclasses to indicate that some state must now be stored
     *  in the engine (and that the engine should now be stored in the
     *  HttpSession).  The caller is responsible for actually creating
     *  the HttpSession (it will have access to the {@link RequestContext}).
     *
     *  @since 1.0.2
     *
     **/

    protected void setStateful()
    {
        stateful = true;
    }

    /**
     *  Allows subclasses to include listener methods easily.
     *
     * @since 1.0.2
     **/

    public ListenerMap getListeners()
    {
        if (listeners == null)
            listeners = new ListenerMap(this);

        return listeners;
    }

    /**
     *  Invoked when a {@link RedirectException} is thrown during the processing of a request.
     *
     *  @throws RequestCycleException if an {@link IOException} is thrown by the redirect
     *
     *  @since 1.0.6
     *
     **/

    protected void redirectOut(IRequestCycle cycle, RedirectException ex) throws RequestCycleException
    {
        String location = ex.getLocation();

        if (CAT.isDebugEnabled())
            CAT.debug("Redirecting to: " + location);

        HttpServletResponse response = cycle.getRequestContext().getResponse();

        String encodedLocation = response.encodeRedirectURL(location);

        try
        {
            response.sendRedirect(encodedLocation);
        }
        catch (IOException ioEx)
        {
            throw new RequestCycleException(
                Tapestry.getString("AbstractEngine.unable-to-redirect", location),
                null,
                ioEx);
        }

    }

    /**
     *  Creates a Map of all the services available to the application.
     * 
     *  <p>Note: the Map returned is not synchronized, on the theory that returned
     *  map is not further modified and therefore threadsafe.
     * 
     **/

    private Map createServiceMap()
    {
        if (CAT.isDebugEnabled())
            CAT.debug("Creating service map.");

        HashMap result = new HashMap();
        IResourceResolver resolver = getResourceResolver();

        Iterator i = specification.getServiceNames().iterator();

        while (i.hasNext())
        {
            String name = (String) i.next();
            String className = specification.getServiceClassName(name);

            if (CAT.isDebugEnabled())
                CAT.debug("Creating service " + name + " as instance of " + className);

            Class serviceClass = resolver.findClass(className);

            try
            {
                IEngineService service = (IEngineService) serviceClass.newInstance();
                String serviceName = service.getName();

                if (!service.getName().equals(name))
                    throw new ApplicationRuntimeException(
                        Tapestry.getString("AbstractEngine.service-name-mismatch", name, serviceClass, serviceName));

                result.put(serviceName, service);
            }
            catch (InstantiationException ex)
            {
                String message =
                    Tapestry.getString("AbstractEngine.unable-to-instantiate-service", name, className);

                CAT.error(message, ex);

                throw new ApplicationRuntimeException(message, ex);
            }
            catch (IllegalAccessException ex)
            {
                String message =
                    Tapestry.getString("AbstractEngine.unable-to-instantiate-service", name, className);

                CAT.error(message, ex);

                throw new ApplicationRuntimeException(message, ex);
            }
        }

        // Result should not be modified after this point, for threadsafety issues.
        // We could wrap it in an unmodifiable, but for efficiency we don't.

        return result;

    }
}