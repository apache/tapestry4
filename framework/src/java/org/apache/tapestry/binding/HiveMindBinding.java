// Copyright 2005 The Apache Software Foundation
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
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.InjectedValueProvider;

/**
 * A binding that accesses a HiveMind object. This is similar to injecting a HiveMind object as a
 * property and referencing that property.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class HiveMindBinding extends AbstractBinding
{
    private String _objectReference;

    private InjectedValueProvider _provider;

    public HiveMindBinding(String description, ValueConverter valueConverter, Location location,
            String objectReference, InjectedValueProvider provider)
    {
        super(description, valueConverter, location);

        Defense.notNull(objectReference, "objectReference");
        Defense.notNull(provider, "provider");

        _objectReference = objectReference;
        _provider = provider;
    }

    public Object getObject()
    {
        return _provider.obtainValue(_objectReference, getLocation());
    }

}