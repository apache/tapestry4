/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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
 * 
 * <p>
 * <table border=1 align="center">
 * <tr>
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Direction </th>
 *    <th>Required</th>
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>pagesDisplayed</td>
 *  <td>int</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>7</td>
 *  <td align="left">Determines the maximum number of pages to be displayed in the page list
 *      when the table has more than one page.
 *      <p>For example, if the table has 20 pages, and 10 is the current page,
 *      pages from 7 to 13 in the page list will be shown if this parameter has 
 *      a value of 7.
 *  </td> 
 * </tr>
 *
 * </table> 
 * 
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
     * @return
     */
    public int getCurrentPage()
    {
        if (m_nCurrentPage < 0)
            m_nCurrentPage = super.getCurrentPage();
        return m_nCurrentPage;
    }

    /**
     * @return
     */
    public int getPageCount()
    {
        if (m_nPageCount < 0)
            m_nPageCount = super.getPageCount();
        return m_nPageCount;
    }

    /**
     * @return
     */
    public int getStartPage()
    {
        if (m_nStartPage < 0)
            m_nStartPage = super.getStartPage();
        return m_nStartPage;
    }

    /**
     * @return
     */
    public int getStopPage()
    {
        if (m_nStopPage < 0)
            m_nStopPage = super.getStopPage();
        return m_nStopPage;
    }

    /**
     * @param i
     */
    public void setCurrentPage(int i)
    {
        m_nCurrentPage = i;
    }

    /**
     * @param i
     */
    public void setPageCount(int i)
    {
        m_nPageCount = i;
    }

    /**
     * @param i
     */
    public void setStartPage(int i)
    {
        m_nStartPage = i;
    }

    /**
     * @param i
     */
    public void setStopPage(int i)
    {
        m_nStopPage = i;
    }

}
