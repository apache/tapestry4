//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;
import ognl.Ognl;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.listener.ListenerMap;

/**
 *  Tests the {@link org.apache.tapestry.listener.ListenerMap} and
 *  {@link org.apache.tapestry.listener.ListenerMapPropertyAccessor}
 *  classes.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 2.2
 * 
 **/

public class TestListenerMap extends TapestryTestCase
{
    public static class Listener
    {
        public int invokeCount = 0;

        public void nonListenerMethod()
        {
        }

        public String almostListenerMethod(IRequestCycle cycle)
        {
            return null;
        }

        public void actualListenerMethod(IRequestCycle cycle)
        {
            invokeCount++;
        }

        public void listenerThrows(IRequestCycle cycle)
        {
            invokeCount++;
        }

        public static void nearMiss(IRequestCycle cycle)
        {
        }

        static void mustBePublic(IRequestCycle cycle)
        {
        }

        public void invokeAndThrow(IRequestCycle cycle)
        {
            throw new ApplicationRuntimeException("From invokeAndThrow");
        }

        public void invokeAndThrowRuntime(IRequestCycle cycle)
        {
            throw new RuntimeException("From invokeAndThrowRuntime");
        }

        public String toString()
        {
            return "TestListenerMap.Listener[" + invokeCount + "]";
        }
    }

    public static class ListenerSubclass extends Listener
    {
        public void subclassMethod(IRequestCycle cycle)
        {
            invokeCount++;
        }
    }

    private void attempt(String methodName, Listener listener, ListenerMap map)
    {
        int count = listener.invokeCount;

        IActionListener l = (IActionListener) map.getListener(methodName);

        assertNotNull(l);

        try
        {
            l.actionTriggered(null, null);
        }
        catch (ApplicationRuntimeException ex)
        {
            throw new AssertionFailedError("Unexpected ApplicationRuntimeException.");
        }

        assertEquals("Invoke count.", count + 1, listener.invokeCount);
    }

    public void testListenerMethods()
    {
        Listener l = new Listener();
        ListenerMap m = new ListenerMap(l);

        attempt("actualListenerMethod", l, m);
        attempt("listenerThrows", l, m);
    }

    public void testListenerNames()
    {
        Listener l = new Listener();
        ListenerMap m = new ListenerMap(l);

        List names = new ArrayList(m.getListenerNames());
        Collections.sort(names);

        checkList(
            "Method names.",
            new String[] {
                "actualListenerMethod",
                "invokeAndThrow",
                "invokeAndThrowRuntime",
                "listenerThrows" },
            names);
    }

    public void testSubclassMethods()
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);

        attempt("actualListenerMethod", l, m);
        attempt("listenerThrows", l, m);
        attempt("subclassMethod", l, m);
    }

    public void testSubclassListenerNames()
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);

        List names = new ArrayList(m.getListenerNames());
        Collections.sort(names);

        checkList(
            "Method names.",
            new String[] {
                "actualListenerMethod",
                "invokeAndThrow",
                "invokeAndThrowRuntime",
                "listenerThrows",
                "subclassMethod" },
            names);
    }

    public void testListenerMethodPropertyAccess() throws Exception
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);

        IActionListener al = (IActionListener) Ognl.getValue("subclassMethod", m);

        int count = l.invokeCount;

        al.actionTriggered(null, null);

        assertEquals("Invocation count.", count + 1, l.invokeCount);
    }

    public void testPropertyAccess() throws Exception
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);

        // class is a handy, read-only property.

        Class c = (Class) Ognl.getValue("class", m);

        assertEquals("ListenerMap class property.", ListenerMap.class, c);
    }

    public void testInvokeAndThrow() throws Exception
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);
        IActionListener listener = (IActionListener) m.getListener("invokeAndThrow");

        try
        {
            listener.actionTriggered(null, null);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(ex, "From invokeAndThrow");
        }
    }

    public void testInvokeAndThrowRuntime() throws Exception
    {
        Listener l = new ListenerSubclass();
        ListenerMap m = new ListenerMap(l);
        IActionListener listener = (IActionListener) m.getListener("invokeAndThrowRuntime");

        try
        {
            listener.actionTriggered(null, null);

            throw new AssertionFailedError("Unreachable.");
        }
        catch (ApplicationRuntimeException ex)
        {
            checkException(
                ex,
                "Unable to invoke method invokeAndThrowRuntime on TestListenerMap.Listener[0]: From invokeAndThrowRuntime");
        }
    }

    public void testToString() throws Exception
    {
        Listener l = new Listener();
        ListenerMap m = new ListenerMap(l);

        IActionListener listener = (IActionListener) m.getListener("actualListenerMethod");

        assertEquals(
            "ToString",
            "SyntheticListener[TestListenerMap.Listener[0] public void org.apache.tapestry.junit.TestListenerMap$Listener.actualListenerMethod(org.apache.tapestry.IRequestCycle)]",
            listener.toString());

        assertEquals("ToString", "ListenerMap[TestListenerMap.Listener[0]]", m.toString());
    }

    public void testIsCached() throws Exception
    {
        Listener l = new Listener();
        ListenerMap m = new ListenerMap(l);

        IActionListener listener = (IActionListener) m.getListener("actualListenerMethod");

        assertSame("Listener", listener, m.getListener("actualListenerMethod"));
    }

}
