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
 *  Exception thrown by an {@link IEngineService} when it discovers that
 *  the an action link was for an out-of-date version of the page.
 *
 *  <p>The application should redirect to the StaleLink page.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class StaleLinkException extends RequestCycleException
{
	private transient IPage page;
	private String pageName;
	private String targetIdPath;
	private String targetActionId;

	public StaleLinkException()
	{
		super();
	}

	/**
	 *  Constructor used when the action id is found, but the target id path
	 *  did not match the actual id path.
	 *
	 */

	public StaleLinkException(
		IComponent component,
		String targetActionId,
		String targetIdPath)
	{
		super(
			Tapestry.getString(
				"StaleLinkException.action-mismatch",
				new String[] { targetActionId, component.getIdPath(), targetIdPath }),
			component);

		page = component.getPage();
		pageName = page.getName();
		this.targetActionId = targetActionId;
		this.targetIdPath = targetIdPath;
	}

	/**
	 *  Constructor used when the target action id is not found.
	 *
	 */

	public StaleLinkException(
		IPage page,
		String targetActionId,
		String targetIdPath)
	{
		this(
			Tapestry.getString(
				"StaleLinkException.component-mismatch",
				targetActionId,
				targetIdPath),
			page);

		this.targetActionId = targetActionId;
		this.targetIdPath = targetIdPath;
	}

	public StaleLinkException(String message, IComponent component)
	{
		super(message, component);
	}

	public StaleLinkException(String message, IPage page)
	{

		super(message, null);
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