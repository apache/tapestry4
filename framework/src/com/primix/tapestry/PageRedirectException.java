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

package com.primix.tapestry;

/**
 *  Exception thrown by a {@link IComponent component} or {@link IEngineService}
 *  that wishes to force the application to a particular page.  This is often used
 *  to protect a sensitive page until the user is authenticated.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class PageRedirectException extends RequestCycleException
{
	private String targetPageName;

	public PageRedirectException(String targetPageName)
	{
		super();

		this.targetPageName = targetPageName;
	}

	public PageRedirectException(IPage page)
	{
		this(page.getName());
	}
	
	public PageRedirectException(String message, IComponent component,
		String targetPageName)
	{
		super(message, component);
		this.targetPageName = targetPageName;
	}

	public PageRedirectException(String message, IComponent component, 
		Throwable rootCause,
		String targetPageName)
	{
		super(message, component, rootCause);

		this.targetPageName = targetPageName;
	}

	public PageRedirectException(String message, String targetPageName)
	{
		super(message);

		this.targetPageName = targetPageName;
	}

	public String getTargetPageName()
	{
		return targetPageName;
	}
}

