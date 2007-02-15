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
package org.apache.tapestry.spec;

import java.util.Map;

import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;


/**
 * Specification for something that can listen to and act on client side generated
 * browser events.
 *
 * @author jkuhnert
 */
public interface IEventListener
{
    /**
     * Adds a deferred event listener binding for the specified component.
     * 
     * @param componentId 
     *          The component this is for.
     * @param events
     *          The events that should cause the listener to be executed.
     * @param methodName
     *          The page/component listener name that should be executed when
     *          one of the supplied events occurs.
     * @param async 
     *          If submitting a form, whether or not to do it asynchronously.
     * @param focus
     *          If submitting a form, controls whether or not to focus it after an update.
     */
    void addEventListener(String componentId, String[] events, 
            String methodName, String formId, boolean validateForm, boolean async, boolean focus);
    
    /**
     * Adds a deferred event listener binding for the specified html element.
     * 
     * @param elementId
     * @param events
     * @param methodName
     * @param async 
     *          If submitting a form, whether or not to do it asynchronously.
     * @param focus
     *          If submitting a form, controls whether or not to focus it after an update.
     */
    void addElementEventListener(String elementId, String[] events, 
            String methodName, String formId, boolean validateForm, boolean async, boolean focus);
    
    /**
     * Checks if any element events are bound to this component.
     * 
     * @return True if any element events are mapped from this component.
     */
    boolean hasElementEvents();
    
    /**
     * Gets component bound event properties.
     * 
     * @param componentId The component to get event listeners for.
     * @return The bound component event property container, or null if none exist.
     */
    ComponentEventProperty getComponentEvents(String componentId);
    
    /**
     * Gets element bound event properties.
     * 
     * @param elementId The element to get listeners for.
     * @return The bound element event property container, or null if none exist.
     */
    ComponentEventProperty getElementEvents(String elementId);
    
    /**
     * Returns a list of element / component bound event listeners that were specified
     * as invoking the form component with a matching id to <code>formId</code> of type
     * {@link EventBoundListener} .
     * 
     * @param formId
     *          The form that the event listeners were bound to submit when the event occurs.
     * @param event
     *           The event that caused the current invocation.
     * 
     * @return A list of events bound to the specified form, empty if none exist.
     */
    EventBoundListener[] getFormEvents(String formId, BrowserEvent event);
    
    /**
     * Gets all mapped element events for this component.
     * 
     * @return Mapped elements events, if any.
     */
    Map getElementEvents();
}
