package com.primix.foundation.prop;

import java.beans.*;
import java.lang.reflect.*;
import java.util.*;
import com.primix.foundation.DynamicInvocationException;
import org.apache.log4j.*;

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
 * Streamlines dynamic access to one of a single class's properties.  
 * This enapsulates the getter and/or
 * setter methods for a particular property of a particular bean class.
 * 
 * @version $Id$
 * @author Howard Ship
 *
 * @see PropertyHelper
 */
 
class PropertyAccessor implements IPropertyAccessor
{
	private static final Category CAT = 
		Category.getInstance(PropertyAccessor.class.getName());

	protected PropertyDescriptor pd;
	private Method accessor;
	private Method mutator;
	private Object[] args;

	PropertyAccessor(PropertyDescriptor pd)
	{
		this.pd = pd;

		accessor = pd.getReadMethod();
		mutator = pd.getWriteMethod();
	}

	/**
	*
	*  @throws MissingAccessorException if the class does not define an accessor method
	*  for the property.
	*
	*/

	public Object get(Object target)
	{
		Object result;
		String propertyName;

		if (CAT.isDebugEnabled())
			CAT.debug("Getting property " + pd.getName() + " from " + target);

		if (accessor == null)
		{
			propertyName = pd.getName();

			throw new MissingAccessorException("No accessor method for property " + propertyName
				+ ".", target, propertyName);
		}

		try
		{
			result = accessor.invoke(target, null);
		}
		catch (Exception e)
		{
			String message;

			message = "Could not invoke method " + accessor.getName() + " on " +
				target + ".";

			throw new DynamicInvocationException(message, e);
		}

		return result;

	}

	/**
	*  Returns the type of the property.
	*
	*/

	public Class getType()
	{
		return pd.getPropertyType();
	}

	public boolean isReadable()
	{
		return (accessor != null);
	}

	public boolean isReadWrite()
	{
		return (accessor != null) &&
			(mutator != null);
	}

	public boolean isWritable()
	{
		return (mutator != null);
	}

	/**
	*
	*  @throws MissingAccessorException if the class does not define a mutator method
	*  for the property.
	*
	*/

	public void set(Object subject, Object value)
	{
		if (mutator == null)
		{
			String propertyName = pd.getName();

			throw new MissingAccessorException("No mutator method for property " +
				propertyName + ".", subject, propertyName);
		}

		if (CAT.isDebugEnabled())
			CAT.debug("Setting property " + pd.getName() + " of " + subject + " to " + value);

		// The array is lazily allocated and then held onto for any future work.
		// This creates a window for multithreading problems.

		if (args == null)
			args = new Object[1];

		args[0] = value;
	
		try
		{
			mutator.invoke(subject, args);
			}	
		catch (Exception e)
		{
			String message;

			message = "Could not invoke method " + mutator.getName() + " on " +
				subject + ".";

			throw new DynamicInvocationException(message, e);
			}
	}
}



