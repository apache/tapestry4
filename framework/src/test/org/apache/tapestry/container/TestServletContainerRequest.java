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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.container.ServletContainerRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestServletContainerRequest extends BaseContainerTestCase
{
    public void testGetParameterNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        List l = cr.getParameterNames();

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

        ContainerRequest cr = new ServletContainerRequest(request);

        assertSame(value, cr.getParameterValue("artist"));

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

        ContainerRequest cr = new ServletContainerRequest(request);

        assertSame(values, cr.getParameterValues("artist"));

        verifyControls();
    }

    public void testGetContextPath()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getContextPath();
        control.setReturnValue("/foo");

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        assertEquals("/foo", cr.getContextPath());

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        List l = cr.getAttributeNames();

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

        ContainerRequest cr = new ServletContainerRequest(request);

        assertSame(attribute, cr.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.setAttribute("name", attribute);

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        cr.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.removeAttribute("tonull");

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        cr.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetSession()
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        HttpSession session = (HttpSession) newMock(HttpSession.class);

        request.getSession(false);
        control.setReturnValue(null);

        // Get it, doesn't exist, create false

        replayControls();

        ContainerRequest cr = new ServletContainerRequest(request);

        assertNull(cr.getSession(false));

        verifyControls();

        request.getSession(true);
        control.setReturnValue(session);

        // #2: Get it, create is true, it is created.

        replayControls();

        ContainerSession cs = cr.getSession(true);

        verifyControls();

        // #3: Cached in local variable, make sure same
        // think returned.

        replayControls();

        assertSame(cs, cr.getSession(false));

        verifyControls();
    }
}