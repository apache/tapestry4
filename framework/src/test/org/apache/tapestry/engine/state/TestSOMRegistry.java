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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests {@link TestSOMRegistry}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestSOMRegistry extends HiveMindTestCase
{
    public void testInitializeAndGet()
    {
        Object stateObject = new Object();
        MockControl pmc = newControl(StateObjectPersistenceManager.class);
        StateObjectPersistenceManager pm = (StateObjectPersistenceManager) pmc.getMock();

        StateObjectFactory f = (StateObjectFactory) newMock(StateObjectFactory.class);

        pm.get("fred", f);
        pmc.setReturnValue(stateObject);

        StateObjectContribution c = new StateObjectContribution();
        c.setName("fred");
        c.setScope("wierd");
        c.setFactory(f);

        Map applicationContributions = new HashMap();
        applicationContributions.put("fred", c);

        Map persistenceManagers = new HashMap();
        persistenceManagers.put("wierd", pm);

        replayControls();

        SOMRegistryImpl r = new SOMRegistryImpl();
        r.setApplicationContributions(applicationContributions);
        r.setFactoryContributions(Collections.EMPTY_MAP);
        r.setPersistenceManagers(persistenceManagers);
        r.initializeService();

        StateObjectManager som = r.get("fred");

        assertSame(stateObject, som.get());

        verifyControls();
    }

    public void testInitializeUnknownScope()
    {
        Location l = fabricateLocation(55);
        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        StateObjectContribution c = new StateObjectContribution();
        c.setName("fred");
        c.setScope("wierd");
        c.setLocation(l);

        Map applicationContributions = new HashMap();
        applicationContributions.put("fred", c);

        log.error(StateMessages.unknownScope("fred", "wierd"), l, null);

        replayControls();

        SOMRegistryImpl r = new SOMRegistryImpl();
        r.setApplicationContributions(applicationContributions);
        r.setFactoryContributions(Collections.EMPTY_MAP);
        r.setPersistenceManagers(Collections.EMPTY_MAP);
        r.setErrorLog(log);
        r.initializeService();

        verifyControls();
    }

    public void testGetUnknownObjectName()
    {
        SOMRegistryImpl r = new SOMRegistryImpl();

        try
        {
            r.get("angel");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(StateMessages.unknownStateObjectName("angel"), ex.getMessage());
        }
    }
}