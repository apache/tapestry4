package net.sf.tapestry.resolver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.IEngine;
import net.sf.tapestry.INamespace;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.IResourceLocation;
import net.sf.tapestry.ISpecificationSource;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.spec.ComponentSpecification;

/**
 *  Base class for resolving a {@link net.sf.tapestry.spec.ComponentSpecification}
 *  for a particular page or component, within a specified 
 *  {@link net.sf.tapestry.INamespace}.  In some cases, a search is necessary.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 *
 **/

public class AbstractSpecificationResolver
{
    private static final Log LOG = LogFactory.getLog(AbstractSpecificationResolver.class);

    private ISpecificationSource _specificationSource;

    private INamespace _namespace;

    private ComponentSpecification _specification;

    private IResourceLocation _applicationRootLocation;

    private IResourceLocation _webInfLocation;

    private IResourceLocation _webInfAppLocation;

    public AbstractSpecificationResolver(IRequestCycle cycle)
    {
        IEngine engine = cycle.getEngine();

        _specificationSource = engine.getSpecificationSource();

        _applicationRootLocation = Tapestry.getApplicationRootLocation(cycle);

        String servletName = cycle.getRequestContext().getServlet().getServletConfig().getServletName();

        _webInfLocation = _applicationRootLocation.getRelativeLocation("/WEB-INF/");

        _webInfAppLocation = _webInfLocation.getRelativeLocation(servletName + "/");
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

    public ComponentSpecification getSpecification()
    {
        return _specification;
    }

    /**
     *  Invoked in subclass to set the final specification the initial
     *  inputs are resolved to.
     * 
     **/

    protected void setSpecification(ComponentSpecification specification)
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
