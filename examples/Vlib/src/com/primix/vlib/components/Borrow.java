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

package com.primix.vlib.components;

import com.primix.tapestry.components.*;
import com.primix.tapestry.*;
import com.primix.vlib.*;
import com.primix.vlib.pages.*;
import com.primix.vlib.ejb.*;
import java.rmi.*;
import javax.ejb.*;

/**
 *  Implements the Borrow link that appears on many pages.
 *
 * <table border=1>
 * <tr> <th>Parameter</th> <th>Type</th> <th>Read / Write </th> <th>Required</th> <th>Default</th> <th>Description</th>
 * </tr>
 *
 * <tr>
 *      <td>book</td>
 *      <td>{@link Book}</td>
 *      <td>R</td>
 *      <td>yes</td>
 *      <td>&nbsp;</td>
 *      <td>The book to create a link to.
 *      </td>
 *  </tr>
 *
 * </table>
 *
 *  <p>Informal parameters are not allowed.  A body is not allowed.
 *
 *  @author Howard Ship
 *  @version $Id$
 *
 */

public class Borrow
	extends BaseComponent
{
    private IBinding bookBinding;
    private Book book;
	
    public void setBookBinding(IBinding value)
    {
		bookBinding = value;
    }
	
    public IBinding getBookBinding()
    {
		return bookBinding;
    }
	
    /**
	 *  Gets the book to create a link for.  This is cached for the duration of the componen's
	 * {@link #render(IResponseWriter, IRequestCycle)} method.
	 *
	 */
	
    public Book getBook()
    {
		if (book == null)
			book = (Book)bookBinding.getObject("book", Book.class);
		
		if (book == null)
			throw new NullValueForBindingException(bookBinding);
		
		return book;
    }
	
    /**
	 *  Overriden to simply clear the book property after the component finishes rendering.
	 *
	 */
	
    public void render(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
    {
		try
		{
			super.render(writer, cycle);
		}
		finally
		{
			book = null;
		}
    }
	
    public boolean isLinkEnabled()
    {
		Visit visit = (Visit)page.getVisit();
		
		if (!visit.isUserLoggedIn())
			return false;
		
		// If the user is logged in, they can borrow it if they are
		// not already holding it and aren't the owner.
		
		Book book = getBook();
		
		// If the book is not lendable, then disable the link.
		
		if (!book.isLendable())
			return false;
		
		// Otherwise, can only borrow it if not already holding it.
		
		return ! visit.isLoggedInUser(book.getHolderPrimaryKey());
    }
	
	public IDirectListener getBorrowListener()
	{
	    return new IDirectListener()
	    {
			public void directTriggered(IDirect direct, String[] context,
					IRequestCycle cycle)
				throws RequestCycleException
			{
				Integer bookPK;
				
				// The primary key of the book to borrow is encoded in the context.
				bookPK = new Integer(context[0]);
				
				borrowBook(bookPK, cycle);
			}
	    };
	}
	
	private void borrowBook(Integer bookPK, IRequestCycle cycle)
		throws RequestCycleException
	{
		Visit visit = (Visit)page.getVisit();
		Home home = (Home)cycle.getPage("Home");
		VirtualLibraryEngine vengine = visit.getEngine();
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations bean = vengine.getOperations();				
				IBook book = bean.borrowBook(bookPK, visit.getUserPK());
				
				// TBD:  Make borrowBook() return Book, not IBook
				
				home.setMessage("Borrowed: " + book.getTitle());
				
				break;
			}
			catch (BorrowException ex)
			{
				home.setError(ex.getMessage());
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(
					"Unable to find book or user. ", ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Remote exception borrowing book.", ex, i > 0);
			}
		}
		
		cycle.setPage(home);				
	}
	
}
