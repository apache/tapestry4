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

package org.apache.tapestry.valid;

import java.util.Map;

import org.apache.tapestry.form.IFormComponent;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.valid.IntValidator}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestIntValidator extends BaseValidatorTestCase
{
    public void testToStringNull()
    {
        IValidator validator = new IntValidator();

        assertNull(validator.toString(null, null));
    }

    public void testToStringNonZero()
    {
        Number number = new Integer(37);

        IValidator validator = new IntValidator();

        assertEquals("37", validator.toString(null, number));
    }

    public void testToStringZero()
    {
        Number number = new Integer(0);

        IValidator validator = new IntValidator();

        assertEquals("0", validator.toString(null, number));
    }

    public void testToStringZeroAsNull()
    {
        Number number = new Integer(0);

        IValidator validator = new IntValidator("zeroIsNull");

        assertNull(validator.toString(null, number));
    }

    public void testToObjectNull() throws Exception
    {
        IValidator validator = new IntValidator();

        assertNull(validator.toObject(null, ""));
    }

    public void testToObjectSucess() throws Exception
    {
        IValidator validator = new IntValidator();

        Number expected = new Integer("-12345");

        assertEquals(expected, validator.toObject(null, "-12345"));
    }

    public void testToObjectFailure()
    {
        IFormComponent field = newField("BamBam");

        replay();

        IValidator validator = new IntValidator();

        try
        {
            validator.toObject(field, "abcdef");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals(ex.getMessage(), "BamBam must be a numeric value. ");
            assertSame(ValidationConstraint.NUMBER_FORMAT, ex.getConstraint());
        }
    }

    public void testToObjectTooSmall()
    {
        IFormComponent field = newField("Fred");

        replay();

        IValidator validator = new IntValidator("minimum=10");

        try
        {
            validator.toObject(field, "9");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Fred must not be smaller than 10.", ex.getMessage());
            assertSame(ValidationConstraint.TOO_SMALL, ex.getConstraint());
        }
    }

    public void testToObjectTooLarge()
    {
        IFormComponent field = newField("Barney");

        replay();

        IValidator validator = new IntValidator("maximum=10");

        try
        {
            validator.toObject(field, "207");
            unreachable();
        }
        catch (ValidatorException ex)
        {
            assertEquals("Barney must not be larger than 10.", ex.getMessage());
            assertSame(ValidationConstraint.TOO_LARGE, ex.getConstraint());
        }
    }

    public void testScriptSymbolsJustFormat()
    {
        IFormComponent field = newField("Fred");

        replay();

        IntValidator validator = new IntValidator();

        Map map = validator.buildSymbols(field);

        assertEquals(1, map.size());

        assertEquals(map.get("formatMessage"), "Fred must be an integer value. ");

        verify();
    }

    public void testScriptSymbolsRequired()
    {
        IFormComponent field = newField("Barney", 2);

        replay();

        IntValidator validator = new IntValidator("required");

        Map map = validator.buildSymbols(field);

        assertEquals(2, map.size());

        assertEquals("You must enter a value for Barney.", map.get("requiredMessage"));
        assertEquals(map.get("formatMessage"), "Barney must be an integer value. ");

        verify();
    }

    public void testScriptSymbolsMinimum()
    {
        IFormComponent field = newField("Barney", 2);

        replay();

        IntValidator validator = new IntValidator("minimum=5");

        Map map = validator.buildSymbols(field);

        assertEquals(4, map.size());

        assertEquals(map.get("minimum"), new Integer(5));
        assertNull(map.get("maximum"));
        assertEquals("Barney must not be smaller than 5.", map.get("rangeMessage"));
        assertEquals(map.get("formatMessage"), "Barney must be an integer value. ");

        verify();
    }
    
    public void testScriptSymbolsMaximum()
    {
        IFormComponent field = newField("Barney", 2);

        replay();

        IntValidator validator = new IntValidator("maximum=5");

        Map map = validator.buildSymbols(field);

        assertEquals(4, map.size());

        assertEquals(new Integer(5), map.get("maximum"));
        assertNull(map.get("minimum"));
        assertEquals("Barney must not be larger than 5.", map.get("rangeMessage"));
        assertEquals(map.get("formatMessage"), "Barney must be an integer value. ");

        verify();
    }    
    
    public void testScriptSymbolsRange()
    {
        IFormComponent field = newField("Barney", 2);

        replay();

        IntValidator validator = new IntValidator("maximum=5,minimum=1");

        Map map = validator.buildSymbols(field);

        assertEquals(4, map.size());

        assertEquals(new Integer(1), map.get("minimum"));
        assertEquals(new Integer(5), map.get("maximum"));
        assertEquals("Barney must be between 1 and 5.", map.get("rangeMessage"));
        assertEquals(map.get("formatMessage"), "Barney must be an integer value. ");

        verify();
    }
}