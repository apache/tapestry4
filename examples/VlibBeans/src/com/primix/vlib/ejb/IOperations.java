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
 *  Remote interface to the {@link OperationsBean} stateless
 *  session bean.  A repository for simple operations such as
 *  adding a new book or borrowing an existing book.
 *  
 *  @version $Id$
 *  @author Howard Ship
 *
 */

package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import javax.rmi.*;
import javax.naming.*;

public interface IOperations extends EJBObject
{
	/**
	 *  Locates the book and the borrower, then sets the holder of the book
	 *  to the borrower and increments the lend count on the book.
	 *
	 *  <p>The borrowed book is returned.
	 */
	 
	public IBook borrowBook(Integer bookPrimaryKey, Integer borrowerPrimaryKey)
	throws FinderException, RemoteException;

	/**
	 *  Adds a book which will be owned and held by the specified owner.
	 *
	 *  <p>Returns the newly created book.
	 */

	public IBook addBook(Integer ownerPK, String title, String author, String ISBN, 
						 String description, Integer publisherPK)
	throws CreateException, RemoteException;



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
	throws CreateException, RemoteException;
		
	/**
	 *  Updates a book to an existing publisher.
	 *
	 *  <p>Returns the updated book.
	 */

	public IBook updateBook(Integer bookPK, String title, String author, String ISBN, 
						 	String description, Integer holderPK, Integer publisherPK)
	throws FinderException, RemoteException;

	/**
	 *  Updates a book for a unknown publisher.
	 *
	 *  <p>Returns the updated book.
	 */

	public IBook updateBook(Integer bookPK, String title, String author, String ISBN, 
						 	String description, Integer holderPK, String publisherName)
	throws CreateException, FinderException, RemoteException;


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
	 *  Retrieves a single {@link Book} by its primary key.
	 *
	 *  @throws FinderException if the Book does not exist.
	 *
	 */
	 
	public Book getBook(Integer primaryKey)
	throws FinderException, RemoteException;
	 	
	/**
	 *  Attempts to register a new user, first checking that the
	 *  e-mail and names are unique.
	 *
	 */
	 
	public IPerson registerNewUser(String firstName, String lastName, 
									String email, String password)
	throws RegistrationException, CreateException, RemoteException;

    /**
     *  Returns a book to its owner.
     *
     *  @throws FinderException if the book is not known.
     *
     */

    public IBook returnBook(Integer bookPrimaryKey)
    throws RemoteException, FinderException;
}