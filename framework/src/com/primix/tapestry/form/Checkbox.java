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
 *  Implements a component that manages an HTML &lt;input type=checkbox&gt;
 *  form element.
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
 *    <td>selected</td>
 *    <td>java.lang.Boolean</td>
 *    <td>R / W</td>
 *   	<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>Indicates whether the checkbox is selected or not.
 *
 *			<p>Corresponds to the <code>checked</code> HTML attribute.</td>
 *	</tr>
 *
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the text field is active or not.  If disabled, then
 *			any value that comes up when the form is submitted is ignored.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *
 *	</table>
 *
 * <p>Informal parameters are allowed.  A body is not allowed.
 
 *
 *  @author Howard Ship
 *  @version $Id$
 */

public class Checkbox extends AbstractFormComponent
{
	private IBinding selectedBinding;
	private IBinding disabledBinding;
	private boolean staticDisabled;
	private boolean disabledValue;

	private String name;

	public String getName()
	{
		return name;
	}

	private static final String[] reservedNames = 
    { "type", "name", "checked"};

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public IBinding getSelectedBinding()
	{
		return selectedBinding;
	}


	/**
	*  Renders the form elements, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	*  <p>In traditional HTML, many checkboxes would have the same name but different values.
	*  Under Tapestry, it makes more sense to have different names and a fixed value.
	*  For a checkbox, we only care about whether the name appears as a request parameter.
	*
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle) 
	throws RequestCycleException
	{
		boolean rewinding;
		String value;
		boolean disabled = false;
		Form form;
		boolean checked;

		form = getForm(cycle);

		// It isn't enough to know whether the cycle in general is rewinding, need to know
		// specifically if the form which contains this component is rewinding.

		rewinding = form.isRewinding();

		// Used whether rewinding or not.

		name = form.getNextElementId("Checkbox");

		if (staticDisabled)
			disabled = disabledValue;
		else if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();

		if (rewinding)
		{
			if (!disabled)
			{
				value = cycle.getRequestContext().getParameter(name);

				checked = (value != null);

				selectedBinding.setBoolean(checked);
			}

		}
		else
		{
			checked = selectedBinding.getBoolean();

			writer.beginEmpty("input");
			writer.attribute("type", "checkbox");

			writer.attribute("name", name);

			if (disabled)
				writer.attribute("disabled");

			if (checked)
				writer.attribute("checked");

			generateAttributes(cycle, writer, reservedNames);

			writer.closeTag();
		}

	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;

		staticDisabled = value.isStatic();
		if (staticDisabled)
			disabledValue = value.getBoolean();
	}

	public void setSelectedBinding(IBinding value)
	{
		selectedBinding = value;
	}
}

