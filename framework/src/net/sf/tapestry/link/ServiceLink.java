package net.sf.tapestry.link;

import net.sf.tapestry.IRequestCycle;

/**
 *  A component for creating a link for an arbitrary {@link net.sf.tapestry.IEngineService
 *  engine service}.  A ServiceLink component can emulate an {@link ActionLink},
 *  {@link PageLink} or {@link DirectLink} component, but is most often used in
 *  conjunction with an application-specific service.  
 *
 *  [<a href="../../../../../ComponentReference/ServiceLink.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ServiceLink extends GestureLink
{
    private String _service;
    private Object _parameters;


    /**
     *  Returns name of the service specified by the service parameter.
     **/

    protected String getServiceName()
    {
        return _service;
    }

    /** 
     *  Invokes {@link DirectLink#constructContext(Object)} to create Object[]
     *  from the context parameter (which may be an object, array of Strings or List of Strings).
     * 
     **/

    protected Object[] getServiceParameters(IRequestCycle cycle)
    {
        return DirectLink.constructServiceParameters(_parameters);

    }

    public String getService()
    {
        return _service;
    }

    public void setService(String service)
    {
        _service = service;
    }

    public Object getParameters()
    {
        return _parameters;
    }

    public void setParameters(Object parameters)
    {
        _parameters = parameters;
    }

}