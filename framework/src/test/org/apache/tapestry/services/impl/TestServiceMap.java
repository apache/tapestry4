// Copyright 2004, 2005 The Apache Software Foundation
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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.ServiceMapImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestServiceMap extends HiveMindTestCase
{
    private IEngineService newService(String name)
    {
        MockControl control = newControl(IEngineService.class);
        IEngineService service = (IEngineService) control.getMock();

        service.getName();
        control.setReturnValue(name);

        return service;
    }

    private IEngineService newService()
    {
        return (IEngineService) newMock(IEngineService.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private EngineServiceContribution constructService(String name, IEngineService service)
    {
        EngineServiceContribution result = new EngineServiceContribution();
        result.setName(name);
        result.setService(service);

        return result;
    }

    /**
     * Gets an application-defined and factory-defined service where there are no naming conflicts.
     * Because ServiceMap now returns proxies, we have to do a little extra indirection to ensure
     * that we get what's expected.
     */
    public void testGetNoConflict() throws Exception
    {
        IRequestCycle cycle1 = newCycle();
        IRequestCycle cycle2 = newCycle();

        IEngineService factory = newService("factory");
        IEngineService application = newService("application");

        EngineServiceContribution factoryc = constructService("factory", factory);
        EngineServiceContribution applicationc = constructService("application", application);

        factory.service(cycle1);
        application.service(cycle2);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.singletonList(factoryc));
        m.setApplicationServices(Collections.singletonList(applicationc));

        m.initializeService();

        m.getService("factory").service(cycle1);
        m.getService("application").service(cycle2);

        verifyControls();
    }

    public void testNameMismatch() throws Exception
    {
        IRequestCycle cycle = newCycle();
        Location l = fabricateLocation(1289);

        IEngineService service = newService("actual-name");

        EngineServiceContribution contribution = constructService("expected-name", service);
        contribution.setLocation(l);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.singletonList(contribution));
        m.setApplicationServices(Collections.EMPTY_LIST);

        m.initializeService();

        IEngineService proxy = m.getService("expected-name");

        try
        {
            proxy.service(cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Engine service EasyMock for interface org.apache.tapestry.engine.IEngineService is mapped to name 'expected-name' but indicates a name of 'actual-name'.",
                    ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verifyControls();
    }

    public void testGetServiceRepeated()
    {
        IEngineService application = newService();
        EngineServiceContribution applicationc = constructService("application", application);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.EMPTY_LIST);
        m.setApplicationServices(Collections.singletonList(applicationc));

        m.initializeService();

        IEngineService service = m.getService("application");
        assertSame(service, m.getService("application"));

        verifyControls();
    }

    public void testApplicationOverridesFactory() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IEngineService factory = newService();
        IEngineService application = newService("override");

        EngineServiceContribution factoryc = constructService("override", factory);
        EngineServiceContribution applicationc = constructService("override", application);

        application.service(cycle);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.singletonList(factoryc));
        m.setApplicationServices(Collections.singletonList(applicationc));

        m.initializeService();

        m.getService("override").service(cycle);

        verifyControls();
    }

    public void testUnknownService()
    {
        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.EMPTY_LIST);
        m.setApplicationServices(Collections.EMPTY_LIST);

        m.initializeService();

        try
        {
            m.getService("missing");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(ImplMessages.noSuchService("missing"), ex.getMessage());
        }

        try
        {
            m.resolveEngineService("resolve-missing");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(ImplMessages.noSuchService("resolve-missing"), ex.getMessage());

        }
    }

    public void testDuplicateName() throws Exception
    {
        Location l = fabricateLocation(37);
        IRequestCycle cycle = newCycle();

        IEngineService first = newService("duplicate");
        IEngineService second = newService();

        EngineServiceContribution firstc = constructService("duplicate", first);
        firstc.setLocation(l);

        EngineServiceContribution secondc = constructService("duplicate", second);

        List list = new ArrayList();
        list.add(firstc);
        list.add(secondc);

        ErrorLog log = (ErrorLog) newMock(ErrorLog.class);

        first.service(cycle);

        log.error(ImplMessages.dupeService("duplicate", firstc), l, null);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(list);
        m.setApplicationServices(Collections.EMPTY_LIST);
        m.setErrorLog(log);

        m.initializeService();

        m.getService("duplicate").service(cycle);

        verifyControls();
    }
}