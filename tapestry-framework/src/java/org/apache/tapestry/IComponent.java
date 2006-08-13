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

package org.apache.tapestry;

import java.util.Collection;
import java.util.Map;

import org.apache.hivemind.LocationHolder;
import org.apache.hivemind.Messages;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.listener.ListenerMap;
import org.apache.tapestry.services.impl.ComponentEventInvoker;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.spec.IContainedComponent;

/**
 * Defines an object which may be used to provide dynamic content on a Tapestry web page.
 * <p>
 * Components are created dynamically from thier class names (part of the
 * {@link IComponentSpecification}).
 * 
 * @author Howard Lewis Ship
 */

public interface IComponent extends IRender, LocationHolder
{

    /**
     * Adds an asset to the component. This is invoked from the page loader.
     */

    void addAsset(String name, IAsset asset);

    /**
     * Adds a component to a container. Should only be called during the page loading process, which
     * is responsible for any checking.
     * 
     * @see IPageLoader
     */

    void addComponent(IComponent component);

    /**
     * Adds a new renderable element to the receiver's body. The element may be either another
     * component, or static HTML. Such elements come from inside the receiver's tag within its
     * container's template, and represent static text and other components.
     * <p>
     * The method {@link #renderBody(IMarkupWriter, IRequestCycle)}is used to render these
     * elements.
     * 
     * @since 2.2
     */

    void addBody(IRender element);
    
    /**
     * Returns the asset map for the component, which may be empty but will not be null.
     * <p>
     * The return value is unmodifiable.
     */

    Map getAssets();

    /**
     * Returns the named asset, or null if not found.
     */

    IAsset getAsset(String name);

    /**
     * Returns the binding with the given name or null if not found.
     * <p>
     * Bindings are added to a component using {@link #setBinding(String,IBinding)}.
     */

    IBinding getBinding(String name);

    /**
     * Returns a {@link Collection}of the names of all bindings (which includes bindings for both
     * formal and informal parameters).
     * <p>
     * The return value is unmodifiable. It will be null for a {@link IPage page}, or may simply be
     * empty for a component with no bindings.
     */

    Collection getBindingNames();

    /**
     * Returns a {@link Map}of the {@link IBinding bindings}for this component; this includes
     * informal parameters as well as formal bindings.
     * 
     * @since 1.0.5
     */

    Map getBindings();

    /**
     * Retrieves an contained component by its id. Contained components have unique ids within their
     * container.
     * 
     * @exception ApplicationRuntimeException
     *                runtime exception thrown if the named component does not exist.
     */

    IComponent getComponent(String id);

    /**
     * Returns the component which embeds the receiver. All components are contained within other
     * components, with the exception of the root page component.
     * <p>
     * A page returns null.
     */

    IComponent getContainer();

    /**
     * Sets the container of the component. This is write-once, an attempt to change it later will
     * throw an {@link ApplicationRuntimeException}.
     */

    void setContainer(IComponent value);

    /**
     * Returns a string identifying the name of the page and the id path of the reciever within the
     * page (seperated by a slash). Note that this extended id is indetned primarily for identifying
     * the component to the user (since slashes are legal characters within page names). Pages
     * simply return their name.
     * 
     * @see #getIdPath()
     */

    String getExtendedId();

    /**
     * Returns the simple id of the component, as defined in its specification.
     * <p>
     * An id will be unique within the component which contains this component.
     * <p>
     * A {@link IPage page}will always return null.
     */

    String getId();

    /**
     * Sets the id of the component. This is write-once, an attempt to change it later will throw an
     * {@link ApplicationRuntimeException}.
     */

    void setId(String value);

    /**
     * Returns the qualified id of the component. This represents a path from the {@link IPage page}
     * to this component, showing how components contain each other.
     * <p>
     * A {@link IPage page}will always return null. A component contained on a page returns its
     * simple id. Other components return their container's id path followed by a period and their
     * own name.
     * 
     * @see #getId()
     */

    String getIdPath();

    /**
     * Returns the component's client-side element id. This has traditionally been an 
     * {@link IFormComponent} only binding but now applies to all components. The method
     * should check to see if any id parameter/property has been set already and use that
     * above all others, falling back to {@link #getId()} if nothing else is found.
     * 
     * @return the id, or null if the component doesn't support a client id.
     * @since 4.1
     */
    String getClientId();
    
    /**
     * Returns the page which ultimately contains the receiver. A page will return itself.
     */

    IPage getPage();

    /**
     * Sets the page which ultimiately contains the component. This is write-once, an attempt to
     * change it later will throw an {@link ApplicationRuntimeException}.
     */

    void setPage(IPage value);

    /**
     * Returns the specification which defines the component.
     */

    IComponentSpecification getSpecification();

    /**
     * Invoked to make the receiver render its body (the elements and components its tag wraps
     * around, on its container's template). This method is public so that the
     * {@link org.apache.tapestry.components.RenderBody}component may operate.
     * 
     * @since 2.2
     */

    void renderBody(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Adds a binding to a container. Should only be called during the page loading process (which
     * is responsible for eror checking).
     * 
     * @see IPageLoader
     */

    void setBinding(String name, IBinding binding);

    /**
     * Returns the contained components as an unmodifiable {@link Map}. This allows peer components
     * to work together without directly involving their container ... the classic example is to
     * have an {@link org.apache.tapestry.components.Insert}work with an enclosing
     * {@link org.apache.tapestry.components.Foreach}.
     * <p>
     * This is late addition to Tapestry, because it also opens the door to abuse, since it is quite
     * possible to break the "black box" aspect of a component by interacting directly with
     * components it embeds. This creates ugly interelationships between components that should be
     * seperated.
     * 
     * @return A Map of components keyed on component id. May return an empty map, but won't return
     *         null.
     */

    Map getComponents();

    /**
     * Allows a component to finish any setup after it has been constructed.
     * <p>
     * The exact timing is not specified, but any components contained by the receiving component
     * will also have been constructed before this method is invoked.
     * <p>
     * As of release 1.0.6, this method is invoked <em>before</em> bindings are set. This should
     * not affect anything, as bindings should only be used during renderring.
     * <p>
     * Release 2.2 added the cycle parameter which is, regretfully, not backwards compatible.
     * 
     * @since 0.2.12
     */

    void finishLoad(IRequestCycle cycle, IPageLoader loader,
            IComponentSpecification specification);

    /**
     * Returns component strings for the component. Starting in release 4.0, this method is
     * unimplemented (and is automatically injected into each component implementation).
     * 
     * @since 3.0
     */

    Messages getMessages();

    /**
     * Returns the {@link INamespace}in which the component was defined (as an alias).
     * 
     * @since 2.2
     */

    INamespace getNamespace();

    /**
     * Sets the {@link INamespace}for the component. The namespace should only be set once.
     * 
     * @since 2.2
     */

    void setNamespace(INamespace namespace);

    /**
     * Returns true if the component is currently rendering.
     * 
     * @since 4.0
     */

    boolean isRendering();

    /**
     * Invoked after {@link #finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}to
     * switch the component from its initial construction state into its active state. The
     * difference concerns parameters, whose defaults values may be set from inside
     * {@link #finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}.
     * 
     * @since 4.0
     */

    void enterActiveState();

    /**
     * Returns a {@link IBeanProvider} from which managed beans can be obtained.
     * 
     * @since 4.0
     */

    IBeanProvider getBeans();

    /**
     * Returns a {@link ListenerMap} for the component. The map contains a number of synthetic
     * read-only properties that implement the {@link IActionListener} interface, but in fact, cause
     * public instance methods to be invoked (via reflection).
     * 
     * @since 4.0
     */

    ListenerMap getListeners();

    /**
     * Returns the {@link org.apache.tapestry.spec.IContainedComponent}. This will be null for
     * pages. This property is set when a component is constructed, and links the component instance
     * to the reference in the containing page or component's template or specification. This is
     * useful to allow a component to know its type or the meta-data associated with the component.
     * 
     * @return the contained component, or null for a page.
     * @since 4.0
     */

    IContainedComponent getContainedComponent();

    /**
     * Sets the {@link #getContainedComponent()} property; this may only be done once.
     * 
     * @param containedComponent
     *            may not be null
     * @since 4.0
     */
    void setContainedComponent(IContainedComponent containedComponent);
    
    /**
     * Returns the event connection manager services that handles creating/accepting
     * browser events associated with various properties of components.
     * 
     * @return eventInvoker, may not be null
     * @since 4.1
     */
    ComponentEventInvoker getEventInvoker();
}
