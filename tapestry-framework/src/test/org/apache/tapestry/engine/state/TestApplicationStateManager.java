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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.BaseComponentTestCase;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.engine.state.ApplicationStateManagerImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestApplicationStateManager extends BaseComponentTestCase
{
    private StateObjectManagerRegistry newRegistry(String name,
            StateObjectManager manager)
    {
        StateObjectManagerRegistry result = newMock(StateObjectManagerRegistry.class);
        
        expect(result.get(name)).andReturn(manager);

        return result;
    }

    public void testExistsInCache()
    {
        Object stateObject = new Object();
        
        StateObjectManager m = newMock(StateObjectManager.class);
        
        StateObjectManagerRegistry r = newRegistry("fred", m);
        
        expect(m.get()).andReturn(stateObject);
        
        replay();
        
        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertSame(stateObject, asm.get("fred"));

        assertEquals(true, asm.exists("fred"));

        verify();
    }

    public void testNotExist()
    {

        StateObjectManager m = newMock(StateObjectManager.class);

        StateObjectManagerRegistry r = newRegistry("barney", m);

        expect(m.exists()).andReturn(false);
        
        replay();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertEquals(false, asm.exists("barney"));

        verify();
    }

    public void testGet()
    {
        Object stateObject = new Object();

        StateObjectManager m = newMock(StateObjectManager.class);
        
        StateObjectManagerRegistry r = newMock(StateObjectManagerRegistry.class);
        
        expect(r.get("barney")).andReturn(m);
        
        expect(m.get()).andReturn(stateObject);
        
        replay();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertSame(stateObject, asm.get("barney"));

        verify();

        replay();

        // Note: doesn't affect the SOPM

        assertSame(stateObject, asm.get("barney"));

        verify();
        
        expect(r.get("barney")).andReturn(m);
        
        expect(m.get()).andReturn(stateObject);

        replay();

        // Clear the cache
        asm.passivateService();

        // This invoked on the SOPM
        assertSame(stateObject, asm.get("barney"));

        verify();
    }

    public void testFlush()
    {
        Object stateObject = new Object();

        StateObjectManager m = newMock(StateObjectManager.class);

        StateObjectManagerRegistry r = newMock(StateObjectManagerRegistry.class);
        
        expect(r.get("barney")).andReturn(m);
        
        expect(m.get()).andReturn(stateObject);

        replay();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);
        
        assertSame(stateObject, asm.get("barney"));
        
        verify();

        expect(r.get("barney")).andReturn(m);

        m.store(stateObject);

        replay();

        asm.flush();

        verify();
    }
}