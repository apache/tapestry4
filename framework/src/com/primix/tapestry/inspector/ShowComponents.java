package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.components.*;
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
 *  Component of the {@link Inspector} page used to display
 *  the ids and types of all embedded components.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class ShowComponents extends BaseComponent
implements ILifecycle
{
	private List sortedComponents;
	private IComponent component;

	public ShowComponents(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public void cleanupAfterRender(IRequestCycle cycle)
	{
		sortedComponents = null;
		component = null;
	}
	
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
				
			leftComponent = (IComponent)left;
			rightComponent = (IComponent)right;

			leftId = leftComponent.getId();
			rightId = rightComponent.getId();
			
			return leftId.compareTo(rightId);			
		}
	}

	public List getSortedComponents()
	{
		if (sortedComponents == null)
			buildSortedComponents();
		
		return sortedComponents;
	}
	
	private void buildSortedComponents()
	{
		Inspector inspector;
		Map components;
		IComponent inspectedComponent;
		
		inspector = (Inspector)page;
		
		inspectedComponent = inspector.getInspectedComponent();
		
		// Get a Map of the components and simply return null if there
		// are none.
		
		components = inspectedComponent.getComponents();
		if (components == null)
			return;
		
		if (components.size() == 0)
			return;
			
		// Create the sorted List from the values.
		
		sortedComponents = new ArrayList(components.values());
		
		Collections.sort(sortedComponents, new ComponentComparitor());	
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
	 *  Selects the component identified in the context <i>and<i> changes
	 *  the view to SPECIFICATION.
	 *
	 */
	 
	public IDirectListener getSelectListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				Inspector inspector;
				
				inspector = (Inspector)getPage();
				
				if (context == null)
					inspector.setInspectedIdPath(null);
				else
					inspector.setInspectedIdPath(context[0]);
					
				inspector.setView(View.SPECIFICATION);	
			}
		};
	}
}
