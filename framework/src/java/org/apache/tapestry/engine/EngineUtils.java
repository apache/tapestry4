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

package org.apache.tapestry.engine;

import org.apache.tapestry.web.WebRequest;

/**
 * Utilities needed by engine services and etc.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EngineUtils
{

    /**
     * Invoked by {@link #getURL(String, String, int, String, boolean)} to see if an absolute URL is
     * needed (because a specific scheme, server or port was indicated that does not match the
     * incoming request).
     * 
     * @param scheme
     *            the desired URL scheme, or null
     * @param server
     *            the desired URL server name, or null
     * @param port
     *            the desired URL port, or 0
     * @param request
     *            the request to check against
     * @return true if absolute URL is needed, false otherwise
     */
    public static boolean needAbsoluteURL(String scheme, String server, int port, WebRequest request)
    {
        if (scheme != null && !scheme.equals(request.getScheme()))
            return true;
    
        if (server != null && !server.equals(request.getServerName()))
            return true;
    
        if (port != 0 && port != request.getServerPort())
            return true;
    
        return false;
    }

}
