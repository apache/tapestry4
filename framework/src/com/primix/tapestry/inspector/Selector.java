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
 *  Component of the {@link Inspector} page used to select the page and "crumb trail"
 *  of the inspected component.
 *
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */
 
public class Selector extends BaseComponent
{
	public Selector(IPage page, IComponent container, String id,
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	/**
	 *  Gets the listener for the form.  When the form is submitted,
	 *  the inspectedPageName of the {@link Inspector} page will be updated,
	 *  but we need to reset the inspectedIdPath as well.
	 *
	 */
	 
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				Inspector inspector;
				
				inspector = (Inspector)getPage();
				
				inspector.setInspectedIdPath(null);
			}
		};
	}
	
	/**
	 *  Returns an {IPropertySelectionModel} used to select the name of the page
	 *  to inspect.  The page names are sorted.
	 *
	 */
	 
	public IPropertySelectionModel getPageModel()
	{
		List sortedPageNames;
		String[] pageNames;
		
		sortedPageNames = new ArrayList(page.getApplication().getSpecification().getPageNames());
		
		Collections.sort(sortedPageNames);
		
		pageNames = new String[sortedPageNames.size()];
		pageNames = (String[])sortedPageNames.toArray(pageNames);
		
		// It would be nice to cache this between request cycles ... but this same
		// component may be used for a different application in a subsequent cycle,
		// in which case the page names wouldn't match.  
		
		return new StringPropertySelectionModel(pageNames);
	}
	
	/**
	 *  The crumb trail is all the components from the inspected component up to
	 *  (but not including) the page.
	 *
	 */
	 
	public List getCrumbTrail()
	{
		List list;
		IPage inspectedPage;
		IComponent component;
		Inspector inspector;
		String inspectedPageName;
		String inspectedIdPath;
		IComponent container;
		
		inspector = (Inspector)page;
		
		inspectedIdPath = inspector.getInspectedIdPath();
		
		if (inspectedIdPath == null)
			return null;
		
		inspectedPageName = inspector.getInspectedPageName();
		inspectedPage = inspector.getRequestCycle().getPage(inspectedPageName);

		component = inspectedPage.getNestedComponent(inspectedIdPath);
		
		list = new ArrayList();

		container = component.getContainer();
				
		do
		{
			list.add(component);
			
			component = container;
			
			container = component.getContainer();
		}
		while (container != null);
		
		// Reverse the list, such that the inspected component is last, and the
		// top-most container is first.
		
		Collections.reverse(list);
		
		return list;
	}

	public boolean getEnableUp()
	{
		Inspector inspector;
		
		inspector = (Inspector)page;
		
		return inspector.getInspectedIdPath() != null;
	}
	
}
