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

package org.apache.tapestry.binding;

import java.util.Date;

import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.coerce.ValueConverter;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.binding.StaticBinding}. It also tests some common behaviors
 * provided by {@link org.apache.tapestry.binding.AbstractBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class TestStaticBinding extends BindingTestCase
{
    public void testGetObject()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replayControls();

        StaticBinding b = new StaticBinding("param", "literal-value", vc, l);

        assertSame(l, b.getLocation());
        assertEquals("param", b.getParameterName());
        assertEquals("literal-value", b.getObject());

        assertEquals(true, b.isInvariant());
        assertNull(b.getComponent());

        verifyControls();
    }

    public void testToString()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replayControls();

        StaticBinding b = new StaticBinding("param", "literal-value", vc, l);

        assertEquals("StaticBinding[literal-value]", b.toString());

        verifyControls();
    }

    public void testGetObjectWithClass()
    {
        MockControl control = newControl(ValueConverter.class);
        ValueConverter vc = (ValueConverter) control.getMock();

        Date date = new Date();

        vc.coerceValue("my-literal", Date.class);
        control.setReturnValue(date);

        replayControls();

        StaticBinding b = new StaticBinding("param", "my-literal", vc, fabricateLocation(99));

        assertSame(date, b.getObject(Date.class));

        verifyControls();
    }

    public void testGetObjectException()
    {
        MockControl control = newControl(ValueConverter.class);
        ValueConverter vc = (ValueConverter) control.getMock();

        Exception innerException = new RuntimeException("Failure");

        vc.coerceValue("my-literal", Date.class);
        control.setThrowable(innerException);

        replayControls();

        Location location = fabricateLocation(99);
        StaticBinding b = new StaticBinding("param", "my-literal", vc, location);

        try
        {
            b.getObject(Date.class);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Error converting value for parameter 'param': Failure", ex.getMessage());
            assertSame(innerException, ex.getRootCause());
            assertSame(location, ex.getLocation());
            assertSame(b, ex.getBinding());
        }

        verifyControls();
    }

    public void testSetObject()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replayControls();

        StaticBinding b = new StaticBinding("param", "literal-value", vc, l);

        try
        {
            b.setObject("fred");
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Binding value may not be updated.", ex.getMessage());
            assertSame(b, ex.getBinding());
            assertSame(l, ex.getLocation());
        }

    }
}