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

import com.primix.tapestry.util.*;
import com.primix.tapestry.util.prop.*;
import com.primix.tapestry.*;

import net.sf.tapestry.*;
import net.sf.tapestry.components.*;
import net.sf.tapestry.html.*;
import net.sf.tapestry.spec.*;
import net.sf.tapestry.util.prop.*;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import java.util.*;

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

	/** 
	 *  A property path, relative to the inspected component, used
	 *  by the ShowProperties view.  This is set to null
	 *  whenever the inspected component is changed.
	 *
	 *  @since 1.0.6
	 *
	 */

	private String explorePath;

	/**
	 *  The currently explored object; the explorePath is applied
	 *  to the inspected component.
	 *
	 *  @since 1.0.6
	 *
	 */

	private Object exploredObject;

	private static final int MAP_SIZE = 7;

	private Map blocks;

	public void detach()
	{
		view = View.SPECIFICATION;
		inspectedPageName = null;
		inspectedIdPath = null;
		explorePath = null;
		exploredObject = null;

		super.detach();
	}

	public void finishLoad(
		IPageLoader loader,
		ComponentSpecification specification)
		throws PageLoaderException
	{
		super.finishLoad(loader, specification);

		blocks = new HashMap(MAP_SIZE);

		blocks.put(View.TEMPLATE, getComponent("templateBlock"));
		blocks.put(View.SPECIFICATION, getComponent("specificationBlock"));
		blocks.put(View.ENGINE, getComponent("engineBlock"));
		blocks.put(View.PROPERTIES, getComponent("propertiesBlock"));
		blocks.put(View.LOGGING, getComponent("loggingBlock"));
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

	/** 
	 *  Invoked to change the component being inspected within the current
	 *  page.  This should be used, not {@link #setInspectedIdPath(String)},
	 *  since this also sets the explore path to null.
	 *
	 *  @since 1.0.6
	 */

	public void selectComponent(String idPath)
	{
		setInspectedIdPath(idPath);
		setExplorePath(null);
	}

	/** @since 1.0.6 **/

	public String getExplorePath()
	{
		return explorePath;
	}

	public void setExplorePath(String value)
	{
		explorePath = value;
		exploredObject = null;

		fireObservedChange("explorePath", value);
	}

	/**
	 *  Method invoked by the {@link ShowInspector} component, 
	 *  to begin inspecting a page.
	 *
	 */

	public void inspect(String pageName, IRequestCycle cycle)
	{
		setInspectedPageName(pageName);
		selectComponent(null);

		cycle.setPage(this);
	}

	/**
	 *  Listener for the component selection, which allows a particular component.  
	 *  
	 *  <p>The context is a single string,
	 *  the id path of the component to be selected (or null to inspect
	 *  the page itself).  This invokes
	 *  {@link #selectComponent(String)}.
	 *
	 */

	public void selectComponent(String[] context, IRequestCycle cycle)
	{
		String newIdPath;

		// The up button may generate a null context.

		if (context == null)
			newIdPath = null;
		else
			newIdPath = context[0];

		selectComponent(newIdPath);
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
		return "Tapestry Inspector: " + engine.getSpecification().getName();
	}

	/**
	 *  Returns the object currently being explored.  This is
	 *  either the inspected component, or an object
	 *  retrieved from it via the explorePath.
	 *
	 *  @since 1.0.6
	 *
	 */

	public Object getExploredObject()
	{
		// The only problem is that sometimes the explorePath leads
		// to a null property.  Need to track this better (maybe using Void?).

		if (exploredObject == null)
		{
			IComponent inspectedComponent = getInspectedComponent();

			if (explorePath == null)
				exploredObject = inspectedComponent;
			else
			{
				PropertyHelper helper = PropertyHelper.forInstance(inspectedComponent);

				exploredObject = helper.getPath(inspectedComponent, explorePath);
			}
		}

		return exploredObject;
	}

	/**
	 *  Returns the {@link Block} for the currently selected view.
	 *
	 */

	public Block getBlockForView()
	{
		return (Block) blocks.get(view);
	}
}