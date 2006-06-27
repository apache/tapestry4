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

package org.apache.tapestry.callback;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.testng.annotations.Test;

/**
 * @author Howard M. Lewis Ship
 */
@Test
public class TestDirectCallback extends BaseComponentTestCase
{
    public void testNoParams()
    {
        IPage page = newMock(IPage.class);
        IDirect component = newMock(IDirect.class);

        expect(component.getPage()).andReturn(page);

        expect(page.getPageName()).andReturn("Fred");

        expect(component.getIdPath()).andReturn("foo.bar");

        replay();

        DirectCallback callback = new DirectCallback(component, null);

        assertEquals("DirectCallback[Fred/foo.bar]", callback.toString());

        verify();

        IRequestCycle cycle = newCycleGetPage("Fred", page);

        expect(page.getNestedComponent("foo.bar")).andReturn(component);

        cycle.setListenerParameters(null);

        component.trigger(cycle);

        replay();

        callback.performCallback(cycle);

        verify();
    }

    public void testWithParams()
    {
        Object[] params = new Object[]
        { "p1", "p2" };

        IPage page = newMock(IPage.class);
        IDirect component = newMock(IDirect.class);

        expect(component.getPage()).andReturn(page);

        expect(page.getPageName()).andReturn("Barney");

        expect(component.getIdPath()).andReturn("foo.bar");

        replay();

        DirectCallback callback = new DirectCallback(component, params);

        assertEquals("DirectCallback[Barney/foo.bar p1, p2]", callback.toString());

        verify();

        IRequestCycle cycle = newCycleGetPage("Barney", page);

        expect(page.getNestedComponent("foo.bar")).andReturn(component);

        cycle.setListenerParameters(params);

        component.trigger(cycle);

        replay();

        callback.performCallback(cycle);

        verify();
    }

    public void testNotDirect()
    {
        IPage page = newMock(IPage.class);
        IDirect component = newMock(IDirect.class);

        expect(component.getPage()).andReturn(page);

        expect(page.getPageName()).andReturn("Fred");

        expect(component.getIdPath()).andReturn("foo.bar");

        replay();

        DirectCallback callback = new DirectCallback(component, null);

        assertEquals("DirectCallback[Fred/foo.bar]", callback.toString());

        verify();

        IRequestCycle cycle = newCycleGetPage("Fred", page);

        Location l = newLocation();
        IComponent component2 = newComponent("Fred/foo.bar", l);

        expect(page.getNestedComponent("foo.bar")).andReturn(component2);

        replay();

        try
        {
            callback.performCallback(cycle);
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Component Fred/foo.bar does not implement the IDirect interface.", ex
                    .getMessage());
            assertSame(component2, ex.getComponent());
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}
