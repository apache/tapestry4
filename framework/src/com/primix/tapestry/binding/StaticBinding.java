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
 * <p>It may be useful to cache static bindings the way {@link FieldBindings} are cached.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class StaticBinding extends AbstractBinding
{
	private String value;
    private boolean accessed;
    private int intValue;
    private double doubleValue;

	public StaticBinding(String value)
	{
		this.value = value;
	}

    private void parse()
    {
        accessed = true;

        intValue = Integer.parseInt(value);
        doubleValue = Double.parseDouble(value);
    }

	/**
	*  Invokes {@link #getInteger()} and converts the result.
	*
	*/

	public int getInt()
	{
        if (!accessed) parse();

        return intValue;
	}

    public double getDouble()
    {
        if (!accessed) parse();

        return doubleValue;
    }

	public String getString()
	{
		return value;
	}

	public Object getValue()
	{
		return value;
	}

	public String toString()
	{
		return "StaticBinding[" + value + "]";
	}
}

