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
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 *
 *  <p>Because the ShowInspector component is implemented using a {@link Rollover},
 *  the containing page must use a {@link Body} component instead of
 *  a &lt;body&gt; tag.
 *
 *  Informal parameters are not allowed.  May not contain a body.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 

package com.primix.tapestry.inspector;

import com.primix.tapestry.*;

public class ShowInspector extends BaseComponent
{
	/**
	 *  Returns the window target (used in the target attribute of the &lt;a&gt; tag).
	 *
	 *  <p>We use the creation time of the session as a kind of unique key.
	 *
	 *  @since 0.2.9
	 *
	 */
	 
	public String getWindowTarget()
	{
		return "Tapestry Inspector " +
			getPage().getRequestCycle().getRequestContext().getSession().getCreationTime();
	}

	/**
	 *  Gets the listener for the link component.
	 *
	 */
	 
	public IDirectListener getListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IDirect direct, String[] context,
					IRequestCycle cycle)
			{
				Inspector inspector;
				
				inspector = (Inspector)cycle.getPage("Inspector");
				
				inspector.inspect(getPage().getName(), cycle);
			}
		};
	}
	
}
