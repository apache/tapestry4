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

package net.sf.tapestry;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 *  A Gesture represents a possible action within the client web browser;
 *  either clicking a link or submitting a form.  A full URL for the Gesture
 *  can be generated, or the query parameters for the Gesture can be extracted
 *  (seperately from the servlet path).  The latter case is used when submitting
 *  forms.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.3
 */

public class Gesture
{
    private static final int DEFAULT_HTTP_PORT = 80;

    private IRequestCycle cycle;
    private Map queryParameters;
    private boolean stateful;

    /**
     *  Creates a new Gesture, for the given request cycle and with a set
     *  or query parameters.
     *  
     * @param cycle the {@link IRequestCycle} the Gesture is to be created for
     * @param queryParameters a {@link Map} of parameters.  Keys and values
     *  are both String.  Map not be null; one query parameter must be
     *  specify the engine service.
     * @param stateful if true, the service which generated the Gesture
     * is stateful and expects that the final URL will be passed through
     * {@link IRequestCycle#encodeURL(String)}.
     **/

    public Gesture(IRequestCycle cycle, Map queryParameters, boolean stateful)
    {
        this.cycle = cycle;

        this.queryParameters = new HashMap(queryParameters);
        this.stateful = stateful;
    }

    /**
     *  Returns the {@link Iterator} for the query parameter map's entry set.
     *  Each value will be {@link Map} entry.
     *
     **/

    public Iterator getQueryParameters()
    {
        return queryParameters.entrySet().iterator();
    }

    public String getServletPath()
    {
        return cycle.getEngine().getServletPath();
    }

    /**
     *  Something of a misnomer; returns the URI, relative to the server's root.
     *  If the Gesture is stateful, then the URI is filtered
     *  through {@link IRequestCycle#encodeURL(String)}.
     *
     **/

    public String getURL()
    {
        StringBuffer buffer = new StringBuffer();

        constructURL(buffer);

        String result = buffer.toString();

        if (stateful)
            return cycle.encodeURL(result);

        return result;
    }

    /**
     *  Constructs an absolute URL, including scheme, server and port.
     *  This is often useful when the URL will be included in a client-side script.
     *
     **/

    public String getAbsoluteURL()
    {
        return getAbsoluteURL(null, null, 0);
    }

    /**
     *  Generates a complete URL.
     *  @param scheme if non-null, used as the scheme (instead of the scheme defined by the cycle)
     *  @param server if non-null, used as the server (instead of the server defined by the cycle)
     *  @param port if non-zero, used as the port (instead of the port defined by the cycle)
     * 
     **/

    public String getAbsoluteURL(String scheme, String server, int port)
    {
        StringBuffer buffer = new StringBuffer();
        HttpServletRequest request = cycle.getRequestContext().getRequest();

        if (scheme == null)
            scheme = request.getScheme();

        buffer.append(scheme);
        buffer.append("://");

        if (server == null)
            server = request.getServerName();

        buffer.append(server);

        if (port == 0)
            port = request.getServerPort();

        if (!(scheme.equals("http") && port == DEFAULT_HTTP_PORT))
        {
            buffer.append(':');
            buffer.append(port);
        }

        // Add the servlet path and the rest of the URL & query parameters.
        // The servlet path start with a leading slash.

        constructURL(buffer);

        String result = buffer.toString();

        if (stateful)
            result = cycle.encodeURL(result);

        return result;
    }

    private void constructURL(StringBuffer buffer)
    {
        buffer.append(getServletPath());

        Iterator i = getQueryParameters();
        boolean first = true;

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();

            if (first)
                buffer.append('?');
            else
                buffer.append('&');

            first = false;

            buffer.append(entry.getKey().toString());
            buffer.append('=');
            buffer.append(entry.getValue().toString());
        }

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Gesture[");

        buffer.append(getServletPath());
        buffer.append(' ');

        buffer.append(queryParameters);

        if (stateful)
            buffer.append(" stateful");

        buffer.append(']');

        return buffer.toString();
    }
}