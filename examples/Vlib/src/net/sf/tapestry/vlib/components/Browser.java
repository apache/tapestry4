/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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
     *  Register this component as a {@link PageDetachListener}.
     *
     *  @since 1.0.5
     *
     **/

    protected void finishLoad()
    {
        getPage().addPageDetachListener(this);
    }

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