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

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineServiceInnerProxy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestEngineServiceInnerProxy extends HiveMindTestCase
{
    private ILink newLink()
    {
        return (ILink) newMock(ILink.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private EngineServiceSource newSource(String name, IEngineService service)
    {
        MockControl control = newControl(EngineServiceSource.class);
        EngineServiceSource source = (EngineServiceSource) control.getMock();

        source.resolveEngineService(name);
        control.setReturnValue(service);

        return source;
    }

    public void testGetName()
    {
        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("Outer");
        EngineServiceSource source = (EngineServiceSource) newMock(EngineServiceSource.class);

        replayControls();

        EngineServiceInnerProxy proxy = new EngineServiceInnerProxy("Inner", outer, source);

        assertEquals("Inner", proxy.getName());
        assertEquals("<InnerProxy for engine service 'Inner'>", proxy.toString());

        verifyControls();
    }

    public void testGetLink()
    {
        ILink link = newLink();
        IRequestCycle cycle = newCycle();

        MockControl control = newControl(IEngineService.class);
        IEngineService service = (IEngineService) control.getMock();

        Object parameter = new Object();

        service.getLink(cycle, parameter);
        control.setReturnValue(link);

        EngineServiceSource source = newSource("fred", service);

        replayControls();

        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("fred");
        EngineServiceInnerProxy inner = new EngineServiceInnerProxy("fred", outer, source);

        outer.installDelegate(inner);

        assertSame(link, outer.getLink(cycle, parameter));

        assertSame(service, outer.getDelegate());

        verifyControls();
    }

    public void testService() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IEngineService service = (IEngineService) newMock(IEngineService.class);

        service.service(cycle);

        EngineServiceSource source = newSource("fred", service);

        replayControls();

        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("fred");
        EngineServiceInnerProxy inner = new EngineServiceInnerProxy("fred", outer, source);

        outer.installDelegate(inner);

        outer.service(cycle);

        assertSame(service, outer.getDelegate());

        verifyControls();
    }
}