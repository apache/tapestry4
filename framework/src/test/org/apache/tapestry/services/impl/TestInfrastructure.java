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

package org.apache.tapestry.services.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorLog;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;

/**
 * Tests for {@link org.apache.tapestry.services.impl.InfrastructureImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestInfrastructure extends HiveMindTestCase
{
    private static class DeferredObjectFixture implements DeferredObject
    {
        private Object _object;

        private Location _location;

        public DeferredObjectFixture(Object object, Location location)
        {
            _object = object;
            _location = location;
        }

        public Location getLocation()
        {
            return _location;
        }

        public Object getObject()
        {
            return _object;
        }
    }

    private InfrastructureContribution newContribution(String propertyName, String mode,
            Object object)
    {
        return newContribution(propertyName, mode, object, null);
    }

    private InfrastructureContribution newContribution(String propertyName, String mode,
            Object object, Location location)
    {
        DeferredObject deferred = new DeferredObjectFixture(object, location);

        InfrastructureContribution ic = new InfrastructureContribution();
        ic.setDeferredObject(deferred);
        ic.setProperty(propertyName);
        ic.setMode(mode);
        ic.setLocation(location);

        return ic;
    }

    public void testGetPropertyUninitialized()
    {
        InfrastructureImpl infra = new InfrastructureImpl();

        try
        {
            infra.getProperty("foo");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(ImplMessages.infrastructureNotInitialized(), ex.getMessage());
        }
    }

    public void testGetNullProperty()
    {
        InfrastructureImpl infra = new InfrastructureImpl();

        infra.setNormalContributions(Collections.EMPTY_LIST);
        infra.setOverrideContributions(Collections.EMPTY_LIST);

        infra.initialize("test");

        try
        {
            infra.getProperty("fred");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(ImplMessages.missingInfrastructureProperty("fred"), ex.getMessage());
        }
    }

    public void testReinitalize()
    {
        InfrastructureImpl infra = new InfrastructureImpl();

        infra.setNormalContributions(Collections.EMPTY_LIST);
        infra.setOverrideContributions(Collections.EMPTY_LIST);

        infra.initialize("ONE");

        try
        {
            infra.initialize("TWO");
            unreachable();
        }
        catch (IllegalStateException ex)
        {
            assertEquals(ImplMessages.infrastructureAlreadyInitialized("TWO", "ONE"), ex
                    .getMessage());
        }
    }

    /**
     * Test that a contribution for a mode quietly overrides a contribution for the same property
     * that does not specify a mode.
     */

    public void testModeOverridesNonMode()
    {
        Object fredModal = new Object();
        Object plainFred = new Object();

        InfrastructureImpl infra = new InfrastructureImpl();

        List l = new ArrayList();
        l.add(newContribution("fred", "bedrock", fredModal));
        l.add(newContribution("fred", null, plainFred));

        infra.setNormalContributions(l);
        infra.setOverrideContributions(Collections.EMPTY_LIST);

        infra.initialize("bedrock");

        assertSame(fredModal, infra.getProperty("fred"));
    }
    
    public void testWrongModeIgnored()
    {
        Object fredModal = new Object();
        Object wrongFred = new Object();

        InfrastructureImpl infra = new InfrastructureImpl();

        List l = new ArrayList();
        l.add(newContribution("fred", "bedrock", fredModal));
        l.add(newContribution("fred", "shale", wrongFred));

        infra.setNormalContributions(l);
        infra.setOverrideContributions(Collections.EMPTY_LIST);

        infra.initialize("bedrock");

        assertSame(fredModal, infra.getProperty("fred"));
    }

    /**
     * Test that override contributions trump contributions from the normal path.
     */

    public void testOverrides()
    {
        Object normalFred = new Object();
        Object overrideFred = new Object();

        InfrastructureImpl infra = new InfrastructureImpl();

        infra.setNormalContributions(Collections.singletonList(newContribution(
                "fred",
                null,
                normalFred)));
        infra.setOverrideContributions(Collections.singletonList(newContribution(
                "fred",
                null,
                overrideFred)));

        infra.initialize("bedrock");

        assertSame(overrideFred, infra.getProperty("fred"));
    }

    public void testDuplicate()
    {
        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        Location l1 = fabricateLocation(99);
        Location l2 = fabricateLocation(132);

        Object fredModal = new Object();
        Object duplicateFred = new Object();

        List l = new ArrayList();
        l.add(newContribution("fred", "bedrock", fredModal, l1));
        InfrastructureContribution conflict = newContribution("fred", "bedrock", duplicateFred, l2);
        l.add(conflict);

        log.error(ImplMessages.duplicateInfrastructureContribution(conflict, l1), l2, null);

        replayControls();

        InfrastructureImpl infra = new InfrastructureImpl();
        infra.setNormalContributions(l);
        infra.setOverrideContributions(Collections.EMPTY_LIST);
        infra.setErrorLog(log);

        infra.initialize("bedrock");

        assertSame(fredModal, infra.getProperty("fred"));

        verifyControls();
    }
}