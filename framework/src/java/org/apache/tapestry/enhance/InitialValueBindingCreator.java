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

package org.apache.tapestry.enhance;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingSource;

/**
 * Encapsulates information needed to construct an initial value binding for a specified property
 * (with an initial value).
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class InitialValueBindingCreator
{
    private BindingSource _bindingSource;

    private String _description;

    private String _initialValue;

    private Location _location;

    /**
     * This method is just implemented for testing purposes.
     */

    public boolean equals(Object obj)
    {
        InitialValueBindingCreator c = (InitialValueBindingCreator) obj;

        return _bindingSource == c._bindingSource && _description.equals(c._description)
                && _initialValue.equals(c._initialValue) && _location.equals(c._location);
    }

    public InitialValueBindingCreator(BindingSource bindingSource, String description,
            String initialValue, Location location)
    {
        _bindingSource = bindingSource;
        _description = description;
        _initialValue = initialValue;
        _location = location;
    }

    public IBinding createBinding(IComponent component)
    {
        return _bindingSource.createBinding(component, _description, _initialValue, _location);
    }
}