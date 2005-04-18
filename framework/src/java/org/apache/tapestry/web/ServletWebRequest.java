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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.describe.DescriptionReceiver;

/**
 * Adapter from {@link javax.servlet.http.HttpServletRequest}&nbsp;to
 * {@link org.apache.tapestry.web.WebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ServletWebRequest implements WebRequest
{
    private final HttpServletRequest _servletRequest;

    private final HttpServletResponse _servletResponse;

    private WebSession _webSession;

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response)
    {
        Defense.notNull(request, "request");
        Defense.notNull(response, "response");

        _servletRequest = request;
        _servletResponse = response;
    }

    public List getParameterNames()
    {
        return WebUtils.toSortedList(_servletRequest.getParameterNames());
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

    public WebSession getSession(boolean create)
    {
        if (_webSession != null)
            return _webSession;

        HttpSession session = _servletRequest.getSession(create);

        if (session != null)
            _webSession = new ServletWebSession(session);

        return _webSession;
    }

    public List getAttributeNames()
    {
        return WebUtils.toSortedList(_servletRequest.getAttributeNames());
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

    public String getScheme()
    {
        return _servletRequest.getScheme();
    }

    public String getServerName()
    {
        return _servletRequest.getServerName();
    }

    public int getServerPort()
    {
        return _servletRequest.getServerPort();
    }

    public String getRequestURI()
    {
        return _servletRequest.getRequestURI();
    }

    public void forward(String URL)
    {
        if (HiveMind.isBlank(URL))
        {
            performForward("/");
            return;
        }

        boolean internal = !(URL.startsWith("/") || URL.indexOf("://") > 0);

        if (internal)
            performForward("/" + URL);
        else
            sendRedirect(URL);
    }

    private void sendRedirect(String URL)
    {
        String finalURL = _servletResponse.encodeRedirectURL(URL);

        try
        {
            _servletResponse.sendRedirect(finalURL);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(WebMessages.unableToRedirect(URL, ex), ex);
        }

    }

    private void performForward(String URL)
    {
        RequestDispatcher dispatcher = _servletRequest.getRequestDispatcher(URL);

        if (dispatcher == null)
            throw new ApplicationRuntimeException(WebMessages.unableToFindDispatcher(URL));

        try
        {
            dispatcher.forward(_servletRequest, _servletResponse);
        }
        catch (ServletException ex)
        {
            throw new ApplicationRuntimeException(WebMessages.unableToForward(URL, ex), ex);
        }
        catch (IOException ex)
        {
            throw new ApplicationRuntimeException(WebMessages.unableToForward(URL, ex), ex);
        }
    }

    /**
     * Returns {@link HttpServletRequest#getServletPath()}.
     */
    public String getActivationPath()
    {
        String servletPath = _servletRequest.getServletPath();
        String pathInfo = _servletRequest.getPathInfo();

        return pathInfo == null ? servletPath : servletPath + pathInfo;
    }

    public Locale getLocale()
    {
        return _servletRequest.getLocale();
    }

    public void describeTo(DescriptionReceiver receiver)
    {
        receiver.describeAlternate(_servletRequest);
    }
}