// Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.workbench.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.util.StringSplitter;
import org.apache.tapestry.workbench.Visit;

/**
 * Common navigational border for the Workbench tutorial.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.7
 */

public abstract class Border extends BaseComponent implements PageRenderListener
{

    /**
     * Array of page names, read from the Strings file; this is the same regardless of localization,
     * so it is static (shared by all).
     */

    private static String[] _tabOrder;

    public void pageBeginRender(PageEvent event)
    {
        Visit visit = (Visit) getPage().getEngine().getVisit(event.getRequestCycle());

        setActivePageName(visit.getActiveTabName());

        if (_tabOrder == null)
        {
            String tabOrderValue = getMessages().getMessage("tabOrder");

            StringSplitter splitter = new StringSplitter(' ');

            _tabOrder = splitter.splitToArray(tabOrderValue);
        }
    }

    /**
     * Returns the logical names of the pages accessible via the navigation bar, in appopriate
     * order.
     */

    public String[] getPageTabNames()
    {
        return _tabOrder;
    }

    public abstract void setPageName(String value);

    public abstract String getPageName();

    public abstract void setActivePageName(String activePageName);

    public abstract String getActivePageName();

    public boolean isActivePage()
    {
        return getPageName().equals(getActivePageName());
    }

    public String getPageTitle()
    {
        // Need to check for synchronization issues, but I think
        // ResourceBundle is safe.

        return getMessages().getMessage(getPageName());
    }

    public IAsset getLeftTabAsset()
    {
        String name = isActivePage() ? "activeLeft" : "inactiveLeft";

        return getAsset(name);
    }

    public IAsset getMidTabAsset()
    {
        String name = isActivePage() ? "activeMid" : "inactiveMid";

        return getAsset(name);
    }

    public IAsset getRightTabAsset()
    {
        String name = isActivePage() ? "activeRight" : "inactiveRight";

        return getAsset(name);
    }

    public void selectPage(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        String newPageName = (String) parameters[0];

        Visit visit = (Visit) getPage().getEngine().getVisit(cycle);

        visit.setActiveTabName(newPageName);

        cycle.activate(newPageName);
    }
}