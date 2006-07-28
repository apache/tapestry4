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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;

import java.util.List;

import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.testng.annotations.Test;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

/**
 * Tests for {@link org.apache.tapestry.web.PortletWebRequest}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestPortletWebRequest extends BasePortletWebTestCase
{
    private PortletRequest newPortletRequest()
    {
        return newMock(PortletRequest.class);
    }

    public void testGetParameterNames()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getParameterNames()).andReturn(newEnumeration());
        
        replay();

        WebRequest wr = new PortletWebRequest(request);

        List l = wr.getParameterNames();

        checkList(l);

        verify();
    }

    public void testGetParameterValue()
    {
        String value = "William Orbit";
        
        PortletRequest request = newPortletRequest();

        expect(request.getParameter("artist")).andReturn(value);
        
        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertSame(value, wr.getParameterValue("artist"));

        verify();
    }

    public void testGetParameterValues()
    {
        String[] values = { "William Orbit", "Steely Dan" };
        
        PortletRequest request = newPortletRequest();

        expect(request.getParameterValues("artist")).andReturn(values);
        
        replay();

        WebRequest wr = new PortletWebRequest(request);
        
        assertSame(values, wr.getParameterValues("artist"));

        verify();
    }

    public void testGetContextPath()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getContextPath()).andReturn("/foo");

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("/foo", wr.getContextPath());

        verify();
    }

    public void testGetAttributeNames()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getAttributeNames()).andReturn(newEnumeration());

        replay();

        WebRequest wr = new PortletWebRequest(request);

        List l = wr.getAttributeNames();

        checkList(l);

        verify();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();
        PortletRequest request = newPortletRequest();

        expect(request.getAttribute("attr")).andReturn(attribute);

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertSame(attribute, wr.getAttribute("attr"));

        verify();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        PortletRequest request = newPortletRequest();

        request.setAttribute("name", attribute);

        replay();

        WebRequest wr = new PortletWebRequest(request);

        wr.setAttribute("name", attribute);

        verify();
    }

    public void testSetAttributeToNull()
    {
        PortletRequest request = newPortletRequest();

        request.removeAttribute("tonull");

        replay();

        WebRequest wr = new PortletWebRequest(request);

        wr.setAttribute("tonull", null);

        verify();
    }

    public void testGetSession()
    {
        PortletRequest request = newPortletRequest();

        PortletSession session = newMock(PortletSession.class);

        expect(request.getPortletSession(false)).andReturn(null);

        // Get it, doesn't exist, wreate false

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertNull(wr.getSession(false));

        verify();

        expect(request.getPortletSession(true)).andReturn(session);
        
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

    public void testGetScheme()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getScheme()).andReturn("http");
        
        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("http", wr.getScheme());

        verify();
    }

    public void testGetServerName()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getServerName()).andReturn("www.myhost.com");
        
        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("www.myhost.com", wr.getServerName());

        verify();
    }

    public void testGetServerPort()
    {
        PortletRequest request = newPortletRequest();

        expect(request.getServerPort()).andReturn(80);

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals(80, wr.getServerPort());

        verify();
    }

    public void testGetRequestURIUnsupported()
    {
        PortletRequest request = newPortletRequest();

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertEquals("<PortletRequest>", wr.getRequestURI());

        verify();
    }

    public void testForwardUnsupported()
    {
        PortletRequest request = newPortletRequest();

        replay();

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

        verify();
    }

    public void testGetPathInfo()
    {
        PortletRequest request = newPortletRequest();

        replay();

        WebRequest wr = new PortletWebRequest(request);

        assertNull(wr.getPathInfo());

        verify();
    }
}
