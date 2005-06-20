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

import java.util.Date;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests for {@link org.apache.tapestry.form.validator.MaxDate}
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
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

        replayControls();

        MaxDate v = new MaxDate();
        v.setMaxDate(today);

        v.validate(field, message, yesterday);

        verifyControls();
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

        replayControls();

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

        verifyControls();
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

        replayControls();

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

        verifyControls();
    }

    public void testRenderComponentNoOp()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        FormComponentContributorContext context = newContext();
        IFormComponent field = newField();

        replayControls();

        new MaxDate().renderContribution(writer, cycle, context, field);

        verifyControls();
    }
}
