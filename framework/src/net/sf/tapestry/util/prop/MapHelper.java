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

package net.sf.tapestry.util.prop;

import com.primix.tapestry.util.prop.*;
import java.util.*;

/**
 *  A subclass of {@link PropertyHelper} that allows values of
 *  a <code>java.util.Map</code> to be accessed as if they were JavaBean properties of
 *  the <code>java.util.Map</code> itself.
 *
 *  <p>This requires that the keys of the <code>Map</code> be valid
 *  JavaBeans property names.
 *
 *  <p>This class includes a static initializer that invokes 
 *  {@link PropertyHelper#register(Class,Class)}.
 *
 *  <p>TBD:  Better error detection.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class MapHelper extends PropertyHelper
{
	public static class MapAccessor implements IPropertyAccessor
	{
		private String name;

		private MapAccessor(String name)
		{
			this.name = name;
		}

		public Object get(Object instance)
		{
			return ((Map) instance).get(name);
		}

		/** @since 1.0.6 **/

		public String getName()
		{
			return name;
		}

		public boolean isReadable()
		{
			return true;
		}

		public boolean isWritable()
		{
			return true;
		}

		public boolean isReadWrite()
		{
			return true;
		}

		/**
		 *  Returns {@link Object}.class, because we never know the
		 *  type of objects stored in a {@link Map}.
		 *
		 */

		public Class getType()
		{
			return Object.class;
		}

		public void set(Object instance, Object value)
		
		{
			((Map) instance).put(name, value);
		}

		public String toString()
		{
			return "MapHelper.MapAccessor[" + name + "]";
		}
	}

	/**
	 *  Map of MapAccessor, keyed on property name.
	 *
	 */

	private static final Map accessorMap = new HashMap();

	public MapHelper(Class beanClass)
	{
		super(beanClass);
	}

	public IPropertyAccessor getAccessor(Object instance, String name)
	{
		IPropertyAccessor result;

		result = super.getAccessor(instance, name);

		if (result == null)
		{
			synchronized (accessorMap)
			{
				result = (IPropertyAccessor) accessorMap.get(name);

				if (result == null)
				{
					result = new MapAccessor(name);
					accessorMap.put(name, result);
				}
			}
		}

		return result;
	}

	/** @since 1.0.6 **/

	public Collection getSyntheticPropertyNames(Object instance)
	{
		Map map = (Map) instance;

		return map.keySet();
	}
}