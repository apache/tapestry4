package com.primix.tapestry.components.html.form;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import com.primix.tapestry.components.html.*;

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
 *		<td>A listener that is informed after the value parameter is updated.  This
 * allows the data set operated on by the rest of the {@link Form} components
 * to be synchronized.
 *
 *  <p>A typical use is to encode the primary key of an entity as a Hidden; when the
 *  form is submitted, the Hidden's listener re-reads the corresponding entity
 *  from the database.</td>
 *  </tr>
 * 
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
	private IBinding listenerBinding;
	private String name;

	public String getName()
	{
		return name;
	}

	public IBinding getValueBinding()
	{
		return valueBinding;
	}

	public void setValueBinding(IBinding value)
	{
		valueBinding = value;
	}

	public IBinding getListenerBinding()
	{
		return listenerBinding;
	}

	public void setListenerBinding(IBinding value)
	{
		listenerBinding = value;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		Form form;
		boolean formRewound;
		String value;
		IActionListener listener;

			form = getForm(cycle);
		formRewound = form.isRewinding();

			name = form.getNextElementId("Hidden");

		// If the form containing the Hidden isn't rewound, then render.

		if (!formRewound)
		{
			// Optimiziation: if the page is rewinding (some other action or
			// form was submitted), then don't bother rendering.

			if (cycle.isRewinding())
				return;

				value = valueBinding.getString();

			writer.beginOrphan("input");
			writer.attribute("type", "hidden");
			writer.attribute("name", name);
			writer.attribute("value", value);

			return;
		}

		value = cycle.getRequestContext().getParameter(name);

		// A listener is not always necessary ... it's easy to code
		// the synchronization as a side-effect of the accessor method.

		valueBinding.setString(value);

		if (listenerBinding == null)
			return;

		try
		{
			listener = (IActionListener)listenerBinding.getValue();
		}
		catch (ClassCastException e)
		{
			throw new RequestCycleException("Parameter listener is not type IActionListener.",
				this, cycle, e);
		}

		if (listener != null)
			listener.actionTriggered(this, cycle);
	}
}

