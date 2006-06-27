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
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Max}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestMax extends BaseValidatorTestCase
{

    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        Integer object = new Integer(10);

        replay();

        new Max("max=50").validate(field, messages, object);

        verify();
    }

    public void testFail()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                { "My Field", new Double(10) },
                "Exception!");

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

    public void testFailCustomMessage()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                { "My Field", new Double(100) },
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

    public void testRenderContribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IFormComponent field = newField("My Field", "myfield");
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        context.includeClasspathScript("/org/apache/tapestry/form/validator/NumberValidator.js");
        
        trainFormatMessage(context, null, ValidationStrings.VALUE_TOO_LARGE, new Object[]
        { "My Field", new Double(20) }, "default message");

        context.addSubmitHandler("function(event) { Tapestry.validate_max_number(event, 'myfield', 20.0, 'default message'); }");

        replay();

        new Max("max=20").renderContribution(writer, cycle, context, field);

        verify();
    }

    public void testRenderContributionCustomMessage()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IFormComponent field = newField("My Field", "myfield");
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);

        context.includeClasspathScript("/org/apache/tapestry/form/validator/NumberValidator.js");

        trainFormatMessage(
                context,
                "custom",
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                { "My Field", new Double(20) },
                "custom\\message");
        
        context.addSubmitHandler("function(event) { Tapestry.validate_max_number(event, 'myfield', 20.0, 'custom\\\\message'); }");

        replay();

        new Max("max=20,message=custom").renderContribution(writer, cycle, context, field);

        verify();
    }

}
