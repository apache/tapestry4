//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

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
 *  are defined using an {@link org.apache.commons.lang.enum.Enum}.  A MultiplePropertySelection is dependent on
 *  an {link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select one or more {@link org.apache.commons.lang.enum.Enum} to assign to a property; the
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

public abstract class MultiplePropertySelection extends AbstractFormComponent
{

    /**
     *  A shared instance of {@link CheckBoxMultiplePropertySelectionRenderer}.
     *
     **/

    public static final IMultiplePropertySelectionRenderer DEFAULT_CHECKBOX_RENDERER =
        new CheckBoxMultiplePropertySelectionRenderer();

    public abstract IBinding getSelectedListBinding();

    protected void finishLoad()
    {
        setRenderer(DEFAULT_CHECKBOX_RENDERER);
    }

    /**
     *  Returns true if the component is disabled (this is relevant to the
     *  renderer).
     *
     **/

    public abstract boolean isDisabled();

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

        boolean rewinding = form.isRewinding();

        String name = form.getElementId(this);

        List selectedList = (List) getSelectedListBinding().getObject("selectedList", List.class);

        if (selectedList == null)
            throw Tapestry.createRequiredParameterException(this, "selectedList");

        IPropertySelectionModel model = getModel();

        if (model == null)
            throw Tapestry.createRequiredParameterException(this, "model");

        // Handle the form processing first.
        if (rewinding)
        {
            // If disabled, ignore anything that comes up from the client.

            if (isDisabled())
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

        IMultiplePropertySelectionRenderer renderer = getRenderer();

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

    public abstract IPropertySelectionModel getModel();

    public abstract IMultiplePropertySelectionRenderer getRenderer();

    public abstract void setRenderer(IMultiplePropertySelectionRenderer renderer);

}