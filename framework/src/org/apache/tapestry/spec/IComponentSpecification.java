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

import java.util.Collection;
import java.util.List;

import org.apache.tapestry.ILocatable;
import org.apache.tapestry.ILocationHolder;
import org.apache.tapestry.IResourceLocation;
import org.apache.tapestry.util.IPropertyHolder;

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
 *  @see IComponentSpecification
 *  @see org.apache.tapestry.engine.IPageLoader
 * 
 * @author glongman@intelligentworks.com
 * @version $Id$
 */
public interface IComponentSpecification extends IPropertyHolder, ILocationHolder, ILocatable
{
    /**
     * @throws IllegalArgumentException if the name already exists.
     *
     **/
    public abstract void addAsset(String name, IAssetSpecification asset);
    /**
     *  @throws IllegalArgumentException if the id is already defined.
     *
     **/
    public abstract void addComponent(String id, IContainedComponent component);
    /**
     *  Adds the parameter.   The name is added as a reserved name.
     *
     *  @throws IllegalArgumentException if the name already exists.
     **/
    public abstract void addParameter(String name, IParameterSpecification spec);
    /**
     *  Returns true if the component is allowed to wrap other elements (static HTML
     *  or other components).  The default is true.
     *
     *  @see #setAllowBody(boolean)
     *
     **/
    public abstract boolean getAllowBody();
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
    public abstract boolean getAllowInformalParameters();
    /**
     *  Returns the {@link IAssetSpecification} with the given name, or null
     *  if no such specification exists.
     *
     *  @see #addAsset(String,IAssetSpecification)
     **/
    public abstract IAssetSpecification getAsset(String name);
    /**
     *  Returns a <code>List</code>
     *  of the String names of all assets, in alphabetical
     *  order
     *
     **/
    public abstract List getAssetNames();
    /**
     *  Returns the specification of a contained component with the given id, or
     *  null if no such contained component exists.
     *
     *  @see #addComponent(String, IContainedComponent)
     *
     **/
    public abstract IContainedComponent getComponent(String id);
    public abstract String getComponentClassName();
    /**
     *  Returns an <code>List</code>
     *  of the String names of the {@link IContainedComponent}s
     *  for this component.
     *
     *  @see #addComponent(String, IContainedComponent)
     *
     **/
    public abstract List getComponentIds();
    /**
     *  Returns the specification of a parameter with the given name, or
     *  null if no such parameter exists.
     *
     *  @see #addParameter(String, IParameterSpecification)
     *
     **/
    public abstract IParameterSpecification getParameter(String name);
    /**
     *  Returns a List of
     *  of String names of all parameters.  This list
     *  is in alphabetical order.
     *
     *  @see #addParameter(String, IParameterSpecification)
     *
     **/
    public abstract List getParameterNames();
    public abstract void setAllowBody(boolean value);
    public abstract void setAllowInformalParameters(boolean value);
    public abstract void setComponentClassName(String value);
    /**
     *  @since 1.0.4
     *
     *  @throws IllegalArgumentException if the bean already has a specification.
     **/
    public abstract void addBeanSpecification(String name, IBeanSpecification specification);
    /**
     * Returns the {@link IBeanSpecification} for the given name, or null
     * if not such specification exists.
     *
     * @since 1.0.4
     *
     **/
    public abstract IBeanSpecification getBeanSpecification(String name);
    /**
     *  Returns an unmodifiable collection of the names of all beans.
     *
     **/
    public abstract Collection getBeanNames();
    /**
     *  Adds the value as a reserved name.  Reserved names are not allowed
     *  as the names of informal parameters.  Since the comparison is
     *  caseless, the value is converted to lowercase before being
     *  stored.
     *
     *  @since 1.0.5
     *
     **/
    public abstract void addReservedParameterName(String value);
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
    public abstract boolean isReservedParameterName(String value);
    /**
     *  Returns the documentation for this component.
     * 
     *  @since 1.0.9
     **/
    public abstract String getDescription();
    /**
     *  Sets the documentation for this component.
     * 
     *  @since 1.0.9
     **/
    public abstract void setDescription(String description);
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
    public abstract String getPublicId();
    /** @since 2.2 **/
    public abstract void setPublicId(String publicId);
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
    public abstract boolean isPageSpecification();
    /** @since 2.2 **/
    public abstract void setPageSpecification(boolean pageSpecification);
    /** @since 3.0 **/
    public abstract IResourceLocation getSpecificationLocation();
    /** @since 3.0 **/
    public abstract void setSpecificationLocation(IResourceLocation specificationLocation);
    /**
     *  Adds a new property specification.  The name of the property must
     *  not already be defined (and must not change after being added).
     * 
     *  @since 3.0
     * 
     **/
    public abstract void addPropertySpecification(IPropertySpecification spec);
    /**
     *  Returns a sorted, immutable list of the names of all 
     *  {@link org.apache.tapestry.spec.IPropertySpecification}s.
     * 
     *  @since 3.0
     * 
     **/
    public abstract List getPropertySpecificationNames();
    /**
     *  Returns the named {@link org.apache.tapestry.spec.IPropertySpecification},
     *  or null  if no such specification exist.
     * 
     *  @since 3.0
     *  @see #addPropertySpecification(IPropertySpecification)
     * 
     **/
    public abstract IPropertySpecification getPropertySpecification(String name);
}