// Copyright 2004 The Apache Software Foundation
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

import org.apache.commons.logging.Log;
import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.ErrorHandler;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.engine.IEngineService;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.ServiceMapImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestServiceMap extends HiveMindTestCase
{
    private IEngineService constructService(String name)
    {
        MockControl control = newControl(IEngineService.class);
        IEngineService result = (IEngineService) control.getMock();

        result.getName();
        control.setReturnValue(name);

        return result;
    }

    /**
     * Gets an application-defined and factory-defined service where there are no naming conflicts.
     */
    public void testGetNoConflict()
    {
        IEngineService factory = constructService("factory");
        IEngineService application = constructService("application");

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.singletonList(factory));
        m.setApplicationServices(Collections.singletonList(application));

        m.initializeService();

        assertSame(factory, m.getService("factory"));
        assertSame(application, m.getService("application"));

        verifyControls();
    }

    public void testApplicationOverridesFactory()
    {
        IEngineService factory = constructService("override");
        IEngineService application = constructService("override");

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(Collections.singletonList(factory));
        m.setApplicationServices(Collections.singletonList(application));

        m.initializeService();

        assertSame(application, m.getService("override"));

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
    }

    public void testDuplicateName()
    {
        IEngineService first = constructService("duplicate");
        IEngineService second = constructService("duplicate");

        List list = new ArrayList();
        list.add(first);
        list.add(second);

        Log log = (Log) newMock(Log.class);
        ErrorHandler eh = (ErrorHandler) newMock(ErrorHandler.class);

        eh.error(log, ImplMessages.dupeService("duplicate", first), null, null);

        replayControls();

        ServiceMapImpl m = new ServiceMapImpl();

        m.setFactoryServices(list);
        m.setApplicationServices(Collections.EMPTY_LIST);
        m.setLog(log);
        m.setErrorHandler(eh);

        m.initializeService();

        assertSame(first, m.getService("duplicate"));

        verifyControls();
    }
}