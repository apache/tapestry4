/*
 * Tapestry Web Application Framework
 * Copyright (c)  2001 by Howard Ship and Primix
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

package com.primix.tapestry;

/**
 *  Exception thrown to force a redirection to an arbitrary location.
 *  This is used when, after processing a request (such as a form
 *  submission or a link being clicked), it is desirous to go
 *  to some arbitrary new location.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.6
 *
 */

public class RedirectException extends RequestCycleException
{
	private String location;
	
	
	public RedirectException(String location)
	{
		this(null, location);
	}
	
	/** 
	 *  @param message A message describing why the redirection is taking place.
	 *  @param location The location to redirect to, may be a relative path (relative
	 *  to the {@link ServletContext}).
	 *
	 *  @see javax.servlet.http.HttpServletResponse#sendRedirect(String)
	 *  @see javax.servlet.http.HttpServletResponse#encodeRedirectURL(String)
	 *
	 */
	
	public RedirectException(String message, String location)
	{
		super(message);
		
		this.location = location;
	}
	
	public String getLocation()
	{
		return location;
	}
}

