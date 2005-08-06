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

package org.apache.tapestry.portlet;

import java.util.List;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.web.PortletWebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestPortletWebRequest extends BasePortletWebTestCase
{
    private PortletRequest newRequest()
    {
        return (PortletRequest) newMock(PortletRequest.class);
    }

    public void testGetParameterNames()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        List l = wr.getParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetParameterValue()
    {
        String value = "William Orbit";

        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getParameter("artist");
        control.setReturnValue(value);

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertSame(value, wr.getParameterValue("artist"));

        verifyControls();
    }

    public void testGetParameterValues()
    {
        String[] values =
        { "William Orbit", "Steely Dan" };
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getParameterValues("artist");
        control.setReturnValue(values);

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertSame(values, wr.getParameterValues("artist"));

        verifyControls();
    }

    public void testGetContextPath()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getContextPath();
        control.setReturnValue("/foo");

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("/foo", wr.getContextPath());

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        List l = wr.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getAttribute("attr");
        control.setReturnValue(attribute);

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertSame(attribute, wr.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.setAttribute("name", attribute);

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        wr.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.removeAttribute("tonull");

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        wr.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetSession()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        PortletSession session = (PortletSession) newMock(PortletSession.class);

        request.getPortletSession(false);
        control.setReturnValue(null);

        // Get it, doesn't exist, wreate false

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertNull(wr.getSession(false));

        verifyControls();

        request.getPortletSession(true);
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
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getScheme();
        control.setReturnValue("http");

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("http", wr.getScheme());

        verifyControls();
    }

    public void testGetServerName()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getServerName();
        control.setReturnValue("www.myhost.com");

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("www.myhost.com", wr.getServerName());

        verifyControls();
    }

    public void testGetServerPort()
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getServerPort();
        control.setReturnValue(80);

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals(80, wr.getServerPort());

        verifyControls();
    }

    public void testGetRequestURIUnsupported()
    {
        PortletRequest request = newRequest();

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("<PortletRequest>", wr.getRequestURI());

        verifyControls();
    }

    public void testForwardUnsupported()
    {
        PortletRequest request = newRequest();

        replayControls();

        WebRequest wr = new PortletWebRequest(request);

        try
        {
            wr.forward(null);
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Expected.
        }

        verifyControls();
    }

}