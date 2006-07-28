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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.util.List;

import org.apache.tapestry.event.BrowserEvent;
import org.testng.annotations.Test;

import com.javaforge.tapestry.testng.TestBase;


/**
 * Tests for {@link ComponentEventProperty} and {@link EventBoundListener}.
 * 
 * @author jkuhnert
 */
@Test
public class ComponentEventPropertyTest extends TestBase
{

    public void testAddEventListener()
    {
        String[] events = {"onClick", "onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", null, false, false);
        
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
    }
    
    public void testAddFormEventListener()
    {
        String[] events = {"onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        
        prop.addListener(events, "doFoo", "formid", false, true);
        
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
    }
    
    public void testGetFormEvents()
    {
        String[] events = {"onFoo"};
        ComponentEventProperty prop = new ComponentEventProperty("compid");
        BrowserEvent event = new BrowserEvent("onFoo", null);
        
        prop.addListener(events, "doFoo", "formid", false, false);
        
        List listeners = prop.getFormEventListeners("formid", event, null);
        assertEquals(1, listeners.size());
        
        EventBoundListener listener = (EventBoundListener)listeners.get(0);
        assertEquals("compid", listener.getComponentId());
        assertEquals("formid", listener.getFormId());
        assertFalse(listener.isValidateForm());
        
        assertEquals("doFoo", listener.getMethodName());
    }
}
