// Copyright Jun 3, 2006 The Apache Software Foundation
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
package org.apache.tapestry.internal.event;

import org.apache.tapestry.TestBase;
import org.apache.tapestry.event.BrowserEvent;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 * Tests for {@link ComponentEventProperty} and {@link EventBoundListener}.
 * 
 * @author jkuhnert
 */
@Test
public class ComponentEventPropertyTest extends TestBase {

    public void test_Add_Event_Listener()
    {
        String[] events = {"onClick", "onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", null, false, false, false);
        
        assertEquals("compid", prop.getComponentId());
        assertEquals(2, prop.getEvents().size());
        assertEquals(0, prop.getFormEvents().size());
        
        assertNotNull(prop.getEventListeners("onClick"));
        
        List listeners = prop.getEventListeners("onClick");
        assertEquals(1, listeners.size());
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals("compid", listener.getComponentId());
        assertNull(listener.getFormId());
        assertEquals("doFoo", listener.getMethodName());
        assert listener.isAutoSubmit();
    }
    
    public void test_Add_Form_Event_Listener()
    {
        String[] events = {"onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", "formid", false, true, false);
        
        assertEquals("compid", prop.getComponentId());
        assertEquals(0, prop.getEvents().size());
        assertEquals(1, prop.getFormEvents().size());
        
        assertNotNull(prop.getFormEventListeners("onFoo"));
        
        List listeners = prop.getFormEventListeners("onFoo");
        assertEquals(1, listeners.size());
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals("compid", listener.getComponentId());
        assertEquals("formid", listener.getFormId());
        assertFalse(listener.isValidateForm());
        assertTrue(listener.isAsync());
        
        assertEquals(1, prop.getFormEvents().size());
        
        assertEquals("doFoo", listener.getMethodName());
        assert listener.isAutoSubmit();
    }
    
    public void test_Add_Multiple_Event_Listener()
    {
        String[] events = {"onClick", "onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", "form1", false, false, false);
        prop.addListener(new String[]{"onchange"}, "doBar", "form2", false, false, false);
        prop.addListener(new String[]{"onchange"}, "secondForm", "form1", false, false, false);
        
        assertEquals("compid", prop.getComponentId());
        assertEquals(prop.getEvents().size(), 0);
        assertEquals(prop.getFormEvents().size(), 3);
        
        Set s = prop.getFormEvents();
        String[] fevents = (String[])s.toArray(new String[s.size()]);
        
        assertEquals(fevents.length, 3);
        
        List listeners = prop.getFormEventListeners("onchange");
        assertEquals(listeners.size(), 2);
        
        listeners = prop.getFormEventListeners("onClick");
        assertEquals(listeners.size(), 1);
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals("compid", listener.getComponentId());
        assertEquals("form1", listener.getFormId());
        assertEquals("doFoo", listener.getMethodName());
        
    }
    
    public void test_Get_Form_Events()
    {
        String[] events = {"onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        BrowserEvent event = new BrowserEvent("onFoo", null);
        
        prop.addListener(events, "doFoo", "formid", false, false, true);
        
        List listeners = prop.getFormEventListeners("formid", event, null);
        assertEquals(1, listeners.size());
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals("compid", listener.getComponentId());
        assertEquals("formid", listener.getFormId());
        assertFalse(listener.isValidateForm());
        
        assertEquals("doFoo", listener.getMethodName());
        assertTrue(listener.shouldFocusForm());
    }

    public void test_Connect_Auto_Submit_Events()
    {
        String[] events = {"onClick"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", null, false, false, false, true);

        assertEquals("compid", prop.getComponentId());
        assertEquals(prop.getEvents().size(), 1);
        assertEquals(prop.getFormEvents().size(), 0);
        
        assertNotNull(prop.getEventListeners("onClick"));

        List listeners = prop.getEventListeners("onClick");
        assertEquals(listeners.size(), 1);
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals(listener.getComponentId(), "compid");
        assertNull(listener.getFormId());
        assertEquals(listener.getMethodName(), "doFoo");
        assert listener.isAutoSubmit();

        prop.connectAutoSubmitEvents("form");

        assertEquals(prop.getEvents().size(), 0);
        assertEquals(prop.getFormEvents().size(), 1);

        listeners = prop.getFormEventListeners("onClick");
        assertEquals(listeners.size(), 1);

        listener = (EventBoundListener)listeners.get(0);
        assertEquals(listener.getComponentId(), "compid");
        assertEquals(listener.getFormId(), "form");
        assertEquals(listener.getMethodName(), "doFoo");
    }

    public void test_ReWire_Component_Id()
    {
        String[] events = {"onClick", "onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");

        prop.addListener(events, "doFoo", null, false, false, false);
        prop.addListener(new String[]{"onchange"}, "doBar", "form2", false, false, false);
        prop.addListener(new String[]{"onchange"}, "secondForm", "form1", false, false, false);
        
        assertEquals("compid", prop.getComponentId());
        assertEquals(prop.getEvents().size(), 2);
        assertEquals(prop.getFormEvents().size(), 1);

        String path = "new/Path/Id";
        prop.rewireComponentId(path);

        assertEquals(prop.getComponentId(), path);

        Iterator it = prop.getEvents().iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();

            List listeners = prop.getEventListeners(key);
            for (int i=0; i < listeners.size(); i++) {

                EventBoundListener listener = (EventBoundListener) listeners.get(i);
                assertEquals(listener.getComponentId(), path);
            }
        }

        it = prop.getFormEvents().iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();

            List listeners = prop.getFormEventListeners(key);
            for (int i=0; i < listeners.size(); i++) {

                EventBoundListener listener = (EventBoundListener) listeners.get(i);
                assertEquals(listener.getComponentId(), path);
            }
        }
    }
}
