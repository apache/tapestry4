// Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.coerce;

import java.util.Collections;
import java.util.Date;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.test.HiveMindTestCase;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.coerce.ValueConverterImpl}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestValueConverter extends HiveMindTestCase
{

    public void testAlreadyCorrectType()
    {
        ValueConverter v = new ValueConverterImpl();

        Object input = new Integer(7);

        assertSame(input, v.coerceValue(input, Integer.class));
        assertSame(input, v.coerceValue(input, Number.class));
    }

    /**
     * Check that primitive types are converted to equivalent wrapper classes for the assignable
     * check. Ok, maybe we should check all the primitive types.
     */

    public void testAlreadyCorrectPrimitiveType()
    {
        ValueConverter v = new ValueConverterImpl();

        Object input = new Integer(9);

        assertSame(input, v.coerceValue(input, int.class));
    }

    public void testNoConverter()
    {
        ValueConverter v = new ValueConverterImpl();

        try
        {
            v.coerceValue("FRED", Date.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(CoerceMessages.noConverter(Date.class), ex.getMessage());
        }
    }

    /**
     * Verify that the message generated for a primtive type identifies the wrapper class.
     */

    public void testNoConverterPrimitive()
    {
        ValueConverter v = new ValueConverterImpl();

        try
        {
            v.coerceValue(new Object(), int.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(CoerceMessages.noConverter(Integer.class), ex.getMessage());
        }
    }

    public void testConverterFound()
    {
        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        Object input = new Integer(7);
        Object output = "SEVEN";

        tc.convertValue(input);
        tcc.setReturnValue(output);

        replayControls();

        ValueConverterImpl v = new ValueConverterImpl();

        TypeConverterContribution contrib = new TypeConverterContribution();

        contrib.setSubjectClass(String.class);
        contrib.setConverter(tc);

        v.setContributions(Collections.singletonList(contrib));

        v.initializeService();

        assertSame(output, v.coerceValue(input, String.class));

        verifyControls();
    }

    public void testConverterFoundForNullValue()
    {
        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        Object output = "NULL";

        tc.convertValue(null);
        tcc.setReturnValue(output);

        replayControls();

        ValueConverterImpl v = new ValueConverterImpl();

        TypeConverterContribution contrib = new TypeConverterContribution();

        contrib.setSubjectClass(String.class);
        contrib.setConverter(tc);

        v.setContributions(Collections.singletonList(contrib));

        v.initializeService();

        assertSame(output, v.coerceValue(null, String.class));

        verifyControls();
    }

    public void testNoConverterFoundForNullValue()
    {
        ValueConverterImpl v = new ValueConverterImpl();

        assertNull(v.coerceValue(null, Date.class));
    }

    public void testStringToNumberPrimitive()
    {
        ValueConverterImpl v = new ValueConverterImpl();

        Object result = v.coerceValue("123", int.class);

        assertEquals(new Integer(123), result);
    }

    public void testStringToNumberWrapper()
    {
        ValueConverterImpl v = new ValueConverterImpl();

        Object result = v.coerceValue("47347437", Long.class);

        assertEquals(new Long(47347437), result);
    }

    public void testInvalidStringToNumber()
    {
        ValueConverterImpl v = new ValueConverterImpl();

        try
        {
            v.coerceValue("fred12345", double.class);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertExceptionSubstring(
                    ex,
                    "Unable to convert 'fred12345' to an instance of java.lang.Double");
        }

    }

    public void testStringToNonNumericPrimitive()
    {
        MockControl tcc = newControl(TypeConverter.class);
        TypeConverter tc = (TypeConverter) tcc.getMock();

        Object input = "false";
        Object output = Boolean.FALSE;

        tc.convertValue(input);
        tcc.setReturnValue(output);

        replayControls();

        ValueConverterImpl v = new ValueConverterImpl();

        TypeConverterContribution contrib = new TypeConverterContribution();

        contrib.setSubjectClass(Boolean.class);
        contrib.setConverter(tc);

        v.setContributions(Collections.singletonList(contrib));

        v.initializeService();

        assertSame(output, v.coerceValue(input, Boolean.class));

        verifyControls();
    }

    public void testNumberToNumber()
    {
        ValueConverterImpl v = new ValueConverterImpl();

        Object result = v.coerceValue(new Integer(123), long.class);

        assertEquals(new Long(123), result);
    }
}