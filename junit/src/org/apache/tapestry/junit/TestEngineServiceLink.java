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

package org.apache.tapestry.junit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.tapestry.ApplicationServlet;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.engine.EngineServiceLink;
import org.apache.tapestry.request.RequestContext;

import org.apache.tapestry.junit.mock.MockContext;
import org.apache.tapestry.junit.mock.MockServletConfig;

public class TestEngineServiceLink extends TapestryTestCase
{
    private class TestRequest implements HttpServletRequest
    {
        public String getAuthType()
        {
            return null;
        }

        public String getContextPath()
        {
            return null;
        }

        public Cookie[] getCookies()
        {
            return null;
        }

        public long getDateHeader(String arg0)
        {
            return 0;
        }

        public String getHeader(String arg0)
        {
            return null;
        }

        public Enumeration getHeaderNames()
        {
            return null;
        }

        public Enumeration getHeaders(String arg0)
        {
            return null;
        }

        public int getIntHeader(String arg0)
        {
            return 0;
        }

        public String getMethod()
        {
            return null;
        }

        public String getPathInfo()
        {
            return null;
        }

        public String getPathTranslated()
        {
            return null;
        }

        public String getQueryString()
        {
            return null;
        }

        public String getRemoteUser()
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
            return null;
        }

        public HttpSession getSession()
        {
            return null;
        }

        public HttpSession getSession(boolean arg0)
        {
            return null;
        }

        public Principal getUserPrincipal()
        {
            return null;
        }

        public boolean isRequestedSessionIdFromCookie()
        {
            return false;
        }

        public boolean isRequestedSessionIdFromUrl()
        {
            return false;
        }

        public boolean isRequestedSessionIdFromURL()
        {
            return false;
        }

        public boolean isRequestedSessionIdValid()
        {
            return false;
        }

        public boolean isUserInRole(String arg0)
        {
            return false;
        }

        public Object getAttribute(String arg0)
        {
            return null;
        }

        public Enumeration getAttributeNames()
        {
            return null;
        }

        public String getCharacterEncoding()
        {
            return null;
        }

        public int getContentLength()
        {
            return 0;
        }

        public String getContentType()
        {
            return null;
        }

        public ServletInputStream getInputStream() throws IOException
        {
            return null;
        }

        public Locale getLocale()
        {
            return null;
        }

        public Enumeration getLocales()
        {
            return null;
        }

        public String getParameter(String arg0)
        {
            return null;
        }

        public Map getParameterMap()
        {
            return null;
        }

        public Enumeration getParameterNames()
        {
            return null;
        }

        public String[] getParameterValues(String arg0)
        {
            return null;
        }

        public String getProtocol()
        {
            return null;
        }

        public BufferedReader getReader() throws IOException
        {
            return null;
        }

        public String getRealPath(String arg0)
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

        public RequestDispatcher getRequestDispatcher(String arg0)
        {
            return null;
        }

        public String getScheme()
        {
            return "http";
        }

        public String getServerName()
        {
            return "testserver";
        }

        public int getServerPort()
        {
            return 80;
        }

        public boolean isSecure()
        {
            return false;
        }

        public void removeAttribute(String arg0)
        {
        }

        public void setAttribute(String arg0, Object arg1)
        {
        }

        public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException
        {
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

    private MockRequestCycle create(String servletPath) throws Exception
    {
        MockContext servletContext = new MockContext();
        MockServletConfig config = new MockServletConfig("servlet", servletContext);
        ApplicationServlet servlet = new ApplicationServlet();

        servlet.init(config);

        MockEngine engine = new MockEngine();
        engine.setServletPath(servletPath);

        HttpServletRequest request = new TestRequest();
        RequestContext context = new RequestContext(servlet, request, null);

        return new MockRequestCycle(engine, context);
    }

    public void testGetURL() throws Exception
    {
        MockRequestCycle c = create("/context/servlet");

        EngineServiceLink l = new EngineServiceLink(c, "myservice", null, null, true);

        assertEquals("/context/servlet?service=myservice", l.getURL());

        assertEquals("/context/servlet?service=myservice", c.getLastEncodedURL());

        assertEquals("/context/servlet", l.getURL(null, false));

        assertEquals("/context/servlet#anchor", l.getURL("anchor", false));

        assertEquals("/context/servlet?service=myservice#anchor", l.getURL("anchor", true));

        checkList(
            "parameterNames",
            new String[] { Tapestry.SERVICE_QUERY_PARAMETER_NAME },
            l.getParameterNames());
    }

    public void testGetAbsoluteURL() throws Exception
    {
        MockRequestCycle c = create("/context/servlet");

        EngineServiceLink l = new EngineServiceLink(c, "myservice", null, null, true);

        assertEquals("http://testserver/context/servlet?service=myservice", l.getAbsoluteURL());

        assertEquals("http://testserver/context/servlet?service=myservice", c.getLastEncodedURL());

        assertEquals(
            "http://testserver/context/servlet#anchor",
            l.getAbsoluteURL(null, null, 0, "anchor", false));

        assertEquals(
            "http://testserver/context/servlet?service=myservice#anchor",
            l.getAbsoluteURL(null, null, 0, "anchor", true));

        assertEquals(
            "frob://magic:77/context/servlet?service=myservice",
            l.getAbsoluteURL("frob", "magic", 77, null, true));
    }

    public void testContext() throws Exception
    {
        MockRequestCycle c = create("/alpha/bravo");

        EngineServiceLink l =
            new EngineServiceLink(
                c,
                "myservice",
                new String[] { "Alpha", "Bravo", "Tango" },
                null,
                true);

        checkList(
            "parameterNames",
            new String[] { Tapestry.SERVICE_QUERY_PARAMETER_NAME },
            l.getParameterNames());

        checkList(
            "service parameters values",
            new String[] { "myservice/Alpha/Bravo/Tango" },
            l.getParameterValues(Tapestry.SERVICE_QUERY_PARAMETER_NAME));

        assertEquals("/alpha/bravo?service=myservice/Alpha/Bravo/Tango", l.getURL());

    }

    public void testServiceParameters() throws Exception
    {
        MockRequestCycle c = create("/alpha/bravo");

        EngineServiceLink l =
            new EngineServiceLink(
                c,
                "myservice",
                null,
                new String[] { "One Two", "Three Four" },
                true);

        assertEquals("/alpha/bravo?service=myservice&sp=One+Two&sp=Three+Four", l.getURL());

        checkList(
            "parameterNames",
            new String[] {
                Tapestry.SERVICE_QUERY_PARAMETER_NAME,
                Tapestry.PARAMETERS_QUERY_PARAMETER_NAME },
            l.getParameterNames());

        checkList(
            "service parameters values",
            new String[] { "One Two", "Three Four" },
            l.getParameterValues(Tapestry.PARAMETERS_QUERY_PARAMETER_NAME));
    }

    public void testUnknownParameter() throws Exception
    {
        MockRequestCycle c = create("/context/servlet");

        EngineServiceLink l = new EngineServiceLink(c, "myservice", null, null, true);

        try
        {
            l.getParameterValues("unknown");

            unreachable();
        }
        catch (IllegalArgumentException ex)
        {
        }
    }

}
