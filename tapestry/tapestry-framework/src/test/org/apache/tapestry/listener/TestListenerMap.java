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

package org.apache.tapestry.listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.listener.ListenerMapImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestListenerMap extends BaseComponentTestCase
{
    public void test_Get_Listener()
    {
        Object target = new Object();
        IRequestCycle cycle = newCycle();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        invoker.invokeListenerMethod(target, cycle);

        replay();

        ListenerMap lm = new ListenerMapImpl(target, map);

        IActionListener l1 = lm.getListener("method");

        l1.actionTriggered(null, cycle);

        verify();

        IActionListener l2 = lm.getListener("method");

        assertSame(l1, l2);
    }

    public void test_GetListener_Names()
    {
        Object target = new Object();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replay();

        ListenerMap lm = new ListenerMapImpl(target, map);

        // Copy both collections into ArrayLists for comparison purposes.

        assertEquals(new ArrayList(map.keySet()), new ArrayList(lm.getListenerNames()));

        verify();

        try
        {
            lm.getListenerNames().clear();
            unreachable();
        }
        catch (UnsupportedOperationException ex)
        {
            // Ignore. Expected result.
        }
    }

    public void test_Can_Provide_Listener()
    {
        Object target = new Object();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replay();

        ListenerMap lm = new ListenerMapImpl(target, map);

        assertEquals(true, lm.canProvideListener("method"));
        assertEquals(false, lm.canProvideListener("foobar"));

        verify();
    }

    public void test_Missing_Listener()
    {
        Object target = "*TARGET*";
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replay();

        ListenerMap lm = new ListenerMapImpl(target, map);

        try
        {
            lm.getListener("foobar");
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("Object *TARGET* does not implement a listener method named 'foobar'.", ex
                    .getMessage());
            assertSame(target, ex.getComponent());
        }

        verify();
    }

	public void test_Get_Implicit_Listener()
	{
		Object target = new Object();
        IRequestCycle cycle = newCycle();
        ListenerMethodInvoker invoker = newInvoker();
		IComponent component = newMock(IComponent.class);
		expect(component.getId()).andReturn("action").times(2);
		Map map = newMap("doAction", invoker);

        invoker.invokeListenerMethod(target, cycle);

        replay();

        ListenerMap lm = new ListenerMapImpl(target, map);

        IActionListener l1 = lm.getImplicitListener(component);

        l1.actionTriggered(null, cycle);

		IActionListener l2 = lm.getImplicitListener(component);

		verify();

        assertSame(l1, l2);
	}

	public void test_Missing_Implicit_Listener()
    {
        ListenerMethodInvoker invoker = newInvoker();
	    IComponent component = newMock(IComponent.class);
	    expect(component.getLocation()).andReturn(null);
	    expect(component.getId()).andReturn("action");
        Map map = newMap("method", invoker);

        replay();

        ListenerMap lm = new ListenerMapImpl("test", map);

        try
        {
            lm.getImplicitListener(component);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals("No implicit listener method named 'doAction' found in " + component, ex
                    .getMessage());
            assertSame(component, ex.getComponent());
        }

        verify();
    }

    private Map newMap(Object key, Object value)
    {
        Map result = new HashMap();

        result.put(key, value);

        return result;
    }

    private ListenerMethodInvoker newInvoker()
    {
        return newMock(ListenerMethodInvoker.class);
    }
}
