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

package org.apache.tapestry.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.hivemind.Resource;
import org.apache.hivemind.lib.chain.ChainBuilder;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.INamespace;
import org.apache.tapestry.engine.IPropertySource;
import org.apache.tapestry.event.ResetEventListener;
import org.apache.tapestry.services.ComponentPropertySource;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.PropertyHolderPropertySource;

/**
 * TODO: Figure out a testing strategy for this beast!
 * 
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
public class ComponentPropertySourceImpl implements ComponentPropertySource, ResetEventListener
{
    private IPropertySource _globalProperties;

    private ChainBuilder _chainBuilder;

    private Map _componentSources = new HashMap();

    private Map _localizedComponentSources = new HashMap();

    private Map _namespaceSources = new HashMap();

    private Map _localizedNamespaceSources = new HashMap();

    public synchronized void resetEventDidOccur()
    {
        _componentSources.clear();
        _localizedComponentSources.clear();
        _namespaceSources.clear();
        _localizedNamespaceSources.clear();
    }

    private synchronized IPropertySource getSourceForNamespace(INamespace namespace)
    {
        Resource key = namespace.getSpecificationLocation();

        IPropertySource result = (IPropertySource) _namespaceSources.get(key);

        if (result == null)
        {
            result = createSourceForNamespace(namespace);
            _namespaceSources.put(key, result);
        }

        return result;
    }

    private synchronized IPropertySource getSourceForComponent(IComponent component)
    {
        Resource key = component.getSpecification().getSpecificationLocation();

        IPropertySource result = (IPropertySource) _componentSources.get(key);

        if (result == null)
        {
            result = createSourceForComponent(component);
            _componentSources.put(key, result);
        }

        return result;
    }

    private synchronized LocalizedPropertySource getLocalizedSourceForComponent(IComponent component)
    {
        Resource key = component.getSpecification().getSpecificationLocation();

        LocalizedPropertySource result = (LocalizedPropertySource) _localizedComponentSources
                .get(key);

        if (result == null)
        {
            result = new LocalizedPropertySource(getSourceForComponent(component));

            _localizedComponentSources.put(key, result);
        }

        return result;
    }

    private synchronized LocalizedPropertySource getLocalizedSourceForNamespace(INamespace namespace)
    {
        Resource key = namespace.getSpecificationLocation();

        LocalizedPropertySource result = (LocalizedPropertySource) _localizedNamespaceSources
                .get(key);

        if (result == null)
        {
            result = new LocalizedPropertySource(getSourceForNamespace(namespace));

            _localizedNamespaceSources.put(key, result);
        }

        return result;
    }

    private IPropertySource createSourceForComponent(IComponent component)
    {
        IComponentSpecification specification = component.getSpecification();

        List sources = new ArrayList();

        sources.add(new PropertyHolderPropertySource(specification));
        sources.add(getSourceForNamespace(component.getNamespace()));

        return (IPropertySource) _chainBuilder.buildImplementation(
                IPropertySource.class,
                sources,
                ImplMessages.componentPropertySourceDescription(specification));
    }

    private IPropertySource createSourceForNamespace(INamespace namespace)
    {
        List sources = new ArrayList();

        sources.add(new PropertyHolderPropertySource(namespace.getSpecification()));
        sources.add(_globalProperties);

        return (IPropertySource) _chainBuilder.buildImplementation(
                IPropertySource.class,
                sources,
                ImplMessages.namespacePropertySourceDescription(namespace));
    }

    public String getComponentProperty(IComponent component, String propertyName)
    {
        return getSourceForComponent(component).getPropertyValue(propertyName);
    }

    public String getLocalizedComponentProperty(IComponent component, Locale locale,
            String propertyName)
    {
        return getLocalizedSourceForComponent(component).getPropertyValue(propertyName, locale);
    }

    public String getNamespaceProperty(INamespace namespace, String propertyName)
    {
        return getSourceForNamespace(namespace).getPropertyValue(propertyName);
    }

    public String getLocalizedNamespaceProperty(INamespace namespace, Locale locale,
            String propertyName)
    {
        return getLocalizedSourceForNamespace(namespace).getPropertyValue(propertyName, locale);
    }

    public void setChainBuilder(ChainBuilder chainBuilder)
    {
        _chainBuilder = chainBuilder;
    }

    public void setGlobalProperties(IPropertySource globalProperties)
    {
        _globalProperties = globalProperties;
    }
}