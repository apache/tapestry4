/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.valid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.RequestCycleException;
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
        {
            String errorMessage =
                getString(
                    "invalid-date-format",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    getEffectiveDisplayFormat());

            throw new ValidatorException(errorMessage, ValidationConstraint.DATE_FORMAT);
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

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_SMALL);
        }

        if (_maximum != null && _maximum.compareTo(result) < 0)
        {
            String errorMessage =
                getString(
                    "date-too-late",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    format.format(_maximum));

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE);
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

    public void renderValidatorContribution(
        IFormComponent field,
        IMarkupWriter writer,
        IRequestCycle cycle)
        throws RequestCycleException
    {
        if (!(isClientScriptingEnabled() && isRequired()))
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