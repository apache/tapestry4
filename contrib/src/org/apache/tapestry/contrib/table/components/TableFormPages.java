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
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;

/**
 * A low level Table component that renders the pages in the table.
 * 
 * This component is a variant of {@link org.apache.tapestry.contrib.table.components.TablePages}, 
 * but is designed for operation in a form. The necessary page data is stored 
 * in hidden fields, so that no StaleLink exceptions occur during a rewind. 
 * The links also submit the form, which ensures that the data in the other 
 * form fields is preserved even when the page chages.
 *  
 * The component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * <p>
 * The component generates a list of pages in the Table centered around the 
 * current one and allows you to navigate to other pages.
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableFormPages.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableFormPages extends TablePages 
    implements PageDetachListener, PageRenderListener
{
    private int m_nCurrentPage;
    private int m_nPageCount;
    private int m_nStartPage;
    private int m_nStopPage;    

    public TableFormPages()
    {
        initialize();
    }

    /**
     * @see org.apache.tapestry.event.PageDetachListener#pageDetached(org.apache.tapestry.event.PageEvent)
     */
    public void pageDetached(PageEvent event)
    {
        initialize();
    }
    
    /**
     * @see org.apache.tapestry.event.PageRenderListener#pageBeginRender(org.apache.tapestry.event.PageEvent)
     */
    public void pageBeginRender(PageEvent event)
    {
        // values set during rewind are removed
        initialize();
    }

    /**
     * Initialize the values and return the object to operation identical
     * to that of the super class.
     */
    private void initialize()
    {
        m_nCurrentPage = -1;
        m_nPageCount = -1;
        m_nStartPage = -1;
        m_nStopPage = -1;
    }

    // This would ideally be a delayed invocation -- called after the form rewind
    public void changePage(IRequestCycle objCycle)
    {
        ITableModelSource objSource = getTableModelSource(); 
        setCurrentPage(objSource, getSelectedPage());

        // ensure that the change is saved
        objSource.fireObservedStateChange();
    }

    // defined in the JWC file
    public abstract int getSelectedPage();


    /**
     * @return the current page
     */
    public int getCurrentPage()
    {
        if (m_nCurrentPage < 0)
            m_nCurrentPage = super.getCurrentPage();
        return m_nCurrentPage;
    }

    /**
     * @return number of all pages to display
     */
    public int getPageCount()
    {
        if (m_nPageCount < 0)
            m_nPageCount = super.getPageCount();
        return m_nPageCount;
    }

    /**
     * @return the first page to display
     */
    public int getStartPage()
    {
        if (m_nStartPage < 0)
            m_nStartPage = super.getStartPage();
        return m_nStartPage;
    }

    /**
     * @return the last page to display
     */
    public int getStopPage()
    {
        if (m_nStopPage < 0)
            m_nStopPage = super.getStopPage();
        return m_nStopPage;
    }

    /**
     * @param i the current page
     */
    public void setCurrentPage(int i)
    {
        m_nCurrentPage = i;
    }

    /**
     * @param i number of all pages to display
     */
    public void setPageCount(int i)
    {
        m_nPageCount = i;
    }

    /**
     * @param i the first page to display
     */
    public void setStartPage(int i)
    {
        m_nStartPage = i;
    }

    /**
     * @param i the last page to display
     */
    public void setStopPage(int i)
    {
        m_nStopPage = i;
    }

}
