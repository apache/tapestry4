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

package org.apache.tapestry.vlib.components;

import java.rmi.RemoteException;

import org.apache.tapestry.AbstractComponent;
import org.apache.tapestry.IActionListener;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IPage;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.vlib.VirtualLibraryEngine;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.IBookQuery;

/**
 *  Implements a paging browser for the results of a {@link IBookQuery}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public abstract class Browser extends AbstractComponent implements PageRenderListener
{
    public abstract IBookQuery getQuery();

    /**
     *  Default for the page size; the number of results viewed on each page.
     *
     **/

    public static final int DEFAULT_PAGE_SIZE = 15;

    /**
     *  The maximum number of items to display on a page.
     * 
     **/

    private int _pageSize = DEFAULT_PAGE_SIZE;

    public abstract int getResultCount();

    public abstract void setResultCount(int resultCount);

    public abstract int getCurrentPage();

    public abstract void setCurrentPage(int currentPage);

    public abstract void setElement(String element);

    public abstract String getElement();

    public abstract void setValue(Object value);

    public abstract IActionListener getListener();

    public abstract Object[] getPageResults();

    public abstract void setPageResults(Object[] pageResults);

    /**
     *  Invoked by the container when the query (otherwise accessed via the query
     *  parameter) changes.  Re-caches the number of results and sets the current page
     *  back to 1.
     *
     **/

    public void initializeForResultCount(int resultCount)
    {
        setResultCount(resultCount);
        setCurrentPage(1);
        setPageCount(computePageCount());
    }

    public abstract int getPageCount();

    public abstract void setPageCount(int pageCount);

    private int computePageCount()
    {
        // For 0 ... pageSize  elements, its just one page.

        int resultCount = getResultCount();

        if (resultCount <= _pageSize)
            return 1;

        // We need the number of results divided by the results per page.

        int result = resultCount / _pageSize;

        // If there's any left-over, then we need an additional page.

        if (resultCount % _pageSize > 0)
            result++;

        return result;
    }

	/**
	 *  Invoked to change the displayed page number.
	 * 
	 *  @param page page to display, numbered from one.  The currentPage property will be
	 *  updated.  The value is constrained to fit in the valid range of pages
	 *  for the component.
	 * 
	 **/
	
    public void jump(int page)
    {
        if (page < 2)
        {
            setCurrentPage(1);
            return;
        }

        int pageCount = getPageCount();
        if (page > getPageCount())
        {
            setCurrentPage(pageCount);
            return;
        }

        setCurrentPage(page);
    }

    public boolean getDisableBack()
    {
        return getCurrentPage() <= 1;
    }

    public boolean getDisableNext()
    {
        return getCurrentPage() >= getPageCount();
    }

    public String getRange()
    {
        int currentPage = getCurrentPage();
        int resultCount = getResultCount();

        int low = (currentPage - 1) * _pageSize + 1;
        int high = Math.min(currentPage * _pageSize, resultCount);

        return low + " - " + high;
    }

    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object[] books = getPageResults();
        int count = Tapestry.size(books);
		String element = getElement();
		
        for (int i = 0; i < count; i++)
        {
            setValue(books[i]);

            if (element != null)
            {
                writer.begin(element);
                renderInformalParameters(writer, cycle);
            }

            renderBody(writer, cycle);

            if (element != null)
                writer.end();
        }
    }

    protected void finishLoad()
    {
        setElement("tr");
    }

    public void pageBeginRender(PageEvent event)
    {
        int resultCount = getResultCount();
        int currentPage = getCurrentPage();

        int low = (currentPage - 1) * _pageSize;
        int high = Math.min(currentPage * _pageSize, resultCount) - 1;

        if (low > high)
            return;

        Book[] pageResults = null;
        
        int i = 0;
        while (true)
        {

            try
            {
                pageResults = getQuery().get(low, high - low + 1);

                break;
            }
            catch (RemoteException ex)
            {
                IPage page = getPage();

                if (i++ == 0)
                    getListener().actionTriggered(this, page.getRequestCycle());
                else
                {
                    VirtualLibraryEngine vengine = (VirtualLibraryEngine) page.getEngine();
                    vengine.rmiFailure("Unable to retrieve query results.", ex, i);
                }

            }
        }

        setPageResults(pageResults);
    }

}