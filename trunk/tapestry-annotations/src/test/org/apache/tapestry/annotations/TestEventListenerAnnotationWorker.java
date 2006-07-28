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

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link TestEventListenerAnnotationWorker}.
 * @author jkuhnert
 */
@Test
public class TestEventListenerAnnotationWorker extends BaseAnnotationTestCase
{

    public void testEventConnection()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        worker.setComponentEventInvoker(invoker);
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "eventListener");
        
        assertTrue(worker.canEnhance(m));
        assertFalse(worker.canEnhance(findMethod(AnnotatedPage.class, "getPersistentProperty")));
        worker.peformEnhancement(op, spec, m, resource);
        
        verify();
        
        ComponentEventProperty property = invoker.getComponentEvents("email");
        assertNotNull(property);
        
        List listeners = property.getEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
        
        property = invoker.getElementEvents("foo");
        assertNotNull(property);
        
        listeners = property.getEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
    }
    
    public void testFormEventConnection()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        worker.setComponentEventInvoker(invoker);
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "formListener");
        
        assertTrue(worker.canEnhance(m));
        worker.peformEnhancement(op, spec, m, resource);
        
        verify();
        
        ComponentEventProperty property = invoker.getComponentEvents("email");
        assertNotNull(property);
        
        List listeners = property.getFormEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
        EventBoundListener formListener = (EventBoundListener)listeners.get(0);
        assertEquals("testForm", formListener.getFormId());
        assertFalse(formListener.isValidateForm());
    }
    
    public void testTargetsNotFound()
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
    
    public void testCanEnhance()
    {
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        
        replay();
        
        Method m = findMethod(AnnotatedPage.class, "getDefaultPageSize");
        
        assertFalse(worker.canEnhance(m));
        
        verify();
    }
}
