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
import static org.easymock.EasyMock.expect;
import org.testng.annotations.Test;

import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * Tests for {@link org.apache.tapestry.form.translator.NumberTranslator}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestNumberTranslator extends FormComponentContributorTestCase
{
    public void test_Default_Format()
    {
        NumberTranslator translator = new NumberTranslator();

        testFormat(translator, new Integer(10), "10");
    }

    public void test_Omit_Zero()
    {
        NumberTranslator translator = new NumberTranslator("pattern=0.00,omitZero=true");
        
        testFormat(translator, new Integer(0), "");
        
        assertEquals(translator.getValueForEmptyInput(), null);
    }

    public void test_Omit_Zero_Off()
    {
        NumberTranslator translator = new NumberTranslator("!omitZero,pattern=0.00");
        
        testFormat(translator, new Integer(0), "0.00");
    }

    public void test_Custom_Format()
    {
        NumberTranslator translator = new NumberTranslator();

        translator.setPattern("$#0.00");

        testFormat(translator, new Integer(10), "$10.00");
    }

    public void test_Initializer_Format()
    {
        NumberTranslator translator = new NumberTranslator("pattern=#0%");

        testFormat(translator, new Double(0.10), "10%");
    }

    private void testFormat(Translator translator, Number number, String expected)
    {
        IFormComponent field = newField();

        String result = translator.format(field, Locale.ENGLISH, number);

        assertEquals(result, expected);
    }

    public void test_Null_Format()
    throws Exception
    {
        NumberTranslator translator = new NumberTranslator("pattern=0.00,omitZero");
        IFormComponent field = newField();
        ValidationMessages messages = newMock(ValidationMessages.class);
        
        replay();
        
        Number num = (Number)translator.parse(field, messages, "");
        
        assert num == null;
        
        verify();
    }

    public void test_Null_Format_Pattern()
    {
        NumberTranslator translator = new NumberTranslator();

        replay();

        String result = translator.format(_component, null, null);

        assertEquals("", result);

        verify();
    }
    
    public void test_Localized_Format()
    {
        IFormComponent field = newField();
        NumberTranslator translator = new NumberTranslator();
        translator.setPattern("#,###");
        
        replay();
        
        Integer input = new Integer(10999999);
        String result = translator.format(field, Locale.FRENCH, input);
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.FRENCH);
        
        verify();
        
        assertEquals(result, "10" + sym.getGroupingSeparator() + "999" + sym.getGroupingSeparator() + "999");
    }
    
    public void test_Default_Parse() throws Exception
    {
        NumberTranslator translator = new NumberTranslator();

        testParse(translator, "0.1", new Double(0.1));
    }

    public void test_Custom_Parse() throws Exception
    {
        NumberTranslator translator = new NumberTranslator();

        translator.setPattern("#%");

        testParse(translator, "10%", new Double(0.1));
    }

    public void test_Trimmed_Parse() throws Exception
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

    public void test_Failed_Parse_Default_Message()
    {
        NumberTranslator translator = new NumberTranslator();
        
        testFailedParse(translator, null);
    }

    public void test_Failed_Parse_Custom_Message()
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

    public void test_Render_Contribution()
    {
        NumberTranslator translator = new NumberTranslator();
        IFormComponent field = newField("Number Field", "numberField", 1);
        
        JSONObject json = new JSONObject();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        expect(context.getProfile()).andReturn(json);
        context.addInitializationScript(field, "dojo.require(\"dojo.i18n.number\");");
        
        trainGetLocale(context, Locale.ENGLISH);
        
        trainBuildMessage(context, null, ValidationStrings.INVALID_NUMBER, 
                new Object[] { "Number Field", "#" }, "invalid number message");
        
        replay();
        
        translator.renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals(json.toString(),
                "{\"constraints\":{\"numberField\":" +
                "[[dojo.i18n.number.isReal,null,{places:0,decimal:\".\",separator:\"\"}]]}," +
                "\"numberField\":{\"constraints\":[\"invalid number message\"]}}");
    }

    public void test_Render_Grouping_Separator()
    {
        NumberTranslator translator = new NumberTranslator();
        translator.setPattern("###,##");
        IFormComponent field = newField("Number Field", "numberField", 1);

        JSONObject json = new JSONObject();

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        expect(context.getProfile()).andReturn(json);
        context.addInitializationScript(field, "dojo.require(\"dojo.i18n.number\");");

        trainGetLocale(context, Locale.US);

        trainBuildMessage(context, null, ValidationStrings.INVALID_NUMBER, new String[] { "Number Field", "#,##" },
                          "invalid number message");

        replay();

        translator.renderContribution(writer, cycle, context, field);

        verify();

        assertEquals(json.toString(),
                "{\"constraints\":{\"numberField\":" +
                "[[dojo.i18n.number.isReal,null," +
                "{places:0,decimal:\".\",separator:\",\",groupSize:2}]]}," +
                "\"numberField\":{\"constraints\":[\"invalid number message\"]}}");
    }

    public void test_Message_Render_Contribution()
    {
        NumberTranslator translator = new NumberTranslator();
        IFormComponent field = newField("Number Field", "myfield", 1);
        
        String messageOverride = "You entered a bunk value for {0}. I should look like {1}. Watch out for ''this''!";

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        expect(context.getProfile()).andReturn(json);
        context.addInitializationScript(field, "dojo.require(\"dojo.i18n.number\");");
        
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
        
        assertEquals(json.toString(),
                "{\"constraints\":{\"myfield\":[[dojo.i18n.number.isReal,null," +
                "{places:0,decimal:\".\",separator:\"\"}]]}," +
                "\"myfield\":{\"constraints\":[\"Blah Blah 'Field Name' Blah.\"]}}");
    }
    
    public void test_Trim_Render_Contribution()
    {
        IFormComponent field = newField("Number Field", "myfield", 2);
        
        NumberTranslator translator = new NumberTranslator();
        JSONObject json = new JSONObject();
        
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        expect(context.getProfile()).andReturn(json);
        context.addInitializationScript(field, "dojo.require(\"dojo.i18n.number\");");
        
        trainGetLocale(context, Locale.ENGLISH);
        
        trainBuildMessage(context, null, ValidationStrings.INVALID_NUMBER, new Object[]
        { "Number Field", "#" }, "invalid number message");
        
        expect(context.getProfile()).andReturn(json);
        
        replay();

        translator.setTrim(true);
        
        translator.renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals(json.toString(),
                "{\"trim\":[\"myfield\"]," +
                "\"constraints\":{\"myfield\":[[dojo.i18n.number.isReal,null," +
                "{places:0,decimal:\".\",separator:\"\"}]]}," +
                "\"myfield\":{\"constraints\":[\"invalid number message\"]}}");
    }
}
