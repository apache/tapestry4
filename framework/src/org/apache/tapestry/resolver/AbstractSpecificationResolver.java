/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.resolver;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.ISpecificationSource;
import org.apache.tapestry.spec.IApplicationSpecification;
import org.apache.tapestry.spec.IComponentSpecification;

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

public class AbstractSpecificationResolver
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

}
