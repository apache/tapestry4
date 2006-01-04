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

package org.apache.tapestry.services.impl;

import org.apache.tapestry.services.AbsoluteURLBuilder;
import org.apache.tapestry.web.WebRequest;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AbsoluteURLBuilderImpl implements AbsoluteURLBuilder
{
    private WebRequest _request;

    public String constructURL(String URI, String scheme, String server, int port)
    {
        // Though, really, what does a leading colon with no scheme before it
        // mean?

        if (URI.indexOf(':') >= 0)
            return URI;

        StringBuffer buffer = new StringBuffer();

        // Should check the length here, first.

        if (URI.length()> 2 && URI.substring(0, 2).equals("//"))
        {
            buffer.append(scheme);
            buffer.append(':');
            buffer.append(URI);
            return buffer.toString();
        }

        buffer.append(scheme);
        buffer.append("://");
        buffer.append(server);

        if (port > 0)
        {
            buffer.append(':');
            buffer.append(port);
        }

        if (URI.charAt(0) != '/')
            buffer.append('/');

        buffer.append(URI);

        return buffer.toString();
    }

    public String constructURL(String URI)
    {
        String scheme = _request.getScheme();
        String server = _request.getServerName();
        int port = _request.getServerPort();

        // Keep things simple ... port 80 is accepted as the
        // standard port for http so it can be ommitted.
        // Some of the Tomcat code indicates that port 443 is the default
        // for https, and that needs to be researched.

        if (scheme.equals("http") && port == 80)
            port = 0;

        return constructURL(URI, scheme, server, port);
    }

    public void setRequest(WebRequest request)
    {
        _request = request;
    }
}