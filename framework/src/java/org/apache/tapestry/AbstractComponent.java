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
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.hivemind.impl.BaseLocatable;
import org.apache.hivemind.util.Defense;
import org.apache.hivemind.util.PropertyUtils;
import org.apache.tapestry.bean.BeanProvider;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.listener.ListenerMap;
import org.apache.tapestry.spec.IComponentSpecification;

/**
 * Abstract base class implementing the {@link IComponent}interface.
 * 
 * @author Howard Lewis Ship
 */

public abstract class AbstractComponent extends BaseLocatable implements IComponent
{
    /**
     * The page that contains the component, possibly itself (if the component is in fact, a page).
     */

    private IPage _page;

    /**
     * The component which contains the component. This will only be null if the component is
     * actually a page.
     */

    private IComponent _container;

    /**
     * The simple id of this component.
     */

    private String _id;

    /**
     * The fully qualified id of this component. This is calculated the first time it is needed,
     * then cached for later.
     */

    private String _idPath;

    private static final int MAP_SIZE = 5;

    /**
     * A {@link Map}of all bindings (for which there isn't a corresponding JavaBeans property); the
     * keys are the names of formal and informal parameters.
     */

    private Map _bindings;

    private Map _components;

    private static final int BODY_INIT_SIZE = 5;

    private INamespace _namespace;

    /**
     * Used in place of JDK 1.3's Collections.EMPTY_MAP (which is not available in JDK 1.2).
     */

    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(1));

    /**
     * The number of {@link IRender}objects in the body of this component.
     */

    private int _bodyCount = 0;

    /**
     * An aray of elements in the body of this component.
     */

    private IRender[] _body;

    /**
     * The components' asset map.
     */

    private Map _assets;

    /**
     * A mapping that allows public instance methods to be dressed up as {@link IActionListener}
     * listener objects.
     * 
     * @since 1.0.2
     */

    private ListenerMap _listeners;

    /**
     * A bean provider; these are lazily created as needed.
     * 
     * @since 1.0.4
     */

    private IBeanProvider _beans;

    /**
     * Returns true if the component is currently rendering.
     * 
     * @see #prepareForRender(IRequestCycle)
     * @see #cleanupAfterRender(IRequestCycle)
     * @since 3.1
     */

    private boolean _rendering;

    /**
     * @since 3.1
     */

    private boolean _active;

    public void addAsset(String name, IAsset asset)
    {
        Defense.notNull(name, "name");
        Defense.notNull(asset, "asset");

        checkActiveLock();

        if (_assets == null)
            _assets = new HashMap(MAP_SIZE);

        _assets.put(name, asset);
    }

    public void addComponent(IComponent component)
    {
        Defense.notNull(component, "component");

        checkActiveLock();

        if (_components == null)
            _components = new HashMap(MAP_SIZE);

        _components.put(component.getId(), component);
    }

    /**
     * Adds an element (which may be static text or a component) as a body element of this
     * component. Such elements are rendered by {@link #renderBody(IMarkupWriter, IRequestCycle)}.
     * 
     * @since 2.2
     */

    public void addBody(IRender element)
    {
        Defense.notNull(element, "element");

        // TODO: Tweak the ordering of operations inside the PageLoader so that this
        // check is allowable. Currently, the component is entering active state
        // before it loads its template.

        // checkActiveLock();

        // Should check the specification to see if this component
        // allows body. Curently, this is checked by the component
        // in render(), which is silly.

        if (_body == null)
        {
            _body = new IRender[BODY_INIT_SIZE];
            _body[0] = element;

            _bodyCount = 1;
            return;
        }

        // No more room? Make the array bigger.

        if (_bodyCount == _body.length)
        {
            IRender[] newWrapped;

            newWrapped = new IRender[_body.length * 2];

            System.arraycopy(_body, 0, newWrapped, 0, _bodyCount);

            _body = newWrapped;
        }

        _body[_bodyCount++] = element;
    }

    /**
     * Invokes {@link #finishLoad()}. Subclasses may overide as needed, but must invoke this
     * implementation. {@link BaseComponent}loads its HTML template.
     */

    public void finishLoad(IRequestCycle cycle, IPageLoader loader,
            IComponentSpecification specification)
    {
        finishLoad();
    }

    /**
     * Converts informal parameters into additional attributes on the curently open tag.
     * <p>
     * Invoked from subclasses to allow additional attributes to be specified within a tag (this
     * works best when there is a one-to-one corespondence between an {@link IComponent}and a HTML
     * element.
     * <p>
     * Iterates through the bindings for this component. Filters out bindings for formal parameters.
     * <p>
     * For each acceptible key, the value is extracted using {@link IBinding#getObject()}. If the
     * value is null, no attribute is written.
     * <p>
     * If the value is an instance of {@link IAsset}, then {@link IAsset#buildURL(IRequestCycle)}
     * is invoked to convert the asset to a URL.
     * <p>
     * Finally, {@link IMarkupWriter#attribute(String,String)}is invoked with the value (or the
     * URL).
     * <p>
     * The most common use for informal parameters is to support the HTML class attribute (for use
     * with cascading style sheets) and to specify JavaScript event handlers.
     * <p>
     * Components are only required to generate attributes on the result phase; this can be skipped
     * during the rewind phase.
     */

    protected void renderInformalParameters(IMarkupWriter writer, IRequestCycle cycle)
    {
        String attribute;

        if (_bindings == null)
            return;

        Iterator i = _bindings.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();

            if (isFormalParameter(name))
                continue;

            IBinding binding = (IBinding) entry.getValue();

            Object value = binding.getObject();
            if (value == null)
                continue;

            if (value instanceof IAsset)
            {
                IAsset asset = (IAsset) value;

                // Get the URL of the asset and insert that.

                attribute = asset.buildURL(cycle);
            }
            else
                attribute = value.toString();

            writer.attribute(name, attribute);
        }

    }

    /** @since 3.1 */
    private boolean isFormalParameter(String name)
    {
        Defense.notNull(name, "name");

        return getSpecification().getParameter(name) != null;
    }

    /**
     * Returns the named binding, or null if it doesn't exist.
     * <p>
     * In Tapestry 3.0, it was possible to force a binding to be stored in a component property by
     * defining a concrete or abstract property named "nameBinding" of type {@link IBinding}. This
     * has been removed in release 3.1 and bindings are always stored inside a Map of the component.
     * 
     * @see #setBinding(String,IBinding)
     */

    public IBinding getBinding(String name)
    {
        Defense.notNull(name, "name");

        if (_bindings == null)
            return null;

        return (IBinding) _bindings.get(name);
    }

    /**
     * Returns true if the specified parameter is bound.
     * 
     * @since 3.1
     */

    public boolean isParameterBound(String parameterName)
    {
        Defense.notNull(parameterName, "parameterName");

        return _bindings != null && _bindings.containsKey(parameterName);
    }

    public IComponent getComponent(String id)
    {
        Defense.notNull(id, "id");

        IComponent result = null;

        if (_components != null)
            result = (IComponent) _components.get(id);

        if (result == null)
            throw new ApplicationRuntimeException(Tapestry.format("no-such-component", this, id),
                    this, null, null);

        return result;
    }

    public IComponent getContainer()
    {
        return _container;
    }

    public void setContainer(IComponent value)
    {
        checkActiveLock();

        if (_container != null)
            throw new ApplicationRuntimeException(Tapestry
                    .getMessage("AbstractComponent.attempt-to-change-container"));

        _container = value;
    }

    /**
     * Returns the name of the page, a slash, and this component's id path. Pages are different,
     * they simply return their name.
     * 
     * @see #getIdPath()
     */

    public String getExtendedId()
    {
        if (_page == null)
            return null;

        return _page.getPageName() + "/" + getIdPath();
    }

    public String getId()
    {
        return _id;
    }

    public void setId(String value)
    {
        if (_id != null)
            throw new ApplicationRuntimeException(Tapestry
                    .getMessage("AbstractComponent.attempt-to-change-component-id"));

        _id = value;
    }

    public String getIdPath()
    {
        String containerIdPath;

        if (_container == null)
            throw new NullPointerException(Tapestry
                    .format("AbstractComponent.null-container", this));

        containerIdPath = _container.getIdPath();

        if (containerIdPath == null)
            _idPath = _id;
        else
            _idPath = containerIdPath + "." + _id;

        return _idPath;
    }

    public IPage getPage()
    {
        return _page;
    }

    public void setPage(IPage value)
    {
        if (_page != null)
            throw new ApplicationRuntimeException(Tapestry
                    .getMessage("AbstractComponent.attempt-to-change-page"));

        _page = value;
    }

    /**
     * Renders all elements wrapped by the receiver.
     */

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle)
    {
        for (int i = 0; i < _bodyCount; i++)
            _body[i].render(writer, cycle);
    }

    /**
     * Adds the binding with the given name, replacing any existing binding with that name.
     * <p>
     * 
     * @see #getBinding(String)
     */

    public void setBinding(String name, IBinding binding)
    {
        Defense.notNull(name, "name");
        Defense.notNull(binding, "binding");

        if (_bindings == null)
            _bindings = new HashMap(MAP_SIZE);

        _bindings.put(name, binding);
    }

    public String toString()
    {
        StringBuffer buffer;

        buffer = new StringBuffer(super.toString());

        buffer.append('[');

        buffer.append(getExtendedId());

        buffer.append(']');

        return buffer.toString();
    }

    /**
     * Returns an unmodifiable {@link Map}of components, keyed on component id. Never returns null,
     * but may return an empty map. The returned map is immutable.
     */

    public Map getComponents()
    {
        if (_components == null)
            return EMPTY_MAP;

        return Collections.unmodifiableMap(_components);

    }

    public Map getAssets()
    {
        if (_assets == null)
            return EMPTY_MAP;

        return Collections.unmodifiableMap(_assets);
    }

    public IAsset getAsset(String name)
    {
        if (_assets == null)
            return null;

        return (IAsset) _assets.get(name);
    }

    public Collection getBindingNames()
    {
        // If no conainer, i.e. a page, then no bindings.

        if (_container == null)
            return null;

        HashSet result = new HashSet();

        // All the informal bindings go into the bindings Map.

        if (_bindings != null)
            result.addAll(_bindings.keySet());

        // Now, iterate over the formal parameters and add the formal parameters
        // that have a binding.

        List names = getSpecification().getParameterNames();

        int count = names.size();

        for (int i = 0; i < count; i++)
        {
            String name = (String) names.get(i);

            if (result.contains(name))
                continue;

            if (getBinding(name) != null)
                result.add(name);
        }

        return result;
    }

    /**
     * Returns an unmodifiable {@link Map}of all bindings for this component.
     * 
     * @since 1.0.5
     */

    public Map getBindings()
    {
        if (_bindings == null)
            return Collections.EMPTY_MAP;

        return Collections.unmodifiableMap(_bindings);
    }

    /**
     * Returns a {@link ListenerMap}for the component. A {@link ListenerMap}contains a number of
     * synthetic read-only properties that implement the {@link IActionListener}interface, but in
     * fact, cause public instance methods to be invoked.
     * 
     * @since 1.0.2
     */

    public ListenerMap getListeners()
    {
        if (_listeners == null)
            _listeners = new ListenerMap(this);

        return _listeners;
    }

    /**
     * Returns the {@link IBeanProvider}for this component. This is lazily created the first time
     * it is needed.
     * 
     * @since 1.0.4
     */

    public IBeanProvider getBeans()
    {
        if (_beans == null)
            _beans = new BeanProvider(this);

        return _beans;
    }

    /**
     * Invoked, as a convienience, from
     * {@link #finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}. This implemenation
     * does nothing. Subclasses may override without invoking this implementation.
     * 
     * @since 1.0.5
     */

    protected void finishLoad()
    {
    }

    /**
     * The main method used to render the component. Invokes
     * {@link #prepareForRender(IRequestCycle)}, then
     * {@link #renderComponent(IMarkupWriter, IRequestCycle)}.
     * {@link #cleanupAfterRender(IRequestCycle)}is invoked in a <code>finally</code> block.
     * <p>
     * Subclasses should not override this method; instead they will implement
     * {@link #renderComponent(IMarkupWriter, IRequestCycle)}.
     * 
     * @since 2.0.3
     */

    public final void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        try
        {
            _rendering = true;

            prepareForRender(cycle);

            renderComponent(writer, cycle);
        }
        finally
        {
            _rendering = false;

            cleanupAfterRender(cycle);
        }
    }

    /**
     * Invoked by {@link #render(IMarkupWriter, IRequestCycle)}to prepare the component to render.
     * This implementation sets JavaBeans properties from matching bound parameters. This
     * implementation does nothing.
     * 
     * @since 2.0.3
     */

    protected void prepareForRender(IRequestCycle cycle)
    {
    }

    /**
     * Invoked by {@link #render(IMarkupWriter, IRequestCycle)}to actually render the component
     * (with any parameter values already set). This is the method that subclasses must implement.
     * 
     * @since 2.0.3
     */

    protected abstract void renderComponent(IMarkupWriter writer, IRequestCycle cycle);

    /**
     * Invoked by {@link #render(IMarkupWriter, IRequestCycle)}after the component renders. This
     * implementation does nothing.
     * 
     * @since 2.0.3
     */

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
    }

    public INamespace getNamespace()
    {
        return _namespace;
    }

    public void setNamespace(INamespace namespace)
    {
        _namespace = namespace;
    }

    /**
     * Returns the body of the component, the element (which may be static HTML or components) that
     * the component immediately wraps. May return null. Do not modify the returned array. The array
     * may be padded with nulls.
     * 
     * @since 2.3
     * @see #getBodyCount()
     */

    public IRender[] getBody()
    {
        return _body;
    }

    /**
     * Returns the active number of elements in the the body, which may be zero.
     * 
     * @since 2.3
     * @see #getBody()
     */

    public int getBodyCount()
    {
        return _bodyCount;
    }

    /**
     * Empty implementation of
     * {@link org.apache.tapestry.event.PageRenderListener#pageEndRender(PageEvent)}. This allows
     * classes to implement {@link org.apache.tapestry.event.PageRenderListener}and only implement
     * the {@link org.apache.tapestry.event.PageRenderListener#pageBeginRender(PageEvent)}method.
     * 
     * @since 3.0
     */

    public void pageEndRender(PageEvent event)
    {
    }

    /**
     * Sets a property of a component.
     * 
     * @see IComponent
     * @since 3.0
     */
    public void setProperty(String propertyName, Object value)
    {
        PropertyUtils.write(this, propertyName, value);
    }

    /**
     * Gets a property of a component.
     * 
     * @see IComponent
     * @since 3.0
     */
    public Object getProperty(String propertyName)
    {
        return PropertyUtils.read(this, propertyName);
    }

    /**
     * @since 3.1
     */

    public boolean isRendering()
    {
        return _rendering;
    }

    /**
     * Returns true if the component has been transitioned into its active state by invoking
     * {@link #enterActiveState()}
     * 
     * @since 3.1
     */

    protected boolean isInActiveState()
    {
        return _active;
    }

    /** @since 3.1 */
    public void enterActiveState()
    {
        _active = true;
    }

    /** @since 3.1 */

    protected void checkActiveLock()
    {
        if (_active)
            throw new UnsupportedOperationException(TapestryMessages.componentIsLocked(this));
    }
}