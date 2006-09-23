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

import java.util.Collections;

import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.form.FormComponentContributorContext;
import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.form.ValidationMessages;
import org.apache.tapestry.json.JSONObject;
import org.apache.tapestry.valid.ValidationConstants;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidationStrings;
import org.apache.tapestry.valid.ValidatorException;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.form.validator.Required}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
@Test
public class TestRequired extends BaseValidatorTestCase
{
    public void testValidateNotNull() throws Exception
    {
        IFormComponent field = newField();
        ValidationMessages messages = newMessages();

        replay();

        new Required().validate(field, messages, "not null");

        verify();
    }

    public void testValidateNull() throws Exception
    {
        IFormComponent field = newField("Fred");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "Fred" },
                "Default Message for Fred.");

        replay();

        try
        {
            new Required().validate(field, messages, null);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Default Message for Fred.", ex.getMessage());
            assertSame(ValidationConstraint.REQUIRED, ex.getConstraint());
        }

        verify();
    }

    public void testValidateEmptyString() throws Exception
    {
        IFormComponent field = newField("Fred");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "Fred" },
                "Default Message for Fred.");

        replay();

        try
        {
            new Required().validate(field, messages, "");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Default Message for Fred.", ex.getMessage());
            assertSame(ValidationConstraint.REQUIRED, ex.getConstraint());
        }
        
        verify();
    }

    public void testValidateEmptyCollection() throws Exception
    {
        IFormComponent field = newField("Fred");
        ValidationMessages messages = newMessages(
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "Fred" },
                "Default Message for Fred.");

        replay();

        try
        {
            new Required().validate(field, messages, Collections.EMPTY_LIST);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Default Message for Fred.", ex.getMessage());
            assertSame(ValidationConstraint.REQUIRED, ex.getConstraint());
        }
        
        verify();
    }

    public void testValidateNullCustomMessage() throws Exception
    {
        IFormComponent field = newField("Fred");
        ValidationMessages messages = newMessages(
                "custom",
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "Fred" },
                "Custom Message for Fred.");

        replay();

        try
        {
            Required required = new Required("message=custom");

            required.validate(field, messages, null);
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Custom Message for Fred.", ex.getMessage());
            assertSame(ValidationConstraint.REQUIRED, ex.getConstraint());
        }

        verify();
    }
    
    public void test_Render_Contribution()
    {
        IMarkupWriter writer = newWriter();
        IRequestCycle cycle = newCycle();
        JSONObject json = new JSONObject();
        
        FormComponentContributorContext context = newMock(FormComponentContributorContext.class);
        
        IFormComponent field = newField("Fred", "fred");
        
        context.registerForFocus(ValidationConstants.REQUIRED_FIELD);
        
        expect(context.getProfile()).andReturn(json);
        
        trainFormatMessage(
                context,
                null,
                ValidationStrings.REQUIRED_FIELD,
                new Object[]
                { "Fred" },
                "Default\\Message for Fred.");
        
        replay();
        
        new Required().renderContribution(writer, cycle, context, field);
        
        verify();
        
        assertEquals("{\"required\":[\"fred\"],\"fred\":{\"required\":[\"Default\\\\Message for Fred.\"]}}",
                json.toString());
    }
    
    public void testIsRequired()
    {
        assertEquals(true, new Required().isRequired());
    }
}
