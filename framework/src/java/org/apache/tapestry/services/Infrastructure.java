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

package org.apache.tapestry.services;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.ClassResolver;
import org.apache.hivemind.Resource;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.engine.state.ApplicationStateManager;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Tapestry infrastructure ... key services required by the {@link org.apache.tapestry.IEngine}
 * instance.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public interface Infrastructure
{
    /**
     * Returns the {@link org.apache.tapestry.spec.IApplicationSpecification}for the current
     * application.
     */

    public IApplicationSpecification getApplicationSpecification();

    /**
     * Returns an {@link IPropertySource}configured to search the application specification, etc.
     * See <code>tapestry.ApplicationPropertySource</code>.
     */
    public IPropertySource getApplicationPropertySource();

    /**
     * Returns an {@link IPropertySource}configured to search the servlet, servlet context, and
     * factory defaults.
     */

    public IPropertySource getGlobalPropertySource();

    /**
     * Returns the coordinator to be notified of reset events (which will, in turn, notify other
     * services).
     */

    public ResetEventCoordinator getResetEventCoordinator();

    /**
     * Returns the source of component message bundles.
     */

    public ComponentMessagesSource getComponentMessagesSource();

    /**
     * Returns component or page template contents.
     */

    public TemplateSource getTemplateSource();

    /**
     * Returns the source of all application, page, component and library specifications.
     */

    public ISpecificationSource getSpecificationSource();

    /**
     * Returns a generic, shared ObjectPool instance.
     */
    public ObjectPool getObjectPool();

    /**
     * Returns the source for pages. The source is a cache of pages, but also can create new
     * instances when needed.
     */

    public IPageSource getPageSource();

    /**
     * Returns the ClassResolver used by the Tapestry HiveMind module, which should be sufficient
     * for use throughout the application.
     */

    public ClassResolver getClassResolver();

    /**
     * The DataSqueezer, used when constructing and decoding values stored in URLs (as query
     * parameters or hidden form fields).
     */

    public DataSqueezer getDataSqueezer();

    /**
     * The source for read-to-execute versions of Tapestry script templates.
     */

    public IScriptSource getScriptSource();

    /**
     * The object from which engine services are obtained.
     */

    public ServiceMap getServiceMap();

    /**
     * Service used to report exceptions to the console.
     */

    public RequestExceptionReporter getRequestExceptionReporter();

    /**
     * Renders the active page as the response.
     */

    public ResponseRenderer getResponseRenderer();

    /**
     * Constructs {@link org.apache.tapestry.engine.ILink}instances for
     * {@link org.apache.tapestry.engine.IEngineService}s.
     */

    public LinkFactory getLinkFactory();

    /**
     * Used by the {@link org.apache.tapestry.IEngine}to create instances of
     * {@link org.apache.tapestry.IRequestCycle}.
     */

    public RequestCycleFactory getRequestCycleFactory();

    /**
     * Accesses application state objects (Visit and Global from Tapestry 3.0, but now more can be
     * created).
     */

    public ApplicationStateManager getApplicationStateManager();

    /**
     * Returns the container request for the current request cycle.
     * <p>
     * Note: to be renamed to getRequest().
     */

    public WebRequest getRequest();

    /**
     * Returns the container response for the current request cycle.
     * <p>
     * Note: to be renamed to getResponse().
     */

    public WebResponse getResponse();

    /**
     * Returns the context path, which identifies the application within the application server.
     * Context path should be used as a prefix for any URLs generated. The context path may be the
     * empty string, and will not end in a slash (servlet paths should start with a slash).
     */

    public String getContextPath();

    /**
     * Returns the application's id; a unique name that is incorporated into various session
     * attribute keys and into certain paths when searching for resources. For a servlet-based
     * Tapestry application, the id is the name of the servlet.
     */

    public String getApplicationId();

    /**
     * Returns the root context resource, which is the starting point when looking for resources
     * within the application.
     */

    public Resource getContextRoot();

    /**
     * Returns an object used to access component meta-data properties.
     */

    public ComponentPropertySource getComponentPropertySource();
}