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

package com.primix.vlib.ejb.impl;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.naming.*;
import javax.rmi.*;
import javax.sql.*;
import java.sql.*;
import javax.jms.*;
import com.primix.tapestry.util.jdbc.*;
import com.primix.tapestry.util.ejb.*;
import com.primix.vlib.ejb.*;

import java.sql.Connection;

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
	private transient Context environment;
	private transient IBookHome bookHome;
	private transient IPersonHome personHome;
	private transient IPublisherHome publisherHome;
	
	public static final String MAIL_QUEUE_JNDI_NAME = "queue/Vlib-MailQueue";
	
	private static final int MAIL_QUEUE_PRIORITY = 4;
	
	private final static int MAP_SIZE = 7;
	
	/**
	 *  Data source, retrieved from the ENC property 
	 *  "jdbc/dataSource".
	 *
	 */
	
	private transient DataSource dataSource;
	
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
	 *  Closes the mail queue session, if it has been opened.
	 *
	 */
	
	public void ejbRemove()
	{
		try
		{
			// Closing a session closes any
			// producers created with it.
			
			if (mailQueueSession != null)
				mailQueueSession.close();
		}
		catch (Exception ex)
		{
			System.err.println("Failure removing Operations bean: " + ex.getMessage());
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
	 *  Finds the book and borrower (by thier primary keys) and updates the book.
	 *
	 *  <p>The borrowed book is returned.
	 *
	 */
	
	public IBook borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
		throws FinderException, RemoteException, BorrowException
	{
		IBookHome bookHome = getBookHome();
		IPersonHome personHome = getPersonHome();
		
		IBook book = bookHome.findByPrimaryKey(bookPrimaryKey);
		
		if (!book.isLendable())
			throw new BorrowException("Book may not be borrowed.");
		
		// Verify that the borrower exists.
		
		IPerson borrower = personHome.findByPrimaryKey(borrowerPrimaryKey);
		
		// TBD: Check that borrower has authenticated
		
		// findByPrimaryKey() throws an exception if the EJB doesn't exist,
		// so we're safe.
		
		IPerson owner = personHome.findByPrimaryKey(book.getOwnerPK());
		
		String bookTitle = book.getTitle();
		String ownerEmail = owner.getEmail();
		
		sendMail(ownerEmail,
				"Book borrow notification.",
				"'" + bookTitle + 
				"'\n" +
				"has been borrowed by " +
				owner.getNaturalName() + 
				" <" +
					ownerEmail + ">.\n");

		// Here's the real work; just setting the holder of the book
		// to be the borrower.
		
		book.setHolderPK(borrowerPrimaryKey);

		return book;
	}
	
	/**
	 *  Adds a new book, verifying that the publisher and holder actually exist.
	 *
	 */
	
	public IBook addBook(Map attributes)
		throws CreateException, RemoteException
	{
		IBookHome home = getBookHome();
		
		return home.create(attributes);
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
	
	public IBook addBook(Map attributes, String publisherName)
		throws CreateException, RemoteException
	{
		IPublisher publisher = null;
		IPublisherHome publisherHome = getPublisherHome();
		
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
		
		attributes.put("publisherPK", publisher.getPrimaryKey());
		
		return addBook(attributes);
	}
	
	/**
	 *  Updates a book.
	 *
	 *  <p>Returns the updated book.
	 *
	 *  @param bookPK The primary key of the book to update.
	 *  
	 */
	
	public IBook updateBook(Integer bookPK, Map attributes)
		throws FinderException, RemoteException
	{
		IBookHome bookHome = getBookHome();
		
		IBook book = bookHome.findByPrimaryKey(bookPK);
		
		book.updateEntityAttributes(attributes);
		
		return book;
	}
	
	
	/**
	 *  Updates a book, adding a new Publisher at the same time.
	 *
	 *  <p>Returns the updated book.
	 *
	 *  @param bookPK The primary key of the book to update.
	 *  @param attributes attributes to change
	 *  @param publisherName The name of the new publisher.
	 *  @throws FinderException if the book, holder or publisher can not be located.
	 *  @throws CreateException if the {@link IPublisher} can not be created.
	 */
	
	public IBook updateBook(Integer bookPK, Map attributes, String publisherName)
		throws CreateException, FinderException, RemoteException
	{
		IPublisher publisher = null;
		
		IPublisherHome publisherHome = getPublisherHome();
		
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
		
		// Don't duplicate all that other code!
		
		attributes.put("publisherPK", publisher.getPrimaryKey());
		
		return updateBook(bookPK, attributes);		
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
		
		Map attributes = new HashMap();
		
		attributes.put("lastName", lastName.trim());
		attributes.put("firstName", firstName.trim());
		attributes.put("email", email.trim());
		attributes.put("password", password.trim());
		
		return home.create(attributes);
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
		columns[Book.OWNER_PK_COLUMN] = set.getObject(column++);
		columns[Book.OWNER_NAME_COLUMN] = 
			buildName(set.getString(column++), set.getString(column++));
		columns[Book.HOLDER_PK_COLUMN] = set.getObject(column++);
		columns[Book.HOLDER_NAME_COLUMN] =
			buildName(set.getString(column++), set.getString(column++));
		columns[Book.PUBLISHER_PK_COLUMN] = set.getObject(column++);
		columns[Book.PUBLISHER_NAME_COLUMN] = set.getString(column++);
		columns[Book.AUTHOR_COLUMN] = set.getString(column++);
		columns[Book.HIDDEN_COLUMN] = getBoolean(set, column++);
		columns[Book.LENDABLE_COLUMN] = getBoolean(set, column++);
		
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
			"owner.PERSON_ID", "owner.FIRST_NAME", "owner.LAST_NAME",
			"holder.PERSON_ID", "holder.FIRST_NAME", "holder.LAST_NAME",
			"publisher.PUBLISHER_ID", "publisher.NAME",
			"book.AUTHOR", "book.HIDDEN", "book.LENDABLE"
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
		
		// InstantDB is configure to always do case-insentive matching
		// for like.
		
		assembly.addSep(" AND ");
		assembly.add(column);
		assembly.addParameter(" LIKE ?",
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
		
		result.newLine("SELECT PERSON_ID, FIRST_NAME, LAST_NAME, EMAIL, ");
		result.newLine("  VERIFIED, LOCKED_OUT, ADMIN");
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
		columns[Person.VERIFIED_COLUMN] = getBoolean(set, column++);
		columns[Person.LOCKED_OUT_COLUMN] = getBoolean(set, column++);
		columns[Person.ADMIN_COLUMN] = getBoolean(set, column++);
		
		return new Person(columns);
	}	
	
	private Boolean getBoolean(ResultSet set, int index)
		throws SQLException
	{
		int scalar = set.getInt(index);
		
		return scalar == 0 ? Boolean.FALSE : Boolean.TRUE;
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
			
			assembly.addParameter("LOWER  (EMAIL) = ?", trimmedEmail);
			
			statement = assembly.createStatement(connection);
			set = statement.executeQuery();
			
			if (set.next())
				throw new RegistrationException("Email address is already in use by another user.");
			
			close(null, statement, set);
			
			assembly = new StatementAssembly();
			assembly.newLine("SELECT PERSON_ID");
			assembly.newLine("FROM PERSON");
			assembly.newLine("WHERE ");
			
			assembly.addParameter("LOWER (FIRST_NAME) = ?", trimmedFirstName);
			assembly.addSep(" AND ");
			assembly.addParameter("LOWER (LAST_NAME) = ?", trimmedLastName);
			
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
	
    public IBook returnBook(Integer bookPrimaryKey)
		throws RemoteException, FinderException
    {
		IBookHome bookHome;
		IBook book;
		
		bookHome = getBookHome();
		book = bookHome.findByPrimaryKey(bookPrimaryKey);
		
		// Return the book ... that is, make its holder its owner.
		
		book.setHolderPK(book.getOwnerPK());
		
		return book;
    }
	
	private QueueSender mailQueueSender;
	private QueueSession mailQueueSession;
	
	protected QueueSession getMailQueueSession()
		throws NamingException, JMSException
	{
		if (mailQueueSession == null)
		{
			Context context = new InitialContext();
			
			QueueConnectionFactory factory = (QueueConnectionFactory)context.lookup("QueueConnectionFactory");
			
			QueueConnection connection = factory.createQueueConnection();
			
			mailQueueSession = connection.createQueueSession(true, Session.AUTO_ACKNOWLEDGE);
		}
		
		return mailQueueSession;
	}
			
	protected QueueSender getMailQueueSender()
		throws NamingException, JMSException
	{
		if (mailQueueSender == null)
		{
			Context context = new InitialContext();
			
			Queue queue = (Queue)context.lookup("queue/Vlib-MailQueue");
			
			mailQueueSender = getMailQueueSession().createSender(queue);
		}
		
		return mailQueueSender;
	}
	
	protected void sendMail(String emailAddress, String subject, String content)
		throws EJBException
	{
		try
		{
			QueueSender sender = getMailQueueSender();
			
			QueueSession session = getMailQueueSession();
			
			MailMessage message = new MailMessage(emailAddress, subject, content);
			
			ObjectMessage queueMessage = session.createObjectMessage(message);

			System.out.println("Sending message: " + queueMessage + " via " + sender);
			
			sender.send(queueMessage, DeliveryMode.PERSISTENT, MAIL_QUEUE_PRIORITY, 0);
		}
		catch (NamingException ex)
		{
			throw new XEJBException(ex);
		}
		catch (JMSException ex)
		{
			throw new XEJBException(ex);
		}
	}
}  


