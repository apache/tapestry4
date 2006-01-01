// Copyright 2004, 2005, 2006 The Apache Software Foundation
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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * A binding where the path is the id of a child component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class ComponentBinding extends AbstractBinding
{
    private final IComponent _component;

    private final String _componentId;

    public ComponentBinding(String description, ValueConverter valueConverter, Location location,
            IComponent component, String componentId)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(componentId, "componentId");

        _component = component;
        _componentId = componentId;
    }

    public Object getObject()
    {
        try
        {
            return _component.getComponent(_componentId);
        }
        catch (Exception ex)
        {
            throw new ApplicationRuntimeException(ex.getMessage(), getLocation(), ex);
        }
    }

    public Object getComponent()
    {
        return _component;
    }

}