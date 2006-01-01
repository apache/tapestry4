// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.web.WebSession;

/**
 * Tests for {@link org.apache.tapestry.portlet.PortletWebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class PortletWebSessionTest extends BasePortletWebTestCase
{

    public void testGetAttributeNames()
    {
        PortletSession session = newSession();

        session.getAttributeNames();
        setReturnValue(session, newEnumeration());

        replayControls();

        WebSession ws = new PortletWebSession(session);

        List l = ws.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    private PortletSession newSession()
    {
        return (PortletSession) newMock(PortletSession.class);
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        PortletSession session = newSession();

        session.getAttribute("attr");
        setReturnValue(session, attribute);

        replayControls();

        WebSession ws = new PortletWebSession(session);

        assertSame(attribute, ws.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        PortletSession session = newSession();

        session.setAttribute("name", attribute);

        replayControls();

        WebSession ws = new PortletWebSession(session);

        ws.setAttribute("name", attribute);

        verifyControls();
    }

    public void testGetId()
    {
        PortletSession session = newSession();

        session.getId();
        setReturnValue(session, "abc");

        replayControls();

        WebSession ws = new PortletWebSession(session);

        assertEquals("abc", ws.getId());

        verifyControls();
    }

    public void testInvalidate()
    {
        PortletSession session = newSession();

        session.invalidate();

        replayControls();

        WebSession ws = new PortletWebSession(session);

        ws.invalidate();

        verifyControls();
    }
}