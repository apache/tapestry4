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
 * Displays the book inventory list for a single {@link IPerson}, showing
 * what books are owned by the person, who has them borrowed, etc.  If the
 * user is logged in, then books can be borrowed from this page as well.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class PersonPage extends BasePage
implements IExternalPage
{
	private transient IBookQuery bookQuery;
	private Handle bookQueryHandle;
	private Book currentMatch;
	private int matchCount;
	private String email;
	private String fullName;
	
	public PersonPage(IApplication application, ComponentSpecification componentSpecification)
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
		email = null;
		fullName = null;
	}
	
	public String getEmail()
	{
		return email;
	}
	
	public String getEmailURL()
	{
		return "mailto:" + email;
	}
	
	/**
	 *  Sets the email transient page property.
	 *
	 */
	 
	public void setEmail(String value)
	{
		email = value;
	}
		
	public String getFullName()
	{
		return fullName;
	}
	
	/**
	 *  Sets the fullName transient page property.
	 *
	 */
	
	public void setFullName(String value)
	{
		fullName = value;
	}
	
	public int getMatchCount()
	{
		return matchCount;
	}
	
	/**
	 *  Sets the matchCount transient page property.  This is set when
	 *  the onwer query is run.
	 *
	 */
	 
	public void setMatchCount(int value)
	{
		matchCount = value;
	}
	
	public Handle getBookQueryHandle()
	{
		return bookQueryHandle;
	}
	
	/**
	 *  Sets the handle of the {@link IBookQuery} session bean used to
	 *  locate books owned by the person.
	 *
	 */
	 
	public void setBookQueryHandle(Handle value)
	{
		bookQueryHandle = value;
		fireObservedChange("bookQueryHandle", value);
	}
	
	/**
	 *  Gets the {@link IBookQuery} session bean that contains
	 *  the books owned by the user, restoring it from the
	 *  handle property, or creating it fresh as needed.
	 *
	 */
	 
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
	
	/**
	 *  Sets the bookQuery transient page property, which also sets
	 *  the handle persistent page property.
	 *
	 */
	 
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
	
	/**
	 *  Invoked by the external service to being viewing the
	 *  identified person.
	 *
	 */
	 
	public void setup(Integer personPK, IRequestCycle cycle)
	{
		VirtualLibraryApplication app;
		int count;
		IPersonHome home;
		IPerson person;
		IBookQuery query;
		Home homePage;
		
		app = (VirtualLibraryApplication)application;
		
		query = getBookQuery();
		
		try
		{
			count = query.ownerQuery(personPK);
			setMatchCount(count);
			
			home = app.getPersonHome();
			person = home.findByPrimaryKey(personPK);
			
			setEmail(person.getEmail());
			setFullName(person.getNaturalName());
			
		}
		catch (FinderException e)
		{
			homePage = (Home)cycle.getPage("Home");
			homePage.setError("Person " + personPK + " not found in database.");
			
			cycle.setPage(homePage);
			
			return;
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		cycle.setPage(this);
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
	
	public boolean getEnableBorrow()
	{
		VirtualLibraryApplication app;
      
		app = (VirtualLibraryApplication)application;
        
        // Can't borrow until logged in.
        
        if (!app.isUserLoggedIn())
            return false;
 
 		// Otherwise, can only borrow it if not already holding it.
		
		return ! app.isLoggedInUser(currentMatch.getHolderPrimaryKey());
	}
    
}
