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

package org.apache.tapestry.engine;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.request.RequestContext;

/**
 *  A EngineServiceLink represents a possible action within the client web browser;
 *  either clicking a link or submitting a form, which is constructed primarily
 *  from the {@link org.apache.tapestry.IEngine#getServletPath() servlet path},
 *  with some additional query parameters.  A full URL for the EngineServiceLink
 *  can be generated, or the query parameters for the EngineServiceLink can be extracted
 *  (separately from the servlet path).  The latter case is used when submitting
 *  constructing {@link org.apache.tapestry.form.Form forms}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.4
 * 
 **/

public class EngineServiceLink implements ILink
{
    private static final int DEFAULT_HTTP_PORT = 80;

    private IRequestCycle _cycle;
    private String _serviceName;
    private String _context;
    private String[] _parameters;
    private boolean _stateful;

    /**
     *  Creates a new EngineServiceLink.  A EngineServiceLink always names a service to be activated
     *  by the link, has an optional list of service context strings,
     *  an optional list of service parameter strings and may be stateful
     *  or stateless.
     * 
     *  <p>ServiceLink parameter strings may contain any characters.
     * 
     *  <p>ServiceLink context strings must be URL safe, and may not contain
     *  slash ('/') characters.  Typically, only letters, numbers and simple
     *  punctuation ('.', '-', '_', ':') is recommended (no checks are currently made,
     *  however).  Context strings are generally built from page names
     *  and component ids, which are limited to safe characters.
     *  
     *  @param cycle The {@link IRequestCycle} the EngineServiceLink is to be created for.
     *  @param serviceName The name of the service to be invoked by the EngineServiceLink.
     *  @param serviceContext an optional array of strings to be provided
     *  to the service to provide a context for executing the service.  May be null
     *  or empty.  <b>Note: copied, not retained.</b>
     *  @param serviceParameters An array of parameters, may be 
     *  null or empty. <b>Note: retained, not copied.</b>
     *  @param stateful if true, the service which generated the EngineServiceLink
     *  is stateful and expects that the final URL will be passed through
     *  {@link IRequestCycle#encodeURL(String)}.
     **/

    public EngineServiceLink(
        IRequestCycle cycle,
        String serviceName,
        String[] serviceContext,
        String[] serviceParameters,
        boolean stateful)
    {
        _cycle = cycle;
        _serviceName = serviceName;
        _context = constructContext(serviceContext);
        _parameters = serviceParameters;
        _stateful = stateful;
    }

    private String constructContext(String[] serviceContext)
    {
        int count = Tapestry.size(serviceContext);

        if (count == 0)
            return null;

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < count; i++)
        {
            if (i > 0)
                buffer.append('/');

            buffer.append(serviceContext[i]);
        }

        return buffer.toString();
    }

    public String getURL()
    {
        return getURL(null, true);
    }

    public String getURL(String anchor, boolean includeParameters)
    {
        return constructURL(new StringBuffer(), anchor, includeParameters);
    }

    public String getAbsoluteURL()
    {
        return getAbsoluteURL(null, null, 0, null, true);
    }

    public String getAbsoluteURL(
        String scheme,
        String server,
        int port,
        String anchor,
        boolean includeParameters)
    {
        StringBuffer buffer = new StringBuffer();
        RequestContext context = _cycle.getRequestContext();

        if (scheme == null)
            scheme = context.getScheme();

        buffer.append(scheme);
        buffer.append("://");

        if (server == null)
            server = context.getServerName();

        buffer.append(server);

        if (port == 0)
            port = context.getServerPort();

        if (!(scheme.equals("http") && port == DEFAULT_HTTP_PORT))
        {
            buffer.append(':');
            buffer.append(port);
        }

        // Add the servlet path and the rest of the URL & query parameters.
        // The servlet path starts with a leading slash.

        return constructURL(buffer, anchor, includeParameters);
    }

    private String constructURL(StringBuffer buffer, String anchor, boolean includeParameters)
    {
        buffer.append(_cycle.getEngine().getServletPath());

        if (includeParameters)
        {
            buffer.append('?');
            buffer.append(Tapestry.SERVICE_QUERY_PARAMETER_NAME);
            buffer.append('=');
            buffer.append(_serviceName);

            if (_context != null)
            {
                buffer.append('&');
                buffer.append(Tapestry.CONTEXT_QUERY_PARMETER_NAME);
                buffer.append('=');
                buffer.append(_context);
            }

            int count = Tapestry.size(_parameters);

            for (int i = 0; i < count; i++)
            {
                buffer.append('&');

                buffer.append(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);
                buffer.append('=');

                try
                {
                    // We use the older, deprecated version of this method, which is compatible
                    // with the JDK 1.2.2.

                    String encoded = URLEncoder.encode(_parameters[i]);

                    buffer.append(encoded);
                }
                catch (Exception ex)
                {
                    // JDK1.2.2 claims this throws Exception.  It doesn't
                    // and we ignore it.
                }
            }
        }

        if (anchor != null)
        {
            buffer.append('#');
            buffer.append(anchor);
        }

        String result = buffer.toString();

        if (_stateful)
            result = _cycle.encodeURL(result);

        return result;
    }

    public String[] getParameterNames()
    {
        List list = new ArrayList();

        list.add(Tapestry.SERVICE_QUERY_PARAMETER_NAME);

        if (_context != null)
            list.add(Tapestry.CONTEXT_QUERY_PARMETER_NAME);

        if (Tapestry.size(_parameters) != 0)
            list.add(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);

        return (String[]) list.toArray(new String[list.size()]);
    }

    public String[] getParameterValues(String name)
    {
        if (name.equals(Tapestry.SERVICE_QUERY_PARAMETER_NAME))
        {
            return new String[] { _serviceName };
        }

        if (name.equals(Tapestry.CONTEXT_QUERY_PARMETER_NAME))
        {
            return new String[] { _context };
        }

        if (name.equals(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME))
        {
            return _parameters;
        }

        throw new IllegalArgumentException(
            Tapestry.getString("EngineServiceLink.unknown-parameter-name", name));

    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("serviceName", _serviceName);
        builder.append("context", _context);
        builder.append("parameters", _parameters);
        builder.append("stateful", _stateful);

        return builder.toString();
    }

}