package net.sf.tapestry.form;

import java.io.IOException;

import net.sf.tapestry.BindingException;
import net.sf.tapestry.IActionListener;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IForm;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.util.io.DataSqueezer;

/**
 *  Implements a hidden field within a {@link Form}.
 *
 *  [<a href="../../../../../ComponentReference/Hidden.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Hidden extends AbstractFormComponent
{
    private IBinding _valueBinding;
    private IActionListener _listener;
    private String _name;

    /**
     *  If true, the value is encoded using a DataSqueezer.  If false,
     *  the value must be a String and is not encoded.
     * 
     *  @since 2.2
     * 
     **/

    private boolean _encode = true;

    public String getName()
    {
        return _name;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        IForm form = getForm(cycle);
        boolean formRewound = form.isRewinding();

        _name = form.getElementId(this);

        // If the form containing the Hidden isn't rewound, then render.

        if (!formRewound)
        {
            // Optimiziation: if the page is rewinding (some other action or
            // form was submitted), then don't bother rendering.

            if (cycle.isRewinding())
                return;

            String externalValue = null;

            if (_encode)
            {
                Object value = _valueBinding.getObject();

                try
                {
                    externalValue = getDataSqueezer().squeeze(value);
                }
                catch (IOException ex)
                {
                    throw new RequestCycleException(this, ex);
                }
            }
            else
                externalValue = (String) _valueBinding.getObject("value", String.class);

            writer.beginEmpty("input");
            writer.attribute("type", "hidden");
            writer.attribute("name", _name);
            writer.attribute("value", externalValue);

            return;
        }

        String externalValue = cycle.getRequestContext().getParameter(_name);
        Object value = null;

        if (_encode)
        {
            try
            {
                value = getDataSqueezer().unsqueeze(externalValue);
            }
            catch (IOException ex)
            {
                throw new RequestCycleException(this, ex);
            }
        }
        else
            value = externalValue;

        // A listener is not always necessary ... it's easy to code
        // the synchronization as a side-effect of the accessor method.

        _valueBinding.setObject(value);

        if (_listener != null)
            _listener.actionTriggered(this, cycle);
    }

    /** @since 2.2 **/

    private DataSqueezer getDataSqueezer()
    {
        return getPage().getEngine().getDataSqueezer();
    }

    public IActionListener getListener()
    {
        return _listener;
    }

    public void setListener(IActionListener listener)
    {
        _listener = listener;
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding valueBinding)
    {
        _valueBinding = valueBinding;
    }

    /**
     * 
     *  Returns false.  Hidden components are never disabled.
     * 
     *  @since 2.2
     * 
     **/

    public boolean isDisabled()
    {
        return false;
    }

    /** 
     * 
     *  Returns true if the compent encodes object values using a
     *  {@link net.sf.tapestry.util.io.DataSqueezer}, false
     *  if values are always Strings.
     * 
     *  @since 2.2
     * 
     **/

    public boolean getEncode()
    {
        return _encode;
    }

    /** @since 2.2 **/

    public void setEncode(boolean encode)
    {
        _encode = encode;
    }

}