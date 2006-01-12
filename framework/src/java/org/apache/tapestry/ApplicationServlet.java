// Copyright 2004, 2005, 2006 The Apache Software Foundation
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
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.Registry;
import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.DefaultClassResolver;
import org.apache.hivemind.impl.RegistryBuilder;
import org.apache.hivemind.impl.StrictErrorHandler;
import org.apache.hivemind.impl.XmlModuleDescriptorProvider;
import org.apache.hivemind.util.ContextResource;
import org.apache.tapestry.services.ApplicationInitializer;
import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.util.exception.ExceptionAnalyzer;

/**
 * Links a servlet container with a Tapestry application. The servlet init
 * parameter <code>org.apache.tapestry.application-specification</code> should
 * be set to the complete resource path (within the classpath) to the
 * application specification, i.e., <code>/com/foo/bar/MyApp.application</code>.
 * As of release 4.0, this servlet will also create a HiveMind Registry and
 * manage it.
 * 
 * @author Howard Lewis Ship
 * @see org.apache.tapestry.services.ApplicationInitializer
 * @see org.apache.tapestry.services.ServletRequestServicer
 */

public class ApplicationServlet extends HttpServlet
{

    private static final Log LOG = LogFactory.getLog(ApplicationServlet.class);

    /**
     * Prefix used to store the HiveMind Registry into the ServletContext. This
     * string is suffixed with the servlet name (in case multiple Tapestry
     * applications are executing within a single web application).
     * 
     * @since 4.0
     */

    private static final String REGISTRY_KEY_PREFIX = "org.apache.tapestry.Registry:";

    private static final long serialVersionUID = -8046042689991538059L;

    /**
     * @since 4.0
     */

    private Registry _registry;

    /**
     * The key used to store the registry into the ServletContext.
     * 
     * @since 4.0
     */

    private String _registryKey;

    /**
     * @since 4.0
     */
    private ServletRequestServicer _requestServicer;

    /**
     * @since 2.3
     */

    private ClassResolver _resolver;

    /**
     * Looks for a file in the servlet context; if it exists, it is expected to
     * be a HiveMind module descriptor, and is added to the builder.
     * 
     * @since 4.0
     */

    protected void addModuleIfExists(RegistryBuilder builder, ServletContext context, String path)
    {
        Resource r = new ContextResource(context, path);

        if (r.getResourceURL() == null) return;

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(_resolver, r));
    }

    /**
     * Invoked by {@link #constructRegistry(ServletConfig)} to create and return
     * an {@link ErrorHandler} instance to be used when constructing the
     * Registry (and then to handle any runtime exceptions). This implementation
     * returns a new instance of
     * {@link org.apache.hivemind.impl.StrictErrorHandler}.
     * 
     * @since 4.0
     */
    protected ErrorHandler constructErrorHandler(ServletConfig config)
    {
        return new StrictErrorHandler();
    }

    /**
     * Invoked from {@link #init(ServletConfig)}to construct the Registry to be
     * used by the application.
     * <p>
     * This looks in the standard places (on the classpath), but also in the
     * WEB-INF/name and WEB-INF folders (where name is the name of the servlet).
     * 
     * @since 4.0
     */
    protected Registry constructRegistry(ServletConfig config)
    {
        ErrorHandler errorHandler = constructErrorHandler(config);

        RegistryBuilder builder = new RegistryBuilder(errorHandler);

        builder.addModuleDescriptorProvider(new XmlModuleDescriptorProvider(_resolver));

        String name = config.getServletName();
        ServletContext context = config.getServletContext();

        addModuleIfExists(builder, context, "/WEB-INF/" + name + "/hivemodule.xml");
        addModuleIfExists(builder, context, "/WEB-INF/hivemodule.xml");

        return builder.constructRegistry(Locale.getDefault());
    }

    /**
     * Invoked from {@link #init(ServletConfig)}to create a resource resolver
     * for the servlet (which will utlimately be shared and used through the
     * application).
     * <p>
     * This implementation constructs a {@link DefaultResourceResolver},
     * subclasses may provide a different implementation.
     * 
     * @see #getResourceResolver()
     * @since 2.3
     */

    protected ClassResolver createClassResolver()
    {
        return new DefaultClassResolver();
    }

    /**
     * Shuts down the registry (if it exists).
     * 
     * @since 4.0
     */
    public void destroy()
    {
        getServletContext().removeAttribute(_registryKey);

        if (_registry != null)
        {
            _registry.shutdown();
            _registry = null;
        }
    }

    /**
     * Invokes {@link #doService(HttpServletRequest, HttpServletResponse)}.
     * 
     * @since 1.0.6
     */

    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        doService(request, response);
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
     * Handles the GET and POST requests. Performs the following:
     * <ul>
     * <li>Construct a {@link RequestContext}
     * <li>Invoke {@link #getEngine(RequestContext)}to get or create the
     * {@link IEngine}
     * <li>Invoke {@link IEngine#service(RequestContext)}on the application
     * </ul>
     */

    protected void doService(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException
    {
        try
        {
            _registry.setupThread();

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

    /**
     * Reads the application specification when the servlet is first
     * initialized. All {@link IEngine engine instances}will have access to the
     * specification via the servlet.
     * 
     * @see #constructApplicationSpecification()
     * @see #createResourceResolver()
     */

    public void init(ServletConfig config)
        throws ServletException
    {
        String name = config.getServletName();

        _registryKey = REGISTRY_KEY_PREFIX + name;

        long startTime = System.currentTimeMillis();
        long elapsedToRegistry = 0;

        super.init(config);

        _resolver = createClassResolver();

        try
        {
            _registry = constructRegistry(config);

            elapsedToRegistry = System.currentTimeMillis() - startTime;

            initializeApplication();

            config.getServletContext().setAttribute(_registryKey, _registry);
        }
        catch (Exception ex)
        {
            show(ex);

            throw new ServletException(TapestryMessages.servletInitFailure(ex), ex);
        }

        long elapsedOverall = System.currentTimeMillis() - startTime;

        LOG.info(TapestryMessages.servletInit(name, elapsedToRegistry, elapsedOverall));
    }

    /**
     * Invoked from {@link #init(ServletConfig)}, after the registry has been
     * constructed, to bootstrap the application via the
     * <code>tapestry.MasterApplicationInitializer</code> service.
     * 
     * @since 4.0
     */
    protected void initializeApplication()
    {
        ApplicationInitializer ai = (ApplicationInitializer)_registry.getService("tapestry.init.MasterInitializer",
                ApplicationInitializer.class);

        ai.initialize(this);

        _registry.cleanupThread();

        _requestServicer = (ServletRequestServicer)_registry.getService("tapestry.request.ServletRequestServicer",
                ServletRequestServicer.class);
    }

    protected void show(Exception ex)
    {
        System.err.println("\n\n**********************************************************\n\n");

        new ExceptionAnalyzer().reportException(ex, System.err);

        System.err.println("\n**********************************************************\n");

    }
}
