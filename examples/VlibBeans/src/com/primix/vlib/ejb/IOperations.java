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

package com.primix.vlib.ejb;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import javax.rmi.*;
import javax.naming.*;

/**
 *  Remote interface to the Operations stateless
 *  session bean.  A repository for simple operations such as
 *  adding a new book or borrowing an existing book.
 *  
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public interface IOperations 
	extends EJBObject
{
	/**
	 *  Locates the book and the borrower, then sets the holder of the book
	 *  to the borrower and increments the lend count on the book.
	 *
	 */
	
	public Book borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
		throws BorrowException, FinderException, RemoteException;
	
	/**
	 *  Adds a book which will be owned and held by the specified owner.
	 *
	 *  <p>Returns the primary key of the newly created book.
	 */
	
	public Integer addBook(Map attributes)
		throws CreateException, RemoteException;
	
	
	/**
	 *  Adds a book, which will be owned and help by the specified owner.
	 *
	 * <p>The publisherName may either be the name of a known publisher, or
	 * a new name.  A new {@link IPublisher} will be created as necessary.
	 *
	 * <p>Returns the primary key of the newly created book.
	 *
	 */
	
	public Integer addBook(Map attributes, String publisherName)
		throws CreateException, RemoteException;
	
	
	/**
	 *  Updates a book to an existing publisher.
	 *
	 */
	
	public void updateBook(Integer bookPK, Map attributes)
		throws FinderException, RemoteException;
	
	/**
	 *  Updates a book for a unknown publisher.
	 *
	 */
	
	public void updateBook(Integer bookPK, Map attributes, String publisherName)
		throws CreateException, FinderException, RemoteException;
	
	/**
	 *  Updates a Person.  Returns the attributes of the update person.
	 *
	 */
	
	public void updatePerson(Integer personPK, Map attributes)
		throws FinderException, RemoteException;
	
	/**
	 *  Retrieves the light-weight version of all {@link IPublisher} beans, sorted by name.
	 *
	 */
	
	public Publisher[] getPublishers()
		throws RemoteException;	
	
	/**
	 *  Retrieves the light-weight version of all the {@link IPerson} beans, sorted
	 *  by last name, then by first name.
	 *
	 */
	
	public Person[] getPersons()
		throws RemoteException;	
	
	/**
	 *  Retrieves a single {@link Person} by its primary key.
	 *
	 *  @throws FinderException if the Person does not exist.
	 *
	 */
	
	public Person getPerson(Integer primaryKey)
		throws FinderException, RemoteException;
	
	/**
	 *  Attempts to login the user in.
	 *
	 *  @returns the user
	 *  @throws LoginException if the email address is invalid, the password
	 *  is invalid, or the user may not log in for other reasons.
	 *
	 */
	
	public Person login(String email, String password)
		throws LoginException, RemoteException;
	
	/**
	 *  Retrieves the attributes of a {@link IPerson} as a {@link Map}.
	 *
	 */
	
	public Map getPersonAttributes(Integer primaryKey)
		throws FinderException, RemoteException;
	
	/**
	 *  Retrieves a single {@link Book} by its primary key.  Returns the
	 *  book's attributes as a {@link Map}.
	 *
	 *  @throws FinderException if the Book does not exist.
	 *
	 */
	
	public Book getBook(Integer primaryKey)
		throws FinderException, RemoteException;
	
	/**
	 *  Retrieves the attributes of a {@link IBook} as a {@link Map}.
	 *
	 */
	
	public Map getBookAttributes(Integer primaryKey)
		throws FinderException, RemoteException;
	
	/**
	 *  Attempts to register a new user, first checking that the
	 *  e-mail and names are unique.  Returns the primary key of
	 *  the new {@link IPerson}.
	 *
	 */
	
	public Person registerNewUser(String firstName, String lastName, 
			String email, String password)
		throws RegistrationException, CreateException, RemoteException;
	
    /**
	 *  Returns a book to its owner.
	 *
	 *  @throws FinderException if the book is not known.
	 *
	 */
	
    public Book returnBook(Integer bookPrimaryKey)
		throws RemoteException, FinderException;
	
	/**
	 * Deletes a Book.
	 *
	 * @returns the Book as it was before being deleted.
	 */
	
	public Book deleteBook(Integer bookPrimaryKey)
		throws RemoveException, RemoteException;
	
	/**
	 *  Transfers a number of books to a new owner.
	 *
	 */
	
	public void transferBooks(Integer newOwnerPrimaryKey, Integer[] books)
		throws FinderException, RemoteException;
	
	
	/**
	 *  Updates the list of Publishers in the database.
	 *
	 *
	 * @param updated an array of {@link Publisher} used to update
	 * existing publishers (used to change their names).  May be null or
	 * empty.
	 * @param deleted an array of {@link Integer}, the primary key
	 * of any publisher to be deleted.  No check is made that 
	 * existing books aren't tied to this Publisher.  May be null or
	 * empty.
	 */
	
	public void updatePublishers(Publisher[] updated, Integer[] deleted)
		throws FinderException, RemoveException, RemoteException;

}
