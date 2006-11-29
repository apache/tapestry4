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
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * An implementation of Tapestry {@link IBinding} that simplifies 
 * access to a component's clientId.<br/>
 * You can use this binding in order to safely find out the 
 * client-side id that was (or will be) generated for a given 
 * component.
 * <p/>
 * Here's an example of how to get the client id of a Tapestry component:
 * <code>
 * &lt;span jwcid="@Any" dojoType="myDojoComponent" contentId="clientId:content" /&gt;
 * </code>
 * <p/>
 * The previous code can also be written using the ognl binding:
 * <code>
 * &lt;span jwcid="@Any" dojoType="myDojoComponent" contentId="ognl:components.content.clientId" /&gt;
 * </code>
 * but the clientId binding is more compact and performs much faster.
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

    public Object getObject()
    {
        try
        {
            return _component.getComponent(_componentId).getClientId();
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
        
    public boolean isInvariant()
    {
        // clientId can change even for the same component, so ...
        return false;
    }

}
