package net.sf.tapestry.form;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;

/**
 * Provides a Form <tt>java.util.Date</tt> field component for selecting dates.
 *
 *  [<a href="../../../../../ComponentReference/DatePicker.html">Component Reference</a>]
 *
 * @author Paul Geerts
 * @author Malcolm Edgar
 * @version $Id$
 * @since 2.2
 * 
 **/

public class DatePicker extends BaseComponent
{
    private IBinding _valueBinding;
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM yyyy");
    private boolean _disabled;

    public String getFormat()
    {
        return _dateFormat.toPattern();
    }

    public void setFormat(String format)
    {
        _dateFormat = new SimpleDateFormat(format);
    }

    public String getTimeMillis()
    {
        Date date = getValue();

        if (date == null)
            return "";

        return Long.toString(date.getTime());
    }

    public void setTimeMillis(String value)
    {
    }

    public String getText()
    {
        Date date = getValue();

        if (date == null)
            return "";

        return _dateFormat.format(date);
    }

    public void setText(String text)
    {
        if (text.length() >= 6)
        {
            try
            {
                setValue(_dateFormat.parse(text));
            }
            catch (ParseException pe)
            {
                setValue(null);
            }
        }
        else
        {
            setValue(null);
        }
    }

    public Date getValue()
    {
        return (Date) _valueBinding.getObject();
    }

    public void setValue(Date value)
    {
        _valueBinding.setObject(value);
    }

    public IBinding getValueBinding()
    {
        return _valueBinding;
    }

    public void setValueBinding(IBinding value)
    {
        _valueBinding = value;
    }

    public boolean isDisabled()
    {
        return _disabled;
    }

    public void setDisabled(boolean disabled)
    {
        _disabled = disabled;
    }        
}
