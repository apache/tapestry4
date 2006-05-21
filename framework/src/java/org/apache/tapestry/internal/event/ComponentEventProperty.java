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
    
    /**
     * Adds a listener bound to the specified client side
     * events.
     * @param events
     * @param methodName
     */
    public void addListener(String[] events, String methodName)
    {
        for (int i=0; i < events.length; i++) {
            addEventListener(events[i], methodName);
        }
    }
    
    /**
     * Adds a listener to the specified client side event.
     * @param event
     * @param methodName
     */
    public void addEventListener(String event, String methodName)
    {
        List listeners = getEventListeners(event);
        if (!listeners.contains(methodName))
            listeners.add(methodName);
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
     * The set of all events.
     * @return The unique set of events.
     */
    public Set getEvents()
    {
        return _eventMap.keySet();
    }
}
