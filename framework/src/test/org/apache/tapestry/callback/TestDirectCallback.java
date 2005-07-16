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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IDirect;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.components.BaseComponentTestCase;
import org.easymock.MockControl;

/**
 * @author Howard M. Lewis Ship
 */
public class TestDirectCallback extends BaseComponentTestCase
{
    public void testNoParams()
    {
        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        MockControl componentc = newControl(IDirect.class);
        IDirect component = (IDirect) componentc.getMock();

        component.getPage();
        componentc.setReturnValue(page);

        page.getPageName();
        pagec.setReturnValue("Fred");

        component.getIdPath();
        componentc.setReturnValue("foo.bar");

        replayControls();

        DirectCallback callback = new DirectCallback(component, null);

        assertEquals("DirectCallback[Fred/foo.bar]", callback.toString());

        verifyControls();

        IRequestCycle cycle = newCycleGetPage("Fred", page);

        page.getNestedComponent("foo.bar");
        pagec.setReturnValue(component);

        cycle.setListenerParameters(null);

        component.trigger(cycle);

        replayControls();

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testWithParams()
    {
        Object[] params = new Object[]
        { "p1", "p2" };

        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        MockControl componentc = newControl(IDirect.class);
        IDirect component = (IDirect) componentc.getMock();

        component.getPage();
        componentc.setReturnValue(page);

        page.getPageName();
        pagec.setReturnValue("Barney");

        component.getIdPath();
        componentc.setReturnValue("foo.bar");

        replayControls();

        DirectCallback callback = new DirectCallback(component, params);

        assertEquals("DirectCallback[Barney/foo.bar p1, p2]", callback.toString());

        verifyControls();

        IRequestCycle cycle = newCycleGetPage("Barney", page);

        page.getNestedComponent("foo.bar");
        pagec.setReturnValue(component);

        cycle.setListenerParameters(params);

        component.trigger(cycle);

        replayControls();

        callback.performCallback(cycle);

        verifyControls();
    }

    public void testNotDirect()
    {
        MockControl pagec = newControl(IPage.class);
        IPage page = (IPage) pagec.getMock();

        MockControl componentc = newControl(IDirect.class);
        IDirect component = (IDirect) componentc.getMock();

        component.getPage();
        componentc.setReturnValue(page);

        page.getPageName();
        pagec.setReturnValue("Fred");

        component.getIdPath();
        componentc.setReturnValue("foo.bar");

        replayControls();

        DirectCallback callback = new DirectCallback(component, null);

        assertEquals("DirectCallback[Fred/foo.bar]", callback.toString());

        verifyControls();

        IRequestCycle cycle = newCycleGetPage("Fred", page);

        Location l = newLocation();
        IComponent component2 = newComponent("Fred/foo.bar", l);

        page.getNestedComponent("foo.bar");
        pagec.setReturnValue(component2);

        replayControls();

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

        verifyControls();
    }
}
