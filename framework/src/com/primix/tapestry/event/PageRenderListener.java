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
 *  An object that listens to page events.  The {@link IPage page} generates
 *  events before and after rendering a response, and at the end of the request
 *  cycle.
 *
 *  @author Howard Ship
 *  @version $Id$
 *  @since 1.0.5
 */
	
public interface PageRenderListener extends EventListener
{
	/**
	 *  Invoked before just before the page renders a response.  This provides
	 *  listeners with a last chance to initialize themselves for the render.
	 *  This initialization can include modifying peristent page properties.
	 *
	 *
	 */
	
	public void pageBeginRender(PageEvent event);
	
	/**
	 *  Invoked after a successful render of the page.
	 *  Allows objects to release any resources they needed during the
	 *  the render.
	 *
	 */
	
	public void pageEndRender(PageEvent event);
}

