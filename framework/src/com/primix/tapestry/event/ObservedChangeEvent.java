/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */



/**
 * Event which describes a change to a particular {@link IComponent}.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry.event;

import java.io.Serializable;
import java.util.EventObject;
import com.primix.tapestry.IComponent;

public class ObservedChangeEvent extends EventObject
{
    private IComponent component;
    private String propertyName;
    private Serializable newValue;

    public ObservedChangeEvent(IComponent component, String propertyName, char newValue)
    {
        this(component, propertyName, new Character(newValue));
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, byte newValue)
    {
        this(component, propertyName, new Byte(newValue));
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, short newValue)
    {
        this(component, propertyName, new Short(newValue));
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, int newValue)
    {
        this(component, propertyName, new Integer(newValue));
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, long newValue)
    {
        this(component, propertyName, new Long(newValue));
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, double newValue)
    {
        this(component, propertyName, new Double(newValue));
    }
  
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
	 */
	 
    public ObservedChangeEvent(IComponent component, String propertyName, Object newValue)
    {
        super(component);

		if (propertyName == null)
			throw new IllegalArgumentException(
			"Must specify non-null propertyName when creating " +
			"ObservedChangeEvent for " + component + ".");
		
		if (newValue != null &&
		    !(newValue instanceof Serializable))
			throw new IllegalArgumentException(
			"Must specify a serializable object as the new value of a property " +
			"when creating an ObservedChangeEvent.");
			 
        this.component = component;
        this.propertyName = propertyName;
        this.newValue = (Serializable)newValue;
    }
  
    public ObservedChangeEvent(IComponent component, String propertyName, boolean newValue)
    {
        this(component, propertyName, new Boolean(newValue));
    }
  
  	/**
	 *  Constructor for an unknown change event.  The receiver must acknowledge
	 *  that the observed object changed.  This is used when a property of
	 *  the component is itself is a data holder (such as a Collection object) and
	 *  it changes.
	 *
	 */
	 
  	public ObservedChangeEvent(IComponent component)
	{
		super(component);
		
		this.component = component;
	}
	
    public IComponent getComponent()
    {
        return component;
    }
  
    public Serializable getNewValue()
    {
        return newValue;
    }
  
    public String getPropertyName()
    {
        return propertyName;
    }
  
}
