package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import java.io.IOException;
import java.rmi.*;
import javax.rmi.*;
import java.io.*;

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

/**
 *  Controller for the MyBooks page.  An instance is stored in the {@link HttpSession},
 *  and persists until the user succesfully logs in.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class MyBooksDelegate extends BookQueryDelegate
implements ILoginCallback
{

	private transient Book[] ownedBooks;
	private transient Book[] borrowedBooks;
	private transient String error;
	private transient String message;

	private final static String SESSION_ATTRIBUTE_NAME = "pages.mybooks";

	/**
	 *  Creates the LoginDelegate and stores it as an attribute
	 *  of the {@link HttpSession}.
	 *
	 */


	public MyBooksDelegate(RequestContext context)
	{
		super(context);

		context.setSessionAttribute(SESSION_ATTRIBUTE_NAME, this);
	}

	/**
	 *  Gets the delegate from the {@link HttpSession}, or creates a new
	 *  one (and stores it in the session).
	 *
	 */

	public static MyBooksDelegate get(RequestContext context)
	{
		MyBooksDelegate result;

		result = (MyBooksDelegate)context.getSessionAttribute(SESSION_ATTRIBUTE_NAME);
		if (result == null)
			result = new MyBooksDelegate(context);

		return result;	
	}

	public String getError()
	{
		return error;
	}

	public String getMessage()
	{
		return message;
	}

	public void setError(String value)
	{
		error = value;
	}

	public void setMessage(String value)
	{
		message = value;
	}

	public void service(RequestContext context) throws ServletException, IOException
	{
		if (!application.isUserLoggedIn())
		{
			LoginDelegate login = LoginDelegate.get(context);
			login.performLogin(this, context);
			return;
		}

		String action = context.getPathInfo(0);

		if (action != null &&
			action.equals("return"))
			returnBook(context);

		render(context);			
	}

	private void render(RequestContext context) throws ServletException, IOException
	{
		IBookQuery query;
		int count;
		Integer userPK = application.getUserPK();

		ownedBooks = null;
		borrowedBooks = null;
		
		try
		{	
			query = getOrCreateQuery();

			count = query.ownerQuery(userPK);

			if (count > 0)
				ownedBooks = query.get(0, count)	;

			count = query.borrowerQuery(userPK);
			if (count > 0)
				borrowedBooks = query.get(0, count);

			forward("/jsp/MyBooks.jsp", "My Books", "My Books", context);	
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}
		finally
		{
			ownedBooks = null;
			borrowedBooks = null;
			message = null;
			error = null;
		}
	}


	private void returnBook(RequestContext context)
	throws ServletException
	{
	    IOperations operations = application.getOperations();
		Integer bookPK = new Integer(context.getPathInfo(1));
		
	    try
	    {
	        IBook book = operations.returnBook(bookPK);

	        setMessage("Returned book: " + book.getTitle());
	    }
	    catch (FinderException ex)
	    {
	        setError("Could not return book: " + ex.getMessage());
	        return;
	    }
	    catch (RemoteException ex)
	    {
	        throw new ServletException(ex);
	    }
	}

	public void postLogin(RequestContext context) throws IOException, ServletException
	{
		render(context);
	}

	public Book[] getOwnedBooks()
	{
		return ownedBooks;
	}

	public Book[] getBorrowedBooks()
	{
		return borrowedBooks;
	}

}
