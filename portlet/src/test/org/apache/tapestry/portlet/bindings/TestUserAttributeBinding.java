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

import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.test.HiveMindTestCase;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.portlet.bindings.UserAttributeBindingFactory;
import org.easymock.MockControl;

/**
 * Tests for {@link org.apache.tapestry.portlet.bindings.UserAttributeBinding} and
 * {@link org.apache.tapestry.portlet.bindings.UserAttributeBindingFactory}.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class TestUserAttributeBinding extends HiveMindTestCase
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
        MockControl control = newControl(Map.class);
        Map map = (Map) control.getMock();

        map.get(key);
        control.setReturnValue(value);

        return map;
    }

    private ValueConverter newConverter()
    {
        return (ValueConverter) newMock(ValueConverter.class);
    }

    private PortletRequest newRequest(Map userInfo)
    {
        MockControl control = newControl(PortletRequest.class);
        PortletRequest request = (PortletRequest) control.getMock();

        request.getAttribute(PortletRequest.USER_INFO);
        control.setReturnValue(userInfo);

        return request;
    }

    public void testGetObject()
    {
        Map map = newMap("foo.bar", "baz");
        ValueConverter vc = newConverter();
        PortletRequest request = newRequest(map);
        Location l = newLocation();

        replayControls();

        IBinding b = newBinding("description", vc, l, request, "foo.bar");

        assertSame(l, b.getLocation());
        assertEquals("description", b.getDescription());
        assertEquals(false, b.isInvariant());
        assertEquals("baz", b.getObject());

        verifyControls();
    }

    public void testGetObjectNoUserInfo()
    {
        ValueConverter vc = newConverter();
        PortletRequest request = newRequest(null);
        Location l = newLocation();

        replayControls();

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

        verifyControls();
    }

    public void testSetObject()
    {
        Object newValue = new Object();
        String valueConverted = "CONVERTED";

        Map map = new HashMap();

        MockControl converterc = newControl(ValueConverter.class);
        ValueConverter converter = (ValueConverter) converterc.getMock();
        PortletRequest request = newRequest(map);
        Location l = newLocation();

        converter.coerceValue(newValue, String.class);
        converterc.setReturnValue(valueConverted);

        replayControls();

        IBinding b = newBinding("description", converter, l, request, "foo.bar");

        b.setObject(newValue);

        assertSame(valueConverted, map.get("foo.bar"));

        verifyControls();
    }

}
