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

package org.apache.tapestry;

import java.util.Locale;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.services.Infrastructure;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.spec.IApplicationSpecification;

/**
 * Defines the core, session-persistant object used to run a Tapestry application for a single
 * client (each client will have its own instance of the engine).
 * <p>
 * The engine exists to provide core services to the pages and components that make up the
 * application. The engine is a delegate to the {@link ApplicationServlet}via the
 * {@link #service(RequestContext)}method.
 * <p>
 * Starting in release 3.1, the engine is kept around only for compatibility (with release 3.0).
 * It's functions have been moved over into a collection of HiveMind services (or are in the process
 * of doing so).
 * 
 * @author Howard Lewis Ship
 */

public interface IEngine extends WebRequestServicer
{
    /**
     * Returns the locale for the engine. This locale is used when selecting templates and assets.
     */

    public Locale getLocale();

    /**
     * Changes the engine's locale. Any subsequently loaded pages will be in the new locale (though
     * pages already loaded stay in the old locale). Generally, you should render a new page after
     * changing the locale, to show that the locale has changed.
     */

    public void setLocale(Locale value);

    /**
     * Gets the named service, or throws an {@link org.apache.tapestry.ApplicationRuntimeException}
     * if the engine can't provide the named service.
     * 
     * @deprecated To be removed in 3.2. Engine services can now be injected.
     */

    public IEngineService getService(String name);

    /**
     * Returns the application specification that defines the application and its pages.
     * 
     * @deprecated To be removed in 3.2. This value can be injected.
     */

    public IApplicationSpecification getSpecification();

    /**
     * Returns the source of all component specifications for the application. The source is shared
     * between sessions.
     * 
     * @see org.apache.tapestry.engine.AbstractEngine#createSpecificationSource(RequestContext)
     * @deprecated To be removed in 3.2. This value can be injected.
     */

    public ISpecificationSource getSpecificationSource();

    /**
     * Returns an object that can resolve resources and classes.
     * 
     * @deprecated To be removed in 3.2. This value can be injected (into services).
     */

    public ClassResolver getClassResolver();

    /**
     * Returns the visit object, an object that represents the client's visit to the application.
     * This is where most server-side state is stored (with the exception of persistent page
     * properties).
     * <p>
     * Returns the visit, if it exists, or null if it has not been created.
     * 
     * @deprecated To be removed in 3.2. Application state objects can now be injected.
     */

    public Object getVisit();

    /**
     * Returns the visit object, creating it if necessary.
     * 
     * @deprecated To be removed in 3.2. Application state objects can now be injected.
     */

    public Object getVisit(IRequestCycle cycle);

    /**
     * Allows the visit object to be removed; typically done when "shutting down" a user's session
     * (by setting the visit to null).
     * 
     * @deprecated To be removed in 3.2. Application state objects can now be injected.
     */

    public void setVisit(Object value);

    /**
     * Returns the globally shared application object. The global object is stored in the servlet
     * context and shared by all instances of the engine for the same application (within the same
     * JVM; the global is <em>not</em> shared between nodes in a cluster).
     * <p>
     * Returns the global object, if it exists, or null if not defined.
     * 
     * @since 2.3
     * @deprecated To be removed in 3.2. Application state objects can now be injected.
     */

    public Object getGlobal();

    /**
     * Returns a source for parsed {@link org.apache.tapestry.IScript}s. The source is shared
     * between all sessions.
     * 
     * @since 1.0.2
     * @deprecated To be removed in 3.2. This value can now be injected.
     */

    public IScriptSource getScriptSource();

    /**
     * Returns a {@link org.apache.tapestry.engine.IPropertySource}that should be used to obtain
     * configuration data. The returned source represents a search path that includes (at a
     * minimum):
     * <ul>
     * <li>Properties of the {@link org.apache.tapestry.spec.ApplicationSpecification}
     * <li>Initial Parameters of servlet (configured in the <code>web.xml</code> deployment
     * descriptor)
     * <li>Initial Parameter of the servlet context (also configured in <code>web.xml</code>)
     * <li>System properties (defined with the <code>-D</code> JVM command line parameter)
     * <li>Hard-coded "factory defaults" (for some properties)
     * </ul>
     * 
     * @since 2.3
     * @see org.apache.tapestry.engine.AbstractEngine#createPropertySource(RequestContext)
     * @deprecated To be removed in 3.2. This value can now be injected.
     */

    public IPropertySource getPropertySource();

    /**
     * Returns the encoding to be used to generate the responses and accept the requests.
     * 
     * @since 3.0
     */

    public String getOutputEncoding();

    /**
     * Returns the {@link org.apache.tapestry.services.Infrastructure}&nbsp;object, a central
     * registry of key HiveMind services used by Tapestry.
     * 
     * @since 3.1
     */

    public Infrastructure getInfrastructure();
}