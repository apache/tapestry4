package com.primix.tapestry.spec;

import com.primix.tapestry.*;
import com.primix.foundation.*;
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
 *  A specification for a component, as read from an XML specification file.
 *
 *  <p>A specification consists of
 *  <ul>
 *  <li>An implementing class
 *  <li>An optional template
 * <li>A set of contained components
 * <li>Bindings for the properties of each contained component
 * <li>A set of named assets
 * </ul>
 *
 * <p>From this information, an actual component may be instantiated and
 *  initialized.  Instantiating a component is usually a recursive process, since
 *  to initialize a container component, it is necessary to instantiate and initialize
 *  its contained components as well.
 *
 *  @see IComponent
 *  @see ContainedComponent
 *  @see IPageLoader
 *
 * @author Howard Ship
 * @version $Id$
 */

public class ComponentSpecification
{
	private String componentClassName;
	private String specificationResourcePath;

	private Map components;

	private static final int MAP_SIZE = 7;

	private Map assets;

	/**
	*  Defines all formal parameters.
	*
	*/

	private Map parameters;

	/**
	*  Is the component allowed to have a body (that is, wrap other elements?).
	*
	*/

	private boolean allowBody = true;

	/**
	*  Is the component allow to have informal parameter specified.
	*
	*/

	private boolean allowInformalParameters = true;

	public void addAsset(String name, AssetSpecification asset)
	{
		if (assets == null)
			assets = new HashMap(MAP_SIZE);

		assets.put(name, asset);
	}

	public void addComponent(String id, ContainedComponent component)
	{
		if (components == null)
			components = new HashMap(MAP_SIZE);

		components.put(id, component);
	}

	public void addParameter(String name, ParameterSpecification spec)
	{
		if (parameters == null)
			parameters = new HashMap(MAP_SIZE);

		parameters.put(name, spec);
	}

	/**
	*  Returns true if the component is allowed to wrap other elements (static HTML
	*  or other components).  The default is true.
	*
	*  @see #setAllowBody(boolean)
	*
	*/

	public boolean getAllowBody()
	{
		return allowBody;
	}

	/**
	*  Returns true if the component allows informal parameters (parameters
	*  not formally defined).  Informal parameters are generally used to create
	*  additional HTML attributes for an HTML tag renderred by the
	*  component.  This is often used to specify JavaScript event handlers or the class
	*  of the component (for Cascarding Style Sheets).
	*
	* <p>The default value is true.
	*
	*  @see #setAllowInformalParameters(boolean)
	*/

	public boolean getAllowInformalParameters()
	{
		return allowInformalParameters;
	}

	/**
	*  Returns the {@link AssetSpecification} with the given name, or null
	*  if no such specification exists.
	*
	*  @see #addAsset(String,AssetSpecification)
	*/

	public AssetSpecification getAsset(String name)
	{
		if (assets == null)
			return null;

		return (AssetSpecification)assets.get(name);
	}

	/**
	*  Returns an unmodifiable <code>Collection</code>
    *  of the String names of all assets.
	*
	*/

	public Collection getAssetNames()
	{
    	if (assets == null)
        		return Collections.EMPTY_SET;
            
        return Collections.unmodifiableCollection(assets.keySet());
	}

	/**
	*  Returns the specification of a contained component with the given id, or
	*  null if no such contained component exists.
	*
	*  @see #addComponent(String, ContainedComponent)
	*
	*/

	public ContainedComponent getComponent(String id)
	{
		if (components == null)
			return null;

		return (ContainedComponent)components.get(id);
	}

	public String getComponentClassName()
	{
		return componentClassName;
	}

	/**
	*  Returns an umodifiable <code>Collection</code>
    *  of the String names of the {@link ContainedComponent}s
	*  for this component.
	*
	*  @see #addComponent(String, ContainedComponent)
	*
	*/

	public Collection getComponentIds()
	{
		if (components == null)
			return Collections.EMPTY_SET;
        
        return Collections.unmodifiableCollection(components.keySet());    
	}

	/**
	*  Returns the specification of a parameter with the given name, or
	*  null if no such parameter exists.
	*
	*  @see #addParameter(String, ParameterSpecification)
	*
	*/

	public ParameterSpecification getParameter(String name)
	{
		if (parameters == null)
			return null;

		return (ParameterSpecification)parameters.get(name);
	}

	/**
	*  Returns an umodifiable <code>Collection</code>
    *  of String names of all parameters.
	*
	*  @see #addParameter(String, ParameterSpecification)
	*
	*/

	public Collection getParameterNames()
	{
		if (parameters == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(parameters.keySet());
	}

	/**
	*  Returns the String used to identify the resource parsed to form this
	*  <code>ComponentSpecification</code>.
	*
	*/

	public String getSpecificationResourcePath()
	{
		return specificationResourcePath;
	}

	public void setAllowBody(boolean value)
	{
		allowBody = value;
	}

	public void setAllowInformalParameters(boolean value)
	{
		allowInformalParameters = value;
	}

	public void setComponentClassName(String value)
	{
		componentClassName = value;
	}

	public void setSpecificationResourcePath(String value)
	{
		specificationResourcePath = value;
	}     

	public String toString()
	{
		StringBuffer buffer;
		buffer = new StringBuffer(super.toString());

		buffer.append('[');

		if (componentClassName != null)
			buffer.append(componentClassName);

		buffer.append(']');

		return buffer.toString();
	}
}

