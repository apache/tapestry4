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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.testng.annotations.Test;

/**
 * Tests for {@link org.apache.tapestry.binding.ComponentBinding}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestComponentBinding extends BindingTestCase
{
    public void testGetObject()
    {
        IComponent nested = newComponent();
        IComponent component = newMock(IComponent.class);
        
        expect(component.getComponent("foo")).andReturn(nested);

        ValueConverter vc = newValueConverter();

        Location l = newLocation();

        replay();

        ComponentBinding b = new ComponentBinding("param", vc, l, component, "foo");

        assertSame(component, b.getComponent());
        assertSame(nested, b.getObject());

        verify();
    }

    public void testGetObjectFailure()
    {
        IComponent component = newMock(IComponent.class);

        Throwable t = new ApplicationRuntimeException("No such component.");

        expect(component.getComponent("foo")).andThrow(t);

        ValueConverter vc = newValueConverter();

        Location l = newLocation();

        replay();

        ComponentBinding b = new ComponentBinding("param", vc, l, component, "foo");

        try
        {
            b.getObject();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(t.getMessage(), ex.getMessage());
            assertSame(t, ex.getRootCause());
            assertSame(l, ex.getLocation());
        }

        verify();
    }
}