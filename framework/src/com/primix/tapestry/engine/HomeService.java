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

package com.primix.tapestry.engine;

import javax.servlet.ServletException;

import java.io.IOException;

import com.primix.tapestry.*;

/**
 *  An implementation of the home service that renders the Home page.
 *  This is the most likely candidate for overriding ... for example,
 *  to select the page to render based on known information about the
 *  user (stored as a cookie).
 * 
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.9
 *
 */

public class HomeService extends AbstractService
{

	public Gesture buildGesture(
		IRequestCycle cycle,
		IComponent component,
		String[] parameters)
	{
		if (parameters != null && parameters.length > 0)
			throw new IllegalArgumentException(
				Tapestry.getString("service-no-parameters", HOME_SERVICE));

		return assembleGesture(cycle, HOME_SERVICE, null, null, true);
	}

	public boolean service(
		IEngineServiceView engine,
		IRequestCycle cycle,
		ResponseOutputStream output)
		throws RequestCycleException, ServletException, IOException
	{

		IPage home = cycle.getPage(IEngine.HOME_PAGE);

		home.validate(cycle);

		// If it validates, then render it.

		cycle.setPage(home);

		engine.renderResponse(cycle, output);

		return true;
	}

	public String getName()
	{
		return HOME_SERVICE;
	}

}