// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.request.RequestContext;
import org.apache.tapestry.services.ApplicationGlobals;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.spec.ApplicationSpecification;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;

import com.sun.jndi.ldap.pool.Pool;

/**
 * Links a servlet container with a Tapestry application. The servlet has some responsibilities
 * related to bootstrapping the application (in terms of logging, reading the
 * {@link ApplicationSpecification specification}, etc.). It is also responsible for creating or
 * locating the {@link IEngine}and delegating incoming requests to it.
 * <p>
 * The servlet init parameter <code>org.apache.tapestry.specification-path</code> should be set to
 * the complete resource path (within the classpath) to the application specification, i.e.,
 * <code>/com/foo/bar/MyApp.application</code>.
 * <p>
 * In some servlet containers (notably <a href="www.bea.com"/>WebLogic </a>) it is necessary to
 * invoke {@link HttpSession#setAttribute(String,Object)}in order to force a persistent value to be
 * replicated to the other servers in the cluster. Tapestry applications usually only have a single
 * persistent value, the {@link IEngine engine}. For persistence to work in such an environment,
 * the JVM system property <code>org.apache.tapestry.store-engine</code> must be set to
 * <code>true</code>. This will force the application servlet to restore the engine into the
 * {@link HttpSession}at the end of each request cycle.
 * <p>
 * As of release 1.0.1, it is no longer necessary for a {@link HttpSession}to be created on the
 * first request cycle. Instead, the HttpSession is created as needed by the {@link IEngine}...
 * that is, when a visit object is created, or when persistent page state is required. Otherwise,
 * for sessionless requests, an {@link IEngine}from a {@link Pool}is used. Additional work must be
 * done so that the {@link IEngine}can change locale <em>without</em> forcing the creation of a
 * session; this involves the servlet and the engine storing locale information in a {@link Cookie}.
 * <p>
 * As of release 3.1, this servlet will also create a HiveMind Registry and manage it.
 * 
 * @author Howard Lewis Ship
 */

public class ApplicationServlet extends HttpServlet
{
    /**
     * Name of the cookie written to the client web browser to identify the locale.
     */

    public static final String LOCALE_COOKIE_NAME = "org.apache.tapestry.locale";

    /**
     * The application specification, which is read once and kept in memory thereafter.
     */

    private IApplicationSpecification _specification;

    /**
     * Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     * 
     * @since 1.0.6
     */

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException,
            ServletException
    {
        doService(request, response);
    }

    /**
     * @since 2.3
     */

    private ClassResolver _resolver;

    /**
     * @since 3.1
     */

    private Registry _registry;

    /**
     * @since 3.1
     */
    private RequestServicer _requestServicer;

    /**
     * Handles the GET and POST requests. Performs the following:
     * <ul>
     * <li>Construct a {@link RequestContext}
     * <li>Invoke {@link #getEngine(RequestContext)}to get or create the {@link IEngine}
     * <li>Invoke {@link IEngine#service(RequestContext)}on the application
     * </ul>
     */

    protected void doService(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        try
        {
            _requestServicer.service(request, response);
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
            _registry.cleanupThread();
        }
    }

    protected void show(Exception ex)
    {
        System.err.println("\n\n**********************************************************\n\n");

        new ExceptionAnalyzer().reportException(ex, System.err);

        System.err.println("\n**********************************************************\n");

    }

    /**
     * Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     */

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        doService(request, response);
    }

    /**
     * Returns the application specification, which is read by the {@link #init(ServletConfig)}
     * method.
     * 
     * @deprecated Use {@link RequestContext#getApplicationSpecification()}instead.
     */

    public IApplicationSpecification getApplicationSpecification()
    {
        return _specification;
    }

    /**
     * Reads the application specification when the servlet is first initialized. All
     * {@link IEngine engine instances}will have access to the specification via the servlet.
     * 
     * @see #getApplicationSpecification()
     * @see #constructApplicationSpecification()
     * @see #createResourceResolver()
     */

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        _resolver = createClassResolver();

        try
        {

            _registry = constructRegistry(config);

            initializeApplication();
        }
        catch (Exception ex)
        {
            show(ex);

            throw new ServletException(TapestryMessages.servletInitFailure(ex), ex);
        }
    }

    /**
     * Invoked from {@link #init(ServletConfig)}to create a resource resolver for the servlet
     * (which will utlimately be shared and used through the application).
     * <p>
     * This implementation constructs a {@link DefaultResourceResolver}, subclasses may provide a
     * different implementation.
     * 
     * @see #getResourceResolver()
     * @since 2.3
     */

    protected ClassResolver createClassResolver() throws ServletException
    {
        return new DefaultClassResolver();
    }

    /**
     * Closes the stream, ignoring any exceptions.
     */

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
     * Returns a class resolver that can access classes and resources related to the current web
     * application context. Relies on {@link java.lang.Thread#getContextClassLoader()}, which is
     * set by most modern servlet containers.
     * 
     * @since 2.3
     */

    public ClassResolver getClassResolver()
    {
        return _resolver;
    }

    /**
     * Invoked from {@link #init(ServletConfig)}to construct the Registry to be used by the
     * application.
     * <p>
     * This looks in the standard places (on the classpath), but also in the WEB-INF/name and
     * WEB-INF folders (where name is the name of the servlet).
     * 
     * @since 3.1
     */
    protected Registry constructRegistry(ServletConfig config)
    {
        RegistryBuilder builder = new RegistryBuilder();

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(_resolver));

        String name = config.getServletName();
        ServletContext context = config.getServletContext();

        addModuleIfExists(builder, context, "/WEB-INF/" + name + "/hivemodule.xml");
        addModuleIfExists(builder, context, "/WEB-INF/hivemodule.xml");

        return builder.constructRegistry(Locale.getDefault());
    }

    /**
     * Looks for a file in the servlet context; if it exists, it is expected to be a HiveMind module
     * descriptor, and is added to the builder.
     * 
     * @since 3.1
     */

    protected void addModuleIfExists(RegistryBuilder builder, ServletContext context, String path)
    {
        Resource r = new ContextResource(context, path);

        if (r.getResourceURL() == null)
            return;

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(_resolver, r));
    }

    /**
     * Invoked from {@link #init(ServletConfig)}, after the registry has been constructed, to
     * bootstrap the application via the <code>tapestry.MasterApplicationInitializer</code>
     * service.
     * 
     * @since 3.1
     */
    protected void initializeApplication()
    {
        ApplicationInitializer ai = (ApplicationInitializer) _registry.getService(
                "tapestry.init.MasterInitializer",
                ApplicationInitializer.class);

        ai.initialize(this);

        _registry.cleanupThread();

        // This is temporary, since most of the code still gets the
        // specification from the servlet --- in fact, has to downcase
        // RequestContext.getServlet() to do so.

        ApplicationGlobals ag = (ApplicationGlobals) _registry.getService(
                "tapestry.globals.ApplicationGlobals",
                ApplicationGlobals.class);

        _specification = ag.getSpecification();

        _requestServicer = (RequestServicer) _registry.getService(
                "tapestry.request.RequestServicerPipeline",
                RequestServicer.class);
    }

    /**
     * Returns the Registry used by the application.
     * 
     * @since 3.1
     */
    public Registry getRegistry()
    {
        return _registry;
    }

    /**
     * Shuts down the registry (if it exists).
     * 
     * @since 3.1
     */
    public void destroy()
    {
        if (_registry != null)
        {
            _registry.shutdown();
            _registry = null;
        }
    }

}