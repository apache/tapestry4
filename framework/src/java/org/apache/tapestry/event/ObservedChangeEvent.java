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

package org.apache.tapestry.event;

import java.util.EventObject;

import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;

/**
 * Event which describes a change to a particular {@link IComponent}.
 * 
 * @author Howard Ship
 */

public class ObservedChangeEvent extends EventObject
{
    private IComponent _component;

    private String _propertyName;

    private Object _newValue;

    /**
     * Creates the event. The new value must be null, or be a serializable object. (It is declared
     * as Object as a concession to the Java 2 collections framework, where the implementations are
     * serializable but the interfaces (Map, List, etc.) don't extend Serializable ... so we wait
     * until runtime to check).
     * 
     * @param component
     *            The component (not necessarily a page) whose property changed.
     * @param propertyName
     *            the name of the property which was changed.
     * @param newValue
     *            The new value of the property.
     * @throws IllegalArgumentException
     *             if propertyName is null, or if the new value is not serializable
     */

    public ObservedChangeEvent(IComponent component, String propertyName, Object newValue)
    {
        super(component);

        Defense.notNull(propertyName, "propertyName");

        _component = component;
        _propertyName = propertyName;
        _newValue = newValue;
    }

    public IComponent getComponent()
    {
        return _component;
    }

    public Object getNewValue()
    {
        return _newValue;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

}