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

import static org.easymock.EasyMock.expect;
import static org.testng.AssertJUnit.assertEquals;

import java.util.Locale;

import org.apache.tapestry.IPage;
import org.apache.tapestry.form.IFormComponent;

/**
 * Tests for {@link org.apache.tapestry.valid.EmailValidator}.
 * 
 * @author Tsvetelin Saykov
 * @author Jimmy Dyson
 * @since 3.0
 */

public class TestUrlValidator extends BaseValidatorTestCase
{
    private UrlValidator v = new UrlValidator();

    public void testValidUrl() throws ValidatorException
    {
        IFormComponent field = newField();

        replay();

        Object result = v.toObject(field, "http://www.google.com");
        assertEquals("http://www.google.com", result);

        verify();
    }

    public void testInvalidUrl()
    {
        IFormComponent field = newField("url");

        replay();

        try
        {
            v.toObject(field, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.URL_FORMAT, ex.getConstraint());
            assertEquals("Invalid URL.", ex.getMessage());
        }

        verify();
    }

    public void testOverrideInvalidUrlFormatMessage()
    {
        IFormComponent field = newField("url");

        replay();

        v.setInvalidUrlFormatMessage("Try a valid URL (for {0}), like \"http://www.google.com\"");

        try
        {
            v.toObject(field, "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Try a valid URL (for url), like \"http://www.google.com\"", ex
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
            v.toObject(field, "http://www.test.com");
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
        v.setMinimumLengthMessage("URLs must be at least 20 characters.");

        try
        {
            v.toObject(field, "http://www.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("URLs must be at least 20 characters.", ex.getMessage());
        }

        verify();
    }

    public void testDisallowedProtocol()
    {
        IPage page = newPage(Locale.ENGLISH);
        IFormComponent field = newMock(IFormComponent.class);

        expect(field.getPage()).andReturn(page);

        replay();

        v.setAllowedProtocols("http,https");

        try
        {
            v.toObject(field, "ftp://ftp.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.DISALLOWED_PROTOCOL, ex.getConstraint());
            assertEquals("Disallowed protocol - protocol must be http or https.", ex.getMessage());
        }

        verify();
    }
}