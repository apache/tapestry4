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

package org.apache.tapestry.services;

/**
 * Service used to construct absolute URLs (often used for redirects).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public interface AbsoluteURLBuilder
{
    /**
     * Constructs a URL from the given URI (that is, service path), schema, server and port.
     * 
     * @param URI
     *            either a complete URL (that is, containing a colon), in which case it is returned
     *            unchanged, or the path within the server.
     * @param scheme
     *            scheme to prefix URI with
     * @param server
     *            to prefix the URI with (unless the URI begins with "//"
     * @param port
     *            to suffix the server with (unless the URI begins with "//")
     */
    public String constructURL(String URI, String scheme, String server, int port);

    /**
     * Constructs a URL, defaulting scheme, server and port to the values for the current request.
     */

    public String constructURL(String URI);
}