package com.primix.tapestry.binding;

import com.primix.tapestry.*;

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
 * Stores a static (invariant) String as the value.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class StaticBinding extends AbstractBinding
{
	private String value;
	private Integer integerValue;

	public StaticBinding(String value)
	{
		this.value = value;
	}

	/**
	*  Invokes {@link #getInteger()} and converts the result.
	*
	*/

	public int getInt()
	throws NullValueForBindingException
	{
		if (integerValue == null)
			getInteger();

		if (integerValue == null)
			throw new NullValueForBindingException(this);

		return integerValue.intValue();		
	}

	/**
	*  Attempts to build an {@link Integer} from the {@link String}
	*  using the <code>String</code> constructor of
	*  {@link Integer}, returning that value.
	*
	*/

	public Integer getInteger()
	{
		if (integerValue == null)
			integerValue = new Integer(value);

		return integerValue;
	}

	public String getString()
	{
		return value;
	}

	public Object getValue()
	{
		return value;
	}

	/**
	*  Returns true.
	*
	*/

	public boolean isStatic()
	{
		return true;
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setBoolean(boolean value) throws ReadOnlyBindingException
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setInt(int value) throws ReadOnlyBindingException
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/

	public void setString(String value) throws ReadOnlyBindingException
	{
		throw new ReadOnlyBindingException(this);
	}

	/**
	*  @throws ReadOnlyBindingException always.
	*
	*/


	public void setValue(Object value) throws ReadOnlyBindingException
	{
		throw new ReadOnlyBindingException(this);
	}

	public String toString()
	{
		return "StaticBinding[" + value + "]";
	}
}

