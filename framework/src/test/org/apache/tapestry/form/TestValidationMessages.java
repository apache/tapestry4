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

package org.apache.tapestry.form;

import java.util.Locale;

import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.valid.ValidationStrings;

/**
 * Tests for {@link org.apache.tapestry.form.ValidationMessagesImpl}.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class TestValidationMessages extends HiveMindTestCase
{
    public void testMessageOverrideNull()
    {
        ValidationMessages m = new ValidationMessagesImpl(Locale.ENGLISH);

        assertEquals("You must enter a value for My Field.", m.formatValidationMessage(
                null,
                ValidationStrings.REQUIRED_TEXT_FIELD,
                new Object[]
                { "My Field" }));

        m = new ValidationMessagesImpl(new Locale("es"));

        assertEquals("Tiene que ingresar un valor para My Field.", m.formatValidationMessage(
                null,
                ValidationStrings.REQUIRED_TEXT_FIELD,
                new Object[]
                { "My Field" }));
    }

    public void testMessageOverride()
    {
        ValidationMessages m = new ValidationMessagesImpl(Locale.ENGLISH);

        assertEquals("Gimme data for My Field.", m.formatValidationMessage(
                "Gimme data for {0}.",
                ValidationStrings.REQUIRED_TEXT_FIELD,
                new Object[]
                { "My Field" }));
    }

}
