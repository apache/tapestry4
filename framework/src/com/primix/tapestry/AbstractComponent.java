package com.primix.tapestry;

import java.io.Serializable;
import com.primix.foundation.prop.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.spec.*;
import java.util.*;
/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

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
		// Force the MapHelper class to exist, which gets it registered
		// with the PropertyHelper.

		MapHelper.class.getName();
	}

	/**
	*  The specification used to originally build the component.
	*
	*/

	protected final ComponentSpecification specification;

	/**
	*  The page that contains the component, possibly itself (if the component is
	*  in fact, a page).
	*
	*/

	protected final IPage page;

	/**
	*  The component which contains the component.  This will only be
	*  null if the component is actually a page.
	*
	*/

	private final IComponent container;

	/**
	*  The simple id of this component.
	*
	*/

	protected final String id;

	/**
	*  The fully qualified id of this component.  This is calculated the first time
	*  it is needed, then cached for later.
	*
	*/

	protected String idPath;

	private static final int MAP_SIZE = 5;

	/**
	 *  A {@link Map} of all bindings; the keys are the names of formal and informal
	 *  parameters.
	 *
	 */
	 
	private Map bindings;
	
	/**
	 *  An unmodifiable {@link Collection} of the names of all bindings (the
	 *  keys of the bindings map).
	 *
	 */
	 
	private Collection bindingNames;

	private Map components;
	private Map safeComponents;

	private static final int WRAPPED_INIT_SIZE = 5;

	/**
	*  The number of {@link IRender} objects wrapped by
	*  this component.
	*
	*/

	protected int wrappedCount = 0;

	/**
	*  An array of elements wrapped by this component.
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
	*  Used as a set to filter out informal parameters.  The value is
	*  always Boolean.TRUE (we're testing for existence).  The values
	*  are the names of all formal parameters and the names of all
	*  reserved attributes.  Each name is converted to all lower-case
	*  for storage.
	*
	*/

	private Set maskedAttributes;

	private static Map maskedAttributesStorage;

	/**
	*  Standard constructor for all components.
	*
	*  <p>This constructor also initializes the assets for the
	*  component from the specification.
	*
	* @param page The page to which the new component belongs.  This
	* may only be null when creating a new page.
	*
	* @param container The container of the new component.  This will
	* only be null when creating a page.
	*
	* @param name The name of the component within its container
	* component.
	*
	* @param specification The ComponentSpecification for the
	* component.
	*
	*/

	public AbstractComponent(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		this.page = page;
		this.container = container;
		this.id = id;
		this.specification = specification;

		setupAssets();
	}

	public void addComponent(String name, IComponent component)
	{
		if (components == null)
			components = new HashMap(MAP_SIZE);

		components.put(name, component);	
	}

	public void addWrapped(IRender element)
	{
		// Should check the specification to see if this component
		// allows body.  Currently, this is checked by the component
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

	private void buildMaskedAttributes(String[] reservedNames)
	{
		String className;
		Iterator e;
		String parameterName;
		int i;	

		className = getClass().getName();

		if (maskedAttributesStorage != null)
			maskedAttributes = (Set)maskedAttributesStorage.get(className);

		if (maskedAttributes != null)
			return;

		maskedAttributes = new HashSet(MAP_SIZE);

		e = specification.getParameterNames().iterator();
		while (e.hasNext())
		{
			parameterName = (String)e.next();

			maskedAttributes.add(parameterName.toLowerCase());
		}

		if (reservedNames != null)
		{
			for (i = 0; i < reservedNames.length; i++)
				maskedAttributes.add(reservedNames[i].toLowerCase());
		}

		// This is problematic!  It should be SYNCHRONIZED.
        
		if (maskedAttributesStorage == null)
			maskedAttributesStorage = new HashMap(MAP_SIZE);

		maskedAttributesStorage.put(className, maskedAttributes);	
	}

	/**
	*  Does nothing.  Subclasses may override as needed.
	*
	*  @see ILifecycle
	*
	*/

	public void cleanupAfterRender(IRequestCycle cycle)
	{
	}

	/**
	*  Does nothing.  Subclasses may override as needed.
	*
	*  @see ILifecycle
	*
	*/

	public void finishLoad(IPageLoader loader, ComponentSpecification specification)
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
	*  currently open tag.
	* 
	*  <p>Invoked from subclasses to allow additional attributes to
	*  be specified within a tag (this works best when there is a
	*  one-to-one correspondence between an {@link IComponent} and a
	*  HTML element.
	*
	*  <p>Iterates through the bindings for this component.  Filters
	*  out bindings when the name matches a formal parameter, or any
	*  value provided in the reservedNames array (which may be null).
	*  Reserved names correspond to attributes that are or may be
	*  produced by the component.  Filtering is case-insensitive.
	*
	*  <p>For each acceptible key, the value is extracted using {@link IBinding#getValue()}.
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

	protected void generateAttributes(IRequestCycle cycle, IResponseWriter writer, 
		String[] reservedNames)
	{
		Iterator i;
		String key;
		IBinding binding;
		Object value;
		String attribute;
		IAsset asset;

		if (bindings == null)
			return;

		i = bindings.keySet().iterator();

		while (i.hasNext())
		{
			if (maskedAttributes == null)
				buildMaskedAttributes(reservedNames);

			key = (String)i.next();

			if (maskedAttributes.contains(key.toLowerCase()))
				continue;

			binding = (IBinding)bindings.get(key);

			value = binding.getValue();
			if (value == null)
				continue;
			
			if (value instanceof IAsset)
			{
				asset = (IAsset)value;
				
				// Get the URL of the asset and insert that.
				
				attribute = asset.buildURL(cycle);
			}
			else
				attribute = value.toString();
			
			writer.attribute(key, attribute);

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

	public ComponentSpecification getSpecification()
	{
		return specification;
	}

	/**
	*  Does nothing.  Subclasses may override.
	*
	*  @see ILifecycle
	*
	*/

	public void prepareForRender(IRequestCycle cycle)
	{
		// Does nothing.
	}

	/**
	*  Does nothing.  Subclasses may override.
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
		int i;

		for (i = 0; i < wrappedCount; i++)
			wrapped[i].render(writer, cycle);
	}

	/**
	*  Does nothing.  Subclasses may override as needed.
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

	private void setupAssets()
	{
		Iterator i;
		String name;
		AssetSpecification spec;
		IAsset asset;

		i = specification.getAssetNames().iterator();
        
		while (i.hasNext())
		{
			name = (String)i.next();
            
			spec = (AssetSpecification)specification.getAsset(name);
			asset = spec.getAsset();

			if (assets == null)
				assets = new HashMap(MAP_SIZE);

			assets.put(name, asset);
		}			
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
		// Note: Collections.EMPTY_MAP not available until JDK 1.3 ... and
		// we're trying for JDK 1.2 compatibility.

		if (components == null)
			return new HashMap();	
		
		if (safeComponents == null)
			safeComponents = Collections.unmodifiableMap(components);
			
		return safeComponents;
	}
	
	public Map getAssets()
	{
    	// Note: Collections.EMPTY_MAP not available until JDK 1.3 ... and
        // we're trying for JDK 1.2 compatibility.
        
    	if (assets == null)
        	return new HashMap();
            		        
		if (safeAssets == null)
			safeAssets = Collections.unmodifiableMap(assets);
		
		return safeAssets;	
	}
	
	public Collection getBindingNames()
	{
		if (bindings == null)
			return null;
		
		if (bindingNames == null)
			bindingNames = Collections.unmodifiableCollection(bindings.keySet());
		
		return bindingNames;	
	}	
}
