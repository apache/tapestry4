// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.Defense;
import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ListenerMethodBinding extends AbstractBinding
{
    private final IComponent _component;

    private final String _methodName;

    public ListenerMethodBinding(IComponent component, String methodName, String description,
            ValueConverter valueConverter, Location location)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(methodName, "methodName");

        _component = component;
        _methodName = methodName;
    }

    public Object getComponent()
    {
        return _component;
    }

    public Object getObject()
    {
        return _component.getListeners().getListener(_methodName);
    }

}