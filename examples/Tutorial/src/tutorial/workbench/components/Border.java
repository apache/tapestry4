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

	private static final String[] pageTabNames =
		{ "Home", "Localization", "Fields", "Palette" };

	public void finishLoad()
	{
		super.finishLoad();

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
		return pageTabNames;
	}

	public void setPageName(String value)
	{
		pageName = value;
	}

	public String getPageName()
	{
		return pageName;
	}

	public void selectPage(String[] context, IRequestCycle cycle)
	{
		String newPageName = context[0];

		Visit visit = (Visit) page.getEngine().getVisit(cycle);

		visit.setActiveTabName(newPageName);

		cycle.setPage(newPageName);
	}

	public IAsset getPageImage()
	{
		String suffix;

		if (pageName.equals(activePageName))
			suffix = "-active";
		else
			suffix = "-inactive";

		return getAsset(pageName + suffix);
	}

	public IAsset getPageFocusImage()
	{
		String suffix;

		if (pageName.equals(activePageName))
			suffix = "-focus";
		else
			suffix = "-inactive-focus";

		return getAsset(pageName + suffix);
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