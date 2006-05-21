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
package org.apache.tapestry.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.listener.ListenerInvoker;


/**
 * Managed and handles invoking listener methods for components 
 * in response to requested event listener invocations.
 * 
 * @author jkuhnert
 */
public class ComponentEventInvoker implements ResetEventListener
{
    private Map _components = new HashMap();
    
    private Map _elements = new HashMap();
    
    private ListenerInvoker _invoker;
    
    /**
     * Causes the configured listeners for the passed component to
     * be invoked.
     * @param component
     *          The component that recieved the invocations.
     * @param cycle
     *          The associated request.
     * @param event
     *          The event that started it all.
     */
    public void invokeListeners(IComponent component, IRequestCycle cycle, 
            BrowserEvent event)
    {
        Defense.notNull(component, "component");
        Defense.notNull(cycle, "cycle");
        Defense.notNull(event, "event");
        
        String id = component.getId();
        
        if (hasEvents(id)) {
            
            ComponentEventProperty prop = getComponentEvents(component.getId());
            invokeListeners(prop, component, cycle, event);
        }
        
        // else, may be an element invoked event
        String targetId = (String)event.getTarget().get("id");
        
        if (hasElementEvents(targetId)) {
            
            ComponentEventProperty prop = getElementEvents(component.getId());
            invokeListeners(prop, component, cycle, event);
        }
    }
    
    void invokeListeners(ComponentEventProperty prop, IComponent component, 
            IRequestCycle cycle, BrowserEvent event)
    {
        List listeners = prop.getEventListeners(event.getName());
        for (int i=0; i < listeners.size(); i++) {
            String methodName = (String)listeners.get(i);
            
            IComponent container = component.getContainer();
            if (container == null) // only IPage has no container
                container = component; 
            
            IActionListener listener = component.getListeners().getListener(methodName);
            _invoker.invokeListener(listener, component, cycle);
        }
    }
    
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
     */
    public void addEventListener(String componentId, String[] events, String methodName)
    {
        ComponentEventProperty property = getComponentEvents(componentId);
        
        property.addListener(events, methodName);
    }
    
    /**
     * Adds a deferred event listener binding for the specified html element.
     * 
     * @param elementId
     * @param events
     * @param methodName
     */
    public void addElementEventListener(String elementId, String[] events, String methodName)
    {
        ComponentEventProperty property = getElementEvents(elementId);
        
        property.addListener(events, methodName);
    }
    
    /**
     * Returns whether or not the specified component has any
     * connected events.
     * 
     * @param componentId
     *          The unique component id to check.
     * @return True, if at least one listener is connected to the specified
     *          component.
     */
    public boolean hasEvents(String componentId)
    {
        return _components.get(componentId) != null;
    }
    
    /**
     * 
     * @return True, if any html element events exist.
     */
    public boolean hasElementEvents()
    {
        return _elements.size() > 0;
    }
    
    /**
     * 
     * @return True, if the html element has events.
     */
    public boolean hasElementEvents(String id)
    {
        return _elements.get(id) != null;
    }
    
    /**
     * Gets event properties fro the specified component, creates a new
     * instance if one doesn't exist already.
     * 
     * @param id The component id 
     * @return A new/existing instance.
     */
    public ComponentEventProperty getComponentEvents(String id)
    {
        ComponentEventProperty prop = (ComponentEventProperty)_components.get(id);
        if (prop == null) {
            prop = new ComponentEventProperty();
            _components.put(id, prop);
        }
        
        return prop;
    }
    
    /**
     * Gets event properties fro the specified component, creates a new
     * instance if one doesn't exist already.
     * 
     * @param id The component id 
     * @return A new/existing instance.
     */
    public ComponentEventProperty getElementEvents(String id)
    {
        ComponentEventProperty prop = (ComponentEventProperty)_elements.get(id);
        if (prop == null) {
            prop = new ComponentEventProperty();
            _elements.put(id, prop);
        }
        
        return prop;
    }
    
    public void resetEventDidOccur()
    {
        _components.clear();
        _elements.clear();
    }
    
    /**
     * Injected.
     * @param invoker
     */
    public void setListenerInvoker(ListenerInvoker invoker)
    {
        _invoker = invoker;
    }
}
