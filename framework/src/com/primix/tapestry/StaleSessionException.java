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
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the <code>HttpSession</code> has timed out (and been replaced by a new, empty
 *  one).
 *
 *  <p>The application should redirect to the stale-session page.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */

package com.primix.tapestry;

public class StaleSessionException extends RequestCycleException
{
	private transient IPage page;
	private String pageName;

	public StaleSessionException()
	{
		super();
	}

	public StaleSessionException(String message, IPage page)
	{
		super(message, null, null);
		this.page = page;
		
		if (page != null)
			pageName = page.getName();	
	}
	
	public String getPageName()
	{
		return pageName;
	}

	/**
	*  Returns the page referenced by the service URL, if known, or null otherwise.
	*
	*/

	public IPage getPage()
	{
		return page;
	}
}

