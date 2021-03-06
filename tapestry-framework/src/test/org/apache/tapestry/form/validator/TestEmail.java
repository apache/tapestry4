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

import static org.easymock.EasyMock.expect;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Email}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestEmail extends BaseValidatorTestCase
{
    @Test(dataProvider = "validEmails", timeOut = 1000)
    public void test_OK(String email) throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        replay();

        new Email().validate(field, messages, email);

        verify();
    }

    @Test(dataProvider = "invalidEmails", timeOut = 1000)
    public void test_Fail(String email)
    {
        IFormComponent field = newField("My Email");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.INVALID_EMAIL,
                new Object[]
                { "My Email" },
                "default message");

        replay();

        try
        {
            new Email().validate(field, messages, email);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.EMAIL_FORMAT, ex.getConstraint());
        }

        verify();
    }

    public void test_Fail_Custom_Message()
    {
        IFormComponent field = newField("My Email");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.INVALID_EMAIL,
                new Object[]
                { "My Email" },
                "custom message");

        replay();

        try
        {
            new Email("message=custom").validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.EMAIL_FORMAT, ex.getConstraint());
        }

        verify();
    }

    public void test_Render_Contribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        IFormComponent field = newField("Fred", "myfield");
        
        expect(context.getProfile()).andReturn(json);
        
        trainFormatMessage(context, null, ValidationStrings.INVALID_EMAIL, 
                new Object[] { "Fred" }, "default\\message");
        
        replay();

        new Email().renderContribution(writer, cycle, context, field);

        verify();
        
        assertEquals("{\"constraints\":{\"myfield\":[[tapestry.form.validation.isEmailAddress,false,true]]},"
                +"\"myfield\":{\"constraints\":[\"default\\\\message\"]}}",
                json.toString());
    }

    public void test_Render_Contribution_Custom_Message()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        IFormComponent field = newField("Fred", "barney");
        
        expect(context.getProfile()).andReturn(json);
        
        trainFormatMessage(
                context,
                "custom",
                ValidationStrings.INVALID_EMAIL,
                new Object[]
                { "Fred" },
                "custom message");
        
        replay();
        
        new Email("message=custom").renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"constraints\":{\"barney\":[[tapestry.form.validation.isEmailAddress,false,true]]},"
                + "\"barney\":{\"constraints\":[\"custom message\"]}}",
                json.toString());
    }

    @DataProvider(name="validEmails")
    protected Object[][] getValidEmails() {
        return new Object[][] {
                {"hlship@apache.org"},
                {"j@apache.org"},
                {"jkuhnert@a.org"},
                {"J@A.oRg"},
                {"foo@example-bar.domain.com"},
                {"FOO@EXample-bAr.domain.com"},
                {"_foo@example.com"},
                {"$user+mailbox_@example-domain.com"},
        };
    }

    @DataProvider(name="invalidEmails")
    protected Object[][] getInvalidEmails() {
        return new Object[][] {
                {"fred"},
                {"foooooooooooooooooooooo"},
                {"foooooooooooooooooooooooooooo"},
                {"LASKFODSKFO@$#)DJMZCV)TQKALAD"},
                {""},
                {"aa@foooooooooooooooooooooooooooooooooooooooooooooooooooooooo"},
                {"aa@.foooooooooooooooooooooooooooooooooooooooooooooooooooooooo"},
        };
    }
}
