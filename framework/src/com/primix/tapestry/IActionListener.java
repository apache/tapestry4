package com.primix.tapestry;

import com.primix.tapestry.components.html.link.*;
import com.primix.tapestry.components.html.form.*;

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
 * Defines a listener to an {@link Action}, {@link Form} or some kind
 * of {@link AbstractFormComponent form component}, which is way to
 * get behavior when the component's URL is triggered (or the form
 * containing the component is submitted).
 *
 * @author Howard Ship
 * @version $Id$
 */


public interface IActionListener
{

	/**
	*  Method invoked by the component (an {@link Action}, {@link
	*  Form} or some kind of {@link AbstractFormComponent form
	*  component}), when its URL is triggered.
	*
	*  @param component The component which was "triggered".
	*  @param cycle The request cycle in which the component was triggered.
	*/

	public void actionTriggered(IComponent component, IRequestCycle cycle)
	throws RequestCycleException;
}
