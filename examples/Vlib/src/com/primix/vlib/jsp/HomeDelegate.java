package com.primix.vlib.jsp;

import com.primix.servlet.*;
import javax.ejb.*;
import com.primix.vlib.ejb.*;
import java.util.*;
import javax.servlet.*;
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
 *  Controller delegate for the home page of the application, invoked
 *  by {@link HomeServlet}.  The HomeDelegate is stored in the {@link HttpSession}.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class HomeDelegate extends BookQueryDelegate
{
	/**
	 *  Name of text field used to enter the title to search for.
	 *
	 */
	 
	public static final String TITLE_NAME = "title";
	
	
	/**
	 *  Name of the text field used to enter the author to search for.
	 *
	 */
	 
	public static final String AUTHOR_NAME = "author";
	
	/**
	 *  Name of the &lt;select&gt; used to enter a publisher name.
	 *
	 */
	 
	public static final String PUBLISHER_NAME = "publisher";
	
	private static final String SESSION_ATTRIBUTE_NAME = "page.home";

	/**
	 *  The primary key of the most recently selected publisher in the pop up.
	 *
	 */
	 
	private Integer publisherPK;

	/**
	 *  Holds the full set of matches from the most recent query (until
	 *  the end of rendering).
	 *
	 */
	 
	private transient Book[] matches;

	public HomeDelegate(RequestContext context)
	{
		super(context);
		
		context.setSessionAttribute(SESSION_ATTRIBUTE_NAME, this);
	}
	
	/**
	 *  Gets the delegate from the {@link HttpSession}, or creates a new
	 *  one (and stores it in the session).
	 *
	 */
	 
	public static HomeDelegate get(RequestContext context)
	{
		HomeDelegate result;
		
		result = (HomeDelegate)context.getSessionAttribute(SESSION_ATTRIBUTE_NAME);
		if (result == null)
			result = new HomeDelegate(context);
		
		return result;	
	}
	
	public void service(RequestContext context)
	throws ServletException, IOException
	{
		String action;
		
		action = context.getPathInfo(0);
		if (action == null)
			action = "";
			
		try
		{
			if (action.equals("search"))
			{
				search(context);
				return;
			}
			
            if (action.equals("borrow"))
                borrow(context);

			// Default .. .show the Home page.
			
			display(context);
			
		}
		finally
		{
			application.cleanup();
		}
	}

	/**
	 *  Invoked by the HomeDelegate or other delegates to cause the
	 *  home page to be displayed.  Often, {@link #setMessage(String)} or
	 *  {@link #setError(String)} is invoked first.
	 *
	 */
	 
	public void display(RequestContext context)
	throws IOException, ServletException
	{
		forward("/jsp/Home.jsp", "Primix Virtual Library", null, context);
	}

	private void search(RequestContext context)
	throws ServletException, IOException
	{
		String author;
		String title;
		String publisher;
		IBookQuery query;
		int count;
		
		// First, extract the request parameters.
		
		author = context.getParameter(AUTHOR_NAME);
		title = context.getParameter(TITLE_NAME);
		
		publisher = context.getParameter(PUBLISHER_NAME);
		if (publisher.equals(""))
			publisherPK = null;
		else
			publisherPK = new Integer(publisher);
		
		query = getOrCreateQuery();	
		
		try
		{
			count = query.masterQuery(title, author, publisherPK);
			
			if (count == 0)
				matches = new Book[0];
			else	
				matches = query.get(0, count);
			
			// Render the page
			
			forward("/jsp/Matches.jsp", "Matching Books", null, context);
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}
		finally
		{
			matches = null;
            application.cleanup();
		}
		
	}

    private void borrow(RequestContext context)
    throws ServletException, IOException
    {
    	IOperations bean;
    	IBook book;
        Integer bookPK;
        Integer userPK;

        userPK = application.getUserPK() ;
        if (userPK == null)
        {
            context.getRequest().setAttribute("error",
                "You may only borrow books once you are logged in.");
            return;
        }

        bookPK = new Integer(context.getPathInfo(1));

    	bean = application.getOperations();				

    	try
    	{
    		book = bean.borrowBook(bookPK, application.getUserPK());

    		context.getRequest().setAttribute("message", "Borrowed: " + book.getTitle());
    	}
    	catch (FinderException ex)
    	{
    		throw new ServletException(
    			"Unable to find book or user. ", ex);
    	}
    	catch (RemoteException ex)
    	{
    		throw new ServletException(ex);
    	}
    }


	public Book[] getMatches()
	{
		return matches;
	}


	/**
	 *  Invoked by the JSP to write the &lt;select&gt; and &lt;option&gt;
	 *  tags.
	 */
	 
	public void writePublisherSelect(HTMLWriter writer)
	throws ServletException
	{
		IOperations operations;
		Publisher[] publishers;
		int i;
		Integer primaryKey;
		
		operations = application.getOperations();
		
		try
		{
			publishers = operations.getPublishers();
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}
		
		writer.begin("select");
		writer.attribute("name", PUBLISHER_NAME);
		
		writer.beginOrphan("option");
		
		if (publisherPK == null)
			writer.attribute("selected");
		
		for (i = 0; i < publishers.length; i++)
		{
			primaryKey = publishers[i].getPrimaryKey();
			
			writer.beginOrphan("option");
			writer.attribute("value", primaryKey.toString());
			
			// If it matches the most recently selected publisher PK,
			// then mark it as selected.
			
			if (primaryKey.equals(publisherPK))
				writer.attribute("selected");
			
			writer.print(publishers[i].getName());
		}
		
		writer.end();
		
	}

	
}
