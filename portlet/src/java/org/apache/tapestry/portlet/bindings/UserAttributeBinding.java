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

import java.util.Map;

import javax.portlet.PortletRequest;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Allows access to a Portlet user attrbute.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class UserAttributeBinding extends AbstractBinding
{
    private PortletRequest _request;

    private String _attributeName;

    public UserAttributeBinding(String description, ValueConverter valueConverter,
            Location location, PortletRequest request, String attributeName)
    {
        super(description, valueConverter, location);

        Defense.notNull(request, "request");
        Defense.notNull(attributeName, "attributeName");

        _request = request;
        _attributeName = attributeName;
    }

    public boolean isInvariant()
    {
        // These can always be changed.
        return false;
    }

    private Map getUserInfo()
    {
        Map result = (Map) _request.getAttribute(PortletRequest.USER_INFO);

        if (result == null)
            throw new ApplicationRuntimeException(BindingsMessages.noUserInfo(), getLocation(),
                    null);

        return result;
    }

    public Object getObject()
    {
        return getUserInfo().get(_attributeName);
    }

    public void setObject(Object value)
    {
        String asString = (String) getValueConverter().coerceValue(value, String.class);

        getUserInfo().put(_attributeName, asString);
    }
}
