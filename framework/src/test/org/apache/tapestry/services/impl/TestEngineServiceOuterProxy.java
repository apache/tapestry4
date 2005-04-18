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
 * Tests for {@link org.apache.tapestry.services.impl.EngineServiceOuterProxy}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestEngineServiceOuterProxy extends HiveMindTestCase
{
    public void testToString()
    {
        IEngineService s = new EngineServiceOuterProxy("fred");

        assertEquals("<OuterProxy for engine service 'fred'>", s.toString());
    }

    public void testGetLink()
    {
        IRequestCycle cycle = newCycle();
        ILink link = (ILink) newMock(ILink.class);

        MockControl control = newControl(IEngineService.class);
        IEngineService delegate = (IEngineService) control.getMock();

        Object parameter = new Object();

        delegate.getLink(cycle, parameter);
        control.setReturnValue(link);

        replayControls();

        EngineServiceOuterProxy proxy = new EngineServiceOuterProxy("xxx");
        proxy.installDelegate(delegate);

        assertSame(link, proxy.getLink(cycle, parameter));

        verifyControls();
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    public void testGetName()
    {

        EngineServiceOuterProxy proxy = new EngineServiceOuterProxy("Fred");

        assertEquals("Fred", proxy.getName());
    }

    public void testService() throws Exception
    {
        IRequestCycle cycle = newCycle();
        IEngineService delegate = (IEngineService) newMock(IEngineService.class);

        delegate.service(cycle);

        replayControls();

        EngineServiceOuterProxy proxy = new EngineServiceOuterProxy("xxx");
        proxy.installDelegate(delegate);

        assertSame(delegate, proxy.getDelegate());

        proxy.service(cycle);

        verifyControls();
    }
}