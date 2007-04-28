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

import org.apache.tapestry.IForm;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;

import java.util.Map;


/**
 * Specification for something that can listen to and act on client side generated
 * browser events.
 *
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
     * @param formId
     *          The optional id of the form that should be submitted as part of this event invocation.
     * @param validateForm
     *          If a formId was specified, whether or not that form should have client side valiation
     *          invoked during the process.
     * @param async 
     *          If submitting a form, whether or not to do it asynchronously.
     * @param focus
     *          If submitting a form, controls whether or not to focus it after an update.
     * @param autoSubmit
     *          If true - auto form wiring is performed on any component targets implementing {@link org.apache.tapestry.form.IFormComponent} so
     *          that the enclosing form is submitted as part of the event in order to maintain consistent form state as in normal listener method
     *          invocations.
     */
    void addEventListener(String componentId, String[] events, String methodName,
                          String formId, boolean validateForm, boolean async, boolean focus, boolean autoSubmit);
    
    /**
     * Adds a deferred event listener binding for the specified html element.
     * 
     * @param elementId
     *          The client side html element id to match against.
     * @param events
     *          The client side events to bind to.
     * @param methodName
     *          The listener that should be invoked when the event happens.
     * @param formId
     *          The optional id of the form that should be submitted as part of this event invocation.
     * @param validateForm
     *          If a formId was specified, whether or not that form should have client side valiation
     *          invoked during the process.
     * @param async 
     *          If submitting a form, whether or not to do it asynchronously.
     * @param focus
     *          If submitting a form, controls whether or not to focus it after an update.
     */
    void addElementEventListener(String elementId, String[] events, 
            String methodName, String formId, boolean validateForm, boolean async, boolean focus);

    /**
     * Invoked during rendering when a component has been detected as a {@link org.apache.tapestry.form.IFormComponent} and may
     * possibly need its events to be wired up as form events.
     *
     * @param componentId The components standard base id.
     * @param form The form containing the component.
     */
    void connectAutoSubmitEvents(String componentId, IForm form);

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

    /**
     * Gets all component event mappings.
     *
     * @return Map of component {@link ComponentEventProperty} values this component is listening to.
     */
    Map getComponentEvents();

    /**
     * Invoked during page load to map event connections previously made via the {@link org.apache.tapestry.IComponent#getId()} identifier
     * to use the more unique {@link org.apache.tapestry.IComponent#getIdPath()}.
     *
     * @param componentId
     *          The basic component id.
     * @param idPath
     *          The id of the component pre-pended with the path of components containing it.
     */
    void rewireComponentId(String componentId, String idPath);

    /**
     * Used during page load to test if this event listener has already had its component / form target
     * id paths resolved.
     *
     * @return True if connections have been resolved, false otherwise.
     */
    boolean getTargetsResolved();

    /**
     * Sets the resolved state to that found, ie what is returned by {@link #getTargetsResolved()}.
     *
     * @param resolved The resolution state.
     */
    void setTargetsResolved(boolean resolved);
}
