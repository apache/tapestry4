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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.easymock.MockControl;

/**
 * Tests for {@link SessionPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestSessionPropertyPersistenceStrategy extends HiveMindTestCase
{
    private WebRequest newRequest(boolean create, WebSession session)
    {
        MockControl control = newControl(WebRequest.class);
        WebRequest request = (WebRequest) control.getMock();

        request.getSession(create);

        control.setReturnValue(session);

        return request;
    }

    private WebSession newSession()
    {
        return (WebSession) newMock(WebSession.class);
    }

    private WebSession newSession(String attributeName, Object value)
    {
        MockControl control = newControl(WebSession.class);
        WebSession session = (WebSession) control.getMock();

        session.getAttributeNames();

        control.setReturnValue(Collections.singletonList(attributeName));

        if (value != null)
        {
            session.getAttribute(attributeName);
            control.setReturnValue(value);
        }

        return session;
    }

    private WebSession newSession(String attributeName, boolean remove)
    {
        MockControl control = newControl(WebSession.class);
        WebSession session = (WebSession) control.getMock();

        session.getAttributeNames();

        control.setReturnValue(Collections.singletonList(attributeName));

        if (remove)
            session.setAttribute(attributeName, null);

        return session;
    }

    public void testStorePageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("myapp,Home,foo", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Home", null, "foo", value);

        verifyControls();
    }

    public void testClearPageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        session.setAttribute("myapp,Help,bar", null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Help", null, "bar", null);

        verifyControls();
    }

    public void testStoreComponentProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("gloop,Nerf,zip.zap,spaz", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("gloop");
        s.setRequest(request);

        s.store("Nerf", "zip.zap", "spaz", value);

        verifyControls();
    }

    public void testGetStoredChangesNoSession()
    {
        WebRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        assertTrue(s.getStoredChanges("Foo", null).isEmpty());

        verifyControls();
    }

    public void testGetStoreChangesNoMatch()
    {
        WebSession session = newSession("myapp,Home,foo,bar", null);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help", null);

        assertTrue(actual.isEmpty());

        verifyControls();
    }

    public void testGetStoredPageProperty()
    {
        Object value = new Object();
        WebSession session = newSession("myapp,Help,bar", value);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

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
        WebSession session = newSession("myapp,Help,zap.biff,bar", value);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

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
        WebRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo", null);

        verifyControls();
    }

    public void testDiscardChangesNoMatch()
    {
        WebSession session = newSession("myapp,Home,foo", false);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo", null);
        verifyControls();
    }

    public void testDiscardChangesWithMatch()
    {
        WebSession session = newSession("myapp,Home,foo", true);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        s.discardStoredChanges("Home", null);

        verifyControls();
    }
}