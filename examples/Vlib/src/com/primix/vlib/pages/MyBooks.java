package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.spec.*;
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
 *  
 *
 * @author Howard Ship
 * @version $Id$
 */


public class MyBooks extends BasePage
{
	private String error;
	private String message;
	
	private Handle handle;
	private IBookQuery query;
	
	private Book currentBook;
	
	public MyBooks(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();
		
		error = null;
		message = null;
		handle = null;
		query = null;
		currentBook = null;
	}
	
	public void setHandle(Handle value)
	{
		handle = value;
		
		fireObservedChange("handle", value);
	}

	public Handle getHandle()
	{
		return handle;
	}
	
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
			
			
	public Book[] getOwnedBooks()
	{
		IBookQuery query;
		int count;
		VirtualLibraryApplication app;
		
		query = getQuery();
		app = (VirtualLibraryApplication)application;
		
		try
		{
			count = query.ownerQuery(app.getUserPK());
			
			return query.get(0, count);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException("Could not find owned books: " + e, e);
		}
	}

	public void setCurrentBook(Book value)
	{
		currentBook = value;
	}
	
	public Book getCurrentBook()
	{
		return currentBook;
	}

	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
	}
	
	public void setMessage(String value)
	{
		message = value;
	}
	
	public String getMessage()
	{
		return message;
	}

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

	public IDirectListener getDeleteListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
			}
		};
	}

	public IDirectListener getShowHolderListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context,
					IRequestCycle cycle)
			{
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
}