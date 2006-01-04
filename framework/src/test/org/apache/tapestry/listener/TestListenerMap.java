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
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;

/**
 * Tests for {@link org.apache.tapestry.listener.ListenerMapImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestListenerMap extends HiveMindTestCase
{
    public void testGetListener()
    {
        Object target = new Object();
        IRequestCycle cycle = newCycle();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        invoker.invokeListenerMethod(target, cycle);

        replayControls();

        ListenerMap lm = new ListenerMapImpl(target, map);

        IActionListener l1 = lm.getListener("method");

        l1.actionTriggered(null, cycle);

        verifyControls();

        IActionListener l2 = lm.getListener("method");

        assertSame(l1, l2);
    }

    public void testGetListenerNames()
    {
        Object target = new Object();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replayControls();

        ListenerMap lm = new ListenerMapImpl(target, map);

        // Copy both collections into ArrayLists for comparison purposes.

        assertEquals(new ArrayList(map.keySet()), new ArrayList(lm.getListenerNames()));

        verifyControls();

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

    public void testCanProvideListener()
    {
        Object target = new Object();
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replayControls();

        ListenerMap lm = new ListenerMapImpl(target, map);

        assertEquals(true, lm.canProvideListener("method"));
        assertEquals(false, lm.canProvideListener("foobar"));

        verifyControls();
    }

    public void testMissingListener()
    {
        Object target = "*TARGET*";
        ListenerMethodInvoker invoker = newInvoker();
        Map map = newMap("method", invoker);

        replayControls();

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

        verifyControls();
    }

    private Map newMap(Object key, Object value)
    {
        Map result = new HashMap();

        result.put(key, value);

        return result;
    }

    private ListenerMethodInvoker newInvoker()
    {
        return (ListenerMethodInvoker) newMock(ListenerMethodInvoker.class);
    }

    private IRequestCycle newCycle()
    {
        return (IRequestCycle) newMock(IRequestCycle.class);
    }
}