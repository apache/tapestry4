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

package org.apache.tapestry.link;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;

/**
 *  Renders a link using an absolute URL, not simply a URI
 *  (as with {@link org.apache.tapestry.link.DefaultLinkRenderer}.  In addition,
 *  the scheme, server and port may be changed (this may be appropriate when
 *  switching between secure and insecure portions of an application).
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 3.0
 * 
 **/

public class AbsoluteLinkRenderer extends DefaultLinkRenderer
{
    private String _scheme;
    private String _serverName;
    private int _port;

    public int getPort()
    {
        return _port;
    }

    public String getScheme()
    {
        return _scheme;
    }

    public String getServerName()
    {
        return _serverName;
    }

    /**
     *  Used to override the port in the final URL, if specified.  If not specified,
     *  the port provided by the {@link javax.servlet.ServletRequest#getServerPort() request}
     *  is used (typically, the value 80).
     *
     **/

    public void setPort(int port)
    {
        _port = port;
    }
    
    /**
     *  Used to override the scheme in the final URL, if specified.  If not specified,
     *  the scheme provided by the {@link javax.servlet.ServletRequest#getScheme() request}
     *  is used (typically, <code>http</code>).
     *
     **/

    public void setScheme(String scheme)
    {
        _scheme = scheme;
    }

    /**
     *  Used to override the server name in the final URL, if specified.  If not specified,
     *  the port provided by the {@link javax.servlet.ServletRequest#getServerName() request}
     *  is used.
     *
     **/

    public void setServerName(String serverName)
    {
        _serverName = serverName;
    }

    protected String constructURL(ILink link, String anchor, IRequestCycle cycle)
    {
        return link.getAbsoluteURL(_scheme, _serverName, _port, anchor, true);
    }

}
