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

package com.primix.tapestry.listener;

import com.primix.tapestry.Tapestry;
import com.primix.tapestry.util.prop.*;
import java.util.*;

/**
 *  {@link PropertyHelper} class for {@link ListenerMap}.  Exposes the
 *  listeners created by the {@link ListenerMap} as read-only properties
 *  of the map.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.2
 */

public class ListenerMapHelper extends PropertyHelper
{
	public static class ListenerMapAccessor implements IPropertyAccessor
	{
		private String name;

		private ListenerMapAccessor(String name)
		{
			this.name = name;
		}

		/** @since 1.0.6 **/

		public String getName()
		{
			return name;
		}

		public Object get(Object instance)
		{
			ListenerMap listenerMap = (ListenerMap) instance;

			return listenerMap.getListener(name);
		}

		public boolean isReadable()
		{
			return true;
		}

		public boolean isWritable()
		{
			return false;
		}

		public boolean isReadWrite()
		{
			return false;
		}

		/**
		 *  Returns {@link Object}.class, because we never know the
		 *  type of objects provided by the {@link ListenerMap}.
		 *
		 */

		public Class getType()
		{
			return Object.class;
		}

		/**
		 *  Updating is not allowed.
		 *
		 *  @throws UnsupportedOperationException always.
		 *
		 */

		public void set(Object instance, Object value)
		
		{
			throw new UnsupportedOperationException(
				Tapestry.getString("ListenerMapHelper.set-not-supported"));
		}
	}

	public ListenerMapHelper(Class beanClass)
	{
		super(beanClass);
	}

	public IPropertyAccessor getAccessor(Object instance, String name)
	{
		IPropertyAccessor result;

		result = super.getAccessor(instance, name);

		if (result == null)
			result = new ListenerMapAccessor(name);

		return result;
	}

	/**
	 *  @since 1.0.6
	 *
	 */

	public Collection getSyntheticPropertyNames(Object instance)
	{
		ListenerMap listenerMap = (ListenerMap) instance;

		return listenerMap.getListenerNames();
	}

}