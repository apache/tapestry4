package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import java.sql.*;
import com.primix.foundation.jdbc.*;

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
 *  Implementation of the {@link IOperations} stateless session bean.
 *
 *  <p>Implenents a number of stateless operations for the front end.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class OperationsBean implements SessionBean
{
	private SessionContext context;
	private Context environment;
	private transient IBookHome bookHome;
	private transient IPersonHome personHome;
	private transient IPublisherHome publisherHome;
		
	/**
	 *  Data source, retrieved from the ENC property 
	 *  "jdbc/dataSource".
	 *
	 */
	 
	private DataSource dataSource;
	
	public void ejbCreate()
	{
		Context initial;
		
		try
		{
			initial = new InitialContext();
			environment = (Context)initial.lookup("java:comp/env");
		}
		catch (NamingException e)
		{
			throw new EJBException("Could not lookup environment: " + e);
		}
	
		try
		{
			dataSource = (DataSource)environment.lookup("jdbc/dataSource");
		}
		catch (NamingException e)
		{
			throw new EJBException("Could not lookup data source: " + e);
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
	
	
	public IBook addBook(Integer ownerPK, String title, String author, String ISBN, String description,
						 Integer publisherPK)
	throws CreateException, RemoteException
	{
		IBookHome bookHome;
		IPersonHome personHome;
		IPublisherHome publisherHome;
		IBook book;
		
		// First, verify that the person and publisher do exist.
		
		personHome = getPersonHome();
		publisherHome = getPublisherHome();
		bookHome = getBookHome();

		try
		{
			personHome.findByPrimaryKey(ownerPK);
		}
		catch (FinderException e)
		{
			throw new CreateException("Could not create book; owner not found: " + e);
		}
		
		try
		{
			publisherHome.findByPrimaryKey(publisherPK);
		}
		catch (FinderException e)
		{
			throw new CreateException("Could not create book; publisher not found: " + e);
		}

		book = bookHome.create(title, author, ISBN, publisherPK, ownerPK);
		book.setDescription(description);
		
		return book;
	}


	/**
	 *  Adds a book, which will be owned and help by the specified owner.
	 *
	 * <p>The publisherName may either be the name of a known publisher, or
	 * a new name.  A new {@link IPublisher} will be created as necessary.
	 *
	 * <p>Returns the newly created book.
	 *
	 */
	 
	public IBook addBook(Integer ownerPK, String title, String author, String ISBN, 
						 String description, String publisherName)
	throws CreateException, RemoteException
	{
		IBookHome bookHome;
		IPersonHome personHome;
		IPublisherHome publisherHome;
		IBook book;
		IPublisher publisher = null;
		Integer publisherPK;
		
		// First, verify that the person and publisher do exist.
		
		personHome = getPersonHome();
		publisherHome = getPublisherHome();
		bookHome = getBookHome();

		try
		{
			personHome.findByPrimaryKey(ownerPK);
		}
		catch (FinderException e)
		{
			throw new CreateException("Could not create book; owner not found: " + e);
		}
		
		// Find or create the publisher.
		
		try
		{
			publisher = publisherHome.findByName(publisherName);
		}
		catch (FinderException e)
		{
			// Ignore, means that no publisher with the given name already exists.
		}
		
		if (publisher == null)
			publisher = publisherHome.create(publisherName);
			
		publisherPK = (Integer)publisher.getPrimaryKey();
		
		book = bookHome.create(title, author, ISBN, publisherPK, ownerPK);
		book.setDescription(description);
		
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
	
	private IPublisherHome getPublisherHome()
	{
		Object raw;
		
		if (publisherHome == null)
		{
			try
			{
				raw = environment.lookup("ejb/Publisher");
				
				publisherHome = (IPublisherHome)PortableRemoteObject.narrow(raw, IPublisherHome.class);
			}
			catch (NamingException e)
			{
				throw new EJBException("Could not lookup Publisher home interface: " + e);
			}
		
		}
		
		return publisherHome;
	}
	
	public Publisher[] getPublishers()
	{
		Connection connection = null;
		IStatement statement = null;
		ResultSet set = null;
		StatementAssembly assembly;
		Integer primaryKey;
		String name;
		List list;
		Publisher[] result;
		
		try
		{
			connection = getConnection();
			
		
			assembly = new StatementAssembly();
			
			assembly.newLine("SELECT PUBLISHER_ID, NAME");
			assembly.newLine("FROM PUBLISHER");
			assembly.newLine("ORDER BY NAME");
		
			statement = assembly.createStatement(connection);	
			
			set = statement.executeQuery();	
			list = new ArrayList();
			
			while (set.next())
			{
				primaryKey = (Integer)set.getObject(1);
				name = set.getString(2);
				
				list.add(new Publisher(primaryKey, name));
			}
		}
		catch (SQLException e)
		{
			throw new EJBException("Could not fetch all Publishers: " + e.getMessage());
		}
		finally
		{
			close(connection, statement, set);
		}
		
		// Convert from List to Publisher[]
		
		result = new Publisher[list.size()];
		
		return (Publisher[])list.toArray(result);
	}
	
	
	private Connection getConnection()
	{
		try
		{
			return dataSource.getConnection();
		}
		catch (SQLException e)
		{
			throw new EJBException("Unable to get database connection from pool: " + e);
		}
	}
	
	private void close(Connection connection, IStatement statement, ResultSet resultSet)
	{
		if (resultSet != null)
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing result set.");
				e.printStackTrace();
			}
		}
		
		if (statement != null)
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing statement.");
				e.printStackTrace();
			}
		}
		
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing connection.");
				e.printStackTrace();
			}
		}
	}

}  


