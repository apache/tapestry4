/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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

    }

    public TestEngineServiceLink(String name)
    {
        super(name);
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
            new String[] {
                Tapestry.SERVICE_QUERY_PARAMETER_NAME },
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
