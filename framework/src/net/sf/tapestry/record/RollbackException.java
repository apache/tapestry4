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

package net.sf.tapestry.record;

import com.primix.tapestry.*;

import net.sf.tapestry.*;

/**
 * Identifies a failure to rollback a page to a prior state.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class RollbackException extends ApplicationRuntimeException
{
	private String componentId;
	private String propertyName;
	private Object newValue;

	public RollbackException(
		IComponent component,
		String propertyName,
		Object newValue,
		Throwable rootCause)
	{
		super(
			Tapestry.getString(
				"RollbackException.message",
				propertyName, component.getExtendedId(), newValue),
			rootCause);

		this.componentId = component.getExtendedId();
		this.propertyName = propertyName;
		this.newValue = newValue;
	}

	public String getComponentId()
	{
		return componentId;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public Object getNewValue()
	{
		return newValue;
	}

}