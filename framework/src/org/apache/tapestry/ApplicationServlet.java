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

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.util.ClasspathResource;
import org.apache.tapestry.engine.BaseEngine;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.parse.SpecificationParser;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.resource.ContextResourceLocation;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.DelegatingPropertySource;
import org.apache.tapestry.util.ServletContextPropertySource;
import org.apache.tapestry.util.ServletPropertySource;
import org.apache.tapestry.util.SystemPropertiesPropertySource;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;
import org.apache.tapestry.util.pool.Pool;
import org.apache.tapestry.util.xml.DocumentParseException;

/**
 *  Links a servlet container with a Tapestry application.  The servlet has some
 *  responsibilities related to bootstrapping the application (in terms of
 *  logging, reading the {@link ApplicationSpecification specification}, etc.).
 *  It is also responsible for creating or locating the {@link IEngine} and delegating
 *  incoming requests to it.
 * 
 *  <p>The servlet init parameter
 *  <code>org.apache.tapestry.specification-path</code>
 *  should be set to the complete resource path (within the classpath)
 *  to the application specification, i.e.,
 *  <code>/com/foo/bar/MyApp.application</code>. 
 *
 *  <p>In some servlet containers (notably
 *  <a href="www.bea.com"/>WebLogic</a>)
 *  it is necessary to invoke {@link HttpSession#setAttribute(String,Object)}
 *  in order to force a persistent value to be replicated to the other
 *  servers in the cluster.  Tapestry applications usually only have a single
 *  persistent value, the {@link IEngine engine}.  For persistence to
 *  work in such an environment, the
 *  JVM system property <code>org.apache.tapestry.store-engine</code>
 *  must be set to <code>true</code>.  This will force the application
 *  servlet to restore the engine into the {@link HttpSession} at the
 *  end of each request cycle.
 * 
 *  <p>As of release 1.0.1, it is no longer necessary for a {@link HttpSession}
 *  to be created on the first request cycle.  Instead, the HttpSession is created
 *  as needed by the {@link IEngine} ... that is, when a visit object is created,
 *  or when persistent page state is required.  Otherwise, for sessionless requests,
 *  an {@link IEngine} from a {@link Pool} is used.  Additional work must be done
 *  so that the {@link IEngine} can change locale <em>without</em> forcing 
 *  the creation of a session; this involves the servlet and the engine storing
 *  locale information in a {@link Cookie}.
 * 
 *  @version $Id$
 *  @author Howard Lewis Ship
 * 
 **/

public class ApplicationServlet extends HttpServlet
{
    private static final Log LOG = LogFactory.getLog(ApplicationServlet.class);

    /** @since 2.3 **/

    private static final String APP_SPEC_PATH_PARAM =
        "org.apache.tapestry.application-specification";

    /**
     *  Name of the cookie written to the client web browser to
     *  identify the locale.
     *
     **/

    private static final String LOCALE_COOKIE_NAME = "org.apache.tapestry.locale";

    /**
     *  A {@link Pool} used to store {@link IEngine engine}s that are not currently
     *  in use.  The key is on {@link Locale}.
     *
     **/

    private Pool _enginePool = new Pool();

    /**
     *  The application specification, which is read once and kept in memory
     *  thereafter.
     *
     **/

    private IApplicationSpecification _specification;

    /**
     * The name under which the {@link IEngine engine} is stored within the
     * {@link HttpSession}.
     *
     **/

    private String _attributeName;

    /**
     *  The resolved class name used to instantiate the engine.
     * 
     *  @since 3.0
     * 
     **/

    private String _engineClassName;

    /**
     *  Used to search for configuration properties.
     * 
     *  
     *  @since 3.0
     * 
     **/

    private IPropertySource _propertySource;

    /**
     *  Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     *
     *  @since 1.0.6
     *
     **/

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doService(request, response);
    }

    /**
     *  @since 2.3
     * 
     **/

    private ClassResolver _resolver;

    /**
     * Handles the GET and POST requests. Performs the following:
     * <ul>
     * <li>Construct a {@link RequestContext}
     * <li>Invoke {@link #getEngine(RequestContext)} to get or create the {@link IEngine}
     * <li>Invoke {@link IEngine#service(RequestContext)} on the application
     * </ul>
     **/

    protected void doService(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        RequestContext context = null;

        try
        {

            // Create a context from the various bits and pieces.

            context = createRequestContext(request, response);

            // The subclass provides the engine.

            IEngine engine = getEngine(context);

            if (engine == null)
                throw new ServletException(
                    Tapestry.getMessage("ApplicationServlet.could-not-locate-engine"));

            boolean dirty = engine.service(context);

            HttpSession session = context.getSession();

            // When there's an active session, we *may* store it into
            // the HttpSession and we *will not* store the engine
            // back into the engine pool.

            if (session != null)
            {
                // If the service may have changed the engine and the
                // special storeEngine flag is on, then re-save the engine
                // into the session.  Otherwise, we only save the engine
                // into the session when the session is first created (is new).

                try
                {

                    boolean forceStore =
                        engine.isStateful() && (session.getAttribute(_attributeName) == null);

                    if (forceStore || dirty)
                    {
                        if (LOG.isDebugEnabled())
                            LOG.debug("Storing " + engine + " into session as " + _attributeName);

                        session.setAttribute(_attributeName, engine);
                    }
                }
                catch (IllegalStateException ex)
                {
                    // Ignore because the session been's invalidated.
                    // Allow the engine (which has state particular to the client)
                    // to be reclaimed by the garbage collector.

                    if (LOG.isDebugEnabled())
                        LOG.debug("Session invalidated.");
                }

                // The engine is stateful and stored in a session.  Even if it started
                // the request cycle in the pool, it doesn't go back.

                return;
            }

            if (engine.isStateful())
            {
                LOG.error(
                    Tapestry.format(
                        "ApplicationServlet.engine-stateful-without-session",
                        engine));
                return;
            }

            // No session; the engine contains no state particular to
            // the client (except for locale).  Don't throw it away,
            // instead save it in a pool for later reuse (by this, or another
            // client in the same locale).

            if (LOG.isDebugEnabled())
                LOG.debug("Returning " + engine + " to pool.");

            _enginePool.store(engine.getLocale(), engine);

        }
        catch (ServletException ex)
        {
            log("ServletException", ex);

            show(ex);

            // Rethrow it.

            throw ex;
        }
        catch (IOException ex)
        {
            log("IOException", ex);

            show(ex);

            // Rethrow it.

            throw ex;
        }
        finally
        {
            if (context != null)
                context.cleanup();
        }

    }

    /**
     *  Invoked by {@link #doService(HttpServletRequest, HttpServletResponse)} to create
     *  the {@link RequestContext} for this request cycle.  Some applications may need to
     *  replace the default RequestContext with a subclass for particular behavior.
     * 
     *  @since 2.3
     * 
     **/

    protected RequestContext createRequestContext(
        HttpServletRequest request,
        HttpServletResponse response)
        throws IOException
    {
        return new RequestContext(this, request, response);
    }

    protected void show(Exception ex)
    {
        System.err.println("\n\n**********************************************************\n\n");

        new ExceptionAnalyzer().reportException(ex, System.err);

        System.err.println("\n**********************************************************\n");

    }

    /**
     *  Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     *
     *
     **/

    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doService(request, response);
    }

    /**
     *  Returns the application specification, which is read
     *  by the {@link #init(ServletConfig)} method.
     *
     **/

    public IApplicationSpecification getApplicationSpecification()
    {
        return _specification;
    }

    /**
     *  Retrieves the {@link IEngine engine} that will process this
     *  request.  This comes from one of the following places:
     *  <ul>
     *  <li>The {@link HttpSession}, if the there is one.
     *  <li>From the pool of available engines
     *  <li>Freshly created
     *  </ul>
     *
     **/

    protected IEngine getEngine(RequestContext context) throws ServletException
    {
        IEngine engine = null;
        HttpSession session = context.getSession();

        // If there's a session, then find the engine within it.

        if (session != null)
        {
            engine = (IEngine) session.getAttribute(_attributeName);
            if (engine != null)
            {
                if (LOG.isDebugEnabled())
                    LOG.debug("Retrieved " + engine + " from session " + session.getId() + ".");

                return engine;
            }

            if (LOG.isDebugEnabled())
                LOG.debug("Session exists, but doesn't contain an engine.");
        }

        Locale locale = getLocaleFromRequest(context);

        engine = (IEngine) _enginePool.retrieve(locale);

        if (engine == null)
        {
            engine = createEngine(context);
            engine.setLocale(locale);
        }
        else
        {
            if (LOG.isDebugEnabled())
                LOG.debug("Using pooled engine " + engine + " (from locale " + locale + ").");
        }

        return engine;
    }

    /**
     *  Determines the {@link Locale} for the incoming request.
     *  This is determined from the locale cookie or, if not set,
     *  from the request itself.  This may return null
     *  if no locale is determined.
     *
     **/

    protected Locale getLocaleFromRequest(RequestContext context) throws ServletException
    {
        Cookie cookie = context.getCookie(LOCALE_COOKIE_NAME);

        if (cookie != null)
            return Tapestry.getLocale(cookie.getValue());

        return context.getRequest().getLocale();
    }

    /**
     *  Reads the application specification when the servlet is
     *  first initialized.  All {@link IEngine engine instances}
     *  will have access to the specification via the servlet.
     * 
     *  @see #getApplicationSpecification()
     *  @see #constructApplicationSpecification()
     *  @see #createResourceResolver()
     *
     **/

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        _resolver = createClassResolver();

        _specification = constructApplicationSpecification();

        _attributeName = "org.apache.tapestry.engine:" + config.getServletName();
    }

    /**
     *  Invoked from {@link #init(ServletConfig)} to create a resource resolver
     *  for the servlet (which will utlimately be shared and used through the
     *  application).
     * 
     *  <p>This implementation constructs a {@link DefaultResourceResolver}, subclasses
     *  may provide a different implementation.
     * 
     *  @see #getResourceResolver()
     *  @since 2.3
     * 
     **/

    protected ClassResolver createClassResolver() throws ServletException
    {
        return new DefaultClassResolver();
    }

    /**
     *  Invoked from {@link #init(ServletConfig)} to read and construct
     *  the {@link ApplicationSpecification} for this servlet.
     *  Invokes {@link #getApplicationSpecificationPath()}, opens
     *  the resource as a stream, then invokes
     *  {@link #parseApplicationSpecification(IResourceLocation)}.
     * 
     *  <p>
     *  This method exists to be overriden in
     *  applications where the application specification cannot be
     *  loaded from the classpath.  Alternately, a subclass
     *  could override this method, invoke this implementation,
     *  and then add additional data to it (for example, an application
     *  where some of the pages are defined in an external source
     *  such as a database).
     *  
     *  @since 2.2
     * 
     **/

    protected IApplicationSpecification constructApplicationSpecification() throws ServletException
    {
        Resource specLocation = getApplicationSpecificationLocation();

        if (specLocation == null)
        {
            if (LOG.isDebugEnabled())
                LOG.debug(Tapestry.getMessage("ApplicationServlet.no-application-specification"));

            return constructStandinSpecification();
        }

        if (LOG.isDebugEnabled())
            LOG.debug("Loading application specification from " + specLocation);

        return parseApplicationSpecification(specLocation);
    }

    /**
     *  Gets the location of the application specification, if there is one.
     *  
     *  <ul>
     *  <li>Invokes {@link #getApplicationSpecificationPath()} to get the
     *  location of the application specification on the classpath.
     *  <li>If that return null, search for the application specification:
     *  <ul>
     *  <li><i>name</i>.application in /WEB-INF/<i>name</i>/
     *  <li><i>name</i>.application in /WEB-INF/
     *  </ul>
     *  </ul>
     * 
     *  <p>Returns the location of the application specification, or null
     *  if not found.
     * 
     *  @since 3.0
     * 
     **/

    protected Resource getApplicationSpecificationLocation() throws ServletException
    {
        String path = getApplicationSpecificationPath();

        if (path != null)
            return new ClasspathResource(_resolver, path);

        ServletContext context = getServletContext();
        String servletName = getServletName();
        String expectedName = servletName + ".application";

        Resource webInfLocation = new ContextResourceLocation(context, "/WEB-INF/");
        Resource webInfAppLocation = webInfLocation.getRelativeResource(servletName + "/");

        Resource result = check(webInfAppLocation, expectedName);
        if (result != null)
            return result;

        return check(webInfLocation, expectedName);
    }

    /**
     *  Checks for the application specification relative to the specified
     *  location.
     * 
     *  @since 3.0
     * 
     **/

    private Resource check(Resource location, String name)
    {
        Resource result = location.getRelativeResource(name);

        if (LOG.isDebugEnabled())
            LOG.debug("Checking for existence of " + result);

        if (result.getResourceURL() != null)
        {
            LOG.debug("Found.");
            return result;
        }

        return null;
    }

    /**
     *  Invoked from {@link #constructApplicationSpecification()} when
     *  the application doesn't have an explicit specification.  A
     *  simple specification is constructed and returned.  This is useful
     *  for minimal applications and prototypes.
     * 
     *  @since 3.0
     * 
     **/

    protected IApplicationSpecification constructStandinSpecification()
    {
        ApplicationSpecification result = new ApplicationSpecification();

        Resource virtualLocation =
            new ContextResourceLocation(getServletContext(), "/WEB-INF/");

        result.setSpecificationLocation(virtualLocation);

        result.setName(getServletName());
        result.setResourceResolver(_resolver);

        return result;
    }

    /**
     *  Invoked from {@link #constructApplicationSpecification()} to
     *  actually parse the stream (with content provided from the path)
     *  and convert it into an {@link ApplicationSpecification}.
     * 
     *  @since 2.2
     * 
     **/

    protected IApplicationSpecification parseApplicationSpecification(Resource location)
        throws ServletException
    {
        try
        {
            SpecificationParser parser = new SpecificationParser(_resolver);

            return parser.parseApplicationSpecification(location);
        }
        catch (DocumentParseException ex)
        {
            show(ex);

            throw new ServletException(
                Tapestry.format("ApplicationServlet.could-not-parse-spec", location),
                ex);
        }
    }

    /**
     *  Closes the stream, ignoring any exceptions.
     * 
     **/

    protected void close(InputStream stream)
    {
        try
        {
            if (stream != null)
                stream.close();
        }
        catch (IOException ex)
        {
            // Ignore it.
        }
    }

    /**
     *  Reads the servlet init parameter
     *  <code>org.apache.tapestry.application-specification</code>, which
     *  is the location, on the classpath, of the application specification.
     *
     *  <p>
     *  If the parameter is not set, this method returns null, and a search
     *  for the application specification within the servlet context
     *  will begin.
     * 
     *  @see #getApplicationSpecificationLocation()
     * 
     **/

    protected String getApplicationSpecificationPath() throws ServletException
    {
        return getInitParameter(APP_SPEC_PATH_PARAM);
    }

    /**
     *  Invoked by {@link #getEngine(RequestContext)} to create
     *  the {@link IEngine} instance specific to the
     *  application, if not already in the
     *  {@link HttpSession}.
     *
     *  <p>The {@link IEngine} instance returned is stored into the
     *  {@link HttpSession}.
     *
     *  @see #getEngineClassName()    
     *
     **/

    protected IEngine createEngine(RequestContext context) throws ServletException
    {
        try
        {
            String className = getEngineClassName();

            if (LOG.isDebugEnabled())
                LOG.debug("Creating engine from class " + className);

            Class engineClass = getClassResolver().findClass(className);

            IEngine result = (IEngine) engineClass.newInstance();

            if (LOG.isDebugEnabled())
                LOG.debug("Created engine " + result);

            return result;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
    }

    /**
     * 
     *  Returns the name of the class to use when instantiating
     *  an engine instance for this application.  
     *  If the application specification
     *  provides a value, this is returned.  Otherwise, a search for
     *  the configuration property 
     *  <code>org.apache.tapestry.engine-class</code>
     *  occurs (see {@link #searchConfiguration(String)}).
     * 
     *  <p>If the search is still unsuccessful, then
     *  {@link org.apache.tapestry.engine.BaseEngine} is used.
     * 
     **/

    protected String getEngineClassName()
    {
        if (_engineClassName != null)
            return _engineClassName;

        _engineClassName = _specification.getEngineClassName();

        if (_engineClassName == null)
            _engineClassName = searchConfiguration("org.apache.tapestry.engine-class");

        if (_engineClassName == null)
            _engineClassName = BaseEngine.class.getName();

        return _engineClassName;
    }

    /**
     *  Searches for a configuration property in:
     *  <ul>
     *  <li>The servlet's initial parameters
     *  <li>The servlet context's initial parameters
     *  <li>JVM system properties
     *  </ul>
     * 
     *  @see #createPropertySource()
     *  @since 3.0
     * 
     **/

    protected String searchConfiguration(String propertyName)
    {
        if (_propertySource == null)
            _propertySource = createPropertySource();

        return _propertySource.getPropertyValue(propertyName);
    }

    /**
     *  Creates an instance of {@link IPropertySource} used for
     *  searching of configuration values.  Subclasses may override
     *  this method if they want to change the normal locations
     *  that properties are searched for within.
     * 
     *  @since 3.0
     * 
     **/

    protected IPropertySource createPropertySource()
    {
        DelegatingPropertySource result = new DelegatingPropertySource();

        result.addSource(new ServletPropertySource(getServletConfig()));
        result.addSource(new ServletContextPropertySource(getServletContext()));
        result.addSource(SystemPropertiesPropertySource.getInstance());

        return result;
    }

    /**
     *  Invoked from the {@link IEngine engine}, just prior to starting to
     *  render a response, when the locale has changed.  The servlet writes a
     *  {@link Cookie} so that, on subsequent request cycles, an engine localized
     *  to the selected locale is chosen.
     *
     *  <p>At this time, the cookie is <em>not</em> persistent.  That may
     *  change in subsequent releases.
     *
     *  @since 1.0.1
     **/

    public void writeLocaleCookie(Locale locale, IEngine engine, RequestContext cycle)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Writing locale cookie " + locale);

        Cookie cookie = new Cookie(LOCALE_COOKIE_NAME, locale.toString());
        cookie.setPath(engine.getServletPath());

        cycle.addCookie(cookie);
    }

    /**
     *  Returns a resource resolver that can access classes and resources related
     *  to the current web application context.  Relies on
     *  {@link java.lang.Thread#getContextClassLoader()}, which is set by
     *  most modern servlet containers.
     * 
     *  @since 2.3
     *
     **/

    public ClassResolver getClassResolver()
    {
        return _resolver;
    }

}