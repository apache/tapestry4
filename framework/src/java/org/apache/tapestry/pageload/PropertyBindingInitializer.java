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

package org.apache.tapestry.pageload;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * Initializes (at finishLoad()) and re-initializes (on page detach) a property of a component using
 * a {@link org.apache.tapestry.IBinding}as the value source.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */

public class PropertyBindingInitializer implements PageDetachListener
{
    private IComponent _component;

    private String _propertyName;

    private IBinding _binding;

    private Class _propertyType;

    public PropertyBindingInitializer(IComponent component, String propertyName, IBinding binding)
    {
        Defense.notNull(component, "component");
        Defense.notNull(propertyName, "propertyName");
        Defense.notNull(binding, "binding");

        _component = component;
        _propertyName = propertyName;
        _binding = binding;

        _propertyType = PropertyUtils.getPropertyType(component, _propertyName);
    }

    public void pageDetached(PageEvent event)
    {

        try
        {
            Object value = _binding.getObject(_propertyType);

            PropertyUtils.write(_component, _propertyName, value);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(PageloadMessages.unableToInitializeProperty(
                    _propertyName,
                    _component,
                    ex), _component, _binding.getLocation(), ex);
        }
    }
}