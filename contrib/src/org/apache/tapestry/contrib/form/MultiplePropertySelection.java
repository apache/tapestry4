/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.contrib.form;

import java.util.List;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IForm;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.form.AbstractFormComponent;
import org.apache.tapestry.form.IPropertySelectionModel;

/**
 *  A component which uses &lt;input type=checkbox&gt; to
 *  set a property of some object.  Typically, the values for the object
 *  are defined using an {@link org.apache.tapestry.util.Enum}.  A MultiplePropertySelection is dependent on
 *  an {link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select one or more {@link org.apache.tapestry.util.Enum} to assign to a property; the
 * {@link org.apache.tapestry.form.EnumPropertySelectionModel} class simplifies this.
 * 
 *  <p>The {@link org.apache.tapestry.contrib.palette.Palette} component
 *  is more powerful, but requires client-side JavaScript and 
 *  is not fully cross-browser compatible.
 *
 *  <p>
 *
 * <table border=1>
 * <tr>
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Direction</td>
 *    <td>Required</td>
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *
 * <tr>
 *		<td>selectedList</td>
 *		<td>java.util.List</td>
 *		<td>in-out</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The property to set.  During rendering, this property is read, and sets
 * the default value of the options in the select.
 * When the form is submitted, list is cleared, then has each
 * selected option added to it. </td> </tr>
 *
 * <tr>
 *		<td>renderer</td>
 *		<td>{@link IMultiplePropertySelectionRenderer}</td>
 *		<td>in</td>
 *		<td>no</td>
 *		<td>shared instance of {@link CheckBoxMultiplePropertySelectionRenderer}</td>
 *		<td>Defines the object used to render this component.  The default
 *  renders a table of checkboxes.</td></tr>
 *
 *  <tr>
 *		<td>model</td>
 *		<td>{@link IPropertySelectionModel}</td>
 *		<td>in</td>
 *		<td>yes</td>
 *		<td>&nbsp;</td>
 *		<td>The model provides a list of possible labels, and matches those labels
 *  against possible values that can be assigned back to the property.</td> </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>in</td>
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
 * <p>Informal parameters are not allowed.
 *
 *
 *  @version $Id$
 *  @author Sanjay Munjal
 *
 **/

public class MultiplePropertySelection extends AbstractFormComponent
{
    private IPropertySelectionModel model;
    private boolean disabled;
    private IMultiplePropertySelectionRenderer renderer = DEFAULT_CHECKBOX_RENDERER;
    private IBinding selectedListBinding;
    private String name;

    /**
     *  A shared instance of {@link CheckBoxMultiplePropertySelectionRenderer}.
     *
     **/

    public static final IMultiplePropertySelectionRenderer DEFAULT_CHECKBOX_RENDERER =
        new CheckBoxMultiplePropertySelectionRenderer();

    public IBinding getSelectedListBinding()
    {
        return selectedListBinding;
    }

    public void setSelectedListBinding(IBinding value)
    {
        selectedListBinding = value;
    }

    /**
     *  Returns the name assigned to this PropertySelection by the 
     *  {@link IForm}
     *  that wraps it.
     *
     **/

    public String getName()
    {
        return name;
    }

    /**
     *  Returns true if the component is disabled (this is relevant to the
     *  renderer).
     *
     **/

    public boolean isDisabled()
    {
        return disabled;
    }

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    /**
     *  Renders the component, much of which is the responsiblity
     *  of the {@link IMultiplePropertySelectionRenderer renderer}.  The possible options,
     *  their labels, and the values to be encoded in the form are provided
     *  by the {@link IPropertySelectionModel model}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        IForm form = getForm(cycle);

        updateDelegate(form);

        boolean rewinding = form.isRewinding();

        name = form.getElementId(this);

        List selectedList = (List) selectedListBinding.getObject("selectedList", List.class);

        if (selectedList == null)
            throw Tapestry.createRequiredParameterException(this, "selectedList");

        // Handle the form processing first.
        if (rewinding)
        {
            // If disabled, ignore anything that comes up from the client.

            if (disabled)
                return;

            // get all the values
            String[] optionValues = cycle.getRequestContext().getParameters(name);

            // Clear the list

            selectedList.clear();

            // Nothing was selected
            if (optionValues != null)
            {
                // Go through the array and translate and put back in the list
                for (int i = 0; i < optionValues.length; i++)
                {
                    // Translate the new value
                    Object selectedValue = model.translateValue(optionValues[i]);

                    // Add this element in the list back
                    selectedList.add(selectedValue);
                }
            }

            return;
        }

        // Start rendering
        renderer.beginRender(this, writer, cycle);

        int count = model.getOptionCount();

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            // Try to find the option in the list and if yes, then it is checked.
            boolean optionSelected = selectedList.contains(option);

            renderer.renderOption(this, writer, cycle, model, option, i, optionSelected);
        }

        // A PropertySelection doesn't allow a body, so no need to worry about
        // wrapped components.
        renderer.endRender(this, writer, cycle);
    }

    public IPropertySelectionModel getModel()
    {
        return model;
    }

    public void setModel(IPropertySelectionModel model)
    {
        this.model = model;
    }

    public IMultiplePropertySelectionRenderer getRenderer()
    {
        return renderer;
    }

    public void setRenderer(IMultiplePropertySelectionRenderer renderer)
    {
        this.renderer = renderer;
    }

}