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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */


package com.primix.tapestry.inspector;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;


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
	private IPropertySelectionRenderer renderer;
	
	/**
	 *  When the form is submitted,
	 *  the inspectedPageName of the {@link Inspector} page will be updated,
	 *  but we need to reset the inspectedIdPath as well.
	 *
	 */
	
	public void formSubmit(IRequestCycle cycle)
	{
		Inspector inspector = (Inspector)getPage();
		
		inspector.selectComponent(null);
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
		
		sortedPageNames = new ArrayList(page.getEngine().getSpecification().getPageNames());
		
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
		List list = null;
		IComponent component;
		Inspector inspector;
		IComponent container;
		
		inspector = (Inspector)page;
		
		component = inspector.getInspectedComponent();
		
		while (true)
		{
			container = component.getContainer();
			if (container == null)
				break;
			
			if (list == null)
				list = new ArrayList();
			
			list.add(component);
			
			component = container;
			
		}
		
		if (list == null)
			return null;
		
		// Reverse the list, such that the inspected component is last, and the
		// top-most container is first.
		
		Collections.reverse(list);
		
		return list;
	}
	
}
