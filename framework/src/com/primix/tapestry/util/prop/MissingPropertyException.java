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

/**
 *  Exception thrown by {@link PropertyHelper} when an propery is specified
 *  which does not exist.
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class MissingPropertyException extends RuntimeException
{
	// Make this transient, since we can't count on it being serializable.

	private transient Object instance;
	private String propertyName;
	private transient Object rootObject;
	private String propertyPath;

	public MissingPropertyException(Object instance, String propertyName)
	{
		this(instance, propertyName, instance, propertyName);
	}

	public MissingPropertyException(
		Object rootObject,
		String propertyPath,
		Object instance,
		String propertyName)
	{
		super(
			"Class "
				+ instance.getClass().getName()
				+ " does not implement a "
				+ propertyName
				+ " property.");

		this.instance = instance;
		this.propertyName = propertyName;
		this.rootObject = rootObject;
		this.propertyPath = propertyPath;
	}

	/**
	 *  The object in which property access failed.
	 *
	 */

	public Object getInstance()
	{
		return instance;
	}

	/**
	 *  The name of the property the instance fails to provide.
	 *
	 */

	public String getPropertyName()
	{
		return propertyName;
	}

	/**
	 *  The root object, the object which is the root of the property path.
	 *
	 */

	public Object getRootObject()
	{
		return rootObject;
	}

	/**
	 *  The property path (containing the invalid property name).
	 *
	 */

	public String getPropertyPath()
	{
		return propertyPath;
	}
}