// Copyright 2005 The Apache Software Foundation
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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.services.RequestGlobals;
import org.apache.tapestry.services.ServletRequestServicer;
import org.apache.tapestry.services.WebRequestServicer;
import org.apache.tapestry.web.ServletWebRequest;
import org.apache.tapestry.web.ServletWebResponse;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebResponse;

/**
 * Bridges from the {@link org.apache.tapestry.services.ServletRequestServicer}&nbsp;pipeline to
 * the {@link org.apache.tapestry.services.WebRequestServicer}&nbsp;pipeline. Also, stores the web
 * request and web response into {@link org.apache.tapestry.services.RequestGlobals}. Intercepts
 * runtime exceptions and throws them wrapped as {@link javax.servlet.ServletException}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class WebRequestServicerPipelineBridge implements ServletRequestServicer
{
    private RequestGlobals _requestGlobals;

    private WebRequestServicer _webRequestServicer;

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException
    {
        _requestGlobals.store(request, response);

        WebRequest webRequest = new ServletWebRequest(request, response);
        WebResponse webResponse = new ServletWebResponse(response);

        try
        {
            _webRequestServicer.service(webRequest, webResponse);
        }
        catch (RuntimeException ex)
        {
            throw new ServletException(ex);
        }
    }

    public void setRequestGlobals(RequestGlobals requestGlobals)
    {
        _requestGlobals = requestGlobals;
    }

    public void setWebRequestServicer(WebRequestServicer webRequestServicer)
    {
        _webRequestServicer = webRequestServicer;
    }
}