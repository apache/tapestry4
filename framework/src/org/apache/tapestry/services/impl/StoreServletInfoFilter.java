//Copyright 2004 The Apache Software Foundation
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package org.apache.tapestry.services.impl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tapestry.services.RequestServicer;
import org.apache.tapestry.services.RequestServicerFilter;
import org.apache.tapestry.services.ServletInfo;

/**
 * {@link org.apache.tapestry.services.RequestServicerFilter} used
 * to store the current request and response into
 * {@link org.apache.tapestry.services.ServletInfo} where it
 * can be accessed by other services.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class StoreServletInfoFilter implements RequestServicerFilter
{
    private ServletInfo _servletInfo;

    public void service(
        HttpServletRequest request,
        HttpServletResponse response,
        RequestServicer servicer)
        throws IOException, ServletException
    {
        _servletInfo.store(request, response);

        servicer.service(request, response);
    }

    public void setServletInfo(ServletInfo info)
    {
        _servletInfo = info;
    }

}
