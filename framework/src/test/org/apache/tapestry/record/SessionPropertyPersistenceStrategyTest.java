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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertSame;
import static org.testng.AssertJUnit.assertTrue;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.engine.ServiceEncoding;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.testng.annotations.Test;

/**
 * Tests for {@link SessionPropertyPersistenceStrategy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class SessionPropertyPersistenceStrategyTest extends BaseComponentTestCase
{
    private ServiceEncoding newEncoding()
    {
        return newMock(ServiceEncoding.class);
    }

    private WebRequest newRequest(boolean create, WebSession session)
    {
        WebRequest request = newMock(WebRequest.class);

        expect(request.getSession(create)).andReturn(session);

        return request;
    }

    private WebSession newSession()
    {
        return newMock(WebSession.class);
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
        WebSession session = newMock(WebSession.class);

        expect(session.getAttributeNames()).andReturn(Collections.singletonList(attributeName));
        
        if (value != null)
            trainGetAttribute(session, attributeName, value);

        return session;
    }

    public void testAddParametersDoesNothing()
    {
        ServiceEncoding encoding = newEncoding();

        replay();

        SessionPropertyPersistenceStrategy strategy = new SessionPropertyPersistenceStrategy();

        strategy.addParametersForPersistentProperties(encoding, false);

        verify();
    }

    public void testClearPageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        session.setAttribute("session,myapp,Help,bar", null);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Help", null, "bar", null);

        verify();
    }

    public void testDiscardChangesNoMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo", false);
        WebRequest request = newRequest(false, session);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        s.discardStoredChanges("Foo");
        verify();
    }

    public void testDiscardChangesNoSession()
    {
        WebRequest request = newRequest(false, null);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        s.discardStoredChanges("Foo");

        verify();
    }

    public void testDiscardChangesWithMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo", true);
        WebRequest request = newRequest(false, session);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        s.discardStoredChanges("Home");

        verify();
    }

    public void testGetStoreChangesNoMatch()
    {
        WebSession session = newSession("session,myapp,Home,foo,bar", null);
        WebRequest request = newRequest(false, session);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertTrue(actual.isEmpty());

        verify();
    }

    public void testGetStoredChangesNoSession()
    {
        WebRequest request = newRequest(false, null);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);

        assertTrue(s.getStoredChanges("Foo").isEmpty());

        verify();
    }

    public void testGetStoredComponentProperty()
    {
        Object value = new Object();
        WebSession session = newSession("session,myapp,Help,zap.biff,bar", value);
        WebRequest request = newRequest(false, session);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertEquals(1, actual.size());

        PropertyChange pc = (PropertyChange) actual.iterator().next();

        assertEquals("zap.biff", pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verify();
    }

    public void testGetStoredPageProperty()
    {
        Object value = new Object();
        WebSession session = newSession("session,myapp,Help,bar", value);
        WebRequest request = newRequest(false, session);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();
        s.setRequest(request);
        s.setApplicationId("myapp");

        Collection actual = s.getStoredChanges("Help");

        assertEquals(1, actual.size());

        PropertyChange pc = (PropertyChange) actual.iterator().next();

        assertNull(pc.getComponentPath());
        assertEquals("bar", pc.getPropertyName());
        assertSame(value, pc.getNewValue());

        verify();
    }

    public void testStoreComponentProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("session,gloop,Nerf,zip.zap,spaz", value);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("gloop");
        s.setRequest(request);

        s.store("Nerf", "zip.zap", "spaz", value);

        verify();
    }

    public void testStorePageProperty()
    {
        WebSession session = newSession();
        WebRequest request = newRequest(true, session);

        Object value = new Object();

        session.setAttribute("session,myapp,Home,foo", value);

        replay();

        SessionPropertyPersistenceStrategy s = new SessionPropertyPersistenceStrategy();

        s.setApplicationId("myapp");
        s.setRequest(request);

        s.store("Home", null, "foo", value);

        verify();
    }

    private void trainGetAttribute(WebSession session, String attributeName, Object value)
    {
        expect(session.getAttribute(attributeName)).andReturn(value);
    }

    private void trainGetAttributeNames(WebSession session, List names)
    {
        expect(session.getAttributeNames()).andReturn(names);
    }
}