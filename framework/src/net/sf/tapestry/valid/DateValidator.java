package net.sf.tapestry.valid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;

/**
 *  Provides input validation for strings treated as dates.  In addition,
 *  allows a minimum and maximum date to be set.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class DateValidator extends BaseValidator
{
    private DateFormat _format;
    private String _displayFormat;
    private Date _minimum;
    private Date _maximum;
    private Calendar _calendar;
    private String _scriptPath = "/net/sf/tapestry/valid/DateValidator.script";

    private static DateFormat defaultDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static String defaultDateDisplayFormat = "MM/DD/YYYY";

    public void setFormat(DateFormat value)
    {
        _format = value;
    }

    public DateFormat getFormat()
    {
        return _format;
    }

    private DateFormat getEffectiveFormat()
    {
        if (_format == null)
            return defaultDateFormat;

        return _format;
    }

    public String getDisplayFormat()
    {
        return _displayFormat;
    }

    public void setDisplayFormat(String value)
    {
        _displayFormat = value;
    }

    private String getEffectiveDisplayFormat()
    {
        if (_displayFormat == null)
            return defaultDateDisplayFormat;

        return _displayFormat;
    }

    public String toString(IField file, Object value)
    {
        if (value == null)
            return null;

        Date date = (Date) value;

        DateFormat format = getEffectiveFormat();

        // DateFormat is not threadsafe, so guard access to it.

        synchronized(format)
        {
            return format.format(date);
        }
    }

    public Object toObject(IField field, String value) throws ValidatorException
    {
        if (checkRequired(field, value))
            return null;

        DateFormat format = getEffectiveFormat();

        Date result;

        try
        {
            // DateFormat is not threadsafe, so guard access
            // to it.

            synchronized (format)
            {
                result = format.parse(value);
            }

            if (_calendar == null)
                _calendar = new GregorianCalendar();

            _calendar.setTime(result);

            // SimpleDateFormat allows two-digit dates to be
            // entered, i.e., 12/24/66 is Dec 24 0066 ... that's
            // probably not what is really wanted, so treat
            // it as an invalid date.

            if (_calendar.get(Calendar.YEAR) < 1000)
                result = null;

        }
        catch (ParseException ex)
        {
            // ParseException does not include a useful error message
            // about what's wrong.
            result = null;
        }

        if (result == null)
        {
            String errorMessage =
                getString(
                    "invalid-date-format",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    getEffectiveDisplayFormat());

            throw new ValidatorException(errorMessage, ValidationConstraint.DATE_FORMAT, value);
        }

        // OK, check that the date is in range.

        if (_minimum != null && _minimum.compareTo(result) > 0)
        {
            String errorMessage =
                getString(
                    "date-too-early",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    format.format(_minimum));

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_SMALL, value);
        }

        if (_maximum != null && _maximum.compareTo(result) < 0)
        {
            String errorMessage =
                getString(
                    "date-too-late",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    format.format(_maximum));

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE, value);
        }

        return result;

    }

    public Date getMaximum()
    {
        return _maximum;
    }

    public void setMaximum(Date maximum)
    {
        _maximum = maximum;
    }

    public Date getMinimum()
    {
        return _minimum;
    }

    public void setMinimum(Date minimum)
    {
        _minimum = minimum;
    }

    /** 
     * 
     *  @since 2.2
     * 
     **/

    public void renderValidatorContribution(IField field, IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
    {
        if (! (isClientScriptingEnabled() && isRequired()))
            return;

        Map symbols = new HashMap();

        Locale locale = field.getPage().getLocale();
        String displayName = field.getDisplayName();

        symbols.put("requiredMessage", getString("field-is-required", locale, displayName));

        processValidatorScript(_scriptPath, cycle, field, symbols);
    }

    /**
     *  @since 2.2
     * 
     **/

    public String getScriptPath()
    {
        return _scriptPath;
    }

    /**
     *  Allows a developer to use the existing validation logic with a different client-side
     *  script.  This is often sufficient to allow application-specific error presentation
     *  (perhaps by using DHTML to update the content of a &lt;span&gt; tag, or to use
     *  a more sophisticated pop-up window than <code>window.alert()</code>).
     * 
     *  @since 2.2
     * 
     **/

    public void setScriptPath(String scriptPath)
    {
        _scriptPath = scriptPath;
    }

}