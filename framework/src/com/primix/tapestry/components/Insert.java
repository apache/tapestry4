package com.primix.tapestry.components;

import com.primix.tapestry.event.ChangeObserver;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import java.text.Format;

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
 *  Used to insert some text (from a parameter) into the HTML.
 *
 *
  * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th>
 * <th>Read / Write</th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 * <tr>
 *  <td>value</td> <td>Object</td> <td>R</td>
 *  <td>no</td> <td>&nbsp;</td>
 *  <td>The value to be inserted.  If the binding is null, then nothing is inserted.
 *  Any object may be inserted, the <code>toString()</code> method is used
 *  to convert it to a printable value.</td> </tr>
 *
 * <tr>
 *	<td>format</td>
 *	<td>{@link Format}</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td>An optional format object used to convert the value parameter for
 *  insertion into the HTML response. </td> </tr>
 *
 * </table>
 *
 * <p>Informal parameters are not allowed.  The component must not have a body.
 *
 * @author Howard Ship
 * @version $Id$
 */


 
public class Insert extends AbstractComponent
{
	private IBinding valueBinding;
	private IBinding formatBinding;

	public Insert(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getFormatBinding()
	{
		return formatBinding;
	}
	
	public void setFormatBinding(IBinding value)
	{
		formatBinding = value;
	}
	
	public IBinding getValueBinding()
	{
		return valueBinding;
	}

	/**
	*  Prints its value parameter, possibly formatted by its format parameter.
	*  Notes:
	*  <ul>
	*  <li>If the cycle is rewinding, then this method does nothing.
	*  <li>If both the value and the format are null, then this method does nothing
	*  <li>If the format is non-null, then {@link Format#format(Object)} is invoked,
	*  even if the value is null
	*  </ul>
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		Object value = null;
		Format format = null;
		String insert;

		if (cycle.isRewinding())
			return;

		if (valueBinding != null)
			value = valueBinding.getValue();

		if (formatBinding != null)
			format = (Format)formatBinding.getValue();
		
		if (value == null && format == null)
			return;
			
		if (format == null)
			insert = value.toString();
		else
			insert = format.format(value);

		writer.print(insert);
			
	}

	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}
}

