/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
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

package net.sf.tapestry.inspector;

import com.primix.tapestry.event.*;
import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;

import net.sf.tapestry.*;
import net.sf.tapestry.event.*;
import net.sf.tapestry.spec.*;

import java.util.*;

/**
 *  Component of the {@link Inspector} page used to display
 *  the specification, parameters and bindings and assets of the inspected component.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class ShowSpecification
	extends BaseComponent
	implements PageRenderListener
{
	private IComponent inspectedComponent;
	private ComponentSpecification inspectedSpecification;
	private String parameterName;
	private String assetName;
	private List sortedComponents;
	private IComponent component;
	private List assetNames;
	private List formalParameterNames;
	private List informalParameterNames;
	private List sortedPropertyNames;
	private String propertyName;
	private List beanNames;
	private String beanName;
	private BeanSpecification beanSpecification;

	private static class ComponentComparitor implements Comparator
	{
		public int compare(Object left, Object right)
		{
			IComponent leftComponent;
			String leftId;
			IComponent rightComponent;
			String rightId;

			if (left == right)
				return 0;

			leftComponent = (IComponent) left;
			rightComponent = (IComponent) right;

			leftId = leftComponent.getId();
			rightId = rightComponent.getId();

			return leftId.compareTo(rightId);
		}
	}

	/**
	 *  Registers this component as a {@link PageRenderListener}.
	 *
	 * @since 1.0.5
	 */

	protected void finishLoad()
	{
		page.addPageRenderListener(this);
	}

	/**
	 *  Clears all cached information about the component and such after
	 *  each render (including the rewind phase render used to process
	 *  the tab view).
	 *
	 *  @since 1.0.5
	 *
	 */

	public void pageEndRender(PageEvent event)
	{
		inspectedComponent = null;
		inspectedSpecification = null;
		parameterName = null;
		assetName = null;
		sortedComponents = null;
		component = null;
		assetNames = null;
		formalParameterNames = null;
		informalParameterNames = null;
		sortedPropertyNames = null;
		propertyName = null;
		beanNames = null;
		beanName = null;
		beanSpecification = null;
	}

	/**
	 *  Gets the inspected component and specification from the {@link Inspector} page.
	 *
	 *  @since 1.0.5
	 */

	public void pageBeginRender(PageEvent event)
	{
		Inspector inspector;

		inspector = (Inspector) page;

		inspectedComponent = inspector.getInspectedComponent();
		inspectedSpecification = inspectedComponent.getSpecification();
	}

	public IComponent getInspectedComponent()
	{
		return inspectedComponent;
	}

	public ComponentSpecification getInspectedSpecification()
	{
		return inspectedSpecification;
	}

	/**
	 *  Returns a sorted list of formal parameter names.
	 *
	 */

	public List getFormalParameterNames()
	{
		if (formalParameterNames != null)
			return formalParameterNames;

		Collection names = inspectedSpecification.getParameterNames();
		if (names != null && names.size() > 0)
		{
			formalParameterNames = new ArrayList(names);
			Collections.sort(formalParameterNames);
		}

		return formalParameterNames;
	}

	/**
	 *  Returns a sorted list of informal parameter names.  This is
	 *  the list of all bindings, with the list of parameter names removed,
	 *  sorted.
	 *
	 */

	public List getInformalParameterNames()
	{
		if (informalParameterNames != null)
			return informalParameterNames;

		Collection names = inspectedComponent.getBindingNames();
		if (names != null && names.size() > 0)
		{
			informalParameterNames = new ArrayList(names);

			// Remove the names of any formal parameters.  This leaves
			// just the names of informal parameters (informal parameters
			// are any parameters/bindings that don't match a formal parameter
			// name).

			names = inspectedSpecification.getParameterNames();
			if (names != null)
				informalParameterNames.removeAll(names);

			Collections.sort(informalParameterNames);
		}

		return informalParameterNames;
	}

	public String getParameterName()
	{
		return parameterName;
	}

	public void setParameterName(String value)
	{
		parameterName = value;
	}

	/**
	 *  Returns the {@link ParameterSpecification} corresponding to
	 *  the value of the parameterName property.
	 *
	 */

	public ParameterSpecification getParameterSpecification()
	{
		return inspectedSpecification.getParameter(parameterName);
	}

	/**
	 *  Returns the {@link IBinding} corresponding to the value of
	 *  the parameterName property.
	 *
	 */

	public IBinding getBinding()
	{
		return inspectedComponent.getBinding(parameterName);
	}

	public void setAssetName(String value)
	{
		assetName = value;
	}

	public String getAssetName()
	{
		return assetName;
	}

	/**
	 *  Returns the {@link IAsset} corresponding to the value
	 *  of the assetName property.
	 *
	 */

	public IAsset getAsset()
	{
		return (IAsset) inspectedComponent.getAssets().get(assetName);
	}

	/**
	 *  Returns a sorted list of asset names, or null if the
	 *  component contains no assets.
	 *
	 */

	public List getAssetNames()
	{
		if (assetNames != null)
			return assetNames;

		Map assets = inspectedComponent.getAssets();

		if (assets != null && assets.size() > 0)
		{
			assetNames = new ArrayList(assets.keySet());
			Collections.sort(assetNames);
		}

		return assetNames;
	}

	public List getSortedComponents()
	{
		if (sortedComponents != null)
			return sortedComponents;

		Inspector inspector = (Inspector) page;
		IComponent inspectedComponent = inspector.getInspectedComponent();

		// Get a Map of the components and simply return null if there
		// are none.

		Map components = inspectedComponent.getComponents();

		if (components != null && components.size() > 0)
		{
			sortedComponents = new ArrayList(components.values());

			Collections.sort(sortedComponents, new ComponentComparitor());
		}

		return sortedComponents;
	}

	public void setComponent(IComponent value)
	{
		component = value;
	}

	public IComponent getComponent()
	{
		return component;
	}

	/**
	 *  Returns the type of the component, as specified in the container's
	 *  specification (i.e., the component alias if known).
	 *
	 */

	public String getComponentType()
	{
		String id;
		ComponentSpecification containerSpecification;
		IComponent container;
		ContainedComponent contained;

		container = component.getContainer();

		containerSpecification = container.getSpecification();

		id = component.getId();
		contained = containerSpecification.getComponent(id);

		return contained.getType();
	}

	/**
	 *  Returns a list of the properties for the component
	 *  (from its specification), or null if the component
	 *  has no properties.
	 *
	 */

	public List getSortedPropertyNames()
	{
		if (sortedPropertyNames != null)
			return sortedPropertyNames;

		Collection names = inspectedSpecification.getPropertyNames();
		if (names != null && names.size() > 0)
		{
			sortedPropertyNames = new ArrayList(names);
			Collections.sort(sortedPropertyNames);
		}

		return sortedPropertyNames;
	}

	public void setPropertyName(String value)
	{
		propertyName = value;
	}

	public String getPropertyName()
	{
		return propertyName;
	}

	public String getPropertyValue()
	{
		return inspectedSpecification.getProperty(propertyName);
	}

	public List getBeanNames()
	{
		if (beanNames != null)
			return beanNames;

		Collection names = inspectedSpecification.getBeanNames();

		if (names != null && names.size() > 0)
		{
			beanNames = new ArrayList(names);
			Collections.sort(beanNames);
		}

		return beanNames;
	}

	public void setBeanName(String value)
	{
		beanName = value;
		beanSpecification = inspectedSpecification.getBeanSpecification(beanName);
	}

	public String getBeanName()
	{
		return beanName;
	}

	public BeanSpecification getBeanSpecification()
	{
		return beanSpecification;
	}
}