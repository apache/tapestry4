/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

package com.primix.vlib.pages.admin;

import com.primix.tapestry.*;
import com.primix.tapestry.form.*;
import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import com.primix.vlib.*; 
import com.primix.vlib.ejb.*;
	
/**
 *  Class TransferBooks
 *
 *  @author Howard Ship
 *  @version $Id$
 */


public class TransferBooks
	extends AdminPage
{
	private Integer fromUserPK;
	private Integer toUserPK;
	private boolean selectionsOk;
	private IPropertySelectionModel userBookModel;
	private IBookQuery bookQuery;
	
	/**
	 *  {@link List} of Book primary keys ({@link Integer}).
	 *
	 */
	
	private List selectedBooks;
	
	public void detach()
	{
		fromUserPK = null;
		toUserPK = null;
		selectionsOk = false;
		userBookModel = null;
		bookQuery = null;
		
		super.detach();
	}
	
	public void beginResponse(IResponseWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
		super.beginResponse(writer, cycle);
		
		if (selectionsOk)
			getUserBookModel();
	}
	
	public void setFromUserPK(Integer value)
	{
		fromUserPK = value;
		fireObservedChange("fromUserPK", value);
	}
	
	public Integer getFromUserPK()
	{
		return fromUserPK;
	}
	
	public Integer getToUserPK()
	{
		return toUserPK;
	}
	
	public void setToUserPK(Integer value)
	{
		toUserPK = value;
		
		fireObservedChange("toUserPK", value);
	}
	
	public boolean isSelectionsOk()
	{
		return selectionsOk;
	}
	
	public void setSelectionsOk(boolean value)
	{
		selectionsOk = value;
		
		fireObservedChange("selectionsOk", value);
	}
	
	public void setBookQuery(IBookQuery value)
	{
		bookQuery = value;
		
		fireObservedChange("bookQuery", value);
	}
	
	public IBookQuery getBookQuery()
	{
		if (bookQuery == null)
		{
			VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
			
			setBookQuery(vengine.createNewQuery());
		}
		
		return bookQuery;
	}
	
	public IActionListener getSelectFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
				throws RequestCycleException
			{
				processSelectForm();
			}
		};
	}
	
	private void processSelectForm()
	{
		setSelectionsOk(false);
		
		if (fromUserPK.equals(toUserPK))
		{
			setError("Please select two different people.");
			return;
		}
		
		if (getUserBookModel().getOptionCount() == 0)
		{
			setError("Selected user owns no books.");
			return;
		}
		
		// Selections ok, enable bottom half of page.
		
		setSelectionsOk(true);
	}
	
	public IActionListener getXferFormListener()
	{
		return new IActionListener()
		{
			public void actionTriggered(IComponent component, IRequestCycle cycle)
			{
				processXferForm();
			}
		};
	}
	
	private void processXferForm()
	{
		setSelectionsOk(false);
		
		List selectedKeys = getSelectedBooks();
		int count = selectedKeys.size();
		
		if (count == 0)
		{
			setMessage("No books selected.");
			return;
		}
		
		Integer[] keys = (Integer[])selectedKeys.toArray(new Integer[count]);
		
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		
		for (int i = 0; i < 2; i++)
		{
			try
			{
				IOperations operations = vengine.getOperations();
				
				operations.transferBooks(toUserPK, keys);
				
				break;
			}
			catch (FinderException ex)
			{
				throw new ApplicationRuntimeException(ex);
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Unable to transfer ownership of books.", ex, i > 0);
			}
		}
		
		setMessage("Transfered " + count + " books.");
	}
	
	public List getSelectedBooks()
	{
		if (selectedBooks == null)
			selectedBooks = new ArrayList();
		
		return selectedBooks;
	}
	
	public IPropertySelectionModel getUserBookModel()
	{
		if (userBookModel == null)
			userBookModel = buildUserBookModel();
		
		return userBookModel;
	}
	
	private IPropertySelectionModel buildUserBookModel()
	{
		VirtualLibraryEngine vengine = (VirtualLibraryEngine)engine;
		Book[] books = null;
		
		for (int i = 0; i < 2; i++)
		{
			books = null;
			
			try
			{
				IBookQuery query = getBookQuery();
				
				int count = query.ownerQuery(fromUserPK);
				
		        if (count > 0)
					books = query.get(0, count);
				
				break;
			}
			catch (RemoteException ex)
			{
				vengine.rmiFailure("Unable to retrieve books owned by user.", ex, i > 0);
				
				setBookQuery(null);
			}
		}
		
     	EntitySelectionModel model = new EntitySelectionModel();
		
		if (books != null)
			for (int i = 0; i < books.length; i++)
				model.add(books[i].getPrimaryKey(),	books[i].getTitle());
		
		return model;	  
	}
				
}

