package com.primix.tapestry.components;

import com.primix.tapestry.event.ChangeObserver;
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

	public Insert(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getValueBinding()
	{
		return valueBinding;
	}

	/**
	*  Prints its <b>value</b> parameter to the writer.  Does nothing if rewinding, or if
	*  the binding or binding value is null.
	*
	*/

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		Object value;

		if (cycle.isRewinding() || valueBinding == null)
			return;

		value = valueBinding.getValue();

		if (value != null)
			writer.print(value.toString());

	}

	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}
}

