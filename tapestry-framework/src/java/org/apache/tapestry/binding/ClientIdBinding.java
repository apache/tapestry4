// Copyright 2006 The Apache Software Foundation
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
import org.apache.tapestry.binding.AbstractBinding;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * A binding returning the client-side id of a component.
 */
public class ClientIdBinding extends AbstractBinding
{
    private final IComponent _component;

    private final String _componentId;

    public ClientIdBinding(String description, ValueConverter valueConverter, Location location,
            IComponent component, String componentId)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(componentId, "componentId");

        _component = component;
        _componentId = componentId;
    }

    /**
     * The client-side id of a component. Null is returned of the component in question 
     * hasn't already rendered.
     */
    public Object getObject()
    {
        try
        {            
            return _component.getPage().getRequestCycle().getAttribute("org.apache.tapestry.lastId." 
                    + _componentId);
            // could try checking for null here and returning the following
            // but this still isn't really correct.
            // return _component.getComponent(_componentId).getClientId();
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
        
    /**
     * Not invariant 'cause the same component might have multiple client-side ids 
     * when rendered more than once.
     */
    public boolean isInvariant()
    {
        return false;
    }

}
