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
import org.apache.tapestry.internal.event.IComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;


/**
 * Performs {@link EventListener} annotation enhancements on components.
 * 
 * @author jkuhnert
 */
public class EventListenerAnnotationWorker implements SecondaryAnnotationWorker
{
 
    private IComponentEventInvoker _invoker;
    
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
        String formId = listener.submitForm();
        boolean validateForm = listener.validateForm();
        boolean async = listener.async();
        
        if (targets.length < 1 && elements.length < 1)
            throw new ApplicationRuntimeException(AnnotationMessages.targetsNotFound(method));
        
        for (int i=0; i < targets.length; i++) {
            
            spec.addEventListener(targets[i], listener.events(), 
                    method.getName(), formId, validateForm, async);
            
            _invoker.addEventListener(targets[i], spec);
            
            if (formId != null)
                _invoker.addFormEventListener(formId, spec);
        }
        
        for (int i=0; i < elements.length; i++) {
            
            spec.addElementEventListener(elements[i], listener.events(), 
                    method.getName(), formId, validateForm, async);
            
            if (formId != null)
                _invoker.addFormEventListener(formId, spec);
        }
    }
    
    /** Injected. */
    public void setInvoker(IComponentEventInvoker invoker)
    {
        _invoker = invoker;
    }
}
