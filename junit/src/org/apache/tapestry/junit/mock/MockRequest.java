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

package org.apache.tapestry.junit.mock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *  Mock implementation of {@link javax.servlet.http.HttpServletRequest}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class MockRequest extends AttributeHolder implements HttpServletRequest
{
    /**
     * HTTP content type header name.
     */
    private static final String CONTENT_TYPE_HEADER_KEY = "Content-type";
    /**
     *  Map of String[].
     * 
     **/

    private Map _parameters = new HashMap();

    /**
     *  Map of String[]
     * 
     **/

    private Map _headers = new HashMap();

    private String _method = "GET";

    private String _contextPath;

    private MockContext _servletContext;
    private MockSession _session;
    private String _servletPath;
    private List _cookies = new ArrayList();
    private String _contentPath;

    /**
     *  This can be stored within the header, but doing it this way emulates a browser that 
     *  does not put the encoding in the request, which appears to be the general case. 
     **/
    private String _encoding = null;

    public MockRequest(MockContext servletContext, String servletPath)
    {
        _servletContext = servletContext;

        _contextPath = "/" + servletContext.getServletContextName();
        _servletPath = servletPath;

        _session = _servletContext.getSession();
    }

    public String getAuthType()
    {
        return null;
    }

    public Cookie[] getCookies()
    {
        return (Cookie[]) _cookies.toArray(new Cookie[_cookies.size()]);
    }

    public long getDateHeader(String arg0)
    {
        return 0;
    }

    public String getHeader(String key)
    {
        String getHeader = null;

        if (key != null)
        {
            getHeader = (String) _headers.get(key.toLowerCase());
        }
        return getHeader;
    }

    public Enumeration getHeaders(String name)
    {
        String[] headers = (String[]) _headers.get(name);

        if (headers == null)
            return Collections.enumeration(Collections.EMPTY_LIST);

        return Collections.enumeration(Arrays.asList(headers));
    }

    public Enumeration getHeaderNames()
    {
        return getEnumeration(_headers);
    }

    public int getIntHeader(String arg0)
    {
        return 0;
    }

    public String getMethod()
    {
        return _method;
    }

    public String getPathInfo()
    {
        return null;
    }

    public String getPathTranslated()
    {
        return null;
    }

    public String getContextPath()
    {
        return _contextPath;
    }

    public String getQueryString()
    {
        return null;
    }

    public String getRemoteUser()
    {
        return null;
    }

    public boolean isUserInRole(String arg0)
    {
        return false;
    }

    public Principal getUserPrincipal()
    {
        return null;
    }

    public String getRequestedSessionId()
    {
        return null;
    }

    public String getRequestURI()
    {
        return null;
    }

    public StringBuffer getRequestURL()
    {
        return null;
    }

    public String getServletPath()
    {
        return _servletPath;
    }

    public HttpSession getSession(boolean create)
    {
        if (create && _session == null)
            _session = _servletContext.createSession();

        return _session;
    }

    public HttpSession getSession()
    {
        return _session;
    }

    public boolean isRequestedSessionIdValid()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return false;
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return false;
    }

    public String getCharacterEncoding()
    {
        return _encoding;
    }

    public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
    {
        _encoding = arg0;
    }

    public int getContentLength()
    {
        return 0;
    }

    public String getContentType()
    {
        return getHeader(CONTENT_TYPE_HEADER_KEY);
    }

    public void setContentType(String contentType)
    {
        setHeader(CONTENT_TYPE_HEADER_KEY, contentType);
    }

    public ServletInputStream getInputStream() throws IOException
    {
        if (_contentPath == null)
            return null;

        return new MockServletInputStream(_contentPath);
    }

    public String getParameter(String name)
    {
        String[] values = getParameterValues(name);

        if (values == null || values.length == 0)
            return null;

        return values[0];
    }

    public Enumeration getParameterNames()
    {
        return Collections.enumeration(_parameters.keySet());
    }

    public String[] getParameterValues(String name)
    {
        return (String[]) _parameters.get(name);
    }

    /** 
     *  Not part of 2.1 API, not used by Tapestry.
     * 
     **/

    public Map getParameterMap()
    {
        return null;
    }

    public String getProtocol()
    {
        return null;
    }

    public String getScheme()
    {
        return "http";
    }

    public String getServerName()
    {
        return "junit-test";
    }

    public int getServerPort()
    {
        return 80;
    }

    public BufferedReader getReader() throws IOException
    {
        return null;
    }

    public String getRemoteAddr()
    {
        return null;
    }

    public String getRemoteHost()
    {
        return null;
    }

    private Locale _locale = Locale.ENGLISH;

    public Locale getLocale()
    {
        return _locale;
    }

    public void setLocale(Locale locale)
    {
        _locale = locale;
    }

    public Enumeration getLocales()
    {
        return Collections.enumeration(Collections.singleton(_locale));
    }

    public boolean isSecure()
    {
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String path)
    {
        return _servletContext.getRequestDispatcher(path);
    }

    public String getRealPath(String arg0)
    {
        return null;
    }

    public void setContextPath(String contextPath)
    {
        _contextPath = contextPath;
    }

    public void setMethod(String method)
    {
        _method = method;
    }

    public void setParameter(String name, String[] values)
    {
        _parameters.put(name, values);
    }

    public void setParameter(String name, String value)
    {
        setParameter(name, new String[] { value });
    }

    public void addCookie(Cookie cookie)
    {
        _cookies.add(cookie);
    }

    public void addCookies(Cookie[] cookies)
    {
        if (cookies == null)
            return;

        for (int i = 0; i < cookies.length; i++)
            addCookie(cookies[i]);
    }

    private void setHeader(String key, String value)
    {
        if (key != null)
        {
            _headers.put(key.toLowerCase(), value);
        }
    }

    /**
     *  Delegates this to the {@link org.apache.tapestry.junit.mock.MockSession}, if
     *  it exists.
     * 
     **/

    public void simulateFailover()
    {
        if (_session != null)
            _session.simulateFailover();
    }

    public String getContentPath()
    {
        return _contentPath;
    }

    public void setContentPath(String contentPath)
    {
        _contentPath = contentPath;
    }

    public int getRemotePort()
    {
        return 0;
    }

    public String getLocalName()
    {
        return null;
    }

    public String getLocalAddr()
    {
        return null;
    }

    public int getLocalPort()
    {
        return 0;
    }

}
