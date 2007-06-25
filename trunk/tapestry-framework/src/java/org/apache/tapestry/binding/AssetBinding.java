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
import org.apache.tapestry.BindingException;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.coerce.ValueConverter;

/**
 * A binding that is connected to an asset provided by a component.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class AssetBinding extends AbstractBinding
{
    private final IComponent _component;

    public AssetBinding(String description, ValueConverter valueConverter, Location location,
            IComponent component, String assetName)
    {
        super(assetName, valueConverter, location);

        Defense.notNull(component, "component");
        Defense.notNull(assetName, "assetName");

        _component = component;
    }

    public Object getObject()
    {
        IAsset result = _component.getAsset(_description);

        if (result == null)
            throw new BindingException(BindingMessages.missingAsset(_component, _description),
                    _component, getLocation(), this, null);

        return result;
    }

    public Object getComponent()
    {
        return _component;
    }
}
