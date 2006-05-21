// Copyright May 21, 2006 The Apache Software Foundation
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
package org.apache.tapestry.event;

import java.util.Map;


/**
 * Represents a browser generated event "target". Most browser events will
 * have an event.target property, which is what this represents.
 * 
 * @author jkuhnert
 */
public class EventTarget
{
    private final Map _properties;
    
    /**
     * Creates a new target with an immutable set
     * of properties.
     * 
     * @param properties The properties of the target.
     */
    public EventTarget(Map properties)
    {
        _properties = properties;
    }
    
    /**
     * Gets a target object property. (Could be things like "id" 
     * for html dom nodes, etc..)
     * @param key The key of the property.
     * @return The property value, or null if it doesn't exist.
     */
    public Object get(String key)
    {
        return _properties.get(key);
    }
}
