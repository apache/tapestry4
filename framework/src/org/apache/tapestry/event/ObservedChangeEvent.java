/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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