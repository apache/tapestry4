/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.vlib.ejb;

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

    public Book borrowBook(Integer bookdId, Integer borrowerId)
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

    public void updateBook(Integer bookId, Map attributes) throws FinderException, RemoteException;

    /**
     *  Updates a book for a unknown publisher.
     *
     **/

    public void updateBook(Integer bookId, Map attributes, String publisherName)
        throws CreateException, FinderException, RemoteException;

    /**
     *  Updates a Person.  Returns the attributes of the update person.
     *
     **/

    public void updatePerson(Integer personId, Map attributes) throws FinderException, RemoteException;

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

    public Person getPerson(Integer personId) throws FinderException, RemoteException;

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

    public Map getPersonAttributes(Integer personId) throws FinderException, RemoteException;

    /**
     *  Retrieves a single {@link Book} by its primary key.  Returns the
     *  book's attributes as a {@link Map}.
     *
     *  @throws FinderException if the Book does not exist.
     *
     **/

    public Book getBook(Integer bookId) throws FinderException, RemoteException;

    /**
     *  Retrieves the attributes of a {@link IBook} as a {@link Map}.
     *
     **/

    public Map getBookAttributes(Integer bookId) throws FinderException, RemoteException;

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

    public Book returnBook(Integer bookId) throws RemoteException, FinderException;

    /**
     *  Deletes a Book.
     *
     *  @return the Book as it was before being deleted.
     **/

    public Book deleteBook(Integer bookId) throws RemoveException, RemoteException;

    /**
     *  Transfers a number of books to a new owner.
     *
     **/

    public void transferBooks(Integer newOwnerId, Integer[] bookIds)
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
     *	</ul>
     *
     * <p>Explicitly, names and email addresses may not be changed.
     *
     * <p>In addition, users may be deleted entirely, or may have their password reset.
     *
     *  @param updated a list of persons to update.  May be null or empty.
     *  @param resetPassword  a list of primary keys; corresponding Persons will
     *  have thier password reset.  May be null or empty.
     *  @param newPassword the password to be set for each user in resetPassword 
     *  @param deleted a list of persons to delete.  Books owned by any of these persons
     *  are transfered to the administrator.  May be null or empty.
     *  @param adminId the administrator performing the operation; books may be transferred
     *  to this person.
     *
     **/

    public void updatePersons(
        Person[] updated,
        Integer[] resetPassword,
        String newPassword,
        Integer[] deleted,
        Integer adminId)
        throws FinderException, RemoveException, RemoteException;

}