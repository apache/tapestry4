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

import java.net.URL;

import net.sf.tapestry.Gesture;
import net.sf.tapestry.IEngineService;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Abstract super-class for components that generate some form of
 *  &lt;a&gt; hyperlink using an {@link IEngineService}.
 *  Supplies support for the following parameters:
 *
 *  <ul>
 *  <li>scheme</li>
 *  <li>port</li>
 *  <li>anchor</li>
 * </ul>
 *
 * <p>Subclasses usually need only implement {@link #getServiceName()}
 * and {@link #getContext(IRequestCycle)}.
 * 
 *                       
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class GestureLink extends AbstractServiceLink
{
    private String _anchor;
    private String _scheme;
    private int _port;

    /**
     *  Constructs a URL based on the service, context plus scheme, port and anchor.
     * 
     **/

    protected String getURL(IRequestCycle cycle) throws RequestCycleException
    {
        return buildURL(cycle, getServiceParameters(cycle));
    }


    /**
     *  @deprecated To be removed in 2.3, use {@link #getServiceParameters(IRequestCycle)}
     *  @return null subclasses may override (but should override
     *  {@link #getServiceParameters(IRequestCycle)} instead)
     * 
     **/

    protected String[] getContext(IRequestCycle cycle) throws RequestCycleException
    {
        return getServiceParameters(cycle);
    }

    /**
     *  Invoked by {@link #getURL(IRequestCycle)}.
     *  The default implementation 
     *  invokes {@link #getContext(IRequestCycle)}, which returns null.
     *  (In 2.3, this method will simply return null).
     *  Implementations can provide appropriate parameters as needed.
     *  
     *  @since 2.2
     * 
     **/
        
    protected String[] getServiceParameters(IRequestCycle cycle) throws RequestCycleException
    {
        return getContext(cycle);
    }

    private String buildURL(IRequestCycle cycle, String[] serviceParameters)
        throws RequestCycleException
    {
        String URL = null;

        String serviceName = getServiceName();
        IEngineService service = cycle.getEngine().getService(serviceName);

        if (service == null)
            throw new RequestCycleException(
                Tapestry.getString("GestureLink.missing-service", serviceName),
                this);

        Gesture g = service.buildGesture(cycle, this, serviceParameters);

        // Now, dress up the URL with scheme, server port and anchor,
        // as necessary.

        if (_scheme == null && _port == 0)
            URL = g.getURL();
        else
            URL = g.getAbsoluteURL(_scheme, null, _port);

        if (_anchor == null)
            return URL;

        return URL + "#" + _anchor;
    }

    /**
     *  Returns the service used to build URLs.  This method is implemented
     *  by subclasses.
     *
     **/

    protected abstract String getServiceName();

    public String getAnchor()
    {
        return _anchor;
    }

    public void setAnchor(String anchor)
    {
        this._anchor = anchor;
    }

    public int getPort()
    {
        return _port;
    }

    public void setPort(int port)
    {
        this._port = port;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public void setScheme(String scheme)
    {
        this._scheme = scheme;
    }

}