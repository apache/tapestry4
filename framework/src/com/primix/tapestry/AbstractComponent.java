/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 *
 * This library is free software.
 *
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.tapestry;

import java.io.Serializable;
import com.primix.tapestry.util.prop.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.listener.*;
import com.primix.tapestry.bean.*;
import java.util.*;


/**
 *  Abstract base class implementing the {@link IComponent} interface.
 *
 * <p>In addition, this class implements empty stubs for the methods of
 * the {@link ILifecycle} interface.  Subclasses will still need
 * to implement the interface for any of the methods to be invoked.
 *
 * @author Howard Ship
 * @version $Id$
 */

public abstract class AbstractComponent implements IComponent
{	
	static
	{
		// Register the BeanProviderHelper to provide access to the
		// beans of a bean provider as named properties.
		
		PropertyHelper.register(IBeanProvider.class, BeanProviderHelper.class);
	}
	
	/**
	 *  The specification used to originally build the component.
	 *
	 */
	
	protected ComponentSpecification specification;
	
	/**
	 *  The page that contains the component, possibly itself (if the component is
	 *  in fact, a page).
	 *
	 */
	
	protected IPage page;
	
	/**
	 *  The component which contains the component.  This will only be
	 *  null if the component is actually a page.
	 *
	 */
	
	private IComponent container;
	
	/**
	 *  The simple id of this component.
	 *
	 */
	
	protected String id;
	
	/**
	 *  The fully qualified id of this component.  This is calculated the first time
	 *  it is needed, then cached for later.
	 *
	 */
	
	private String idPath;
	
	private static final int MAP_SIZE = 5;
	
	/**
	 *  A {@link Map} of all bindings; the keys are the names of formal and informal
	 *  parameters.
	 *
	 */
	
	private Map bindings;
	
	private Map components;
	private Map safeComponents;
	
	private static final int WRAPPED_INIT_SIZE = 5;
	
	/**
	 *  Used in place of JDK 1.3's Collections.EMPTY_MAP (which is not
	 *  available in JDK 1.2).
	 *
	 */
	
	private static final Map EMPTY_MAP = Collections.unmodifiableMap(new HashMap(1));
	
	/**
	 *  The number of {@link IRender} objects wrapped by
	 *  this component.
	 *
	 */
	
	protected int wrappedCount = 0;
	
	/**
	 *  An aray of elements wrapped by this component.
	 *
	 */
	
	protected IRender[] wrapped;
	
	/**
	 *  The components' asset map.
	 *
	 */
	
	private Map assets;
	private Map safeAssets;
	
	/**
	 *  A mapping that allows public instance methods to be dressed up
	 *  as {@link IActionListener} or {@link IDirectListener} listener
	 *  objects.
	 *
	 *  @since 1.0.2
	 */
	
	private ListenerMap listeners;

	/**
	 * A bean provider; these are lazily created as needed.
	 *
	 * @since 1.0.4
	 */
	
	private IBeanProvider beans;
	
    public void addAsset(String name, IAsset asset)
    {
		if (assets == null)
			assets = new HashMap(MAP_SIZE);
		
		assets.put(name, asset);
    }
	
	public void addComponent(IComponent component)
	{
		if (components == null)
			components = new HashMap(MAP_SIZE);
		
		components.put(component.getId(), component);
	}
	
    /**
	 *  Adds an element (which may be static text or a component) as a wrapped
	 *  element of this component.  Such elements are rendered
	 *  by {@link #renderWrapped(IResponseWriter, IRequestCycle)}.
	 *
	 */
	
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
	 *  Does nothing.  Subclasses may overide as needed.
	 *
	 *  @see ILifecycle
	 *
	 */
	
	public void cleanupAfterRender(IRequestCycle cycle)
	{
	}
	
	/**
	 *  Does nothing.  Subclasses may overide as needed.  {@link BaseComponent}
	 * loads its HTML template.
	 *
	 */
	
	public void finishLoad(IPageLoader loader, ComponentSpecification specification)
		throws PageLoaderException
	{
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
	 */
	
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
	 *  <p>Finally, {@link IResponseWriter#attribute(String,String)} is
	 *  invoked with the value (or the URL).
	 *
	 *  <p>The most common use for informal parameters is to support
	 *  the HTML class attribute (for use with cascading style sheets)
	 *  and to specify JavaScript event handlers.
	 *
	 *  <p>Components are only required to generate attributes on the
	 *  result phase; this can be skipped during the rewind phase.
	 */
	
	protected void generateAttributes(IResponseWriter writer, IRequestCycle cycle)
	{
		String attribute;
		
		if (bindings == null)
			return;
		
		Iterator i = bindings.entrySet().iterator();
		
		while (i.hasNext())
		{			
			Map.Entry entry = (Map.Entry)i.next();
			String name = (String)entry.getKey();
			
			// Skip over formal parameters stored in the bindings
			// Map.  We're just interested in informal parameters.
			
			if (specification.getParameter(name) != null)
				continue;
			
			IBinding binding = (IBinding)entry.getValue();
			
			Object value = binding.getObject();
			if (value == null)
				continue;
			
			if (value instanceof IAsset)
			{
				IAsset asset = (IAsset)value;
				
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
	 */
	
	public IBinding getBinding(String name)
	{
		PropertyHelper helper;
		IPropertyAccessor accessor;
		
		helper = PropertyHelper.forClass(getClass());
		accessor = helper.getAccessor(this, name + "Binding");
		
		if (accessor != null &&
			accessor.isReadWrite() &&
			accessor.getType().equals(IBinding.class))
		{
			return (IBinding)accessor.get(this);
		}
		
		if (bindings == null)
			return null;
		
		return (IBinding)bindings.get(name);
	}
	
	/**
	 *  Return's the page's change observer.  In practical terms, this
	 *  will be an {@link IPageRecorder}.
	 *
	 *  @see IPage#getChangeObserver()
	 *
	 */
	
	public ChangeObserver getChangeObserver()
	{
		return page.getChangeObserver();
	}
	
	public IComponent getComponent(String id)
	{
		IComponent result = null;
		
		if (components != null)
			result = (IComponent)components.get(id);
		
		if (result == null)
			throw new NoSuchComponentException(id, this);
		
		return result;
	}
	
	public IComponent getContainer()
	{
		return container;
	}
	
    public void setContainer(IComponent value)
    {
		if (container != null)
			throw new ApplicationRuntimeException("Attempt to change existing container.");
		
		container = value;
    }
	
	/**
	 *  Returns the name of the page, a slash, and this component's id path.
	 *  Pages are different, they simply return their name.
	 *
	 *  @see #getIdPath()
	 *
	 */
	
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
			throw new ApplicationRuntimeException("Attempt to change existing component id.");
		
		id = value;
    }
	
	public String getIdPath()
	{
		String containerIdPath;
		
		if (container == null)
			throw new NullPointerException(this + " container is null.");
		
		containerIdPath = container.getIdPath();
		
		if (containerIdPath == null)
			idPath = id;
		else
			idPath = containerIdPath + "." + id;
		
		return idPath;
	}
	
	public IPage getPage()
	{
		return page;
	}
	
    public void setPage(IPage value)
    {
		if (page != null)
			throw new ApplicationRuntimeException("Attempt to change existing containing page.");
		
		page = value;
    }
	
	public ComponentSpecification getSpecification()
	{
		return specification;
	}
	
    public void setSpecification(ComponentSpecification value)
    {
		if (specification != null)
			throw new ApplicationRuntimeException("Attempt to change existing component specification.");
		
		specification = value;
    }
	
	/**
	 *  Does nothing.  Subclasses may overide.
	 *
	 *  @see ILifecycle
	 *
	 */
	
	public void prepareForRender(IRequestCycle cycle)
	{
		// Does nothing.
	}
	
	/**
	 *  Does nothing.  Subclasses may overide.
	 *
	 *  @see ILifecycle
	 *
	 */
	
	public void cleanupComponent()
	{
		// Does nothing.
	}
	
	/**
	 *  Renders all elements wrapped by the receiver.
	 *
	 */
	
	public void renderWrapped(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		for (int i = 0; i < wrappedCount; i++)
			wrapped[i].render(writer, cycle);
	}
	
	/**
	 *  Does nothing.  Subclasses may overide as needed.
	 *
	 *  @see ILifecycle
	 *
	 */
	
	public void reset()
	{
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
	 *  #generateAttribute(IResponseWriter, String[]) is to be used.
	 *  It relies on using the collection of bindings (to store informal parameters).
	 */
	
	public void setBinding(String name, IBinding binding)
	{
		PropertyHelper helper;
		IPropertyAccessor accessor;
		
		helper = PropertyHelper.forClass(getClass());
		accessor = helper.getAccessor(this, name + "Binding");
		
		if (accessor != null &&
			accessor.getType().equals(IBinding.class))
		{
			accessor.set(this, binding);
			return;
		}
		
		if (bindings == null)
			bindings = new HashMap(MAP_SIZE);
		
		bindings.put(name, binding);
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
	 *
	 */
	
	public Map getComponents()
	{
		if (components == null)
			return EMPTY_MAP;
		
		if (safeComponents == null)
			safeComponents = Collections.unmodifiableMap(components);
		
		return safeComponents;
	}
	
	public Map getAssets()
	{
		if (assets == null)
			return EMPTY_MAP;
		
		if (safeAssets == null)
			safeAssets = Collections.unmodifiableMap(assets);
		
		return safeAssets;
	}
	
	public IAsset getAsset(String name)
	{
		if (assets == null)
			return null;
		
		return (IAsset)assets.get(name);
	}
	
	public Collection getBindingNames()
	{
		// If no conainer, i.e. a page, then no bindings.
		
		if (container == null)
			return null;
		
		ContainedComponent contained = container.getSpecification().getComponent(id);
	
		// If no informal parameters, then it's safe to return
		// just the names of the formal parameters.
		
		if (bindings == null || bindings.size() == 0)
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
		
		result.addAll(bindings.keySet());
		
		return result;
	}
	
	/**
	 *  Returns a {@link ListenerMap} for the component.  A {@link ListenerMap} contains a number of
	 *  synthetic read-only properties that implement the {@link IActionListener} and/or {@link IDirectListener}
	 *  interfaces, but in fact, cause public instance methods to be invoked.
	 *
	 *  @since 1.0.2
	 */
	
	public ListenerMap getListeners()
	{
		if (listeners == null)
			listeners = new ListenerMap(this);
		
		return listeners;
	}
	
	/**
	 *  Returns the {@link IBeanProvider} for this component.  This is lazily created the
	 *  first time it is needed.
	 *
	 *  @since 1.0.4
	 *
	 */
	
	public IBeanProvider getBeans()
	{
		if (beans == null)
			beans = new BeanProvider(this);
		
		return beans;
	}
}
