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

package com.primix.tapestry.callback;

import com.primix.tapestry.*;

/**
 *  Simple callback for re-invoking a {@link IDirect} component trigger..
 *
 *  @version $Id$
 *  @author Howard Ship
 *  @since 0.2.9
 *
 */

public class DirectCallback implements ICallback
{
	private String pageName;
	private String componentIdPath;
	private String[] context;

	public String toString()
	{
		StringBuffer buffer = new StringBuffer("DirectCallback[");

		buffer.append(pageName);
		buffer.append('/');
		buffer.append(componentIdPath);

		if (context != null)
		{
			char sep = ' ';

			for (int i = 0; i < context.length; i++)
			{
				buffer.append(sep);
				buffer.append(context[i]);

				sep = '/';
			}
		}

		buffer.append(']');

		return buffer.toString();

	}

	/**
	 *  Creates a new DirectCallback for the component.  The context
	 *  (which may be null) is retained, not copied.
	 *
	 */

	public DirectCallback(IDirect component, String[] context)
	{
		pageName = component.getPage().getName();
		componentIdPath = component.getIdPath();
		this.context = context;
	}

	/**
	 *  Locates the {@link IDirect} component that was previously identified
	 *  (and whose page and id path were stored),
	 *  and invokes {@link IDirect#trigger(IRequestCycle, String[])} upon it.
	 *
	 */

	public void peformCallback(IRequestCycle cycle) throws RequestCycleException
	{
		IPage page = cycle.getPage(pageName);
		IComponent component = page.getNestedComponent(componentIdPath);
		IDirect direct = null;

		try
		{
			direct = (IDirect) component;
		}
		catch (ClassCastException ex)
		{
			throw new RequestCycleException(
				"Component " + component.getExtendedId() + " is not type IDirect.",
				component,
				ex);
		}

		direct.trigger(cycle, context);
	}
}