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
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 * <table border=1>
 * <tr> 
 *    <td>Property</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 *
 *  <tr>
 *      <td>value</td>
 *      <td>{@link Object}</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>Boolean.TRUE</td>
 *      <td>The value is used to determine if the radio button is initially selected
 * (when rendering) and is the value assigned to the selected parameter when the
 *  form is submitted, if the HTML radio button is selected.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, then the <code>Radio</code> is disabled.  It will
 * write a disabled attribute into its tag when rendering, and will not update
 * its selected binding.
 * 		<p>A binding may also be disabled if its containing {@link RadioGroup} is
 *		   disabled. </td> </tr>
 *	</table>
 *
 * <p>Informal parameters are allowed, but may not contain a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


package com.primix.tapestry.form;

import com.primix.tapestry.*;

public class Radio extends AbstractComponent
{
	private IBinding disabledBinding;
	private boolean staticDisabled;
	private boolean disabledValue;
	private IBinding valueBinding;
	private Object staticValue;

	private static final String[] reservedNames = { "value", "checked", "type", "name"};

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;

		staticDisabled = value.isStatic();
		if (staticDisabled)
			disabledValue = value.getBoolean();
	}

	public IBinding getValueBinding()
	{
		return valueBinding;
	}

	public void setValueBinding(IBinding value)
	{
		valueBinding = value;

		if (valueBinding.isStatic())
			staticValue = valueBinding.getObject();
	}

	public Object getValue()
	{
		if (staticValue != null)
			return staticValue;

		if (valueBinding == null)
			return Boolean.TRUE;

		return valueBinding.getObject();
	}

	/**
	*  Renders the form element, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	*
	* <p>If the <code>label</code> property is set, it is inserted after the
	* &lt;input&gt; tag.
	*
	**/

	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		boolean rewinding;
		boolean disabled = false;
		IBinding binding;
		String name;
		int option;
		RadioGroup group;

		group = RadioGroup.get(cycle);
		if (group == null)
			throw new RequestCycleException(
				"Radio component must be contained within a RadioGroup.",
				this);

		// The group determines rewinding from the form.

		rewinding = group.isRewinding();

		option = group.getNextOptionId();

		if (staticDisabled)
			disabled = disabledValue;
		else if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();

		if (rewinding)
		{
			// If not disabled and this is the selected button within the radio group,
			// then update set the selection from the group to the value for this
			// radio button.  This will update the selected parameter of the RadioGroup.

			if (!disabled &&
				group.isSelected(option))
				group.updateSelection(getValue());
			return;
		}

		writer.beginEmpty("input");

		writer.attribute("type", "radio");

		writer.attribute("name", group.getName());

		// As the group if the value for this Radio matches the selection
		// for the group as a whole; if so this is the default radio and is checked.

		if (group.isSelection(getValue()))
			writer.attribute("checked");

		if (disabled)
			writer.attribute("disabled");

		// The value for the Radio matches the option number (provided by the RadioGroup).
		// When the form is submitted, the RadioGroup will know which option was,
		// in fact, selected by the user.

		writer.attribute("value", option);

		generateAttributes(cycle, writer, reservedNames);

	}

}

