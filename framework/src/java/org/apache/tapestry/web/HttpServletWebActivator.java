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

package org.apache.tapestry.web;

import java.util.List;

import javax.servlet.http.HttpServlet;

import org.apache.tapestry.describe.DescriptionReceiver;

/**
 * Adapts {@link javax.servlet.http.HttpServlet}&nbsp; as
 * {@link org.apache.tapestry.web.WebActivator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class HttpServletWebActivator implements WebActivator
{
    private HttpServlet _httpServlet;

    public HttpServletWebActivator(HttpServlet servlet)
    {
        _httpServlet = servlet;
    }

    public String getActivatorName()
    {
        return _httpServlet.getServletName();
    }

    public List getInitParameterNames()
    {
        return WebUtils.toSortedList(_httpServlet.getInitParameterNames());
    }

    public String getInitParameterValue(String name)
    {
        return _httpServlet.getInitParameter(name);
    }

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_httpServlet);
    }

}