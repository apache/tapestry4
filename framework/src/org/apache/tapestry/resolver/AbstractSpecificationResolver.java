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

package org.apache.tapestry.resolver;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.pool.IPoolable;

/**
 *  Base class for resolving a {@link org.apache.tapestry.spec.IComponentSpecification}
 *  for a particular page or component, within a specified 
 *  {@link org.apache.tapestry.INamespace}.  In some cases, a search is necessary.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 *
 **/

public class AbstractSpecificationResolver implements IPoolable
{
    private ISpecificationSource _specificationSource;

    private INamespace _namespace;

    private IComponentSpecification _specification;

    private IResourceLocation _applicationRootLocation;

    private IResourceLocation _webInfLocation;

    private IResourceLocation _webInfAppLocation;

    private ISpecificationResolverDelegate _delegate;

    public AbstractSpecificationResolver(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();

        _specificationSource = engine.getSpecificationSource();

        _applicationRootLocation = Tapestry.getApplicationRootLocation(cycle);

        String servletName =
            cycle.getRequestContext().getServlet().getServletConfig().getServletName();

        _webInfLocation = _applicationRootLocation.getRelativeLocation("/WEB-INF/");

        _webInfAppLocation = _webInfLocation.getRelativeLocation(servletName + "/");

        IApplicationSpecification specification = engine.getSpecification();

        if (specification.checkExtension(Tapestry.SPECIFICATION_RESOLVER_DELEGATE_EXTENSION_NAME))
            _delegate =
                (ISpecificationResolverDelegate) engine.getSpecification().getExtension(
                    Tapestry.SPECIFICATION_RESOLVER_DELEGATE_EXTENSION_NAME,
                    ISpecificationResolverDelegate.class);
        else
            _delegate = NullSpecificationResolverDelegate.getSharedInstance();
    }

    /**
     *  Returns the {@link ISpecificationResolverDelegate} instance registered
     *  in the application specification as extension
     *  {@link Tapestry#SPECIFICATION_RESOLVER_DELEGATE_EXTENSION_NAME},
     *  or null if no such extension exists.
     * 
     **/

    public ISpecificationResolverDelegate getDelegate()
    {
        return _delegate;
    }

    /**
     *  Returns the location of the servlet, within the
     *  servlet context.
     * 
     **/

    protected IResourceLocation getApplicationRootLocation()
    {
        return _applicationRootLocation;
    }

    /**
     *  Invoked in subclasses to identify the resolved namespace.
     * 
     **/

    protected void setNamespace(INamespace namespace)
    {
        _namespace = namespace;
    }

    /**
     *  Returns the resolve namespace.
     * 
     **/

    public INamespace getNamespace()
    {
        return _namespace;
    }

    /**
     *  Returns the specification source for the running application.
     * 
     **/

    protected ISpecificationSource getSpecificationSource()
    {
        return _specificationSource;
    }

    /**
     *  Returns the location of /WEB-INF/, in the servlet context.
     * 
     **/

    protected IResourceLocation getWebInfLocation()
    {
        return _webInfLocation;
    }

    /**
     *  Returns the location of the application-specific subdirectory, under
     *  /WEB-INF/, in the servlet context.
     * 
     **/

    protected IResourceLocation getWebInfAppLocation()
    {
        return _webInfAppLocation;
    }

    /**
     *  Returns the resolved specification.
     * 
     **/

    public IComponentSpecification getSpecification()
    {
        return _specification;
    }

    /**
     *  Invoked in subclass to set the final specification the initial
     *  inputs are resolved to.
     * 
     **/

    protected void setSpecification(IComponentSpecification specification)
    {
        _specification = specification;
    }

    /**
     *  Clears the namespace, specification and simpleName properties.
     * 
     **/

    protected void reset()
    {
        _namespace = null;
        _specification = null;
    }

    /** Does nothing. */
    public void discardFromPool()
    {

    }

    /** Invokes {@link #reset()} */

    public void resetForPool()
    {
        reset();
    }

}
