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

import javax.portlet.PortletRequest;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.AbstractBindingFactory;
import org.apache.tapestry.binding.BindingFactory;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.form.validator.AbstractValidatorWrapper;

/**
 * Factory used to create {@link org.apache.tapestry.portlet.bindings.UserAttributeBinding}s.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class UserAttributeBindingFactory extends AbstractBindingFactory
{
    private PortletRequest _request;

    /**
     * Interprets the path as the Portlet user attribute name.
     */

    public IBinding createBinding(IComponent root, String bindingDescription, String expression,
            Location location)
    {
        return new UserAttributeBinding(bindingDescription, getValueConverter(), location,
                _request, expression);
    }

    public void setRequest(PortletRequest request)
    {
        _request = request;
    }
}
