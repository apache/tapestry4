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

public class OperationsBean implements SessionBean, IMailMessageConstants
{
	private SessionContext context;
	private transient Context environment;
	private transient IBookHome bookHome;
	private transient IPersonHome personHome;
	private transient IPublisherHome publisherHome;

	private QueueSender mailQueueSender;
	private QueueSession mailQueueSession;

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
			environment = (Context) initial.lookup("java:comp/env");
		}
		catch (NamingException e)
		{
			throw new XEJBException("Could not lookup environment.", e);
		}

		try
		{
			dataSource = (DataSource) environment.lookup("jdbc/dataSource");
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
	 *  <p>The attributes of the borrowed book is returned.
	 *
	 */

	public Book borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
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

		// Here's the real work; just setting the holder of the book
		// to be the borrower.

		book.setHolderPK(borrowerPrimaryKey);

		sendMail(
			owner.getEmail(),
			"Book borrow notification.",
			"Your book, '"
				+ book.getTitle()
				+ "',\n"
				+ "has been borrowed by "
				+ borrower.getNaturalName()
				+ " <"
				+ borrower.getEmail()
				+ ">.\n");

		return getBook(bookPrimaryKey);
	}

	/**
	 *  Adds a new book, verifying that the publisher and holder actually exist.
	 *
	 */

	public Integer addBook(Map attributes) throws CreateException, RemoteException
	{
		IBookHome home = getBookHome();

		attributes.put("dateAdded", new Timestamp(System.currentTimeMillis()));
		
		IBook book = home.create(attributes);

		return (Integer) book.getPrimaryKey();
	}

	/**
	 *  Adds a book, which will be owned and held by the specified owner.
	 *
	 * <p>The publisherName may either be the name of a known publisher, or
	 * a new name.  A new {@link IPublisher} will be created as necessary.
	 *
	 * <p>Returns the newly created book, as a {@link Map} of attributes.
	 *
	 */

	public Integer addBook(Map attributes, String publisherName)
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

	public void updateBook(Integer bookPK, Map attributes)
		throws FinderException, RemoteException
	{
		IBookHome bookHome = getBookHome();

		IBook book = bookHome.findByPrimaryKey(bookPK);

		book.updateEntityAttributes(attributes);
	}

	/**
	 *  Updates a book, adding a new Publisher at the same time.
	 *
	 *
	 *  @param bookPK The primary key of the book to update.
	 *  @param attributes attributes to change
	 *  @param publisherName The name of the new publisher.
	 *  @throws FinderException if the book, holder or publisher can not be located.
	 *  @throws CreateException if the {@link IPublisher} can not be created.
	 */

	public void updateBook(Integer bookPK, Map attributes, String publisherName)
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

		updateBook(bookPK, attributes);
	}

	public void updatePerson(Integer primaryKey, Map attributes)
		throws FinderException, RemoteException
	{
		IPersonHome home = getPersonHome();

		IPerson person = home.findByPrimaryKey(primaryKey);

		person.updateEntityAttributes(attributes);
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
				primaryKey = (Integer) set.getObject(1);
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

		return (Publisher[]) list.toArray(result);
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

		return (Person[]) list.toArray(result);
	}

	/**
	 *  Gets the {@link Person} for primary key.
	 *
	 *  @throws FinderException if the Person does not exist.
	 */

	public Person getPerson(Integer primaryKey) throws FinderException
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

	public Person login(String email, String password)
		throws RemoteException, LoginException
	{
		IPersonHome home = getPersonHome();
		IPerson person = null;
		Person result = null;

		try
		{
			person = home.findByEmail(email);
		}
		catch (FinderException ex)
		{
			throw new LoginException("Unknown e-mail address.", false);
		}

		if (!person.getPassword().equals(password))
			throw new LoginException("Invalid password.", true);

		try
		{
			result = getPerson((Integer) person.getPrimaryKey());
		}
		catch (FinderException ex)
		{
			throw new LoginException("Could not read person.", false);
		}

		if (result.isLockedOut())
			throw new LoginException(
				"You have been locked out of the Virtual Library.",
				false);

		// Set the last access time for any subsequent login.

		person.setLastAccess(new Timestamp(System.currentTimeMillis()));

		return result;
	}

	public Map getPersonAttributes(Integer primaryKey)
		throws FinderException, RemoteException
	{
		IPersonHome home = getPersonHome();

		IPerson person = home.findByPrimaryKey(primaryKey);

		return person.getEntityAttributes();
	}

	/**
	 *  Retrieves a single {@link Book} by its primary key.
	 *
	 *  @throws FinderException if the Book does not exist.
	 *
	 */

	public Book getBook(Integer primaryKey) throws FinderException
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

	public Map getBookAttributes(Integer primaryKey)
		throws FinderException, RemoteException
	{
		IBookHome home = getBookHome();

		IBook book = home.findByPrimaryKey(primaryKey);

		return book.getEntityAttributes();
	}

	/**
	 *  Attempts to register a new user, first checking that the
	 *  e-mail and names are unique.  Returns the primary key of the
	 *  new {@link IPerson}.
	 *
	 */

	public Person registerNewUser(
		String firstName,
		String lastName,
		String email,
		String password)
		throws RegistrationException, CreateException, RemoteException
	{
		IPersonHome home;

		if (password == null || password.trim().length() == 0)
			throw new RegistrationException("Must specify a password.");

		validateUniquePerson(firstName, lastName, email);

		home = getPersonHome();

		Map attributes = new HashMap();

		attributes.put("lastName", lastName.trim());
		attributes.put("firstName", firstName.trim());
		attributes.put("email", email.trim());
		attributes.put("password", password.trim());
		attributes.put("lastAccess", new Timestamp(System.currentTimeMillis()));

		IPerson person = home.create(attributes);

		Integer primaryKey = (Integer) person.getPrimaryKey();

		try
		{
			return getPerson(primaryKey);
		}
		catch (FinderException ex)
		{
			throw new XCreateException("Unable to find newly created Person.", ex);
		}
	}

	public Book deleteBook(Integer bookPrimaryKey)
		throws RemoveException, RemoteException
	{
		IBookHome home = getBookHome();
		Book result = null;

		try
		{
			result = getBook(bookPrimaryKey);
		}
		catch (FinderException ex)
		{
			throw new XRemoveException(ex);
		}

		home.remove(bookPrimaryKey);

		return result;

	}

	/**
	 *  Transfers a number of books to a new owner.
	 *
	 */

	public void transferBooks(Integer newOwnerPrimaryKey, Integer[] books)
		throws FinderException, RemoteException
	{
		if (books == null)
			throw new RemoteException("Must supply non-null list of books to transfer.");

		if (newOwnerPrimaryKey == null)
			throw new RemoteException("Must provide an owner for the books.");

		// Verify that the new owner exists.

		IPersonHome personHome = getPersonHome();
		IPerson newOwner = personHome.findByPrimaryKey(newOwnerPrimaryKey);

		// Direct SQL would be more efficient, but this'll probably do.

		IBookHome home = getBookHome();

		for (int i = 0; i < books.length; i++)
		{
			IBook book = home.findByPrimaryKey(books[i]);

			book.setOwnerPK(newOwnerPrimaryKey);
		}
	}

	public void updatePublishers(Publisher[] updated, Integer[] deleted)
		throws FinderException, RemoveException, RemoteException
	{
		IPublisherHome home = getPublisherHome();

		if (updated != null)
		{
			for (int i = 0; i < updated.length; i++)
			{
				IPublisher publisher = home.findByPrimaryKey(updated[i].getPrimaryKey());
				publisher.setName(updated[i].getName());
			}
		}

		if (deleted != null)
		{
			for (int i = 0; i < deleted.length; i++)
			{
				home.remove(deleted[i]);
			}
		}
	}

	public void updatePersons(
		Person[] updated,
		Integer[] resetPassword,
		Integer[] deleted,
		Integer adminPK)
		throws FinderException, RemoveException, RemoteException
	{
		IPersonHome home = getPersonHome();

		if (updated != null & updated.length > 0)
		{
			for (int i = 0; i < updated.length; i++)
			{
				Person u = updated[i];
				IPerson person = home.findByPrimaryKey(updated[i].getPrimaryKey());

				person.setAdmin(u.isAdmin());
				person.setLockedOut(u.isLockedOut());
				person.setVerified(u.isVerified());
			}
		}

		if (resetPassword != null && resetPassword.length > 0)
		{
			Random r = new Random(System.currentTimeMillis());
			long value = 0;

			for (int i = 0; i < resetPassword.length; i++)
			{
				IPerson person = home.findByPrimaryKey(resetPassword[i]);

				do
				{
					value = ((value << 32) ^ r.nextLong());
					if (value < 0)
						value = -value;
					// Repeat until a magic number equivalent to seven digits
				}
				while (value < 2176782336l);

				String password = Long.toString(value, Character.MAX_RADIX);

				person.setPassword(password);

				sendMail(
					person.getEmail(),
					"Virtual Library password reset.",
					"Your password for the Virtual Library has been reset to '" + password + "'.");
			}
		}

		if (deleted != null && deleted.length > 0)
		{
			moveBooksFromDeletedPersons(deleted, adminPK);

			for (int i = 0; i < deleted.length; i++)
				home.remove(deleted[i]);
		}
	}

	/**
	 *  Invoked to execute a bulk update that moves books to the new admin.
	 *
	 */

	private void moveBooksFromDeletedPersons(Integer deleted[], Integer adminPK)
		throws RemoveException
	{
		StatementAssembly assembly = new StatementAssembly();

		assembly.add("UPDATE BOOK");
		assembly.newLine("SET OWNER_ID = ");
		assembly.add(adminPK);
		assembly.newLine("WHERE OWNER_ID IN (");
		assembly.addList(deleted, ", ");
		assembly.add(")");

		Connection connection = null;
		IStatement statement = null;

		try
		{
			connection = getConnection();

			statement = assembly.createStatement(connection);

			statement.executeUpdate();

			statement.close();
			statement = null;

			connection.close();
			connection = null;
		}
		catch (SQLException ex)
		{
			throw new XRemoveException(
				"Unable to move books from deleted owners: " + ex.getMessage(),
				ex);
		}
		finally
		{
			close(connection, statement, null);
		}

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

		columns[Book.PRIMARY_KEY_COLUMN] = set.getObject(column++);
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
		columns[Book.DATE_ADDED_COLUMN] = set.getTimestamp(column++);

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
			"book.BOOK_ID",
			"book.TITLE",
			"book.DESCRIPTION",
			"book.ISBN",
			"owner.PERSON_ID",
			"owner.FIRST_NAME",
			"owner.LAST_NAME",
			"holder.PERSON_ID",
			"holder.FIRST_NAME",
			"holder.LAST_NAME",
			"publisher.PUBLISHER_ID",
			"publisher.NAME",
			"book.AUTHOR",
			"book.HIDDEN",
			"book.LENDABLE",
			"book.DATE_ADDED" };

	private static final String[] bookAliasColumns =
		{ "BOOK book", "PERSON owner", "PERSON holder", "PUBLISHER publisher" };

	private static final String[] bookJoins =
		{
			"book.OWNER_ID = owner.PERSON_ID",
			"book.HOLDER_ID = holder.PERSON_ID",
			"book.PUBLISHER_ID = publisher.PUBLISHER_ID" };

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

	protected void addSubstringSearch(
		StatementAssembly assembly,
		String column,
		String value)
	{
		String trimmed;

		if (value == null)
			return;

		trimmed = value.trim();
		if (trimmed.length() == 0)
			return;

		// InstantDB is configured to always do case-insentive matching
		// for like.

		assembly.addSep(" AND ");
		assembly.add(column);
		assembly.addParameter(" LIKE ?", "%" + trimmed.toLowerCase() + "%");
	}

	/**
	 *  Closes the resultSet (if not null), then the statement (if not null), 
	 *  then the Connection (if not null).  Exceptions are written to System.out.
	 *
	 */

	protected void close(
		Connection connection,
		IStatement statement,
		ResultSet resultSet)
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

				personHome = (IPersonHome) PortableRemoteObject.narrow(raw, IPersonHome.class);
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

				publisherHome =
					(IPublisherHome) PortableRemoteObject.narrow(raw, IPublisherHome.class);
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

				bookHome = (IBookHome) PortableRemoteObject.narrow(raw, IBookHome.class);
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
		result.newLine("  VERIFIED, LOCKED_OUT, ADMIN, AUTH_CODE, LAST_ACCESS");
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
		columns[Person.AUTHORIZATION_CODE_COLUMN] = set.getString(column++);
		columns[Person.LAST_ACCESS_COLUMN] = set.getTimestamp(column++);

		return new Person(columns);
	}

	private Boolean getBoolean(ResultSet set, int index) throws SQLException
	{
		return set.getBoolean(index) ? Boolean.TRUE : Boolean.FALSE;
	}

	private void validateUniquePerson(
		String firstName,
		String lastName,
		String email)
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
			throw new RegistrationException(
				"Could not access database: " + e.getMessage(),
				e);
		}
		finally
		{
			close(connection, statement, set);
		}
	}

	public Book returnBook(Integer bookPrimaryKey)
		throws RemoteException, FinderException
	{
		IBookHome bookHome;
		IBook book;

		bookHome = getBookHome();
		book = bookHome.findByPrimaryKey(bookPrimaryKey);

		// Return the book ... that is, make its holder its owner.

		Integer borrowerPK = book.getHolderPK();
		Integer ownerPK = book.getOwnerPK();

		book.setHolderPK(ownerPK);

		IPersonHome personHome = getPersonHome();
		IPerson owner = personHome.findByPrimaryKey(ownerPK);
		IPerson borrower = personHome.findByPrimaryKey(borrowerPK);

		sendMail(
			owner.getEmail(),
			"Book return notification.",
			borrower.getNaturalName()
				+ " <"
				+ borrower.getEmail()
				+ "> has returned your book,\n"
				+ "'"
				+ book.getTitle()
				+ "'.\n");

		return getBook(bookPrimaryKey);
	}

	protected QueueSession getMailQueueSession()
		throws NamingException, JMSException
	{
		if (mailQueueSession == null)
		{
			Context context = new InitialContext();

			QueueConnectionFactory factory =
				(QueueConnectionFactory) context.lookup("QueueConnectionFactory");

			QueueConnection connection = factory.createQueueConnection();

			mailQueueSession =
				connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
		}

		return mailQueueSession;
	}

	protected QueueSender getMailQueueSender() throws NamingException, JMSException
	{
		if (mailQueueSender == null)
		{
			Context context = new InitialContext();

			Queue queue = (Queue) context.lookup("queue/Vlib-MailQueue");

			mailQueueSender = getMailQueueSession().createSender(queue);
		}

		return mailQueueSender;
	}

	protected void sendMail(String emailAddress, String subject, String content)
		throws EJBException
	{
		
		// Sending mail is temporarily disabled
		
		if (true)
			return;
			
		try
		{
			QueueSender sender = getMailQueueSender();

			QueueSession session = getMailQueueSession();

			TextMessage queueMessage = session.createTextMessage();
			queueMessage.setStringProperty(EMAIL_ADDRESS, emailAddress);
			queueMessage.setStringProperty(SUBJECT, subject);
			queueMessage.setText(content);

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