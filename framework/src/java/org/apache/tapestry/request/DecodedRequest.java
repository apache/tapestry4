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

package org.apache.tapestry.request;

import javax.servlet.http.HttpServletRequest;

/**
 * Contains properties of an {@link javax.servlet.http.HttpServletRequest}that have been extracted
 * from the request (or otherwise determined). An instance of this is created by an
 * {@link org.apache.tapestry.request.IRequestDecoder}. The decoder must set the serverName and
 * requestURI properties, and should set the scheme and server port properties.
 * 
 * @see IRequestDecoder
 * @author Howard Lewis Ship
 * @since 2.2
 */

public class DecodedRequest
{
    private String _scheme = "http";

    private String _serverName;

    private String _requestURI;

    private int _serverPort = 80;

    public DecodedRequest()
    {
    }

    /**
     * Initializes default values for the properties from the request provided.
     * 
     * @since 3.1
     */

    public DecodedRequest(HttpServletRequest request)
    {
        _scheme = request.getScheme();
        _serverName = request.getServerName();
        _requestURI = request.getRequestURI();
        _serverPort = request.getServerPort();
    }

    /**
     * Default value is 80.
     */

    public int getServerPort()
    {
        return _serverPort;
    }

    /**
     * Default value is 'http'.
     */

    public String getScheme()
    {
        return _scheme;
    }

    /**
     * No default, a value must be set by the decoder.
     */

    public String getServerName()
    {
        return _serverName;
    }

    /**
     * No default, a value must be set by the decoder.
     */

    public String getRequestURI()
    {
        return _requestURI;
    }

    public void setServerPort(int serverPort)
    {
        _serverPort = serverPort;
    }

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

    public void setRequestURI(String URI)
    {
        _requestURI = URI;
    }

}