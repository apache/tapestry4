package net.sf.tapestry.form;

import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Base class for implementing various types of text input fields.
 *  This includes {@link TextField} and
 *  {@link net.sf.tapestry.valid.ValidField}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.2
 * 
 **/

public abstract class AbstractTextField extends AbstractFormComponent
{
    private int _displayWidth;
    private int _maximumLength;
    private boolean _hidden;
    private boolean _disabled;

    private String _name;

    public String getName()
    {
        return _name;
    }

    /**
     *  Renders the form element, or responds when the form containing the element
     *  is submitted (by checking {@link Form#isRewinding()}.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        String value;

        IForm form = getForm(cycle);

        // It isn't enough to know whether the cycle in general is rewinding, need to know
        // specifically if the form which contains this component is rewinding.

        boolean rewinding = form.isRewinding();

        // If the cycle is rewinding, but the form containing this field is not,
        // then there's no point in doing more work.

        if (!rewinding && cycle.isRewinding())
            return;

        // Used whether rewinding or not.

        _name = form.getElementId(this);

        if (rewinding)
        {
            if (!_disabled)
            {
                value = cycle.getRequestContext().getParameter(_name);

                updateValue(value);
            }

            return;
        }

        writer.beginEmpty("input");

        writer.attribute("type", _hidden ? "password" : "text");

        if (_disabled)
            writer.attribute("disabled");

        writer.attribute("name", _name);

        if (_displayWidth != 0)
            writer.attribute("size", _displayWidth);

        if (_maximumLength != 0)
            writer.attribute("maxlength", _maximumLength);

        value = readValue();
        if (value != null)
            writer.attribute("value", value);

        generateAttributes(writer, cycle);

        beforeCloseTag(writer, cycle);

        writer.closeTag();
    }

    /**
     *  Invoked from {@link #render(IMarkupWriter, IRequestCycle)}
     *  just before the tag is closed.  This implementation does nothing,
     *  subclasses may override.
     *
     **/

    protected void beforeCloseTag(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        // Do nothing.
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when a value is obtained from the
     *  {@link javax.servlet.http.HttpServletRequest}.
     *
     **/

    abstract protected void updateValue(String value) throws RequestCycleException;

    /**
     *  Invoked by {@link #render(IMarkupWriter writer, IRequestCycle cycle)}
     *  when rendering a response.
     *
     *  @return the current value for the field, as a String, or null.
     **/

    abstract protected String readValue() throws RequestCycleException;

    public boolean getHidden()
    
    {
        return _hidden;
    }

    public void setHidden(boolean hidden)
    {
        _hidden = hidden;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }

    public int getDisplayWidth()
    {
        return _displayWidth;
    }

    public void setDisplayWidth(int displayWidth)
    {
        _displayWidth = displayWidth;
    }

    public int getMaximumLength()
    {
        return _maximumLength;
    }

    public void setMaximumLength(int maximumLength)
    {
        _maximumLength = maximumLength;
    }

}