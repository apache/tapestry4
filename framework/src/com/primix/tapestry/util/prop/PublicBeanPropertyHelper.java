/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix
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

package com.primix.tapestry.util.prop;

import java.lang.reflect.*;

/**
 *  Allows public instance variables of {@link IPublicBean} instances
 *  to be treated as read/write JavaBeans properties.
 *
 * @author Howard Ship
 * @version $Id$
 * @since 1.0.1
 */
 
public class PublicBeanPropertyHelper 
extends PropertyHelper
{
	public PublicBeanPropertyHelper(Class beanClass)
	{
		super(beanClass);
	}

	/**
	 *  Invoked to build a list of property accessor.  The super-implementation
	 *  takes care of all the JavaBeans properties; we add additional
	 *  properties mapped to public instance variables.
	 *
	 *  <p>Only accessible fields are included (that is, public fields that
	 *  aren't static).  Also, if a JavaBeans property
	 *  exists, it takes priority over the field access (for the attribute
	 *  with the same name).
	 *
	 */

	protected void buildPropertyAccessors()
	{
		super.buildPropertyAccessors();

		Field fields[] = beanClass.getFields();

		for (int i = 0; i < fields.length; i++)
		{
			Field field = fields[i];
			int mods = field.getModifiers();

			// Skip over static variables and non-public instance variables.

			if (! Modifier.isPublic(mods) || Modifier.isStatic(mods))
				continue;

			String name = field.getName();

			// Skip it if there's already a matching property
			// (i.e., with real accessor methods).

			if (accessors.containsKey(name))
				continue;

			accessors.put(name, new FieldAccessor(field));						
		}
	}
}




