package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
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
 *  Implementation of the VlibOperations stateless session bean.
 *
 *  <p>Implenents a number of stateless operations for the front end.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class VlibOperationsBean implements SessionBean
{
	private SessionContext context;
	private Context environment;
	private IBookHome bookHome;
	private IPersonHome personHome;
		
	public void ejbCreate()
	{
		Context initial;
		Integer blockSizeProperty;
		
		try
		{
			initial = new InitialContext();
			environment = (Context)initial.lookup("java:comp/env");
		}
		catch (NamingException e)
		{
			throw new EJBException("Could not lookup environment: " + e);
		}
		
	}
	
	/**
	 *  Does nothing, not invoked in stateless session beans.
	 */
	 
	public void ejbPassivate()
	{
	}
	
	public void setSessionContext(SessionContext value)
	{
		context = value;
	}
	
	/**
	 *  Does nothing, not invoked in stateless session beans.
	 *
	 */
	 
	public void ejbActivate()
	{
	}
	
	/**
	 *  Does nothing.
	 */
	 
	public void ejbRemove()
	{
		// Does nothing.
	}

	/**
	 *  Finds the book and borrower (by thier primary keys) and updates the book.
	 *
	 *  <p>The borrowed book is returned.
	 *
	 */
	 
	public IBook borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
	throws FinderException, RemoteException
	{
		IBookHome bookHome;
		IPersonHome personHome;
		IPerson borrower;
		IBook book;
		
		bookHome = getBookHome();
		personHome = getPersonHome();
		
		book = bookHome.findByPrimaryKey(bookPrimaryKey);
		borrower = personHome.findByPrimaryKey(borrowerPrimaryKey);
		
		// findByPrimaryKey() throws an exception if the EJB doesn't exist,
		// so we're safe.
		
		book.setHolderPK(borrowerPrimaryKey);
		book.incrementLendCount();
		
		return book;
	}
	
	private IBookHome getBookHome()
	{
		Object raw;
		
		if (bookHome == null)
		{
			try
			{
				raw = environment.lookup("ejb/Book");
				
				bookHome = (IBookHome)PortableRemoteObject.narrow(raw, IBookHome.class);
			}
			catch (NamingException e)
			{
				throw new EJBException("Could not lookup Book home interface: " + e);
			}
		
		}
		
		return bookHome;
	}
	
	private IPersonHome getPersonHome()
	{
		Object raw;
		
		if (personHome == null)
		{
			try
			{
				raw = environment.lookup("ejb/Person");
				
				personHome = (IPersonHome)PortableRemoteObject.narrow(raw, IPersonHome.class);
			}
			catch (NamingException e)
			{
				throw new EJBException("Could not lookup Person home interface: " + e);
			}
		
		}
		
		return personHome;
	}
	
}  