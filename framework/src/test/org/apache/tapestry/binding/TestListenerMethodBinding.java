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

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.PageRedirectException;
import org.apache.tapestry.RedirectException;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.listener.ListenerMap;

/**
 * Test for {@link org.apache.tapestry.binding.ListenerMethodBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
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

        replayControls();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        assertSame(b, b.getObject());
        assertSame(component, b.getComponent());

        b.actionTriggered(sourceComponent, cycle);

        verifyControls();
    }

    public void testToString()
    {
        IComponent component = newComponent();
        Location l = newLocation();
        ValueConverter vc = newValueConverter();

        trainGetExtendedId(component, "Fred/barney");

        replayControls();

        ListenerMethodBinding b = new ListenerMethodBinding("param", vc, l, component, "foo");

        String toString = b.toString();
        String description = toString.substring(toString.indexOf('[') + 1, toString.length() - 1);

        assertEquals(
                "param, component=Fred/barney, methodName=foo, location=classpath:/org/apache/tapestry/binding/TestListenerMethodBinding, line 1",
                description);

        verifyControls();
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
        setThrowable(listener, t);

        replayControls();

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

        verifyControls();
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
        setThrowable(listener, t);

        replayControls();

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

        verifyControls();
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
        setThrowable(listener, t);

        trainGetExtendedId(component, "Fred/barney");

        replayControls();

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

        verifyControls();
    }

    private void trainGetListener(IComponent component, ListenerMap lm, IActionListener listener)
    {
        trainGetListeners(component, lm);
        trainGetListener(lm, "foo", listener);
    }

    protected IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }

    private void trainGetListener(ListenerMap map, String methodName, IActionListener listener)
    {
        map.getListener(methodName);
        setReturnValue(map, listener);
    }

    private void trainGetListeners(IComponent component, ListenerMap lm)
    {
        component.getListeners();
        setReturnValue(component,lm);
    }

    private ListenerMap newListenerMap()
    {
        return (ListenerMap) newMock(ListenerMap.class);
    }

    private IActionListener newListener()
    {
        return (IActionListener) newMock(IActionListener.class);
    }
}