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
import java.rmi.*;
import javax.rmi.*;

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


public class MyBooks
extends Protected
{
	private String message;
	
	private IBookQuery ownedQuery;
    private Book[] ownedBooks;

    private IBookQuery borrowedQuery;
    private Book[] borrowedBooks;
	
	private Book currentBook;
	
	public void detach()
	{
		message = null;
		ownedQuery = null;
        ownedBooks = null;
        borrowedQuery = null;
        borrowedBooks = null;
		currentBook = null;

		super.detach();
	}
	
    /**
     *  A dirty little secret of Tapestry and page recorders:  persistent
     *  properties must be set before the render (when this method is invoked)
     *  and can't change during the render.  We force
     *  the creation of the queries that will
     *  be referenced later.
     *
     */

    public void beginResponse(IResponseWriter writer, IRequestCycle cycle) 
    throws RequestCycleException
    {
        getOwnedQuery();
        getBorrowedQuery();
    }

    public void setOwnedQuery(IBookQuery value)
    {
        ownedQuery = value;

        fireObservedChange("ownedQuery", ownedQuery);
    }

    /**
     *  Gets the query object responsible for the finding books owned by the user.
     *
     */

	public IBookQuery getOwnedQuery()
	{
		
		if (ownedQuery == null)
            setOwnedQuery(getNewQuery());

        return ownedQuery;
	}

    private IBookQuery getNewQuery()
    {
		// Create a new query.
		
        VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		IBookQueryHome home = vengine.getBookQueryHome();
			
		try
		{
			return home.create();
		}
		catch (CreateException e)
		{
			throw new ApplicationRuntimeException("Could not create BookQuery bean: " + e, e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
		
	}
			
    public void setBorrowedQuery(IBookQuery value)
    {
        borrowedQuery = value;

        fireObservedChange("borrowedQuery", value);
    }
	
    public IBookQuery getBorrowedQuery()
    {
        if (borrowedQuery == null)
            setBorrowedQuery(getNewQuery());

        return borrowedQuery;
    }

	/**
	 *  Gets the results of the query, the list of books owned by the user.
	 *
	 */
	 		
	public Book[] getOwnedBooks()
	{
        if (ownedBooks == null)
        {
	        IBookQuery query = getOwnedQuery();
	
	        try
	        {
                Visit visit = (Visit)getVisit();
		        int count = query.ownerQuery(visit.getUserPK());
		
		        ownedBooks =  query.get(0, count);
	        }
	        catch (RemoteException e)
	        {
		        throw new ApplicationRuntimeException("Could not find owned books: " + e, e);
	        }
        }

        return ownedBooks;
	}


    public Book[] getBorrowedBooks()
    {
        if (borrowedBooks == null)
        {
            IBookQuery query = getBorrowedQuery();
    
            try
            {
                Visit visit = (Visit)getVisit();
    	        int count = query.borrowerQuery(visit.getUserPK());
    	
    	        borrowedBooks =  query.get(0, count);
            }
            catch (RemoteException e)
            {
    	        throw new ApplicationRuntimeException("Could not find borrowed books: " + e, e);
            }
        }

        return borrowedBooks;
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
			public void directTriggered(IDirect direct, String[] context,
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
			public void directTriggered(IDirect direct, String[] context,
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
			public void directTriggered(IDirect direct, String[] context,
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
     *  Listener used to return a book.
     *
     */

    public IDirectListener getReturnListener()
    {
        return new IDirectListener()
        {
    	    public void directTriggered(IDirect direct, String[] context,
    			    IRequestCycle cycle)
    	    {
                Integer bookPK;

                bookPK = new Integer(context[0]);

                returnBook(bookPK);
      	    }
        };
    }

    private void returnBook(Integer bookPK)
    {
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		IOperations operations = vengine.getOperations();

        try
        {
            IBook book = operations.returnBook(bookPK);

            setMessage("Returned book: " + book.getTitle());
        }
        catch (FinderException ex)
        {
            setError("Could not return book: " + ex.getMessage());
            return;
        }
        catch (RemoteException ex)
        {
            throw new ApplicationRuntimeException(ex);
        }
    }
    
	
    /**
     *  Listener used to return a book.
     *
     */

    public IDirectListener getAddNewBookListener()
    {
        return new IDirectListener()
        {
    	    public void directTriggered(IDirect direct, String[] context,
    			    IRequestCycle cycle)
    	    {
				NewBook page = (NewBook)cycle.getPage("NewBook");
				
				// Setup defaults for the new book.
				
				page.getAttributes().put("lendable", Boolean.TRUE);
				
				cycle.setPage(page);
      	    }
        };
    }
	
 	/**
	 *  Removes the book query beans.
	 */
	 
 	public void cleanupPage()
 	{
		try
		{
			if (ownedQuery != null)
			    ownedQuery.remove();

            if (borrowedQuery != null)
                borrowedQuery.remove();

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
