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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestServletWebRequest extends BaseWebTestCase
{
    public void testGetParameterNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

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

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

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

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

        assertSame(values, wr.getParameterValues("artist"));

        verifyControls();
    }

    public void testGetContextPath()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getContextPath();
        control.setReturnValue("/foo");

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

        assertEquals("/foo", wr.getContextPath());

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

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

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

        assertSame(attribute, wr.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.setAttribute("name", attribute);

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

        wr.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.removeAttribute("tonull");

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

        wr.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetSession()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpSession session = (HttpSession) newMock(HttpSession.class);

        request.getSession(false);
        control.setReturnValue(null);

        // Get it, doesn't exist, wreate false

        replayControls();

        WebRequest wr = new ServletWebRequest(request);

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
}