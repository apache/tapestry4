package com.primix.tapestry.components.validating;

import com.primix.tapestry.*;
import com.primix.tapestry.components.*;
import java.util.*;
import java.text.*;

/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 * 
 *  Implements a special text field used for enterring dates.
 *
 * <table border=1>
 * <tr> 
 *    <td>Parameter</td>
 *    <td>Type</td>
 *	  <td>Read / Write </td>
 *    <td>Required</td> 
 *    <td>Default</td>
 *    <td>Description</td>
 * </tr>
 *  
 *  <tr>
 *      <td>date</td>
 *      <td>java.util.Date</td>
 *      <td>R / W</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The date property to edit.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>required</td>
 *      <td>boolean</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>no</td>
 *      <td>If true, then a value must be entered.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>minimum</td>
 *      <td>java.util.Date</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>&nbsp;</td>
 *      <td>If provided, the date entered must be equal to or later than the
 *  provided minimum date.</td>
 *  </tr>
 *
 *  <tr>
 *      <td>maximum</td>
 *      <td>java.util.Date</td>
 *      <td>R</td>
 *      <td>no</td>
 *      <td>If provided, the date entered must be less than or equal to the
 *  provided maximum date.</td>
 * </tr>
 *
 *  <tr>
 *      <td>displayName</td>
 *      <td>String</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>A textual name for the field that is used when formulating error messages.
 *      </td>
 *  </tr>
 *
 *  <tr>
 *      <td>delegate</td>
 *      <td>{@link IValidationDelegate}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>Object used to assist in error reporting.</td>
 *  </tr>
 *
 *  <tr>
 * 		<td>disabled</td>
 *		<td>boolean</td>
 *		<td>R</td>
 *		<td>no</td>
 *		<td>false</td>
 *		<td>Applied to the underlying {@link TextField}.</td>
 *	</tr>
 *
 *  </table>
 *
 *  <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class DateField
extends AbstractValidatingTextField
{
    private IBinding dateBinding;
    private IBinding minimumBinding;
    private IBinding maximumBinding;

    // This is shared by all instances.

    private static DateFormat format;
    private Calendar calendar;

    public IBinding getDateBinding()
    {
        return dateBinding;
    }

    public void setDateBinding(IBinding value)
    {
        dateBinding = value;
    }

    public IBinding getMinimumBinding()
    {
        return minimumBinding;
    }

    public void setMinimumBinding(IBinding value)
    {
        minimumBinding = value;
    }

    public IBinding getMaximumBinding()
    {
        return maximumBinding;
    }

    public void setMaximumBinding(IBinding value)
    {
        maximumBinding = value;
    }


    protected String read()
    {
        Date date;

        date = (Date)dateBinding.getValue();
        if (date == null)
            return "";

        return getFormat().format(date);
    }

    /**
     *  Returns the {@link DateFormat} used to render and parse dates.  This is <em>not</em>
     *  locale specific (yet), it is always MM/dd/yyyy.
     *
     */

    public DateFormat getFormat()
    {
        if (format == null)
        {
            format = new SimpleDateFormat("MM/dd/yyyy");
            format.setLenient(false);
        }

        return format;
    }

    protected void update(String value)
    {
        Date date;                   
        Date boundary;
        String errorMessage;
        DateFormat format;

        // The value is already trimmed.  Is it null?

        if (value.length() == 0)
        {
            if (isRequired())
            {
                errorMessage = getString("field-is-required", getDisplayName());

                notifyDelegate(ValidationConstraint.REQUIRED, 
                    errorMessage);
            }

            dateBinding.setValue(null);

            return;
        }

        format = getFormat();  

        try
        {
            date = format.parse(value);

            if (calendar == null)
                calendar = new GregorianCalendar();

            calendar.setTime(date);

            // SimpleDateFormat allows two-digit dates to be
            // entered, i.e., 12/24/66 is Dec 24 0066 ... that's
            // probably not what is really wanted, so treat
            // it as an invalid date.

            if (calendar.get(Calendar.YEAR) < 1000)
                date = null;

        }
        catch (ParseException e)
        {
            date = null;
        }

        if (date == null)
        {
            errorMessage = getString("invalid-date-format", getDisplayName());

            notifyDelegate(ValidationConstraint.DATE_FORMAT,  errorMessage);
            return;
        }

        // OK, check that the date is in range.

        if (minimumBinding != null)
        {
            boundary = (Date)minimumBinding.getValue();

            if (boundary != null &&
                boundary.compareTo(date) > 0)
            {
                errorMessage = getString("date-too-early",
                    getDisplayName(),
                    format.format(boundary));

                notifyDelegate(ValidationConstraint.TOO_SMALL,
                    errorMessage);

                return;
            }
        }

        if (maximumBinding != null)
        {
            boundary = (Date)maximumBinding.getValue();

            if (boundary != null &&
                boundary.compareTo(date) < 0)
            {
                errorMessage = getString("date-too-late",
                    getDisplayName(),
                    format.format(boundary));

                notifyDelegate(ValidationConstraint.TOO_LARGE, 
                    errorMessage);

                return;
            }
        }

        // OK ... finally, everything is OK.

        dateBinding.setValue(date);

    }

}











