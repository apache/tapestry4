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
 *  Implements a component that manages an HTML &lt;submit&gt; form element.
 *
 *  <p>This component is generally only used when the form has multiple
 *  submit buttons, and it is important for the application to know
 *  which one was pressed.  You may also want to use
 *  {@link ImageButton} which accomplishes much the same thing, but uses
 *  a graphic image instead.
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
 *		<td>label</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>The label put on the button (this becomes the HTML value attribute).
 *		</td>
 *	</tr>
 *
 *  <tr>
 *	  <td>disabled</id>
 *	  <td>boolean</td>
 *    <td>R</td>
 *    <td>no</td>
 *	  <td>false</td>
 *    <td>If set to true, the button will be disabled (will not respond to
 *  the mouse); the browser should provide a "greyed out" appearance.
 *  </td> </tr>
 *
 *  <tr>
 *      <td>selected</td>
 *      <td>java.lang.Object</td>
 *      <td>W</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>This parameter is bound to a property that is
 *    updated when the submit button is clicked by the user.  The property
 *  is updated to match the tag parameter.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>tag</td>
 *      <td>java.lang.Object</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Tag used with the selected parameter to indicate which Submit button
 *  on a form was clicked.</td>
 *  </tr>
 *
 *	</table>
 *
 * <p>Allows informal parameters, but may not contain a body.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Submit extends AbstractFormComponent
{
	private IBinding labelBinding;
	private String staticLabelValue;
	
	private IBinding disabledBinding;

    private IBinding selectedBinding;
    private IBinding tagBinding;
    private Object staticTagValue;
	
	private static final String[] reservedNames = 
	{ "type", "name" };

	public Submit(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}
	
	public IBinding getLabelBinding()
	{
		return labelBinding;
	}
	
	public void setLabelBinding(IBinding value)
	{
		labelBinding = value;
		
		if (value.isStatic())
			staticLabelValue = value.getString();
	}

	public IBinding getDisabledBinding()
	{
		return disabledBinding;
	}
	
	public void setDisabledBinding(IBinding value)
	{
		disabledBinding = value;
	}
	
    public void setSelectedBinding(IBinding value)
    {
        selectedBinding = value;
    }

    public IBinding getSelectedBinding()
    {
        return selectedBinding;
    }

    public void setTagBinding(IBinding value)
    {
        tagBinding = value;

        if (value.isStatic())
            staticTagValue = value.getValue();
    }

    public IBinding getTagBinding()
    {
        return tagBinding;
    }

	public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		Form form;
		boolean rewinding;
		String name;
		String label = null;
		boolean disabled = false;
		String value;
        Object tagValue = staticTagValue;
		
		form = getForm(cycle);
		
		rewinding = form.isRewinding();
		
		name = form.getNextElementId("Submit");
		
		if (disabledBinding != null)
			disabled = disabledBinding.getBoolean();
		
		if (rewinding)
		{
			// Don't bother doing anything if disabled.
			
			if (disabled)
				return;
				
            // How to know which Submit button was actually
            // clicked?  When submitted, it produces a request parameter
            // with its name and value (the value serves double duty as both
            // the label on the button, and the parameter value).
         
			value = cycle.getRequestContext().getParameter(name);

            // If the value isn't there, then this button wasn't
            // selected.

            if (value == null)
                return;

            // OK, now to notify the application code (via the parameters)
            // that *this* Submit was selected.  We do this by applying
            // a tag (presumably, specific to the Submit in question)
            // to the selected binding.


            if (tagValue == null)
                tagValue = tagBinding.getValue();

            if (tagValue == null)
                throw new RequiredParameterException(this, "tag", tagBinding, cycle);

            selectedBinding.setValue(tagValue);

            return;
		}
		
		if (staticLabelValue != null)
			label = staticLabelValue;
		else
			if (labelBinding != null)
				label = labelBinding.getString();
					
		writer.beginOrphan("input");
		writer.attribute("type", "submit");
		writer.attribute("name", name);
		
		if (disabled)
			writer.attribute("disabled");
		
		if (label != null)
			writer.attribute("value", label);	

		generateAttributes(cycle, writer, reservedNames);
		
		writer.closeTag();
	}	
	
}

