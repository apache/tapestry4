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

import org.apache.hivemind.Location;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;


/**
 * Tests functionality of {@link TestEventListenerAnnotationWorker}.
 * @author jkuhnert
 */
public class TestEventListenerAnnotationWorker extends BaseAnnotationTestCase
{

    public void testEventConnection()
    {
        Location l = newLocation();
        
        EnhancementOperation op = newOp();
        IComponentSpecification spec = newSpec();
        
        EventListenerAnnotationWorker worker = new EventListenerAnnotationWorker();
        ComponentEventInvoker invoker = new ComponentEventInvoker();
        worker.setComponentEventInvoker(invoker);
        
        replayControls();
        
        Method m = findMethod(AnnotatedPage.class, "eventListener");
        worker.performEnhancement(op, spec, m, l);
        
        verifyControls();
        
        ComponentEventProperty property = invoker.getComponentEvents("email");
        assertNotNull(property);
        
        List listeners = property.getEventListeners("onClick");
        assertNotNull(listeners);
        assertEquals(1, listeners.size());
    }

}
