//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.contrib.table.components;

import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * A low level Table component that renders the pages in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * <p>
 * The component generates a list of pages in the Table centered around the 
 * current one and allows you to navigate to other pages.
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TablePages.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TablePages extends AbstractTableViewComponent
{
    // Bindings    
    public abstract int getPagesDisplayed();

    // Transient
    private int m_nDisplayPage;

    /**
     * Returns the displayPage.
     * @return int
     */
    public int getDisplayPage()
    {
        return m_nDisplayPage;
    }

    /**
     * Sets the displayPage.
     * @param displayPage The displayPage to set
     */
    public void setDisplayPage(int displayPage)
    {
        m_nDisplayPage = displayPage;
    }

    public int getCurrentPage()
    {
        return getTableModelSource().getTableModel().getPagingState().getCurrentPage() + 1;
    }

    public int getPageCount()
    {
        return getTableModelSource().getTableModel().getPageCount();
    }

    public boolean getCondBack()
    {
        return getCurrentPage() > 1;
    }

    public boolean getCondFwd()
    {
        return getCurrentPage() < getPageCount();
    }

    public boolean getCondCurrent()
    {
        return getDisplayPage() == getCurrentPage();
    }

    public int getStartPage()
    {
        int nCurrent = getCurrentPage();
        int nPagesDisplayed = getPagesDisplayed();

        int nRightMargin = nPagesDisplayed / 2;
        int nStop = nCurrent + nRightMargin;
        int nLastPage = getPageCount();

        int nLeftAddon = 0;
        if (nStop > nLastPage)
            nLeftAddon = nStop - nLastPage;

        int nLeftMargin = (nPagesDisplayed - 1) / 2 + nLeftAddon;
        int nStart = nCurrent - nLeftMargin;
        int nFirstPage = 1;
        if (nStart < nFirstPage)
            nStart = nFirstPage;
        return nStart;
    }

    public int getStopPage()
    {
        int nCurrent = getCurrentPage();
        int nPagesDisplayed = getPagesDisplayed();

        int nLeftMargin = (nPagesDisplayed - 1) / 2;
        int nStart = nCurrent - nLeftMargin;
        int nFirstPage = 1;

        int nRightAddon = 0;
        if (nStart < nFirstPage)
            nRightAddon = nFirstPage - nStart;

        int nRightMargin = nPagesDisplayed / 2 + nRightAddon;
        int nStop = nCurrent + nRightMargin;
        int nLastPage = getPageCount();
        if (nStop > nLastPage)
            nStop = nLastPage;
        return nStop;
    }

    public Integer[] getPageList()
    {
        int nStart = getStartPage();
        int nStop = getStopPage();

        Integer[] arrPages = new Integer[nStop - nStart + 1];
        for (int i = nStart; i <= nStop; i++)
            arrPages[i - nStart] = new Integer(i);

        return arrPages;
    }

    public Object[] getFirstPageContext()
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(1)};
    }

    public Object[] getLastPageContext()
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getPageCount())};
    }

    public Object[] getBackPageContext()
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getCurrentPage() - 1)};
    }

    public Object[] getFwdPageContext()
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getCurrentPage() + 1)};
    }

    public Object[] getDisplayPageContext()
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(m_nDisplayPage)};
    }

    public void changePage(IRequestCycle objCycle)
    {
        Object[] arrParameters = objCycle.getServiceParameters();
        if (arrParameters.length != 2
            && !(arrParameters[0] instanceof ComponentAddress)
            && !(arrParameters[1] instanceof Integer))
        {
            // error
            return;
        }

        ComponentAddress objAddress = (ComponentAddress) arrParameters[0];
        ITableModelSource objSource = (ITableModelSource) objAddress.findComponent(objCycle);
        setCurrentPage(objSource, ((Integer) arrParameters[1]).intValue());

        // ensure that the change is saved
        objSource.fireObservedStateChange();
    }

    public void setCurrentPage(ITableModelSource objSource, int nPage)
    {
        objSource.getTableModel().getPagingState().setCurrentPage(nPage - 1);
    }

}
