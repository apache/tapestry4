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

package com.primix.tapestry.bean;

import com.primix.tapestry.ApplicationRuntimeException;
import com.primix.tapestry.IBeanProvider;
import com.primix.tapestry.IResourceResolver;
import com.primix.tapestry.Tapestry;
import com.primix.tapestry.util.prop.PropertyHelper;
import java.lang.reflect.Field;

/**
 *  Initializes a bean with the value of a public static field.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 */

public class FieldBeanInitializer extends AbstractBeanInitializer
{
	private String fieldName;
	private Object fieldValue;
	private boolean fieldResolved = false;

	public FieldBeanInitializer(String propertyName, String fieldName)
	{
		super(propertyName);

		this.fieldName = fieldName;
	}

	public void setBeanProperty(IBeanProvider provider, Object bean)
	{
		if (!fieldResolved)
			resolveField(provider.getResourceResolver());
			
		PropertyHelper helper = PropertyHelper.forInstance(bean);

		helper.set(bean, propertyName, fieldValue);	
	}

	private synchronized void resolveField(IResourceResolver resolver)
	{
		if (fieldResolved)
			return;

		// This is all copied out of of FieldBinding!!

		int dotx = fieldName.lastIndexOf('.');

		if (dotx < 0)
			throw new ApplicationRuntimeException(
			Tapestry.getString("invalid-field-name", fieldName));

		String className = fieldName.substring(0, dotx);
		String simpleFieldName = fieldName.substring(dotx + 1);

		// Simple class names are assumed to be in the java.lang package.

		if (className.indexOf('.') < 0)
			className = "java.lang." + className;

		Class targetClass = null;

		try
		{
			targetClass = resolver.findClass(className);
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(
				Tapestry.getString("unable-to-resolve-class", className),
				t);
		}

		Field field = null;

		try
		{
			field = targetClass.getField(simpleFieldName);
		}
		catch (NoSuchFieldException ex)
		{
			throw new ApplicationRuntimeException(
				Tapestry.getString("field-not-defined", fieldName),
				ex);
		}

		// Get the value of the field.  null means look for it as a static
		// variable.

		try
		{
			fieldValue = field.get(null);
		}
		catch (IllegalAccessException ex)
		{
			throw new ApplicationRuntimeException(
			Tapestry.getString("illegal-field-access", fieldName),
				ex);
		}
		catch (NullPointerException ex)
		{
			throw new ApplicationRuntimeException(
				Tapestry.getString("field-is-instance", fieldName),
				ex);
		}

		fieldResolved = true;
	}

}