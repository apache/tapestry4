// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.form.translator;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Test case for {@link DateTranslator}.
 * 
 * @author Paul Ferraro
 * @since 4.0
 */
public class TestDateTranslator extends TranslatorTestCase
{
    private Calendar _calendar = Calendar.getInstance();

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        _calendar.clear();
    }

    private Date buildDate(int year, int month, int day)
    {
        _calendar.set(Calendar.YEAR, year);
        _calendar.set(Calendar.MONTH, month);
        _calendar.set(Calendar.DATE, day);

        return _calendar.getTime();
    }

    public void testDefaultFormat()
    {
        DateTranslator translator = new DateTranslator();
        testFormat(translator, buildDate(1976, Calendar.OCTOBER, 29), "10/29/1976");
    }

    public void testCustomFormat()
    {
        DateTranslator translator = new DateTranslator();

        translator.setPattern("yyyy-MM-dd");

        testFormat(translator, buildDate(1976, Calendar.OCTOBER, 29), "1976-10-29");
    }

    public void testInitializerFormat()
    {
        DateTranslator translator = new DateTranslator("pattern=yyyy-MM-dd");

        testFormat(translator, buildDate(1976, Calendar.OCTOBER, 29), "1976-10-29");
    }

    public void testFormat(DateTranslator translator, Date date, String expected)
    {
        IFormComponent field = newField();

        replayControls();

        String result = translator.format(field, Locale.ENGLISH, date);

        assertEquals(expected, result);

        verifyControls();
    }

    public void testNullFormat()
    {
        DateTranslator translator = new DateTranslator();

        replay();

        String result = translator.format(_component, null, null);

        assertEquals("", result);

        verify();
    }

    public void testDefaultParse() throws Exception
    {
        DateTranslator translator = new DateTranslator();

        testParse(translator, "10/29/1976", buildDate(1976, Calendar.OCTOBER, 29));
    }

    public void testCustomParse() throws Exception
    {
        DateTranslator translator = new DateTranslator();

        translator.setPattern("yyyy-MM-dd");

        testParse(translator, "1976-10-29", buildDate(1976, Calendar.OCTOBER, 29));
    }

    public void testTrimmedParse() throws Exception
    {
        DateTranslator translator = new DateTranslator();

        translator.setTrim(true);

        testParse(translator, " 10/29/1976 ", buildDate(1976, Calendar.OCTOBER, 29));
    }

    public void testEmptyParse()
    {
        DateTranslator translator = new DateTranslator();

        replay();

        try
        {
            Date result = (Date) translator.parse(_component, null, "");

            assertEquals(null, result);
        }
        catch (ValidatorException e)
        {
            unreachable();
        }
        finally
        {
            verify();
        }
    }

    private void testParse(DateTranslator translator, String date, Date expected) throws Exception
    {
        IFormComponent field = newField();

        ValidationMessages messages = newValidationMessages(Locale.ENGLISH);

        replayControls();

        Date result = (Date) translator.parse(field, messages, date);

        assertEquals(expected, result);

        verifyControls();
    }

    public void testFailedParseDefaultMessage() throws Exception
    {
        DateTranslator translator = new DateTranslator();

        testFailedParse(translator, null);
    }

    public void testFailedParseCustomMessage() throws Exception
    {
        DateTranslator translator = new DateTranslator();
        String message = "Field Name is an invalid date.";

        translator.setMessage(message);

        testFailedParse(translator, message);
    }

    private void testFailedParse(DateTranslator translator, String overrideMessage)
            throws Exception
    {
        IFormComponent field = newField("My Field");

        MockControl messagesc = newControl(ValidationMessages.class);
        ValidationMessages messages = (ValidationMessages) messagesc.getMock();

        trainGetLocale(messagesc, messages, Locale.ENGLISH);
        trainGetLocale(messagesc, messages, Locale.ENGLISH);

        trainBuildMessage(
                messagesc,
                messages,
                overrideMessage,
                ValidationStrings.INVALID_DATE,
                new Object[]
                { "My Field", "MM/DD/YYYY" },
                "final message");

        replayControls();

        try
        {
            System.out.println(translator.parse(field, messages, "Bad-Date"));

            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals("final message", e.getMessage());
            assertEquals(ValidationConstraint.DATE_FORMAT, e.getConstraint());
        }

        verifyControls();
    }

    public void testRenderContribution()
    {
        DateTranslator translator = new DateTranslator();

        replay();

        translator.renderContribution(null, _cycle, null, _component);

        verify();
    }

    public void testTrimRenderContribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        IFormComponent field = newFieldWithClientId("foo");

        FormComponentContributorContext context = (FormComponentContributorContext) newMock(FormComponentContributorContext.class);

        trainTrim(context, "foo");

        replayControls();

        DateTranslator dt = new DateTranslator();
        dt.setTrim(true);

        dt.renderContribution(writer, cycle, context, field);

        verifyControls();

    }
}
