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

package org.apache.tapestry.junit.valid;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.valid.StringValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 * Tests the {@link StringValidator}class.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 */

public class TestStringValidator extends BaseValidatorTestCase
{
    private StringValidator v = new StringValidator();

    public void testToString()
    {
        IFormComponent field = newField();

        replayControls();

        String in = "Foo";
        String out = v.toString(field, in);

        assertEquals("Result.", in, out);

        verifyControls();
    }

    public void testToStringNull()
    {
        IFormComponent field = newField();

        replayControls();

        String out = v.toString(field, null);

        assertNull("Null expected.", out);

        verifyControls();
    }

    public void testToObjectRequiredFail()
    {
        IFormComponent field = newField("requiredField");

        replayControls();

        v.setRequired(true);

        try
        {
            v.toObject(field, "");

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("You must enter a value for requiredField.", ex.getMessage());
            assertEquals(ValidationConstraint.REQUIRED, ex.getConstraint());
        }

        verifyControls();
    }

    public void testOverrideRequiredMessage()
    {
        IFormComponent field = newField("overrideMessage");

        replayControls();

        v.setRequired(true);
        v.setRequiredMessage("Gimme a value for {0} you bastard.");

        try
        {
            v.toObject(field, "");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Gimme a value for overrideMessage you bastard.", ex.getMessage());
        }

        verifyControls();
    }

    public void testToObjectRequiredPass() throws ValidatorException
    {
        IFormComponent field = newField();

        replayControls();

        v.setRequired(true);

        Object result = v.toObject(field, "stuff");

        assertEquals("Result.", "stuff", result);

        verifyControls();
    }

    public void testToObjectMinimumFail()
    {
        IFormComponent field = newField("minimumLength");

        replayControls();

        v.setMinimumLength(10);

        try
        {
            v.toObject(field, "abc");

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("You must enter at least 10 characters for minimumLength.", ex
                    .getMessage());
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
        }

        verifyControls();
    }

    public void testOverrideMinimumMessage()
    {
        IFormComponent field = newField("overrideMessage");

        replayControls();

        v.setMinimumLength(10);
        v
                .setMinimumLengthMessage("You really think less than 10 characters for {0} is gonna cut it?");

        try
        {
            v.toObject(field, "");
        }
        catch (ValidatorException ex)
        {
            assertEquals(
                    "You really think less than 10 characters for overrideMessage is gonna cut it?",
                    ex.getMessage());
        }
    }

    public void testToObjectMinimumPass() throws ValidatorException
    {
        IFormComponent field = newField();

        replayControls();

        v.setMinimumLength(10);

        String in = "ambidexterous";

        Object out = v.toObject(field, in);

        assertEquals("Result", in, out);

        verifyControls();
    }

    /**
     * An empty string is not subject to the minimum length constraint.
     */

    public void testToObjectMinimumNull() throws ValidatorException
    {
        IFormComponent field = newField();

        replayControls();

        v.setMinimumLength(10);

        String in = "";

        Object out = v.toObject(field, in);

        assertNull("Result", out);

        verifyControls();
    }
}