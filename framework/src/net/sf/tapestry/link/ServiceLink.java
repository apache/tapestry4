//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.link;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.Tapestry;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

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
    private static final Logger LOG = LogManager.getLogger(ServiceLink.class);

    private String _service;
    private Object _parameters;
    private boolean _warning = true;

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

    /**
     *  @deprecated To be removed in 2.3, use {@link #getParameters()}.
     * 
     **/

    public Object getContext()
    {
        return getParameters();
    }

    /**
     *  @deprecated To be removed in 2.3, use {@link #setParameters(Object)}.
     * 
     **/

    public void setContext(Object context)
    {
        if (_warning)
        {
            LOG.warn(Tapestry.getString("deprecated-component-param", getExtendedId(), "context", "parameters"));

            _warning = false;
        }

        setParameters(context);
    }

    public String getService()
    {
        return _service;
    }

    public void setService(String service)
    {
        this._service = service;
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