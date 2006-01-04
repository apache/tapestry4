// Copyright 2004, 2005 The Apache Software Foundation
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
import org.apache.tapestry.TapestryUtils;
import org.apache.tapestry.annotations.Asset;
import org.apache.tapestry.annotations.InjectAsset;
import org.apache.tapestry.annotations.InjectState;
import org.apache.tapestry.annotations.Message;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.workbench.Visit;

/**
 * Common navigational border for the Workbench tutorial.
 * 
 * @author Howard Lewis Ship
 * @since 1.0.7
 */

public abstract class Border extends BaseComponent implements PageBeginRenderListener
{

    /**
     * Array of page names, read from the Strings file; this is the same regardless of localization,
     * so it is static (shared by all).
     */

    private static String[] _tabOrder;

    @InjectState("visit")
    public abstract Visit getVisit();

    @Message
    public abstract String getTabOrder();

    public void pageBeginRender(PageEvent event)
    {
        Visit visit = getVisit();

        setActivePageName(visit.getActiveTabName());

        if (_tabOrder == null)
            _tabOrder = TapestryUtils.split(getTabOrder(), ' ');
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

    // Arbitrary mix of @Asset and @InjectAsset just to
    // test the annotations.

    @Asset("images/tab-active-left.gif")
    public abstract IAsset getActiveLeft();

    @InjectAsset("inactiveLeft")
    public abstract IAsset getInactiveLeft();

    @Asset("images/tab-active-mid.gif")
    public abstract IAsset getActiveMid();

    @InjectAsset("inactiveMid")
    public abstract IAsset getInactiveMid();

    @Asset("images/tab-active-right.gif")
    public abstract IAsset getActiveRight();

    @InjectAsset("inactiveRight")
    public abstract IAsset getInactiveRight();

    public IAsset getLeftTabAsset()
    {
        return isActivePage() ? getActiveLeft() : getInactiveLeft();
    }

    public IAsset getMidTabAsset()
    {
        return isActivePage() ? getActiveMid() : getInactiveMid();
    }

    public IAsset getRightTabAsset()
    {
        return isActivePage() ? getActiveRight() : getInactiveRight();
    }

    public void selectPage(IRequestCycle cycle, String newPageName)
    {
        Visit visit = getVisit();

        visit.setActiveTabName(newPageName);

        cycle.activate(newPageName);
    }
}