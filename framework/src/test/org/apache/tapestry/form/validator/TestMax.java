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
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Max}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestMax extends BaseValidatorTestCase
{

    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        Integer object = new Integer(10);

        replayControls();

        new Max("max=50").validate(field, messages, object);

        verifyControls();
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

        replayControls();

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

        replayControls();

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
        IFormComponent field = newField("My Field");
        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript("/org/apache/tapestry/form/validator/NumberValidator.js");

        trainFormatMessage(contextc, context, null, ValidationStrings.VALUE_TOO_LARGE, new Object[]
        { "My Field", new Double(20) }, "default message");

        context.getFieldDOM();
        contextc.setReturnValue("document.myform.myfield");

        context
                .addSubmitListener("function(event) { validate_max_number(event, document.myform.myfield, 20.0, 'default message'); }");

        replayControls();

        new Max("max=20").renderContribution(writer, cycle, context, field);

        verifyControls();
    }

    public void testRenderContributionCustomMessage()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        IFormComponent field = newField("My Field");
        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript("/org/apache/tapestry/form/validator/NumberValidator.js");

        trainFormatMessage(
                contextc,
                context,
                "custom",
                ValidationStrings.VALUE_TOO_LARGE,
                new Object[]
                { "My Field", new Double(20) },
                "custom message");

        context.getFieldDOM();
        contextc.setReturnValue("document.myform.myfield");

        context
                .addSubmitListener("function(event) { validate_max_number(event, document.myform.myfield, 20.0, 'custom message'); }");

        replayControls();

        new Max("max=20,message=custom").renderContribution(writer, cycle, context, field);

        verifyControls();
    }

}
