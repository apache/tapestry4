// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.form.validator;

import static org.easymock.EasyMock.checkOrder;
import static org.easymock.EasyMock.expect;

import java.util.Date;
import java.util.Locale;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.TranslatedField;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.form.translator.DateTranslator;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.util.Strftime;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.validator.MaxDate}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestMaxDate extends BaseValidatorTestCase
{
    private static final long ONE_DAY = 24 * 60 * 60 * 1000l;
    
    public void testOK() throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date yesterday = new Date(now - ONE_DAY);

        IFormComponent field = newField();
        ValidationMessages message = newMessages();

        replay();

        MaxDate v = new MaxDate();
        v.setMaxDate(today);

        v.validate(field, message, yesterday);

        verify();
    }

    public void testFail() throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date tomorrow = new Date(now + ONE_DAY);

        IFormComponent field = newField("Fred");
        ValidationMessages message = newMessages(
                null,
                ValidationStrings.DATE_TOO_LATE,
                new Object[]
                { "Fred", today },
                "default message");

        replay();

        MaxDate v = new MaxDate();
        v.setMaxDate(today);

        try
        {
            v.validate(field, message, tomorrow);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }

        verify();
    }

    public void testFailCustomMessage() throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date tomorrow = new Date(now + ONE_DAY);

        IFormComponent field = newField("Fred");
        ValidationMessages message = newMessages(
                "custom",
                ValidationStrings.DATE_TOO_LATE,
                new Object[]
                { "Fred", today },
                "custom message");

        replay();

        MaxDate v = new MaxDate("message=custom");
        v.setMaxDate(today);

        try
        {
            v.validate(field, message, tomorrow);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }

        verify();
    }
    
    public void test_Render_Contribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        TranslatedField field = newMock(TranslatedField.class);
        checkOrder(field, false);
        
        Date maxDate = new Date(System.currentTimeMillis() + ONE_DAY);
        DateTranslator translator = new DateTranslator();
        
        expect(field.getTranslator()).andReturn(translator);
        
        expect(field.getClientId()).andReturn("myfield").anyTimes();
        
        expect(field.getDisplayName()).andReturn("My Field");
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        Locale locale = Locale.ENGLISH;
        expect(context.getLocale()).andReturn(locale).anyTimes();
        
        expect(context.getProfile()).andReturn(json);
        
        context.addInitializationScript(field, "dojo.require(\"tapestry.form.datetime\");");
        
        String strMax = translator.format(field, locale, maxDate);
        
        trainFormatMessage(context, null, ValidationStrings.DATE_TOO_LATE, 
                new Object[] { "My Field", strMax }, "default message");
        
        replay();
        
        new MaxDate("maxDate="+strMax).renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[["
                + "tapestry.form.datetime.isValidDate,{max:\""
                + strMax + "\",format:"
                + JSONObject.quote(Strftime.convertToPosixFormat(translator.getPattern())) 
                + "}]]},"
                +"\"myfield\":{\"constraints\":[\"default message\"]}}",
                json.toString());
    }
    
    public void test_Render_Contribution_Custom_Message()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        TranslatedField field = newMock(TranslatedField.class);
        checkOrder(field, false);
        
        Date maxDate = new Date(System.currentTimeMillis() + ONE_DAY);
        DateTranslator translator = new DateTranslator();
        
        expect(field.getTranslator()).andReturn(translator);
        
        expect(field.getClientId()).andReturn("myfield").anyTimes();
        
        expect(field.getDisplayName()).andReturn("My Field");
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        Locale locale = Locale.ENGLISH;
        expect(context.getLocale()).andReturn(locale).anyTimes();
        
        expect(context.getProfile()).andReturn(json);
        
        context.addInitializationScript(field, "dojo.require(\"tapestry.form.datetime\");");
        
        String strMax = translator.format(field, locale, maxDate);
        
        trainFormatMessage(context, "custom", ValidationStrings.DATE_TOO_LATE, 
                new Object[] { "My Field", strMax }, 
                "custom\\message");
        
        replay();
        
        new MaxDate("maxDate=" + strMax + ",message=custom").renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[["
                + "tapestry.form.datetime.isValidDate,{max:\""
                + strMax + "\",format:"
                + JSONObject.quote(Strftime.convertToPosixFormat(translator.getPattern())) 
                + "}]]},"
                + "\"myfield\":{\"constraints\":[\"custom\\\\message\"]}}",
                json.toString());
    }
}
