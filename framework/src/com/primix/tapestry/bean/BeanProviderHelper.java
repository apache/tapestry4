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
 * 1
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

package com.primix.tapestry.bean;

import com.primix.tapestry.*;
import com.primix.tapestry.util.prop.*;
import java.util.*;

/**
 *  A subclass of {@link PropertyHelper} that allows a {@link IBeanProvider}
 *  to expose the beans it can create as properties.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.4
 */

public class BeanProviderHelper extends PropertyHelper
{
	public static class BeanAccessor
	implements IPropertyAccessor
	{
		private String name;

		private BeanAccessor(String name)
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
			IBeanProvider provider = (IBeanProvider)instance;
			
			return provider.getBean(name);
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
		 *  type of objects stored in a {@link Map}.
		 *
		 */
		 
		public Class getType()
		{
			return Object.class;
		}
		
		/**
		 * Does nothing; the IBeanProvider properties are read-only.
		 *
		 */
		
		public void set(Object instance, Object value)
		{ 
		}
	}


	public BeanProviderHelper(Class beanClass)
	{
		super(beanClass);
	}

	public IPropertyAccessor getAccessor(Object instance, String name)
	{
		IPropertyAccessor result;

		result = super.getAccessor(instance, name);

		if (result == null)
			result = new BeanAccessor(name);

		return result;
	}
	
	/** @since 1.0.6 **/
	
	public Collection getSyntheticPropertyNames(Object instance)
	{
		IBeanProvider provider = (IBeanProvider)instance;

		return provider.getBeanNames();
	}
}
