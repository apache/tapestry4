package net.sf.tapestry.form;

import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.RequiredParameterException;

/**
 *  Implements a component that manages an HTML &lt;input type=submit&gt; form element.
 * 
 *  [<a href="../../../../../ComponentReference/Submit.html">Component Reference</a>]
 *
 *  <p>This component is generally only used when the form has multiple
 *  submit buttons, and it is important for the application to know
 *  which one was pressed.  You may also want to use
 *  {@link ImageSubmit} which accomplishes much the same thing, but uses
 *  a graphic image instead.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Submit extends AbstractFormComponent
{
    private String _label;
    private IActionListener _listener;
    private boolean _disabled;
    private Object _tag;
    
    /**
     *  Can't use a "form" direction parameter, because
     *  the binding must be updated before
     *  the listener is invoked.
     * 
     **/
    
    private IBinding _selectedBinding;

    private String _name;

    public String getName()
    {
        return _name;
    }

    public void setSelectedBinding(IBinding value)
    {
        _selectedBinding = value;
    }

    public IBinding getSelectedBinding()
    {
        return _selectedBinding;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {

        IForm form = getForm(cycle);

        boolean rewinding = form.isRewinding();

        _name = form.getElementId(this);

        if (rewinding)
        {
            // Don't bother doing anything if disabled.

            if (_disabled)
                return;

            // How to know which Submit button was actually
            // clicked?  When submitted, it produces a request parameter
            // with its name and value (the value serves double duty as both
            // the label on the button, and the parameter value).

            String value = cycle.getRequestContext().getParameter(_name);

            // If the value isn't there, then this button wasn't
            // selected.

            if (value == null)
                return;

            if (_selectedBinding != null)
                _selectedBinding.setObject(_tag);

            if (_listener != null)
                _listener.actionTriggered(this, cycle);

            return;
        }

        writer.beginEmpty("input");
        writer.attribute("type", "submit");
        writer.attribute("name", _name);

        if (_disabled)
            writer.attribute("disabled");

        if (_label != null)
            writer.attribute("value", _label);

        generateAttributes(writer, cycle);

        writer.closeTag();
    }

    public String getLabel()
    {
        return _label;
    }

    public void setLabel(String label)
    {
        _label = label;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public IActionListener getListener()
    {
        return _listener;
    }

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

    public Object getTag()
    {
        return _tag;
    }

    public void setTag(Object tag)
    {
        _tag = tag;
    }

}