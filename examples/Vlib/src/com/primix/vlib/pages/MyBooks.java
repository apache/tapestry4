package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import javax.rmi.*;

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
 *  Shows a list of the user's books, allowing books to be editted or
 *  even deleted.
 *
 *  <p>Note that, unlike elsewhere, book titles do not link to the
 * {@link ViewBook} page.  It seems to me there would be a conflict between
 * that behavior and the edit behavior; making the book titles not be links
 *  removes the ambiguity over what happens when the book title is clicked
 *  (view vs. edit).
 *
 * @author Howard Ship
 * @version $Id$
 */


public class MyBooks extends Protected
{
	private String message;
	
	private Handle handle;
	private IBookQuery query;
	
	private Book currentBook;
	
	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		message = null;
		handle = null;
		query = null;
		currentBook = null;
	}
	
	/**
	 *  Updates the handle persistent page property, used to back the
	 *  book query.
	 *
	 */
	 
	public void setHandle(Handle value)
	{
		handle = value;
		
		fireObservedChange("handle", value);
	}

	public Handle getHandle()
	{
		return handle;
	}
	
	/**
	 *  Gets a reference to the book query session bean, restoring it from
	 *  the handle if necessary, or even creating it.
	 *
	 */
	 
	public IBookQuery getQuery()
	{
		IBookQueryHome home;
		VirtualLibraryApplication app;
		
		if (query != null)
			return query;
		
		if (handle != null)
		{
			try
			{
				query = (IBookQuery)handle.getEJBObject();
			}
			catch (RemoteException e)
			{
				throw new ApplicationRuntimeException(e.getMessage(), e);
			}
			
			return query;
		}
		
		// Create a new query.
		
		app = (VirtualLibraryApplication)application;
		home = app.getBookQueryHome();
			
		try
		{
			query = home.create();
			
			// Record the handle for the query in the page persistent property.
			
			setHandle(query.getHandle());
		}
		catch (CreateException e)
		{
			throw new ApplicationRuntimeException("Could not create BookQuery bean: " + e, e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
		
		return query;
	}
			
	
	/**
	 *  Gets the results of the query, the list of books owned by the user.
	 *
	 */
	 		
	public Book[] getOwnedBooks()
	{
		IBookQuery query;
		int count;
		VirtualLibraryApplication app;
		
		query = getQuery();
		app = (VirtualLibraryApplication)application;
		
		try
		{
			// This means that we do a new query every time we visit the page.
			// That may not be necessary, in which case, we should
			// do something smart about caching and cache clearing.
			
			count = query.ownerQuery(app.getUserPK());
			
			return query.get(0, count);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not find owned books: " + e, e);
		}
	}

	/**
	 *  Updates the currentBook dynamic page property.
	 *
	 */
	 
	public void setCurrentBook(Book value)
	{
		currentBook = value;
	}
	
	public Book getCurrentBook()
	{
		return currentBook;
	}

	public void setMessage(String value)
	{
		message = value;
	}
	
	public String getMessage()
	{
		return message;
	}

	/**
	 *  Listener that invokes the {@link EditProfile} page to allow a user
	 *  to edit thier name, etc.
	 *
	 */
	 
	public IDirectListener getEditProfileListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				EditProfile page;
				
				page = (EditProfile)cycle.getPage("EditProfile");
				
				page.beginEdit(cycle);
			}
		};
	}
	
	/**
	 *  Listener invoked to allow a user to edit a book.
	 *
	 *  <p>Note:  Could remove this if we change {@link EditBook} to
	 *  implement a {@link IExternalPage}, but that would require
	 */
	 
	public IDirectListener getEditListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				EditBook page;
				Integer bookPK;
				
				bookPK = new Integer(context[0]);
				page = (EditBook)cycle.getPage("EditBook");
				
				page.beginEdit(bookPK, cycle);
			}
		};
	}
	
	/**
	 *  Listener invoked to allow a user to delete a book.
	 *
	 */

	public IDirectListener getDeleteListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
				Integer bookPK;
				ConfirmBookDelete page;
				
				bookPK = new Integer(context[0]);
				
				page = (ConfirmBookDelete)cycle.getPage("ConfirmBookDelete");
				page.selectBook(bookPK, cycle);
			}
		};
	}

	/**
	 *  Should the holder be displayed on the page?  Only if the holder is someone
	 *  else than the owner.  The owner will always be the logged in user (that's
	 *  the point of the MyBooks page).
	 *
	 */
	 
	public boolean getShowHolder()
	{
		Integer ownerPK;
		Integer holderPK;
		
		ownerPK = currentBook.getOwnerPrimaryKey();
		holderPK = currentBook.getHolderPrimaryKey();
		
		return ! ownerPK.equals(holderPK);
	}
	
 	/**
	 *  Removes the book query bean, if the handle to the bean
	 *  is non-null.
	 *
	 */
	 
 	public void cleanupPage()
 	{
		if (handle == null)
			return;
		
		try
		{
			getQuery().remove();
		}
		catch (RemoveException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		super.cleanupPage();
 	}
}