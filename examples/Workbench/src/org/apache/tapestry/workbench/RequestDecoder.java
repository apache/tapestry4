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

package org.apache.tapestry.workbench;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.tapestry.request.DecodedRequest;
import org.apache.tapestry.request.IRequestDecoder;

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
        LOG.debug("<init>");
    }

    public DecodedRequest decodeRequest(HttpServletRequest request)
    {
        if (LOG.isDebugEnabled())
            LOG.debug("Decoding: " + request);

        DecodedRequest result = new DecodedRequest();

        result.setRequestURI(request.getRequestURI());
        result.setScheme(request.getScheme());
        result.setServerName(request.getServerName());
        result.setServerPort(request.getServerPort());

        return result;

    }

}
