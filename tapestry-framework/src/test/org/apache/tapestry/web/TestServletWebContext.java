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
import org.testng.annotations.Test;

import javax.servlet.ServletContext;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebContext}.
 * 
 */
@Test(sequential = true)
public class TestServletWebContext extends BaseWebTestCase
{

    public void testGetInitParameterNames()
    {
        ServletContext context = newMock(ServletContext.class);

        expect(context.getInitParameterNames()).andReturn(newEnumeration());

        replay();

        WebContext wc = new ServletWebContext(context);

        List l = wc.getInitParameterNames();

        checkList(l);

        verify();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";

        ServletContext context = newMock(ServletContext.class);

        expect(context.getInitParameter("artist")).andReturn(value);

        replay();

        WebContext wc = new ServletWebContext(context);

        assertSame(value, wc.getInitParameterValue("artist"));

        verify();
    }

    public void testGetAttributeNames()
    {
        ServletContext context = newMock(ServletContext.class);

        expect(context.getAttributeNames()).andReturn(newEnumeration());

        replay();

        WebContext wc = new ServletWebContext(context);

        List l = wc.getAttributeNames();

        checkList(l);

        verify();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        ServletContext context = newMock(ServletContext.class);

        expect(context.getAttribute("attr")).andReturn(attribute);

        replay();

        WebContext wc = new ServletWebContext(context);

        assertSame(attribute, wc.getAttribute("attr"));

        verify();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        ServletContext context = newMock(ServletContext.class);

        context.setAttribute("name", attribute);

        replay();

        WebContext wc = new ServletWebContext(context);

        wc.setAttribute("name", attribute);

        verify();
    }

    public void testSetAttributeToNull()
    {
        ServletContext context = newMock(ServletContext.class);

        context.removeAttribute("tonull");

        replay();

        WebContext wc = new ServletWebContext(context);

        wc.setAttribute("tonull", null);

        verify();
    }

    public void testGetResource() throws Exception
    {
        URL url = new URL("http://jakarta.apache.org/tapestry");

        ServletContext context = newMock(ServletContext.class);

        expect(context.getResource("/tapestry")).andReturn(url);

        replay();

        WebContext wc = new ServletWebContext(context);

        assertSame(url, wc.getResource("/tapestry"));

        verify();
    }

    public void testGetResourceFailure() throws Exception
    {
        Throwable t = new MalformedURLException("Like this ever happens.");

        ServletContext context = newMock(ServletContext.class);

        expect(context.getResource("/tapestry")).andThrow(t);

        replay();

        // interceptLogging(ServletWebContext.class.getName());

        WebContext wc = new ServletWebContext(context);

        assertNull(wc.getResource("/tapestry"));

        verify();
        
        // assertLoggedMessage("Error getting context resource '/tapestry': Like this ever happens.");
    }
}