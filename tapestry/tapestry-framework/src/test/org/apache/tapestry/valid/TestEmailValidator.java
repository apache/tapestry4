// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.valid;

import org.apache.tapestry.form.IFormComponent;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.valid.EmailValidator}.
 * 
 * @author Howard Lewis Ship
 * @since 3.0
 */
@Test(sequential=true)
public class TestEmailValidator extends BaseValidatorTestCase
{
    private EmailValidator v = new EmailValidator();

    @AfterMethod
    public void reset()
    {
        v.setClientScriptingEnabled(false);
        v.setInvalidEmailFormatMessage(null);
        v.setMinimumLength(-1);
        v.setMinimumLengthMessage(null);
        v.setRequired(false);
        v.setRequiredMessage(null);
    }
    
    public void testValidEmail() throws ValidatorException
    {
        IFormComponent field = newField();

        replay();

        Object result = v.toObject(field, "foo@bar.com");

        assertEquals("foo@bar.com", result);

        verify();
    }

    public void testInvalidEmail()
    {
        IFormComponent field = newField("email");

        replay();

        try
        {
            v.toObject(field, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.EMAIL_FORMAT, ex.getConstraint());
            assertEquals("Invalid email format for email.  Format is user@hostname.", ex
                    .getMessage());
        }

        verify();
    }

    public void testOverrideInvalidEmailFormatMessage()
    {
        IFormComponent field = newField("email");

        replay();

        v
                .setInvalidEmailFormatMessage("Try a valid e-mail address (for {0}), like ''dick@wad.com.''");

        try
        {
            v.toObject(field, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Try a valid e-mail address (for email), like 'dick@wad.com.'", ex
                    .getMessage());
        }

        verify();
    }

    public void testTooShort()
    {
        IFormComponent field = newField("short");

        replay();

        v.setMinimumLength(20);

        try
        {
            v.toObject(field, "foo@bar.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
            assertEquals("You must enter at least 20 characters for short.", ex.getMessage());
        }

        verify();
    }

    public void testOverrideMinimumLengthMessage()
    {
        IFormComponent field = newField("short");

        replay();

        v.setMinimumLength(20);
        v.setMinimumLengthMessage("E-mail addresses must be at least 20 characters.");

        try
        {
            v.toObject(field, "foo@bar.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("E-mail addresses must be at least 20 characters.", ex.getMessage());
        }

        verify();
    }
}