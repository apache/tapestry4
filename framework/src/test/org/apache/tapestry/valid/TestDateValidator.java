// Copyright 2004, 2005 The Apache Software Foundation
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.DateValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests the {@link DateValidator}class.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 */

public class TestDateValidator extends BaseValidatorTestCase
{
    private Calendar calendar = new GregorianCalendar();

    private DateValidator v = new DateValidator();

    private Date buildDate(int month, int day, int year)
    {
        calendar.clear();

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.YEAR, year);

        return calendar.getTime();
    }

    public void testToStringNull()
    {
        String out = v.toString(null, null);

        assertNull(out);
    }

    public void testToStringValid()
    {
        String out = v.toString(null, buildDate(Calendar.DECEMBER, 8, 2001));

        assertEquals("Result.", "12/08/2001", out);
    }

    public void testToStringFormat()
    {
        if (IS_JDK13)
            return;

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);

        v.setFormat(format);

        String out = v.toString(null, buildDate(Calendar.DECEMBER, 8, 2001));

        assertEquals("Result.", "08.12.01", out);
    }

    public void testToObjectNull() throws ValidatorException
    {
        IFormComponent field = newField();

        replayControls();

        Object out = v.toObject(field, null);

        assertNull(out);

        verifyControls();
    }

    public void testToObjectEmpty() throws ValidatorException
    {
        IFormComponent field = newField();

        replayControls();

        Object out = v.toObject(field, "");

        assertNull(out);

        verifyControls();
    }

    public void testToObjectInvalid()
    {
        IFormComponent field = newField("badDatesIndy");

        replayControls();

        try
        {
            v.toObject(field, "frankenhooker");

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Invalid date format for badDatesIndy.  Format is MM/dd/yyyy.", ex
                    .getMessage());
            assertEquals(ValidationConstraint.DATE_FORMAT, ex.getConstraint());
        }

        verifyControls();
    }

    public void testOverrideInvalidDateFormatMessage()
    {

        IFormComponent field = newField("badDatesIndy");

        replayControls();

        v.setInvalidDateFormatMessage("Enter a valid date for {0}.");

        try
        {
            v.toObject(field, "frankenhooker");

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Enter a valid date for badDatesIndy.", ex.getMessage());

        }

        verifyControls();
    }

    public void testToObjectFormat() throws ValidatorException
    {
        if (IS_JDK13)
            return;

        DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT, Locale.GERMAN);

        v.setFormat(format);

        // Again, adjust for missing German localization in JDK 1.3

        Object out = v.toObject(null, "08.12.01");

        assertEquals("Result.", buildDate(Calendar.DECEMBER, 8, 2001), out);
    }

    public void testToObjectMinimum()
    {
        IFormComponent field = newField("toObjectMinimum", Locale.ENGLISH);

        replayControls();

        v.setMinimum(buildDate(Calendar.DECEMBER, 24, 2001));

        try
        {
            v.toObject(field, "12/8/2001");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }

        verifyControls();
    }

    public void testOverrideDateTooEarlyMessage()
    {
        IFormComponent field = newField("inputDate", Locale.ENGLISH);

        replayControls();

        v.setMinimum(buildDate(Calendar.DECEMBER, 24, 2001));
        v.setDateTooEarlyMessage("Provide a date for {0} after Dec 24 2001.");

        try
        {
            v.toObject(field, "12/8/2001");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Provide a date for inputDate after Dec 24 2001.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }

        verifyControls();
    }

    public void testToObjectMinimumNull() throws ValidatorException
    {
        v.setMinimum(buildDate(Calendar.DECEMBER, 24, 2001));

        Object out = v.toObject(null, null);

        assertNull(out);
    }

    public void testToObjectMaximum()
    {
        IFormComponent field = newField("toObjectMaximum");

        replayControls();

        v.setMaximum(buildDate(Calendar.DECEMBER, 24, 2001));

        try
        {
            v.toObject(field, "12/8/2002");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("toObjectMaximum must be on or before 12/24/2001.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }

        verifyControls();
    }

    public void testOverrideDateTooLateMessage()
    {
        IFormComponent field = newField("toObjectMaximum");

        replayControls();

        v.setMaximum(buildDate(Calendar.DECEMBER, 24, 2001));
        v.setDateTooLateMessage("Try again with a date before Dec 24 2001 in {0}.");

        try
        {
            v.toObject(field, "12/8/2002");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Try again with a date before Dec 24 2001 in toObjectMaximum.", ex
                    .getMessage());
        }

        verifyControls();
    }

    public void testToObjectMaximumNull() throws ValidatorException
    {
        v.setMaximum(buildDate(Calendar.DECEMBER, 24, 2001));

        Object out = v.toObject(null, null);

        assertNull(out);
    }
}