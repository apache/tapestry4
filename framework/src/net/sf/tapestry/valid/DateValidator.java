//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.valid;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    private DateFormat format;
    private Date minimum;
    private Date maximum;
    private Calendar calendar;

    private static DateFormat defaultDateFormat = new SimpleDateFormat("MM/dd/yyyy");

    public void setFormat(DateFormat value)
    {
        format = value;
    }

    public DateFormat getFormat()
    {
        return format;
    }

    private DateFormat getEffectiveFormat()
    {
        if (format == null)
            return defaultDateFormat;

        return format;
    }

    public String toString(IField file, Object value)
    {
        if (value == null)
            return null;

        Date date = (Date) value;

        return getEffectiveFormat().format(date);
    }

    public Object toObject(IField field, String value) throws ValidatorException
    {
        if (checkRequired(field, value))
            return null;

        DateFormat format = getEffectiveFormat();

        Date result;

        try
        {
            result = format.parse(value);

            if (calendar == null)
                calendar = new GregorianCalendar();

            calendar.setTime(result);

            // SimpleDateFormat allows two-digit dates to be
            // entered, i.e., 12/24/66 is Dec 24 0066 ... that's
            // probably not what is really wanted, so treat
            // it as an invalid date.

            if (calendar.get(Calendar.YEAR) < 1000)
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
            String errorMessage = getString("invalid-date-format", field.getPage().getLocale(), field.getDisplayName());

            throw new ValidatorException(errorMessage, ValidationConstraint.DATE_FORMAT, value);
        }

        // OK, check that the date is in range.

        if (minimum != null && minimum.compareTo(result) > 0)
        {
            String errorMessage =
                getString(
                    "date-too-early",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    format.format(minimum));

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_SMALL, value);
        }

        if (maximum != null && maximum.compareTo(result) < 0)
        {
            String errorMessage =
                getString(
                    "date-too-late",
                    field.getPage().getLocale(),
                    field.getDisplayName(),
                    format.format(maximum));

            throw new ValidatorException(errorMessage, ValidationConstraint.TOO_LARGE, value);
        }

        return result;

    }

    public Date getMaximum()
    {
        return maximum;
    }

    public void setMaximum(Date maximum)
    {
        this.maximum = maximum;
    }

    public Date getMinimum()
    {
        return minimum;
    }

    public void setMinimum(Date minimum)
    {
        this.minimum = minimum;
    }

}