package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
import java.util.*;

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
 *  Component that can be placed into application pages that will launch
 *  the inspector in a new window.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowInspector extends BaseComponent
{
	public ShowInspector(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	/**
	 *  Returns the target attribute used in the link.  This includes
	 *  the session ID, so it is likely to be unique, forcing a new
	 *  window to be raised.
	 *
	 */
	 
	public String getTarget()
	{
		return "Tapestry Inspector " + 
			page.getRequestCycle().getRequestContext().getSession().getId();
	}

	/**
	 *  Gets the listener for the link component.
	 *
	 */
	 
	public IDirectListener getListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				Inspector inspector;
				
				inspector = (Inspector)cycle.getPage("Inspector");
				
				inspector.inspect(getPage().getName(), cycle);
			}
		};
	}
}
