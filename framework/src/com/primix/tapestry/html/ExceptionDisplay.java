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

package com.primix.tapestry.html;

import com.primix.tapestry.*;
import com.primix.tapestry.util.exception.*;

/**
 *  Class ExceptionDisplayjava
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class ExceptionDisplay
	extends BaseComponent
{
	private IBinding exceptionsBinding;
	private ExceptionDescription exception;
	private int count;
	private int index;
	
	public void setExceptionsBinding(IBinding value)
	{
		exceptionsBinding = value;
	}
	
	public IBinding getExceptionsBinding()
	{
		return exceptionsBinding;
	}
	
	public void setException(ExceptionDescription value)
	{
		exception = value;
	}
	
	public ExceptionDescription getException()
	{
		return exception;
	}
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		ExceptionDescription[] exceptions
			= (ExceptionDescription[])exceptionsBinding.getObject("exceptions", ExceptionDescription[].class);
		
		count = exceptions.length;
		
		super.render(writer, cycle);
	}
	
	public void setIndex(int value)
	{
		index = value;
	}
	
	public boolean isLast()
	{
		return index == (count - 1);
	}
}

