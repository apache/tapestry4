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

import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.engine.state.ApplicationStateManagerImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestApplicationStateManager extends HiveMindTestCase
{
    private StateObjectManagerRegistry newRegistry(String name,
            StateObjectManager manager)
    {
        MockControl c = newControl(StateObjectManagerRegistry.class);
        StateObjectManagerRegistry result = (StateObjectManagerRegistry) c
                .getMock();

        result.get(name);
        c.setReturnValue(manager);

        return result;
    }

    public void testExistsInCache()
    {
        Object stateObject = new Object();

        MockControl c = newControl(StateObjectManager.class);
        StateObjectManager m = (StateObjectManager) c.getMock();

        m.get();
        c.setReturnValue(stateObject);

        StateObjectManagerRegistry r = newRegistry("fred", m);

        replayControls();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertSame(stateObject, asm.get("fred"));

        assertEquals(true, asm.exists("fred"));

        verifyControls();
    }

    public void testNotExist()
    {

        MockControl c = newControl(StateObjectManager.class);
        StateObjectManager m = (StateObjectManager) c.getMock();

        m.exists();
        c.setReturnValue(false);

        StateObjectManagerRegistry r = newRegistry("barney", m);

        replayControls();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertEquals(false, asm.exists("barney"));

        verifyControls();
    }

    public void testGet()
    {
        Object stateObject = new Object();

        MockControl c = newControl(StateObjectManager.class);
        StateObjectManager m = (StateObjectManager) c.getMock();

        m.get();
        c.setReturnValue(stateObject);

        MockControl rc = newControl(StateObjectManagerRegistry.class);
        StateObjectManagerRegistry r = (StateObjectManagerRegistry) rc
                .getMock();

        r.get("barney");
        rc.setReturnValue(m);

        replayControls();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertSame(stateObject, asm.get("barney"));

        verifyControls();

        replayControls();

        // Note: doesn't affect the SOPM

        assertSame(stateObject, asm.get("barney"));

        verifyControls();

        r.get("barney");
        rc.setReturnValue(m);

        m.get();
        c.setReturnValue(stateObject);

        replayControls();

        // Clear the cache
        asm.passivateService();

        // This invoked on the SOPM
        assertSame(stateObject, asm.get("barney"));

        verifyControls();
    }

    public void testFlush()
    {
        Object stateObject = new Object();

        MockControl c = newControl(StateObjectManager.class);
        StateObjectManager m = (StateObjectManager) c.getMock();

        m.get();
        c.setReturnValue(stateObject);

        MockControl rc = newControl(StateObjectManagerRegistry.class);
        StateObjectManagerRegistry r = (StateObjectManagerRegistry) rc
                .getMock();

        r.get("barney");
        rc.setReturnValue(m);

        replayControls();

        ApplicationStateManagerImpl asm = new ApplicationStateManagerImpl();
        asm.setRegistry(r);

        assertSame(stateObject, asm.get("barney"));

        verifyControls();

        r.get("barney");
        rc.setReturnValue(m);

        m.store(stateObject);

        replayControls();

        asm.flush();

        verifyControls();
    }
}