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

package org.apache.tapestry.spec;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.HiveMind;
import org.apache.hivemind.Resource;
import org.apache.hivemind.util.ToStringBuilder;
import org.apache.tapestry.event.BrowserEvent;
import org.apache.tapestry.internal.event.ComponentEventProperty;
import org.apache.tapestry.internal.event.EventBoundListener;

/**
 * A specification for a component, as read from an XML specification file.
 * <p>
 * A specification consists of
 * <ul>
 * <li>An implementing class
 * <li>An optional description
 * <li>A set of contained components
 * <li>Bindings for the properties of each contained component
 * <li>A set of named assets
 * <li>Definitions for managed beans
 * <li>Any reserved names (used for HTML attributes)
 * <li>Declared properties
 * <li>Property injections
 * </ul>
 * <p>
 * From this information, an actual component may be instantiated and initialized. Instantiating a
 * component is usually a recursive process, since to initialize a container component, it is
 * necessary to instantiate and initialize its contained components as well.
 * 
 * @see org.apache.tapestry.IComponent
 * @see IContainedComponent
 * @see org.apache.tapestry.engine.IPageLoader
 * @author Howard Lewis Ship
 */

public class ComponentSpecification extends LocatablePropertyHolder implements
        IComponentSpecification
{
    /**
     * Keyed on component id, value is {@link IContainedComponent}.
     */

    protected Map _components;

    /**
     * Keyed on asset name, value is {@link IAssetSpecification}.
     */

    protected Map _assets;

    /**
     * Defines all formal parameters. Keyed on parameter name, value is
     * {@link IParameterSpecification}.
     */

    protected Map _parameters;

    /**
     * Defines all helper beans. Keyed on name, value is {@link IBeanSpecification}.
     * 
     * @since 1.0.4
     */

    protected Map _beans;

    /**
     * The names of all reserved informal parameter names (as lower-case). This allows the page
     * loader to filter out any informal parameters during page load, rather than during render.
     * 
     * @since 1.0.5
     */

    protected Set _reservedParameterNames;

    private String _componentClassName;

    /** @since 1.0.9 * */

    private String _description;
    
    /**
     * Is the component allowed to have a body (that is, wrap other elements?).
     */

    private boolean _allowBody = true;

    /**
     * Is the component allow to have informal parameter specified.
     */

    private boolean _allowInformalParameters = true;

    /**
     * The XML Public Id used when the page or component specification was read (if applicable).
     * 
     * @since 2.2
     */

    private String _publicId;

    /**
     * Indicates that the specification is for a page, not a component.
     * 
     * @since 2.2
     */

    private boolean _pageSpecification;

    /**
     * The location from which the specification was obtained.
     * 
     * @since 3.0
     */

    private Resource _specificationLocation;

    /**
     * A Map of {@link IPropertySpecification}keyed on the name of the property.
     * 
     * @since 3.0
     */

    private Map _propertySpecifications;

    /**
     * List of {@link InjectSpecification}.
     * 
     * @since 4.0
     */

    private List _injectSpecifications;

    /**
     * Keyed on property name, value is some other object (such as an IAssetSpecification) that has
     * claimed a property of the page.
     * 
     * @since 4.0
     */

    private Map _claimedProperties;

    /**
     * @since 4.0
     */

    private boolean _deprecated = false;
    
    private Map _componentEvents = new HashMap();
    
    private Map _elementEvents = new HashMap();
    
    /**
     * @throws ApplicationRuntimeException
     *             if the name already exists.
     */

    public void addAsset(String name, IAssetSpecification asset)
    {
        if (_assets == null)
            _assets = new HashMap();

        IAssetSpecification existing = (IAssetSpecification) _assets.get(name);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.duplicateAsset(name, existing),
                    asset.getLocation(), null);

        claimProperty(asset.getPropertyName(), asset);

        _assets.put(name, asset);

    }

    /**
     * @throws ApplicationRuntimeException
     *             if the id is already defined.
     */

    public void addComponent(String id, IContainedComponent component)
    {
        if (_components == null)
            _components = new HashMap();

        IContainedComponent existing = (IContainedComponent) _components.get(id);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.duplicateComponent(id, existing),
                    component.getLocation(), null);

        _components.put(id, component);

        claimProperty(component.getPropertyName(), component);
    }

    /**
     * Adds the parameter. The name is added as a reserved name.
     * 
     * @throws ApplicationRuntimeException
     *             if the name already exists.
     */

    public void addParameter(IParameterSpecification spec)
    {
        if (_parameters == null)
            _parameters = new HashMap();

        String name = spec.getParameterName();

        addParameterByName(name, spec);

        Iterator i = spec.getAliasNames().iterator();
        while (i.hasNext())
        {
            String alias = (String) i.next();

            addParameterByName(alias, spec);
        }

        claimProperty(spec.getPropertyName(), spec);
    }

    private void addParameterByName(String name, IParameterSpecification spec)
    {
        IParameterSpecification existing = (IParameterSpecification) _parameters.get(name);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.duplicateParameter(name, existing),
                    spec.getLocation(), null);

        _parameters.put(name, spec);

        addReservedParameterName(name);
    }

    /**
     * Returns true if the component is allowed to wrap other elements (static HTML or other
     * components). The default is true.
     * 
     * @see #setAllowBody(boolean)
     */

    public boolean getAllowBody()
    {
        return _allowBody;
    }

    /**
     * Returns true if the component allows informal parameters (parameters not formally defined).
     * Informal parameters are generally used to create additional HTML attributes for an HTML tag
     * rendered by the component. This is often used to specify JavaScript event handlers or the
     * class of the component (for Cascarding Style Sheets).
     * <p>
     * The default value is true.
     * 
     * @see #setAllowInformalParameters(boolean)
     */

    public boolean getAllowInformalParameters()
    {
        return _allowInformalParameters;
    }

    /**
     * Returns the {@link IAssetSpecification}with the given name, or null if no such specification
     * exists.
     * 
     * @see #addAsset(String,IAssetSpecification)
     */

    public IAssetSpecification getAsset(String name)
    {

        return (IAssetSpecification) get(_assets, name);
    }

    /**
     * Returns a <code>List</code> of the String names of all assets, in alphabetical order.
     */

    public List getAssetNames()
    {
        return sortedKeys(_assets);
    }

    /**
     * Returns the specification of a contained component with the given id, or null if no such
     * contained component exists.
     * 
     * @see #addComponent(String, IContainedComponent)
     */

    public IContainedComponent getComponent(String id)
    {
        return (IContainedComponent) get(_components, id);
    }

    public String getComponentClassName()
    {
        return _componentClassName;
    }

    /**
     * Returns an <code>List</code> of the String names of the {@link IContainedComponent}s for
     * this component.
     * 
     * @see #addComponent(String, IContainedComponent)
     */

    public List getComponentIds()
    {
        return sortedKeys(_components);
    }

    /**
     * Returns the specification of a parameter with the given name, or null if no such parameter
     * exists.
     * 
     * @see #addParameterByName(String, IParameterSpecification)
     */

    public IParameterSpecification getParameter(String name)
    {
        return (IParameterSpecification) get(_parameters, name);
    }

    public Collection getRequiredParameters()
    {
        if (_parameters == null)
            return Collections.EMPTY_LIST;

        Collection result = new ArrayList();

        Iterator i = _parameters.entrySet().iterator();
        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();
            IParameterSpecification spec = (IParameterSpecification) entry.getValue();

            if (!spec.isRequired())
                continue;

            if (!name.equals(spec.getParameterName()))
                continue;

            result.add(spec);
        }

        return result;
    }

    /**
     * Returns a List of of String names of all parameters. This list is in alphabetical order.
     * 
     * @see #addParameterByName(String, IParameterSpecification)
     */

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
     * @since 1.0.4
     * @throws ApplicationRuntimeException
     *             if the bean already has a specification.
     */

    public void addBeanSpecification(String name, IBeanSpecification specification)
    {
        if (_beans == null)
            _beans = new HashMap();

        IBeanSpecification existing = (IBeanSpecification) _beans.get(name);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.duplicateBean(name, existing),
                    specification.getLocation(), null);

        claimProperty(specification.getPropertyName(), specification);

        _beans.put(name, specification);
    }

    /**
     * Returns the {@link IBeanSpecification}for the given name, or null if not such specification
     * exists.
     * 
     * @since 1.0.4
     */

    public IBeanSpecification getBeanSpecification(String name)
    {
        return (IBeanSpecification) get(_beans, name);
    }

    /**
     * Returns an unmodifiable collection of the names of all beans.
     */

    public Collection getBeanNames()
    {
        if (_beans == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableCollection(_beans.keySet());
    }

    /**
     * Adds the value as a reserved name. Reserved names are not allowed as the names of informal
     * parameters. Since the comparison is caseless, the value is converted to lowercase before
     * being stored.
     * 
     * @since 1.0.5
     */

    public void addReservedParameterName(String value)
    {
        if (_reservedParameterNames == null)
            _reservedParameterNames = new HashSet();

        _reservedParameterNames.add(value.toLowerCase());
    }

    /**
     * Returns true if the value specified is in the reserved name list. The comparison is caseless.
     * All formal parameters are automatically in the reserved name list, as well as any additional
     * reserved names specified in the component specification. The latter refer to HTML attributes
     * generated directly by the component.
     * 
     * @since 1.0.5
     */

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
     * Returns the documentation for this component.
     * 
     * @since 1.0.9
     */

    public String getDescription()
    {
        return _description;
    }

    /**
     * Sets the documentation for this component.
     * 
     * @since 1.0.9
     */

    public void setDescription(String description)
    {
        _description = description;
    }

    /**
     * Returns the XML Public Id for the specification file, or null if not applicable.
     * <p>
     * This method exists as a convienience for the Spindle plugin. A previous method used an
     * arbitrary version string, the public id is more useful and less ambiguous.
     * 
     * @since 2.2
     */

    public String getPublicId()
    {
        return _publicId;
    }

    /** @since 2.2 * */

    public void setPublicId(String publicId)
    {
        _publicId = publicId;
    }

    /**
     * Returns true if the specification is known to be a page specification and not a component
     * specification. Earlier versions of the framework did not distinguish between the two, but
     * starting in 2.2, there are seperate XML entities for pages and components. Pages omit several
     * attributes and entities related to parameters, as parameters only make sense for components.
     * 
     * @since 2.2
     */

    public boolean isPageSpecification()
    {
        return _pageSpecification;
    }

    /** @since 2.2 * */

    public void setPageSpecification(boolean pageSpecification)
    {
        _pageSpecification = pageSpecification;
    }

    /** @since 2.2 * */

    private List sortedKeys(Map input)
    {
        if (input == null)
            return Collections.EMPTY_LIST;

        List result = new ArrayList(input.keySet());

        Collections.sort(result);

        return result;
    }

    /** @since 2.2 * */

    private Object get(Map map, Object key)
    {
        if (map == null)
            return null;

        return map.get(key);
    }

    /** @since 3.0 * */

    public Resource getSpecificationLocation()
    {
        return _specificationLocation;
    }

    /** @since 3.0 * */

    public void setSpecificationLocation(Resource specificationLocation)
    {
        _specificationLocation = specificationLocation;
    }

    /**
     * Adds a new property specification. The name of the property must not already be defined (and
     * must not change after being added).
     * 
     * @since 3.0
     */

    public void addPropertySpecification(IPropertySpecification spec)
    {
        if (_propertySpecifications == null)
            _propertySpecifications = new HashMap();

        String name = spec.getName();
        IPropertySpecification existing = (IPropertySpecification) _propertySpecifications
                .get(name);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.duplicateProperty(name, existing),
                    spec.getLocation(), null);

        claimProperty(name, spec);

        _propertySpecifications.put(name, spec);
    }

    /**
     * Returns a sorted, immutable list of the names of all
     * {@link org.apache.tapestry.spec.IPropertySpecification}s.
     * 
     * @since 3.0
     */

    public List getPropertySpecificationNames()
    {
        return sortedKeys(_propertySpecifications);
    }

    /**
     * Returns the named {@link org.apache.tapestry.spec.IPropertySpecification}, or null if no
     * such specification exist.
     * 
     * @since 3.0
     * @see #addPropertySpecification(IPropertySpecification)
     */

    public IPropertySpecification getPropertySpecification(String name)
    {
        return (IPropertySpecification) get(_propertySpecifications, name);
    }

    public void addInjectSpecification(InjectSpecification spec)
    {
        if (_injectSpecifications == null)
            _injectSpecifications = new ArrayList();

        claimProperty(spec.getProperty(), spec);

        _injectSpecifications.add(spec);
    }

    public List getInjectSpecifications()
    {
        return safeList(_injectSpecifications);
    }

    private List safeList(List input)
    {
        if (input == null)
            return Collections.EMPTY_LIST;

        return Collections.unmodifiableList(input);
    }

    private void claimProperty(String propertyName, Object subSpecification)
    {
        if (propertyName == null)
            return;

        if (_claimedProperties == null)
            _claimedProperties = new HashMap();

        Object existing = _claimedProperties.get(propertyName);

        if (existing != null)
            throw new ApplicationRuntimeException(SpecMessages.claimedProperty(
                    propertyName,
                    existing), HiveMind.getLocation(subSpecification), null);

        _claimedProperties.put(propertyName, subSpecification);
    }

    /** @since 4.0 */
    public boolean isDeprecated()
    {
        return _deprecated;
    }

    /** @since 4.0 */
    public void setDeprecated(boolean deprecated)
    {
        _deprecated = deprecated;
    }

    public Set getReservedParameterNames()
    {
        if (_reservedParameterNames == null)
            return Collections.EMPTY_SET;

        return Collections.unmodifiableSet(_reservedParameterNames);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addEventListener(String componentId, String[] events, 
            String methodName, String formId, boolean validateForm, boolean async)
    {
        ComponentEventProperty property = getComponentEvents(componentId);
        
        property.addListener(events, methodName, formId, validateForm, async);
    }
    
    /**
     * {@inheritDoc}
     */
    public void addElementEventListener(String elementId, String[] events, 
            String methodName, String formId, boolean validateForm, boolean async)
    {
        ComponentEventProperty property = getElementEvents(elementId);
        
        property.addListener(events, methodName, formId, validateForm, async);
    }
    
    /**
     * {@inheritDoc}
     */
    public ComponentEventProperty getComponentEvents(String id)
    {
        ComponentEventProperty prop = (ComponentEventProperty)_componentEvents.get(id);
        if (prop == null) {
            prop = new ComponentEventProperty(id);
            _componentEvents.put(id, prop);
        }
        
        return prop;
    }
    
    /**
     * {@inheritDoc}
     */
    public ComponentEventProperty getElementEvents(String id)
    {
        ComponentEventProperty prop = (ComponentEventProperty)_elementEvents.get(id);
        if (prop == null) {
            prop = new ComponentEventProperty(id);
            _elementEvents.put(id, prop);
        }
        
        return prop;
    }
    
    /**
     * {@inheritDoc}
     */
    public Map getElementEvents()
    {
        return _elementEvents;
    }
    
    /**
     * {@inheritDoc}
     */
    public EventBoundListener[] getFormEvents(String formId, BrowserEvent event)
    {
        List ret = new ArrayList();
        
        Iterator it = _componentEvents.keySet().iterator();
        while (it.hasNext()) {
            
            String compId = (String)it.next();
            ComponentEventProperty prop = (ComponentEventProperty)_componentEvents.get(compId);
            
            ret.addAll(prop.getFormEventListeners(formId, event, null));
        }
        
        it = _elementEvents.keySet().iterator();
        while (it.hasNext()) {
            
            String compId = (String)it.next();
            ComponentEventProperty prop = (ComponentEventProperty)_elementEvents.get(compId);
            
            ret.addAll(prop.getFormEventListeners(formId, event, null));
        }
        
        return (EventBoundListener[])ret.toArray(new EventBoundListener[ret.size()]);
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasEvents()
    {
        return _componentEvents.size() > 0;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasEvents(String id)
    {
        return _componentEvents.get(id) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasElementEvents(String id)
    {
        return _elementEvents.get(id) != null;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean hasElementEvents()
    {
        return _elementEvents.size() > 0;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((_componentClassName == null) ? 0 : _componentClassName.hashCode());
        result = prime * result + ((_description == null) ? 0 : _description.hashCode());
        result = prime * result + (_pageSpecification ? 1231 : 1237);
        result = prime * result + ((_publicId == null) ? 0 : _publicId.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj)
    {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ComponentSpecification other = (ComponentSpecification) obj;
        if (_componentClassName == null) {
            if (other._componentClassName != null) return false;
        } else if (!_componentClassName.equals(other._componentClassName)) return false;
        if (_description == null) {
            if (other._description != null) return false;
        } else if (!_description.equals(other._description)) return false;
        if (_pageSpecification != other._pageSpecification) return false;
        if (_publicId == null) {
            if (other._publicId != null) return false;
        } else if (!_publicId.equals(other._publicId)) return false;
        if (_specificationLocation == null) {
            if (other._specificationLocation != null) return false;
        } else if (!_specificationLocation.getPath().equals(other._specificationLocation.getPath())) return false;
        return true;
    }
    
    
}
