/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2002 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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

package net.sf.tapestry.spec;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import net.sf.tapestry.*;
import net.sf.tapestry.Tapestry;
import net.sf.tapestry.util.*;
import net.sf.tapestry.util.BasePropertyHolder;

/**
 *  A specification for a component, as read from an XML specification file.
 *
 *  <p>A specification consists of
 *  <ul>
 *  <li>An implementing class
 *  <li>An optional template
 *  <li>An optional description
 * <li>A set of contained components
 * <li>Bindings for the properties of each contained component
 * <li>A set of named assets
 *  <li>Definitions for helper beans
 *  <li>Any reserved names (used for HTML attributes)
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
 *
 **/

public class ComponentSpecification extends BasePropertyHolder
{
	private String componentClassName;
	protected String specificationResourcePath;

	/** @since 1.0.9 **/
	private String description;

	/**
	 *  Keyed on component id, value is {@link ContainedComponent}.
	 *
	 **/

	protected Map components;

	private static final int MAP_SIZE = 7;

	/**
	 *  Keyed on asset name, value is {@link AssetSpecification}.
	 *
	 */

	protected Map assets;

	/**
	 *  Defines all formal parameters.  Keyed on parameter name, value is
	 * {@link ParameterSpecification}.
	 *
	 **/

	protected Map parameters;

	/**
	 *  Defines all helper beans.  Keyed on name, value is {@link BeanSpecification}.
	 *
	 *  @since 1.0.4
	 **/

	protected Map beans;

	/**
	 *  The names of all reserved informal parameter names (as lower-case).  This
	 *  allows the page loader to filter out any informal parameters during page load,
	 *  rather than during render.
	 *
	 *   @since 1.0.5
	 *
	 **/

	protected Set reservedParameterNames;

	/**
	 *  Is the component allowed to have a body (that is, wrap other elements?).
	 *
	 **/

	private boolean allowBody = true;

	/**
	 *  Is the component allow to have informal parameter specified.
	 *
	 */

	private boolean allowInformalParameters = true;

	/**
	 * @throws IllegalArgumentException if the name already exists.
	 *
	 **/

	public void addAsset(String name, AssetSpecification asset)
	{
		if (assets == null)
			assets = new HashMap(MAP_SIZE);
		else if (assets.containsKey(name))
			throw new IllegalArgumentException(
				Tapestry.getString("ComponentSpecification.duplicate-asset", this, name));

		assets.put(name, asset);
	}

	/**
	 *  @throws IllegalArgumentException if the id is already defined.
	 *
	 **/

	public void addComponent(String id, ContainedComponent component)
	{
		if (components == null)
			components = new HashMap(MAP_SIZE);
		else if (components.containsKey(id))
			throw new IllegalArgumentException(
				Tapestry.getString("ComponentSpecification.duplicate-component", this, id));

		components.put(id, component);
	}

	/**
	 *  Adds the parameter.   The name is added as a reserved name.
	 *
	 *  @throws IllegalArgumentException if the name already exists.
	 **/

	public void addParameter(String name, ParameterSpecification spec)
	{
		if (parameters == null)
			parameters = new HashMap(MAP_SIZE);
		else if (parameters.containsKey(name))
			throw new IllegalArgumentException(
				Tapestry.getString("ComponentSpecification.duplicate-parameter", this, name));

		parameters.put(name, spec);

		addReservedParameterName(name);
	}

	/**
	 *  Returns true if the component is allowed to wrap other elements (static HTML
	 *  or other components).  The default is true.
	 *
	 *  @see #setAllowBody(boolean)
	 *
	 **/

	public boolean getAllowBody()
	{
		return allowBody;
	}

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

	public boolean getAllowInformalParameters()
	{
		return allowInformalParameters;
	}

	/**
	 *  Returns the {@link AssetSpecification} with the given name, or null
	 *  if no such specification exists.
	 *
	 *  @see #addAsset(String,AssetSpecification)
	 **/

	public AssetSpecification getAsset(String name)
	{
		if (assets == null)
			return null;

		return (AssetSpecification) assets.get(name);
	}

	/**
	 *  Returns an unmodifiable <code>Collection</code>
	 *  of the String names of all assets.
	 *
	 **/

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
	 **/

	public ContainedComponent getComponent(String id)
	{
		if (components == null)
			return null;

		return (ContainedComponent) components.get(id);
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
	 **/

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
	 **/

	public ParameterSpecification getParameter(String name)
	{
		if (parameters == null)
			return null;

		return (ParameterSpecification) parameters.get(name);
	}

	/**
	 *  Returns an umodifiable <code>Collection</code>
	 *  of String names of all parameters.
	 *
	 *  @see #addParameter(String, ParameterSpecification)
	 *
	 **/

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
	 **/

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

	/**
	 *  @since 1.0.4
	 *
	 *  @throws IllegalArgumentException if the bean already has a specification.
	 **/

	public void addBeanSpecification(String name, BeanSpecification specification)
	{
		if (beans == null)
			beans = new HashMap(MAP_SIZE);
		else if (beans.containsKey(name))
			throw new IllegalArgumentException(
				Tapestry.getString("ComponentSpecification.duplicate-bean", this, name));

		beans.put(name, specification);
	}

	/**
	 * Returns the {@link BeanSpecification} for the given name, or null
	 * if not such specification exists.
	 *
	 * @since 1.0.4
	 *
	 **/

	public BeanSpecification getBeanSpecification(String name)
	{
		if (beans == null)
			return null;

		return (BeanSpecification) beans.get(name);
	}

	/**
	 *  Returns an unmodifiable collection of the names of all beans.
	 *
	 **/

	public Collection getBeanNames()
	{
		if (beans == null)
			return Collections.EMPTY_LIST;

		return Collections.unmodifiableCollection(beans.keySet());
	}

	/**
	 *  Adds the value as a reserved name.  Reserved names are not allowed
	 *  as the names of informal parameters.  Since the comparison is
	 *  caseless, the value is converted to lowercase before being
	 *  stored.
	 *
	 *  @since 1.0.5
	 *
	 **/

	public void addReservedParameterName(String value)
	{
		if (reservedParameterNames == null)
			reservedParameterNames = new HashSet();

		reservedParameterNames.add(value.toLowerCase());
	}

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

	public boolean isReservedParameterName(String value)
	{
		if (reservedParameterNames == null)
			return false;

		return reservedParameterNames.contains(value.toLowerCase());
	}

	public String toString()
	{
		StringBuffer buffer;
		buffer = new StringBuffer(super.toString());

		buffer.append('[');

		if (specificationResourcePath != null)
			buffer.append(specificationResourcePath);
		else if (componentClassName != null)
			buffer.append(componentClassName);

		buffer.append(']');

		return buffer.toString();
	}

	/**
	 *  Returns the documentation for this component.
	 * 
	 *  @since 1.0.9
	 **/

	public String getDescription()
	{
		return description;
	}

	/**
	 *  Sets the documentation for this component.
	 * 
	 *  @since 1.0.9
	 **/

	public void setDescription(String description)
	{
		this.description = description;
	}

}