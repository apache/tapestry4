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

package org.apache.tapestry.engine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.ApplicationRuntimeException;
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
 *  @since 3.0
 * 
 **/

public class EngineServiceLink implements ILink
{
    private static final int DEFAULT_HTTP_PORT = 80;
    private static final URLCodec _urlCodec = new URLCodec();

    private IRequestCycle _cycle;
    private String _service;
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
        _service = constructServiceValue(serviceName, serviceContext);
        _parameters = serviceParameters;
        _stateful = stateful;
    }

    private String constructServiceValue(String serviceName, String[] serviceContext)
    {
        int count = Tapestry.size(serviceContext);

        if (count == 0)
            return serviceName;

        StringBuffer buffer = new StringBuffer(serviceName);

        for (int i = 0; i < count; i++)
        {
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
            buffer.append(_service);

            int count = Tapestry.size(_parameters);

            for (int i = 0; i < count; i++)
            {
                buffer.append('&');

                buffer.append(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);
                buffer.append('=');

                String encoding = _cycle.getEngine().getOutputEncoding();
                try
                {
                    String encoded = _urlCodec.encode(_parameters[i], encoding);
                    buffer.append(encoded);
                }
                catch (UnsupportedEncodingException e)
                {
                    throw new ApplicationRuntimeException(
                        Tapestry.format("illegal-encoding", encoding),
                        e);
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

        if (Tapestry.size(_parameters) != 0)
            list.add(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME);

        return (String[]) list.toArray(new String[list.size()]);
    }

    public String[] getParameterValues(String name)
    {
        if (name.equals(Tapestry.SERVICE_QUERY_PARAMETER_NAME))
        {
            return new String[] { _service };
        }

        if (name.equals(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME))
        {
            return _parameters;
        }

        throw new IllegalArgumentException(
            Tapestry.format("EngineServiceLink.unknown-parameter-name", name));
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("service", _service);
        builder.append("parameters", _parameters);
        builder.append("stateful", _stateful);

        return builder.toString();
    }

}