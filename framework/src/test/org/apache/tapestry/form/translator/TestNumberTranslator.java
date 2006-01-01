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

import java.util.Locale;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.translator.NumberTranslator}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestNumberTranslator extends FormComponentContributorTestCase
{
    public void testDefaultFormat()
    {
        NumberTranslator translator = new NumberTranslator();

        testFormat(translator, new Integer(10), "10");
    }

    public void testOmitZero()
    {
        NumberTranslator translator = new NumberTranslator("pattern=0.00");

        testFormat(translator, new Integer(0), "");
    }

    public void testOmitZeroOff()
    {
        NumberTranslator translator = new NumberTranslator("!omitZero,pattern=0.00");

        testFormat(translator, new Integer(0), "0.00");
    }

    public void testCustomFormat()
    {
        NumberTranslator translator = new NumberTranslator();

        translator.setPattern("$#0.00");

        testFormat(translator, new Integer(10), "$10.00");
    }

    public void testInitializerFormat()
    {
        NumberTranslator translator = new NumberTranslator("pattern=#0%");

        testFormat(translator, new Double(0.10), "10%");
    }

    public void testFormat(Translator translator, Number number, String expected)
    {
        IFormComponent field = newField();

        String result = translator.format(field, Locale.ENGLISH, number);

        assertEquals(expected, result);
    }

    public void testNullFormat()
    {
        NumberTranslator translator = new NumberTranslator();

        replay();

        String result = translator.format(_component, null, null);

        assertEquals("", result);

        verify();
    }

    public void testDefaultParse() throws Exception
    {
        NumberTranslator translator = new NumberTranslator();

        testParse(translator, "0.1", new Double(0.1));
    }

    public void testCustomParse() throws Exception
    {
        NumberTranslator translator = new NumberTranslator();

        translator.setPattern("#%");

        testParse(translator, "10%", new Double(0.1));
    }

    public void testTrimmedParse() throws Exception
    {
        NumberTranslator translator = new NumberTranslator();

        translator.setTrim(true);

        testParse(translator, " 100 ", new Long(100));
    }

    private void testParse(Translator translator, String number, Number expected) throws Exception
    {
        IFormComponent field = newField();

        MockControl messagesc = newControl(ValidationMessages.class);
        ValidationMessages messages = (ValidationMessages) messagesc.getMock();

        trainGetLocale(messagesc, messages, Locale.ENGLISH);

        replayControls();

        Number result = (Number) translator.parse(field, messages, number);

        assertEquals(expected, result);

        verifyControls();

    }

    public void testFailedParseDefaultMessage()
    {
        NumberTranslator translator = new NumberTranslator();

        testFailedParse(translator, null);
    }

    public void testFailedParseCustomMessage()
    {
        NumberTranslator translator = new NumberTranslator();

        String message = "Field Name is an invalid number.";

        translator.setMessage(message);

        testFailedParse(translator, message);
    }

    private void testFailedParse(Translator translator, String messageOverride)
    {
        IFormComponent field = newField("Number Field");

        MockControl messagesc = newControl(ValidationMessages.class);
        ValidationMessages messages = (ValidationMessages) messagesc.getMock();

        trainGetLocale(messagesc, messages, Locale.ENGLISH);
        trainGetLocale(messagesc, messages, Locale.ENGLISH);

        trainBuildMessage(
                messagesc,
                messages,
                messageOverride,
                ValidationStrings.INVALID_NUMBER,
                new Object[]
                { "Number Field", "#" },
                "invalid number");

        replayControls();

        try
        {
            System.out.println(translator.parse(field, messages, "Bad-Number"));

            unreachable();
        }
        catch (ValidatorException e)
        {
            assertEquals("invalid number", e.getMessage());
            assertEquals(ValidationConstraint.NUMBER_FORMAT, e.getConstraint());
        }

        verifyControls();
    }

    public void testRenderContribution()
    {
        NumberTranslator translator = new NumberTranslator();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript(translator.defaultScript());

        trainGetLocale(contextc, context, Locale.ENGLISH);

        trainBuildMessage(contextc, context, null, ValidationStrings.INVALID_NUMBER, new Object[]
        { "Number Field", "#" }, "invalid number message");

        context
                .addSubmitHandler("function(event) { Tapestry.validate_number(event, 'numberField', 'invalid number message'); }");

        IFormComponent field = newField("Number Field", "numberField", 1);

        replayControls();

        translator.renderContribution(writer, cycle, context, field);

        verifyControls();
    }

    public void testMessageRenderContribution()
    {
        NumberTranslator translator = new NumberTranslator();

        String messageOverride = "You entered a bunk value for {0}. I should look like {1}. Watch out for ''this''!";

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript(translator.defaultScript());

        trainGetLocale(contextc, context, Locale.ENGLISH);

        trainBuildMessage(
                contextc,
                context,
                messageOverride,
                ValidationStrings.INVALID_NUMBER,
                new Object[]
                { "Number Field", "#" },
                "Blah Blah 'Field Name' Blah.");

        context
                .addSubmitHandler("function(event) { Tapestry.validate_number(event, 'myfield', 'Blah Blah \\'Field Name\\' Blah.'); }");

        IFormComponent field = newField("Number Field", "myfield", 1);

        replayControls();

        translator.setMessage(messageOverride);

        translator.renderContribution(writer, cycle, context, field);

        verifyControls();
    }

    public void testTrimRenderContribution()
    {

        IFormComponent field = newField("Number Field", "myfield", 2);

        NumberTranslator translator = new NumberTranslator();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript(translator.defaultScript());

        trainTrim(context, "myfield");

        trainGetLocale(contextc, context, Locale.ENGLISH);

        trainBuildMessage(contextc, context, null, ValidationStrings.INVALID_NUMBER, new Object[]
        { "Number Field", "#" }, "invalid number message");

        context
                .addSubmitHandler("function(event) { Tapestry.validate_number(event, 'myfield', 'invalid number message'); }");

        replayControls();

        translator.setTrim(true);

        translator.renderContribution(writer, cycle, context, field);

        verifyControls();
    }
}
