package net.sf.tapestry.form;

import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Implements a component that manages an HTML &lt;input type=checkbox&gt;
 *  form element.
 *
 *  [<a href="../../../../../ComponentReference/Checkbox.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Checkbox extends AbstractFormComponent
{
    private boolean _disabled;

    /**  @since 2.2 **/
    private boolean _selected;

    private String _name;

    public String getName()
    {
        return _name;
    }

    /**
     *  Renders the form elements, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     *  <p>In traditional HTML, many checkboxes would have the same name but different values.
     *  Under Tapestry, it makes more sense to have different names and a fixed value.
     *  For a checkbox, we only care about whether the name appears as a request parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        if (form.isRewinding())
        {
            String value = cycle.getRequestContext().getParameter(_name);

            _selected = (value != null);

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "checkbox");

        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        if (_selected)
            writer.attribute("checked");

        generateAttributes(writer, cycle);

        writer.closeTag();
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    /** @since 2.2 **/

    public boolean isSelected()
    {
        return _selected;
    }

    /** @since 2.2 **/

    public void setSelected(boolean selected)
    {
        _selected = selected;
    }

}