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

package com.primix.tapestry.form;

import com.primix.tapestry.*;

// Appease Javadoc
import com.primix.tapestry.components.*;
import com.primix.tapestry.html.*;

/**
 *  A base class for building components that correspond to HTML form elements.
 *  All such components must be wrapped (directly or indirectly) by
 *  a {@link Form} component.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public abstract class AbstractFormComponent
    extends AbstractComponent
{
	/**
	*  Returns the {@link Form} wrapping this component.
	*
	*  @throws RequestCycleException if the component is not wrapped by a {@link Form}.
	*
	*/

	public Form getForm(IRequestCycle cycle)
	throws RequestCycleException
	{
		Form result;

		result = Form.get(cycle);

		if (result == null)
			throw new RequestCycleException(
				"This component must be contained within a Form.",
				this);

		return result;
	}

	/**
	*  Returns the name of the component, which is automatically generated
	*  during renderring.
	*
	*  <p>This value is set inside the component's render method and is
	*  <em>not</em> cleared.  If the component is inside a {@link Foreach}, the
	*  value returned is the most recent name generated for the component.
	*
	*  <p>This property is made available to facilitate writing JavaScript that
	*  allows components (in the client web browser) to interact.  It's just a bit
	*  of a hack, but is quite necessary.
	*
	*  <p>In practice, a {@link Delegator} component is used to transfer renderring
	*  control to custom Java code that gets the name of the form and the particular 
	*  components, and works with the {@link Body} component to get the
	*  JavaScript code inserted and referenced.
	*
	*/

	abstract public String getName();
}


