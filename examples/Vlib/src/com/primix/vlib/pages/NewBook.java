package com.primix.vlib.pages;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.ejb.*;
import com.primix.vlib.*;
import javax.ejb.*;
import java.util.*;
import java.rmi.*;
import javax.rmi.*;
import com.primix.foundation.prop.*;

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


public class NewBook extends Protected
{
	private String title;
	private String ISBN;
	private Integer publisherPK;
	private String publisherName;
	private String bookDescription;
	private String author;
	
	public void detach()
	{
		title = null;
		ISBN = null;
		publisherPK = null;
		publisherName = null;
		bookDescription = null;
		author = null;

    	super.detach();
	}
	
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String value)
	{
		title = value;
	}
	
	public String getISBN()
	{
		return ISBN;
	}
	
	public void setISBN(String value)
	{
		ISBN = value;
	}
	
	public Integer getPublisherPrimaryKey()
	{
		return publisherPK;
	}
	
	public void setPublisherPrimaryKey(Integer value)
	{
		publisherPK = value;
	}
	
	public String getPublisherName()
	{
		return publisherName;
	}
	
	public void setPublisherName(String value)
	{
		publisherName = value;
	}
	
	public String getBookDescription()
	{
		return bookDescription;
	}
	
	public void setBookDescription(String value)
	{
		bookDescription = value;
	}
	
	public void setAuthor(String value)
	{
		author = value;
	}
	
	public String getAuthor()
	{
		return author;
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
        IBook book;
        
        if (getError() != null)
            return;

		if (publisherPK == null && isEmpty(publisherName))
		{
			setErrorField("inputPublisherName",
			    "Must enter a publisher name or select an existing publisher from the list.");
			return;
		}
		
		if (publisherPK != null && !isEmpty(publisherName))
		{
			setErrorField("inputPublisherName",
			    "Must either select an existing publisher or enter a new publisher name.");
			return;
		}
		
        Visit visit = (Visit)getVisit();
		IOperations operations = visit.getOperations();		
		Integer userPK = visit.getUserPK();
		
		try
		{
			if (publisherPK != null)
				book = operations.addBook(userPK, title, author, ISBN, 
										bookDescription, publisherPK);
			else
			{
				book = operations.addBook(userPK, title, author, ISBN,
										bookDescription, publisherName);
				
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
		
        myBooks.setMessage("Added: " + title);
		
		cycle.setPage(myBooks);
	}
	
	private boolean isEmpty(String value)
	{
		if (value == null)
			return true;
		
		return value.trim().length() == 0;
	}	
}