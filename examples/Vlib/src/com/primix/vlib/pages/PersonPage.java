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
import com.primix.tapestry.spec.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import com.primix.vlib.components.*;
import javax.ejb.*;
import java.util.*;
import javax.rmi.*;
import java.rmi.*;

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
	private IBookQuery query;
	private Book currentMatch;
	private String email;
	private String fullName;
	
	private Browser browser;
	
	public void detach()
	{
		query = null;
		currentMatch = null;
		email = null;
		fullName = null;
		
		super.detach();
	}
	
	public void finishLoad(IPageLoader loader, ComponentSpecification spec)
		throws PageLoaderException
	{
		super.finishLoad(loader, spec);
		
		browser = (Browser)getComponent("browser");
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
	
	
	/**
	 *  Gets the {@link IBookQuery} session bean that contains
	 *  the books owned by the user, creating it fresh as needed.
	 *
	 */
	
	public IBookQuery getQuery()
	{		
		if (query == null)
		{	
			VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
			setQuery(vengine.createNewQuery());
		}
		
		return query;
	}
	
	/**
	 *  Sets the query persistent page property.
	 *
	 */
	
	public void setQuery(IBookQuery value)
	{
		query = value;
		
		fireObservedChange("query", value);
	}
	
	/**
	 *  Invoked by the external service to being viewing the
	 *  identified person.
	 *
	 */
	
	public void setup(Integer personPK, IRequestCycle cycle)
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IBookQuery query = getQuery();
				
				int count = query.ownerQuery(personPK);
				
				browser.initializeForResultCount(count);
				
				IOperations operations = vengine.getOperations();
				
				Person person = operations.getPerson(personPK);
				
				setEmail(person.getEmail());
				setFullName(person.getNaturalName());
				
				break;
			}
			catch (FinderException e)
			{
				vengine.presentError(
						"Person " + personPK + " not found in database.", cycle);
				return;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception for owner query.", ex, i > 0);
				
				setQuery(null);
				
			}
		}
		
		cycle.setPage(this);
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
	 *  Removes the book query bean, if the handle to the bean
	 *  is non-null.
	 *
	 */
	
	public void cleanupPage()
	{
		try
		{
			if (query != null)
				query.remove();
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
