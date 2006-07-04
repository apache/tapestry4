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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertSame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
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
    public void testGetListener()
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

    public void testGetListenerNames()
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

    public void testCanProvideListener()
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

    public void testMissingListener()
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