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

/**
 * Stores a static (invariant) String as the value.
 *
 * <p>It may be useful to cache static bindings the way {@link FieldBinding}s are cached.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class StaticBinding extends AbstractBinding
{
	private String value;
	private boolean parsedInt;
	private int intValue;
	private boolean parsedDouble;
	private double doubleValue;

	public StaticBinding(String value)
	{
		this.value = value;
	}

	/**
	 *  Always returns String, even if the String has been
	 *  parsed as an integer or double.
	 *
	 * @since 1.0.5
	 */

	public Class getType()
	{
		return String.class;
	}

	/**
	*  Interprets the static value as an integer.
	*
	*/

	public int getInt()
	
	{
		if (!parsedInt)
		{
			intValue = Integer.parseInt(value);
			parsedInt = true;
		}

		return intValue;
	}

	/**
	*  Interprets the static value as a double.
	*
	*/

	public double getDouble()
	{
		if (!parsedDouble)
		{
			doubleValue = Double.parseDouble(value);
			parsedDouble = true;
		}

		return doubleValue;
	}

	public String getString()
	{
		return value;
	}

	public Object getObject()
	{
		return value;
	}

	public String toString()
	{
		return "StaticBinding[" + value + "]";
	}
}