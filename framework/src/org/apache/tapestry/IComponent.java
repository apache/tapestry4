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

package org.apache.tapestry;

import java.util.Collection;
import java.util.Map;

import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 *  Defines an object which may be used to provide dynamic content on a Tapestry web page.
 *
 *  <p>Components are created dynamically from thier class names (part of the
 *  {@link IComponentSpecification}).
 *
 *
 *  @author Howard Leiws Ship
 *  @version $Id$
 * 
 **/

public interface IComponent extends IRender, ILocationHolder
{

    /**
     *  Adds an asset to the component.  This is invoked from the page loader.
     *
     **/

    public void addAsset(String name, IAsset asset);

    /**
     *  Adds a component to a container.  Should only be called during the page
     *  loading process, which is responsible for any checking.
     *
     *  @see IPageLoader
     *
     **/

    public void addComponent(IComponent component);

    /**
     *  Adds a new renderable element to the receiver's body.  The element may be either
     *  another component, or static HTML.  Such elements come from inside
     *  the receiver's tag within its container's template, and represent static
     *  text and other components.
     * 
     *  <p>The method {@link #renderBody(IMarkupWriter, IRequestCycle)} is used
     *  to render these elements.
     * 
     *  @since 2.2
     * 
     **/

    public void addBody(IRender element);

    /**
     *  Returns the asset map for the component, which may be empty but will not be null.
     *
     *  <p>The return value is unmodifiable.
     * 
     **/

    public Map getAssets();

    /**
     *  Returns the named asset, or null if not found.
     *
     **/

    public IAsset getAsset(String name);

    /**
     *  Returns the binding with the given name or null if not found.
     *
     *  <p>Bindings are added to a component using {@link #setBinding(String,IBinding)}.
     **/

    public IBinding getBinding(String name);

    /**
     *  Returns a {@link Collection} of the names of all bindings (which includes
     *  bindings for both formal and informal parameters).
     *
     *  <p>The return value is unmodifiable.  It will be null for a {@link IPage page},
     *  or may simply be empty for a component with no bindings.
     *
     **/

    public Collection getBindingNames();

    /**
     *  Returns a {@link Map} of the {@link IBinding bindings} for this component; 
     *  this includes informal parameters
     *  as well as formal bindings.
     *
     *  @since 1.0.5
     *
     **/

    public Map getBindings();

    /**
     *  Retrieves an contained component by its id.
     *  Contained components have unique ids within their container.
     *
     *  @exception ApplicationRuntimeException runtime exception thrown if the named
     *  component does not exist.
     *
     **/

    public IComponent getComponent(String id);

    /**
     *  Returns the component which embeds the receiver.  All components are contained within
     *  other components, with the exception of the root page component.
     *
     *  <p>A page returns null.
     *
     **/

    public IComponent getContainer();

    /**
     *  Sets the container of the component.    This is write-once,
     *  an attempt to change it later will throw an {@link ApplicationRuntimeException}.
     *
     **/

    public void setContainer(IComponent value);

    /**
     *  Returns a string identifying the name of the page and the id path of the reciever within
     *  the page.  Pages simply return their name.
     *
     *  @see #getIdPath()
     **/

    public String getExtendedId();

    /**
     *  Returns the simple id of the component, as defined in its specification.
     *
     *  <p>An id will be unique within the
     *  component which contains this component.
     *
     *  <p>A  {@link IPage page} will always return null.
     *
     **/

    public String getId();

    /**
     *  Sets the id of the component.    This is write-once,
     *  an attempt to change it later will throw an {@link ApplicationRuntimeException}.
     *
     **/

    public void setId(String value);

    /**
     *  Returns the qualified id of the component.  This represents a path from the
     *  {@link IPage page} to
     *  this component, showing how components contain each other.
     *
     *  <p>A {@link IPage page} will always return
     *  null.  A component contained on a page returns its simple id.
     *  Other components return their container's id path followed by a period and their
     *  own name.
     *
     *  @see #getId()
     **/

    public String getIdPath();

    /**
     *  Returns the page which ultimately contains the receiver.  A page will return itself.
     *
     **/

    public IPage getPage();

    /**
     *  Sets the page which ultimiately contains the component.  This is write-once,
     *  an attempt to change it later will throw an {@link ApplicationRuntimeException}.
     *
     **/

    public void setPage(IPage value);

    /**
     *  Returns the specification which defines the component.
     *
     **/

    public IComponentSpecification getSpecification();

    /**
     *  Sets the specification used by the component.  This is write-once, an attempt
     *  to change it later will throw an {@link ApplicationRuntimeException}.
     *
     **/

    public void setSpecification(IComponentSpecification value);

    /**
     *  Invoked to make the receiver render its body (the elements and components
     *  its tag wraps around, on its container's template).  
     *  This method is public so that the
     *  {@link org.apache.tapestry.components.RenderBody} component may operate.
     * 
     *  @since 2.2
     * 
     **/

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle);

    /**
     *  Adds a binding to a container.  Should only be called during the page
     *  loading process (which is responsible for eror checking).
     *
     *  @see IPageLoader
     *
     **/

    public void setBinding(String name, IBinding binding);

    /**
     *  Returns the contained components as an unmodifiable {@link Map}.  This
     *  allows peer components to work together without directly involving their
     *  container ... the classic example is to have an {@link org.apache.tapestry.components.Insert} 
     *  work with an enclosing {@link org.apache.tapestry.components.Foreach}.
     *
     *  <p>This is late addition to Tapestry, because it also opens the door
     *  to abuse, since it is quite possible to break the "black box" aspect of
     *  a component by interacting directly with components it embeds.  This creates
     *  ugly interelationships between components that should be seperated.
     *
     *  @return A Map of components keyed on component id.  May return an empty map, but won't return
     *  null.
     *
     **/

    public Map getComponents();

    /**
     *  Allows a component to finish any setup after it has been constructed.
     *
     *  <p>The exact timing is not
     *  specified, but any components contained by the
     *  receiving component will also have been constructed
     *  before this method is invoked.
     *
     *  <p>As of release 1.0.6, this method is invoked <em>before</em>
     *  bindings are set.  This should not affect anything, as bindings
     *  should only be used during renderring.
     * 
     *  <p>Release 2.2 added the cycle parameter which is, regretfully, not
     *  backwards compatible.
     *
     *  @since 0.2.12
     * 
     **/

    public void finishLoad(
        IRequestCycle cycle,
        IPageLoader loader,
        IComponentSpecification specification);

    /**
     *  Returns a localized string message.  Each component has an optional
     *  set of localized message strings that are read from properties
     *  files.
     * 
     *  @param key the key used to locate the message
     *  @return the localized message for the key, or a placeholder
     *  if no message is defined for the key.
     * 
     *  @since 2.0.4
     *  @deprecated To be removed in 3.1, use {@link #getMessage(String)}.
     **/

    public String getString(String key);

    /**
     *  Returns a localized string message.  Each component has an optional
     *  set of localized message strings that are read from properties
     *  files.
     * 
     *  @param key the key used to locate the message
     *  @return the localized message for the key, or a placeholder
     *  if no message is defined for the key.
     * 
     *  @since 3.0
     **/

    public String getMessage(String key);
    /**
     *  Returns component strings for the component.
     * 
     *  @since 3.0
     * 
     **/

    public IMessages getMessages();

    /**
     *  Returns the {@link INamespace} in which the component was defined
     *  (as an alias).  
     * 
     *  @since 2.2
     * 
     **/

    public INamespace getNamespace();

    /**
     *  Sets the {@link INamespace} for the component.  The namespace
     *  should only be set once.
     * 
     *  @since 2.2
     * 
     **/

    public void setNamespace(INamespace namespace);
    
    /**
     *  Sets a property of a component.
     *  @param propertyName the property name
     *  @param value the provided value
     */
	public void setProperty(String propertyName, Object value);
	
	/**
	 *  Gets a property of a component.
	 *  @param propertyName the property name
	 *  @return Object the value of property
	 */
	public Object getProperty(String propertyName);

}