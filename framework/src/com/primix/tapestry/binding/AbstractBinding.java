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

package com.primix.tapestry.binding;

import com.primix.tapestry.*;
import java.util.*;

/**
 *  Base class for {@link IBinding} implementations.
 *
 * @author Howard Ship
 * @version $Id$
 */


public abstract class AbstractBinding implements IBinding
{
	/**
	*  Cooerces the raw value into a true or false, according to the
	*  rules set by {@link IBinding#getBoolean()}.
	*
	*  <p>The trick is to determine the best order for checks and to perform
	*  the checks as efficiently as possible.  I honestly don't know if
	*  instanceof is more efficient than catch ClassCastException.
	*
	*/

	public boolean getBoolean()
	{
		Object value;
		Class valueClass;

		value = getObject();

		if (value == null)
			return false;

		valueClass = value.getClass();

		if (valueClass == Boolean.class)
		{
			Boolean booleanValue = (Boolean)value;
			return booleanValue.booleanValue();
		}

		try
		{
			Number numberValue = (Number) value;
			return (numberValue.intValue() != 0);
		}
		catch (ClassCastException e)
		{
			// Not Number, maybe String
		}

		if (Collection.class.isAssignableFrom(valueClass))
		{
			Collection collection = (Collection)value;

			return collection.size() > 0;
		}

		if (valueClass == String.class)
		{
			try
			{
				int i;
				char ch;
				char[] data = ((String)value).toCharArray();

				for (i = 0;; i++)
				{
					if (!Character.isWhitespace(data[i]))
						return true;
				}
			}
			catch (IndexOutOfBoundsException e)
			{
				// Hit end-of-string before finding a non-whitespace character

				return false;
			}
		}

		if (valueClass.isArray())
		{
			Object[] array = (Object[])value;

			return array.length > 0;
		}

		// The value is true because it is not null.

		return true;
	}

	public int getInt()
	{
		Object raw;

		raw = getObject();
		if (raw == null)
			throw new NullValueForBindingException(this);

		if (raw instanceof Number)
		{
			return ((Number)raw).intValue();
		}

		if (raw instanceof Boolean)
		{
			return ((Boolean)raw).booleanValue() 
			? 1 
				: 0;
		}

		// Save parsing for last.  This may also throw a number format exception.

		return Integer.parseInt((String)raw);
	}

	public double getDouble()
	{
		Object raw;

		raw = getObject();
		if (raw == null)
			throw new NullValueForBindingException(this);

		if (raw instanceof Number)
		{
			return ((Number)raw).doubleValue();
		}

		if (raw instanceof Boolean)
		{
			return ((Boolean)raw).booleanValue() 
			? 1 
				: 0;
		}

		// Save parsing for last.  This may also throw a number format exception.

		return Double.parseDouble((String)raw);
	}

	/**
	*  Gets the value for the binding.  If null, returns null,
	*  otherwise, returns the String (<code>toString()</code>) version of
	*  the value.
	*
	*/

	public String getString()
	{
		Object value;

		value = getObject();
		if (value == null)
			return null;

		return value.toString();
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setBoolean(boolean value)
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setInt(int value)
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setDouble(double value)
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setString(String value)
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/


	public void setObject(Object value)
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  Default implementation: returns true.
	*
	*/

	public boolean isStatic()
	{
		return true;
	}

	public Object getObject(String parameterName, Class type)
	{
		Object result = getObject();
		
		if (result == null)
			return result;
			
		if (type.isAssignableFrom(result.getClass()))
			return result;
			
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("Parameter ");
		buffer.append(parameterName);
		buffer.append(" (");
		buffer.append(result);
		buffer.append(") ");
		
		if (type.isInterface())
			buffer.append(" does not implement interface ");
		else
			buffer.append(" is not type ");
			
			
		buffer.append(type.getName());
		buffer.append('.');
			
		throw new BindingException(buffer.toString(), this);				
	}
}

