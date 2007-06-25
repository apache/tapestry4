// Copyright 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.engine.ILink;
import org.apache.tapestry.event.BrowserEvent;
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

/**
 * Tests for {@link org.apache.tapestry.listener.ListenerMapSourceImpl}&nbsp;and
 * {@link org.apache.tapestry.listener.ListenerMethodInvokerImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestListenerMapSource extends BaseComponentTestCase
{

    private IRequestCycle newLCycle(Object[] listenerParameters)
    {
        IRequestCycle cycle = newCycle();

        expect(cycle.getListenerParameters()).andReturn(listenerParameters);

        return cycle;
    }

    private Method findMethod(Class clazz, String name)
    {
        Method[] methods = clazz.getMethods();

        for(int i = 0; i < methods.length; i++)
        {
            if (methods[i].getName().equals(name)) return methods[i];
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
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.fred("Hello", 7);

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("fred").actionTriggered(null, cycle);

        verify();
    }

    public void testFoundWithCycleAndParameters()
    {
        IRequestCycle cycle = newLCycle(new Object[] { new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.wilma(cycle, 7);

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("wilma").actionTriggered(null, cycle);

        verify();
    }
    
    public void testFoundWithAllParameters()
    {
        BrowserEvent event = new BrowserEvent("onClick", null);
        IRequestCycle cycle = newLCycle(new Object[] { new Integer(8), event });
        ListenerMethodHolder holder = newHolder();
        
        holder.bangbangClicked(cycle, event, 8);
        
        replay();
        
        ListenerMapSource source = new ListenerMapSourceImpl();
        
        ListenerMap map = source.getListenerMapForObject(holder);
        
        map.getListener("bangbangClicked").actionTriggered(null, cycle);

        verify();
    }
    
    /**
     * No exact match on parameter count, fall through to the no-arguments
     * method implementation.
     */

    public void testNoParameterMatch()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });
        ListenerMethodHolder holder = newHolder();

        holder.barney();

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("barney").actionTriggered(null, cycle);

        verify();
    }

    public void testFallbackToJustCycle()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });

        ListenerMethodHolder holder = newHolder();

        holder.pebbles(cycle);

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("pebbles").actionTriggered(null, cycle);

        verify();
    }

    public void testReturnPageName()
    {
        IRequestCycle cycle = newLCycle(null);
        ListenerMethodHolder holder = new ListenerMethodHolder("PageName");

        cycle.activate("PageName");

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsPageName").actionTriggered(null, cycle);

        verify();
    }

    public void testReturnLink()
    {
        IRequestCycle cycle = newLCycle(null);
        
        ILink link = newLink("http://foo/bar");
        
        cycle.sendRedirect("http://foo/bar");
        
        ListenerMethodHolder holder = new ListenerMethodHolder(link);

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsLink").actionTriggered(null, cycle);

        verify();
    }

    private ILink newLink(String absoluteURL)
    {
        ILink link = newMock(ILink.class);

        expect(link.getAbsoluteURL()).andReturn(absoluteURL);

        return link;
    }

    public void testReturnPageInstance()
    {
        IPage page = newMock(IPage.class);
        IRequestCycle cycle = newLCycle(null);
        ListenerMethodHolder holder = new ListenerMethodHolder(page);

        cycle.activate(page);

        replay();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("returnsPage").actionTriggered(null, cycle);

        verify();
    }

    @Test(expectedExceptions = ApplicationRuntimeException.class)
    public void test_No_Match()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });

        replay();

        ListenerMethodHolder holder = new ListenerMethodHolder();

        ListenerMapSource source = new ListenerMapSourceImpl();

        ListenerMap map = source.getListenerMapForObject(holder);

        map.getListener("noMatchFound").actionTriggered(null, cycle);

        verify();
    }

    public void testMismatchedTypes()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello" });

        replay();

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
            assertEquals(IllegalArgumentException.class, ex.getRootCause().getClass());
            assertTrue(ex.getMessage().startsWith(
                    "Failure invoking listener method 'public void "
                            + "org.apache.tapestry.listener.ListenerMethodHolder."
                            + "wrongTypes(java.util.Map)' on ListenerMethodHolder:"));
            
            // TODO: IBM jre doesn't format these messages the same as sun's
            // jre,
            // IBM's message has no message string source for the
            // IllegalArgumentException
            assertSame(holder, ex.getComponent());
        }

        verify();
    }

    public void testInvocationTargetException()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });

        ListenerMethodHolder holder = new ListenerMethodHolder();

        RuntimeException exception = new IllegalArgumentException("Just for kicks.");

        holder.setException(exception);

        replay();

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

        verify();
    }

    public void testInvocationTargetExceptionForApplicationRuntimeException()
    {
        IRequestCycle cycle = newLCycle(new Object[] { "Hello", new Integer(7) });

        ListenerMethodHolder holder = new ListenerMethodHolder();

        RuntimeException exception = new ApplicationRuntimeException("Just for kicks.");

        holder.setException(exception);

        replay();

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

        verify();
    }

    private ListenerMethodHolder newHolder()
    {
        return (ListenerMethodHolder)newInstance(ListenerMethodHolder.class);
    }
}
