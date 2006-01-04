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

import java.lang.reflect.Method;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.listener.ListenerMapSourceImpl}&nbsp;and
 * {@link org.apache.tapestry.listener.ListenerMethodInvokerImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestListenerMapSource extends HiveMindTestCase
{
    private IRequestCycle newCycle(Object[] listenerParameters)
    {
        MockControl control = newControl(IRequestCycle.class);
        IRequestCycle cycle = (IRequestCycle) control.getMock();

        cycle.getListenerParameters();
        control.setReturnValue(listenerParameters);

        return cycle;
    }

    private Method findMethod(Class clazz, String name)
    {
        Method[] methods = clazz.getMethods();

        for (int i = 0; i < methods.length; i++)
        {
            if (methods[i].getName().equals(name))
                return methods[i];
        }

        throw new IllegalArgumentException("No method '" + name + "' in " + clazz + ".");
    }

    private void attemptReturnType(boolean expected, Class clazz, String methodName)
    {
        Method m = findMethod(clazz, methodName);

        ListenerMapSourceImpl lms = new ListenerMapSourceImpl();

        assertEquals(expected, lms.isAcceptibleListenerMethodReturnType(m));
    }

    public void testAcceptibleListenerMethodReturnTypes()
    {
        Class clazz = ListenerMethodHolder.class;

        attemptReturnType(true, clazz, "fred");
        attemptReturnType(true, clazz, "returnsString");
        attemptReturnType(true, clazz, "returnsBasePage");
        attemptReturnType(false, clazz, "returnsObject");
        attemptReturnType(false, clazz, "returnsInt");
        attemptReturnType(true, clazz, "returnsLink");
    }

    public void testFoundWithParameters()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.fred("Hello", 7);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("fred").actionTriggered(null, cycle);

        verifyControls();
    }

    public void testFoundWithCycleAndParameters()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.wilma(cycle, 7);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("wilma").actionTriggered(null, cycle);

        verifyControls();
    }

    /**
     * No exact match on parameter count, fall through to the no-arguments method implementation.
     */

    public void testNoParameterMatch()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.barney();

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("barney").actionTriggered(null, cycle);

        verifyControls();
    }

    public void testFallbackToJustCycle()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });

        ListenerMethodHolder holder = newHolder();

        holder.pebbles(cycle);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("pebbles").actionTriggered(null, cycle);

        verifyControls();
    }

    public void testReturnPageName()
    {
        IRequestCycle cycle = newCycle(null);
        ListenerMethodHolder holder = new ListenerMethodHolder("PageName");

        cycle.activate("PageName");

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsPageName").actionTriggered(null, cycle);

        verifyControls();
    }

    public void testReturnLink()
    {
        ILink link = newLink("http://foo/bar");
        
        IRequestCycle cycle = newCycle(null);
        
        cycle.sendRedirect("http://foo/bar");
        
        ListenerMethodHolder holder = new ListenerMethodHolder(link);
        
        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsLink").actionTriggered(null, cycle);

        verifyControls();    
    }

    private ILink newLink(String absoluteURL)
    {
        MockControl control = newControl(ILink.class);
        ILink link = (ILink) control.getMock();

        link.getAbsoluteURL();
        control.setReturnValue(absoluteURL);

        return link;
    }

    public void testReturnPageInstance()
    {
        IPage page = (IPage) newMock(IPage.class);
        IRequestCycle cycle = newCycle(null);
        ListenerMethodHolder holder = new ListenerMethodHolder(page);

        cycle.activate(page);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsPage").actionTriggered(null, cycle);

        verifyControls();
    }

    public void testNoMatch()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });

        replayControls();

        ListenerMethodHolder holder = new ListenerMethodHolder();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        try
        {
            map.getListener("noMatch").actionTriggered(null, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "No listener method named 'noMatch' suitable for 2 listener parameters found in ListenerMethodHolder.",
                    ex.getMessage());
            assertSame(holder, ex.getComponent());
        }

        verifyControls();
    }

    public void testMismatchedTypes()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello" });

        replayControls();

        ListenerMethodHolder holder = new ListenerMethodHolder();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        try
        {
            map.getListener("wrongTypes").actionTriggered(null, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Failure invoking listener method 'public void org.apache.tapestry.listener.ListenerMethodHolder.wrongTypes(java.util.Map)' on ListenerMethodHolder: argument type mismatch",
                    ex.getMessage());
            assertSame(holder, ex.getComponent());
        }

        verifyControls();
    }

    public void testInvocationTargetException()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });

        ListenerMethodHolder holder = new ListenerMethodHolder();

        RuntimeException exception = new IllegalArgumentException("Just for kicks.");

        holder.setException(exception);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        try
        {
            map.getListener("throwsException").actionTriggered(null, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(
                    "Failure invoking listener method 'public void org.apache.tapestry.listener.ListenerMethodHolder.throwsException()' on ListenerMethodHolder: Just for kicks.",
                    ex.getMessage());
            assertSame(holder, ex.getComponent());
            assertSame(exception, ex.getRootCause());
        }

        verifyControls();
    }

    public void testInvocationTargetExceptionForApplicationRuntimeException()
    {
        IRequestCycle cycle = newCycle(new Object[]
        { "Hello", new Integer(7) });

        ListenerMethodHolder holder = new ListenerMethodHolder();

        RuntimeException exception = new ApplicationRuntimeException("Just for kicks.");

        holder.setException(exception);

        replayControls();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        try
        {
            map.getListener("throwsException").actionTriggered(null, cycle);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertSame(exception, ex);
        }

        verifyControls();
    }

    private ListenerMethodHolder newHolder()
    {
        return (ListenerMethodHolder) newMock(ListenerMethodHolder.class);
    }
}