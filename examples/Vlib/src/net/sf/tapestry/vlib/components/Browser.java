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
    private IBinding _queryBinding;
    private IBookQuery _query;
    private int _currentPage;
    private int _resultCount;
    private int _pageCount;
    private List _pageResults;

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
     * Clear out cached values at the end of the request cycle.
     *
     *  @since 1.0.5
     * 
     **/

    public void pageDetached(PageEvent event)
    {
        _query = null;
        _resultCount = 0;
        _currentPage = 0;
        _pageCount = 0;

        if (_pageResults != null)
            _pageResults.clear();
    }

    public void setQueryBinding(IBinding value)
    {
        _queryBinding = value;
        _query = null;
    }

    public IBinding getQueryBinding()
    {
        return _queryBinding;
    }

    public IBookQuery getQuery()
    {
        if (_query == null)
            _query = (IBookQuery) _queryBinding.getObject("query", IBookQuery.class);

        return _query;
    }

    public int getResultCount()
    {
        return _resultCount;
    }

    public void setResultCount(int resultCount)
    {
        _resultCount = resultCount;

        fireObservedChange("resultCount", resultCount);
    }

    public int getCurrentPage()
    {
        return _currentPage;
    }

    public void setCurrentPage(int currentPage)
    {
        _currentPage = currentPage;

        fireObservedChange("currentPage", currentPage);
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
        if (_pageCount == 0)
            _pageCount = computePageCount();

        return _pageCount;
    }

    private int computePageCount()
    {
        // For 0 ... pageSize  elements, its just one page.

        if (_resultCount <= pageSize)
            return 1;

        // We need the number of results divided by the results per page.

        int result = _resultCount / pageSize;

        // If there's any left-over, then we need an additional page.

        if (_resultCount % pageSize > 0)
            result++;

        return result;
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
        if (_pageResults == null)
            _pageResults = new ArrayList(pageSize);

        _pageResults.clear();

        int resultCount = getResultCount();

        int low = (_currentPage - 1) * pageSize;
        int high = Math.min(_currentPage * pageSize, resultCount) - 1;

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

        int low = (_currentPage - 1) * pageSize + 1;
        int high = Math.min(_currentPage * pageSize, resultCount);

        return low + " - " + high;
    }
}