// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.services.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.hivemind.Resource;
import org.apache.hivemind.impl.BaseLocatable;
import org.apache.tapestry.services.RequestGlobals;

/**
 * Wrapper around {@link org.apache.hivemind.service.ThreadLocalStorage} used
 * to store and retrieve Servlet API info.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class RequestGlobalsImpl extends BaseLocatable implements RequestGlobals
{
    private HttpServletRequest _request;
    private HttpServletResponse _response;
    private Resource _applicationRoot;

    public void store(HttpServletRequest request, HttpServletResponse response)
    {
        _request = request;
        _response = response;
    }

    public HttpServletRequest getRequest()
    {
        return _request;
    }

    public HttpServletResponse getResponse()
    {
        return _response;
    }
}
