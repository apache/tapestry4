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

package com.primix.tapestry.form;

import com.primix.tapestry.*;

/**
 *  A common interface implemented by all form components (components that
 *  create interactive elements in the rendered page).
 *
 */

public interface IFormComponent extends IComponent
{
	/**
	 *  Returns the {@link Form} which contains the component.
	 *
	 *  @returns the {@link Form}, or null if the component is not contained by a Form,
	 *  of if the containing Form is not currently renderring.
	 */

	public IForm getForm();

	/**
	 *  Returns the name of the component, which is automatically generated
	 *  during renderring.
	 *
	 *  <p>This value is set inside the component's render method and is
	 *  <em>not</em> cleared.  If the component is inside a {@link com.primix.tapestry.components.Foreach}, the
	 *  value returned is the most recent name generated for the component.
	 *
	 *  <p>This property is made available to facilitate writing JavaScript that
	 *  allows components (in the client web browser) to interact.
	 *
	 *  <p>In practice, a {@link com.primix.tapestry.html.Script} component
	 *  works with the {@link com.primix.tapestry.html.Body} component to get the
	 *  JavaScript code inserted and referenced.
	 *
	 */

	public String getName();
}