/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
