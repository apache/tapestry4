//
// Tapestry Web Application Framework
// Copyright (c) 2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry;

/**
 *  Contains properties of an {@link javax.servlet.http.HttpServletRequest}
 *  that have been extracted from the request (or otherwise determined).
 * 
 *  <p>An alternative idea would have been to create a new 
 *  {@link javax.servlet.http.HttpServletRequest}
 *  wrapper that overode the various methods.  That struck me as causing
 *  more confusion; instead (in the few places it counts), classes will
 *  get the decoded properties from the {@link net.sf.tapestry.RequestContext}.
 *
 *  @see net.sf.tapestry.IRequestDecoder
 *  @see net.sf.tapestry.RequestContext#getScheme()
 *  @see net.sf.tapestry.RequestContext#getServerName()
 *  @see net.sf.tapestry.RequestContext#getServerPort()
 *  @see net.sf.tapestry.RequestContext#getRequestURI()
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