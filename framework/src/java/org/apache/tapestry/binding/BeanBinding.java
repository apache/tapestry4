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

import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Binding whose value is a named bean provided by a component.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class BeanBinding extends AbstractBinding
{
    private final IComponent _component;

    private final String _beanName;

    public BeanBinding(String description, ValueConverter valueConverter, Location location,
            IComponent component, String beanName)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(beanName, "beanName");

        _component = component;
        _beanName = beanName;
    }

    public Object getComponent()
    {
        return _component;
    }

    public Object getObject()
    {
        return _component.getBeans().getBean(_beanName);
    }

}