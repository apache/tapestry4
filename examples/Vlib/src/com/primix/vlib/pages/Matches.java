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

package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;

/**
 *  Run's queries and displays matches.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Matches extends BasePage
{
	private IBookQuery bookQuery;
	private Book currentMatch;
	private int matchCount;
	
	public void detach()
	{
		super.detach();
		
		bookQuery = null;
		currentMatch = null;
		matchCount = 0;
	}
	
	public int getMatchCount()
	{
		return matchCount;
	}
	
	/**
	 *  Sets the persistent matchCount property, the total number of
	 *  matches in the result.
	 *
	 */
	 
	public void setMatchCount(int value)
	{
		matchCount = value;
		
		fireObservedChange("matchCount", value);
	}
	
	/**
	 *  Gets the {@link IBookQuery} session bean for the query, creating
     *  it fresh if necessary.
	 *
	 */
	 
	public IBookQuery getBookQuery()
	{
		if (bookQuery == null)
		{
			try
			{
				// No existing handle, so time to create a new bean.
				
                VirtualLibraryEngine vengine = (VirtualLibraryEngine)getEngine();
				IBookQueryHome home = vengine.getBookQueryHome();
				
                bookQuery = home.create();

				fireObservedChange("bookQuery", bookQuery);
			}
		    catch (CreateException ex)
		    {
			    throw new ApplicationRuntimeException(ex);
		    }
		    catch (RemoteException ex)
		    {
			    throw new ApplicationRuntimeException(ex);
		    }
		}

		return bookQuery;
	}
	
	/**
	 *  Sets the persistent bookQuery property.
	 *
	 */
	 
	public void setBookQuery(IBookQuery value)
	{
		bookQuery = value;
		
        fireObservedChange("bookQuery", value);
	}
	
	/**
	 *  Invoked by the {@link Home} page to perform a query.
	 *
	 */
	 
	public void performQuery(String title, String author, Object publisherPK)
	{
		IBookQueryHome home;
		IBookQuery query;
		int count;
		
		query = getBookQuery();
		
		try
		{
			count = query.masterQuery(title, author, publisherPK);
			setMatchCount(count);
		}
		catch (RemoteException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
		
	}
	
	/**
	 * Gets the matches for the query ... in the future, this will be
	 * part of a larger mechanism for presenting a paged view of
	 * the matches.
	 *
	 */
	 
	public Book[] getMatches()
	{
		int count;
		IBookQuery query;
		
		try
		{
			query = getBookQuery();
			
			count = query.getResultCount();
			
			return query.get(0, count);
		}
		catch (RemoteException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
	}

	public Book getCurrentMatch()
	{
		return currentMatch;
	}

	/**
	 *  Updates the dynamic currentMatch property.
	 *
	 */
	 	
	public void setCurrentMatch(Book value)
	{
		currentMatch = value;
	}
		
 	/**
	 *  Removes the book query bean, if not null.
	 *
	 */
	 
 	public void cleanupPage()
 	{
		try
		{
			if (bookQuery != null)
			    bookQuery.remove();
		}
		catch (RemoveException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
		catch (RemoteException ex)
		{
			throw new ApplicationRuntimeException(ex);
		}
		
		super.cleanupPage();
 	}
}
