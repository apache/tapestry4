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

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;

import net.sf.tapestry.*;
import net.sf.tapestry.form.*;
import net.sf.tapestry.spec.*;
import net.sf.tapestry.spec.ApplicationSpecification;

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
		Inspector inspector = (Inspector) getPage();

		inspector.selectComponent(null);
	}

	/**
	 *  Returns an {IPropertySelectionModel} used to select the name of the page
	 *  to inspect.  The page names are sorted.
	 *
	 */

	public IPropertySelectionModel getPageModel()
	{
		ApplicationSpecification spec = page.getEngine().getSpecification();
		
		Collection sortedPageNames =
			spec.getPageNames();
			
		String[] pageNames =
			(String[])sortedPageNames.toArray(new String[sortedPageNames.size()]);

		// This should be cached for later, but there's the possibility that
		// the application specification may be dynamically modified in a running
		// application.

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

		inspector = (Inspector) page;

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