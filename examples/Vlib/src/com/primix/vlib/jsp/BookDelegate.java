package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import java.io.IOException;
import java.rmi.*;
import javax.rmi.*;

/*
 * Tapestry Web Application Framework
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
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 */

/**
 *  Controller for the Book page, used for displaying the attributes
 *  of a particular book.  An instance is stored in the {@link HttpSession},
 *  and persists until the user succesfully logs in.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class BookDelegate extends VlibDelegate
{
	private transient Book book;

	private final static String SESSION_ATTRIBUTE_NAME = "pages.book";

	/**
	 *  Creates the LoginDelegate and stores it as an attribute
	 *  of the {@link HttpSession}.
	 *
	 */
	 

	public BookDelegate(RequestContext context)
	{
		super(context);
		
		context.setSessionAttribute(SESSION_ATTRIBUTE_NAME, this);
	}
	
	/**
	 *  Gets the delegate from the {@link HttpSession}, or creates a new
	 *  one (and stores it in the session).
	 *
	 */
	 
	public static BookDelegate get(RequestContext context)
	{
		BookDelegate result;
		
		result = (BookDelegate)context.getSessionAttribute(SESSION_ATTRIBUTE_NAME);
		if (result == null)
			result = new BookDelegate(context);
		
		return result;	
	}

	public void service(RequestContext context)
	throws IOException, ServletException
	{
		Integer bookPK;
		IOperations operations;
		
		// Get the primary key encoded into the
		// URL and convert to Integer.
		
		bookPK = new Integer(context.getPathInfo(0));
		
		try
		{
			operations = application.getOperations();
			
			book = operations.getBook(bookPK);
			
			forward("/jsp/Book.jsp", "View Book", book.getTitle(), context);
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}
		catch (FinderException e)
		{
			throw new ServletException(e);
		}
		finally
		{
			book = null;
			
			application.cleanup();
		}
	}

	/**
	 *  Return the {@link Book} to be displayed.
	 *
	 */
	 
	public Book getBook()
	{
		return book;
	}
	
}