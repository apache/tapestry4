//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package tutorial.workbench.components;

import tutorial.workbench.Visit;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IPageLoader;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.PageLoaderException;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;
import net.sf.tapestry.spec.ComponentSpecification;
import net.sf.tapestry.util.StringSplitter;

/**
 *  Common navigational border for the Workbench tutorial.
 * 
 *  @author Howard Lewis Ship
 *  @version $Id$
 *  @since 1.0.7
 *
 **/

public class Border extends BaseComponent implements PageRenderListener
{
    /**
     *  Page name updated in loop.
     *
     **/

    private String pageName;
    private String activePageName;
    private boolean currentPageIsActivePage;

    /**
     * Array of page names, read from the Strings file; this is the same
     * regardless of localization, so it is static (shared by all).
     * 
     **/

    private static String[] tabOrder;

    public void finishLoad()
    {
        page.addPageRenderListener(this);
    }

    public void pageBeginRender(PageEvent event)
    {
        Visit visit = (Visit) page.getEngine().getVisit(event.getRequestCycle());

        activePageName = visit.getActiveTabName();

        if (tabOrder == null)
        {
            String tabOrderValue = getString("tabOrder");

            StringSplitter splitter = new StringSplitter(' ');

            tabOrder = splitter.splitToArray(tabOrderValue);
        }
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
     **/

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

        return getString(pageName);
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

    public void selectPage(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        String newPageName = (String)parameters[0];

        Visit visit = (Visit) page.getEngine().getVisit(cycle);

        visit.setActiveTabName(newPageName);

        cycle.setPage(newPageName);
    }

    /**
     *  Does nothing; the form is about updating the visit.requestDebug
     *  property and nothing more.
     *
     **/

    public void formListener(IRequestCycle cycle)
    {
    }


}