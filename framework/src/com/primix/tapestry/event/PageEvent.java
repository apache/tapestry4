/*
 * Tapestry Web Application Framework
 * Copyright (c) 2001 by Howard Ship and Primix
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

package com.primix.tapestry.event;

import com.primix.tapestry.*;
import java.util.*;
	
/**
 *  Encapsulates information related to the page listener
 *  interfaces.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 */
	
public class PageEvent
	extends EventObject
{
	private transient IPage page;
	private transient IRequestCycle requestCycle;
	
	/**
	 *  Constructs a new instance of the event.  The
	 *  {@link EventObject#getSource()} of the event will
	 *  be the {@link IPage}.
	 *
	 */
	
	public PageEvent(IPage page, IRequestCycle cycle)
	{
		super(page);
		
		this.page = page;
		this.requestCycle = cycle;
	}
	
	public IPage getPage()
	{
		return page;
	}
	
	public IRequestCycle getRequestCycle()
	{
		return requestCycle;
	}
}

