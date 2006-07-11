// Copyright 2004, 2005 The Apache Software Foundation
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
import java.util.Map;

import org.apache.commons.codec.net.URLCodec;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.util.QueryParameterMap;
import org.apache.tapestry.web.WebRequest;

/**
 * A EngineServiceLink represents a possible action within the client web browser; either clicking a
 * link or submitting a form, which is constructed primarily from the servlet path, with some
 * additional query parameters. A full URL for the EngineServiceLink can be generated, or the query
 * parameters for the EngineServiceLink can be extracted (separately from the servlet path). The
 * latter case is used when submitting constructing {@link org.apache.tapestry.form.Form forms}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */

public class EngineServiceLink implements ILink
{
    private static final int DEFAULT_HTTP_PORT = 80;

    private final String _servletPath;

    private final URLCodec _codec;

    private IRequestCycle _cycle;
    
    private String _encoding;

    /** @since 4.0 */
    private final QueryParameterMap _parameters;

    /** @since 4.0 */

    private final WebRequest _request;
    
    /**
     * Creates a new EngineServiceLink.
     * 
     * @param servletPath
     *            The path used to invoke the Tapestry servlet.
     * @param codec
     *            A codec for converting strings into URL-safe formats.
     * @param encoding
     *            The output encoding for the request.
     * @param parameters
     *            The query parameters to be encoded into the url. Keys are strings, values are
     *            null, string or array of string. The map is retained, not copied.
     * @param stateful
     *            if true, the service which generated the EngineServiceLink is stateful and expects
     *            that the final URL will be passed through {@link IRequestCycle#encodeURL(String)}.
     */
    
    public EngineServiceLink(String servletPath, String encoding,
            URLCodec codec, WebRequest request, Map parameters, boolean stateful)
    {
        Defense.notNull(servletPath, "servletPath");
        Defense.notNull(encoding, "encoding");
        Defense.notNull(codec, "codec");
        Defense.notNull(request, "request");
        Defense.notNull(parameters, "parameters");
        
        _servletPath = servletPath;
        _encoding = encoding;
        _codec = codec;
        _request = request;
        _parameters = new QueryParameterMap(parameters);
    }
    
    /**
     * Creates a new EngineServiceLink. Primarily used in portlet applications with the
     * additional {@link IRequestCycle} parameter being used to encode asset urls.
     * 
     * @param cycle
     *            The {@link IRequestCycle}&nbsp; the EngineServiceLink is to be created for.
     * @param servletPath
     *            The path used to invoke the Tapestry servlet.
     * @param codec
     *            A codec for converting strings into URL-safe formats.
     * @param encoding
     *            The output encoding for the request.
     * @param parameters
     *            The query parameters to be encoded into the url. Keys are strings, values are
     *            null, string or array of string. The map is retained, not copied.
     * @param stateful
     *            if true, the service which generated the EngineServiceLink is stateful and expects
     *            that the final URL will be passed through {@link IRequestCycle#encodeURL(String)}.
     */

    public EngineServiceLink(IRequestCycle cycle, String servletPath, String encoding,
            URLCodec codec, WebRequest request, Map parameters, boolean stateful)
    {
        Defense.notNull(cycle, "cycle");
        Defense.notNull(servletPath, "servletPath");
        Defense.notNull(encoding, "encoding");
        Defense.notNull(codec, "codec");
        Defense.notNull(request, "request");
        Defense.notNull(parameters, "parameters");
        
        _cycle = cycle;
        _servletPath = servletPath;
        _encoding = encoding;
        _codec = codec;
        _request = request;
        _parameters = new QueryParameterMap(parameters);
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

    public String getURL(String scheme, String server, int port, String anchor,
            boolean includeParameters)
    {
        boolean useAbsolute = EngineUtils.needAbsoluteURL(scheme, server, port, _request);

        return useAbsolute ? getAbsoluteURL(scheme, server, port, anchor, includeParameters)
                : getURL(anchor, includeParameters);
    }

    public String getAbsoluteURL(String scheme, String server, int port, String anchor,
            boolean includeParameters)
    {
        StringBuffer buffer = new StringBuffer();
        
        int nport = port == 0 ? _request.getServerPort() : port;
        String nscheme = scheme == null ? _request.getScheme() : scheme;
        
        buffer.append(nscheme);
        buffer.append("://");
        
        buffer.append(server == null ? _request.getServerName() : server);
        
        if (!(nscheme.equals("http") && nport == DEFAULT_HTTP_PORT))
        {
            buffer.append(':');
            buffer.append(nport);
        }
        
        // Add the servlet path and the rest of the URL & query parameters.
        // The servlet path starts with a leading slash.

        return constructURL(buffer, anchor, includeParameters);
    }

    private String constructURL(StringBuffer buffer, String anchor, boolean includeParameters)
    {
        buffer.append(_servletPath);

        if (includeParameters)
            addParameters(buffer);

        if (anchor != null)
        {
            buffer.append('#');
            buffer.append(anchor);
        }

        String result = buffer.toString();
        
        // TODO: This is somewhat questionable right now, was added in to support TAPESTRY-802
        if (_cycle != null)
            result = _cycle.encodeURL(result);
        
        return result;
    }

    private void addParameters(StringBuffer buffer)
    {
        String[] names = getParameterNames();

        String sep = "?";

        for (int i = 0; i < names.length; i++)
        {
            String name = names[i];
            String[] values = getParameterValues(name);

            if (values == null)
                continue;

            for (int j = 0; j < values.length; j++)
            {
                buffer.append(sep);
                buffer.append(name);
                buffer.append("=");
                buffer.append(encode(values[j]));

                sep = "&";
            }

        }
    }

    private String encode(String value)
    {
        try
        {
            return _codec.encode(value, _encoding);
        }
        catch (UnsupportedEncodingException ex)
        {
            throw new ApplicationRuntimeException(Tapestry.format("illegal-encoding", _encoding),
                    ex);
        }
    }

    public String[] getParameterNames()
    {
        return _parameters.getParameterNames();
    }

    public String[] getParameterValues(String name)
    {
        return _parameters.getParameterValues(name);
    }
}
