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
import org.apache.tapestry.util.RegexpMatcher;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Pattern}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestPattern extends BaseValidatorTestCase
{

    public void testOK() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        replayControls();

        new Pattern("pattern=\\d+").validate(field, messages, "1232");

        verifyControls();
    }

    public void testFail()
    {
        IFormComponent field = newField("My Pattern");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.REGEX_MISMATCH,
                new Object[]
                { "My Pattern" },
                "default message");

        replayControls();

        try
        {
            new Pattern("pattern=\\d+").validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, ex.getConstraint());
        }

        verifyControls();
    }

    public void testFailCustomMessage()
    {
        IFormComponent field = newField("My Pattern");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.REGEX_MISMATCH,
                new Object[]
                { "My Pattern" },
                "custom message");

        replayControls();

        try
        {
            new Pattern("pattern=\\d+,message=custom").validate(field, messages, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.PATTERN_MISMATCH, ex.getConstraint());
        }

        verifyControls();
    }

    public void testRenderContribution()
    {
        String pattern = new RegexpMatcher().getEscapedPatternString("\\d+");

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript("/org/apache/tapestry/form/validator/RegExValidator.js");

        IFormComponent field = newField("Fred");

        trainFormatMessage(contextc, context, null, ValidationStrings.REGEX_MISMATCH, new Object[]
        { "Fred" }, "default message");

        context.getFieldDOM();
        contextc.setReturnValue("document.fred.barney");

        context
                .addSubmitListener("function(event) { validate_regexp(event, document.fred.barney, '"
                        + pattern + "', 'default message'); }");

        replayControls();

        new Pattern("pattern=\\d+").renderContribution(writer, cycle, context, field);

        verifyControls();
    }

    public void testRenderContributionCustomMessage()
    {
        String pattern = new RegexpMatcher().getEscapedPatternString("\\d+");

        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();

        MockControl contextc = newControl(FormComponentContributorContext.class);
        FormComponentContributorContext context = (FormComponentContributorContext) contextc
                .getMock();

        context.includeClasspathScript("/org/apache/tapestry/form/validator/RegExValidator.js");

        IFormComponent field = newField("Fred");

        trainFormatMessage(
                contextc,
                context,
                "custom",
                ValidationStrings.REGEX_MISMATCH,
                new Object[]
                { "Fred" },
                "custom message");

        context.getFieldDOM();
        contextc.setReturnValue("document.fred.barney");

        context
                .addSubmitListener("function(event) { validate_regexp(event, document.fred.barney, '"
                        + pattern + "', 'custom message'); }");

        replayControls();

        new Pattern("pattern=\\d+,message=custom").renderContribution(writer, cycle, context, field);

        verifyControls();

    }

}
