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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.servlet.ServletContext;

import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.container.ServletContainerContext}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestServletContainerContext extends BaseContainerTestCase
{

    public void testGetInitParameterNames()
    {
        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getInitParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        List l = cc.getInitParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";

        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getInitParameter("artist");
        control.setReturnValue(value);

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        assertSame(value, cc.getInitParameterValue("artist"));

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        List l = cc.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getAttribute("attr");
        control.setReturnValue(attribute);

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        assertSame(attribute, cc.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.setAttribute("name", attribute);

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        cc.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.removeAttribute("tonull");

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        cc.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetResource() throws Exception
    {
        URL url = new URL("http://jakarta.apache.org/tapestry");

        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getResource("/tapestry");
        control.setReturnValue(url);

        replayControls();

        ContainerContext cc = new ServletContainerContext(context);

        assertSame(url, cc.getResource("/tapestry"));

        verifyControls();
    }

    public void testGetResourceFailure() throws Exception
    {
        Throwable t = new MalformedURLException("Like this ever happens.");

        MockControl control = newControl(ServletContext.class);
        ServletContext context = (ServletContext) control.getMock();

        context.getResource("/tapestry");
        control.setThrowable(t);

        replayControls();
     
        interceptLogging(ServletContainerContext.class.getName());
        
        ContainerContext cc = new ServletContainerContext(context);

        assertNull(cc.getResource("/tapestry"));

        verifyControls();

        assertLoggedMessage("Error getting context resource '/tapestry': Like this ever happens.");
    }
}