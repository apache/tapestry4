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
 * Presents a confirmation page before deleting a book.  If the user
 * selects "yes", the book is deleted; otherwise the user is returned
 * to the {@link MyBooks} page.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ConfirmBookDelete extends BasePage
{
	private String bookTitle;
	private Integer bookPK;
	
	public void detach()
	{
		super.detach();
		
		bookTitle = null;
		bookPK = null;
	}
	
	public String getBookTitle()
	{
		return bookTitle;
	}
	
	public Integer getBookPrimaryKey()
	{
		return bookPK;
	}
	
	/** 
	 * Invoked (by {@link MyBooks}) to select a book to be
	 * deleted.  This method sets the temporary page properties
	 * (bookPrimaryKey and bookTitle) and invoked {@link IRequestCycle#setPage(IPage)}.
	 *
	 */
	 
	public void selectBook(Integer bookPK, IRequestCycle cycle)
	{
		Visit visit = (Visit)getVisit();
		IBookHome home;
		IBook book;
		
		this.bookPK = bookPK;
		
		home = visit.getBookHome();
		
		try
		{
			book = home.findByPrimaryKey(bookPK);
			bookTitle = book.getTitle();
		}
		catch (FinderException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		cycle.setPage(this);
	}
	
	
	/**
	 *  Hooked up to the yes component, this actually deletes the book.
	 *
	 */
	 
	public IDirectListener getDeleteBookListener()
	{
		return new IDirectListener()
		{
			public void directTriggered(IComponent component, String[] context, IRequestCycle cycle)
			{
				Integer bookPK;
				
				bookPK = new Integer(context[0]);
				
				deleteBook(bookPK, cycle);
			}
		};
	}
	
	private void deleteBook(Integer bookPK, IRequestCycle cycle)
	{
		Visit visit = (Visit)getVisit();
		IBookHome home;
		
		home = visit.getBookHome();
		
		try
		{
			home.remove(bookPK);
		}
		catch (RemoveException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		cycle.setPage("MyBooks");		
	}
}