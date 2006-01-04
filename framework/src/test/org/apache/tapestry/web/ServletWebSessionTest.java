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

import javax.servlet.http.HttpSession;

/**
 * Tests for {@link org.apache.tapestry.web.ServletWebSession}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ServletWebSessionTest extends BaseWebTestCase
{
    public void testGetAttributeNames()
    {
        HttpSession session = newSession();

        session.getAttributeNames();
        setReturnValue(session, newEnumeration());

        replayControls();

        WebSession ws = new ServletWebSession(session);

        List l = ws.getAttributeNames();

        checkList(l);

        verifyControls();
    }

    private HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    public void testGetAttribute()
    {
        Object attribute = new Object();

        HttpSession session = newSession();

        session.getAttribute("attr");
        setReturnValue(session, attribute);

        replayControls();

        WebSession ws = new ServletWebSession(session);

        assertSame(attribute, ws.getAttribute("attr"));

        verifyControls();
    }

    public void testSetAttribute()
    {
        Object attribute = new Object();

        HttpSession session = newSession();

        session.setAttribute("name", attribute);

        replayControls();

        WebSession ws = new ServletWebSession(session);

        ws.setAttribute("name", attribute);

        verifyControls();
    }

    public void testSetAttributeToNull()
    {
        HttpSession session = newSession();

        session.removeAttribute("tonull");

        replayControls();

        WebSession ws = new ServletWebSession(session);

        ws.setAttribute("tonull", null);

        verifyControls();
    }

    public void testGetId()
    {
        HttpSession session = newSession();

        session.getId();
        setReturnValue(session, "abc");

        replayControls();

        WebSession ws = new ServletWebSession(session);

        assertEquals("abc", ws.getId());

        verifyControls();
    }

    public void testInvalidate()
    {
        HttpSession session = newSession();

        session.invalidate();

        replayControls();

        WebSession ws = new ServletWebSession(session);

        ws.invalidate();

        verifyControls();
    }
}