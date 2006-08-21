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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.tapestry.web.WebContext;
import org.testng.annotations.Test;

import javax.portlet.PortletContext;

/**
 * @author Howard M. Lewis Ship
 */
@Test
public class TestPortletWebContext extends BasePortletWebTestCase
{

    public void testGetInitParameterNames()
    {
        PortletContext context = newMock(PortletContext.class);

        expect(context.getInitParameterNames()).andReturn(newEnumeration());

        replay();

        WebContext wc = new PortletWebContext(context);

        List l = wc.getInitParameterNames();

        checkList(l);

        verify();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";
        
        PortletContext context = newMock(PortletContext.class);
        
        expect(context.getInitParameter("artist")).andReturn(value);

        replay();

        WebContext wc = new PortletWebContext(context);

        assertSame(value, wc.getInitParameterValue("artist"));

        verify();
    }

    public void testGetAttributeNames()
    {
        PortletContext context = newMock(PortletContext.class);

        expect(context.getAttributeNames()).andReturn(newEnumeration());
        
        replay();

        WebContext wc = new PortletWebContext(context);
        
        List l = wc.getAttributeNames();

        checkList(l);

        verify();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        PortletContext context = newMock(PortletContext.class);

        expect(context.getAttribute("attr")).andReturn(attribute);

        replay();

        WebContext wc = new PortletWebContext(context);

        assertSame(attribute, wc.getAttribute("attr"));

        verify();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        PortletContext context = newMock(PortletContext.class);
        
        context.setAttribute("name", attribute);

        replay();

        WebContext wc = new PortletWebContext(context);

        wc.setAttribute("name", attribute);

        verify();
    }

    public void testSetAttributeToNull()
    {
        PortletContext context = newMock(PortletContext.class);

        context.removeAttribute("tonull");

        replay();

        WebContext wc = new PortletWebContext(context);

        wc.setAttribute("tonull", null);

        verify();
    }

    public void testGetResource() throws Exception
    {
        URL url = new URL("http://tapestry.apache.org/");

        PortletContext context = newMock(PortletContext.class);
        
        expect(context.getResource("/tapestry")).andReturn(url);
        
        replay();

        WebContext wc = new PortletWebContext(context);
        
        assertSame(url, wc.getResource("/tapestry"));
        
        verify();
    }

    public void testGetResourceFailure() throws Exception
    {
        Throwable t = new MalformedURLException("Like this ever happens.");

        PortletContext context = newMock(PortletContext.class);

        expect(context.getResource("/tapestry")).andThrow(t);
        
        replay();
        
        WebContext wc = new PortletWebContext(context);

        assertNull(wc.getResource("/tapestry"));
        
        verify();
    }
}
