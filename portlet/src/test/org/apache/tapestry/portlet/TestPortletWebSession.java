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

import javax.portlet.PortletSession;

import org.apache.tapestry.portlet.PortletWebSession;
import org.apache.tapestry.web.WebSession;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestPortletWebSession extends BasePortletWebTestCase
{

    public void testGetAttributeNames()
    {
        MockControl control = newControl(PortletSession.class);
        PortletSession session = (PortletSession) control.getMock();

        session.getAttributeNames();
        control.setReturnValue(newEnumeration());

        replayControls();

        WebSession ws = new PortletWebSession(session);

        List l = ws.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(PortletSession.class);
        PortletSession session = (PortletSession) control.getMock();

        session.getAttribute("attr");
        control.setReturnValue(attribute);

        replayControls();

        WebSession ws = new PortletWebSession(session);

        assertSame(attribute, ws.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        MockControl control = newControl(PortletSession.class);
        PortletSession session = (PortletSession) control.getMock();

        session.setAttribute("name", attribute);

        replayControls();

        WebSession ws = new PortletWebSession(session);

        ws.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        MockControl control = newControl(PortletSession.class);
        PortletSession session = (PortletSession) control.getMock();

        session.removeAttribute("tonull");

        replayControls();

        WebSession ws = new PortletWebSession(session);

        ws.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetId()
    {
        MockControl control = newControl(PortletSession.class);
        PortletSession session = (PortletSession) control.getMock();

        session.getId();
        control.setReturnValue("abc");

        replayControls();

        WebSession ws = new PortletWebSession(session);

        assertEquals("abc", ws.getId());

        verifyControls();
    }
}