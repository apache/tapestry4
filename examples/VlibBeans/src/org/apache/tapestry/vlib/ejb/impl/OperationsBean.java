//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.vlib.ejb.impl;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;
import javax.sql.DataSource;

import org.apache.tapestry.Tapestry;
import org.apache.tapestry.contrib.ejb.XCreateException;
import org.apache.tapestry.contrib.ejb.XEJBException;
import org.apache.tapestry.contrib.ejb.XRemoveException;
import org.apache.tapestry.contrib.jdbc.IStatement;
import org.apache.tapestry.contrib.jdbc.StatementAssembly;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.BorrowException;
import org.apache.tapestry.vlib.ejb.IBook;
import org.apache.tapestry.vlib.ejb.IBookHome;
import org.apache.tapestry.vlib.ejb.IPerson;
import org.apache.tapestry.vlib.ejb.IPersonHome;
import org.apache.tapestry.vlib.ejb.IPublisher;
import org.apache.tapestry.vlib.ejb.IPublisherHome;
import org.apache.tapestry.vlib.ejb.LoginException;
import org.apache.tapestry.vlib.ejb.Person;
import org.apache.tapestry.vlib.ejb.Publisher;
import org.apache.tapestry.vlib.ejb.RegistrationException;
import org.apache.tapestry.vlib.ejb.SortColumn;
import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 *  Implementation of the {@link org.apache.tapestry.vlib.ejb.IOperations} 
 *  stateless session bean.
 *
 *  <p>Implenents a number of stateless operations for the front end.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class OperationsBean implements SessionBean
{
    private SessionContext _context;
    private transient Context _environment;
    private transient IBookHome _bookHome;
    private transient IPersonHome _personHome;
    private transient IPublisherHome _publisherHome;

    /**
     *  Data source, retrieved from the ENC property 
     *  "jdbc/dataSource".
     *
     **/

    private transient DataSource _dataSource;

    /**
     *  Sets up the bean.  Locates the {@link DataSource} for the bean
     *  as <code>jdbc/dataSource</code> within the ENC; this data source is
     *  later used by {@link #getConnection()}.
     *
     **/

    public void ejbCreate()
    {
        Context initial;

        try
        {
            initial = new InitialContext();
            _environment = (Context) initial.lookup("java:comp/env");
        }
        catch (NamingException e)
        {
            throw new XEJBException("Could not lookup environment.", e);
        }

        try
        {
            _dataSource = (DataSource) _environment.lookup("jdbc/dataSource");
        }
        catch (NamingException e)
        {
            e.printStackTrace();
            throw new XEJBException("Could not lookup data source.", e);
        }
    }

    public void ejbRemove()
    {
    }

    /**
     *  Does nothing, not invoked in stateless session beans.
     **/

    public void ejbPassivate()
    {
    }

    public void setSessionContext(SessionContext value)
    {
        _context = value;
    }

    /**
     *  Does nothing, not invoked in stateless session beans.
     *
     **/

    public void ejbActivate()
    {
    }

    /**
     *  Finds the book and borrower (by thier primary keys) and updates the book.
     *
     *  <p>The {@link Book} value object is returned.
     *
     **/

    public Book borrowBook(Integer bookId, Integer borrowerId)
        throws FinderException, RemoteException, BorrowException
    {
        IBookHome bookHome = getBookHome();
        IPersonHome personHome = getPersonHome();

        IBook book = bookHome.findByPrimaryKey(bookId);

        if (!book.getLendable())
            throw new BorrowException("Book may not be borrowed.");

        // Verify that the borrower exists.

        personHome.findByPrimaryKey(borrowerId);

        // TBD: Check that borrower has authenticated

        // findByPrimaryKey() throws an exception if the EJB doesn't exist,
        // so we're safe.

        personHome.findByPrimaryKey(book.getOwnerId());

        // Here's the real work; just setting the holder of the book
        // to be the borrower.

        book.setHolderId(borrowerId);

        return getBook(bookId);
    }

    /**
     *  Adds a new book, verifying that the publisher and holder actually exist.
     *
     **/

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
     **/

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

        attributes.put("publisherId", publisher.getPrimaryKey());

        return addBook(attributes);
    }

    /**
     *  Updates a book.
     *
     *  <p>Returns the updated book.
     *
     *  @param bookId The primary key of the book to update.
     *  
     **/

    public void updateBook(Integer bookId, Map attributes) throws FinderException, RemoteException
    {
        IBookHome bookHome = getBookHome();

        IBook book = bookHome.findByPrimaryKey(bookId);

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
     **/

    public void updateBook(Integer bookId, Map attributes, String publisherName)
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

        attributes.put("publisherId", publisher.getPrimaryKey());

        updateBook(bookId, attributes);
    }

    public void updatePerson(Integer personId, Map attributes)
        throws FinderException, RemoteException
    {
        IPersonHome home = getPersonHome();

        IPerson person = home.findByPrimaryKey(personId);

        person.updateEntityAttributes(attributes);
    }

    public Publisher[] getPublishers()
    {
        Connection connection = null;
        IStatement statement = null;
        ResultSet set = null;

        List list = new ArrayList();

        try
        {
            connection = getConnection();

            StatementAssembly assembly = new StatementAssembly();

            assembly.newLine("SELECT PUBLISHER_ID, NAME");
            assembly.newLine("FROM PUBLISHER");
            assembly.newLine("ORDER BY NAME");

            statement = assembly.createStatement(connection);

            set = statement.executeQuery();

            while (set.next())
            {
                Integer primaryKey = (Integer) set.getObject(1);
                String name = set.getString(2);

                list.add(new Publisher(primaryKey, name));
            }
        }
        catch (SQLException ex)
        {
            ex.printStackTrace();
            throw new XEJBException("Could not fetch all Publishers.", ex);
        }
        finally
        {
            close(connection, statement, set);
        }

        // Convert from List to Publisher[]

        return (Publisher[]) list.toArray(new Publisher[list.size()]);
    }

    /**
     * Fetchs all {@link IPerson} beans in the database and converts them
     * to {@link Person} objects.
     *
     * Returns the {@link Person}s sorted by last name, then first.
     **/

    public Person[] getPersons()
    {
        Connection connection = null;
        IStatement statement = null;
        ResultSet set = null;

        List list = new ArrayList();

        try
        {
            connection = getConnection();

            StatementAssembly assembly = buildBasePersonQuery();
            assembly.newLine("ORDER BY LAST_NAME, FIRST_NAME");

            statement = assembly.createStatement(connection);

            set = statement.executeQuery();

            Object[] columns = new Object[Person.N_COLUMNS];

            while (set.next())
            {
                list.add(convertRowToPerson(set, columns));
            }
        }
        catch (SQLException ex)
        {
            throw new XEJBException("Could not fetch all Persons.", ex);
        }
        finally
        {
            close(connection, statement, set);
        }

        return (Person[]) list.toArray(new Person[list.size()]);
    }

    /**
     *  Gets the {@link Person} for primary key.
     *
     *  @throws FinderException if the Person does not exist.
     **/

    public Person getPerson(Integer personId) throws FinderException
    {
        Connection connection = null;
        IStatement statement = null;
        ResultSet set = null;

        Person result = null;

        try
        {
            connection = getConnection();

            StatementAssembly assembly = buildBasePersonQuery();
            assembly.newLine("WHERE ");
            assembly.add("PERSON_ID = ");
            assembly.addParameter(personId);
            assembly.newLine("ORDER BY LAST_NAME, FIRST_NAME");

            statement = assembly.createStatement(connection);

            set = statement.executeQuery();

            if (!set.next())
                throw new FinderException("Person #" + personId + " does not exist.");

            Object[] columns = new Object[Person.N_COLUMNS];
            result = convertRowToPerson(set, columns);

        }
        catch (SQLException ex)
        {
            throw new XEJBException("Unable to perform database query.", ex);
        }
        finally
        {
            close(connection, statement, set);
        }

        return result;
    }

    public Person login(String email, String password) throws RemoteException, LoginException
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
            throw new LoginException("You have been locked out of the Virtual Library.", false);

        // Set the last access time for any subsequent login.

        person.setLastAccess(new Timestamp(System.currentTimeMillis()));

        return result;
    }

    public Map getPersonAttributes(Integer personId) throws FinderException, RemoteException
    {
        IPersonHome home = getPersonHome();

        IPerson person = home.findByPrimaryKey(personId);

        return person.getEntityAttributes();
    }

    /**
     *  Retrieves a single {@link Book} by its primary key.
     *
     *  @throws FinderException if the Book does not exist.
     *
     **/

    public Book getBook(Integer bookId) throws FinderException
    {
        Connection connection = null;
        IStatement statement = null;
        ResultSet set = null;

        Book result = null;

        try
        {
            connection = getConnection();

            StatementAssembly assembly = buildBaseBookQuery();
            assembly.addSep(" AND ");
            assembly.add("book.BOOK_ID = ");
            assembly.addParameter(bookId);

            statement = assembly.createStatement(connection);

            set = statement.executeQuery();

            if (!set.next())
                throw new FinderException("Book " + bookId + " does not exist.");

            Object[] columns = new Object[Book.N_COLUMNS];
            result = convertRowToBook(set, columns);

        }
        catch (SQLException ex)
        {
            throw new XEJBException("Unable to perform database query.", ex);
        }
        finally
        {
            close(connection, statement, set);
        }

        return result;
    }

    public Map getBookAttributes(Integer bookId) throws FinderException, RemoteException
    {
        IBookHome home = getBookHome();

        IBook book = home.findByPrimaryKey(bookId);

        return book.getEntityAttributes();
    }

    /**
     *  Attempts to register a new user, first checking that the
     *  e-mail and names are unique.  Returns the primary key of the
     *  new {@link IPerson}.
     *
     **/

    public Person registerNewUser(String firstName, String lastName, String email, String password)
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

        Integer personId = (Integer) person.getPrimaryKey();

        try
        {
            return getPerson(personId);
        }
        catch (FinderException ex)
        {
            throw new XCreateException("Unable to find newly created Person.", ex);
        }
    }

    public Book deleteBook(Integer bookId) throws RemoveException, RemoteException
    {
        IBookHome home = getBookHome();
        Book result = null;

        try
        {
            result = getBook(bookId);
        }
        catch (FinderException ex)
        {
            throw new XRemoveException(ex);
        }

        home.remove(bookId);

        return result;

    }

    /**
     *  Transfers a number of books to a new owner.
     *
     **/

    public void transferBooks(Integer newOwnerId, Integer[] bookIds)
        throws FinderException, RemoteException
    {
        if (bookIds == null)
            throw new RemoteException("Must supply non-null list of books to transfer.");

        if (newOwnerId == null)
            throw new RemoteException("Must provide an owner for the books.");

        // Verify that the new owner exists.

        IPersonHome personHome = getPersonHome();
        personHome.findByPrimaryKey(newOwnerId);

        // Direct SQL would be more efficient, but this'll probably do.

        IBookHome home = getBookHome();

        for (int i = 0; i < bookIds.length; i++)
        {
            IBook book = home.findByPrimaryKey(bookIds[i]);

            book.setOwnerId(newOwnerId);
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
                IPublisher publisher = home.findByPrimaryKey(updated[i].getId());
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
        String newPassword,
        Integer[] deleted,
        Integer adminId)
        throws FinderException, RemoveException, RemoteException
    {
        IPersonHome home = getPersonHome();

        int count = Tapestry.size(updated);

        for (int i = 0; i < count; i++)
        {
            Person u = updated[i];
            IPerson person = home.findByPrimaryKey(u.getId());

            person.setAdmin(u.isAdmin());
            person.setLockedOut(u.isLockedOut());
        }

        count = Tapestry.size(resetPassword);

        for (int i = 0; i < count; i++)
        {
            IPerson person = home.findByPrimaryKey(resetPassword[i]);

            person.setPassword(newPassword);
        }

        count = Tapestry.size(deleted);

        if (count > 0)
        {
            returnBooksFromDeletedPersons(deleted);
            moveBooksFromDeletedPersons(deleted, adminId);
        }

        for (int i = 0; i < count; i++)
            home.remove(deleted[i]);
    }

    /**
     *  Invoked to update all books owned by people about to be deleted, to 
     *  reassign the books holder back to the owner.
     * 
     **/

    private void returnBooksFromDeletedPersons(Integer deletedPersonIds[]) throws RemoveException
    {
        StatementAssembly assembly = new StatementAssembly();

        assembly.add("UPDATE BOOK");
        assembly.newLine("SET HOLDER_ID = OWNER_ID");
        assembly.newLine("WHERE HOLDER_ID IN (");
        assembly.addParameterList(deletedPersonIds, ", ");
        assembly.add(")");

        executeUpdate(assembly);
    }

    /**
     *  Invoked to execute a bulk update that moves books to the new admin.
     *
     **/

    private void moveBooksFromDeletedPersons(Integer deletedPersonIds[], Integer adminId)
        throws RemoveException
    {
        StatementAssembly assembly = new StatementAssembly();

        assembly.add("UPDATE BOOK");
        assembly.newLine("SET OWNER_ID = ");
        assembly.addParameter(adminId);
        assembly.newLine("WHERE OWNER_ID IN (");
        assembly.addParameterList(deletedPersonIds, ", ");
        assembly.add(")");

        executeUpdate(assembly);

    }

    private void executeUpdate(StatementAssembly assembly) throws XRemoveException
    {
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
                "Unable to execute " + assembly + ": " + ex.getMessage(),
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
     **/

    protected Book convertRowToBook(ResultSet set, Object[] columns) throws SQLException
    {
        int column = 1;

        columns[Book.ID_COLUMN] = set.getObject(column++);
        columns[Book.TITLE_COLUMN] = set.getString(column++);
        columns[Book.DESCRIPTION_COLUMN] = set.getString(column++);
        columns[Book.ISBN_COLUMN] = set.getString(column++);
        columns[Book.OWNER_ID_COLUMN] = set.getObject(column++);
        columns[Book.OWNER_NAME_COLUMN] =
            buildName(set.getString(column++), set.getString(column++));
        columns[Book.HOLDER_ID_COLUMN] = set.getObject(column++);
        columns[Book.HOLDER_NAME_COLUMN] =
            buildName(set.getString(column++), set.getString(column++));
        columns[Book.PUBLISHER_ID_COLUMN] = set.getObject(column++);
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
     **/

    private static final String[] BOOK_SELECT_COLUMNS =
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

    private static final String[] BOOK_ALIAS_COLUMNS =
        { "BOOK book", "PERSON owner", "PERSON holder", "PUBLISHER publisher" };

    private static final String[] BOOK_JOINS =
        {
            "book.OWNER_ID = owner.PERSON_ID",
            "book.HOLDER_ID = holder.PERSON_ID",
            "book.PUBLISHER_ID = publisher.PUBLISHER_ID" };

    private static final Map BOOK_SORT_ASCENDING = new HashMap();
    private static final Map BOOK_SORT_DESCENDING = new HashMap();

    static {
        BOOK_SORT_ASCENDING.put(SortColumn.TITLE, "book.TITLE");
        BOOK_SORT_ASCENDING.put(SortColumn.HOLDER, "holder.LAST_NAME, holder.FIRST_NAME");
        BOOK_SORT_ASCENDING.put(SortColumn.OWNER, "owner.FIRST_NAME, owner.LAST_NAME");
        BOOK_SORT_ASCENDING.put(SortColumn.PUBLISHER, "publisher.NAME");
        BOOK_SORT_ASCENDING.put(SortColumn.AUTHOR, "book.AUTHOR");

        BOOK_SORT_DESCENDING.put(SortColumn.TITLE, "book.TITLE DESC");
        BOOK_SORT_DESCENDING.put(
            SortColumn.HOLDER,
            "holder.LAST_NAME DESC, holder.FIRST_NAME DESC");
        BOOK_SORT_DESCENDING.put(SortColumn.OWNER, "owner.FIRST_NAME DESC, owner.LAST_NAME DESC");
        BOOK_SORT_DESCENDING.put(SortColumn.PUBLISHER, "publisher.NAME DESC");
        BOOK_SORT_DESCENDING.put(SortColumn.AUTHOR, "book.AUTHOR DESC");
    }

    protected StatementAssembly buildBaseBookQuery()
    {
        StatementAssembly result = new StatementAssembly();

        result.newLine("SELECT ");
        result.addList(BOOK_SELECT_COLUMNS, ", ");

        result.newLine("FROM ");
        result.addList(BOOK_ALIAS_COLUMNS, ", ");

        result.newLine("WHERE ");
        result.addList(BOOK_JOINS, " AND ");

        return result;
    }

    /**
     *  Adds a sort ordering clause to the statement.  If ordering is null,
     *  orders by book title.
     * 
     *  @param assembly to update
     *  @param ordering defines the column to sort on, and the order (ascending or descending)
     *  @since 3.0
     * 
     *
     **/

    protected void addSortOrdering(StatementAssembly assembly, SortOrdering ordering)
    {
        if (ordering == null)
        {
            assembly.newLine("ORDER BY book.TITLE");
            return;
        }

        Map sorts = ordering.isDescending() ? BOOK_SORT_DESCENDING : BOOK_SORT_ASCENDING;

        String term = (String) sorts.get(ordering.getColumn());

        assembly.newLine("ORDER BY ");
        assembly.add(term);
    }

    protected void addSubstringSearch(StatementAssembly assembly, String column, String value)
    {
        if (value == null)
            return;

        String trimmed = value.trim();
        if (trimmed.length() == 0)
            return;

        // Here's the McKoi dependency: LOWER() is a database-specific
        // SQL function.

        assembly.addSep(" AND LOWER(");
        assembly.add(column);
        assembly.add(") LIKE");
        assembly.addParameter("%" + trimmed.toLowerCase() + "%");
    }

    /**
     *  Closes the resultSet (if not null), then the statement (if not null), 
     *  then the Connection (if not null).  Exceptions are written to System.out.
     *
     **/

    protected void close(Connection connection, IStatement statement, ResultSet resultSet)
    {
        if (resultSet != null)
        {
            try
            {
                resultSet.close();
            }
            catch (SQLException ex)
            {
                System.out.println("Exception closing result set.");
                ex.printStackTrace();
            }
        }

        if (statement != null)
        {
            try
            {
                statement.close();
            }
            catch (SQLException ex)
            {
                System.out.println("Exception closing statement.");
                ex.printStackTrace();
            }
        }

        if (connection != null)
        {
            try
            {
                connection.close();
            }
            catch (SQLException ex)
            {
                System.out.println("Exception closing connection.");
                ex.printStackTrace();
            }
        }
    }

    private IPersonHome getPersonHome()
    {
        if (_personHome == null)
        {
            try
            {
                Object raw = _environment.lookup("ejb/Person");

                _personHome = (IPersonHome) PortableRemoteObject.narrow(raw, IPersonHome.class);
            }
            catch (NamingException ex)
            {
                throw new XEJBException("Could not lookup Person home interface.", ex);
            }

        }

        return _personHome;
    }

    private IPublisherHome getPublisherHome()
    {
        if (_publisherHome == null)
        {
            try
            {
                Object raw = _environment.lookup("ejb/Publisher");

                _publisherHome =
                    (IPublisherHome) PortableRemoteObject.narrow(raw, IPublisherHome.class);
            }
            catch (NamingException e)
            {
                throw new XEJBException("Could not lookup Publisher home interface.", e);
            }

        }

        return _publisherHome;
    }

    private IBookHome getBookHome()
    {
        if (_bookHome == null)
        {
            try
            {
                Object raw = _environment.lookup("ejb/Book");

                _bookHome = (IBookHome) PortableRemoteObject.narrow(raw, IBookHome.class);
            }
            catch (NamingException e)
            {
                throw new XEJBException("Could not lookup Book home interface.", e);
            }

        }

        return _bookHome;
    }

    /**
     *  Gets a new connection from the data source.
     *
     **/

    protected Connection getConnection()
    {
        try
        {
            return _dataSource.getConnection();
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
        result.newLine("   LOCKED_OUT, ADMIN, LAST_ACCESS");
        result.newLine("FROM PERSON");

        return result;
    }

    /**
     *  Translates the next row from the result set into a {@link Person}.
     *
     *  <p>This works with queries generated by {@link #buildBasePersonQuery()}.
     *
     **/

    protected Person convertRowToPerson(ResultSet set, Object[] columns) throws SQLException
    {
        int column = 1;

        columns[Person.ID_COLUMN] = set.getObject(column++);
        columns[Person.FIRST_NAME_COLUMN] = set.getString(column++);
        columns[Person.LAST_NAME_COLUMN] = set.getString(column++);
        columns[Person.EMAIL_COLUMN] = set.getString(column++);
        columns[Person.LOCKED_OUT_COLUMN] = getBoolean(set, column++);
        columns[Person.ADMIN_COLUMN] = getBoolean(set, column++);
        columns[Person.LAST_ACCESS_COLUMN] = set.getTimestamp(column++);

        return new Person(columns);
    }

    private Boolean getBoolean(ResultSet set, int index) throws SQLException
    {
        return set.getBoolean(index) ? Boolean.TRUE : Boolean.FALSE;
    }

    private void validateUniquePerson(String firstName, String lastName, String email)
        throws RegistrationException
    {
        Connection connection = null;
        IStatement statement = null;
        ResultSet set = null;

        String trimmedEmail = email.trim().toLowerCase();
        String trimmedLastName = lastName.trim().toLowerCase();
        String trimmedFirstName = firstName.trim().toLowerCase();

        try
        {
            connection = getConnection();

            StatementAssembly assembly = new StatementAssembly();
            assembly.newLine("SELECT PERSON_ID");
            assembly.newLine("FROM PERSON");
            assembly.newLine("WHERE ");

            assembly.add("LOWER(EMAIL) = ");
            assembly.addParameter(trimmedEmail);

            statement = assembly.createStatement(connection);
            set = statement.executeQuery();

            if (set.next())
                throw new RegistrationException("Email address is already in use by another user.");

            close(null, statement, set);

            assembly = new StatementAssembly();
            assembly.newLine("SELECT PERSON_ID");
            assembly.newLine("FROM PERSON");
            assembly.newLine("WHERE ");

            assembly.add("LOWER(FIRST_NAME) = ");
            assembly.addParameter(trimmedFirstName);
            assembly.addSep(" AND ");
            assembly.add("LOWER(LAST_NAME) = ");
            assembly.addParameter(trimmedLastName);

            statement = assembly.createStatement(connection);
            set = statement.executeQuery();

            if (set.next())
                throw new RegistrationException("Name provided is already in use by another user.");

        }
        catch (SQLException e)
        {
            throw new RegistrationException("Could not access database: " + e.getMessage(), e);
        }
        finally
        {
            close(connection, statement, set);
        }
    }

    public Book returnBook(Integer bookId) throws RemoteException, FinderException
    {
        IBookHome bookHome = getBookHome();
        IBook book = bookHome.findByPrimaryKey(bookId);

        Integer ownerPK = book.getOwnerId();

        book.setHolderId(ownerPK);

        return getBook(bookId);
    }

}