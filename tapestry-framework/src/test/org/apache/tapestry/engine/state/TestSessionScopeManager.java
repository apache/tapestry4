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

package org.apache.tapestry.engine.state;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.SessionStoreOptimized;
import org.apache.tapestry.web.WebRequest;
import org.apache.tapestry.web.WebSession;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.state.SessionScopeManager}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestSessionScopeManager extends BaseComponentTestCase
{
    private WebRequest newRequest(boolean create, WebSession session)
    {
        WebRequest request = newMock(WebRequest.class);
        
        expect(request.getSession(create)).andReturn(session);

        return request;
    }

    private WebRequest newRequest(WebSession session)
    {
        WebRequest request = newMock(WebRequest.class);

        expect(request.getSession(true)).andReturn(session);

        return request;
    }

    private WebSession newSession(String key, Object value)
    {
        WebSession session = newSession();
        checkOrder(session, false);
        
        trainGetAttribute(session, key, value);

        return session;
    }

    private StateObjectFactory newFactory(Object stateObject)
    {
        StateObjectFactory factory = newMock(StateObjectFactory.class);

        expect(factory.createStateObject()).andReturn(stateObject);

        return factory;
    }

    public void testExistsNoSession()
    {
        WebRequest request = newRequest(false, null);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);

        assertEquals(false, m.exists("doesn't matter"));

        verify();
    }

    public void testExistsMissing()
    {
        WebSession session = newSession("state:myapp:fred", null);
        WebRequest request = newRequest(false, session);
        
        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("myapp");

        assertEquals(false, m.exists("fred"));

        verify();
    }

    public void testExists()
    {
        WebSession session = newSession("state:testapp:fred", "XXX");
        WebRequest request = newRequest(false, session);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("testapp");

        assertEquals(true, m.exists("fred"));

        verify();
    }

    public void testGetExists()
    {
        Object stateObject = new Object();
        WebSession session = newSession("state:testapp:fred", stateObject);
        WebRequest request = newRequest(session);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("testapp");

        assertSame(stateObject, m.get("fred", null));

        verify();
    }

    public void testGetAndCreate()
    {
        Object stateObject = new Object();
        
        WebSession session = newSession();
        
        WebRequest request = newRequest(session);
        
        trainGetAttribute(session, "state:myapp:fred", null);

        StateObjectFactory factory = newFactory(stateObject);
        
        session.setAttribute("state:myapp:fred", stateObject);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("myapp");

        assertSame(stateObject, m.get("fred", factory));

        verify();
    }

    protected void trainGetAttribute(WebSession session, String name, Object attribute)
    {
        expect(session.getAttribute(name)).andReturn(attribute);
    }

    public void testStore()
    {
        Object stateObject = new Object();

        WebSession session = newSession();
        WebRequest request = newRequest(session);
        
        session.setAttribute("state:myapp:fred", stateObject);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("myapp");

        m.store("fred", stateObject);

        verify();
    }

    protected WebSession newSession()
    {
        return newMock(WebSession.class);
    }

    protected SessionStoreOptimized newOptimized(boolean dirty)
    {
        SessionStoreOptimized optimized = newMock(SessionStoreOptimized.class);

        expect(optimized.isStoreToSessionNeeded()).andReturn(dirty);

        return optimized;
    }

    public void testStoreOptimizedClean()
    {
        Object stateObject = newOptimized(false);

        SessionScopeManager m = new SessionScopeManager();

        replay();

        m.store("fred", stateObject);

        verify();
    }

    public void testStoreOptimizedDirty()
    {
        Object stateObject = newOptimized(true);

        WebSession session = newSession();
        WebRequest request = newRequest(session);
        
        session.setAttribute("state:myapp:fred", stateObject);

        replay();

        SessionScopeManager m = new SessionScopeManager();
        m.setRequest(request);
        m.setApplicationId("myapp");

        m.store("fred", stateObject);

        verify();
    }
}