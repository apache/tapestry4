/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;

import com.primix.tapestry.engine.*;

/**
 *  Additional methods implemented by the engine that are 
 *  exposed to {@link IEngineService engine services}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 */

public interface IEngineServiceView extends IEngine
{
	/**
	 *  Invoked by a service to force the page selected by the {@link IRequestCycle}
	 *  to be renderred.  This takes care of a number of bookkeeping issues, such
	 *  as committing changes in page recorders.
	 * 
	 **/
	
	public void renderResponse(IRequestCycle cycle, ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException;
		
	/**
	 *  Invoked to restart the application from start; this most frequently follows
	 *  some kind of catastrophic failure.  This will invalidate any {@link HttpSession}
	 *  and force a redirect to the application servlet (i.e., invoking the home service
	 *  in a subsequent request cycle).
	 * 
	 **/
	
	public void restart(IRequestCycle cycle) throws IOException;
	
	/**
	 *  Invoked (typically by the reset service) to clear all cached data
	 *  (pages, templates, helper beans, specifications, etc.)
	 *  known to the application.  This is used during debugging of the application.
	 * 
	 **/
	
	public void clearCachedData();
}
