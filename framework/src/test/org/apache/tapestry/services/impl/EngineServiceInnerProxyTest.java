// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.IEngineService;
import org.apache.tapestry.engine.ILink;

/**
 * Tests for {@link org.apache.tapestry.services.impl.EngineServiceInnerProxy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EngineServiceInnerProxyTest extends AbstractEngineServiceProxyTestCase
{
    private EngineServiceSource newSource(String name, IEngineService service)
    {
        EngineServiceSource source = newSource();

        source.resolveEngineService(name);
        setReturnValue(source, service);

        return source;
    }

    public void testGetName()
    {
        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("Outer");
        EngineServiceSource source = newSource();

        replayControls();

        EngineServiceInnerProxy proxy = new EngineServiceInnerProxy("Inner", outer, source);

        assertEquals("Inner", proxy.getName());
        assertEquals("<InnerProxy for engine service 'Inner'>", proxy.toString());

        verifyControls();
    }

    protected EngineServiceSource newSource()
    {
        return (EngineServiceSource) newMock(EngineServiceSource.class);
    }

    public void testGetLinkNonPost()
    {
        ILink link = newLink();

        IEngineService service = newEngineService();

        Object parameter = new Object();

        trainGetLink(service, false, parameter, link);

        EngineServiceSource source = newSource("fred", service);

        replayControls();

        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("fred");
        EngineServiceInnerProxy inner = new EngineServiceInnerProxy("fred", outer, source);

        outer.installDelegate(inner);

        assertSame(link, outer.getLink(false, parameter));

        assertSame(service, outer.getDelegate());

        verifyControls();
    }

    public void testGetLinkPost()
    {
        ILink link = newLink();

        IEngineService service = newEngineService();

        Object parameter = new Object();

        trainGetLink(service, true, parameter, link);

        EngineServiceSource source = newSource("fred", service);

        replayControls();

        EngineServiceOuterProxy outer = new EngineServiceOuterProxy("fred");
        EngineServiceInnerProxy inner = new EngineServiceInnerProxy("fred", outer, source);

        outer.installDelegate(inner);

        assertSame(link, outer.getLink(true, parameter));

        assertSame(service, outer.getDelegate());

        verifyControls();
    }
    public void testService() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IEngineService service = newEngineService();

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