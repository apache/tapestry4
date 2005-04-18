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

package org.apache.tapestry.request;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.ApplicationServlet;

/**
 * This class encapsulates all the relevant data for one request cycle of an
 * {@link ApplicationServlet}. This includes:
 * <ul>
 * <li>{@link HttpServletRequest}
 * <li>{@link HttpServletResponse}
 * <li>{@link HttpSession}
 * </ul>
 * <p>
 * This is a limited and crippled version of the RequestContext as it was available in release 3.0,
 * that exists as a bridge for compatibility only. This saves developers from having to modify their
 * classes to have the {@link javax.servlet.http.HttpServletRequest}&nbsp;or
 * {@link org.apache.tapestry.web.WebRequest}injected into their pages, components, or services. It
 * will be removed in the next release of Tapestry.
 * <p>
 * Interestingly, with the Tapestry 4.0 architecture, a single instance of RequestContext can be
 * shared by all request cycles (that's because the request and response are, in fact, threaded
 * proxies).
 * 
 * @author Howard Lewis Ship
 * @deprecated To be removed in 4.1. Use injection to gain access to the necessary objects.
 */

public class RequestContext
{
    private final HttpServletRequest _request;

    private final HttpServletResponse _response;

    public RequestContext(HttpServletRequest request, HttpServletResponse response)
    {

        _request = request;
        _response = response;
    }

    /**
     * Returns the named parameter from the {@link HttpServletRequest}.
     * <p>
     * Use {@link #getParameters(String)}for parameters that may include multiple values.
     */

    public String getParameter(String name)
    {
        return _request.getParameter(name);
    }

    /**
     * Convienience method for getting a {@link HttpServletRequest}attribute.
     * 
     * @since 2.3
     */

    public Object getAttribute(String name)
    {
        return _request.getAttribute(name);
    }

    /**
     * For parameters that are, or are possibly, multi-valued, this method returns all the values as
     * an array of Strings.
     * 
     * @see #getParameter(String)
     */

    public String[] getParameters(String name)
    {
        return _request.getParameterValues(name);
    }

    public String[] getParameterNames()
    {
        Enumeration e = _request.getParameterNames();
        List names = new ArrayList();

        while (e.hasMoreElements())
            names.add(e.nextElement());

        int count = names.size();

        String[] result = new String[count];

        return (String[]) names.toArray(result);
    }

    /**
     * Returns the request which initiated the current request cycle. Note that the methods
     * {@link #getParameter(String)}and {@link #getParameters(String)}should be used, rather than
     * obtaining parameters directly from the request (since the RequestContext handles the
     * differences between normal and multipart/form requests).
     */

    public HttpServletRequest getRequest()
    {
        return _request;
    }

    public HttpServletResponse getResponse()
    {
        return _response;
    }

    /**
     * Returns the {@link HttpSession}, if necessary, invoking
     * {@link HttpServletRequest#getSession(boolean)}. However, this method will <em>not</em>
     * create a session.
     */

    public HttpSession getSession()
    {
        return _request.getSession(false);
    }

    /**
     * Like {@link #getSession()}, but forces the creation of the {@link HttpSession}, if
     * necessary.
     */

    public HttpSession createSession()
    {
        return _request.getSession(true);
    }

}