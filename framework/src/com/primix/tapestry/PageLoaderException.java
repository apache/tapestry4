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
 *  Encapsulates exceptions that occur when loading a page and its components.
 *
 *
 * @author Howard Ship
 * @version $Id$
 */

public class PageLoaderException extends Exception
{
	private Throwable rootCause;
	private String pageName;
	private transient IComponent component;

	/**
	 * @since 0.2.12
	 *
	 */

	public PageLoaderException(String message, Throwable rootCause)
	{
		this(message, (IComponent) null, rootCause);
	}

	/**
	 * @since 0.2.12
	 *
	 */

	public PageLoaderException(
		String message,
		IComponent component,
		Throwable rootCause)
	{
		super(message);

		this.rootCause = rootCause;
		this.component = component;

		if (component != null)
		{
			IPage page = component.getPage();

			if (page != null)
				pageName = page.getName();
		}

	}

	/**
	 * @since 0.2.12
	 *
	 */

	public PageLoaderException(String message, IComponent component)
	{
		this(message, component, null);
	}

	/**
	 * @since 0.2.12
	 *
	 */

	public PageLoaderException(
		String message,
		String pageName,
		Throwable rootCause)
	{
		this(message, (IComponent) null, rootCause);

		this.pageName = pageName;
	}

	public IComponent getComponent()
	{
		return component;
	}

	public String getPageName()
	{
		return pageName;
	}

	public Throwable getRootCause()
	{
		return rootCause;
	}
}