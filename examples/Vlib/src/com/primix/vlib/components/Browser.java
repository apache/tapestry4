/*
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
 * Watertown, MA 02472
 * http://www.primix.com
 * mailto:hship@primix.com
 * 
 * This library is free software.
 * 
 * You may redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation.
 *
 * Version 2.1 of the license should be included with this distribution in
 * the file LICENSE, as well as License.html. If the license is not
 * included with this distribution, you may find a copy at the FSF web
 * site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
 * Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; wihtout even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.vlib.components;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.*;
import com.primix.vlib.pages.*;
import com.primix.vlib.ejb.*;
import java.rmi.*;
import javax.ejb.*;
import java.util.*;

/**
 *  Implements a paging browser for the results of a {@link IBookQuery}.
 *
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Browser
	extends BaseComponent 
	implements ILifecycle
{
	private IBinding queryBinding;
	private IBookQuery query;
	private int currentPage;
	private int resultCount;
	private int pageCount;
	private int jumpPage;
	private List pageResults;
	private IDirectListener jumpListener;
	
	/**
	 *  Default for the page size; the number of results viewed on each page.
	 *
	 */
	
	public static final int DEFAULT_PAGE_SIZE = 15;
	
	/**
	 *  Later, if we add the ability to change the page size
	 *  (number of results shown per page),
	 *  we can make this an instance variable.
	 *
	 */
	
	private static int pageSize = DEFAULT_PAGE_SIZE;
	
	/**
	 * Clear out cached values at the end of the request cycle.
	 *
	 */
	
	public void reset()
	{
		query = null;
		resultCount = 0;
		currentPage = 0;
		pageCount = 0;
		jumpPage = 0;
		
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
			query = (IBookQuery)queryBinding.getObject("query", IBookQuery.class);
		
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
	
	public void setJumpPage(int value)
	{
		jumpPage = value;
	}
	
	public int getJumpPage()
	{
		return jumpPage;
	}
	
	/**
	 *  Invoked by the container when the query (otherwise accessed via the query
	 *  parameter) changes.  Re-caches the number of results and sets the current page
	 *  back to 1.
	 *
	 */
	
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
	
	private static final int RANGE = 5;
	
	/**
	 * Returns a sorted {@link List} or {@link Integer}.
	 *
	 */
	
	public List getJumpPages()
	{
		Set pageSet = new TreeSet();
		int pageCount = getPageCount();
		
		for (int i = -RANGE; i < RANGE; i++)
		{
			int page = currentPage + i;
			
			if (page >= 1 && page <= pageCount)
				pageSet.add(new Integer(page));
		}
		
		// If the current page of pages doesn't extend to the
		// end of the list then add in the first page and
		// every tenth page.
		
		if (currentPage + RANGE < pageCount)
		{
			pageSet.add(new Integer(1));
			
			for (int i = 10; i <= pageCount; i += 10)
				pageSet.add(new Integer(i));
		}
		
		// Because its a set, there are no duplicates.  Convert to a List
		// and return.
		
		return new ArrayList(pageSet);		
	}
	
	/**
	 * Returns a subset of the results from the query corresponding
	 * to the current page of results.  This may be null if there
	 * are no results.  All pages but the last have the same
	 * number of results, the final page may be short a few.
	 *
	 */
	
	public Book[] getPageResults()
	{
		if (pageResults == null)
			pageResults = new ArrayList(pageSize);
		
		pageResults.clear();
		
		int resultCount = getResultCount();
		
		int low = (currentPage - 1) * pageSize;
		int high = Math.min (currentPage * pageSize, resultCount) - 1;
		
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
	
	public IDirectListener getJumpListener()
	{
		if (jumpListener == null)
			jumpListener = new IDirectListener()
		{
			public void directTriggered(IDirect component, String[] context,
					IRequestCycle cycle)
				throws RequestCycleException
			{
				int page = Integer.parseInt(context[0]);
				
				jump(page);
			}
		};
		
		return jumpListener;
	}
}
