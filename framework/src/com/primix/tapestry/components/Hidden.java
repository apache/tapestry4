package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

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
 *  Implements a hidden field within a {@link Form}.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *  <tr>
 *		<td>value</td>
 *		<td>java.lang.String</td>
 *		<td>R / W</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The value to be stored in the the hidden field.  The parameter is read
 *  when the HTML response is generated, and then written when the form is submitted.
 *	</tr>
 *
 * <tr>
 *		<td>listener</td>
 *		<td>{@link IActionListener}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>A listener that is informed after the value parameter is updated.</td>
 
 
 *	</table>
 *
 * <p>Does not allow informal parameters, and may not contain a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Hidden extends AbstractFormComponent
{
	private IBinding valueBinding;
	
	private static final String[] reservedNames = 
	{ "type", "name", "value" 
	};

	public Hidden(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}
	
	public IBinding getValueBinding()
	{
		return valueBinding;
	}
	
	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		Form form;
		String name;
		boolean rewinding;
		String value;
		
		form = getForm(cycle);
		
		name = "Hidden" + cycle.getNextActionId();
	}
}

