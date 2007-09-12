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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.Location;
import org.apache.hivemind.util.Defense;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.ComponentPropertySource;


/**
 * Simple implementation that allows injection / lookup of meta properties.
 */
public class MetaBinding extends AbstractBinding
{
    private final IComponent _component;

    private final String _key;

    private final ComponentPropertySource _propertySource;
    
    public MetaBinding(String description, ValueConverter valueConverter, Location location, IComponent component, 
            ComponentPropertySource propertySource, String key)
    {
        super(description, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(propertySource, "propertySource");
        Defense.notNull(key, "key");
        
        _component = component;
        _key = key;
        _propertySource = propertySource;
    }

    public Object getObject()
    {
        try
        {
            return _propertySource.getComponentProperty(_component, _key);
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
    
    public String toString()
    {
        StringBuffer buffer = new StringBuffer("MetaBinding");
        buffer.append('[');
        buffer.append(_component.getExtendedId());
        buffer.append(' ');
        buffer.append(_key);
        buffer.append(']');

        return buffer.toString();
    }
}
