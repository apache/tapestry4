/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
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
	 *  rules set by {@link Tapestry#evaluateBoolean(Object)}.
	 *
	 */

	public boolean getBoolean()
	{
		return Tapestry.evaluateBoolean(getObject());
	}

	public int getInt()
	{
		Object raw;

		raw = getObject();
		if (raw == null)
			throw new NullValueForBindingException(this);

		if (raw instanceof Number)
		{
			return ((Number) raw).intValue();
		}

		if (raw instanceof Boolean)
		{
			return ((Boolean) raw).booleanValue() ? 1 : 0;
		}

		// Save parsing for last.  This may also throw a number format exception.

		return Integer.parseInt((String) raw);
	}

	public double getDouble()
	{
		Object raw;

		raw = getObject();
		if (raw == null)
			throw new NullValueForBindingException(this);

		if (raw instanceof Number)
		{
			return ((Number) raw).doubleValue();
		}

		if (raw instanceof Boolean)
		{
			return ((Boolean) raw).booleanValue() ? 1 : 0;
		}

		// Save parsing for last.  This may also throw a number format exception.

		return Double.parseDouble((String) raw);
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