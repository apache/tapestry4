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

package org.apache.tapestry.record;

import java.util.Collection;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link SessionPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestSessionPropertyPersistenceStrategy extends HiveMindTestCase
{
    private HttpServletRequest newRequest(boolean create, HttpSession session)
    {
        MockControl control = newControl(HttpServletRequest.class);
        HttpServletRequest request = (HttpServletRequest) control.getMock();

        request.getSession(create);

        control.setReturnValue(session);

        return request;
    }

    private HttpSession newSession()
    {
        return (HttpSession) newMock(HttpSession.class);
    }

    private HttpSession newSession(String attributeName, Object value)
    {
        MockControl control = newControl(HttpSession.class);
        HttpSession session = (HttpSession) control.getMock();

        session.getAttributeNames();

        control.setReturnValue(Collections.enumeration(Collections.singletonList(attributeName)));

        if (value != null)
        {
            session.getAttribute(attributeName);
            control.setReturnValue(value);
        }

        return session;
    }

    private HttpSession newSession(String attributeName, boolean remove)
    {
        MockControl control = newControl(HttpSession.class);
        HttpSession session = (HttpSession) control.getMock();

        session.getAttributeNames();

        control.setReturnValue(Collections.enumeration(Collections.singletonList(attributeName)));

        if (remove)
            session.removeAttribute(attributeName);

        return session;
    }

    public void testStorePageProperty()
    {
        HttpSession session = newSession();
        HttpServletRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("myapp,Home,foo", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationName("myapp");
        s.setRequest(request);

        s.store("Home", null, "foo", value);

        verifyControls();
    }

    public void testClearPageProperty()
    {
        HttpSession session = newSession();
        HttpServletRequest request = newRequest(true, session);

        session.removeAttribute("myapp,Help,bar");

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationName("myapp");
        s.setRequest(request);

        s.store("Help", null, "bar", null);

        verifyControls();
    }

    public void testStoreComponentProperty()
    {
        HttpSession session = newSession();
        HttpServletRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("gloop,Nerf,zip.zap,spaz", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationName("gloop");
        s.setRequest(request);

        s.store("Nerf", "zip.zap", "spaz", value);

        verifyControls();
    }

    public void testGetStoredChangesNoSession()
    {
        HttpServletRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        assertTrue(s.getStoredChanges("Foo", null).isEmpty());

        verifyControls();
    }

    public void testGetStoreChangesNoMatch()
    {
        HttpSession session = newSession("myapp,Home,foo,bar", null);
        HttpServletRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationName("myapp");

        Collection actual = s.getStoredChanges("Help", null);

        assertTrue(actual.isEmpty());

        verifyControls();
    }

    public void testGetStoredPageProperty()
    {
        Object value = new Object();
        HttpSession session = newSession("myapp,Help,bar", value);
        HttpServletRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationName("myapp");

        Collection actual = s.getStoredChanges("Help", null);

        assertEquals(1, actual.size());

        IPageChange pc = (IPageChange) actual.iterator().next();

        assertNull(pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verifyControls();
    }

    public void testGetStoredComponentProperty()
    {
        Object value = new Object();
        HttpSession session = newSession("myapp,Help,zap.biff,bar", value);
        HttpServletRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationName("myapp");

        Collection actual = s.getStoredChanges("Help", null);

        assertEquals(1, actual.size());

        IPageChange pc = (IPageChange) actual.iterator().next();

        assertEquals("zap.biff", pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verifyControls();
    }

    public void testDiscardChangesNoSession()
    {
        HttpServletRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo", null);

        verifyControls();
    }

    public void testDiscardChangesNoMatch()
    {
        HttpSession session = newSession("myapp,Home,foo", false);
        HttpServletRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo", null);
        verifyControls();
    }

    public void testDiscardChangesWithMatch()
    {
        HttpSession session = newSession("myapp,Home,foo", true);
        HttpServletRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationName("myapp");

        s.discardStoredChanges("Home", null);

        verifyControls();
    }
}