package com.primix.foundation;

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
 *  An exception raised when a dynamic invocation fails with some
 *  form of exception.  This exception is a 
 *  <code>RuntimeException</code> (which
 *  prevents anyone from having to declare it) ... it should only get
 *  raised as a result of programmer error.
 *
 *  This exception is raised 'on behalf' of a more fundamental
 *  exception being raised, which is packaged inside the
 *  <code>DynamicInvocationException</code>.  This root cause exception
 *  may not be a runtime exception.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class DynamicInvocationException extends RuntimeException
{
	private Throwable rootCause;
    
	public DynamicInvocationException(String message)
	{
		super(message);
	}
    
	/**
	*  A variation used when there is some message to describe the
	*  context in which the exception was raised.
	*
	*/

	public DynamicInvocationException(String message, Throwable rootCause)
	{
		super(message);

		this.rootCause = rootCause;
	}
    
	/**
	*  The basic constructor takes some other exception (that is, a
	*  Throwable) and packages it. The new 
	*  <code>DynamicInvocationException</code> can then be thrown.
	*
	*  @param rootCause The original exception thrown.
	*
	*/

	public DynamicInvocationException(Throwable rootCause)
	{
		this.rootCause = rootCause;
	}
    
	/**
	*  Allows access to the originally thrown Exception.  
	*
	*/

	public Throwable getRootCause()
	{
		return rootCause;
	}
}

