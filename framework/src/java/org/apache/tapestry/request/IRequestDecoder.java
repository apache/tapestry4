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
