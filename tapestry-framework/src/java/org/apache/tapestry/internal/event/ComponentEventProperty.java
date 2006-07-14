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
package org.apache.tapestry.internal.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry.event.BrowserEvent;


/**
 * Represents a configured listener/event(s) binding for a 
 * a component and the events that may be optionally listened
 * for on the client browser.
 * 
 * @author jkuhnert
 */
public class ComponentEventProperty
{
    private Map _eventMap = new HashMap();
    private Map _formEventMap = new HashMap();
    
    private String _componentId;
    
    /**
     * Creates a new component event property with
     * the specified component id.
     * @param componentId
     */
    public ComponentEventProperty(String componentId)
    {
        _componentId = componentId;
    }
    
    /**
     * Adds a listener bound to the specified client side
     * events.
     * @param events
     * @param methodName
     * @param async 
     */
    public void addListener(String[] events, String methodName, 
            String formId, boolean validateForm, boolean async)
    {
        for (int i=0; i < events.length; i++) {
            if (formId != null && formId.length() > 0)
                addFormEventListener(events[i], methodName, formId, validateForm, async);
            else
                addEventListener(events[i], methodName);
        }
    }
    
    /**
     * Adds a form listener to the specified client side event.
     * @param event
     * @param methodName
     * @param formId 
     * @param validateForm
     */
    public void addFormEventListener(String event, String methodName,
            String formId, boolean validateForm, boolean async)
    {
        EventBoundListener listener = 
            new EventBoundListener(methodName, formId, validateForm, _componentId, async);
        
        List listeners = getFormEventListeners(event);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }
    
    /**
     * Adds a listener to the specified client side event.
     * @param event
     * @param methodName
     */
    public void addEventListener(String event, String methodName)
    {
        EventBoundListener listener = 
            new EventBoundListener(methodName, _componentId);
        
        List listeners = getEventListeners(event);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }
    
    /**
     * @return the componentId
     */
    public String getComponentId()
    {
        return _componentId;
    }

    /**
     * Gets the current list of listeners for a specific event,
     * creates a new instance if one doesn't exist already.
     * 
     * @param event
     * @return
     */
    public List getEventListeners(String event)
    {
        List listeners = (List)_eventMap.get(event);
        if (listeners == null) {
            listeners = new ArrayList();
            _eventMap.put(event, listeners);
        }
        
        return listeners;
    }
    
    /**
     * Gets the current list of listeners for a specific event,
     * creates a new instance if one doesn't exist already.
     * 
     * @param event
     * @return
     */
    public List getFormEventListeners(String event)
    {
        List listeners = (List)_formEventMap.get(event);
        if (listeners == null) {
            listeners = new ArrayList();
            _formEventMap.put(event, listeners);
        }
        
        return listeners;
    }
    
    /**
     * The set of all non form based events.
     * @return The unique set of events.
     */
    public Set getEvents()
    {
        return _eventMap.keySet();
    }
    
    /**
     * The set of all form based listener events.
     * @return
     */
    public Set getFormEvents()
    {
        return _formEventMap.keySet();
    }
    
    /**
     * Creates a list of listeners bound to a particular form
     * and client side browser event. 
     * 
     * @param formId
     *          The form to find listeners for.
     * @param event
     *          The browser event that generated the request.
     * @param append 
     *          The optional list to add the listeners to.
     * @return The list of listeners to invoke for the form and event passed in,
     *          will be empty if none found.
     */
    public List getFormEventListeners(String formId, BrowserEvent event, List append)
    {   
        List ret = (append == null) ? new ArrayList() : append;
        
        List listeners = (List)_formEventMap.get(event.getName());
        if (listeners == null) 
            return ret;
        
        for (int i=0; i < listeners.size(); i++) {
            EventBoundListener listener = (EventBoundListener)listeners.get(i);
            if (listener.getFormId().equals(formId))
                ret.add(listener);
        }
        
        return ret;
    }
}
