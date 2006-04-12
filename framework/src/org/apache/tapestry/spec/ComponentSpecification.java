/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
 *  @see ContainedComponent
 *  @see org.apache.tapestry.IPageLoader
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class ComponentSpecification extends LocatablePropertyHolder
{
    private String _componentClassName;

    /** @since 1.0.9 **/

    private String _description;

    /**
     *  Keyed on component id, value is {@link ContainedComponent}.
     *
     **/

    protected Map _components;

    /**
     *  Keyed on asset name, value is {@link AssetSpecification}.
     *
     **/

    protected Map _assets;

    /**
     *  Defines all formal parameters.  Keyed on parameter name, value is
     * {@link ParameterSpecification}.
     *
     **/

    protected Map _parameters;

    /**
     *  Defines all helper beans.  Keyed on name, value is {@link BeanSpecification}.
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
     *  @since 2.4
     * 
     **/

    private IResourceLocation _specificationLocation;

    /**
     *  A Map of {@link PropertySpecification} keyed on the name
     *  of the property.
     *
     *  @since 2.4
     * 
     **/

    private Map _propertySpecifications;

    /**
     * @throws IllegalArgumentException if the name already exists.
     *
     **/

    public void addAsset(String name, AssetSpecification asset)
    {
        if (_assets == null)
            _assets = new HashMap();

        if (_assets.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString("ComponentSpecification.duplicate-asset", this, name));

        _assets.put(name, asset);
    }

    /**
     *  @throws IllegalArgumentException if the id is already defined.
     *
     **/

    public void addComponent(String id, ContainedComponent component)
    {
        if (_components == null)
            _components = new HashMap();

        if (_components.containsKey(id))
            throw new IllegalArgumentException(
                Tapestry.getString("ComponentSpecification.duplicate-component", this, id));

        _components.put(id, component);
    }

    /**
     *  Adds the parameter.   The name is added as a reserved name.
     *
     *  @throws IllegalArgumentException if the name already exists.
     **/

    public void addParameter(String name, ParameterSpecification spec)
    {
        if (_parameters == null)
            _parameters = new HashMap();

        if (_parameters.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString("ComponentSpecification.duplicate-parameter", this, name));

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
     *  Returns the {@link AssetSpecification} with the given name, or null
     *  if no such specification exists.
     *
     *  @see #addAsset(String,AssetSpecification)
     **/

    public AssetSpecification getAsset(String name)
    {

        return (AssetSpecification) get(_assets, name);
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
     *  @see #addComponent(String, ContainedComponent)
     *
     **/

    public ContainedComponent getComponent(String id)
    {
        return (ContainedComponent) get(_components, id);
    }

    public String getComponentClassName()
    {
        return _componentClassName;
    }

    /**
     *  Returns an <code>List</code>
     *  of the String names of the {@link ContainedComponent}s
     *  for this component.
     *
     *  @see #addComponent(String, ContainedComponent)
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
     *  @see #addParameter(String, ParameterSpecification)
     *
     **/

    public ParameterSpecification getParameter(String name)
    {
        return (ParameterSpecification) get(_parameters, name);
    }

    /**
     *  Returns a List of
     *  of String names of all parameters.  This list
     *  is in alphabetical order.
     *
     *  @see #addParameter(String, ParameterSpecification)
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

    public void addBeanSpecification(String name, BeanSpecification specification)
    {
        if (_beans == null)
            _beans = new HashMap();

        else
            if (_beans.containsKey(name))
                throw new IllegalArgumentException(
                    Tapestry.getString("ComponentSpecification.duplicate-bean", this, name));

        _beans.put(name, specification);
    }

    /**
     * Returns the {@link BeanSpecification} for the given name, or null
     * if not such specification exists.
     *
     * @since 1.0.4
     *
     **/

    public BeanSpecification getBeanSpecification(String name)
    {
        if (_beans == null)
            return null;

        return (BeanSpecification) _beans.get(name);
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

    /** @since 2.4 **/

    public IResourceLocation getSpecificationLocation()
    {
        return _specificationLocation;
    }

    /** @since 2.4 **/

    public void setSpecificationLocation(IResourceLocation specificationLocation)
    {
        _specificationLocation = specificationLocation;
    }

    /**
     *  Adds a new property specification.  The name of the property must
     *  not already be defined (and must not change after being added).
     * 
     *  @since 2.4
     * 
     **/

    public void addPropertySpecification(PropertySpecification spec)
    {
        if (_propertySpecifications == null)
            _propertySpecifications = new HashMap();

        String name = spec.getName();

        if (_propertySpecifications.containsKey(name))
            throw new IllegalArgumentException(
                Tapestry.getString(
                    "ComponentSpecification.duplicate-property-specification",
                    this,
                    name));

        _propertySpecifications.put(name, spec);
    }

    /**
     *  Returns a sorted, immutable list of the names of all 
     *  {@link org.apache.tapestry.spec.PropertySpecification}s.
     * 
     *  @since 2.4
     * 
     **/

    public List getPropertySpecificationNames()
    {
        return sortedKeys(_propertySpecifications);
    }

    /**
     *  Returns the named {@link org.apache.tapestry.spec.PropertySpecification},
     *  or null  if no such specification exist.
     * 
     *  @since 2.4
     *  @see #addPropertySpecification(PropertySpecification)
     * 
     **/

    public PropertySpecification getPropertySpecification(String name)
    {
        return (PropertySpecification) get(_propertySpecifications, name);
    }

}