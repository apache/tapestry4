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

package org.apache.tapestry.container;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.util.Defense;

/**
 * Adapter from {@link javax.servlet.http.HttpServletRequest}&nbsp;to
 * {@link org.apache.tapestry.container.ContainerRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ServletContainerRequest implements ContainerRequest
{
    private final HttpServletRequest _servletRequest;
    
    private ContainerSession _containerSession;

    public ServletContainerRequest(HttpServletRequest request)
    {
        Defense.notNull(request, "request");

        _servletRequest = request;
    }

    public List getParameterNames()
    {
        return ContainerUtils.toSortedList(_servletRequest.getParameterNames());
    }

    public String getParameterValue(String parameterName)
    {
        return _servletRequest.getParameter(parameterName);
    }

    public String[] getParameterValues(String parameterName)
    {
        return _servletRequest.getParameterValues(parameterName);
    }

    public String getContextPath()
    {
        return _servletRequest.getContextPath();
    }

    public ContainerSession getSession(boolean create)
    {
        if (_containerSession != null)
            return _containerSession;

        HttpSession session = _servletRequest.getSession(create);

        if (session != null)
            _containerSession = new ServletContainerSession(session);

        return _containerSession;
    }

    public List getAttributeNames()
    {
        return ContainerUtils.toSortedList(_servletRequest.getAttributeNames());
    }

    public Object getAttribute(String name)
    {
        return _servletRequest.getAttribute(name);
    }

    public void setAttribute(String name, Object attribute)
    {
        if (attribute == null)
            _servletRequest.removeAttribute(name);
        else
            _servletRequest.setAttribute(name, attribute);
    }
}