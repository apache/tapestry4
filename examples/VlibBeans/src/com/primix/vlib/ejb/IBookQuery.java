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

package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;

/**
 *  Remote interface for the BookQuery stateless session bean.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public interface IBookQuery extends EJBObject
{
	/**
	 *  Returns the total number of results rows in the query.
	 *
	 */

	public int getResultCount() throws RemoteException;

	/**
	 *  Returns a selected subset of the results.
	 *
	 */

	public Book[] get(int offset, int length) throws RemoteException;

	/**
	 *  Performs a query of books with the matching title and (optionally) publisher.
	 *  The results will be sorted by book title.
	 *
	 *  @param title The title to be search for.  Any book with that contains this
	 *  value in its title attribute will be returned.  Use null to not limit the
	 *  search by title.
	 *  @param The author to search for, or null to not limit the search by author.  Any book
	 *  whose author contains this value as a substring will be included.
	 *  @param publisherPK The primary key of a publisher to limit results to, or null
	 *  to select for any publisher.
	 *
	 */

	public int masterQuery(String title, String author, Object publisherPK)
		throws RemoteException;

	/**
	 *  Queries on books owned by a given person, sorted by title.
	 *
	 */

	public int ownerQuery(Integer ownerPK) throws RemoteException;

	/**
	 *  Queries on books held by a given person, sorted by title.
	 *
	 */

	public int holderQuery(Integer holderPK) throws RemoteException;

	/**
	 *  Queries the list of books held by the borrower but not owned by the borrower..
	 *
	 */

	public int borrowerQuery(Integer borrowerPK) throws RemoteException;
}