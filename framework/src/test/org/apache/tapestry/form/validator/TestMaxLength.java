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

public class TestMaxLength extends BaseValidatorTestCase
{

    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        String object = "short and sweet";

        replay();

        new MaxLength("maxLength=50").validate(field, messages, object);

        verify();
    }

    public void testFail()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.VALUE_TOO_LONG,
                new Object[]
                { new Integer(10), "My Field" },
                "Exception!");

        replay();

        try
        {
            new MaxLength("maxLength=10")
                    .validate(field, messages, "brevity is the essence of wit");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Exception!", ex.getMessage());
            assertEquals(ValidationConstraint.MAXIMUM_WIDTH, ex.getConstraint());
        }
    }

    public void testFailCustomMessage()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                "Too Long",
                ValidationStrings.VALUE_TOO_LONG,
                new Object[]
                { new Integer(10), "My Field" },
                "Exception!");

        replay();

        try
        {
            new MaxLength("maxLength=10,message=Too Long").validate(
                    field,
                    messages,
                    "this should be more than ten characters");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Exception!", ex.getMessage());
            assertEquals(ValidationConstraint.MAXIMUM_WIDTH, ex.getConstraint());
        }
    }

    public void testRenderContribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IFormComponent field = newField("My Field", "myfield");
        
        FormComponentContributorContext context = 
            (FormComponentContributorContext)newMock(FormComponentContributorContext.class);

        context.includeClasspathScript("/org/apache/tapestry/form/validator/StringValidator.js");

        trainFormatMessage(context, null, ValidationStrings.VALUE_TOO_LONG, new Object[]
        { new Integer(20), "My Field" }, "default\\message");

        context
                .addSubmitHandler("function(event) { Tapestry.validate_max_length(event, 'myfield', 20, 'default\\\\message'); }");

        replay();

        new MaxLength("maxLength=20").renderContribution(writer, cycle, context, field);

        verify();
    }
}
