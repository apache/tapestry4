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
 *  Extended version of {@link RemoveException} that includes a root cause.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

package com.primix.tapestry.util.ejb;

import javax.ejb.*;

public class XRemoveException extends RemoveException
{
	private Throwable rootCause;
	
	public XRemoveException(String message)
	{
		super(message);
	}
	
	public XRemoveException(String message, Throwable rootCause)
	{
		super(message);
		
		this.rootCause = rootCause;
	}
	
	public XRemoveException(Throwable rootCause)
	{
		super(rootCause.getMessage());
		
		this.rootCause = rootCause;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}