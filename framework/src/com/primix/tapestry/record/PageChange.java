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
 *  Represents a change to a component on a page.
 *
 * @author Howard Ship
 * @version $Id$
 */

package com.primix.tapestry.record;

import com.primix.tapestry.*;

public class PageChange implements IPageChange
{
	String componentPath;
	String propertyName;
	Object newValue;

	public PageChange(String componentPath, String propertyName, Object newValue)
	{
		this.componentPath = componentPath;
		this.propertyName = propertyName;
		this.newValue = newValue;
	}

	/**
	*  The path to the component on the page, or null if the property
	*  is a property of the page.
	*
	*/

	public String getComponentPath()
	{
		return componentPath;
	}

	/**
	*  The new value for the property, which may be null.
	*
	*/

	public Object getNewValue()
	{
		return newValue;
	}

	/**
	*  The name of the property that changed.
	*
	*/

	public String getPropertyName()
	{
		return propertyName;
	}

	public String toString()
	{
		StringBuffer buffer;

		buffer = new StringBuffer(getClass().getName());

		buffer.append('[');

		if (componentPath != null)
		{
			buffer.append(componentPath);
			buffer.append(' ');
		}

		buffer.append(propertyName);

		buffer.append(' ');
		buffer.append(newValue);

		buffer.append(']');

		return buffer.toString();
	}
}

