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

package org.apache.tapestry.binding;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.*;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.listener.ListenerMap;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import org.testng.annotations.Test;

/**
 * Test for {@link org.apache.tapestry.binding.ListenerMethodBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestListenerMethodBinding extends BindingTestCase
{
    public void test_Invoke_Listener()
    {
        IComponent component = newComponent();
        ListenerMap map = newListenerMap();
        IActionListener listener = newListener();
        Location l = newLocation();
        IComponent sourceComponent = newComponent();
        IRequestCycle cycle = newCycle();
        ValueConverter vc = newValueConverter();

        trainGetListener(component, map, listener);

        listener.actionTriggered(sourceComponent, cycle);

        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        assertSame(b, b.getObject());
        assertSame(component, b.getComponent());

        b.actionTriggered(sourceComponent, cycle);

        verify();
    }

    public void test_To_String()
    {
        IComponent component = newComponent();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();

        trainGetExtendedId(component, "Fred/barney");
        
        replay();
        
        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        String toString = b.toString();
        String description = toString.substring(toString.indexOf('[') + 1, toString.length() - 1);
        
        assertTrue(description.indexOf("param, component=Fred/barney, methodName=foo, location=") > -1);
        
        verify();
    }

    public void test_Invoke_And_Page_Redirect()
    {
        IComponent component = newComponent();
        ListenerMap map = newListenerMap();
        IActionListener listener = newListener();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();
        IComponent sourceComponent = newComponent();
        IRequestCycle cycle = newCycle();

        trainGetListener(component, map, listener);

        listener.actionTriggered(sourceComponent, cycle);
        
        Throwable t = new PageRedirectException("TargetPage");
        expectLastCall().andThrow(t);
        
        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        try
        {
            b.actionTriggered(sourceComponent, cycle);
            unreachable();
        }
        catch (PageRedirectException ex)
        {
            assertSame(t, ex);
        }

        verify();
    }

    public void test_Invoke_And_Redirect()
    {
        IComponent component = newComponent();
        ListenerMap map = newListenerMap();
        IActionListener listener = newListener();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();
        IComponent sourceComponent = newComponent();
        IRequestCycle cycle = newCycle();

        trainGetListener(component, map, listener);

        listener.actionTriggered(sourceComponent, cycle);
        
        Throwable t = new RedirectException("http://foo.bar");
        expectLastCall().andThrow(t);

        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        try
        {
            b.actionTriggered(sourceComponent, cycle);
            unreachable();
        }
        catch (RedirectException ex)
        {
            assertSame(t, ex);
        }

        verify();
    }

    @Test(expectedExceptions = RenderRewoundException.class)
    public void test_Invoke_Render_Rewound()
    {        
        IComponent component = newMock(IComponent.class);
        ListenerMap map = newListenerMap();
        IActionListener listener = newMock(IActionListener.class);
        ValueConverter vc = newMock(ValueConverter.class);
        IComponent sourceComponent = newMock(IComponent.class);
        Location l = newMock(Location.class);
        IRequestCycle cycle = newMock(IRequestCycle.class);

        trainGetListener(component, map, listener);

        listener.actionTriggered(sourceComponent, cycle);

        ApplicationRuntimeException t = new RenderRewoundException(null);
        expectLastCall().andThrow(t);

        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        b.actionTriggered(sourceComponent, cycle);

        verify();
    }

    public void test_Invoke_Listener_Failure()
    {
        IComponent component = newComponent();
        ListenerMap map = newListenerMap();
        IActionListener listener = newListener();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();
        IComponent sourceComponent = newComponent();
        IRequestCycle cycle = newCycle();

        trainGetListener(component, map, listener);

        listener.actionTriggered(sourceComponent, cycle);

        Throwable t = new RuntimeException("Failure.");
        expectLastCall().andThrow(t);

        trainGetExtendedId(component, "Fred/barney");

        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        try
        {
            b.actionTriggered(sourceComponent, cycle);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Exception invoking listener method foo of component Fred/barney: Failure.",
                    ex.getMessage());
            assertSame(component, ex.getComponent());
            assertSame(l, ex.getLocation());
            assertSame(b, ex.getBinding());
        }

        verify();
    }

    public void test_Get_Method_Name()
    {
        IComponent component = newComponent();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();
        
        replay();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        assertEquals(b.getMethodName(), "foo");

        verify();
    }

    private void trainGetListener(IComponent component, ListenerMap lm, IActionListener listener)
    {
        trainGetListeners(component, lm);
        trainGetListener(lm, "foo", listener);
    }

    private void trainGetListener(ListenerMap map, String methodName, IActionListener listener)
    {
        expect(map.getListener(methodName)).andReturn(listener);
    }

    private void trainGetListeners(IComponent component, ListenerMap lm)
    {
        expect(component.getListeners()).andReturn(lm);
    }

    private ListenerMap newListenerMap()
    {
        return newMock(ListenerMap.class);
    }

    private IActionListener newListener()
    {
        return newMock(IActionListener.class);
    }
}
