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
 *  @since 1.0.3
 */

public abstract class AbstractFormComponent
    extends AbstractComponent
	implements IFormComponent
{
	/**
	 *  Returns the {@link Form} wrapping this component.
	 *
	 *  @throws RequestCycleException if the component is not wrapped by a {@link Form}.
	 *
	 */
	
	public IForm getForm(IRequestCycle cycle)
		throws RequestCycleException
	{
		IForm result = Form.get(cycle);
		
		if (result == null)
			throw new RequestCycleException(
				"This component must be contained within a Form.",
				this);
		
		return result;
	}
	
	public IForm getForm()
	{
		return Form.get(page.getRequestCycle());
	}
	
	abstract public String getName();
}


