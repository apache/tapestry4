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
 *  Abstract {@link VlibDelegate} class that maintains a reference
 *  to a {@link IBookQuery}.
 *
 *  @version $Id$
 *  @author Howard Ship
 */
 
public class PersonDelegate extends BookQueryDelegate
{
	private transient Person person;
	private transient Book[] books;
	
	private final static String SESSION_ATTRIBUTE_NAME = "pages.person";

	/**
	 *  Creates the LoginDelegate and stores it as an attribute
	 *  of the {@link HttpSession}.
	 *
	 */
	 
	public PersonDelegate(RequestContext context)
	{
		super(context);
		
		context.setSessionAttribute(SESSION_ATTRIBUTE_NAME, this);
	}
	
	/**
	 *  Gets the delegate from the {@link HttpSession}, or creates a new
	 *  one (and stores it in the session).
	 *
	 */
	 
	public static PersonDelegate get(RequestContext context)
	{
		PersonDelegate result;
		
		result = (PersonDelegate)context.getSessionAttribute(SESSION_ATTRIBUTE_NAME);
		if (result == null)
			result = new PersonDelegate(context);
		
		return result;	
	}

	public void service(RequestContext context)
	throws IOException, ServletException
	{
		Integer personPK;
		IOperations operations;
		IBookQuery query;
		int count;
		
		personPK = new Integer(context.getPathInfo(0));
		
		try
		{
			operations = application.getOperations();
			
			person = operations.getPerson(personPK);
			
			query = getOrCreateQuery();
			
			count = query.ownerQuery(personPK);
			
			if (count == 0)
				books = new Book[0];
			else
				books = query.get(0, count);
				
			forward("/jsp/Person.jsp", "Book Inventory",
			    person.getNaturalName(), context);
			    	
		}
		catch (RemoteException e)
		{
			throw new ServletException(e);
		}
		catch (FinderException e)
		{
			throw new ServletException("Person #" + personPK + " not found.", e);
		}
		finally
		{
			person = null;
			books = null;
			application.cleanup();
		}		
	}

	/**
	 *  Writes the person's email address, inside a &lt;a&gt; link with
	 *  a mailto: URL.
	 *
	 */

	public void writeEmail(HTMLWriter writer)
	{
		String email = person.getEmail();
		boolean compressed;
		
		compressed = writer.compress(true);
		writer.begin("a");
		writer.attribute("href", "mailto:" + email);
		writer.print(email);
		writer.end();
		
		writer.setCompressed(compressed);
	}
	
	public Person getPerson()
	{
		return person;
	}
	
	public Book[] getBooks()
	{
		return books;
	}
}
