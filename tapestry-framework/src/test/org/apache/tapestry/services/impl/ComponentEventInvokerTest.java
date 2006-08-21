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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.EventTarget;
import org.apache.tapestry.form.FormSupport;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.listener.ListenerInvoker;
import org.apache.tapestry.listener.ListenerMap;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link ComponentEventInvoker}.
 * 
 * @author jkuhnert
 */
@Test
public class ComponentEventInvokerTest extends BaseComponentTestCase
{
    
    public void test_Event_Properties()
    {
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        
        invoker.addEventListener("comp1", new String[] {"onClick"}, "testFoo", 
                null, false, false);
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
    
    public void test_Invoke_Component_Listener()
    {
        IRequestCycle cycle = newCycle();
        IComponent comp = newComponent();
        ListenerInvoker listenerInvoker = newMock(ListenerInvoker.class);
        ListenerMap listenerMap = newMock(ListenerMap.class);
        
        IActionListener listener1 = newMock(IActionListener.class);
        
        Map tprops = new HashMap();
        tprops.put("id", "testId");
        BrowserEvent event = new BrowserEvent("onSelect", new EventTarget(tprops));
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        invoker.addEventListener("testId", new String[] { "onSelect" }, 
                "fooListener", null, false, false);
        
        expect(comp.getId()).andReturn("testId");
        
        expect(comp.getContainer()).andReturn(comp);
        
        expect(comp.getListeners()).andReturn(listenerMap);
        
        expect(listenerMap.canProvideListener("fooListener")).andReturn(Boolean.TRUE);
        
        expect(comp.getListeners()).andReturn(listenerMap);
        
        expect(listenerMap.getListener("fooListener")).andReturn(listener1);
        
        listenerInvoker.invokeListener(listener1, comp, cycle);
        
        replay();
        
        invoker.invokeListeners(comp, cycle, event);
        
        verify();
    }
    
    public void test_Invoke_Element_Listener()
    {
        IRequestCycle cycle = newCycle();
        IComponent comp = newComponent();
        ListenerInvoker listenerInvoker = newMock(ListenerInvoker.class);
        ListenerMap listenerMap = newMock(ListenerMap.class);
        IActionListener listener = newMock(IActionListener.class);
        
        Map tprops = new HashMap();
        tprops.put("id", "testId");
        BrowserEvent event = new BrowserEvent("onSelect", new EventTarget(tprops));
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        invoker.addElementEventListener("testId", new String[] { "onSelect" }, 
                "fooListener", null, false, false);
        
        expect(comp.getId()).andReturn("testId");
        
        expect(comp.getContainer()).andReturn(comp);
        
        expect(comp.getListeners()).andReturn(listenerMap);
        
        expect(listenerMap.canProvideListener("fooListener")).andReturn(Boolean.TRUE);
        
        expect(comp.getListeners()).andReturn(listenerMap);
        
        expect(listenerMap.getListener("fooListener")).andReturn(listener);
        
        listenerInvoker.invokeListener(listener, comp, cycle);
        
        replay();
        
        invoker.invokeListeners(comp, cycle, event);
        
        verify();
    }
    
    public void test_Invoke_Form_Listener()
    {
        IRequestCycle cycle = newCycle();
        IForm form = newForm();
        FormSupport formSupport = newMock(FormSupport.class);
        
        ListenerInvoker listenerInvoker = newMock(ListenerInvoker.class);
        ListenerMap listenerMap = newMock(ListenerMap.class);
        IActionListener listener = newMock(IActionListener.class);
        //IActionListener listener2 = newMock(IActionListener.class);
        
        Map tprops = new HashMap();
        tprops.put("id", "testId");
        BrowserEvent event = new BrowserEvent("onSelect", new EventTarget(tprops));
        
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        invoker.setListenerInvoker(listenerInvoker);
        
        invoker.addEventListener("testId", new String[] { "onSelect" }, "fooListener",
                "form1", false, false);
        
        invoker.addEventListener("testId2", new String[] { "onSelect" }, "fooListener2",
                "form1", false, false);
        
        expect(formSupport.getForm()).andReturn(form);
        
        expect(form.getId()).andReturn("form1");
        
        expect(form.getContainer()).andReturn(form).anyTimes();
        
        expect(form.getListeners()).andReturn(listenerMap);
        
        expect(listenerMap.getListener("fooListener")).andReturn(listener);
        
        form.addDeferredRunnable(isA(Runnable.class));
        
        replay();
        
        invoker.invokeFormListeners(formSupport, cycle, event);
        
        verify();
    }
    
    
}
