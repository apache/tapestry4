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
 * <p>Subclasses usually need only implement {@link #getServiceName(IRequestCycle)}
 * and {@link #getContext(IRequestCycle)}.
 * 
 *                       
 * @author Howard Lewis Ship
 * @version $Id$
 * 
 **/

public abstract class GestureLink extends AbstractServiceLink
{
    private String anchor;
    private String scheme;
    private int port;

    /**
     *  Constructs a URL based on the service, context plus scheme, port and anchor.
     * 
     **/

    protected String getURL(IRequestCycle cycle) throws RequestCycleException
    {
        return buildURL(cycle, getContext(cycle));
    }

    /**
     *  Invoked by {@link #getURL(IRequestCycle)}.
     *  The default implementation returns null; other
     *  implementations can provide appropriate parameters as needed.
     *  
     **/

    protected String[] getContext(IRequestCycle cycle) throws RequestCycleException
    {
        return null;
    }

    private String buildURL(IRequestCycle cycle, String[] context)
        throws RequestCycleException
    {
        String URL = null;

        String serviceName = getServiceName();
        IEngineService service = cycle.getEngine().getService(serviceName);

        if (service == null)
            throw new RequestCycleException(
                Tapestry.getString("GestureLink.missing-service", serviceName),
                this);

        Gesture g = service.buildGesture(cycle, this, context);

        // Now, dress up the URL with scheme, server port and anchor,
        // as necessary.

        if (scheme == null && port == 0)
            URL = g.getURL();
        else
            URL = g.getAbsoluteURL(scheme, null, port);

        if (anchor == null)
            return URL;

        return URL + "#" + anchor;
    }

    /**
     *  Returns the service used to build URLs.  This method is implemented
     *  by subclasses.
     *
     **/

    protected abstract String getServiceName();

    public String getAnchor()
    {
        return anchor;
    }

    public void setAnchor(String anchor)
    {
        this.anchor = anchor;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getScheme()
    {
        return scheme;
    }

    public void setScheme(String scheme)
    {
        this.scheme = scheme;
    }

}