//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.contrib.inspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.spec.IBeanSpecification;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;
import org.apache.tapestry.spec.IParameterSpecification;

/**
 *  Component of the {@link Inspector} page used to display
 *  the specification, parameters and bindings and assets of the inspected component.
 *
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class ShowSpecification extends BaseComponent implements PageRenderListener
{
    private IComponent _inspectedComponent;
    private IComponentSpecification _inspectedSpecification;
    private String _parameterName;
    private String _assetName;
    private List _sortedComponents;
    private IComponent _component;
    private List _assetNames;
    private List _formalParameterNames;
    private List _informalParameterNames;
    private List _sortedPropertyNames;
    private String _propertyName;
    private List _beanNames;
    private String _beanName;
    private IBeanSpecification _beanSpecification;

    private static class ComponentComparitor implements Comparator
    {
        public int compare(Object left, Object right)
        {
            IComponent leftComponent;
            String leftId;
            IComponent rightComponent;
            String rightId;

            if (left == right)
                return 0;

            leftComponent = (IComponent) left;
            rightComponent = (IComponent) right;

            leftId = leftComponent.getId();
            rightId = rightComponent.getId();

            return leftId.compareTo(rightId);
        }
    }

    /**
     *  Clears all cached information about the component and such after
     *  each render (including the rewind phase render used to process
     *  the tab view).
     *
     *  @since 1.0.5
     *
     **/

    public void pageEndRender(PageEvent event)
    {
        _inspectedComponent = null;
        _inspectedSpecification = null;
        _parameterName = null;
        _assetName = null;
        _sortedComponents = null;
        _component = null;
        _assetNames = null;
        _formalParameterNames = null;
        _informalParameterNames = null;
        _sortedPropertyNames = null;
        _propertyName = null;
        _beanNames = null;
        _beanName = null;
        _beanSpecification = null;
    }

    /**
     *  Gets the inspected component and specification from the {@link Inspector} page.
     *
     *  @since 1.0.5
     **/

    public void pageBeginRender(PageEvent event)
    {
        Inspector inspector = (Inspector) getPage();

        _inspectedComponent = inspector.getInspectedComponent();
        _inspectedSpecification = _inspectedComponent.getSpecification();
    }

    public IComponent getInspectedComponent()
    {
        return _inspectedComponent;
    }

    public IComponentSpecification getInspectedSpecification()
    {
        return _inspectedSpecification;
    }

    /**
     *  Returns a sorted list of formal parameter names.
     *
     **/

    public List getFormalParameterNames()
    {
        if (_formalParameterNames == null)
            _formalParameterNames = sort(_inspectedSpecification.getParameterNames());

        return _formalParameterNames;
    }

    /**
     *  Returns a sorted list of informal parameter names.  This is
     *  the list of all bindings, with the list of parameter names removed,
     *  sorted.
     *
     **/

    public List getInformalParameterNames()
    {
        if (_informalParameterNames != null)
            return _informalParameterNames;

        Collection names = _inspectedComponent.getBindingNames();
        if (names != null && names.size() > 0)
        {
            _informalParameterNames = new ArrayList(names);

            // Remove the names of any formal parameters.  This leaves
            // just the names of informal parameters (informal parameters
            // are any parameters/bindings that don't match a formal parameter
            // name).

            names = _inspectedSpecification.getParameterNames();
            if (names != null)
                _informalParameterNames.removeAll(names);

            Collections.sort(_informalParameterNames);
        }

        return _informalParameterNames;
    }

    public String getParameterName()
    {
        return _parameterName;
    }

    public void setParameterName(String value)
    {
        _parameterName = value;
    }

    /**
     *  Returns the {@link org.apache.tapestry.spec.ParameterSpecification} corresponding to
     *  the value of the parameterName property.
     *
     **/

    public IParameterSpecification getParameterSpecification()
    {
        return _inspectedSpecification.getParameter(_parameterName);
    }

    /**
     *  Returns the {@link IBinding} corresponding to the value of
     *  the parameterName property.
     *
     **/

    public IBinding getBinding()
    {
        return _inspectedComponent.getBinding(_parameterName);
    }

    public void setAssetName(String value)
    {
        _assetName = value;
    }

    public String getAssetName()
    {
        return _assetName;
    }

    /**
     *  Returns the {@link IAsset} corresponding to the value
     *  of the assetName property.
     *
     **/

    public IAsset getAsset()
    {
        return (IAsset) _inspectedComponent.getAssets().get(_assetName);
    }

    /**
     *  Returns a sorted list of asset names, or null if the
     *  component contains no assets.
     *
     **/

    public List getAssetNames()
    {
        if (_assetNames == null)
            _assetNames = sort(_inspectedComponent.getAssets().keySet());

        return _assetNames;
    }

    public List getSortedComponents()
    {
        if (_sortedComponents != null)
            return _sortedComponents;

        Inspector inspector = (Inspector) getPage();
        IComponent inspectedComponent = inspector.getInspectedComponent();

        // Get a Map of the components and simply return null if there
        // are none.

        Map components = inspectedComponent.getComponents();

        _sortedComponents = new ArrayList(components.values());

        Collections.sort(_sortedComponents, new ComponentComparitor());

        return _sortedComponents;
    }

    public void setComponent(IComponent value)
    {
        _component = value;
    }

    public IComponent getComponent()
    {
        return _component;
    }

    /**
     *  Returns the type of the component, as specified in the container's
     *  specification (i.e., the component alias if known).
     *
     **/

    public String getComponentType()
    {
        IComponent container = _component.getContainer();

        IComponentSpecification containerSpecification = container.getSpecification();

        String id = _component.getId();
        IContainedComponent contained = containerSpecification.getComponent(id);

        // Temporary:  An implicit component will not be in the containing
        // component's specification as a ContainedComponent.

        if (contained == null)
            return null;

        return contained.getType();
    }

    /**
     *  Returns a list of the properties for the component
     *  (from its specification), or null if the component
     *  has no properties.
     *
     **/

    public List getSortedPropertyNames()
    {
        if (_sortedPropertyNames == null)
            _sortedPropertyNames = sort(_inspectedSpecification.getPropertyNames());

        return _sortedPropertyNames;
    }

    public void setPropertyName(String value)
    {
        _propertyName = value;
    }

    public String getPropertyName()
    {
        return _propertyName;
    }

    public String getPropertyValue()
    {
        return _inspectedSpecification.getProperty(_propertyName);
    }

    public List getBeanNames()
    {
        if (_beanNames == null)
            _beanNames = sort(_inspectedSpecification.getBeanNames());

        return _beanNames;
    }

    public void setBeanName(String value)
    {
        _beanName = value;
        _beanSpecification = _inspectedSpecification.getBeanSpecification(_beanName);
    }

    public String getBeanName()
    {
        return _beanName;
    }

    public IBeanSpecification getBeanSpecification()
    {
        return _beanSpecification;
    }

    private List sort(Collection c)
    {
        if (c == null || c.size() == 0)
            return null;

        List result = new ArrayList(c);

        Collections.sort(result);

        return result;
    }
}