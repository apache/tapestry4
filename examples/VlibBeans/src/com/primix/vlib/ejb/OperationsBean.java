package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import java.sql.*;
import com.primix.foundation.jdbc.*;
import com.primix.foundation.ejb.*;


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
		
	private final static int MAP_SIZE = 7;
	
	/**
	 *  Data source, retrieved from the ENC property 
	 *  "jdbc/dataSource".
	 *
	 */
	 
	private DataSource dataSource;
	
	/**
	 *  Sets up the bean.  Locates the {@link DataSource} for the bean
	 *  as <code>jdbc/dataSource</code> within the ENC; this data source is
	 *  later used by {@link #getConnection()}.
	 *
	 */
	 
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
			throw new XEJBException("Could not lookup environment.", e);
		}
	
		try
		{
			dataSource = (DataSource)environment.lookup("jdbc/dataSource");
		}
		catch (NamingException e)
		{
            e.printStackTrace();
			throw new XEJBException("Could not lookup data source.", e);
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
	
	/**
	 *  Adds a new book, verifying that the publisher and holder actually exist.
	 *
	 */
	 
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
			throw new XCreateException("Could not create book; owner not found.", e);
		}
		
		try
		{
			publisherHome.findByPrimaryKey(publisherPK);
		}
		catch (FinderException e)
		{
			throw new XCreateException("Could not create book; publisher not found.", e);
		}

		book = bookHome.create(title, author, ISBN, publisherPK, ownerPK);
		book.setDescription(description);
		
		return book;
	}


	/**
	 *  Adds a book, which will be owned and held by the specified owner.
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
			throw new XCreateException("Could not create book; owner not found.", e);
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
	
	/**
	 *  Updates a book.
	 *
	 *  <p>Returns the updated book.
	 *
	 *  @param bookPK The primary key of the book to update.
	 *  @param title The new title, or null to leave the old title unchanged.
	 *  @param author The new author, of null to leave the old author unchanged.
	 *  @param ISBN The new ISBN, or null to leave the old ISBN unchanged.
	 *  @param description The new description, or null to leave the old description
	 *  unchanged.
	 *  @param holderPK The primary key of the new holder ({@link IPerson}), or null
	 *  to leave it unchanged.
	 *  @param publisherPK The primary key of the {@link IPublisher}, or null
	 *  to leave it unchanged.
	 *  @throws FinderException if the book, holder or publisher can't be located.
	 */

	public IBook updateBook(Integer bookPK, String title, String author, String ISBN, 
						 	String description, Integer holderPK, Integer publisherPK)
	throws FinderException, RemoteException
	{
		IBookHome bookHome;
		IPersonHome personHome;
		IPublisherHome publisherHome;
		IBook book;
		Map attributes;
		
		// First, verify that the person and publisher do exist.
		
		personHome = getPersonHome();
		publisherHome = getPublisherHome();
		bookHome = getBookHome();

		
		personHome.findByPrimaryKey(holderPK);
		publisherHome.findByPrimaryKey(publisherPK);
		book = bookHome.findByPrimaryKey(bookPK);
			
		attributes = new HashMap(MAP_SIZE);
		
		if (title != null)
			attributes.put("title", title);
		
		if (author != null)
			attributes.put("author", author);
			
		if (ISBN != null)
			attributes.put("ISBN", ISBN);
			
		if (description != null)
			attributes.put("description", description);
			
		if (holderPK != null)
			attributes.put("holderPK", holderPK);
			
		if (publisherPK != null)
			attributes.put("publisherPK", publisherPK);
			
		book.updateEntityAttributes(attributes);
		
		return book;
	}


	/**
	 *  Updates a book, adding a new Publisher at the same time.
	 *
	 *  <p>Returns the updated book.
	 *
	 *  @param bookPK The primary key of the book to update.
	 *  @param title The new title, or null to leave the old title unchanged.
	 *  @param author The new author, of null to leave the old author unchanged.
	 *  @param ISBN The new ISBN, or null to leave the old ISBN unchanged.
	 *  @param description The new description, or null to leave the old description
	 *  unchanged.
	 *  @param holderPK The primary key of the new holder ({@link IPerson}), or null
	 *  to leave it unchanged.
	 *  @param publisherName The name of the new publisher.
	 *  @throws FinderException if the book, holder or publisher can not be located.
	 *  @throws CreateException if the {@link IPublisher} can not be created.
	 */

	public IBook updateBook(Integer bookPK, String title, String author, String ISBN, 
						 	String description, Integer holderPK, String publisherName)
	throws CreateException, FinderException, RemoteException
	{
		IPublisherHome publisherHome;
		IPublisher publisher = null;
		Integer publisherPK;
		
		publisherHome = getPublisherHome();
		
		try
		{
			publisher = publisherHome.findByName(publisherName);
		}
		catch (FinderException e)
		{
			// Ignore, means we need to create the Publisher
		}
		
		if (publisher == null)
			publisher = publisherHome.create(publisherName);
			
		publisherPK = (Integer)publisher.getPrimaryKey();
		
		// Don't duplicate all that other code!
		
		return updateBook(bookPK, title, author, ISBN, description, holderPK, publisherPK);		
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
            e.printStackTrace();
			throw new XEJBException("Could not fetch all Publishers.", e);
		}
		finally
		{
			close(connection, statement, set);
		}
		
		// Convert from List to Publisher[]
		
		result = new Publisher[list.size()];
		
		return (Publisher[])list.toArray(result);
	}

	/**
	 * Fetchs all {@link IPerson} beans in the database and converts them
	 * to {@link Person} objects.
	 *
	 * Returns the {@link Person}s sorted by last name, then first.
	 */
	 
	public Person[] getPersons()
	{
		Connection connection = null;
		IStatement statement = null;
		ResultSet set = null;
		StatementAssembly assembly;
		Integer primaryKey;
		String name;
		List list;
		Person[] result;
		Object[] columns;
		int column;
		
		try
		{
			connection = getConnection();
			
			assembly = buildBasePersonQuery();
			assembly.newLine("ORDER BY LAST_NAME, FIRST_NAME");
		
			statement = assembly.createStatement(connection);	
			
			set = statement.executeQuery();	
			list = new ArrayList();
			columns = new Object[Person.N_COLUMNS];
			
			while (set.next())
			{
				list.add(convertRowToPerson(set, columns));
			}
		}
		catch (SQLException e)
		{
			throw new XEJBException("Could not fetch all Persons.", e);
		}
		finally
		{
			close(connection, statement, set);
		}
		
		// Convert from List to Person[]
		
		result = new Person[list.size()];
		
		return (Person[])list.toArray(result);
	}
	
	/**
	 *  Gets the {@link Person} for primary key.
	 *
	 *  @throws FinderException if the Person does not exist.
	 */
	 
	public Person getPerson(Integer primaryKey)
	throws FinderException
	{
		Connection connection = null;
		IStatement statement = null;
		ResultSet set = null;
		Object[] columns;
		Person result = null;
		StatementAssembly assembly;
		
		try
		{
			connection = getConnection();
			
			assembly = buildBasePersonQuery();
            assembly.newLine("WHERE ");
			assembly.addParameter("PERSON_ID = ?", primaryKey);
			assembly.newLine("ORDER BY LAST_NAME, FIRST_NAME");
			
			statement = assembly.createStatement(connection);
			
			set = statement.executeQuery();
			
			if (!set.next())
				throw new FinderException("Person " + primaryKey + " does not exist.");
			
			columns = new Object[Person.N_COLUMNS];
			result = convertRowToPerson(set, columns);	
			
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to perform database query.", e);
		}
		finally
		{
			close(connection, statement, set);
		}
		
		return result;
	}
	
	/**
	 *  Retrieves a single {@link Book} by its primary key.
	 *
	 *  @throws FinderException if the Book does not exist.
	 *
	 */
	 
	public Book getBook(Integer primaryKey)
	throws FinderException
	{
		Connection connection = null;
		IStatement statement = null;
		ResultSet set = null;
		Object[] columns;
		Book result = null;
		StatementAssembly assembly;
		
		try
		{
			connection = getConnection();
			
			assembly = buildBaseBookQuery();
			assembly.addSep(" AND ");
			assembly.addParameter("book.BOOK_ID = ?", primaryKey);
			
			statement = assembly.createStatement(connection);
			
			set = statement.executeQuery();
			
			if (!set.next())
				throw new FinderException("Book " + primaryKey + " does not exist.");
			
			columns = new Object[Book.N_COLUMNS];
			result = convertRowToBook(set, columns);	
			
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to perform database query.", e);
		}
		finally
		{
			close(connection, statement, set);
		}
		
		return result;
	}

	/**
	 *  Attempts to register a new user, first checking that the
	 *  e-mail and names are unique.
	 *
	 */
	 
	public IPerson registerNewUser(String firstName, String lastName, 
									String email, String password)
	throws RegistrationException, CreateException, RemoteException
	{
		IPersonHome home;
		
		if (password == null ||
			password.trim().length() == 0)
				throw new RegistrationException("Must specify a password.");
			
		validateUniquePerson(firstName, lastName, email);
		
		home = getPersonHome();
		
		return home.create(lastName.trim(), 
		        firstName.trim(), 
		        email.trim(), 
		        password.trim());
	}


	/**
	 *  Translates the next row from the result set into a {@link Book}.
	 *
	 *  <p>This works with queries generated by {@link #buildBaseBookQuery()}.
	 *
	 */
	 
	protected Book convertRowToBook(ResultSet set, Object[] columns)
	throws SQLException
	{
		int column = 1;
	
		columns[Book.PRIMARY_KEY_COLUMN] =
			set.getObject(column++);
		columns[Book.TITLE_COLUMN] = set.getString(column++);
		columns[Book.DESCRIPTION_COLUMN] = set.getString(column++);
		columns[Book.ISBN_COLUMN] = set.getString(column++);
		columns[Book.LEND_COUNT_COLUMN] = set.getObject(column++);
		columns[Book.OWNER_PK_COLUMN] = set.getObject(column++);
		columns[Book.OWNER_NAME_COLUMN] = 
			buildName(set.getString(column++), set.getString(column++));
		columns[Book.HOLDER_PK_COLUMN] = set.getObject(column++);
		columns[Book.HOLDER_NAME_COLUMN] =
			buildName(set.getString(column++), set.getString(column++));
		columns[Book.PUBLISHER_PK_COLUMN] = set.getObject(column++);
		columns[Book.PUBLISHER_NAME_COLUMN] = set.getString(column++);
		columns[Book.AUTHOR_COLUMN] = set.getString(column++);
		columns[Book.RATING_COLUMN] = set.getObject(column++);
		
		return new Book(columns);
	}

	private String buildName(String firstName, String lastName)
	{
		if (firstName == null)
			return lastName;
		
		return firstName + " " + lastName;
	}	
	
	/**
	 *  All queries must use this exact set of select columns, so that
	 *  {@link #convertRow(ResultSet, Object[])} can build
	 *  the correct {@link Book} from each row.
	 *
	 */
	 	
	private static final String[] bookSelectColumns =
	{
		"book.BOOK_ID", "book.TITLE", "book.DESCRIPTION", "book.ISBN",
		"book.LEND_COUNT",
		"owner.PERSON_ID", "owner.FIRST_NAME", "owner.LAST_NAME",
		"holder.PERSON_ID", "holder.FIRST_NAME", "holder.LAST_NAME",
		"publisher.PUBLISHER_ID", "publisher.NAME",
		"book.AUTHOR", "book.RATING"
	};
	
	private static final String[] bookAliasColumns =
	{
		"BOOK book", "PERSON owner", "PERSON holder", "PUBLISHER publisher"
	};
	
	private static final String[] bookJoins =
	{
		"book.OWNER_ID = owner.PERSON_ID",
		"book.HOLDER_ID = holder.PERSON_ID",
		"book.PUBLISHER_ID = publisher.PUBLISHER_ID"
	};

	protected StatementAssembly buildBaseBookQuery()
	{
		StatementAssembly result;
		
		result = new StatementAssembly();
		
		result.newLine("SELECT ");
		result.addList(bookSelectColumns, ", ");

		result.newLine("FROM ");
		result.addList(bookAliasColumns, ", ");
		
		result.newLine("WHERE ");
		result.addList(bookJoins, " AND ");
		
		return result;
	}
	
	protected void addSubstringSearch(StatementAssembly assembly, String column, String value)
	{
		String trimmed;
		
		if (value == null)
			return;
			
		trimmed = value.trim();
		if (trimmed.length() == 0)
			return;
		
		assembly.addSep(" AND ");
		assembly.addParameter("LCASE(" + column + ") LIKE ?",
				 "%" + trimmed.toLowerCase() + "%");	
	}
	
	/**
	 *  Closes the resultSet (if not null), then the statement (if not null), 
	 *  then the Connection (if not null).  Exceptions are written to System.out.
	 *
	 */
	 
	protected void close(Connection connection, IStatement statement, ResultSet resultSet)
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
				throw new XEJBException("Could not lookup Person home interface.", e);
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
				throw new XEJBException("Could not lookup Publisher home interface.", e);
			}
		
		}
		
		return publisherHome;
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
				throw new XEJBException("Could not lookup Book home interface.", e);
			}
		
		}
		
		return bookHome;
	}
	
	
	/**
	 *  Gets a new connection from the data source.
	 *
	 */
	 
	protected Connection getConnection()
	{
		try
		{
			return dataSource.getConnection();
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to get database connection from pool.", e);
		}
	}
	
	protected StatementAssembly buildBasePersonQuery()
	{
		StatementAssembly result;
		
		result = new StatementAssembly();
		
		result.newLine("SELECT PERSON_ID, FIRST_NAME, LAST_NAME, EMAIL");
		result.newLine("FROM PERSON");
		
		return result;
	}

	/**
	 *  Translates the next row from the result set into a {@link Person}.
	 *
	 *  <p>This works with queries generated by {@link #buildBasePersonQuery()}.
	 *
	 */

	protected Person convertRowToPerson(ResultSet set, Object[] columns)
	throws SQLException
	{
		int column = 1;
	
		columns[Person.PRIMARY_KEY_COLUMN] = set.getObject(column++);
		columns[Person.FIRST_NAME_COLUMN] = set.getString(column++);
		columns[Person.LAST_NAME_COLUMN] = set.getString(column++);
		columns[Person.EMAIL_COLUMN] = set.getString(column++);
		
		return new Person(columns);
	}	
	
	private void validateUniquePerson(String firstName, String lastName, String email)
	throws RegistrationException
	{
		Connection connection = null;
		IStatement statement = null;
		ResultSet set = null;
		StatementAssembly assembly;
		String trimmedEmail;
		String trimmedFirstName;
		String trimmedLastName;
		
		trimmedEmail = email.trim().toLowerCase();
		trimmedLastName = lastName.trim().toLowerCase();
		trimmedFirstName = firstName.trim().toLowerCase();

		try
		{
			connection = getConnection();
			
			assembly = new StatementAssembly();
			assembly.newLine("SELECT PERSON_ID");
			assembly.newLine("FROM PERSON");
			assembly.newLine("WHERE ");
			
			assembly.addParameter("LCASE (EMAIL) = ?", trimmedEmail);
			
			statement = assembly.createStatement(connection);
			set = statement.executeQuery();
			
			if (set.next())
				throw new RegistrationException("Email address is already in use by another user.");
			
			close(null, statement, set);
			
			assembly = new StatementAssembly();
			assembly.newLine("SELECT PERSON_ID");
			assembly.newLine("FROM PERSON");
			assembly.newLine("WHERE ");

			assembly.addParameter("LCASE (FIRST_NAME) = ?", trimmedFirstName);
			assembly.addSep(" AND ");
			assembly.addParameter("LCASE (LAST_NAME) = ?", trimmedLastName);
			
			statement = assembly.createStatement(connection);
			set = statement.executeQuery();
			
			if (set.next())
				throw new RegistrationException("Name provided is already in use by another user.");
			
		}
		catch (SQLException e)
		{
			throw new RegistrationException("Could not access database: " 
			+ e.getMessage(), e);
		}
		finally
		{
			close(connection, statement, set);
		}
	}
}  


