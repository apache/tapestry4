package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;
import java.util.Map;

import javax.ejb.CreateException;
import javax.ejb.EJBObject;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;

/**
 *  Remote interface to the Operations stateless
 *  session bean.  A repository for simple operations such as
 *  adding a new book or borrowing an existing book.
 *  
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IOperations extends EJBObject
{
    /**
     *  Locates the book and the borrower, then sets the holder of the book
     *  to the borrower and increments the lend count on the book.
     *
     **/

    public Book borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
        throws BorrowException, FinderException, RemoteException;

    /**
     *  Adds a book which will be owned and held by the specified owner.
     *
     *  <p>Returns the primary key of the newly created book.
     **/

    public Integer addBook(Map attributes) throws CreateException, RemoteException;

    /**
     *  Adds a book, which will be owned and help by the specified owner.
     *
     *  <p>The publisherName may either be the name of a known publisher, or
     *  a new name.  A new {@link IPublisher} will be created as necessary.
     *
     *  <p>Returns the primary key of the newly created book.
     *
     **/

    public Integer addBook(Map attributes, String publisherName)
        throws CreateException, RemoteException;

    /**
     *  Updates a book to an existing publisher.
     *
     **/

    public void updateBook(Integer bookPK, Map attributes) throws FinderException, RemoteException;

    /**
     *  Updates a book for a unknown publisher.
     *
     **/

    public void updateBook(Integer bookPK, Map attributes, String publisherName)
        throws CreateException, FinderException, RemoteException;

    /**
     *  Updates a Person.  Returns the attributes of the update person.
     *
     **/

    public void updatePerson(Integer personPK, Map attributes) throws FinderException, RemoteException;

    /**
     *  Retrieves the light-weight version of all {@link IPublisher} beans, sorted by name.
     *
     **/

    public Publisher[] getPublishers() throws RemoteException;

    /**
     *  Retrieves the light-weight version of all the {@link IPerson} beans, sorted
     *  by last name, then by first name.
     *
     **/

    public Person[] getPersons() throws RemoteException;

    /**
     *  Retrieves a single {@link Person} by its primary key.
     *
     *  @throws FinderException if the Person does not exist.
     *
     **/

    public Person getPerson(Integer primaryKey) throws FinderException, RemoteException;

    /**
     *  Attempts to login the user in.
     *
     *  @return the user
     *  @throws LoginException if the email address is invalid, the password
     *  is invalid, or the user may not log in for other reasons.
     *
     **/

    public Person login(String email, String password) throws LoginException, RemoteException;

    /**
     *  Retrieves the attributes of a {@link IPerson} as a {@link Map}.
     *
     **/

    public Map getPersonAttributes(Integer primaryKey) throws FinderException, RemoteException;

    /**
     *  Retrieves a single {@link Book} by its primary key.  Returns the
     *  book's attributes as a {@link Map}.
     *
     *  @throws FinderException if the Book does not exist.
     *
     **/

    public Book getBook(Integer primaryKey) throws FinderException, RemoteException;

    /**
     *  Retrieves the attributes of a {@link IBook} as a {@link Map}.
     *
     **/

    public Map getBookAttributes(Integer primaryKey) throws FinderException, RemoteException;

    /**
     *  Attempts to register a new user, first checking that the
     *  e-mail and names are unique.  Returns the primary key of
     *  the new {@link IPerson}.
     *
     **/

    public Person registerNewUser(String firstName, String lastName, String email, String password)
        throws RegistrationException, CreateException, RemoteException;

    /**
     *  Returns a book to its owner.
     *
     *  @throws FinderException if the book is not known.
     *
     **/

    public Book returnBook(Integer bookPrimaryKey) throws RemoteException, FinderException;

    /**
     *  Deletes a Book.
     *
     *  @return the Book as it was before being deleted.
     **/

    public Book deleteBook(Integer bookPrimaryKey) throws RemoveException, RemoteException;

    /**
     *  Transfers a number of books to a new owner.
     *
     **/

    public void transferBooks(Integer newOwnerPrimaryKey, Integer[] books)
        throws FinderException, RemoteException;

    /**
     *  Updates the list of Publishers in the database.
     *
     *
     *  @param updated an array of {@link Publisher} used to update
     *  existing publishers (used to change their names).  May be null or
     *  empty.
     *  @param deleted an array of {@link Integer}, the primary key
     *  of any publisher to be deleted.  No check is made that 
     *  existing books aren't tied to this Publisher.  May be null or
     *  empty.
     **/

    public void updatePublishers(Publisher[] updated, Integer[] deleted)
        throws FinderException, RemoveException, RemoteException;

    /**
     *  Updates a list of Persons.  Main functionality is to allow 
     *  an administrator to edit the following attributes of a Person:
     *  <ul>
     *	<li>admin
     *	<li>lockedOut
     *	<li>verified
     *	</ul>
     *
     * <p>Explicitly, names and email addresses may not be changed.
     *
     * <p>In addition, users may be deleted entirely, or may have their password reset.
     *
     *  @param updated a list of persons to update.  May be null or empty.
     *  @param resetPassword  a list of primary keys; corresponding Persons will
     *  have thier password reset.  May be null or empty.
     *  @param deleted a list of persons to delete.  Books owned by any of these persons
     *  are transfered to the administrator.  May be null or empty.
     *  @param adminPk the administrator performing the operation; books may be transferred
     *  to this person.
     *
     **/

    public void updatePersons(
        Person[] updated,
        Integer[] resetPassword,
        Integer[] deleted,
        Integer adminPK)
        throws FinderException, RemoveException, RemoteException;

}