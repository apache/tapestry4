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

/**
 *  Shows the details of a book, and allows the user to borrow it.
 *
 * @author Howard Ship
 * @version $Id$
 */


public class ViewBook extends BasePage
implements IExternalPage
{
	private Book book;	
	
	public void detach()
	{
		super.detach();

		book = null;
	}
	
	public Book getBook()
	{
		return book;
	}
	
	public void setup(Integer bookPK, IRequestCycle cycle)
	{
		IOperations bean;
		Home home;
		
        Visit visit = (Visit)getVisit();
		bean = visit.getOperations();
		
		try
		{
			book = bean.getBook(bookPK);
		}
		catch (FinderException e)
		{
			home = (Home)cycle.getPage("Home");
			home.setError("Book not found in database.");
			
			cycle.setPage(home);
			return;
		}
		catch (RemoteException e)
		{
			throw new ApplicationRuntimeException(e);
		}
		
		cycle.setPage(this);
	}
	
	/**
	 *  Only want to show the holder if it doesn't match the owner.
	 *
	 */
	 
	public boolean getShowHolder()
	{
		Integer ownerPK;
		Integer holderPK;
		
		ownerPK = book.getOwnerPrimaryKey();
		holderPK = book.getHolderPrimaryKey();
		
		return !ownerPK.equals(holderPK);
	}
	
	/**
	 *  Enable borrow only if logged in AND not already holding the book.
	 *
	 */
	 
	public boolean getEnableBorrow()
	{
		Integer userPK;
		Integer holderPK;
		
        Visit visit = (Visit)getVisit();

		if (!visit.isUserLoggedIn())
			return false;
		
		userPK = visit.getUserPK();
		holderPK = book.getHolderPrimaryKey();
		
		return !userPK.equals(holderPK);
	}
}