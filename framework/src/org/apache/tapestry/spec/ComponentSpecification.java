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

package org.apache.tapestry.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.Tapestry;

/**
 *  A specification for a component, as read from an XML specification file.
 *
 *  <p>A specification consists of
 *  <ul>
 *  <li>An implementing class
 *  <li>An optional template
 *  <li>An optional description
 *  <li>A set of contained components
 *  <li>Bindings for the properties of each contained component
 *  <li>A set of named assets
 *  <li>Definitions for helper beans
 *  <li>Any reserved names (used for HTML attributes)
 *  </ul>
 *
 *  <p>From this information, an actual component may be instantiated and
 *  initialized.  Instantiating a component is usually a recursive process, since
 *  to initialize a container component, it is necessary to instantiate and initialize
 *  its contained components as well.
 *
 *  @see org.apache.tapestry.IComponent
 *  @see IContainedComponent
 *  @see org.apache.tapestry.engine.IPageLoader
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ComponentSpecification extends LocatablePropertyHolder implements IComponentSpecification
{
    private String _componentClassName; 

    /** @since 1.0.9 **/

    private String _description;

    /**
     *  Keyed on component id, value is {@link IContainedComponent}.
     *
     **/

    protected Map _components;

    /**
     *  Keyed on asset name, value is {@link IAssetSpecification}.
     *
     **/

    protected Map _assets;

    /**
     *  Defines all formal parameters.  Keyed on parameter name, value is
     * {@link IParameterSpecification}.
     *
     **/

    protected Map _parameters;

    /**
     *  Defines all helper beans.  Keyed on name, value is {@link IBeanSpecification}.
     *
     *  @since 1.0.4
     **/

    protected Map _beans;

    /**
     *  The names of all reserved informal parameter names (as lower-case).  This
     *  allows the page loader to filter out any informal parameters during page load,
     *  rather than during render.
     *
     *   @since 1.0.5
     *
     **/

    protected Set _reservedParameterNames;

    /**
     *  Is the component allowed to have a body (that is, wrap other elements?).
     *
     **/

    private boolean _allowBody = true;

    /**
     *  Is the component allow to have informal parameter specified.
     *
     **/

    private boolean _allowInformalParameters = true;

    /**
     *  The XML Public Id used when the page or component specification was read
     *  (if applicable).
     * 
     *  @since 2.2
     * 
     **/

    private String _publicId;

    /**
     *  Indicates that the specification is for a page, not a component.
     * 
     *  @since 2.2
     * 
     **/

    private boolean _pageSpecification;

    /**
     *  The location from which the specification was obtained.
     * 
     *  @since 3.0
     * 
     **/

    private IResourceLocation _specificationLocation;

    /**
     *  A Map of {@link IPropertySpecification} keyed on the name
     *  of the property.
     *
     *  @since 3.0
     * 
     **/

    private Map _propertySpecifications;

    /**
     * @throws IllegalArgumentException if the name already exists.
     *
     **/

    public void addAsset(String name, IAssetSpecification asset)
    {
        if (_assets == null)
            _assets = new HashMap();

        if (_assets.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.format("ComponentSpecification.duplicate-asset", this, name));

        _assets.put(name, asset);
    }

    /**
     *  @throws IllegalArgumentException if the id is already defined.
     *
     **/

    public void addComponent(String id, IContainedComponent component)
    {
        if (_components == null)
            _components = new HashMap();

        if (_components.containsKey(id))
            throw new IllegalArgumentException(
                Tapestry.format("ComponentSpecification.duplicate-component", this, id));

        _components.put(id, component);
    }

    /**
     *  Adds the parameter.   The name is added as a reserved name.
     *
     *  @throws IllegalArgumentException if the name already exists.
     **/

    public void addParameter(String name, IParameterSpecification spec)
    {
        if (_parameters == null)
            _parameters = new HashMap();

        if (_parameters.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.format("ComponentSpecification.duplicate-parameter", this, name));

        _parameters.put(name, spec);

        addReservedParameterName(name);
    }

    /**
     *  Returns true if the component is allowed to wrap other elements (static HTML
     *  or other components).  The default is true.
     *
     *  @see #setAllowBody(boolean)
     *
     **/

    public boolean getAllowBody()
    {
        return _allowBody;
    }

    /**
     *  Returns true if the component allows informal parameters (parameters
     *  not formally defined).  Informal parameters are generally used to create
     *  additional HTML attributes for an HTML tag rendered by the
     *  component.  This is often used to specify JavaScript event handlers or the class
     *  of the component (for Cascarding Style Sheets).
     *
     * <p>The default value is true.
     *
     *  @see #setAllowInformalParameters(boolean)
     **/

    public boolean getAllowInformalParameters()
    {
        return _allowInformalParameters;
    }

    /**
     *  Returns the {@link IAssetSpecification} with the given name, or null
     *  if no such specification exists.
     *
     *  @see #addAsset(String,IAssetSpecification)
     **/

    public IAssetSpecification getAsset(String name)
    {

        return (IAssetSpecification) get(_assets, name);
    }

    /**
     *  Returns a <code>List</code>
     *  of the String names of all assets, in alphabetical
     *  order
     *
     **/

    public List getAssetNames()
    {
        return sortedKeys(_assets);
    }

    /**
     *  Returns the specification of a contained component with the given id, or
     *  null if no such contained component exists.
     *
     *  @see #addComponent(String, IContainedComponent)
     *
     **/

    public IContainedComponent getComponent(String id)
    {
        return (IContainedComponent) get(_components, id);
    }

    public String getComponentClassName()
    {
        return _componentClassName;
    }

    /**
     *  Returns an <code>List</code>
     *  of the String names of the {@link IContainedComponent}s
     *  for this component.
     *
     *  @see #addComponent(String, IContainedComponent)
     *
     **/

    public List getComponentIds()
    {
        return sortedKeys(_components);
    }

    /**
     *  Returns the specification of a parameter with the given name, or
     *  null if no such parameter exists.
     *
     *  @see #addParameter(String, IParameterSpecification)
     *
     **/

    public IParameterSpecification getParameter(String name)
    {
        return (IParameterSpecification) get(_parameters, name);
    }

    /**
     *  Returns a List of
     *  of String names of all parameters.  This list
     *  is in alphabetical order.
     *
     *  @see #addParameter(String, IParameterSpecification)
     *
     **/

    public List getParameterNames()
    {
        return sortedKeys(_parameters);
    }

    public void setAllowBody(boolean value)
    {
        _allowBody = value;
    }

    public void setAllowInformalParameters(boolean value)
    {
        _allowInformalParameters = value;
    }

    public void setComponentClassName(String value)
    {
        _componentClassName = value;
    }

    /**
     *  @since 1.0.4
     *
     *  @throws IllegalArgumentException if the bean already has a specification.
     **/

    public void addBeanSpecification(String name, IBeanSpecification specification)
    {
        if (_beans == null)
            _beans = new HashMap();

        else
            if (_beans.containsKey(name))
                throw new IllegalArgumentException(
                    Tapestry.format("ComponentSpecification.duplicate-bean", this, name));

        _beans.put(name, specification);
    }

    /**
     * Returns the {@link IBeanSpecification} for the given name, or null
     * if not such specification exists.
     *
     * @since 1.0.4
     *
     **/

    public IBeanSpecification getBeanSpecification(String name)
    {
        if (_beans == null)
            return null;

        return (IBeanSpecification) _beans.get(name);
    }

    /**
     *  Returns an unmodifiable collection of the names of all beans.
     *
     **/

    public Collection getBeanNames()
    {
        if (_beans == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableCollection(_beans.keySet());
    }

    /**
     *  Adds the value as a reserved name.  Reserved names are not allowed
     *  as the names of informal parameters.  Since the comparison is
     *  caseless, the value is converted to lowercase before being
     *  stored.
     *
     *  @since 1.0.5
     *
     **/

    public void addReservedParameterName(String value)
    {
        if (_reservedParameterNames == null)
            _reservedParameterNames = new HashSet();

        _reservedParameterNames.add(value.toLowerCase());
    }

    /**
     *  Returns true if the value specified is in the reserved name list.
     *  The comparison is caseless.  All formal parameters are automatically
     *  in the reserved name list, as well as any additional
     *  reserved names specified in the component specification.  The latter
     *  refer to HTML attributes generated directly by the component.
     *
     *  @since 1.0.5
     *
     **/

    public boolean isReservedParameterName(String value)
    {
        if (_reservedParameterNames == null)
            return false;

        return _reservedParameterNames.contains(value.toLowerCase());
    }

    public String toString()
    {
        ToStringBuilder builder = new ToStringBuilder(this);

        builder.append("componentClassName", _componentClassName);
        builder.append("pageSpecification", _pageSpecification);
        builder.append("specificationLocation", _specificationLocation);
        builder.append("allowBody", _allowBody);
        builder.append("allowInformalParameter", _allowInformalParameters);

        return builder.toString();
    }

    /**
     *  Returns the documentation for this component.
     * 
     *  @since 1.0.9
     **/

    public String getDescription()
    {
        return _description;
    }

    /**
     *  Sets the documentation for this component.
     * 
     *  @since 1.0.9
     **/

    public void setDescription(String description)
    {
        _description = description;
    }

    /**
     *  Returns the XML Public Id for the specification file, or null
     *  if not applicable.
     * 
     *  <p>
     *  This method exists as a convienience for the Spindle plugin.
     *  A previous method used an arbitrary version string, the
     *  public id is more useful and less ambiguous.
     *  
     *  @since 2.2
     * 
     **/

    public String getPublicId()
    {
        return _publicId;
    }

    /** @since 2.2 **/

    public void setPublicId(String publicId)
    {
        _publicId = publicId;
    }

    /** 
     * 
     *  Returns true if the specification is known to be a page
     *  specification and not a component specification.  Earlier versions
     *  of the framework did not distinguish between the two, but starting
     *  in 2.2, there are seperate XML entities for pages and components.
     *  Pages omit several attributes and entities related
     *  to parameters, as parameters only make sense for components.
     *  
     *  @since 2.2 
     * 
     **/

    public boolean isPageSpecification()
    {
        return _pageSpecification;
    }

    /** @since 2.2 **/

    public void setPageSpecification(boolean pageSpecification)
    {
        _pageSpecification = pageSpecification;
    }

    /** @since 2.2 **/

    private List sortedKeys(Map input)
    {
        if (input == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(input.keySet());

        Collections.sort(result);

        return result;
    }

    /** @since 2.2 **/

    private Object get(Map map, Object key)
    {
        if (map == null)
            return null;

        return map.get(key);
    }

    /** @since 3.0 **/

    public IResourceLocation getSpecificationLocation()
    {
        return _specificationLocation;
    }

    /** @since 3.0 **/

    public void setSpecificationLocation(IResourceLocation specificationLocation)
    {
        _specificationLocation = specificationLocation;
    }

    /**
     *  Adds a new property specification.  The name of the property must
     *  not already be defined (and must not change after being added).
     * 
     *  @since 3.0
     * 
     **/

    public void addPropertySpecification(IPropertySpecification spec)
    {
        if (_propertySpecifications == null)
            _propertySpecifications = new HashMap();

        String name = spec.getName();

        if (_propertySpecifications.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.format(
                    "ComponentSpecification.duplicate-property-specification",
                    this,
                    name));

        _propertySpecifications.put(name, spec);
    }

    /**
     *  Returns a sorted, immutable list of the names of all 
     *  {@link org.apache.tapestry.spec.IPropertySpecification}s.
     * 
     *  @since 3.0
     * 
     **/

    public List getPropertySpecificationNames()
    {
        return sortedKeys(_propertySpecifications);
    }

    /**
     *  Returns the named {@link org.apache.tapestry.spec.IPropertySpecification},
     *  or null  if no such specification exist.
     * 
     *  @since 3.0
     *  @see #addPropertySpecification(IPropertySpecification)
     * 
     **/

    public IPropertySpecification getPropertySpecification(String name)
    {
        return (IPropertySpecification) get(_propertySpecifications, name);
    }

}