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

package org.apache.tapestry.binding;

import static org.easymock.EasyMock.expect;

import java.util.Date;

import org.apache.hivemind.Location;
import org.apache.tapestry.BindingException;
import org.apache.tapestry.coerce.ValueConverter;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.binding.LiteralBinding}. It also tests some common
 * behaviors provided by {@link org.apache.tapestry.binding.AbstractBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestLiteralBinding extends BindingTestCase
{
    public void testGetObject()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replay();

        LiteralBinding b = new LiteralBinding("parameter foo", vc, l, "literal-value");

        assertSame(l, b.getLocation());
        assertEquals("parameter foo", b.getDescription());
        assertEquals("literal-value", b.getObject());

        assertEquals(true, b.isInvariant());
        assertNull(b.getComponent());

        verify();
    }

    public void testToString()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replay();

        LiteralBinding b = new LiteralBinding("parameter foo", vc, l, "literal-value");

        assertEquals("StaticBinding[literal-value]", b.toString());

        verify();
    }

    public void testGetObjectWithClass()
    {
        ValueConverter vc = newMock(ValueConverter.class);
        Location l = fabricateLocation(99);
        
        Date date = new Date();

        expect(vc.coerceValue("my-literal", Date.class)).andReturn(date);

        replay();
        
        LiteralBinding b = new LiteralBinding("parameter foo", vc, l, "my-literal");

        assertSame(date, b.getObject(Date.class));

        verify();
    }

    public void testGetObjectException()
    {
        ValueConverter vc = newMock(ValueConverter.class);
        Location location = fabricateLocation(99);
        
        Exception innerException = new RuntimeException("Failure");

        expect(vc.coerceValue("my-literal", Date.class)).andThrow(innerException);
        
        replay();
        
        LiteralBinding b = new LiteralBinding("parameter foo", vc, location, "my-literal");

        try
        {
            b.getObject(Date.class);
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals("Error converting value for parameter foo: Failure", ex.getMessage());
            assertSame(innerException, ex.getRootCause());
            assertSame(location, ex.getLocation());
            assertSame(b, ex.getBinding());
        }

        verify();
    }

    public void testSetObject()
    {
        Location l = fabricateLocation(22);
        ValueConverter vc = newValueConverter();

        replay();

        LiteralBinding b = new LiteralBinding("parameter foo", vc, l, "literal-value");

        try
        {
            b.setObject("fred");
            unreachable();
        }
        catch (BindingException ex)
        {
            assertEquals(
                    "Binding for parameter foo (StaticBinding[literal-value]) may not be updated.",
                    ex.getMessage());
            assertSame(b, ex.getBinding());
            assertSame(l, ex.getLocation());
        }

    }
}