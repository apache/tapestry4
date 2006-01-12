// Copyright 2005, 2006 The Apache Software Foundation
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.ApplicationRuntimeException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestServletWebRequest extends BaseWebTestCase
{

    private HttpServletRequest newRequest()
    {
        return (HttpServletRequest)newMock(HttpServletRequest.class);
    }

    private HttpServletResponse newResponse()
    {
        return (HttpServletResponse)newMock(HttpServletResponse.class);
    }

    private RequestDispatcher newDispatcher()
    {
        return (RequestDispatcher)newMock(RequestDispatcher.class);
    }

    public void testGetParameterNames()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getParameterNames();
        setReturnValue(request, newEnumeration());

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetParameterValue()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        String value = "William Orbit";

        request.getParameter("artist");
        setReturnValue(request, value);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(value, wr.getParameterValue("artist"));

        verifyControls();
    }

    public void testGetParameterValues()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        String[] values = { "William Orbit", "Steely Dan" };

        request.getParameterValues("artist");
        setReturnValue(request, values);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(values, wr.getParameterValues("artist"));

        verifyControls();
    }

    public void testGetContextPath()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getContextPath();
        setReturnValue(request, "/foo");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo", wr.getContextPath());

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getAttributeNames();
        setReturnValue(request, newEnumeration());

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        Object attribute = new Object();

        request.getAttribute("attr");
        setReturnValue(request, attribute);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(attribute, wr.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        Object attribute = new Object();

        request.setAttribute("name", attribute);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.removeAttribute("tonull");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetSession()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();
        HttpSession session = newSession();

        trainGetSession(request, false, null);

        // Get it, doesn't exist, wreate false

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertNull(wr.getSession(false));

        verifyControls();

        trainGetSession(request, true, session);

        // #2: Get it, wreate is true, it is wreated.

        replayControls();

        WebSession cs = wr.getSession(true);

        verifyControls();

        // #3: Cached in local variable, make sure same
        // think returned.

        replayControls();

        assertSame(cs, wr.getSession(false));

        verifyControls();
    }

    private void trainGetSession(HttpServletRequest request, boolean create, HttpSession session)
    {
        request.getSession(create);
        setReturnValue(request, session);
    }

    private HttpSession newSession()
    {
        return (HttpSession)newMock(HttpSession.class);
    }

    public void testGetScheme()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getScheme();
        setReturnValue(request, "http");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("http", wr.getScheme());

        verifyControls();
    }

    public void testGetServerName()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getServerName();
        setReturnValue(request, "www.myhost.com");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("www.myhost.com", wr.getServerName());

        verifyControls();
    }

    public void testGetServerPort()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getServerPort();
        setReturnValue(request, 80);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals(80, wr.getServerPort());

        verifyControls();
    }

    public void testGetRequestURI()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getRequestURI();
        setReturnValue(request, "/foo/bar");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo/bar", wr.getRequestURI());

        verifyControls();
    }

    public void testForwardInternal()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        trainGetRequestDispatcher(request, "/local.html", dispatcher);

        dispatcher.forward(request, response);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("local.html");

        verifyControls();
    }

    public void testForwardNull()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        trainGetRequestDispatcher(request, "/", dispatcher);

        dispatcher.forward(request, response);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward(null);

        verifyControls();
    }

    private void trainGetRequestDispatcher(HttpServletRequest request, String path, RequestDispatcher dispatcher)
    {
        request.getRequestDispatcher(path);
        setReturnValue(request, dispatcher);
    }

    public void testForwardInternalNoDispatcher()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        trainGetRequestDispatcher(request, "/local.html", null);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        try
        {
            wr.forward("local.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Unable to find a request dispatcher for local resource '/local.html'.", ex.getMessage());
        }

        verifyControls();
    }

    public void testForwardInternalFailure()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();
        RequestDispatcher dispatcher = newDispatcher();

        Throwable t1 = new ServletException("Mock Servlet Exception");

        trainGetRequestDispatcher(request, "/servlet-exception.html", dispatcher);

        dispatcher.forward(request, response);
        setThrowable(dispatcher, t1);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        try
        {
            wr.forward("servlet-exception.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Unable to forward to local resource '/servlet-exception.html': Mock Servlet Exception", ex
                    .getMessage());
            assertSame(t1, ex.getRootCause());
        }

        verifyControls();

        Throwable t2 = new IOException("Mock IO Exception");

        trainGetRequestDispatcher(request, "/io-exception.html", dispatcher);

        dispatcher.forward(request, response);
        setThrowable(dispatcher, t2);

        replayControls();

        try
        {
            wr.forward("io-exception.html");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Unable to forward to local resource '/io-exception.html': Mock IO Exception", ex.getMessage());
            assertSame(t2, ex.getRootCause());
        }

        verifyControls();
    }

    public void testForwardExternal()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        response.encodeRedirectURL("http://foo.bar");
        setReturnValue(response, "<encoded: http://foo.bar>");

        response.sendRedirect("<encoded: http://foo.bar>");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("http://foo.bar");

        verifyControls();
    }

    public void testForwardExternalFailure()
        throws Exception
    {
        HttpServletRequest request = newRequest();
        MockControl responsec = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse)responsec.getMock();

        Throwable t = new IOException("Mock IO Exception");

        response.encodeRedirectURL("/");
        responsec.setReturnValue("<encoded: http://foo.bar>");

        response.sendRedirect("<encoded: http://foo.bar>");
        responsec.setThrowable(t);

        replayControls();

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

        verifyControls();
    }

    public void testGetActivationPath()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getServletPath();
        setReturnValue(request, "/foo");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo", wr.getActivationPath());

        verifyControls();
    }

    public void testGetPathInfo()
    {
        HttpServletRequest request = newRequest();
        HttpServletResponse response = newResponse();

        request.getPathInfo();
        setReturnValue(request, "bar/baz");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("bar/baz", wr.getPathInfo());

        verifyControls();
    }
}
