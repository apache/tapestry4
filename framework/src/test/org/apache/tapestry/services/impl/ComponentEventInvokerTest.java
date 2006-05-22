// Copyright May 21, 2006 The Apache Software Foundation
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
package org.apache.tapestry.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.EventTarget;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.listener.ListenerMap;


/**
 * Tests functionality of {@link ComponentEventInvoker}.
 * 
 * @author jkuhnert
 */
public class ComponentEventInvokerTest extends BaseComponentTestCase
{
    
    public void testEventProperties()
    {
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        
        invoker.addEventListener("comp1", new String[] {"onClick"}, "testFoo");
        assertTrue(invoker.hasEvents("comp1"));
        
        ComponentEventProperty prop = invoker.getComponentEvents("comp1");
        assertNotNull(prop);
        assertEquals(prop.getEventListeners("onClick").size(), 1);
        
        // ensure valid props always returned
        prop = invoker.getComponentEvents("comp2");
        assertNotNull(prop);
        assertEquals(prop.getEvents().size(), 0);
        
        List listeners = prop.getEventListeners("nonExistant");
        assertNotNull(listeners);
        assertEquals(listeners.size(), 0);
    }
    
    public void testInvokeComponentListener()
    {
        IRequestCycle cycle = newCycle();
        IComponent comp = newComponent();
        ListenerInvoker listenerInvoker = (ListenerInvoker)newMock(ListenerInvoker.class);
        ListenerMap listenerMap = (ListenerMap)newMock(ListenerMap.class);
        IActionListener listener = (IActionListener)newMock(IActionListener.class);
        
        Map tprops = new HashMap();
        tprops.put("id", "testId");
        BrowserEvent event = new BrowserEvent("onSelect", new EventTarget(tprops));
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        invoker.addEventListener("testId", new String[] { "onSelect" }, "fooListener");
        
        comp.getId();
        setReturnValue(comp, "testId");
        comp.getContainer();
        setReturnValue(comp, comp);
        comp.getListeners();
        setReturnValue(comp, listenerMap);
        
        listenerMap.getListener("fooListener");
        setReturnValue(listenerMap, listener);
        listenerInvoker.invokeListener(listener, comp, cycle);
        
        replayControls();
        
        invoker.invokeListeners(comp, cycle, event);
        
        verifyControls();
    }
    
    public void testInvokeElementListener()
    {
        IRequestCycle cycle = newCycle();
        IComponent comp = newComponent();
        ListenerInvoker listenerInvoker = (ListenerInvoker)newMock(ListenerInvoker.class);
        ListenerMap listenerMap = (ListenerMap)newMock(ListenerMap.class);
        IActionListener listener = (IActionListener)newMock(IActionListener.class);
        
        Map tprops = new HashMap();
        tprops.put("id", "testId");
        BrowserEvent event = new BrowserEvent("onSelect", new EventTarget(tprops));
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        invoker.addElementEventListener("testId", new String[] { "onSelect" }, "fooListener");
        
        comp.getId();
        setReturnValue(comp, "testId");
        comp.getContainer();
        setReturnValue(comp, comp);
        comp.getListeners();
        setReturnValue(comp, listenerMap);
        
        listenerMap.getListener("fooListener");
        setReturnValue(listenerMap, listener);
        listenerInvoker.invokeListener(listener, comp, cycle);
        
        replayControls();
        
        invoker.invokeListeners(comp, cycle, event);
        
        verifyControls();
    }
}
