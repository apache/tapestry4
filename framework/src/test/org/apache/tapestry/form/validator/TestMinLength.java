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
 * Tests for {@link org.apache.tapestry.form.validator.MinLength}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestMinLength extends BaseValidatorTestCase
{
    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        String object = "a nice long string";

        replayControls();

        new MinLength("minLength=5").validate(field, messages, object);

        verifyControls();
    }

    public void testFail()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.VALUE_TOO_SHORT,
                new Object[]
                { new Integer(10), "My Field" },
                "Exception!");

        replayControls();

        try
        {
            new MinLength("minLength=10").validate(field, messages, "short");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Exception!", ex.getMessage());
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
        }
    }

    public void testFailCustomMessage()
    {
        IFormComponent field = newField("My Field");
        ValidationMessages messages = newMessages(
                "Too Short",
                ValidationStrings.VALUE_TOO_SHORT,
                new Object[]
                { new Integer(10), "My Field" },
                "Exception!");

        replayControls();

        try
        {
            new MinLength("minLength=10,message=Too Short").validate(field, messages, "short");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Exception!", ex.getMessage());
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
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

        context.includeClasspathScript("/org/apache/tapestry/form/validator/StringValidator.js");

        context.getFieldDOM();
        contextc.setReturnValue("document.myform.myfield");

        trainFormatMessage(contextc, context, null, ValidationStrings.VALUE_TOO_SHORT, new Object[]
        { new Integer(20), "My Field" }, "default message");

        context
                .addSubmitListener("function(event) { Tapestry.validate_min_length(event, document.myform.myfield, 20, 'default message'); }");

        replayControls();

        new MinLength("minLength=20").renderContribution(writer, cycle, context, field);

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

        context.includeClasspathScript("/org/apache/tapestry/form/validator/StringValidator.js");

        context.getFieldDOM();
        contextc.setReturnValue("document.myform.myfield");

        trainFormatMessage(
                contextc,
                context,
                "custom",
                ValidationStrings.VALUE_TOO_SHORT,
                new Object[]
                { new Integer(25), "My Field" },
                "custom\\message");

        context
                .addSubmitListener("function(event) { Tapestry.validate_min_length(event, document.myform.myfield, 25, 'custom\\\\message'); }");

        replayControls();

        new MinLength("minLength=25,message=custom").renderContribution(
                writer,
                cycle,
                context,
                field);

        verifyControls();
    }

    public void testNotRequired()
    {
        assertEquals(false, new MinLength().isRequired());
    }
}
