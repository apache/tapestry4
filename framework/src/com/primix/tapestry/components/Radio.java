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
 * <tr>
 *		<td>selected</td>
 *		<td>java.lang.Boolean</td>
 *		<td>R / W </td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>Used to indicate whether the given option is selected.  Only updated
 *    if the {@link Radio} is not disabled.
 *		</td>
 *	</tr>
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


public class Radio extends AbstractComponent
{
	private IBinding selectedBinding;
	private IBinding disabledBinding;
	private boolean staticDisabled;
	private boolean disabledValue;

	private static final String[] reservedNames = { "value", "checked", "type", "name"};

	public Radio(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}

	public IBinding getSelectedBinding()
	{
		return selectedBinding;
	}

	/**
	*  Renders the form element, or responds when the form containing the element
	*  is submitted (by checking {@link Form#isRewinding()}.
	*
	*  <table border=1>
	*  <tr>  <th>attribute</th>  <th>value</th> </tr>
	*  <tr> <td>type</td> <td>radio</td> </tr>
	*  <tr> <td>name</td> <td>specified by containing {@link RadioGroup}</td> </tr>
	*  <tr>  <td>value</td>  <td>from {@link IRequestCycle#getNextActionId()}</td> </tr>
	*  <tr>  <td>checked</td> <td>from <code>selected</code> property</td> </tr>
	*  </tr>
	*  </table>
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
		String value;
		RadioGroup group;

		group = RadioGroup.get(cycle);
		if (group == null)
			throw new RequestCycleException(
				"Radio component must be contained within a RadioGroup.",
				this, cycle);

		if (selectedBinding == null)
			throw new RequiredParameterException(this, "selected", null, cycle);

		// The group determines rewinding from the form.

		rewinding = group.isRewinding();

		value = group.getNextOptionId();

		if (staticDisabled)
			disabled = disabledValue;
		else if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();

		if (rewinding)
		{
			if (!disabled)
				selectedBinding.setBoolean(group.isSelected(value));
		}
		else
		{
			writer.beginOrphan("input");

			writer.attribute("type", "radio");

			writer.attribute("name", group.getName());

			if (selectedBinding.getBoolean())
				writer.attribute("checked");

			if (disabled)
				writer.attribute("disabled");

			writer.attribute("value", value);

			generateAttributes(cycle, writer, reservedNames);
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

