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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.services.BindingFactory;
import org.apache.tapestry.services.BindingSource;

/**
 * Implementation of the <code>tapestry.bindings.BindingSource</code> service.
 * 
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class BindingSourceImpl implements BindingSource
{
    private List _contributions;

    private BindingFactory _literalBindingFactory;

    /**
     * Keyed on prefix, value is {@link BindingFactory}.
     */
    private Map _factoryMap = new HashMap();

    public void initializeService()
    {
        Iterator i = _contributions.iterator();

        while (i.hasNext())
        {
            BindingPrefixContribution c = (BindingPrefixContribution) i.next();

            _factoryMap.put(c.getPrefix(), c.getFactory());
        }
    }

    public IBinding createBinding(IComponent component, String bindingDescription, String locator,
            Location location)
    {
        BindingFactory factory = _literalBindingFactory;
        String path = locator;

        int colonx = locator.indexOf(':');

        if (colonx > 1)
        {
            String prefix = locator.substring(0, colonx);

            BindingFactory prefixedFactory = (BindingFactory) _factoryMap.get(prefix);

            if (prefixedFactory != null)
            {
                factory = prefixedFactory;
                path = locator.substring(colonx + 1);
            }
        }

        return factory.createBinding(component, bindingDescription, path, location);
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    public void setLiteralBindingFactory(BindingFactory literalBindingFactory)
    {
        _literalBindingFactory = literalBindingFactory;
    }
}