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

package org.apache.tapestry.request;

/**
 *  Contains properties of an {@link javax.servlet.http.HttpServletRequest}
 *  that have been extracted from the request (or otherwise determined).
 * 
 *  <p>An alternative idea would have been to create a new 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  wrapper that overode the various methods.  That struck me as causing
 *  more confusion; instead (in the few places it counts), classes will
 *  get the decoded properties from the {@link RequestContext}.
 *
 *  @see IRequestDecoder
 *  @see RequestContext#getScheme()
 *  @see RequestContext#getServerName()
 *  @see RequestContext#getServerPort()
 *  @see RequestContext#getRequestURI()
 * 
 *  @author Howard Lewis Ship
 *  @version DecodedRequest.java,v 1.1 2002/08/20 21:49:58 hship Exp
 *  @since 2.2
 * 
 **/

public class DecodedRequest
{
    private String _scheme;
    private String _serverName;
    private String _requestURI;
    private int _serverPort;

    public int getServerPort()
    {
        return _serverPort;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public String getServerName()
    {
        return _serverName;
    }

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