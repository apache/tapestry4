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

package org.apache.tapestry.resolver;

import org.apache.hivemind.Resource;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Base class for resolving a
 * {@link org.apache.tapestry.spec.IComponentSpecification}for a particular
 * page or component, within a specified {@link org.apache.tapestry.INamespace}.
 * In some cases, a search is necessary.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class AbstractSpecificationResolver {

    private INamespace _namespace;

    private IComponentSpecification _specification;

    /** Set by container. */
    private ISpecificationSource _specificationSource;

    private ISpecificationResolverDelegate _delegate;

    private String _applicationId;

    private Resource _contextRoot;

    /** Initialized in initializeService(). */

    private Resource _webInfLocation;

    private Resource _webInfAppLocation;

    public void initializeService()
    {
        _webInfLocation = _contextRoot.getRelativeResource("WEB-INF/");

        _webInfAppLocation = _webInfLocation.getRelativeResource(_applicationId
                + "/");
    }

    /**
     * Returns the {@link ISpecificationResolverDelegate}instance registered in
     * the application specification as extension
     * {@link Tapestry#SPECIFICATION_RESOLVER_DELEGATE_EXTENSION_NAME}, or null
     * if no such extension exists.
     */

    public ISpecificationResolverDelegate getDelegate()
    {
        return _delegate;
    }

    /**
     * Returns the location of the servlet, within the servlet context.
     */

    protected Resource getContextRoot()
    {
        return _contextRoot;
    }

    public void setContextRoot(Resource contextRoot)
    {
        _contextRoot = contextRoot;
    }

    /**
     * Invoked in subclasses to identify the resolved namespace.
     */

    protected void setNamespace(INamespace namespace)
    {
        _namespace = namespace;
    }

    /**
     * Returns the resolve namespace.
     */

    public INamespace getNamespace()
    {
        return _namespace;
    }

    /**
     * Returns the specification source for the running application.
     */

    protected ISpecificationSource getSpecificationSource()
    {
        return _specificationSource;
    }

    /**
     * Returns the location of /WEB-INF/, in the servlet context.
     */

    protected Resource getWebInfLocation()
    {
        return _webInfLocation;
    }

    /**
     * Returns the location of the application-specific subdirectory, under
     * /WEB-INF/, in the servlet context.
     */

    protected Resource getWebInfAppLocation()
    {
        return _webInfAppLocation;
    }

    /**
     * Returns the resolved specification.
     */

    public IComponentSpecification getSpecification()
    {
        return _specification;
    }

    /**
     * Invoked in subclass to set the final specification the initial inputs are
     * resolved to.
     */

    protected void setSpecification(IComponentSpecification specification)
    {
        _specification = specification;
    }

    /**
     * Clears the namespace and specification properties.
     */

    protected void reset()
    {
        _namespace = null;
        _specification = null;
    }

    /** @since 4.0 */
    public void setDelegate(ISpecificationResolverDelegate delegate)
    {
        _delegate = delegate;
    }

    /** @since 4.0 */
    public void setApplicationId(String applicationId)
    {
        _applicationId = applicationId;
    }

    /** @since 4.0 */
    public void setSpecificationSource(ISpecificationSource source)
    {
        _specificationSource = source;
    }

    /** @since 4.0 */
    protected INamespace getApplicationNamespace()
    {
        return _specificationSource.getApplicationNamespace();
    }

    /** @since 4.0 */
    protected INamespace getFrameworkNamespace()
    {
        return _specificationSource.getFrameworkNamespace();
    }

    /**
     * @since 4.0
     */
    protected INamespace findNamespaceForId(INamespace containerNamespace,
            String libraryId)
    {
        if (libraryId == null) return containerNamespace;

        if (libraryId.equals(INamespace.APPLICATION_NAMESPACE))
            return getApplicationNamespace();

        if (libraryId.equals(INamespace.FRAMEWORK_NAMESPACE))
            return getFrameworkNamespace();

        return containerNamespace.getChildNamespace(libraryId);
    }
}
