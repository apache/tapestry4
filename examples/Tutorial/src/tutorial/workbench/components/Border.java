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

package tutorial.workbench.components;

import com.primix.tapestry.*;
import com.primix.tapestry.event.*;
import com.primix.tapestry.util.StringSplitter;
import java.util.ResourceBundle;
import tutorial.workbench.*;

/**
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 */

public class Border extends BaseComponent implements PageRenderListener
{
	/**
	 *  Page name updated in loop.
	 *
	 */

	private String pageName;
	private String activePageName;
	private boolean currentPageIsActivePage;

	private ResourceBundle stringsBundle;

	/**
	 * Array of page names, read from the Strings file; this is the same
	 * regardless of localization, so it is static (shared by all).
	 * 
	 **/

	private static String[] tabOrder;

	public void finishLoad()
	{
		super.finishLoad();

		stringsBundle =
			ResourceBundle.getBundle(
				"tutorial.workbench.components.BorderStrings",
				getPage().getLocale());

		if (tabOrder == null)
		{
			String tabOrderValue = stringsBundle.getString("tabOrder");

			StringSplitter splitter = new StringSplitter(' ');

			tabOrder = splitter.splitToArray(tabOrderValue);
		}

		page.addPageRenderListener(this);
	}

	public void pageBeginRender(PageEvent event)
	{
		Visit visit = (Visit) page.getEngine().getVisit(event.getRequestCycle());

		activePageName = visit.getActiveTabName();
	}

	public void pageEndRender(PageEvent event)
	{
		pageName = null;
		activePageName = null;
	}

	/**
	 *  Returns the logical names of the pages accessible via the
	 *  navigation bar, in appopriate order.
	 *
	 */

	public String[] getPageTabNames()
	{
		return tabOrder;
	}

	public void setPageName(String value)
	{
		pageName = value;

		currentPageIsActivePage = pageName.equals(activePageName);
	}

	public String getPageName()
	{
		return pageName;
	}

	public String getPageTitle()
	{
		// Need to check for synchronization issues, but I think
		// ResourceBundle is safe.
		
		return stringsBundle.getString(pageName);
	}

	public IAsset getLeftTabAsset()
	{
		String name = currentPageIsActivePage ? "activeLeft" : "inactiveLeft";

		return getAsset(name);
	}

	public IAsset getMidTabAsset()
	{
		String name = currentPageIsActivePage ? "activeMid" : "inactiveMid";

		return getAsset(name);
	}

	public IAsset getRightTabAsset()
	{
		String name = currentPageIsActivePage ? "activeRight" : "inactiveRight";

		return getAsset(name);
	}

	public void selectPage(String[] context, IRequestCycle cycle)
	{
		String newPageName = context[0];

		Visit visit = (Visit) page.getEngine().getVisit(cycle);

		visit.setActiveTabName(newPageName);

		cycle.setPage(newPageName);
	}

	/**
	 *  Does nothing; the form is about updating the visit.requestDebug
	 *  property and nothing more.
	 *
	 */

	public void formListener(IRequestCycle cycle)
	{
	}

}