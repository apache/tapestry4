// Copyright May 14, 2006 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Resource;
import org.apache.tapestry.enhance.EnhancementOperation;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;


/**
 * Performs {@link EventListener} annotation enhancements on components.
 * 
 * @author jkuhnert
 */
public class EventListenerAnnotationWorker implements SecondaryAnnotationWorker
{
    private ComponentEventInvoker _invoker;
    
    /** 
     * {@inheritDoc}
     */
    public boolean canEnhance(Method method)
    {
        return method.getAnnotation(EventListener.class) != null;
    }
    
    /** 
     * {@inheritDoc}
     */
    public void peformEnhancement(EnhancementOperation op, IComponentSpecification spec, 
            Method method, Resource classResource)
    {
        EventListener listener = method.getAnnotation(EventListener.class);
        
        String[] targets = listener.targets();
        String[] elements = listener.elements();
        
        if (targets.length < 1 && elements.length < 1)
            throw new ApplicationRuntimeException(AnnotationMessages.targetsNotFound(method));
        
        for (int i=0; i < targets.length; i++) {
            if (spec.getComponent(targets[i]) == null)
                throw new ApplicationRuntimeException(AnnotationMessages.componentNotFound(method, targets[i]));
            
            _invoker.addEventListener(targets[i], listener.events(), method.getName());
        }
        
        for (int i=0; i < elements.length; i++)
            _invoker.addElementEventListener(elements[i], listener.events(), method.getName());
    }
    
    /**
     * Injected.
     * @param invoker
     */
    public void setComponentEventInvoker(ComponentEventInvoker invoker)
    {
        _invoker = invoker;
    }
}