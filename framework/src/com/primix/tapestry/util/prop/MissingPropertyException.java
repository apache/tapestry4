package com.primix.foundation.prop;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Exception thrown by {@link PropertyHelper} when an propery is specified
 *  which does not exist.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class MissingPropertyException extends RuntimeException
{
	// Make this transient, since we can't count on it being serializable.
	
	private transient Object instance;
	private String propertyName;
    private transient Object rootObject;
    private String propertyPath;
    
    public MissingPropertyException(Object instance, String propertyName)
    {
        this(null, null, instance, propertyName);
    }

	public MissingPropertyException(Object rootObject, String propertyPath,
	    Object instance, String propertyName)
	{
		super("Class " + instance.getClass().getName() + " does not implement a " +
			propertyName + " property.");

		this.instance = instance;
		this.propertyName = propertyName;
        this.rootObject = rootObject;
        this.propertyPath = propertyPath;
	}
    
	public Object getInstance()
	{
		return instance;
	}
    
	public String getPropertyName()
	{
		return propertyName;
	}

    public Object getRootObject()
    {
        return rootObject;
    }

    public String getPropertyPath()
    {
        return propertyPath;
    }
}

