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

package com.primix.tapestry;

/**
 *  Exception thrown when a property of an {@link IComponent} is accessed that
 *  is only valid while the component is actually rendering (such properties
 *  are related to parameters, and satisfied by {@link IBinding bindings}.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class RenderOnlyPropertyException extends ApplicationRuntimeException
{
	private IComponent component;
	private String propertyName;

	public RenderOnlyPropertyException(IComponent component, String propertyName)
	{
		super(
			Tapestry.getString(
				"RenderOnlyPropertyException.message",
				propertyName,
				component));

		this.component = component;
		this.propertyName = propertyName;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public IComponent getComponent()
	{
		return component;
	}
}