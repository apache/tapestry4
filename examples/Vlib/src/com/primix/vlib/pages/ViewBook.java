/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000-2001 by Howard Lewis Ship
 *
 * Howard Lewis Ship
 * http://sf.net/projects/tapestry
 * mailto:hship@users.sf.net
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
 * but WITHOUT ANY WARRANTY; without even the implied waranty of
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
 *  Shows the details of a book, and allows the user to borrow it.
 *
 * @author Howard Ship
 * @version $Id$
 */

public class ViewBook extends BasePage implements IExternalPage
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

	public void setBook(Book value)
	{
		book = value;

		fireObservedChange("book", value);
	}

	public void setup(Integer bookPK, IRequestCycle cycle)
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine) engine;

		for (int i = 0; i < 2; i++)
		{
			IOperations bean = vengine.getOperations();

			try
			{
				setBook(bean.getBook(bookPK));

				cycle.setPage(this);

				return;
			}
			catch (FinderException ex)
			{
				vengine.presentError("Book not found in database.", cycle);
				return;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure(
					"Remote exception obtaining information for book " + bookPK + ".",
					ex,
					i > 0);
			}
		}

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

}