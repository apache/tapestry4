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
import org.apache.tapestry.valid.StringValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 *  Tests the {@link StringValidator} class.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class TestStringValidator extends TapestryTestCase
{
    private StringValidator v = new StringValidator();

    public void testToString()
    {
        String in = "Foo";
        String out = v.toString(new MockField("myField"), in);

        assertEquals("Result.", in, out);
    }

    public void testToStringNull()
    {
        String out = v.toString(new MockField("nullField"), null);

        assertNull("Null expected.", out);
    }

    public void testToObjectRequiredFail()
    {
        v.setRequired(true);

        try
        {
            v.toObject(new MockField("requiredField"), "");

            fail("Exception expected.");
        }
        catch (ValidatorException ex)
        {
            assertEquals("You must enter a value for requiredField.", ex.getMessage());
            assertEquals(ValidationConstraint.REQUIRED, ex.getConstraint());
        }
    }

    public void testOverrideRequiredMessage()
    {
        v.setRequired(true);
        v.setRequiredMessage("Gimme a value for {0} you bastard.");

        try
        {
            v.toObject(new MockField("overrideMessage"), "");
        }
        catch (ValidatorException ex)
        {
            assertEquals("Gimme a value for overrideMessage you bastard.", ex.getMessage());
        }
    }

    public void testToObjectRequiredPass() throws ValidatorException
    {
        v.setRequired(true);

        Object result = v.toObject(new MockField("requiredField"), "stuff");

        assertEquals("Result.", "stuff", result);
    }

    public void testToObjectMinimumFail()
    {
        v.setMinimumLength(10);

        try
        {
            v.toObject(new MockField("minimumLength"), "short");

            fail("Exception expected.");
        }
        catch (ValidatorException ex)
        {
            assertEquals(
                "You must enter at least 10 characters for minimumLength.",
                ex.getMessage());
            assertEquals(ValidationConstraint.MINIMUM_WIDTH, ex.getConstraint());
        }
    }

    public void testOverrideMinimumMessage()
    {
        v.setMinimumLength(10);
        v.setMinimumLengthMessage(
            "You really think less than 10 characters for {0} is gonna cut it?");

        try
        {
            v.toObject(new MockField("overrideMessage"), "");
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
        v.setMinimumLength(10);

        String in = "ambidexterous";

        Object out = v.toObject(new MockField("minimum"), in);

        assertEquals("Result", in, out);
    }

    /**
     *  An empty string is not subject to the minimum length constraint.
     * 
     **/

    public void testToObjectMinimumNull() throws ValidatorException
    {
        v.setMinimumLength(10);

        String in = "";

        Object out = v.toObject(new MockField("minimum"), in);

        assertNull("Result", out);
    }

}