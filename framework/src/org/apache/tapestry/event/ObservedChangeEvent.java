//  Copyright 2004 The Apache Software Foundation
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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.Tapestry;

/**
 * Event which describes a change to a particular {@link IComponent}.
 *
 * @author Howard Ship
 * @version $Id$
 * 
 **/

public class ObservedChangeEvent extends EventObject
{
    private IComponent _component;
    private String _propertyName;
    private Object _newValue;

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, char newValue)
    {
        this(component, propertyName, new Character(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, byte newValue)
    {
        this(component, propertyName, new Byte(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, short newValue)
    {
        this(component, propertyName, new Short(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, int newValue)
    {
        this(component, propertyName, new Integer(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, long newValue)
    {
        this(component, propertyName, new Long(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
   public ObservedChangeEvent(IComponent component, String propertyName, double newValue)
    {
        this(component, propertyName, new Double(newValue));
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
   public ObservedChangeEvent(IComponent component, String propertyName, float newValue)
    {
        this(component, propertyName, new Float(newValue));
    }

    /**
     *  Creates the event.  The new value must be null, or be a serializable object.
     *  (It is declared as Object as a concession to the Java 2 collections framework, where
     *  the implementations are serializable but the interfaces (Map, List, etc.) don't
     *  extend Serializable ... so we wait until runtime to check).
     *
     *  @param component The component (not necessarily a page) whose property changed.
     *  @param propertyName the name of the property which was changed.
     *  @param newValue The new value of the property. 
     *
     *  @throws IllegalArgumentException if propertyName is null, or
     *  if the new value is not serializable
     *
     **/

    public ObservedChangeEvent(IComponent component, String propertyName, Object newValue)
    {
        super(component);

        if (propertyName == null)
            throw new IllegalArgumentException(
                Tapestry.format("ObservedChangeEvent.null-property-name", component));

        _component = component;
        _propertyName = propertyName;
        _newValue = newValue;
    }

	/**
	 * @deprecated To be removed in 3.1. Use {@link #ObservedChangeEvent(IComponent, String, Object)} instead.
	 */
    public ObservedChangeEvent(IComponent component, String propertyName, boolean newValue)
    {
        this(component, propertyName, newValue ? Boolean.TRUE : Boolean.FALSE);
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