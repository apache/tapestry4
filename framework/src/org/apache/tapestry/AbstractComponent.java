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

package org.apache.tapestry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ognl.OgnlRuntime;

import org.apache.tapestry.bean.BeanProvider;
import org.apache.tapestry.bean.BeanProviderPropertyAccessor;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.event.ChangeObserver;
import org.apache.tapestry.event.ObservedChangeEvent;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.listener.ListenerMap;
import org.apache.tapestry.param.ParameterManager;
import org.apache.tapestry.spec.BaseLocatable;
import org.apache.tapestry.spec.IComponentSpecification;
import org.apache.tapestry.util.prop.OgnlUtils;

/**
 *  Abstract base class implementing the {@link IComponent} interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class AbstractComponent extends BaseLocatable implements IComponent
{
    static {
        // Register the BeanProviderHelper to provide access to the
        // beans of a bean provider as named properties.

        OgnlRuntime.setPropertyAccessor(IBeanProvider.class, new BeanProviderPropertyAccessor());
    }

    /**
     *  The specification used to originally build the component.
     *
     * 
     **/

    private IComponentSpecification _specification;

    /**
     *  The page that contains the component, possibly itself (if the component is
     *  in fact, a page).
     *
     * 
     **/

    private IPage _page;

    /**
     *  The component which contains the component.  This will only be
     *  null if the component is actually a page.
     *
     **/

    private IComponent _container;

    /**
     *  The simple id of this component.
     *
     * 
     **/

    private String _id;

    /**
     *  The fully qualified id of this component.  This is calculated the first time
     *  it is needed, then cached for later.
     *
     **/

    private String _idPath;

    private static final int MAP_SIZE = 5;

    /**
     *  A {@link Map} of all bindings (for which there isn't a corresponding
     *  JavaBeans property); the keys are the names of formal and informal
     *  parameters.
     *
     **/

    private Map _bindings;

    private Map _components;
    private static final int BODY_INIT_SIZE = 5;

    private INamespace _namespace;

    /**
     *  Used in place of JDK 1.3's Collections.EMPTY_MAP (which is not
     *  available in JDK 1.2).
     *
     **/

    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(1));

    /**
     *  The number of {@link IRender} objects in the body of
     *  this component.
     *
     * 
     **/

    private int _bodyCount = 0;

    /**
     *  An aray of elements in the body of this component.
     *
     * 
     **/

    private IRender[] _body;

    /**
     *  The components' asset map.
     *
     **/

    private Map _assets;

    /**
     *  A mapping that allows public instance methods to be dressed up
     *  as {@link IActionListener} listener
     *  objects.
     *
     *  @since 1.0.2
     * 
     **/

    private ListenerMap _listeners;

    /**
     *  A bean provider; these are lazily created as needed.
     *
     *  @since 1.0.4
     * 
     **/

    private IBeanProvider _beans;

    /**
     *  Manages setting and clearing parameter properties for the component.
     * 
     *  @since 2.0.3
     * 
     **/

    private ParameterManager _parameterManager;

    /**
     *  Provides access to localized Strings for this component.
     * 
     *  @since 2.0.4
     * 
     **/

    private IComponentStrings _strings;

    public void addAsset(String name, IAsset asset)
    {
        if (_assets == null)
            _assets = new HashMap(MAP_SIZE);

        _assets.put(name, asset);
    }

    public void addComponent(IComponent component)
    {
        if (_components == null)
            _components = new HashMap(MAP_SIZE);

        _components.put(component.getId(), component);
    }

    /**
     *  Adds an element (which may be static text or a component) as a body
     *  element of this component.  Such elements are rendered
     *  by {@link #renderBody(IMarkupWriter, IRequestCycle)}.
     *
     *  @since 2.2
     * 
     **/

    public void addBody(IRender element)
    {
        // Should check the specification to see if this component
        // allows body.  Curently, this is checked by the component
        // in render(), which is silly.

        if (_body == null)
        {
            _body = new IRender[BODY_INIT_SIZE];
            _body[0] = element;

            _bodyCount = 1;
            return;
        }

        // No more room?  Make the array bigger.

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
     *  Registers this component as a listener of the page if it
     *  implements {@link org.apache.tapestry.event.PageDetachListener} or 
     *  {@link org.apache.tapestry.event.PageRenderListener}.
     * 
     *  <p>
     *  Invokes {@link #finishLoad()}.  Subclasses may overide as needed, but
     *  must invoke this implementation.
     *  {@link BaseComponent}
     *  loads its HTML template. 
     *
     **/

    public void finishLoad(
        IRequestCycle cycle,
        IPageLoader loader,
        IComponentSpecification specification)
    {
        if (this instanceof PageDetachListener)
            _page.addPageDetachListener((PageDetachListener) this);

        if (this instanceof PageRenderListener)
            _page.addPageRenderListener((PageRenderListener) this);

        finishLoad();
    }

    protected void fireObservedChange(String propertyName, int newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, Object newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, boolean newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, double newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, float newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, long newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, char newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, byte newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, short newValue)
    {
        ChangeObserver observer = getChangeObserver();

        if (observer == null)
            return;

        ObservedChangeEvent event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }
    
    /**
     *  @deprecated To be removed in 3.1.  Use 
     *  {@link #renderInformalParameters(IMarkupWriter, IRequestCycle)}
     *  instead.
     * 
     **/
    
	protected void generateAttributes(IMarkupWriter writer, IRequestCycle cycle)
	{
		renderInformalParameters(writer, cycle);
	}
	

    /**
     *  Converts informal parameters into additional attributes on the
     *  curently open tag.
     *
     *  <p>Invoked from subclasses to allow additional attributes to
     *  be specified within a tag (this works best when there is a
     *  one-to-one corespondence between an {@link IComponent} and a
     *  HTML element.
     *
     *  <p>Iterates through the bindings for this component.  Filters
     *  out bindings when the name matches a formal parameter (as of 1.0.5,
     *  informal bindings are weeded out at page load / template load time,
     *  if they match a formal parameter, or a specificied reserved name).
     *  For the most part, all the bindings here are either informal parameter,
     *  or formal parameter without a corresponding JavaBeans property.
     *
     *  <p>For each acceptible key, the value is extracted using {@link IBinding#getObject()}.
     *  If the value is null, no attribute is written.
     *
     *  <p>If the value is an instance of {@link IAsset}, then
     *  {@link IAsset#buildURL(IRequestCycle)} is invoked to convert the asset
     *  to a URL.
     *
     *  <p>Finally, {@link IMarkupWriter#attribute(String,String)} is
     *  invoked with the value (or the URL).
     *
     *  <p>The most common use for informal parameters is to support
     *  the HTML class attribute (for use with cascading style sheets)
     *  and to specify JavaScript event handlers.
     *
     *  <p>Components are only required to generate attributes on the
     *  result phase; this can be skipped during the rewind phase.
     **/

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

    /**
     *  Returns the named binding, or null if it doesn't exist.
     *
     *  All components are expected to have a read/write accessor for each
     *  formal parameter (such accessors are fabricated as needed).  Formal
     *  parameters are accessed via this property, informal parameters are
     *  stored in a Map.
     *
     *  @see #setBinding(String,IBinding)
     *
     **/

    public IBinding getBinding(String name)
    {
        if (_specification.getParameter(name) != null)
        {
            IResourceResolver resolver = getPage().getEngine().getResourceResolver();
            String bindingPropertyName = name + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX;

            return (IBinding) OgnlUtils.get(bindingPropertyName, resolver, this);
        }

        if (_bindings == null)
            return null;

        return (IBinding) _bindings.get(name);
    }

    /**
     *  Return's the page's change observer.  In practical terms, this
     *  will be an {@link IPageRecorder}.
     *
     *  @see IPage#getChangeObserver()
     *
     **/

    public ChangeObserver getChangeObserver()
    {
        return _page.getChangeObserver();
    }

    public IComponent getComponent(String id)
    {
        IComponent result = null;

        if (_components != null)
            result = (IComponent) _components.get(id);

        if (result == null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("no-such-component", this, id),
                this);

        return result;
    }

    public IComponent getContainer()
    {
        return _container;
    }

    public void setContainer(IComponent value)
    {
        if (_container != null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractComponent.attempt-to-change-container"));

        _container = value;
    }

    /**
     *  Returns the name of the page, a slash, and this component's id path.
     *  Pages are different, they simply return their name.
     *
     *  @see #getIdPath()
     *
     **/

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
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractComponent.attempt-to-change-component-id"));

        _id = value;
    }

    public String getIdPath()
    {
        String containerIdPath;

        if (_container == null)
            throw new NullPointerException(
                Tapestry.getString("AbstractComponent.null-container", this));

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
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractComponent.attempt-to-change-page"));

        _page = value;
    }

    public IComponentSpecification getSpecification()
    {
        return _specification;
    }

    public void setSpecification(IComponentSpecification value)
    {
        if (_specification != null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractComponent.attempt-to-change-spec"));

        _specification = value;
    }

    /**
     *  Renders all elements wrapped by the receiver.
     *
     **/

    public void renderBody(IMarkupWriter writer, IRequestCycle cycle)
    {
        for (int i = 0; i < _bodyCount; i++)
            _body[i].render(writer, cycle);
    }

    /**
     *  Adds the binding with the given name, replacing any existing binding
     *  with that name.
     *
     *  <p>Formal parameters will have an read/write property.
     *
     *  <p>Informal parameters should <em>not</em> be stored in
     *  instance variables if @link
     *  #generateAttribute(IMarkupWriter, String[]) is to be used.
     *  It relies on using the collection of bindings (to store informal parameters).
     **/

    public void setBinding(String name, IBinding binding)
    {
        if (_specification.getParameter(name) != null)
        {
            String bindingPropertyName = name + Tapestry.PARAMETER_PROPERTY_NAME_SUFFIX;
            IResourceResolver resolver = getPage().getEngine().getResourceResolver();
            OgnlUtils.set(bindingPropertyName, resolver, this, binding);
            return;
        }

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
     *  Returns an unmodifiable {@link Map} of components, keyed on component id.
     *  Never returns null, but may return an empty map.  The returned map is
     *  immutable.
     *
     **/

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

        List names = _specification.getParameterNames();

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
     *
     *  Returns a {@link Map} of all bindings for this component.  This implementation
     *  is expensive, since it has to merge the disassociated bindings (informal parameters,
     *  and parameters without a JavaBeans property) with the associated bindings (formal
     *  parameters with a JavaBeans property).
     *
     * @since 1.0.5
     *
     **/

    public Map getBindings()
    {
        Map result = new HashMap();

        // Add any informal parameters.

        if (_bindings != null)
            result.putAll(_bindings);

        // Now work on the formal parameters

        Iterator i = _specification.getParameterNames().iterator();
        while (i.hasNext())
        {
            String name = (String) i.next();

            if (result.containsKey(name))
                continue;

            IBinding binding = getBinding(name);

            if (binding != null)
                result.put(name, binding);
        }

        return result;
    }

    /**
     *  Returns a {@link ListenerMap} for the component.  A {@link ListenerMap} contains a number of
     *  synthetic read-only properties that implement the {@link IActionListener} 
     *  interface, but in fact, cause public instance methods to be invoked.
     *
     *  @since 1.0.2
     **/

    public ListenerMap getListeners()
    {
        if (_listeners == null)
            _listeners = new ListenerMap(this);

        return _listeners;
    }

    /**
     *  Returns the {@link IBeanProvider} for this component.  This is lazily created the
     *  first time it is needed.
     *
     *  @since 1.0.4
     *
     **/

    public IBeanProvider getBeans()
    {
        if (_beans == null)
            _beans = new BeanProvider(this);

        return _beans;
    }

    /**
     * 
     *  Invoked, as a convienience, 
     *  from {@link #finishLoad(IRequestCycle, IPageLoader, IComponentSpecification)}.
     *  This implemenation does nothing.  Subclasses may override without invoking
     *  this implementation.
     * 
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
    }

    /**
     *  The main method used to render the component.  
     *  Invokes {@link #prepareForRender(IRequestCycle)}, then
     *  {@link #renderComponent(IMarkupWriter, IRequestCycle)}.
     *  {@link #cleanupAfterRender(IRequestCycle)} is invoked in a 
     *  <code>finally</code> block.
     * 	 
     *  <p>Subclasses should not override this method; instead they
     *  will implement {@link #renderComponent(IMarkupWriter, IRequestCycle)}.
     * 
     *  @since 2.0.3
     * 
     **/

    public final void render(IMarkupWriter writer, IRequestCycle cycle)
    {
        try
        {
            prepareForRender(cycle);

            renderComponent(writer, cycle);
        }
        finally
        {
            cleanupAfterRender(cycle);
        }
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter, IRequestCycle)}
     *  to prepare the component to render.  This implementation
     *  sets JavaBeans properties from matching bound parameters.
     *  Subclasses that override this method must invoke this
     *  implementation as well.
     * 
     *  @since 2.0.3
     * 
     **/

    protected void prepareForRender(IRequestCycle cycle)
    {
        if (_parameterManager == null)
        {
            // Pages inherit from this class too, but pages (by definition)
            // never have parameters.

            if (getSpecification().isPageSpecification())
                return;

            _parameterManager = new ParameterManager(this);
        }

        _parameterManager.setParameters(cycle);
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter, IRequestCycle)}
     *  to actually render the component (with any parameter values
     *  already set).  This is the method that subclasses must implement.
     * 
     *  @since 2.0.3
     * 
     **/

    protected abstract void renderComponent(IMarkupWriter writer, IRequestCycle cycle);

    /**
     *  Invoked by {@link #render(IMarkupWriter, IRequestCycle)}
     *  after the component renders, to clear any parameters back to
     *  null (or 0, or false, or whatever the correct default is).  
     *  Primarily, this is used to ensure
     *  that the component doesn't hold onto any objects that could
     *  otherwise be garbage collected.
     * 
     *  <p>Subclasses may override this implementation, but must
     *  also invoke it.
     * 
     *  @since 2.0.3
     * 
     **/

    protected void cleanupAfterRender(IRequestCycle cycle)
    {
        if (_parameterManager != null)
            _parameterManager.resetParameters(cycle);
    }

    /** @since 3.0 **/

    public IComponentStrings getStrings()
    {
        if (_strings == null)
            _strings = getPage().getEngine().getComponentStringsSource().getStrings(this);

        return _strings;
    }

    /**
     *  Obtains the {@link IComponentStrings} for this component
     *  (if necessary), and gets the string from it.
     * 
     **/

    public String getString(String key)
    {
        return getStrings().getString(key);
    }

    /**
     *  Formats a string, using
     *  {@link MessageFormat#format(java.lang.String, java.lang.Object[])}.
     * 
     *  @param key the key used to obtain a localized pattern using
     *  {@link #getString(String)}
     *  @param arguments passed to the formatter
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object[] arguments)
    {
        return getStrings().format(key, arguments);
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument)
    {
        return getStrings().format(key, argument);
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument1, Object argument2)
    {
        return getStrings().format(key, argument1, argument2);
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument1, Object argument2, Object argument3)
    {
        return getStrings().format(key, new Object[] { argument1, argument2, argument3 });
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
     *  Returns the body of the component, the element (which may be static HTML or components)
     *  that the component immediately wraps.  May return null.  Do not modify the returned
     *  array.  The array may be padded with nulls.
     * 
     *  @since 2.3
     *  @see #getBodyCount()
     * 
     **/

    public IRender[] getBody()
    {
        return _body;
    }

    /**
     *  Returns the active number of elements in the the body, which may be zero.
     * 
     *  @since 2.3
     *  @see #getBody()
     * 
     **/

    public int getBodyCount()
    {
        return _bodyCount;
    }
}