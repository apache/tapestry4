package com.primix.tapestry.inspector;

import com.primix.foundation.*;
import com.primix.tapestry.*;
import com.primix.tapestry.components.*;

/* Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  The Tapestry Inspector page.
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class Inspector extends BasePage
{
	private View view = View.SPECIFICATION;
	private String inspectedPageName;
	private String inspectedIdPath;

	public void detach()
	{
		super.detach();
		
		view = View.SPECIFICATION;
		inspectedPageName = null;
		inspectedIdPath = null;
	}

	public View getView()
	{
		return view;
	}
	
	public void setView(View value)
	{
		view = value;
		
		fireObservedChange("view", value);
	}
	
	public String getInspectedPageName()
	{
		return inspectedPageName;
	}
	
	public void setInspectedPageName(String value)
	{
		inspectedPageName = value;
		
		fireObservedChange("inspectedPageName", value);
	}
	
	public String getInspectedIdPath()
	{
		return inspectedIdPath;
	}

	public void setInspectedIdPath(String value)
	{
		inspectedIdPath = value;
		
		fireObservedChange("inspectedIdPath", value);
	}
	
	public boolean isViewSpecification()
	{
		return view == View.SPECIFICATION;
	}
	
	public boolean isViewTemplate()
	{
		return view == View.TEMPLATE;
	}
	
	public boolean isViewProperties()
	{
		return view == View.PROPERTIES;
	}
	
	public boolean isViewEngine()
	{
		return view == View.ENGINE;
	}
	
	public boolean isViewLogging()
	{
		return view == View.LOGGING;
	}
	
	/**
	 *  Method invoked by the {@link ShowInspector} component, 
	 *  to begin inspecting a page.
	 *
	 */
	 
	public void inspect(String pageName, IRequestCycle cycle)
	{
		setInspectedPageName(pageName);
		setInspectedIdPath(null);
		
		cycle.setPage(this);
	}
	
	/**
	 *  Listener for the component selection, which allows a particular component.  
	 *  
	 *  <p>The context is a single string,
	 *  the id path of the component to be selected (or null to inspect
	 *  the page itself).  This invokes
	 *  {@link #setInspectedIdPath(String)}.
	 *
	 */
	 
	public IDirectListener getSelectComponentListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IDirect direct, String[] context, IRequestCycle cycle)
			{
				String newIdPath;
				
				// The up button may generate a null context.
				
				if (context == null)
					newIdPath = null;
				else
					newIdPath = context[0];
						
				setInspectedIdPath(newIdPath);
			}
		};
	}
	
	/**
	 *  Returns the {@link IPage} currently inspected by the Inspector, as determined
	 *  from the inspectedPageName property.
	 *
	 */
	 
	public IPage getInspectedPage()
	{
        return getRequestCycle().getPage(inspectedPageName);
	}
	
	/**
	 *  Returns the {@link IComponent} current inspected; this is determined
	 *  from the inspectedPageName and inspectedIdPath properties.
	 *
	 */
	 
	public IComponent getInspectedComponent()
	{
		return getInspectedPage().getNestedComponent(inspectedIdPath);	
	}

    public String getInspectorTitle()
    {
        return "Tapestry Inspector: " +
                engine.getSpecification().getName();
    }
}