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

import static org.testng.AssertJUnit.assertEquals;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.tapestry.form.IFormComponent;
import org.testng.annotations.Test;

/**
 * Test the {@link NumberValidator}.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.8
 */
@Test
public class TestNumberValidator extends BaseValidatorTestCase
{
    private NumberValidator v = new NumberValidator();

    private void testPassThru(Class valueTypeClass, Number input) throws ValidatorException
    {
        IFormComponent field = newField();

        replay();

        testPassThru(field, valueTypeClass, input);

        verify();
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
        testPassThru(Short.class, new Short((short) 1000));
    }

    public void testInteger() throws ValidatorException
    {
        testPassThru(Integer.class, new Integer(373));
    }

    public void testByte() throws ValidatorException
    {
        testPassThru(Byte.class, new Byte((byte) 131));
    }

    public void testFloat() throws ValidatorException
    {
        testPassThru(Float.class, new Float(3.1415));
    }

    public void testDouble() throws ValidatorException
    {
        testPassThru(Double.class, new Double(348348.484854848));
    }

    public void testLong() throws ValidatorException
    {
        testPassThru(Long.class, new Long(37373218723l));
    }

    public void testInRange() throws ValidatorException
    {
        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        testPassThru(Integer.class, new Integer(150));
    }

    public void testUnderMinimum()
    {
        IFormComponent field = newField("testUnderMinimum");

        replay();

        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        try
        {
            testPassThru(field, Integer.class, new Integer(50));

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("testUnderMinimum must not be smaller than 100.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }

        verify();
    }

    public void testOverrideNumberTooSmallMessage()
    {
        IFormComponent field = newField("underMinimum");

        replay();

        v.setMinimum(new Integer(100));
        v.setNumberTooSmallMessage("Anything under 100 for {0} is worth jack.");

        try
        {
            testPassThru(field, Integer.class, new Integer(50));
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Anything under 100 for underMinimum is worth jack.", ex.getMessage());
        }

        verify();
    }

    public void testOverMaximum()
    {
        IFormComponent field = newField("overMaximum");

        replay();

        v.setMinimum(new Integer(100));
        v.setMaximum(new Integer(200));

        try
        {
            testPassThru(field, Integer.class, new Integer(250));

            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("overMaximum must not be larger than 200.", ex.getMessage());
            assertEquals(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }

        verify();
    }

    public void testOverrideNumberTooLargeMessage()
    {
        IFormComponent field = newField("overMaximum");

        replay();

        v.setMaximum(new Integer(200));
        v.setNumberTooLargeMessage("You think I want a value larger than {1} for {0}?");

        try
        {
            testPassThru(field, Integer.class, new Integer(1000));
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("You think I want a value larger than 200 for overMaximum?", ex
                    .getMessage());
        }

        verify();
    }

    public void testInvalidFormat()
    {
        v.setValueTypeClass(Integer.class);
        IFormComponent field = newField("invalidFormat");

        replay();

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

        verify();
    }

    public void testOverrideInvalidNumericFormatMessage()
    {
        v.setValueTypeClass(Integer.class);
        v.setInvalidNumericFormatMessage("Dude, gimme a number for {0}.");

        IFormComponent field = newField("invalidFormat");

        replay();

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

        verify();
    }

    public void testBigInteger() throws ValidatorException
    {
        testPassThru(BigInteger.class, new BigInteger(
                "234905873490587234905724908252390487590234759023487523489075"));
    }

    public void testBigDecimal() throws ValidatorException
    {
        testPassThru(BigDecimal.class, new BigDecimal(
                "-29574923857342908744.19058734289734907543289752345897234590872349085"));
    }

    /** @since 3.0 * */

    private void checkAdaptorType(int expectedType, Class numberType)
    {
        NumberValidator.NumberStrategy a = NumberValidator.getStrategy(numberType);

        assertEquals(expectedType, a.getNumberType());
    }

    /** @since 3.0 * */

    public void testAdaptorTypes() throws Exception
    {
        checkAdaptorType(NumberValidator.NUMBER_TYPE_INTEGER, Byte.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_INTEGER, Short.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_INTEGER, Integer.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_INTEGER, Long.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_INTEGER, BigInteger.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_REAL, Float.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_REAL, Double.class);
        checkAdaptorType(NumberValidator.NUMBER_TYPE_REAL, BigDecimal.class);
    }

    /** @since 3.0 * */

    private void checkCompare(Number left, Number right)
    {
        NumberValidator.NumberStrategy a = NumberValidator.getStrategy(left.getClass());

        assertEquals(0, a.compare(left, right));
    }

    public void testByteCompare()
    {
        checkCompare(new Byte((byte) 3), new Long(3));
    }

    public void testShortCompare()
    {
        checkCompare(new Short((short) 14), new Double(14.0));
    }

    public void testIntegerCompare()
    {
        checkCompare(new Integer(19), new Long(19));
    }

    public void testLongCompare()
    {
        checkCompare(new Long(-22), new Short((short) -22));
    }

    public void testBigIntegerCompare()
    {
        checkCompare(new BigInteger("300"), new Long("300"));
    }

    public void testFloatCompare()
    {
        checkCompare(new Float("0"), new Double("0"));
    }

    public void testDoubleCompare()
    {
        checkCompare(new Double("0"), new Float("0"));
    }

    public void testBigDecimalCompare()
    {
        checkCompare(new BigDecimal("-137.75"), new Double("-137.75"));
    }
}