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

package tutorial.workbench;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sf.tapestry.DecodedRequest;
import net.sf.tapestry.IRequestDecoder;

/**
 *  A useless request decoder (does the same as the default), used to test that
 *  a user-supplied extension is actually used.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class RequestDecoder implements IRequestDecoder
{
    private static final Log LOG = LogFactory.getLog(RequestDecoder.class);

    public RequestDecoder()
    {
        LOG.info("<init>");
    }

    public DecodedRequest decodeRequest(HttpServletRequest request)
    {
        LOG.info("Decoding: " + request);

        DecodedRequest result = new DecodedRequest();

        result.setRequestURI(request.getRequestURI());
        result.setScheme(request.getScheme());
        result.setServerName(request.getServerName());
        result.setServerPort(request.getServerPort());

        return result;

    }

}
