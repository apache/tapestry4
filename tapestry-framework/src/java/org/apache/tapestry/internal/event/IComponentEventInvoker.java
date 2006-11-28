// Copyright 2004, 2005 The Apache Software Foundation
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

import java.util.List;

import org.apache.tapestry.IComponent;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.form.FormSupport;
import org.apache.tapestry.spec.IComponentSpecification;


/**
 * Managed and handles invoking listener methods for components 
 * in response to requested event listener invocations.
 *
 * @author jkuhnert
 */
public interface IComponentEventInvoker extends ResetEventListener
{
    /**
     * Adds a listener mapping for events related to the specified 
     * component.
     * 
     * @param componentId 
     *          The component to map a listener with. 
     * @param listener 
     *          The listener.
     */
    void addEventListener(String componentId, IComponentSpecification listener);
    
    /**
     * Returns a list of all known listeners for the specified component.
     * 
     * @param componentId 
     *          The component id to find listeners for.
     * @return The bound listeners, or null if none exist.
     */
    List getEventListeners(String componentId);
    
    /**
     * Adds a mapping for an event listener that should be triggered when the specified
     * form component with id of <code>formId</code> is submitted. This will later
     * be used when the form is submitted to find event listeners bound to fire when 
     * a particular form is submitted. 
     * 
     * @param formId 
     *          The form the event listener is bound to. This is the submitForm parameter
     *          of the EventListener annotation/spec driving an event occurrence to submit a particular form. It doesn't
     *          mean that the actual event target was the form . 
     * 
     * @param listener 
     *          The listener that has form bound event listeners.
     */
    void addFormEventListener(String formId, IComponentSpecification listener);
    
    /**
     * Gets a list of form bound event listeners previously bound via {@link #addFormEventListener(String, IComponentSpecification)}.
     * 
     * @param formId 
     *          The form to get mapped listeners for.
     * @return The list of form event listeners of type {@link IComponentSpecification}, 
     *          if any exist. Null if none exist.
     */
    List getFormEventListeners(String formId);
    
    /**
     * Causes the configured listeners for the passed component to
     * be invoked.
     * 
     * @param component
     *          The component that recieved the invocations.
     * @param cycle
     *          The associated request.
     * @param event
     *          The event that started it all.
     */
    void invokeListeners(IComponent component, final IRequestCycle cycle, final BrowserEvent event);
    
    /**
     * Causes the configured listeners for the passed {@link FormSupport}'s {@link IForm} to
     * be invoked, if mapped to this request/event.
     * 
     * @param formSupport
     *          The form support object being rendered.
     * @param cycle
     *          The associated request.
     * @param event
     *          The event that started it all.
     */
    void invokeFormListeners(FormSupport formSupport, final IRequestCycle cycle, final BrowserEvent event);
}
