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
import com.primix.tapestry.util.prop.*;

/**
 * Presents a confirmation page before deleting a book.  If the user
 * selects "yes", the book is deleted; otherwise the user is returned
 * to the {@link MyLibrary} page.
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
	 * Invoked (by {@link MyLibrary}) to select a book to be
	 * deleted.  This method sets the temporary page properties
	 * (bookPrimaryKey and bookTitle) and invoked {@link IRequestCycle#setPage(IPage)}.
	 *
	 */
	
	public void selectBook(Integer bookPK, IRequestCycle cycle)
	{
		this.bookPK = bookPK;
		
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();
				Book book = operations.getBook(bookPK);
				
				bookTitle = book.getTitle();
				
				break;
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception reading read book #" + bookPK + ".", ex, i > 0);
			}
		}
		
		cycle.setPage(this);
	}
	
	
	/**
	 *  Hooked up to the yes component, this actually deletes the book.
	 *
	 */
	
	public void deleteBook(String[] context, IRequestCycle cycle)
	{
		Integer bookPK = new Integer(context[0]);
		
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		Book book = null;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();
				
				book = operations.deleteBook(bookPK);
				
				break;
			}
			catch (RemoveException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception deleting book #" + bookPK + ".",
					ex, i > 0);
			}
		}
		
		MyLibrary myLibrary = (MyLibrary)cycle.getPage("MyLibrary");
		
		myLibrary.setMessage("Deleted book: " + book.getTitle());
		
		cycle.setPage(myLibrary);		
	}
}
