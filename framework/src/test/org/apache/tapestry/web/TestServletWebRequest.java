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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.ApplicationRuntimeException;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestServletWebRequest extends BaseWebTestCase
{
    private HttpServletRequest newHttpRequest()
    {
        return (HttpServletRequest) newMock(HttpServletRequest.class);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse) newMock(HttpServletResponse.class);
    }

    private RequestDispatcher newDispatcher()
    {
        return (RequestDispatcher) newMock(RequestDispatcher.class);
    }

    public void testGetParameterNames()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getParameterNames()).andReturn(newEnumeration());
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getParameterNames();

        checkList(l);

        verify();
    }

    public void testGetParameterValue()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        String value = "William Orbit";

        expect(request.getParameter("artist")).andReturn(value);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(value, wr.getParameterValue("artist"));

        verify();
    }

    public void testGetParameterValues()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        String[] values =
        { "William Orbit", "Steely Dan" };

        expect(request.getParameterValues("artist")).andReturn(values);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(values, wr.getParameterValues("artist"));

        verify();
    }

    public void testGetContextPath()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getContextPath()).andReturn("/foo");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo", wr.getContextPath());

        verify();
    }

    public void testGetAttributeNames()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getAttributeNames()).andReturn(newEnumeration());
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getAttributeNames();

        checkList(l);

        verify();
    }

    public void testGetAttribute()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        Object attribute = new Object();

        expect(request.getAttribute("attr")).andReturn(attribute);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(attribute, wr.getAttribute("attr"));

        verify();
    }

    public void testSetAttribute()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        Object attribute = new Object();

        request.setAttribute("name", attribute);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("name", attribute);

        verify();
    }

    public void testSetAttributeToNull()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        request.removeAttribute("tonull");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("tonull", null);

        verify();
    }

    public void testGetSession()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        HttpSession session = newSession();

        trainGetSession(request, false, null);

        // Get it, doesn't exist, wreate false

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertNull(wr.getSession(false));

        verify();

        trainGetSession(request, true, session);

        // #2: Get it, wreate is true, it is wreated.

        replay();

        WebSession cs = wr.getSession(true);

        verify();

        // #3: Cached in local variable, make sure same
        // think returned.

        replay();

        assertSame(cs, wr.getSession(false));

        verify();
    }

    private void trainGetSession(HttpServletRequest request, boolean create, HttpSession session)
    {
        expect(request.getSession(create)).andReturn(session);
    }

    private HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    public void testGetScheme()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getScheme()).andReturn("http");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("http", wr.getScheme());

        verify();
    }

    public void testGetServerName()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getServerName()).andReturn("www.myhost.com");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("www.myhost.com", wr.getServerName());

        verify();
    }

    public void testGetServerPort()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getServerPort()).andReturn(80);
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals(80, wr.getServerPort());

        verify();
    }

    public void testGetRequestURI()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getRequestURI()).andReturn("/foo/bar");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo/bar", wr.getRequestURI());

        verify();
    }

    public void testForwardInternal() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        trainGetRequestDispatcher(request, "/local.html", dispatcher);

        dispatcher.forward(request, response);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("local.html");

        verify();
    }

    public void testForwardNull() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        trainGetRequestDispatcher(request, "/", dispatcher);

        dispatcher.forward(request, response);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward(null);

        verify();
    }

    private void trainGetRequestDispatcher(HttpServletRequest request, String path,
            RequestDispatcher dispatcher)
    {
        expect(request.getRequestDispatcher(path)).andReturn(dispatcher);
    }

    public void testForwardInternalNoDispatcher() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        trainGetRequestDispatcher(request, "/local.html", null);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        try
        {
            wr.forward("local.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to find a request dispatcher for local resource '/local.html'.",
                    ex.getMessage());
        }

        verify();
    }

    public void testForwardInternalFailure() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        Throwable t1 = new ServletException("Mock Servlet Exception");

        trainGetRequestDispatcher(request, "/servlet-exception.html", dispatcher);

        dispatcher.forward(request, response);
        expectLastCall().andThrow(t1);

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        try
        {
            wr.forward("servlet-exception.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to forward to local resource '/servlet-exception.html': Mock Servlet Exception",
                    ex.getMessage());
            assertSame(t1, ex.getRootCause());
        }

        verify();

        Throwable t2 = new IOException("Mock IO Exception");

        trainGetRequestDispatcher(request, "/io-exception.html", dispatcher);

        dispatcher.forward(request, response);
        expectLastCall().andThrow(t2);

        replay();

        try
        {
            wr.forward("io-exception.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Unable to forward to local resource '/io-exception.html': Mock IO Exception",
                    ex.getMessage());
            assertSame(t2, ex.getRootCause());
        }

        verify();
    }

    public void testForwardExternal() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(response.encodeRedirectURL("http://foo.bar")).andReturn("<encoded: http://foo.bar>");
        
        response.sendRedirect("<encoded: http://foo.bar>");

        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("http://foo.bar");

        verify();
    }

    public void testForwardExternalFailure() throws Exception
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = (HttpServletResponse)newMock(HttpServletResponse.class);

        Throwable t = new IOException("Mock IO Exception");

        expect(response.encodeRedirectURL("/")).andReturn("<encoded: http://foo.bar>");
        
        response.sendRedirect("<encoded: http://foo.bar>");
        expectLastCall().andThrow(t);
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        try
        {
            wr.forward("/");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Unable to redirect to /: Mock IO Exception", ex.getMessage());
            assertSame(t, ex.getRootCause());
        }

        verify();
    }

    public void testGetActivationPath()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getServletPath()).andReturn("/foo");
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo", wr.getActivationPath());

        verify();
    }

    public void testGetPathInfo()
    {
        HttpServletRequest request = newHttpRequest();
        HttpServletResponse response = newResponse();

        expect(request.getPathInfo()).andReturn("bar/baz");
        
        replay();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("bar/baz", wr.getPathInfo());

        verify();
    }
}