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
 *  Edits the properties of at book.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class EditBook 
	extends Protected
{
	private Integer bookPK;	
	private Map attributes;	
	private String publisherName;
	private boolean cancel;
	
	private static final int MAP_SIZE = 11;
	
	public void detach()
	{
		attributes = null;
		bookPK = null;	
		publisherName = null;	
		cancel = false;
		
		super.detach();
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
	
	public boolean getCancel()
	{
		return cancel;
	}
	
	public void setCancel(boolean value)
	{
		cancel = value;
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
	
	/**
	 *  Invoked (from {@link MyBooks}) to begin editting a book.
	 *  Gets the attributes from the {@link IBook} and updates
	 *  the request cycle to render this page,
	 *
	 */
	
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
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		
		IBookHome bookHome = vengine.getBookHome();
		
		try
		{
			return bookHome.findByPrimaryKey(bookPK);
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
	}
	
	/**
	 *  Used to update the book when the form is submitted.
	 *
	 */
	
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
		if (cancel)
		{
			cycle.setPage("MyBooks");
			return;
		}
		
		// Check for an error from a validation field
		
		if (getError() != null)
			return;
		
		Integer publisherPK = (Integer)attributes.get("publisherPK");
		
		if (publisherPK == null && Tapestry.isNull(publisherName))
		{
			setErrorField("inputPublisherName",
					"Must provide a publisher name if the publisher option is empty.");
			return;
		}
		
		if (publisherPK != null && !Tapestry.isNull(publisherName))
		{
			setErrorField("inputPublisherName",
					"Must leave the publisher name blank if selecting a publisher from the list.");
			return;
		}
		
		// OK, do the update.
		
		Visit visit = (Visit)getVisit();
		
		IOperations bean = visit.getEngine().getOperations();
		
		try
		{
			if (publisherPK != null)
				bean.updateBook(bookPK, attributes);
			else
			{
				bean.updateBook(bookPK, attributes, publisherName);
				visit.clearCache();
			}		
		}
		catch (Throwable t)
		{
			throw new ApplicationRuntimeException(t);
		}
		
		
		MyBooks page = (MyBooks)cycle.getPage("MyBooks");
		page.setMessage("Updated book.");
		
		cycle.setPage(page);
	}
	
}
