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

import org.apache.hivemind.Location;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.TypeConverter;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * A binding that connects directly to a localized string for a component.
 * <p>
 * This name is historical, and probably should be renamed to <code>MessageBinding</code>.
 * 
 * @see IComponent#getString(String)
 * @author Howard Lewis Ship
 * @since 2.0.4
 */

public class StringBinding extends AbstractBinding
{
    private IComponent _component;

    private String _key;

    public StringBinding(IComponent component, String parameterName, String key,
            ValueConverter valueConverter, Location location)
    {
        super(parameterName, valueConverter, location);

        _component = component;
        _key = key;
    }

    public Object getComponent()
    {
        return _component;
    }

    public String getKey()
    {
        return _key;
    }

    /**
     * Accesses the specified localized string. Never returns null.
     */

    public Object getObject()
    {
        return _component.getMessages().getMessage(_key);
    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer("StringBinding");
        buffer.append('[');
        buffer.append(_component.getExtendedId());
        buffer.append(' ');
        buffer.append(_key);
        buffer.append(']');

        return buffer.toString();
    }
}