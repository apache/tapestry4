package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;

/*
 * Copyright (c) 2000 by Howard Ship and Primix Solutions
 *
 * Primix Solutions
 * One Arsenal Marketplace
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

/**
 *  Run's queries and displays matches.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class Matches extends BasePage
{
	private transient IBookQuery bookQuery;
	private Handle bookQueryHandle;
	private Book currentMatch;
	private int matchCount;
	
	public Matches(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		bookQuery = null;
		bookQueryHandle = null;
		currentMatch = null;
		matchCount = 0;
	}
	
	public int getMatchCount()
	{
		return matchCount;
	}
	
	public void setMatchCount(int value)
	{
		matchCount = value;
		
		fireObservedChange("matchCount", value);
	}
	
	public Handle getBookQueryHandle()
	{
		return bookQueryHandle;
	}
	
	public void setBookQueryHandle(Handle value)
	{
		bookQueryHandle = value;
		fireObservedChange("bookQueryHandle", value);
	}
	
	public IBookQuery getBookQuery()
	{
		VirtualLibraryApplication app;
		IBookQueryHome home;
		
		if (bookQuery == null)
		{
			try
			{
				if (bookQueryHandle != null)
				{
					bookQuery = (IBookQuery)PortableRemoteObject.narrow(
												bookQueryHandle.getEJBObject(),
												IBookQuery.class); 
					return bookQuery;
				}
			
				// No existing handle, so time to create a new bean.
				
				app = (VirtualLibraryApplication)application;
				
				home = app.getBookQueryHome();
				
				setBookQuery(home.create());
			}
			catch (Throwable t)
			{
				throw new ApplicationRuntimeException(t);
			}
		}

		return bookQuery;
	}
	
	public void setBookQuery(IBookQuery value)
	{
		bookQuery = value;
		
		try
		{
			if (value == null)
				setBookQueryHandle(null);
			else	
				setBookQueryHandle(value.getHandle());
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
	}
	
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
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(t);
		}
		
	}
	
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
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(t);
		}
	}
	
	public Book getCurrentMatch()
	{
		return currentMatch;
	}
	
	public void setCurrentMatch(Book value)
	{
		currentMatch = value;
	}
	
	public IDirectListener getBookDetailListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context, 
						IRequestCycle cycle)
						throws RequestCycleException
			{
			}
		};
	}
	
	public IDirectListener getPersonLinkListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context, 
						IRequestCycle cycle)
						throws RequestCycleException
			{
			}
		};
	}
	
	/**
	 *  Returns true if the holder of a book doesn't match the owner of the book.
	 *  This cleans up the output a bit.
	 *
	 */
	 
	public boolean getShowHolder()
	{
		Integer ownerPrimaryKey;
		Integer holderPrimaryKey;
		
		ownerPrimaryKey = currentMatch.getOwnerPrimaryKey();
		holderPrimaryKey = currentMatch.getHolderPrimaryKey();
		
		return ! ownerPrimaryKey.equals(holderPrimaryKey);		
	}
	
	public boolean getEnableBorrowBookLink()
	{
		VirtualLibraryApplication app;
      
		app = (VirtualLibraryApplication)application;
        
        // Can't borrow until logged in.
        
        if (!app.isUserLoggedIn())
            return false;
 
 		// Otherwise, can only borrow it if not already holding it.
		
		return ! app.isLoggedInUser(currentMatch.getHolderPrimaryKey());
	}
    
    public IDirectListener getBorrowBookListener()
    {
        return new IDirectListener()
        {
            public void directTriggered(IComponent component, String[] context,
                    IRequestCycle cycle)
					throws RequestCycleException
            {
				Integer bookPK;
				
				// The primary key of the book to borrow is encoded in the context.
				bookPK = new Integer(context[0]);

				borrowBook(bookPK, cycle);
            }
        };
    }

	private void borrowBook(Integer bookPK, IRequestCycle cycle)
	throws RequestCycleException
	{
		VirtualLibraryApplication app;
		IOperations bean;
		Home home;
		Integer borrowerPK;
		IBook book;

		app = (VirtualLibraryApplication)application;

		home = (Home)cycle.getPage("Home");

		bean = app.getOperations();				

		borrowerPK = app.getUserPK();

		try
		{
			book = bean.borrowBook(bookPK, borrowerPK);

			home.setMessage("Borrowed: " + book.getTitle());
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException(
				"Unable to find book or user: " + e.getMessage(), e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not borrow book: " + e, e);
		}

		cycle.setPage(home);				
	}
}
