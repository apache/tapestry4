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

import javax.servlet.http.HttpServletRequest;

/**
 *  Given a {@link javax.servlet.http.HttpServletRequest}, identifies
 *  the correct request properties (server, scheme, URI and port).
 * 
 *  <p>An implementation of this class may be necessary when using
 *  Tapestry with specific firewalls which may obscure
 *  the scheme, server, etc. visible to the client web browser
 *  (the request appears to arrive from the firewall server, not the
 *  client web browser).
 *
 *  @author Howard Lewis Ship
 *  @version IRequestDecoder.java,v 1.1 2002/08/20 21:49:58 hship Exp
 *  @since 2.2
 * 
 **/

public interface IRequestDecoder
{

    /**
     *  Invoked to identify the actual properties from the request.
     * 
     **/

    public DecodedRequest decodeRequest(HttpServletRequest request);
}
