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

package org.apache.tapestry.services;

import org.apache.hivemind.ClassResolver;
import org.apache.tapestry.engine.IComponentClassEnhancer;
import org.apache.tapestry.engine.IPageSource;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.engine.IScriptSource;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IApplicationSpecification;

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
     * Returns the factory responsible for creating enhanced classes for pages and components.
     */

    public IComponentClassEnhancer getComponentClassEnhancer();

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
}