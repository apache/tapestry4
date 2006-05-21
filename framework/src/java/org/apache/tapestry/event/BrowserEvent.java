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
package org.apache.tapestry.event;


/**
 * Represents a client side generated browser event.
 * 
 * @author jkuhnert
 */
public class BrowserEvent
{
    private String _name;
    
    public BrowserEvent(String name)
    {
        _name = name;
    }
    
    /**
     * The name of the event that was generated. 
     * 
     * <p>
     * Examples would be <code>onClick,onSelect,onLoad,etc...</code>.
     * </p>
     * @return The event name.
     */
    public String getName()
    {
        return _name;
    }
}
