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
 *  Implements a component that manages an HTML &lt;option&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link Select} component.
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
 *
 * <tr>
 *		<td>selected</td>
 *		<td>java.lang.Boolean</td>
 *		<td>R / W </td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>Used to indicate whether the given option is selected.
 *		</td>
 *	</tr>
 *
 *  <tr>
 *		<td>label</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>A string which represents the option that may be selected.  This is optional;
 *		any text that follows the &lt;option&gt; tag is considered the label, but this
 *      saves the designed from including one more {@link Insert} component.
 *		</td>
 *	</tr>
 *	</table>
 *
 * <p>Allows informal parameters, but may not contain a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Option extends AbstractComponent
{
	private IBinding selectedBinding;
	private IBinding labelBinding;
	private String labelValue;

	private static final String[] reservedNames = { "value" };

	public IBinding getLabelBinding()
	{
		return labelBinding;
	}

	public IBinding getSelectedBinding()
	{
		return selectedBinding;
	}

	/**
	*  Renders the &lt;option&gt; element, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	*  <table border=1>
	*  <tr>  <th>attribute</th>  <th>value</th> </tr>
	*  <tr>  <td>value</td>  <td>from {@link IRequestCycle#getNextActionId()}</td>  </tr>
	*  <tr> <td>selected</td> <td>from selected property</td> </tr>
	*  <tr> <td><i>other</i></td> <td>from extra bindings</td> </tr>
	*  </tr>
	*  </table>
	*
	* <p>If the <code>label</code> property is set, it is inserted after the
	* &lt;option&gt; tag.
	*
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		String value;
		boolean compressed = false;
		String label = null;
		Select select;
		boolean rewinding;

		select = Select.get(cycle);
		if (select == null)
			throw new RequestCycleException(
				"Option component must be contained within a Select.",
				this, cycle);

		if (selectedBinding == null)
			throw new RequiredParameterException(this, "selected", null, cycle);

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = select.isRewinding();

		value = select.getNextOptionId();

		if (rewinding)
		{
			if (!select.isDisabled())
				selectedBinding.setBoolean(select.isSelected(value));
		}
		else
		{
			compressed = writer.compress(true);

			writer.beginOrphan("option");

			writer.attribute("value", value);

			if (selectedBinding.getBoolean())
				writer.attribute("selected");

			generateAttributes(cycle, writer, reservedNames);

			if (labelValue != null)
				label = labelValue;
			else if (labelBinding != null)
				label = labelBinding.getString();

			if (label != null)
				writer.print(label);
		}

		if (!rewinding)
		{
			writer.setCompressed(compressed);
		}

	}

	public void setLabelBinding(IBinding value)
	{
		labelBinding = value;

		if (value.isStatic())
			labelValue = value.toString();
	}

	public void setSelectedBinding(IBinding value)
	{
		selectedBinding = value;
	}
}

