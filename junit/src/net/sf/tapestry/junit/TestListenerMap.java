//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
package net.sf.tapestry.junit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.listener.ListenerMap;
import ognl.Ognl;

/**
 *  Tests the {@link net.sf.tapestry.listener.ListenerMap} and
 *  {@link net.sf.tapestry.listener.ListenerMapPropertyAccessor}
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

        public void notQuiteListenerMethod(IRequestCycle cycle) throws BindingException
        {
        }

        public void actualListenerMethod(IRequestCycle cycle)
        {
            invokeCount++;
        }

        public void listenerThrows(IRequestCycle cycle) throws RequestCycleException
        {
            invokeCount++;
        }
    }

    public static class ListenerSubclass extends Listener
    {
        public void subclassMethod(IRequestCycle cycle)
        {
            invokeCount++;
        }
    }

    public TestListenerMap(String name)
    {
        super(name);
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
        catch (RequestCycleException ex)
        {
            throw new AssertionFailedError("Unexpected RequestCycleException.");
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

        checkList("Method names.", new String[] { "actualListenerMethod", "listenerThrows" }, names);
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

        checkList("Method names.", new String[] { "actualListenerMethod", "listenerThrows", "subclassMethod" }, names);
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
        
        Class c = (Class)Ognl.getValue("class", m);
        
        assertEquals("ListenerMap class property.", ListenerMap.class, c);
    }
       

}
