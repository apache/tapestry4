package com.primix.tapestry.components;

import com.primix.tapestry.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 *  Encapsulates a SQL related exception thrown by a {@link DatabaseQuery}.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class DatabaseQueryException extends RequestCycleException
{
	private String query;

	public DatabaseQueryException(String message, IComponent component, 
		IRequestCycle cycle, String query, 
		Throwable rootCause)
	{
		super(message, component, cycle, rootCause);

		this.query = query;
	}

	public DatabaseQueryException(String message, IComponent component, 
		IRequestCycle cycle, Throwable rootCause)
	{
		super(message, component, cycle, rootCause);
	}

	public String getQuery()
	{
		return query;
	}
}

