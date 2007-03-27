// Copyright May 20, 2006 The Apache Software Foundation
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
package org.apache.tapestry.annotations;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.internal.event.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.List;


/**
 * Tests functionality of {@link TestEventListenerAnnotationWorker}.
 * @author jkuhnert
 */
@Test
public class TestEventListenerAnnotationWorker extends BaseAnnotationTestCase
{

    public void test_Event_Connection()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        Resource resource = newResource(AnnotatedPage.class);
        
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        worker.setInvoker(invoker);
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "eventListener");
        
        assertTrue(worker.canEnhance(m));
        assertFalse(worker.canEnhance(findMethod(AnnotatedPage.class, "getPersistentProperty")));
        worker.peformEnhancement(op, spec, m, resource);
        
        verify();
        
        assertEquals(1, invoker.getEventListeners("email").size());
        
        ComponentEventProperty property = spec.getComponentEvents("email");
        assertNotNull(property);
        
        List listeners = property.getEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
        
        property = spec.getElementEvents("foo");
        assertNotNull(property);
        
        listeners = property.getEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
        
        assert !((EventBoundListener)listeners.get(0)).shouldFocusForm();
        assert ((EventBoundListener)listeners.get(0)).isAutoSubmit();
    }
    
    public void test_Form_Event_Connection()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = new ComponentSpecification();
        Resource resource = newResource(AnnotatedPage.class);
        IComponentEventInvoker invoker = new ComponentEventInvoker();
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        worker.setInvoker(invoker);
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "formListener");
        
        assertTrue(worker.canEnhance(m));
        worker.peformEnhancement(op, spec, m, resource);
        
        verify();
        
        assertEquals(1, invoker.getEventListeners("email").size());
        assertEquals(1, invoker.getFormEventListeners("testForm").size());
        
        ComponentEventProperty property = spec.getComponentEvents("email");
        assertNotNull(property);
        
        List listeners = property.getFormEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
        
        EventBoundListener formListener = (EventBoundListener)listeners.get(0);
        assertEquals("testForm", formListener.getFormId());
        assertFalse(formListener.isValidateForm());
        assert formListener.shouldFocusForm();
        assert formListener.isAutoSubmit();
    }
    
    public void test_Targets_Not_Found()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "brokenTargetListener");
        
        assertTrue(worker.canEnhance(m));
        
        try {
            worker.peformEnhancement(op, spec, m, resource);
            unreachable();
        } catch (ApplicationRuntimeException e) {
            assertExceptionSubstring(e, "No targets found for");
        }
        
        verify();
    }
    
    public void test_Can_Enhance()
    {
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "getDefaultPageSize");
        
        assertFalse(worker.canEnhance(m));
        
        verify();
    }
    
}
