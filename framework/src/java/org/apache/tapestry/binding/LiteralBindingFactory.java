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
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * Implementation of {@link org.apache.tapestry.binding.BindingFactory}that constructs
 * {@link org.apache.tapestry.binding.LiteralBinding}instances.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class LiteralBindingFactory extends AbstractBindingFactory
{

    public IBinding createBinding(IComponent root, String description, String path,
            Location location)
    {
        return new LiteralBinding(description, getValueConverter(), location, path);
    }

}