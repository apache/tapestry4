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


public class EditBook extends BasePage
{
	private String error;
	private Integer bookPK;	
	private Map attributes;	
	private String publisherName;
	
	private static final int MAP_SIZE = 11;
	
	public EditBook(IApplication application, ComponentSpecification componentSpecification)
	{
		super(application, componentSpecification);
	}

	public void detachFromApplication()
	{
		super.detachFromApplication();

		error = null;
		attributes = null;
		bookPK = null;	
		publisherName = null;	
	}
		
	public void setError(String value)
	{
		error = value;
	}
	
	public String getError()
	{
		return error;
	}
	
	public Map getAttributes()
	{
		if (attributes == null)
			attributes = new HashMap(MAP_SIZE);
			
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
	
	/**
	 *  Gets the book's primary key as a String.
	 *
	 */
	 
	public String getBookPrimaryKey()
	{
		return bookPK.toString();
	}

	/**
	 *  Updates the book's primary key value (converting from String to Integer).
	 *  This allows a Hidden component in the form to synchronize the book being
	 *  editted ... which fixes the Browser Back Button problem.
	 *
	 */
	 
	public void setBookPrimaryKey(String value)
	{
		bookPK = new Integer(value);
	}
	
	public void beginEdit(Integer bookPK, IRequestCycle cycle)
	{
		IBook book;
		
		this.bookPK = bookPK;
		
		book = getBook();
		
		try
		{
			// Get the attributes as a source for our input fields.
			
			attributes = book.getEntityAttributes();
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		cycle.setPage(this);	
	}
	
	private IBook getBook()
	{
		VirtualLibraryApplication app;
		IBookHome bookHome;
		IBook book;
		
		app = (VirtualLibraryApplication)application;
		bookHome = app.getBookHome();
		
		try
		{
			book = bookHome.findByPrimaryKey(bookPK);
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		return book;
	}
	
	public IActionListener getFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				// Add the book or set an error message.
				
				updateBook(cycle);
			}
		};
	}
	
	private void updateBook(IRequestCycle cycle)
	{
		String title;
		String author;
		String ISBN;
		String description;
		Integer holderPK;
		Integer publisherPK;
		MyBooks page;
		VirtualLibraryApplication app;
		IOperations bean;
		
		title = (String)attributes.get("title");
		author = (String)attributes.get("author");
		ISBN = (String)attributes.get("ISBN");
		description = (String)attributes.get("description");
		
		holderPK = (Integer)attributes.get("holderPK");
		publisherPK = (Integer)attributes.get("publisherPK");
		
		if (isEmpty(title))
		{
			setError("Must include a value for title.");
			return;
		}
		
		if (isEmpty(author))
		{
			setError("Must include a value for author.");
			return;
		}
		
		if (publisherPK == null && isEmpty(publisherName))
		{
			setError("Must provide a publisher name if the publisher option is empty.");
			return;
		}
		
		if (publisherPK != null && !isEmpty(publisherName))
		{
			setError("Must leave the publisher name blank if selecting a publisher from the list.");
			return;
		}
		
		// OK, do the update.
		
		app = (VirtualLibraryApplication)application;
		bean = app.getOperations();
		
		try
		{
			if (publisherPK != null)
				bean.updateBook(bookPK, title, author, ISBN, description, holderPK, publisherPK);
			else
			{
				bean.updateBook(bookPK, title, author, ISBN, description, holderPK, 
					publisherName);
				app.clearCache();
			}		
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(t);
		}
		
		page = (MyBooks)cycle.getPage("MyBooks");
		page.setMessage("Updated book.");
		cycle.setPage(page);
	}
	
	private boolean isEmpty(String value)
	{
		if (value == null)
			return true;
		
		if (value.trim().length() == 0)
			return true;
			
		return false;
	}	
}