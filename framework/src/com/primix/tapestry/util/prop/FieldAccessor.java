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

package com.primix.tapestry.util.prop;

import java.lang.reflect.*;
import com.primix.tapestry.Tapestry;
import com.primix.tapestry.util.*;

/**
 *
 *  Facilitiates access to public instance variables as if they were
 *  JavaBeans properties.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.1
 *
 */

class FieldAccessor implements IPropertyAccessor
{
	private Field field;

	FieldAccessor(Field field)
	{
		this.field = field;
	}

	public String getName()
	{
		return field.getName();
	}

	public void set(Object instance, Object value)
	{
		try
		{
			field.set(instance, value);
		}
		catch (Exception ex)
		{
			throw new DynamicInvocationException(
				Tapestry.getString(
					"FieldAccessor.unable-to-set-field",
					field.getName(),
					instance,
					value),
				ex);
		}
	}

	public boolean isWritable()
	{
		return true;
	}

	public boolean isReadWrite()
	{
		return true;
	}

	public boolean isReadable()
	{
		return true;
	}

	public Class getType()
	{
		return field.getType();
	}

	public Object get(Object instance)
	{
		try
		{
			return field.get(instance);
		}
		catch (Exception ex)
		{
			throw new DynamicInvocationException(
				Tapestry.getString(
					"FieldAccessor.unable-to-read-field",
					field.getName(),
					instance),
				ex);
		}
	}
}