package net.sf.tapestry.form;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Implements a component that manages an HTML &lt;input type=radio&gt; form element.
 *  Such a component must be wrapped (possibly indirectly)
 *  inside a {@link RadioGroup} component.
 *
 *  [<a href="../../../../../ComponentReference/Radio.html">Component Reference</a>]
 *
 * 
 *  <p>{@link Radio} and {@link RadioGroup} are generally not used (except
 *  for very special cases).  Instead, a {@link PropertySelection} component is used.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Radio extends AbstractComponent
{
 	private Object _value;
 	private boolean _disabled;
 	
    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {

        RadioGroup group = RadioGroup.get(cycle);
        if (group == null)
            throw new RequestCycleException(Tapestry.getString("Radio.must-be-contained-by-group"), this);

        // The group determines rewinding from the form.

        boolean rewinding = group.isRewinding();

        int option = group.getNextOptionId();

        if (rewinding)
        {
            // If not disabled and this is the selected button within the radio group,
            // then update set the selection from the group to the value for this
            // radio button.  This will update the selected parameter of the RadioGroup.

            if (!_disabled && group.isSelected(option))
                group.updateSelection(_value);
            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", "radio");

        writer.attribute("name", group.getName());

        // As the group if the value for this Radio matches the selection
        // for the group as a whole; if so this is the default radio and is checked.

        if (group.isSelection(_value))
            writer.attribute("checked");

        if (_disabled || group.isDisabled())
            writer.attribute("disabled");

        // The value for the Radio matches the option number (provided by the RadioGroup).
        // When the form is submitted, the RadioGroup will know which option was,
        // in fact, selected by the user.

        writer.attribute("value", option);

        generateAttributes(writer, cycle);

    }

    public boolean getDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

}