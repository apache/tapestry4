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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.portlet.PortletContext;

import org.apache.tapestry.web.WebContext;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 */
public class TestPortletWebContext extends BasePortletWebTestCase
{

    public void testGetInitParameterNames()
    {
        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getInitParameterNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebContext wc = new PortletWebContext(context);

        List l = wc.getInitParameterNames();

        checkList(l);

        verifyControls();
    }

    public void testGetInitParameterValue()
    {
        String value = "William Orbit";

        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getInitParameter("artist");
        control.setReturnValue(value);

        replayControls();

        WebContext wc = new PortletWebContext(context);

        assertSame(value, wc.getInitParameterValue("artist"));

        verifyControls();
    }

    public void testGetAttributeNames()
    {
        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebContext wc = new PortletWebContext(context);

        List l = wc.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getAttribute("attr");
        control.setReturnValue(attribute);

        replayControls();

        WebContext wc = new PortletWebContext(context);

        assertSame(attribute, wc.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.setAttribute("name", attribute);

        replayControls();

        WebContext wc = new PortletWebContext(context);

        wc.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.removeAttribute("tonull");

        replayControls();

        WebContext wc = new PortletWebContext(context);

        wc.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetResource() throws Exception
    {
        URL url = new URL("http://jakarta.apache.org/tapestry");

        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getResource("/tapestry");
        control.setReturnValue(url);

        replayControls();

        WebContext wc = new PortletWebContext(context);

        assertSame(url, wc.getResource("/tapestry"));

        verifyControls();
    }

    public void testGetResourceFailure() throws Exception
    {
        Throwable t = new MalformedURLException("Like this ever happens.");

        MockControl control = newControl(PortletContext.class);
        PortletContext context = (PortletContext) control.getMock();

        context.getResource("/tapestry");
        control.setThrowable(t);

        replayControls();

        interceptLogging(PortletWebContext.class.getName());

        WebContext wc = new PortletWebContext(context);

        assertNull(wc.getResource("/tapestry"));

        verifyControls();

        assertLoggedMessage("Error getting portlet context resource '/tapestry': Like this ever happens.");
    }
}