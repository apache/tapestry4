/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.tapestry.form.IFormComponent;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.valid.NumberValidator;
import org.apache.tapestry.valid.ValidationConstraint;
import org.apache.tapestry.valid.ValidatorException;

/**
 *  Test the {@link NumberValidator}.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.8
 *
 **/

public class TestNumberValidator extends TapestryTestCase
{
    private NumberValidator v = new NumberValidator();

    public TestNumberValidator(String name)
    {
        super(name);
    }

    private void testPassThru(String displayName, Class valueTypeClass, Number input)
        throws ValidatorException
    {
        testPassThru(new TestingField(displayName), valueTypeClass, input);
    }

    private void testPassThru(IFormComponent field, Class valueTypeClass, Number input)
        throws ValidatorException
    {
        v.setValueTypeClass(valueTypeClass);

        String s = v.toString(field, input);

        Object o = v.toObject(field, s);

        assertEquals("Input and output.", input, o);
    }

    public void testShort() throws ValidatorException
    {
        testPassThru("testShort", Short.class, new Short((short) 1000));
    }

    public void testInteger() throws ValidatorException
    {
        testPassThru("testInteger", Integer.class, new Integer(373));
    }

    public void testByte() throws ValidatorException
    {
        testPassThru("testByte", Byte.class, new Byte((byte) 131));
    }

    public void testFloat() throws ValidatorException
    {
        testPassThru("testFloat", Float.class, new Float(3.1415));
    }

    public void testDouble() throws ValidatorException
    {
        testPassThru("testDouble", Double.class, new Double(348348.484854848));
    }

    public void testLong() throws ValidatorException
    {
        testPassThru("testLong", Long.class, new Long(37373218723l));
    }

    public void testInRange() throws ValidatorException
    {
        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        testPassThru("testInRange", Integer.class, new Integer(150));
    }

    public void testUnderMinimum()
    {
        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        try
        {
            testPassThru("testUnderMinimum", Integer.class, new Integer(50));

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("testUnderMinimum must not be smaller than 100.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }
    }

    public void testOverrideNumberTooSmallMessage()
    {
        v.setMinimum(new Integer(100));
        v.setNumberTooSmallMessage("Anything under 100 for {0} is worth jack.");

        try
        {
            testPassThru("underMinimum", Integer.class, new Integer(50));
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Anything under 100 for underMinimum is worth jack.", ex.getMessage());
        }
    }

    public void testOverMaximum()
    {
        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        try
        {
            testPassThru("overMaximum", Integer.class, new Integer(250));

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("overMaximum must not be larger than 200.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }
    }

    public void testOverrideNumberTooLargeMessage()
    {
        v.setMaximum(new Integer(200));
        v.setNumberTooLargeMessage("You think I want a value larger than {1} for {0}?");

        try
        {
            testPassThru("overMaximum", Integer.class, new Integer(1000));
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(
                "You think I want a value larger than 200 for overMaximum?",
                ex.getMessage());
        }
    }

    public void testInvalidFormat()
    {
        v.setValueTypeClass(Integer.class);
        IFormComponent field = new TestingField("invalidFormat");

        try
        {
            v.toObject(field, "xyz");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("invalidFormat must be a numeric value.", ex.getMessage());
            assertEquals(ValidationConstraint.NUMBER_FORMAT, ex.getConstraint());
        }
    }

    public void testOverrideInvalidNumericFormatMessage()
    {
        v.setValueTypeClass(Integer.class);
        v.setInvalidNumericFormatMessage("Dude, gimme a number for {0}.");

        IFormComponent field = new TestingField("invalidFormat");

        try
        {
            v.toObject(field, "xyz");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Dude, gimme a number for invalidFormat.", ex.getMessage());
            assertEquals(ValidationConstraint.NUMBER_FORMAT, ex.getConstraint());
        }
    }

    public void testBigInteger() throws ValidatorException
    {
        testPassThru(
            "testBigInteger",
            BigInteger.class,
            new BigInteger("234905873490587234905724908252390487590234759023487523489075"));
    }

    public void testBigDecimal() throws ValidatorException
    {
        testPassThru(
            "testBigDecimal",
            BigDecimal.class,
            new BigDecimal("-29574923857342908743.29058734289734907543289752345897234590872349085"));
    }
}