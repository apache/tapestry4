package com.primix.tapestry.components;

import com.primix.tapestry.*;
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
 *		<td>renderer</td>
 *		<td>{@link IPropertySelectionRenderer}</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>shared instance of {@link SelectPropertySelectionRenderer}</td>
 *		<td>Defines the object used to render the PropertySelection.
 * <p>{@link SelectPropertySelectionRenderer} renders the component as a &lt;select&gt;.
 * <p>{@link RadioPropertySelectionRenderer} renders the component as a table of
 * radio buttons.</td></tr>
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
 * does not update its value parameter.
 *			
 *			<p>Corresponds to the <code>disabled</code> HTML attribute.</td>
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
	private IBinding valueBinding;
	private IBinding modelBinding;
	private IBinding disabledBinding;
	private IBinding rendererBinding;
	private String name;
	private boolean disabled;
	
	private static IPropertySelectionRenderer defaultSelectRenderer;
	private static IPropertySelectionRenderer defaultRadioRenderer;
	
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
	
	public void setRendererBinding(IBinding value)
	{
		rendererBinding = value;
	}
	
	public IBinding getRendererBinding()
	{
		return rendererBinding;
	}
	
	/**
	 *  Returns the name assigned to this PropertySelection by the {@link Form}
	 *  that wraps it.
	 *
	 */
	 
	public String getName()
	{
		return name;
	}

	/**
	 *  Returns true if this PropertySelection's disabled parameter yields true.
	 *  The corresponding HTML control(s) should be disabled.
	 */
	 
	public boolean isDisabled()
	{
		return disabled;
	}
	
	/**
	 *  Returns the default {@link SelectPropertySelectionRenderer} instance.
	 *  This is a shared instance.
	 *
	 */
	 
	public IPropertySelectionRenderer getDefaultSelectRenderer()
	{
		if (defaultSelectRenderer == null)
			defaultSelectRenderer = new SelectPropertySelectionRenderer();
		
		return defaultSelectRenderer;	
	}
	
	/**
	 *  Returns a shared instance of {@link RadioPropertySelectionRenderer}.
	 *
	 */
	 
	public IPropertySelectionRenderer getDefaultRadioRenderer()
	{
		if (defaultRadioRenderer == null)
			defaultRadioRenderer = new RadioPropertySelectionRenderer();
			
		return defaultRadioRenderer;
	}
	
	/**
	 *  Renders the component, much of which is the responsiblity
	 *  of the {@link IPropertySelectionRenderer renderer}.  The possible options,
	 *  thier labels, and the values to be encoded in the form are provided
	 *  by the {@link IPropertySelectionModel model}.
	 *
	 */
	 
	public void render(IResponseWriter writer, IRequestCycle cycle)
	throws RequestCycleException
	{
		Form form;
		boolean rewinding;
		IPropertySelectionModel model;
		IPropertySelectionRenderer renderer = null;
		Object newValue;
		Object currentValue;
		Object option;
		String optionValue;
		int i;
		boolean selected = false;
		boolean foundSelected = false;
		int count;
		boolean radio = false;
		
		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		if (disabledBinding == null)
			disabled = false;
		else
			disabled = disabledBinding.getBoolean();
		
		model = (IPropertySelectionModel)modelBinding.getValue();
		if (model == null)
			throw new RequiredParameterException(this, "model", modelBinding, cycle);

		name = form.getNextElementId("PropertySelection");
		
		try
		{
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
				
				return;	
			}
			
			if (rendererBinding != null)
				renderer = (IPropertySelectionRenderer)rendererBinding.getValue();
				
			if (renderer == null)
				renderer = getDefaultSelectRenderer();
		
			renderer.beginRender(this, writer, cycle);
					
			count = model.getOptionCount();
			currentValue = valueBinding.getValue();
				
			for (i = 0; i < count; i++)
			{
				option = model.getOption(i);
				
				if (!foundSelected)
				{
					selected = isEqual(option, currentValue);
					if (selected)
						foundSelected = true;
				}		
				
				renderer.renderOption(this, writer, cycle, model, option, i, selected);

				selected = false;			
			}
					
			// A PropertySelection doesn't allow a body, so no need to worry about
			// wrapped components.
			
			renderer.endRender(this, writer, cycle);
		}
		finally
		{
			name = null;
		}
	}
	
	private boolean isEqual(Object left, Object right)
	{
		// Both null, or same object, then are equal
		
		if (left == right)
			return true;
		
		// If one is null, the other isn't, then not equal.
		
		if (left == null || right == null)
			return false;
		
		// Both non-null; use standard comparison.
			
		return left.equals(right);	
	}
}
 