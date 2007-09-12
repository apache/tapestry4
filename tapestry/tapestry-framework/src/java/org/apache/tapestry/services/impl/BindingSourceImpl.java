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

package org.apache.tapestry.services.impl;

import org.apache.hivemind.Location;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.binding.BindingFactory;
import org.apache.tapestry.binding.BindingSource;
import org.apache.tapestry.spec.IParameterSpecification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Implementation of the {@link BindingSource} service.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class BindingSourceImpl implements BindingSource
{
    private List _contributions;

    private Map _propertyMap;

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

    public IBinding createBinding(IComponent component, String description,
            String reference, String defaultBindingType, Location location)
    {
        return createBinding(component, null, description, reference, defaultBindingType, location);
    }

    public IBinding createBinding(IComponent component, IParameterSpecification parameter, String description,
                                  String reference, String defaultBindingType, Location location)
    {
        String prefix = null;
        String path = reference;
        BindingFactory factory = null;

        int colonx = reference.indexOf(':');

        if (colonx > -1)
        {
            String pathPrefix = reference.substring(0, colonx);

            if (_factoryMap.containsKey(pathPrefix))
            {
                prefix = pathPrefix;

                path = reference.substring(colonx + 1);
            }
        } else if (parameter != null && _propertyMap.containsKey(parameter.getParameterName()))
        {
            
            factory = (BindingFactory) _propertyMap.get(parameter.getParameterName());
        }

        if (prefix == null)
        {
            prefix = defaultBindingType;
        }

        if (factory == null)
        {
            factory = (BindingFactory) _factoryMap.get(prefix);
        }
        
        return factory.createBinding(component, description, path, location);
    }

    public void setContributions(List contributions)
    {
        _contributions = contributions;
    }

    public void setPropertyContributions(Map properties)
    {
        _propertyMap = properties;
    }
}
