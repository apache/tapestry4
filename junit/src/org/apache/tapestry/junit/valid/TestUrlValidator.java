//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.junit.valid;

import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.valid.UrlValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests for {@link org.apache.tapestry.valid.EmailValidator}.
 *
 * @author Tsvetelin Saykov
 * @author Jimmy Dyson
 * @version $Id$
 * @since 3.0
 *
 **/

public class TestUrlValidator extends TapestryTestCase
{
    private UrlValidator v = new UrlValidator();

    public void testValidUrl() throws ValidatorException
    {
        Object result = v.toObject(new MockField("url"), "http://www.google.com");
        assertEquals("http://www.google.com", result);
    }

    public void testInvalidUrl()
    {
        try
        {
            v.toObject(new MockField("url"), "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.URL_FORMAT, ex.getConstraint());
            assertEquals("Invalid URL.", ex.getMessage());
        }
    }

    public void testOverrideInvalidUrlFormatMessage()
    {
        v.setInvalidUrlFormatMessage("Try a valid URL (for {0}), like \"http://www.google.com\"");

        try
        {
            v.toObject(new MockField("url"), "fred");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(
                "Try a valid URL (for url), like \"http://www.google.com\"",
                ex.getMessage());
        }
    }

    public void testTooShort()
    {
        v.setMinimumLength(20);

        try
        {
            v.toObject(new MockField("short"), "http://www.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
            assertEquals("You must enter at least 20 characters for short.", ex.getMessage());
        }
    }

    public void testOverrideMinimumLengthMessage()
    {
        v.setMinimumLength(20);
        v.setMinimumLengthMessage("URLs must be at least 20 characters.");

        try
        {
            v.toObject(new MockField("short"), "http://www.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("URLs must be at least 20 characters.", ex.getMessage());
        }
    }

    public void testDisallowedProtocol()
    {
        v.setAllowedProtocols("http,https");

        try
        {
            v.toObject(new MockField("short"), "ftp://ftp.test.com");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ValidationConstraint.DISALLOWED_PROTOCOL, ex.getConstraint());
            assertEquals("Disallowed protocol - protocol must be http or https.", ex.getMessage());
        }
    }
}