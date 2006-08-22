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

package org.apache.tapestry.portlet.bindings;

import static org.easymock.EasyMock.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.tapestry.BaseComponentTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.testng.annotations.Test;

import javax.portlet.PortletRequest;

/**
 * Tests for {@link org.apache.tapestry.portlet.bindings.UserAttributeBinding} and
 * {@link org.apache.tapestry.portlet.bindings.UserAttributeBindingFactory}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
@Test
public class TestUserAttributeBinding extends BaseComponentTestCase
{
    private IBinding newBinding(String bindingDescription, ValueConverter converter,
            Location location, PortletRequest request, String attributeName)
    {
        UserAttributeBindingFactory factory = new UserAttributeBindingFactory();
        factory.setValueConverter(converter);
        factory.setRequest(request);

        return factory.createBinding(null, bindingDescription, attributeName, location);
    }

    private Map newMap(String key, String value)
    {
        Map map = newMock(Map.class);
        checkOrder(map, false);
        
        expect(map.get(key)).andReturn(value);
        
        return map;
    }

    private ValueConverter newConverter()
    {
        return newMock(ValueConverter.class);
    }

    private PortletRequest newRequest(Map userInfo)
    {
        PortletRequest request = newMock(PortletRequest.class);

        expect(request.getAttribute(PortletRequest.USER_INFO)).andReturn(userInfo);

        return request;
    }

    public void testGetObject()
    {
        Map map = newMap("foo.bar", "baz");
        PortletRequest request = newRequest(map);
        ValueConverter vc = newConverter();
        Location l = newLocation();
        
        replay();

        IBinding b = newBinding("description", vc, l, request, "foo.bar");

        assertSame(l, b.getLocation());
        assertEquals("description", b.getDescription());
        assertEquals(false, b.isInvariant());
        assertEquals("baz", b.getObject());

        verify();
    }

    public void testGetObjectNoUserInfo()
    {
        ValueConverter vc = newConverter();
        PortletRequest request = newRequest(null);
        Location l = newLocation();

        replay();

        IBinding b = newBinding("description", vc, l, request, "foo.bar");

        try
        {
            b.getObject();
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertEquals(BindingsMessages.noUserInfo(), ex.getMessage());
            assertSame(l, ex.getLocation());
        }

        verify();
    }

    public void testSetObject()
    {
        Object newValue = new Object();
        String valueConverted = "CONVERTED";

        Map map = new HashMap();
        
        ValueConverter converter = newMock(ValueConverter.class);
        Location l = newLocation();

        expect(converter.coerceValue(newValue, String.class)).andReturn(valueConverted);
        
        PortletRequest request = newRequest(map);
        
        replay();

        IBinding b = newBinding("description", converter, l, request, "foo.bar");

        b.setObject(newValue);

        assertSame(valueConverted, map.get("foo.bar"));

        verify();
    }

}
