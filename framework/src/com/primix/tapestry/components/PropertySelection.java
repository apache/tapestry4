package com.primix.tapestry.components;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.foundation.*;

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
 *  A component which uses either
 *   &lt;select&gt; and &lt;option&gt; elements 
 *  or &lt;input type=radio&gt; to
 *  set a property of some object.  Typically, the values for the object
 *  are defined using an {@link Enum}.  A PropertySelection is dependent on
 *  an {link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select a particular {@link Enum} to assign to a property; the
 * {@link EnumPropertySelectionModel} class simplifies this.
 *
 *  <p>
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
 * <tr>
 *		<td>value</td>
 *		<td>java.lang.Object</td>
 *		<td>R / W</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The property to set.  During rendering, this property is read, and sets
 * the default value of the selection (if it is null, no element is selected).
 * When the form is submitted, this property is updated based on the new
 * selection. </td> </tr>
 *
 * <tr>
 *		<td>radio</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>If true, then the component renders itself as a list of &lt;input type=radio&gt;
 *	controls, instead of a &lt;select&gt;.
 *
 *  <p>In radio mode, the inputs are preceded by a &lt;br&gt;.</td></tr>
 *
 *  <tr>
 *		<td>model</td>
 *		<td>{@link IPropertySelectionModel}</td>
 *		<td>R</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The model provides a list of possible labels, and matches those labels
 *  against possible values that can be assigned back to the property.</td> </tr>
 * 
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Controls whether the &lt;select&gt; is active or not. A disabled PropertySelection
 * does not update its value parameter, and doesn't notify its listener when the form
 * is submitted.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
 *	</tr>
 *
 *  <tr>
 *    <td>listener</td>
 *    <td>{@link IActionListener}</td>
 * 	  <td>R</td>
 * 	  <td>no</td>
 *	  <td>&nbsp;</td>
 *	  <td>The listener, informed <em>after</em> the property value is updated.</td>
 *	</tr>
 *
 *	</table>
 *
 * <p>Informal parameters are allowed, and are applied to the &lt;select&gt; element.
 *  A body is not allowed.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
  
public class PropertySelection extends AbstractFormComponent
{
	private static final String[] reservedNames = 
	{ "name" };
	
	private IBinding valueBinding;
	private IBinding modelBinding;
	private IBinding disabledBinding;
	private IBinding radioBinding;
	private boolean staticRadioBinding;
	private boolean staticRadioValue;
	
	public PropertySelection(IPage page, IComponent container, String id,
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
		
	public IBinding getModelBinding()
	{
		return modelBinding;
	}
	
	public void setModelBinding(IBinding value)
	{
		modelBinding = value;
	}
	
	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}
	
	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}
	
	public IBinding getRadioBinding()
	{
		return radioBinding;
	}
	
	public void setRadioBinding(IBinding value)
	{
		radioBinding = value;
		staticRadioBinding = value.isStatic();
		
		if (staticRadioBinding)
			staticRadioValue = value.getBoolean();
	}	
	
	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		String name;
		IActionListener listener;
		Form form;
		boolean rewinding;
		boolean disabled;
		IPropertySelectionModel model;
		Object newValue;
		Object currentValue;
		Object option;
		String label;
		String optionValue;
		int i;
		boolean needDefault = true;
		int index;
		int count;
		boolean radio = false;
		
		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		name = "PropertySelection" + cycle.getNextActionId();
		
		if (disabledBinding == null)
			disabled = false;
		else
			disabled = disabledBinding.getBoolean();
		
		model = (IPropertySelectionModel)modelBinding.getValue();
		if (model == null)
			throw new RequiredParameterException(this, "model", cycle);
		
		if (rewinding)
		{
			// If disabled, ignore anything that comes up from the client.
			
			if (disabled)
				return;		
			
			optionValue = cycle.getRequestContext().getParameter(name);
			
			if (optionValue == null)
				newValue = null;
			else
				newValue = model.translateValue(optionValue);
			
			valueBinding.setValue(newValue);
			
			listener = getListener(cycle);
			if (listener != null)
				listener.actionTriggered(this, cycle);
			
			return;	
		}
		
		if (staticRadioBinding)
			radio = staticRadioValue;
		else
			if (radioBinding != null)
				radio = radioBinding.getBoolean();	
			
		// Renderring, not rewinding, so generate the <select> and <option> tags.
		
		if (!radio)
		{
			writer.begin("select");
			writer.attribute("name", name);
			
			if (disabled)
				writer.attribute("disabled");
			
			generateAttributes(cycle, writer, reservedNames);
		}
		
		count = model.getOptionCount();
		currentValue = valueBinding.getValue();
			
		for (i = 0; i < count; i++)
		{
			option = model.getOption(i);
			label = model.getLabel(i);
			optionValue = model.getValue(i);
			
			// This gets a bit tricky, since sometimes we're using <input type=radio>
			// and sometimes <option> within a <select>
			
			if (radio)
			{
				if (i > 0)
					writer.beginOrphan("br");
				
				writer.beginOrphan("input");
				writer.attribute("type", "radio");
				writer.attribute("name", name);
				
				if (disabled)
					writer.attribute("disabled");
			}
			else
				writer.beginOrphan("option");
	
			writer.attribute("value", optionValue);
			
			// We use needDefault as an optimization ... once the default/selected
			// value has been found, we can stop looking, which saves on
			// calls to equals().
			
			if (needDefault)
			{
				if (option.equals(currentValue))
				{
					if (radio)
						writer.attribute("checked");
					else	
						writer.attribute("selected");
					
					needDefault = false;
				}
			}
			
			writer.print(label);
		}
		
		if (!radio)
			writer.end("select");
		
		// A PropertySelection doesn't allow a body, so no need to worry about
		// wrapped components.
	}
}
 