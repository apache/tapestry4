//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import net.sf.tapestry.bean.BeanProvider;
import net.sf.tapestry.bean.BeanProviderHelper;
import net.sf.tapestry.event.ChangeObserver;
import net.sf.tapestry.event.ObservedChangeEvent;
import net.sf.tapestry.listener.ListenerMap;
import net.sf.tapestry.param.ParameterManager;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.spec.ContainedComponent;
import net.sf.tapestry.util.prop.IPropertyAccessor;
import net.sf.tapestry.util.prop.PropertyHelper;

/**
 *  Abstract base class implementing the {@link IComponent} interface.
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 * 
 **/

public abstract class AbstractComponent implements IComponent
{
    static {
        // Register the BeanProviderHelper to provide access to the
        // beans of a bean provider as named properties.

        PropertyHelper.register(IBeanProvider.class, BeanProviderHelper.class);
    }

    /**
     *  The specification used to originally build the component.
     *
     *  @deprecated To be made private in 2.3.
     * 
     **/

    protected ComponentSpecification specification;

    /**
     *  The page that contains the component, possibly itself (if the component is
     *  in fact, a page).
     *
     *  @deprecated To be made private in 2.3.
     * 
     **/

    protected IPage page;

    /**
     *  The component which contains the component.  This will only be
     *  null if the component is actually a page.
     *
     **/

    private IComponent _container;

    /**
     *  The simple id of this component.
     *
     *  @deprecated To be made private in 2.3.
     * 
     **/

    protected String id;

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
    private static final int WRAPPED_INIT_SIZE = 5;

    private INamespace _namespace;

    /**
     *  Used in place of JDK 1.3's Collections.EMPTY_MAP (which is not
     *  available in JDK 1.2).
     *
     **/

    private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(1));

    /**
     *  The number of {@link IRender} objects wrapped by
     *  this component.
     *
     *  @deprecated To be made private in 2.3.
     * 
     **/

    protected int wrappedCount = 0;

    /**
     *  An aray of elements wrapped by this component.
     *
     *  @deprecated To be made private in 2.3.
     * 
     **/

    protected IRender[] wrapped;

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
     *  Adds an element (which may be static text or a component) as a wrapped
     *  element of this component.  Such elements are rendered
     *  by {@link #renderWrapped(IMarkupWriter, IRequestCycle)}.
     *
     **/

    public void addWrapped(IRender element)
    {
        // Should check the specification to see if this component
        // allows body.  Curently, this is checked by the component
        // in render(), which is silly.

        if (wrapped == null)
        {
            wrapped = new IRender[WRAPPED_INIT_SIZE];
            wrapped[0] = element;

            wrappedCount = 1;
            return;
        }

        // No more room?  Make the array bigger.

        if (wrappedCount == wrapped.length)
        {
            IRender[] newWrapped;

            newWrapped = new IRender[wrapped.length * 2];

            System.arraycopy(wrapped, 0, newWrapped, 0, wrappedCount);

            wrapped = newWrapped;
        }

        wrapped[wrappedCount++] = element;
    }

    /**
     *  Invokes {@link #finishLoad()}.  Subclasses may overide as needed, but
     *  must invoke this implementation.
     *  {@link BaseComponent}
     * loads its HTML template. 
     *
     **/

    public void finishLoad(IRequestCycle cycle, IPageLoader loader, ComponentSpecification specification) throws PageLoaderException
    {
        finishLoad();
    }

    protected void fireObservedChange(String propertyName, int newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, Object newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, boolean newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, double newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, float newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, long newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, char newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, byte newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    protected void fireObservedChange(String propertyName, short newValue)
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this, propertyName, newValue);

        observer.observeChange(event);
    }

    /**
     *  Fires a change event for no single property; the receiver should
     *  note that the page containing the component is 'dirty' even if
     *  no property appears to have changed.  This is useful in situations
     *  when a property is a mutable object (such as a Collection) and the
     *  state of the property value is changing, even though the property
     *  is not.
     *
     **/

    protected void fireObservedChange()
    {
        ChangeObserver observer;
        ObservedChangeEvent event;

        observer = getChangeObserver();

        if (observer == null)
            return;

        event = new ObservedChangeEvent(this);

        observer.observeChange(event);
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

    protected void generateAttributes(IMarkupWriter writer, IRequestCycle cycle)
    {
        String attribute;

        if (_bindings == null)
            return;

        Iterator i = _bindings.entrySet().iterator();

        while (i.hasNext())
        {
            Map.Entry entry = (Map.Entry) i.next();
            String name = (String) entry.getKey();

            // Skip over formal parameters stored in the bindings
            // Map.  We're just interested in informal parameters.

            if (specification.getParameter(name) != null)
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

    /**
     *  Returns the named binding, or null if it doesn't exist.
     *
     *  <p>This method looks for a JavaBeans property with an
     *  appropriate name, of type {@link IBinding}.  The property
     *  should be named <code><i>name</i>Binding</code>.  If it exists
     *  and is both readable and writable, then it is accessor method
     *  is invoked.  Components which implement such methods can
     *  access their own binding through their instance variables
     *  instead of invoking this method, a performance optimization.
     *
     *  @see #setBinding(String,IBinding)
     *
     **/

    public IBinding getBinding(String name)
    {
        PropertyHelper helper;
        IPropertyAccessor accessor;

        helper = PropertyHelper.forClass(getClass());
        accessor = helper.getAccessor(this, name + "Binding");

        if (accessor != null && accessor.isReadWrite() && accessor.getType().equals(IBinding.class))
            return (IBinding) accessor.get(this);

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
        return page.getChangeObserver();
    }

    public IComponent getComponent(String id)
    {
        IComponent result = null;

        if (_components != null)
            result = (IComponent) _components.get(id);

        if (result == null)
            throw new NoSuchComponentException(id, this);

        return result;
    }

    public IComponent getContainer()
    {
        return _container;
    }

    public void setContainer(IComponent value)
    {
        if (_container != null)
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractComponent.attempt-to-change-container"));

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
        return page.getName() + "/" + getIdPath();
    }

    public String getId()
    {
        return id;
    }

    public void setId(String value)
    {
        if (id != null)
            throw new ApplicationRuntimeException(
                Tapestry.getString("AbstractComponent.attempt-to-change-component-id"));

        id = value;
    }

    public String getIdPath()
    {
        String containerIdPath;

        if (_container == null)
            throw new NullPointerException(Tapestry.getString("AbstractComponent.null-container", this));

        containerIdPath = _container.getIdPath();

        if (containerIdPath == null)
            _idPath = id;
        else
            _idPath = containerIdPath + "." + id;

        return _idPath;
    }

    public IPage getPage()
    {
        return page;
    }

    public void setPage(IPage value)
    {
        if (page != null)
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractComponent.attempt-to-change-page"));

        page = value;
    }

    public ComponentSpecification getSpecification()
    {
        return specification;
    }

    public void setSpecification(ComponentSpecification value)
    {
        if (specification != null)
            throw new ApplicationRuntimeException(Tapestry.getString("AbstractComponent.attempt-to-change-spec"));

        specification = value;
    }

    /**
     *  Renders all elements wrapped by the receiver.
     *
     **/

    public void renderWrapped(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
    {
        for (int i = 0; i < wrappedCount; i++)
            wrapped[i].render(writer, cycle);
    }

    /**
     *  Adds the binding with the given name, replacing any existing binding
     *  with that name.
     *
     *  <p>This method checks to see if a matching JavaBeans property
     *  (with a name of <code><i>name</i>Binding</code> and a type of
     *  {@link IBinding}) exists.  If so, that property is updated.
     *  An optimized component can simply implement accessor and
     *  mutator methods and then access its bindings via its own
     *  instance variables, rather than going through {@link
     *  #getBinding(String)}.
     *
     *  <p>Informal parameters should <em>not</em> be stored in
     *  instance variables if @link
     *  #generateAttribute(IMarkupWriter, String[]) is to be used.
     *  It relies on using the collection of bindings (to store informal parameters).
     **/

    public void setBinding(String name, IBinding binding)
    {
        PropertyHelper helper;
        IPropertyAccessor accessor;

        helper = PropertyHelper.forClass(getClass());
        accessor = helper.getAccessor(this, name + "Binding");

        if (accessor != null && accessor.getType().equals(IBinding.class))
        {
            accessor.set(this, binding);
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

        ContainedComponent contained = _container.getSpecification().getComponent(id);

        // If no informal parameters, then it's safe to return
        // just the names of the formal parameters.

        if (_bindings == null || _bindings.size() == 0)
            return contained.getBindingNames();

        // The new HTML parser means that sometimes, the informal attributes
        // come from the HTML template and aren't known in the contained component
        // specification.  The only thing to do is to build up a set of
        // informal bindings.  A degenerate case:  an informal binding that has
        // setXXXBinding and getXXXBinding methods --- that makes the
        // informal parameter invisible to this method (and thus, to the Inspector).

        HashSet result = new HashSet(contained.getBindingNames());

        // All the informal bindings go into the bindings Map.   Also
        // formal parameters where there isn't a corresponding JavaBeans property.

        result.addAll(_bindings.keySet());

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

        // Add any informal parameters, as well as any formal parameters
        // that don't have a correspoinding JavaBeans property.

        if (_bindings != null)
            result.putAll(_bindings);

        // Now work on the formal parameters

        Iterator i = specification.getParameterNames().iterator();
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
     *  from {@link #finishLoad(IRequestCycle, IPageLoader, ComponentSpecification)}.
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

    public final void render(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException
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

    protected void prepareForRender(IRequestCycle cycle) throws RequestCycleException
    {
        if (_parameterManager == null)
            _parameterManager = new ParameterManager(this);

        _parameterManager.setParameters();
    }

    /**
     *  Invoked by {@link #render(IMarkupWriter, IRequestCycle)}
     *  to actually render the component (with any parameter values
     *  already set).  This is the method that subclasses must implement.
     * 
     *  @since 2.0.3
     * 
     **/

    protected abstract void renderComponent(IMarkupWriter writer, IRequestCycle cycle) throws RequestCycleException;

    /**
     *  Invoked by {@link #render(IMarkupWriter, IRequestCycle)}
     *  after the component renders, to clear any parameters back to
     *  null (or 0, or false).  Primarily, this is used to ensure
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
        _parameterManager.clearParameters();
    }

    /**
     *  Obtains the {@link IComponentStrings} for this component
     *  (if necessary), and gets the string from it.
     * 
     **/

    public String getString(String key)
    {
        if (_strings == null)
            _strings = getPage().getEngine().getComponentStringsSource().getStrings(this);

        return _strings.getString(key);
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
        String format = getString(key);

        return MessageFormat.format(format, arguments);
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument)
    {
        return formatString(key, new Object[] { argument });
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument1, Object argument2)
    {
        return formatString(key, new Object[] { argument1, argument2 });
    }

    /**
     *  Convienience method for {@link #formatString(String, Object[])}
     * 
     *  @since 2.2
     * 
     **/

    public String formatString(String key, Object argument1, Object argument2, Object argument3)
    {
        return formatString(key, new Object[] { argument1, argument2, argument3 });
    }
    
    public INamespace getNamespace()
    {
        return _namespace;
    }

    public void setNamespace(INamespace namespace)
    {
        _namespace = namespace;
    }

}