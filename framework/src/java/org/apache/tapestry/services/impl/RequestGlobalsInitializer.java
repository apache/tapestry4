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

package org.apache.tapestry.services.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.services.RequestServicerFilter;
import org.apache.tapestry.services.RequestGlobals;

/**
 * A {@link org.apache.tapestry.services.RequestServicerFilter} threaded early onto the pipeline,
 * which invokes {@link org.apache.tapestry.services.ServletInfo#store(HttpServletRequest, HttpServletResponse)}
 * so that other services can obtain the current thread's request and response.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class RequestGlobalsInitializer implements RequestServicerFilter
{
    private RequestGlobals _requestGlobals;

    public void setRequestGlobals(RequestGlobals info)
    {
        _requestGlobals = info;
    }

    public void service(
        HttpServletRequest request,
        HttpServletResponse response,
        RequestServicer servicer)
        throws IOException, ServletException
    {
        _requestGlobals.store(request, response);

        servicer.service(request, response);
    }

}
