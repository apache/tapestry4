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

import org.apache.log4j.Category;
import org.apache.log4j.Priority;

import net.sf.tapestry.engine.ResourceResolver;
import net.sf.tapestry.parse.SpecificationParser;
import net.sf.tapestry.spec.ApplicationSpecification;
import net.sf.tapestry.util.StringSplitter;
import net.sf.tapestry.util.exception.ExceptionAnalyzer;
import net.sf.tapestry.util.pool.Pool;
import net.sf.tapestry.util.xml.DocumentParseException;

/**
 * Links a servlet container with a Tapestry application.  The servlet has some
 * responsibilities related to bootstrapping the application (in terms of
 * logging, reading the {@link ApplicationSpecification specification}, etc.).
 * It is also responsible for creating or locating the {@link IEngine} and delegating
 * incoming requests to it.
 *
 * <p>In some servlet containers (notably
 * <a href="www.bea.com"/>WebLogic</a>)
 * it is necessary to invoke {@link HttpSession#setAttribute(String,Object)}
 * in order to force a persistent value to be replicated to the other
 * servers in the cluster.  Tapestry applications usually only have a single
 * persistent value, the {@link IEngine engine}.  For persistence to
 * work in such an environment, the
 * JVM system property <code>net.sf.tapestry.store-engine</code>
 * must be set to <code>true</code>.  This will force the application
 * servlet to restore the engine into the {@link HttpSession} at the
 * end of each request cycle.
 *
 * <p>The application servlet also has a default implementation of
 * {@link #setupLogging} that configures logging for 
 *  <a href="http://jakarta.apache.org/log4j">Log4J</a>.  Subclasses
 * with more sophisticated logging needs will need to overide this
 * method.
 *
 * <p>As of release 1.0.1, it is no longer necessary for a {@link HttpSession}
 * to be created on the first request cycle.  Instead, the HttpSession is created
 * as needed by the {@link IEngine} ... that is, when a visit object is created,
 * or when persistent page state is required.  Otherwise, for sessionless requests,
 * an {@link IEngine} from a {@link Pool} is used.  Additional work must be done
 * so that the {@link IEngine} can change locale <em>without</em> forcing 
 * the creation of a session; this involves the servlet and the engine storing
 * locale information in a {@link Cookie}.
 *
 * <p>This class is derived from the original class
 * <code>com.primix.servlet.GatewayServlet</code>,
 * part of the <b>ServletUtils</b> framework available from
 * <a href="http://www.gjt.org/servlets/JCVSlet/list/gjt/com/primix/servlet">The Giant
 * Java Tree</a>.
 *
 * @version $Id$
 * @author Howard Lewis Ship
 **/

abstract public class ApplicationServlet extends HttpServlet
{
    private static final Category CAT = Category.getInstance(ApplicationServlet.class);

    /**
     *  Name of the cookie written to the client web browser to
     *  identify the locale.
     *
     **/

    private static final String LOCALE_COOKIE_NAME = "net.sf.tapestry.locale";

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

    private ApplicationSpecification _specification;

    /**
     * The name under which the {@link IEngine engine} is stored within the
     * {@link HttpSession}.
     *
     **/

    private String _attributeName;

    /**
     *  Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     *
     *  @since 1.0.6
     *
     **/

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doService(request, response);
    }

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
        IEngine engine;

        try
        {

            // Create a context from the various bits and pieces.

            context = new RequestContext(this, request, response);

            // The subclass provides the engine.

            engine = getEngine(context);

            if (engine == null)
                throw new ServletException(Tapestry.getString("ApplicationServlet.could-not-locate-engine"));

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

                    boolean forceStore = engine.isStateful() && (session.getAttribute(_attributeName) == null);

                    if (forceStore || dirty)
                    {
                        if (CAT.isDebugEnabled())
                            CAT.debug("Storing " + engine + " into session as " + _attributeName);

                        session.setAttribute(_attributeName, engine);
                    }
                }
                catch (IllegalStateException ex)
                {
                    // Ignore because the session been's invalidated.
                    // Allow the engine (which has state particular to the client)
                    // to be reclaimed by the garbage collector.

                    if (CAT.isDebugEnabled())
                        CAT.debug("Session invalidated.");
                }

                return;
            }

            if (engine.isStateful())
            {
                CAT.error("Engine " + engine + " is stateful even though there is no session.  Discarding the engine.");
                return;
            }

            // No session; the engine contains no state particular to
            // the client (except for locale).  Don't throw it away,
            // instead save it in a pool for later reuse (by this, or another
            // client in the same locale).

            if (CAT.isDebugEnabled())
                CAT.debug("Returning " + engine + " to pool.");

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

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        doService(request, response);
    }

    /**
     *  Returns the application specification, which is read
     *  by the {@link #init(ServletConfig)} method.
     *
     **/

    public ApplicationSpecification getApplicationSpecification()
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
                if (CAT.isDebugEnabled())
                    CAT.debug("Retrieved " + engine + " from session " + session.getId() + ".");

                return engine;
            }

            if (CAT.isDebugEnabled())
                CAT.debug("Session exists, but doesn't contain an engine.");
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
            if (CAT.isDebugEnabled())
                CAT.debug("Using pooled engine " + engine + " (from locale " + locale + ").");
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
     **/

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        setupLogging();

        _specification = constructApplicationSpecification();

        _attributeName = "net.sf.tapestry.engine." + config.getServletName();
    }

    /**
     *  Invoked from {@link #init(ServletConfig)} to read and construct
     *  the {@link ApplicationSpecification} for this servlet.
     *  Invokes {@link #getApplicationSpecificationPath()}, opens
     *  the resource as a stream, then invokes
     *  {@link #parseApplicationSpecification(InputStream, String)}.
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

    protected ApplicationSpecification constructApplicationSpecification() throws ServletException
    {
        String path = getApplicationSpecificationPath();

        // Make sure we locate the specification using our
        // own class loader.

        InputStream stream = getClass().getResourceAsStream(path);

        if (stream == null)
            throw new ServletException(Tapestry.getString("ApplicationServlet.could-not-load-spec", path));

        try
        {
            if (CAT.isDebugEnabled())
                CAT.debug("Loading application specification from " + path);

            return parseApplicationSpecification(stream, path);
        }
        finally
        {
            close(stream);
        }
    }

    /**
     *  Invoked from {@link #constructApplicationSpecification()} to
     *  actually parse the stream (with content provided from the path)
     *  and convert it into an {@link ApplicationSpecification}.
     * 
     *  @since 2.2
     * 
     **/

    protected ApplicationSpecification parseApplicationSpecification(InputStream stream, String path)
        throws ServletException
    {
        try
        {
            SpecificationParser parser = new SpecificationParser();

            return parser.parseApplicationSpecification(stream, path,  new ResourceResolver(this));
        }
        catch (DocumentParseException ex)
        {
            show(ex);

            throw new ServletException(Tapestry.getString("ApplicationServlet.could-not-parse-spec", path), ex);
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
            stream.close();
        }
        catch (IOException ex)
        {
            // Ignore it.
        }
    }

    /**
     *  Invoked from {@link #init(ServletConfig)} before the specification is loaded to
     *  setup log4j logging.  This implemention is sufficient for testing, but should
     *  be overiden in production applications.
     *
     *  <p>Gets the JVM system property <code>net.sf.tapestry.root-logging-priority</code>,
     *  and (if non-null), converts it to a {@link Priority} and assigns it to the root
     *  {@link Category}.
     *
     *  <p>For each priority, a check is made for a JVM system property
     *  <code>net.sf.tapestry.log4j.<em>priority</em></code> (i.e. <code>...log4j.DEBUG</code>).
     *  The value is a list of categories seperated by semicolons.  Each of these
     *  categories will be assigned that priority.
     * 
     *  <p>Prior to Tapestry release 2.0.3, this method would also set the pattern
     *  for root category appender.  That is better done using a <code>log4j.properties</code>
     *  file.  This method exists to make it easy to augment or override that
     *  configuration using command line options.
     *
     *  @since 0.2.9
     **/

    protected void setupLogging() throws ServletException
    {
        Priority priority = Priority.ERROR;

        String value = System.getProperty("net.sf.tapestry.root-logging-priority");

        if (value != null)
        {
            priority = Priority.toPriority(value, Priority.ERROR);

            Category root = Category.getRoot();
            root.setPriority(priority);
        }

        Priority[] priorities = Priority.getAllPossiblePriorities();
        StringSplitter splitter = new StringSplitter(';');

        for (int i = 0; i < priorities.length; i++)
        {
            priority = priorities[i];
            String key = "net.sf.tapestry.log4j." + priority.toString();
            String categoryList = System.getProperty(key);

            if (categoryList != null)
            {
                String[] categories = splitter.splitToArray(categoryList);

                for (int j = 0; j < categories.length; j++)
                {
                    Category cat = Category.getInstance(categories[j]);
                    cat.setPriority(priority);
                }
            }
        }

    }

    /**
     *  Overridden in subclasses to provide a resource path
     *  to the application specification.  This implementation
     *  simply throws {@link ServletException}.
     *
     **/

    protected String getApplicationSpecificationPath() throws ServletException
    {
        throw new ServletException(
            Tapestry.getString("ApplicationServlet.get-app-path-not-overriden", getClass().getName()));
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
     *  <p>This implementation instantiates a new engine as specified
     *  by {@link ApplicationSpecification#getEngineClassName()}.
     *
     **/

    protected IEngine createEngine(RequestContext context) throws ServletException
    {
        ClassLoader classLoader = getClass().getClassLoader();

        try
        {
            String className = _specification.getEngineClassName();

            if (className == null)
                throw new ServletException(Tapestry.getString("ApplicationServlet.no-engine-class"));

            if (CAT.isDebugEnabled())
                CAT.debug("Creating engine from class " + className);

            Class engineClass = Class.forName(className, true, classLoader);

            IEngine result = (IEngine) engineClass.newInstance();

            if (CAT.isDebugEnabled())
                CAT.debug("Created engine " + result);

            return result;
        }
        catch (Exception ex)
        {
            throw new ServletException(ex);
        }
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
        if (CAT.isDebugEnabled())
            CAT.debug("Writing locale cookie " + locale);

        Cookie cookie = new Cookie(LOCALE_COOKIE_NAME, locale.toString());
        cookie.setPath(engine.getServletPath());

        cycle.addCookie(cookie);
    }
}