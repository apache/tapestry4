// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.hivemind.util.Defense;

/**
 * A wrapper that uses a {@link org.apache.tapestry.request.DecodedRequest}to override default
 * values for several HttpServletRequest properties.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class DecodedRequestWrapper extends HttpServletRequestWrapper
{
    private final DecodedRequest _decoded;

    public DecodedRequestWrapper(HttpServletRequest request, DecodedRequest decoded)
    {
        super(request);

        Defense.notNull(decoded, "decoded");

        _decoded = decoded;
    }

    public String getRequestURI()
    {
        return _decoded.getRequestURI();
    }

    public String getScheme()
    {
        return _decoded.getScheme();
    }

    public String getServerName()
    {
        return _decoded.getServerName();
    }

    public int getServerPort()
    {
        return _decoded.getServerPort();
    }

    public String toString()
    {
        return "<DecodedRequestWrapper for " + getRequest() + ">";
    }
}