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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.testng.AssertJUnit.*;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.listener.ListenerMap;
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
    public void testInvokeListener()
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

    public void testToString()
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

    public void testInvokeAndPageRedirect()
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

    public void testInvokeAndRedirect()
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

    public void testInvokeListenerFailure()
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

    private void trainGetListener(IComponent component, ListenerMap lm, IActionListener listener)
    {
        trainGetListeners(component, lm);
        trainGetListener(lm, "foo", listener);
    }

    protected IRequestCycle newCycle()
    {
        return newMock(IRequestCycle.class);
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