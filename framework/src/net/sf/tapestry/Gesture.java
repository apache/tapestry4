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

import java.net.URLEncoder;
import java.util.Collections;
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
 *  <p>Note: This class was changed signficantly in a non-backwards compatible
 *  way in release 2.2.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.3
 * 
 **/

public class Gesture
{
    private static final int DEFAULT_HTTP_PORT = 80;

    private IRequestCycle _cycle;
    private String _serviceName;
    private String[] _serviceContext;
    private String[] _serviceParameters;
    private boolean _stateful;

    /**
     *  Creates a new Gesture.  A Gesture always names a service to be activated
     *  by the Gesture, has an optional list of service context strings,
     *  an optional list of service parameter strings and may be stateful
     *  or stateless.
     * 
     *  <p>Service parameter strings may contain any characters.
     * 
     *  <p>Service context strings must be URL safe, and may not contain
     *  slash ('/') characters.  Typically, only letters, numbers and simple
     *  punctuation ('.', '-', '_') is recommended (no checks are currently made,
     *  however).
     *  
     *  @param cycle The {@link IRequestCycle} the Gesture is to be created for.
     *  @param serviceName The name of the service to be invoked by the Gesture.
     *  @param serviceContext an optional array of strings to be provided
     *  to the service to provide a context for executing the service.  May be null
     *  or empty.  <b>Note: retained, not copied.</b>
     *  @param serviceParameters An array of parameters, may be 
     *  null or empty. <b>Note: retained, not copied.</b>
     *  @param stateful if true, the service which generated the Gesture
     *  is stateful and expects that the final URL will be passed through
     *  {@link IRequestCycle#encodeURL(String)}.
     **/

    public Gesture(IRequestCycle cycle, String serviceName, String[] serviceContext, String[] serviceParameters, boolean stateful)
    {
        _cycle = cycle;
        _serviceName= serviceName;
        _serviceContext = serviceContext;
        _serviceParameters = serviceParameters;
        _stateful = stateful;
    }


    /**
     *  Returns the URI for the Gesture, exclusing any service parameters.
     *  This is used (for example) by {@link net.sf.tapestry.form.Form} which encodes
     *  the service parameters as hidden form fields.  The URL is encoded
     *  if the service is stateful.
     * 
     *  @since 2.2
     * 
     **/

    public String getBareURL()
    {
        return constructURL(new StringBuffer(), false);
    }

    /**
     *  Returns an array of parameters (possibly null) that should be included
     *  as multiple values of query parameter {@link IEngineService#PARAMETERS_QUERY_PARAMETER_NAME}
     *  in the request.  This is primarily for the benefit of
     *  {@link net.sf.tapestry.form.Form}, which encodes these as hidden fields.
     * 
     **/
    
    public String[] getServiceParameters()
    {
        return _serviceParameters;
    }
    
    /**
     *  Something of a misnomer; returns the URI, relative to the server's root.
     *  If the Gesture is stateful, then the URI is filtered
     *  through {@link IRequestCycle#encodeURL(String)}.
     *
     **/

    public String getURL()
    {
        return constructURL(new StringBuffer(), true);
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
        HttpServletRequest request = _cycle.getRequestContext().getRequest();

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
        // The servlet path starts with a leading slash.

        return constructURL(buffer, true);
    }

    private String constructURL(StringBuffer buffer, boolean includeParameters)
    {
        buffer.append(_cycle.getEngine().getServletPath());

        buffer.append('/');
        buffer.append(_serviceName);
        

        if (_serviceContext != null)
        {
            for (int i = 0; i < _serviceContext.length; i++)
            {
                buffer.append('/');

                buffer.append(_serviceContext[i]);
            }
        }

        int count = 
            includeParameters ? Tapestry.size(_serviceParameters) : 0;
            
        for (int i = 0; i < count; i++)
        {
            if (i == 0)
                buffer.append('?');
            else
                buffer.append('&');
                
            buffer.append(IEngineService.PARAMETERS_QUERY_PARAMETER_NAME);
            buffer.append('=');
            
            try
            {
                // We use the older, deprecated version of this method, which is compatible
                // with the JDK 1.2.2.
                
                String encoded = URLEncoder.encode(_serviceParameters[i]);
                
                buffer.append(encoded);
            }
            catch (Exception ex)
            {
                // JDK1.2.2 claims this throws Exception.  It doesn't
                // and we ignore it.
            }
        }

        String result = buffer.toString();

        if (_stateful)
            result = _cycle.encodeURL(result);

        return result;
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("Gesture[");

        buffer.append(getBareURL());
        buffer.append(' ');

        for (int i = 0; i < Tapestry.size(_serviceParameters); i++)
        {
            if (i == 0)
                buffer.append(" parameters=");
            else
                buffer.append(',');
                
            buffer.append(_serviceParameters[i]);
        }

        if (_stateful)
            buffer.append(" (stateful)");

        buffer.append(']');

        return buffer.toString();
    }
}