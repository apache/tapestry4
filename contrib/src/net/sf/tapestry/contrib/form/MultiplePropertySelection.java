package net.sf.tapestry.contrib.form;

import java.util.List;

import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;
import net.sf.tapestry.form.AbstractFormComponent;
import net.sf.tapestry.form.IPropertySelectionModel;

/**
 *  A component which uses &lt;input type=checkbox&gt; to
 *  set a property of some object.  Typically, the values for the object
 *  are defined using an {@link net.sf.tapestry.util.Enum}.  A MultiplePropertySelection is dependent on
 *  an {link IPropertySelectionModel} to provide the list of possible values.
 *
 *  <p>Often, this is used to select one or more {@link net.sf.tapestry.util.Enum} to assign to a property; the
 * {@link net.sf.tapestry.form.EnumPropertySelectionModel} class simplifies this.
 * 
 *  <p>The {@link net.sf.tapestry.contrib.palette.Palette} component
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
        throws RequestCycleException
    {
        IForm form = getForm(cycle);
        boolean rewinding = form.isRewinding();

        name = form.getElementId(this);

        List selectedList =
            (List) selectedListBinding.getObject("selectedList", List.class);

        if (selectedList == null)
            throw new RequiredParameterException(this, "selectedList", selectedListBinding);

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