/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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