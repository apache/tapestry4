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
import java.util.List;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;

/**
 * Tests for {@link SessionPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class SessionPropertyPersistenceStrategyTest extends HiveMindTestCase
{
    private ServiceEncoding newEncoding()
    {
        return (ServiceEncoding) newMock(ServiceEncoding.class);
    }

    private WebRequest newRequest(boolean create, WebSession session)
    {
        WebRequest request = (WebRequest) newMock(WebRequest.class);

        request.getSession(create);

        setReturnValue(request, session);

        return request;
    }

    private WebSession newSession()
    {
        return (WebSession) newMock(WebSession.class);
    }

    private WebSession newSession(String attributeName, boolean remove)
    {
        WebSession session = newSession();

        trainGetAttributeNames(session, Collections.singletonList(attributeName));

        if (remove)
            session.setAttribute(attributeName, null);

        return session;
    }

    private WebSession newSession(String attributeName, Object value)
    {
        WebSession session = (WebSession) newMock(WebSession.class);

        session.getAttributeNames();

        setReturnValue(session, Collections.singletonList(attributeName));

        if (value != null)
            trainGetAttribute(session, attributeName, value);

        return session;
    }

    public void testAddParametersDoesNothing()
    {
        ServiceEncoding encoding = newEncoding();

        replayControls();

        SessionPropertyPersistenceStrategy strategy = new SessionPropertyPersistenceStrategy();

        strategy.addParametersForPersistentProperties(encoding, false);

        verifyControls();
    }

    public void testClearPageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        session.setAttribute("session,myapp,Help,bar", null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Help", null, "bar", null);

        verifyControls();
    }

    public void testDiscardChangesNoMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo", false);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        s.discardStoredChanges("Foo");
        verifyControls();
    }

    public void testDiscardChangesNoSession()
    {
        WebRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo");

        verifyControls();
    }

    public void testDiscardChangesWithMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo", true);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        s.discardStoredChanges("Home");

        verifyControls();
    }

    public void testGetStoreChangesNoMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo,bar", null);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertTrue(actual.isEmpty());

        verifyControls();
    }

    public void testGetStoredChangesNoSession()
    {
        WebRequest request = newRequest(false, null);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        assertTrue(s.getStoredChanges("Foo").isEmpty());

        verifyControls();
    }

    public void testGetStoredComponentProperty()
    {
        Object value = new Object();
        WebSession session = newSession("session,myapp,Help,zap.biff,bar", value);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertEquals(1, actual.size());

        PropertyChange pc = (PropertyChange) actual.iterator().next();

        assertEquals("zap.biff", pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verifyControls();
    }

    public void testGetStoredPageProperty()
    {
        Object value = new Object();
        WebSession session = newSession("session,myapp,Help,bar", value);
        WebRequest request = newRequest(false, session);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertEquals(1, actual.size());

        PropertyChange pc = (PropertyChange) actual.iterator().next();

        assertNull(pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verifyControls();
    }

    public void testStoreComponentProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("session,gloop,Nerf,zip.zap,spaz", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("gloop");
        s.setRequest(request);

        s.store("Nerf", "zip.zap", "spaz", value);

        verifyControls();
    }

    public void testStorePageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("session,myapp,Home,foo", value);

        replayControls();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Home", null, "foo", value);

        verifyControls();
    }

    private void trainGetAttribute(WebSession session, String attributeName, Object value)
    {
        session.getAttribute(attributeName);
        setReturnValue(session, value);
    }

    private void trainGetAttributeNames(WebSession session, List names)
    {
        session.getAttributeNames();
        setReturnValue(session, names);
    }
}