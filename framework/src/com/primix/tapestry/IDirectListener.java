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
 * Defines a listener to an {@link IDirect} component, which is a how the
 * application produces behavior when
 * the component is triggered.
 *
 * @author Howard Ship
 * @version $Id$
 */

public interface IDirectListener
{

	/**
	*  Method invoked by the {@link IDirect} component, when its URL
	*  is triggered.
	*  
	*  <p>A direct service URL includes an array of application specific
	*  strings, the context, which define contextual state for the action.
	*  Note that null values provided to the component when rendering will
	*  be converted to empty strings before invoking the listener.
	*
	*  @param component The component which was "triggered".
	*  @param parameters An array of parameters for the action.
	*  @param cycle The request cycle in which the component was triggered.
	*/

	public void directTriggered(
		IDirect component,
		String[] context,
		IRequestCycle cycle)
		throws RequestCycleException;
}