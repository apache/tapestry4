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
 *  
 *
 * @author Howard Ship
 * @version $Id$
 */


public class NewBook extends Protected
{
	private Map attributes;
	private String publisherName;
	private boolean cancel;
	
	public void detach()
	{
		attributes = null;
		publisherName = null;
		cancel = false;
		
		super.detach();
	}
	
	public Map getAttributes()
	{
		if (attributes == null)
			attributes = new HashMap();
		
		return attributes;
	}
	
	public String getPublisherName()
	{
		return publisherName;
	}
	
	public void setPublisherName(String value)
	{
		publisherName = value;
	}
	
	public void setCancel(boolean value)
	{
		cancel = value;
	}
	
	public boolean getCancel()
	{
		return cancel;
	}
	
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				// Add the book or set an error message.
				
				addBook(cycle);
			}
		};
	}
	
	private void addBook(IRequestCycle cycle)
	{
		IBook book = null;	
	
		if (cancel)
		{
			cycle.setPage("MyBooks");
			return;
		}
		
		if (getError() != null)
			return;
		
		Map attributes = getAttributes();
		
		Integer publisherPK = (Integer)attributes.get("publisherPK");
		
		if (publisherPK == null && Tapestry.isNull(publisherName))
		{
			setErrorField("inputPublisherName",
					"Must enter a publisher name or select an existing publisher from the list.");
			return;
		}
		
		if (publisherPK != null && ! Tapestry.isNull(publisherName))
		{
			setErrorField("inputPublisherName",
					"Must either select an existing publisher or enter a new publisher name.");
			return;
		}
		
		Visit visit = (Visit)getVisit();
		Integer userPK = visit.getUserPK();
		
		IOperations operations = visit.getEngine().getOperations();		
		
		attributes.put("ownerPK", userPK);
		attributes.put("holderPK", userPK);
		
		try
		{
			if (publisherPK != null)
				book = operations.addBook(attributes);
			else
			{
				book = operations.addBook(attributes, publisherName);
				
				// Clear the app's cache of info; in this case, known publishers.
				
				visit.clearCache();
			}	
		}
		catch (CreateException e)
		{
			setError("Error adding book: " + e.getMessage());
			return;
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e.getMessage(), e);
		}
		
		// Success.  First, update the message property of the return page.
		
		MyBooks myBooks = (MyBooks)cycle.getPage("MyBooks");
		
		myBooks.setMessage("Added: " + attributes.get("title"));
		
		cycle.setPage(myBooks);
	}
	
}
