package com.primix.foundation.ejb;

import javax.ejb.*;

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
 *  Extended version of {@link EJBException} that includes a root cause.
 *  {@link EJBException} doesn't have quite the right constructor for this ...
 *  it has an equivalent to the rootCause property, (causedByException), but
 *  doesn't have a constructor that allows us to set a custom message.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class XEJBException extends EJBException
{
	private Throwable rootCause;
	
	public XEJBException(String message)
	{
		super(message);
	}
	
	public XEJBException(String message, Throwable rootCause)
	{
		super(message);
		
		this.rootCause = rootCause;
	}
	
	public XEJBException(Throwable rootCause)
	{
		super(rootCause.getMessage());
		
		this.rootCause = rootCause;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}