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

import java.lang.reflect.Method;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;


/**
 * Tests functionality of {@link TestEventListenerAnnotationWorker}.
 * @author jkuhnert
 */
public class TestEventListenerAnnotationWorker extends BaseAnnotationTestCase
{

    public void testEventConnection()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        IContainedComponent container = (IContainedComponent)newMock(IContainedComponent.class);
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        worker.setComponentEventInvoker(invoker);
        
        spec.getComponent("email");
        setReturnValue(spec, container);
        
        replayControls();
        
        Method m = findMethod(AnnotatedPage.class, "eventListener");
        
        assertTrue(worker.canEnhance(m));
        assertFalse(worker.canEnhance(findMethod(AnnotatedPage.class, "getPersistentProperty")));
        worker.peformEnhancement(op, spec, m, resource);
        
        verifyControls();
        
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
    
    public void testComponentNotFound()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        
        spec.getComponent("email");
        setReturnValue(spec, null);
        
        replayControls();
        
        Method m = findMethod(AnnotatedPage.class, "eventListener");
        
        assertTrue(worker.canEnhance(m));
        
        try {
            worker.peformEnhancement(op, spec, m, resource);
            unreachable();
        } catch (ApplicationRuntimeException e) {
            assertExceptionSubstring(e, "Unable to find component");
        }
        
        verifyControls();
    }
    
    public void testTargetsNotFound()
    {
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        Resource resource = newResource(AnnotatedPage.class);
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        
        replayControls();
        
        Method m = findMethod(AnnotatedPage.class, "brokenTargetListener");
        
        assertTrue(worker.canEnhance(m));
        
        try {
            worker.peformEnhancement(op, spec, m, resource);
            unreachable();
        } catch (ApplicationRuntimeException e) {
            assertExceptionSubstring(e, "No targets found for");
        }
        
        verifyControls();
    }
}
