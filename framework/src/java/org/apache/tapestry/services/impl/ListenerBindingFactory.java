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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.ListenerMethodBinding;
import org.apache.tapestry.coerce.ValueConverter;
import org.apache.tapestry.services.BindingFactory;

/**
 * Factory of {@link org.apache.tapestry.binding.ListenerMethodBinding}, mapped to the "listener:"
 * prefix.
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ListenerBindingFactory implements BindingFactory
{
    private ValueConverter _valueConverter;

    public IBinding createBinding(IComponent root, String name, String path, Location location)
    {
        return new ListenerMethodBinding(root, path, name, _valueConverter, location);
    }

    public void setValueConverter(ValueConverter valueConverter)
    {
        _valueConverter = valueConverter;
    }
}