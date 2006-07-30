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

package org.apache.tapestry.form.translator;

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.FormComponentContributorTestCase;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.translator.NumberTranslator}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
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

    private void testFormat(Translator translator, Number number, String expected)
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
        
        ValidationMessages messages = newMock(ValidationMessages.class);

        trainGetLocale(messages, Locale.ENGLISH);

        replay();

        Number result = (Number) translator.parse(field, messages, number);

        assertEquals(expected, result);

        verify();

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
        
        ValidationMessages messages = newMock(ValidationMessages.class);

        trainGetLocale(messages, Locale.ENGLISH);

        trainBuildMessage(
                messages,
                messageOverride,
                ValidationStrings.INVALID_NUMBER,
                new Object[]
                { "Number Field", "#" },
                "invalid number");

        replay();

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

        verify();
    }

    public void testRenderContribution()
    {
        NumberTranslator translator = new NumberTranslator();
        IFormComponent field = newField("Number Field", "numberField", 1);
        
        JSONObject json = new JSONObject();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        expect(context.getProfile()).andReturn(json);
        
        trainGetLocale(context, Locale.ENGLISH);
        
        trainBuildMessage(context, null, ValidationStrings.INVALID_NUMBER, 
                new Object[] { "Number Field", "#" }, "invalid number message");
        
        replay();
        
        translator.renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals( "{\"constraints\":{\"numberField\":[dojo.validate.isRealNumber,"
                + "{places:0,decimal:\".\",separator:\",\"}]},"
                + "\"numberField\":{\"constraints\":\"invalid number message\"}}",
                json.toString());
    }

    public void testMessageRenderContribution()
    {
        NumberTranslator translator = new NumberTranslator();
        IFormComponent field = newField("Number Field", "myfield", 1);
        
        String messageOverride = "You entered a bunk value for {0}. I should look like {1}. Watch out for ''this''!";

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        expect(context.getProfile()).andReturn(json);
        
        trainGetLocale(context, Locale.ENGLISH);

        trainBuildMessage(
                context,
                messageOverride,
                ValidationStrings.INVALID_NUMBER,
                new Object[] { "Number Field", "#" },
                "Blah Blah 'Field Name' Blah.");
        
        replay();

        translator.setMessage(messageOverride);

        translator.renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[dojo.validate.isRealNumber,"
                + "{places:0,decimal:\".\",separator:\",\"}]},"
                + "\"myfield\":{\"constraints\":\"Blah Blah \'Field Name\' Blah.\"}}",
                json.toString());
    }
    
    public void testTrimRenderContribution()
    {
        IFormComponent field = newField("Number Field", "myfield", 2);
        
        NumberTranslator translator = new NumberTranslator();
        JSONObject json = new JSONObject();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        expect(context.getProfile()).andReturn(json);
        
        trainGetLocale(context, Locale.ENGLISH);
        
        trainBuildMessage(context, null, ValidationStrings.INVALID_NUMBER, new Object[]
        { "Number Field", "#" }, "invalid number message");
        
        expect(context.getProfile()).andReturn(json);
        
        replay();

        translator.setTrim(true);
        
        translator.renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"trim\":\"myfield\","
                + "\"constraints\":{\"myfield\":[dojo.validate.isRealNumber,"
                + "{places:0,decimal:\".\",separator:\",\"}]},"
                + "\"myfield\":{\"constraints\":\"invalid number message\"}}",
                json.toString());
                
    }
}
