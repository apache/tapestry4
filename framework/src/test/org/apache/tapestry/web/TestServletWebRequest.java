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
 * @since 3.1
 */
public class TestServletWebRequest extends BaseWebTestCase
{
    private HttpServletRequest newRequest()
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
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getParameterNames();
        control.setReturnValue(newEnumeration());

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetParameterValue()
    {
        String value = "William Orbit";

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getParameter("artist");
        control.setReturnValue(value);

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(value, wr.getParameterValue("artist"));

        verifyControls();
    }

    public void testGetParameterValues()
    {
        String[] values =
        { "William Orbit", "Steely Dan" };
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getParameterValues("artist");
        control.setReturnValue(values);

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(values, wr.getParameterValues("artist"));

        verifyControls();
    }

    public void testGetContextPath()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getContextPath();
        control.setReturnValue("/foo");

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo", wr.getContextPath());

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getAttributeNames();
        control.setReturnValue(newEnumeration());

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        List l = wr.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getAttribute("attr");
        control.setReturnValue(attribute);

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertSame(attribute, wr.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.setAttribute("name", attribute);

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.removeAttribute("tonull");

        HttpServletResponse response = newResponse();

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetSession()
    {
        HttpServletResponse response = newResponse();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpSession session = (HttpSession) newMock(HttpSession.class);

        request.getSession(false);
        control.setReturnValue(null);

        // Get it, doesn't exist, wreate false

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertNull(wr.getSession(false));

        verifyControls();

        request.getSession(true);
        control.setReturnValue(session);

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

    public void testGetScheme()
    {
        HttpServletResponse response = newResponse();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getScheme();
        control.setReturnValue("http");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("http", wr.getScheme());

        verifyControls();
    }

    public void testGetServerName()
    {
        HttpServletResponse response = newResponse();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getServerName();
        control.setReturnValue("www.myhost.com");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("www.myhost.com", wr.getServerName());

        verifyControls();
    }

    public void testGetServerPort()
    {
        HttpServletResponse response = newResponse();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getServerPort();
        control.setReturnValue(80);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals(80, wr.getServerPort());

        verifyControls();
    }

    public void testGetRequestURI()
    {
        HttpServletResponse response = newResponse();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getRequestURI();
        control.setReturnValue("/foo/bar");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        assertEquals("/foo/bar", wr.getRequestURI());

        verifyControls();
    }

    public void testForwardInternal() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();

        RequestDispatcher dispatcher = newDispatcher();

        request.getRequestDispatcher("/local.html");
        control.setReturnValue(dispatcher);

        dispatcher.forward(request, response);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("local.html");

        verifyControls();
    }

    public void testForwardNull() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();

        RequestDispatcher dispatcher = newDispatcher();

        request.getRequestDispatcher("/");
        control.setReturnValue(dispatcher);

        dispatcher.forward(request, response);

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward(null);

        verifyControls();
    }

    public void testForwardInternalNoDispatcher() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();
        request.getRequestDispatcher("/local.html");
        control.setReturnValue(null);

        replayControls();

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

        verifyControls();
    }

    public void testForwardInternalFailure() throws Exception
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpServletResponse response = newResponse();

        MockControl dispatcherc = newControl(RequestDispatcher.class);
        RequestDispatcher dispatcher = (RequestDispatcher) dispatcherc.getMock();

        Throwable t1 = new ServletException("Mock Servlet Exception");

        request.getRequestDispatcher("/servlet-exception.html");
        control.setReturnValue(dispatcher);

        dispatcher.forward(request, response);
        dispatcherc.setThrowable(t1);

        replayControls();

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

        verifyControls();

        Throwable t2 = new IOException("Mock IO Exception");

        request.getRequestDispatcher("/io-exception.html");
        control.setReturnValue(dispatcher);

        dispatcher.forward(request, response);
        dispatcherc.setThrowable(t2);

        replayControls();

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

        verifyControls();
    }

    public void testForwardExternal() throws Exception
    {
        HttpServletRequest request = newRequest();
        MockControl responsec = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) responsec.getMock();

        response.encodeRedirectURL("http://foo.bar");
        responsec.setReturnValue("<encoded: http://foo.bar>");

        response.sendRedirect("<encoded: http://foo.bar>");

        replayControls();

        WebRequest wr = new ServletWebRequest(request, response);

        wr.forward("http://foo.bar");

        verifyControls();
    }

    public void testForwardExternalFailure() throws Exception
    {
        HttpServletRequest request = newRequest();
        MockControl responsec = newControl(HttpServletResponse.class);
        HttpServletResponse response = (HttpServletResponse) responsec.getMock();

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
}