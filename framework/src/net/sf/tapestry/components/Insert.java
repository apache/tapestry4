package net.sf.tapestry.components;

import java.text.Format;

import net.sf.tapestry.AbstractComponent;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.Tapestry;

/**
 *  Used to insert some text (from a parameter) into the HTML.
 *
 *  [<a href="../../../../../ComponentReference/Insert.html">Component Reference</a>]
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public class Insert extends AbstractComponent
{
    private Object _value;
    private Format _format;

    // The class parameter is connected to the styleClass property
    private String _styleClass;
    private boolean _raw;

    /**
     *  Prints its value parameter, possibly formatted by its format parameter.
     *
     **/

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        if (cycle.isRewinding())
            return;

        if (_value == null)
            return;

        String insert = null;

        if (_format == null)
        {
            insert = _value.toString();
        }
        else
        {
            try
            {
                insert = _format.format(_value);
            }
            catch (Exception ex)
            {
                throw new RequestCycleException(Tapestry.getString("Insert.unable-to-format", _value), this, ex);
            }
        }

        if (_styleClass != null)
        {
            writer.begin("span");
            writer.attribute("class", _styleClass);
        }

        if (_raw)
            writer.printRaw(insert);
        else
            writer.print(insert);

        if (_styleClass != null)
            writer.end(); // <span>
    }

    public Object getValue()
    {
        return _value;
    }

    public void setValue(Object value)
    {
        _value = value;
    }

    public Format getFormat()
    {
        return _format;
    }

    public void setFormat(Format format)
    {
        _format = format;
    }

    public String getStyleClass()
    {
        return _styleClass;
    }

    public void setStyleClass(String styleClass)
    {
        _styleClass = styleClass;
    }

    public boolean getRaw()
    {
        return _raw;
    }

    public void setRaw(boolean raw)
    {
        _raw = raw;
    }

}