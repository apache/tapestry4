// Copyright 2005, 2006 The Apache Software Foundation
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
 * Tests for {@link org.apache.tapestry.form.validator.MinDate}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestMinDate extends BaseValidatorTestCase
{

    private static final long ONE_DAY = 24 * 60 * 60 * 1000L;

    public void testOK()
        throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date yesterday = new Date(now - ONE_DAY);

        IFormComponent field = newField();
        ValidationMessages message = newMessages();

        replayControls();

        MinDate v = new MinDate();
        v.setMinDate(yesterday);

        v.validate(field, message, today);

        verifyControls();
    }

    public void testFail()
        throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date tomorrow = new Date(now + ONE_DAY);

        IFormComponent field = newField("Fred");
        ValidationMessages message = newMessages(null, ValidationStrings.DATE_TOO_EARLY, new Object[] { "Fred",
                tomorrow }, "default message");

        replayControls();

        MinDate v = new MinDate();
        v.setMinDate(tomorrow);

        try
        {
            v.validate(field, message, today);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("default message", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }

        verifyControls();
    }

    public void testFailCustomMessage()
        throws Exception
    {
        long now = System.currentTimeMillis();

        Date today = new Date(now);
        Date tomorrow = new Date(now + ONE_DAY);

        IFormComponent field = newField("Fred");
        ValidationMessages message = newMessages("custom", ValidationStrings.DATE_TOO_EARLY, new Object[] { "Fred",
                tomorrow }, "custom message");

        replayControls();

        MinDate v = new MinDate("message=custom");
        v.setMinDate(tomorrow);

        try
        {
            v.validate(field, message, today);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("custom message", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
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

        new MinDate().renderContribution(writer, cycle, context, field);

        verifyControls();
    }
}
