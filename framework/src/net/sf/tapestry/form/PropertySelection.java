//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.form;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.Tapestry;

/**
 *  A component which uses either
 *   &lt;select&gt; and &lt;option&gt; elements 
 *  or &lt;input type=radio&gt; to
 *  set a property of some object.  Typically, the values for the object
 *  are defined using an {@link net.sf.tapestry.util.Enum}.  
 *  A PropertySelection is dependent on
 *  an {@link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select a particular 
 *  {@link net.sf.tapestry.util.Enum} to assign to a property; the
 *  {@link EnumPropertySelectionModel} class simplifies this.
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
 *		<td>value</td>
 *		<td>java.lang.Object</td>
 *		<td>in-out</td>
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
 *		<td>in</td>
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
 * <p>Informal parameters are not allowed,  A body is not allowed.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class PropertySelection extends AbstractFormComponent
{
    private IPropertySelectionRenderer renderer;
    private IPropertySelectionModel model;
    private boolean disabled;

    private IBinding valueBinding;
    private String name;

    /**
     *  A shared instance of {@link SelectPropertySelectionRenderer}.
     *
     **/

    public static final IPropertySelectionRenderer DEFAULT_SELECT_RENDERER =
        new SelectPropertySelectionRenderer();

    /**
     *  A shared instance of {@link RadioPropertySelectionRenderer}.
     *
     **/

    public static final IPropertySelectionRenderer DEFAULT_RADIO_RENDERER =
        new RadioPropertySelectionRenderer();

    public IBinding getValueBinding()
    {
        return valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        valueBinding = value;
    }

    /**
     *  Returns the name assigned to this PropertySelection by the {@link Form}
     *  that wraps it.
     *
     **/

    public String getName()
    {
        return name;
    }

    /**
     *  Returns true if this PropertySelection's disabled parameter yields true.
     *  The corresponding HTML control(s) should be disabled.
     **/

    public boolean isDisabled()
    {
        return disabled;
    }

    /**
     *  Renders the component, much of which is the responsiblity
     *  of the {@link IPropertySelectionRenderer renderer}.  The possible options,
     *  thier labels, and the values to be encoded in the form are provided
     *  by the {@link IPropertySelectionModel model}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        IForm form = getForm(cycle);
        if (form == null)
            throw new RequestCycleException(
                Tapestry.getString("must-be-wrapped-by-form", "PropertySelection"),
                this);

        boolean rewinding = form.isRewinding();

        name = form.getElementId(this);

        if (rewinding)
        {
            // If disabled, ignore anything that comes up from the client.

            if (disabled)
                return;

            String optionValue = cycle.getRequestContext().getParameter(name);

            Object newValue =
                (optionValue == null) ? null : model.translateValue(optionValue);

            valueBinding.setObject(newValue);

            return;
        }

        IPropertySelectionRenderer finalRenderer =
            (renderer == null) ? DEFAULT_SELECT_RENDERER : renderer;

        finalRenderer.beginRender(this, writer, cycle);

        int count = model.getOptionCount();
        Object currentValue = valueBinding.getObject();
        boolean foundSelected = false;
        boolean selected = false;

        for (int i = 0; i < count; i++)
        {
            Object option = model.getOption(i);

            if (!foundSelected)
            {
                selected = isEqual(option, currentValue);
                if (selected)
                    foundSelected = true;
            }

            finalRenderer.renderOption(this, writer, cycle, model, option, i, selected);

            selected = false;
        }

        // A PropertySelection doesn't allow a body, so no need to worry about
        // wrapped components.

        finalRenderer.endRender(this, writer, cycle);
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

    public void setDisabled(boolean disabled)
    {
        this.disabled = disabled;
    }

    public IPropertySelectionModel getModel()
    {
        return model;
    }

    public void setModel(IPropertySelectionModel model)
    {
        this.model = model;
    }

    public IPropertySelectionRenderer getRenderer()
    {
        return renderer;
    }

    public void setRenderer(IPropertySelectionRenderer renderer)
    {
        this.renderer = renderer;
    }

}