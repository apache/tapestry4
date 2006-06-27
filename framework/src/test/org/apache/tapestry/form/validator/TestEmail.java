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

import static org.testng.AssertJUnit.assertEquals;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Email}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestEmail extends BaseValidatorTestCase
{
    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        replay();

        new Email().validate(field, messages, "hlship@apache.org");

        verify();
    }

    public void testFail()
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
            new Email().validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.EMAIL_FORMAT, ex.getConstraint());
        }

        verify();
    }

    public void testFailCustomMessage()
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

    public void testRenderContribution()
    {
        String pattern = new RegexpMatcher().getEscapedPatternString(Email.PATTERN);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        
        FormComponentContributorContext context = 
            (FormComponentContributorContext)newMock(FormComponentContributorContext.class);

        context.includeClasspathScript("/org/apache/tapestry/form/validator/RegExValidator.js");

        IFormComponent field = newField("Fred", "myfield");

        trainFormatMessage(context, null, ValidationStrings.INVALID_EMAIL, new Object[]
        { "Fred" }, "default\\message");

        context.addSubmitHandler("function(event) { Tapestry.validate_regex(event, 'myfield', '"
                + pattern + "', 'default\\\\message'); }");

        replay();

        new Email().renderContribution(writer, cycle, context, field);

        verify();
    }

    public void testRenderContributionCustomMessage()
    {

        String pattern = new RegexpMatcher().getEscapedPatternString(Email.PATTERN);

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormComponentContributorContext context = 
            (FormComponentContributorContext)newMock(FormComponentContributorContext.class);

        context.includeClasspathScript("/org/apache/tapestry/form/validator/RegExValidator.js");

        IFormComponent field = newField("Fred", "barney");

        trainFormatMessage(
                context,
                "custom",
                ValidationStrings.INVALID_EMAIL,
                new Object[]
                { "Fred" },
                "custom message");

        context.addSubmitHandler("function(event) { Tapestry.validate_regex(event, 'barney', '"
                + pattern + "', 'custom message'); }");

        replay();

        new Email("message=custom").renderContribution(writer, cycle, context, field);

        verify();

    }
}
