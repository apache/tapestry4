//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.valid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.IFormComponent;

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
    private String _scriptPath = "/org/apache/tapestry/valid/DateValidator.script";

    private static DateFormat defaultDateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private static final String defaultDateDisplayFormat = "MM/DD/YYYY";

    private String _dateTooEarlyMessage;
    private String _dateTooLateMessage;
    private String _invalidDateFormatMessage;

    public void setFormat(DateFormat value)
    {
        _format = value;
    }

    public DateFormat getFormat()
    {
        return _format;
    }

	/**
	 * @return the {@link DateFormat} the validator will use, returning the default if no
	 * other date format is specified via {@link #setFormat(DateFormat)}
	 * 
	 * @since 3.0
	 */
    public DateFormat getEffectiveFormat()
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

	/**
     * @return the display format message the validator will use, returning the default if no
     * other display format message is specified.  The default is the {@link SimpleDateFormat#toPattern()}
     * for {@link SimpleDateFormat}s, or "MM/DD/YYYY" for unknown {@link DateFormat} subclasses.
     * 
     * @since 3.0
     */
    public String getEffectiveDisplayFormat()
    {
        if (_displayFormat == null)
        {
            DateFormat format = getEffectiveFormat();
            if (format instanceof SimpleDateFormat) 
                return ((SimpleDateFormat)format).toPattern();
            else
                return defaultDateDisplayFormat;
        }

        return _displayFormat;
    }

    public String toString(IFormComponent file, Object value)
    {
        if (value == null)
            return null;

        Date date = (Date) value;

        DateFormat format = getEffectiveFormat();

        // DateFormat is not threadsafe, so guard access to it.

        synchronized (format)
        {
            return format.format(date);
        }
    }

    public Object toObject(IFormComponent field, String value) throws ValidatorException
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
            throw new ValidatorException(
                buildInvalidDateFormatMessage(field),
                ValidationConstraint.DATE_FORMAT);

        // OK, check that the date is in range.

        if (_minimum != null && _minimum.compareTo(result) > 0)
            throw new ValidatorException(
                buildDateTooEarlyMessage(field, format.format(_minimum)),
                ValidationConstraint.TOO_SMALL);

        if (_maximum != null && _maximum.compareTo(result) < 0)
            throw new ValidatorException(
                buildDateTooLateMessage(field, format.format(_maximum)),
                ValidationConstraint.TOO_LARGE);

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

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
    {
        if (!(isClientScriptingEnabled() && isRequired()))
            return;

        Map symbols = new HashMap();

        symbols.put("requiredMessage", buildRequiredMessage(field));

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

    /** @since 3.0 */

    public String getDateTooEarlyMessage()
    {
        return _dateTooEarlyMessage;
    }

    /** @since 3.0 */

    public String getDateTooLateMessage()
    {
        return _dateTooLateMessage;
    }

    /** @since 3.0 */

    public String getInvalidDateFormatMessage()
    {
        return _invalidDateFormatMessage;
    }

    /** @since 3.0 */

    protected String buildInvalidDateFormatMessage(IFormComponent field)
    {
        String pattern =
            getPattern(
                _invalidDateFormatMessage,
                "invalid-date-format",
                field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName(), getEffectiveDisplayFormat());
    }

    /** @since 3.0 **/

    protected String buildDateTooEarlyMessage(IFormComponent field, String earliestDate)
    {
        String pattern =
            getPattern(_dateTooEarlyMessage, "date-too-early", field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName(), earliestDate);
    }

    /** @since 3.0 */

    protected String buildDateTooLateMessage(IFormComponent field, String latestDate)
    {
        String pattern =
            getPattern(_dateTooLateMessage, "date-too-late", field.getPage().getLocale());

        return formatString(pattern, field.getDisplayName(), latestDate);
    }

    /**
     *  Overrides the bundle key
     *  <code>date-too-early</code>.
     *  Parameter {0} is the display name of the field.
     *  Parameter {1} is the earliest allowed date.
     * 
     *  @since 3.0
     */

    public void setDateTooEarlyMessage(String string)
    {
        _dateTooEarlyMessage = string;
    }

    /**
     *  Overrides the bundle key
     *  <code>date-too-late</code>.
     *  Parameter {0} is the display name of the field.
     *  Parameter {1} is the latest allowed date.
     * 
     *  @since 3.0
     */

    public void setDateTooLateMessage(String string)
    {
        _dateTooLateMessage = string;
    }

    /**
     *  Overrides the bundle key
     *  <code>invalid-date-format</code>.
     *  Parameter {0} is the display name of the field.
     *  Parameter {1} is the allowed format.
     * 
     *  @since 3.0
     */

    public void setInvalidDateFormatMessage(String string)
    {
        _invalidDateFormatMessage = string;
    }

}