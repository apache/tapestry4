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

package net.sf.tapestry.vlib.components;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.ApplicationRuntimeException;
import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.vlib.ejb.Book;
import net.sf.tapestry.vlib.ejb.IBookQuery;

/**
 *  Implements a paging browser for the results of a {@link IBookQuery}.
 *
 *
 *  @author Howard Lewis Ship
 *  @version $Id$
 *
 **/

public class Browser extends BaseComponent implements PageDetachListener
{
    private IBinding queryBinding;
    private IBookQuery query;
    private int currentPage;
    private int resultCount;
    private int pageCount;
    private List pageResults;

    /**
     *  Default for the page size; the number of results viewed on each page.
     *
     **/

    public static final int DEFAULT_PAGE_SIZE = 15;

    /**
     *  Later, if we add the ability to change the page size
     *  (number of results shown per page),
     *  we can make this an instance variable.
     *
     **/

    private static int pageSize = DEFAULT_PAGE_SIZE;

    /**
     *  Register this component as a {@link PageDetachListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        page.addPageDetachListener(this);
    }

    /**
     * Clear out cached values at the end of the request cycle.
     *
     *  @since 1.0.5
     **/

    public void pageDetached(PageEvent event)
    {
        query = null;
        resultCount = 0;
        currentPage = 0;
        pageCount = 0;

        if (pageResults != null)
            pageResults.clear();
    }

    public void setQueryBinding(IBinding value)
    {
        queryBinding = value;
        query = null;
    }

    public IBinding getQueryBinding()
    {
        return queryBinding;
    }

    public IBookQuery getQuery()
    {
        if (query == null)
            query = (IBookQuery) queryBinding.getObject("query", IBookQuery.class);

        return query;
    }

    public int getResultCount()
    {
        return resultCount;
    }

    public void setResultCount(int value)
    {
        resultCount = value;

        fireObservedChange("resultCount", value);
    }

    public int getCurrentPage()
    {
        return currentPage;
    }

    public void setCurrentPage(int value)
    {
        currentPage = value;

        fireObservedChange("currentPage", value);
    }

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
    }

    public int getPageCount()
    {
        if (pageCount == 0)
            pageCount = computePageCount();

        return pageCount;
    }

    private int computePageCount()
    {
        // For 0 ... pageSize  elements, its just one page.

        if (resultCount <= pageSize)
            return 1;

        // We need the number of results divided by the results per page.

        int result = resultCount / pageSize;

        // If there's any left-over, then we need an additional page.

        if (resultCount % pageSize > 0)
            result++;

        return result;
    }

    public int getPreviousPage()
    {
        return Math.max(currentPage - 1, 1);
    }

    public int getNextPage()
    {
        return Math.min(currentPage + 1, getPageCount());
    }

    /**
     *  Returns a subset of the results from the query corresponding
     *  to the current page of results.  This may be null if there
     *  are no results.  All pages but the last have the same
     *  number of results, the final page may be short a few.
     *
     **/

    public Book[] getPageResults()
    {
        if (pageResults == null)
            pageResults = new ArrayList(pageSize);

        pageResults.clear();

        int resultCount = getResultCount();

        int low = (currentPage - 1) * pageSize;
        int high = Math.min(currentPage * pageSize, resultCount) - 1;

        if (low > high)
            return null;

        try
        {
            return getQuery().get(low, high - low + 1);
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }

    private void jump(int page)
    {
        if (page < 2)
        {
            setCurrentPage(1);
            return;
        }

        int pageCount = getPageCount();
        if (page > pageCount)
        {
            setCurrentPage(pageCount);
            return;
        }

        setCurrentPage(page);
    }

    public void jump(IRequestCycle cycle)
    {
        Object[] parameters = cycle.getServiceParameters();
        
        int page = ((Integer)parameters[0]).intValue();

        jump(page);
    }

    public String getRange()
    {
        int resultCount = getResultCount();

        int low = (currentPage - 1) * pageSize + 1;
        int high = Math.min(currentPage * pageSize, resultCount);

        return low + " - " + high;
    }

    public boolean getShowPreviousLink()
    {
        return currentPage > 1;
    }

    public boolean getShowNextLink()
    {
        return currentPage < getPageCount();
    }
}