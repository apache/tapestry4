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
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Stores a static (invariant) string as the value.
 * <p>
 * Note that the name exists for historical reasons; a better name would be
 * <code>LiteralBinding</code>.
 * 
 * @author Howard Lewis Ship
 */

public class StaticBinding extends AbstractBinding
{
    private String _value;

    public StaticBinding(String parameterName, String value, ValueConverter valueConverter,
            Location location)
    {
        super(parameterName, valueConverter, location);

        _value = value;
    }

    public Object getObject()
    {
        return _value;
    }

    public String toString()
    {
        return "StaticBinding[" + _value + "]";
    }
}