package net.sf.tapestry.form;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;

/**
 * Provides a Form <tt>java.sql.Date</tt> field component for selecting dates.
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
    private static final SimpleDateFormat SQL_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private IBinding _valueBinding;
    private SimpleDateFormat _dateFormat = new SimpleDateFormat("dd MMM yyyy");

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
                java.util.Date date = _dateFormat.parse(text);

                // DateFormat is not threadsafe

                synchronized (SQL_DATE_FORMAT)
                {
                    setValue(Date.valueOf(SQL_DATE_FORMAT.format(date)));
                }
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
}
