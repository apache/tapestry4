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

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
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
 * Tests for {@link org.apache.tapestry.form.validator.Max}.
 *
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestMax extends BaseValidatorTestCase
{

    public void test_OK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        Integer object = new Integer(10);

        replay();

        new Max("max=50").validate(field, messages, object);

        verify();
    }

    public void test_Fail()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                        { "My Field", new Double(10).toString() },
                "Exception!");

        expect(messages.getLocale()).andReturn(Locale.getDefault()).atLeastOnce();

        replay();

        try
        {
            new Max("max=10").validate(field, messages, new Integer(30));
        }
        catch (ValidatorException ex)
        {
            assertEquals("Exception!", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }
    }

    public void test_Fail_Custom_Message()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                        { "My Field", new Double(100).toString() },
                "custom message");

        replay();

        try
        {
            new Max("max=10,message=custom").validate(field, messages, new Integer(3));
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }
    }

    public void test_Render_Contribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        IFormComponent field = newField("My Field", "myfield");

        Locale locale = Locale.GERMAN;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        expect(context.getLocale()).andReturn(locale).anyTimes();
        expect(context.getProfile()).andReturn(json);

        trainFormatMessage(context, null, ValidationStrings.VALUE_TOO_LARGE,
                           new Object[] {
                                   "My Field",
                                   new Double(20).toString()
                           }, "default message");

        replay();

        new Max("max=20").renderContribution(writer, cycle, context, field);

        verify();

        assertEquals(json.toString(),
                     "{\"constraints\":{\"myfield\":" +
                     "[[tapestry.form.validation.lessThanOrEqual,\"20.0\",{decimal:\",\"}]]}," +
                     "\"myfield\":{\"constraints\":[\"default message\"]}}");
    }

    public void test_Render_Contribution_Custom_Message()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        IFormComponent field = newField("My Field", "myfield");

        Locale locale = Locale.JAPAN;
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);

        expect(context.getLocale()).andReturn(locale).anyTimes();
        expect(context.getProfile()).andReturn(json);

        trainFormatMessage(
                context,
                "custom",
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[] {
                        "My Field",
                        new Double(20).toString()
                }, "custom\\message");

        replay();

        new Max("max=20,message=custom").renderContribution(writer, cycle, context, field);

        verify();

        assertEquals(json.toString(),
                     "{\"constraints\":{\"myfield\":" +
                     "[[tapestry.form.validation.lessThanOrEqual,\"20.0\",{decimal:\".\"}]]}," +
                     "\"myfield\":{\"constraints\":[\"custom\\\\message\"]}}");
    }

}
