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

import org.apache.tapestry.event.BrowserEvent;

import java.util.*;


/**
 * Represents a configured listener/event(s) binding for a 
 * a component and the events that may be optionally listened
 * for on the client browser.
 */
public class ComponentEventProperty
{
    private Map _eventMap = new HashMap();
    private Map _formEventMap = new HashMap();
    
    private String _componentId;
    
    /**
     * Creates a new component event property mapped to the specified component id.
     *
     * @param componentId
     *          The component which is the target of all mappings in this property.
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
            String formId, boolean validateForm, boolean async, boolean focus)
    {
        addListener(events, methodName, formId, validateForm, async, focus, false);
    }

    /**
     * Adds a listener bound to the specified client side
     * events.
     * 
     * @param events The javascript events to bind to.
     * @param methodName The method to invoke when triggered.
     * @param formId Optional form to bind event to.
     * @param validateForm Whether or not form client side validation should be performed.
     * @param async  Whether or not the request should be asynchronous.
     * @param focus Whether or not the form should recieve focus events. (if any forms are involved)
     * @param autoSubmit Whether or not {@link org.apache.tapestry.form.IFormComponent}s should have their forms autowired for submission.
     */
    public void addListener(String[] events, String methodName, 
            String formId, boolean validateForm, boolean async, boolean focus, boolean autoSubmit)
    {
        for (int i=0; i < events.length; i++) {
            if (formId != null && formId.length() > 0)
                addFormEventListener(events[i], methodName, formId, validateForm, async, focus);
            else
                addEventListener(events[i], methodName, autoSubmit);
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
            String formId, boolean validateForm, boolean async, boolean focus)
    {
        EventBoundListener listener = new EventBoundListener(methodName, formId, validateForm, _componentId, async, focus);
        
        List listeners = getFormEventListeners(event);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }
    
    /**
     * Adds a listener to the specified client side event.
     * @param event
     * @param methodName
     */
    public void addEventListener(String event, String methodName, boolean autoSubmit)
    {
        EventBoundListener listener = new EventBoundListener(methodName, _componentId);
        
        List listeners = getEventListeners(event);
        if (!listeners.contains(listener))
            listeners.add(listener);
    }

    public void connectAutoSubmitEvents(String formIdPath)
    {
        Iterator it = getEvents().iterator();
        while (it.hasNext()) {
            String key = (String)it.next();

            List listeners = (List) _eventMap.get(key);
            Iterator lit = listeners.iterator();
            while (lit.hasNext()) {
                
                EventBoundListener listener = (EventBoundListener) lit.next();

                listener.setFormId(formIdPath);
                lit.remove();

                List formListeners = getFormEventListeners(key);
                if (!formListeners.contains(listener))
                    formListeners.add(listener);
            }
            
            // remove mapping if empty
            
            if (listeners.size() == 0)
                it.remove();
        }
    }

    /**
     * Replaces all instances of the existing component id mapped for this property with the new
     * {@link org.apache.tapestry.IComponent#getIdPath()} version.
     *
     * @param idPath The component id path.
     */
    public void rewireComponentId(String idPath)
    {
        _componentId = idPath;

        Iterator it = getEvents().iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();

            List listeners = (List)_eventMap.get(key);

            for (int i=0; i < listeners.size(); i++) {

                EventBoundListener listener = (EventBoundListener) listeners.get(i);
                listener.setComponentId(idPath);
            }
        }

        it = getFormEvents().iterator();
        while (it.hasNext())
        {
            String key = (String) it.next();

            List listeners = (List)_formEventMap.get(key);

            for (int i=0; i < listeners.size(); i++) {

                EventBoundListener listener = (EventBoundListener) listeners.get(i);
                listener.setComponentId(idPath);
            }
        }
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
     * 
     * @return The current set of listeners bound to the specified event.
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
     * 
     * @return The current set of listeners that will submit a form bound to the
     *          specified event.
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
     * 
     * @return All mapped form event keys.
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
