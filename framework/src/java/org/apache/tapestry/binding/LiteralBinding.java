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
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Stores a static (invariant) string as the value.
 * 
 * <p>
 * Note: renamed from StaticBinding to LiteralBinding in release 3.1.
 * 
 * @author Howard Lewis Ship
 */

public class LiteralBinding extends AbstractBinding
{
    private final String _value;

    public LiteralBinding(String description, ValueConverter valueConverter, Location location,
            String value)
    {
        super(description, valueConverter, location);

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