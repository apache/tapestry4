package net.sf.tapestry.vlib.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

/**
 *  Remote interface for the BookQuery stateless session bean.
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public interface IBookQuery extends EJBObject
{
    /**
     *  Returns the total number of results rows in the query.
     *
     **/

    public int getResultCount() throws RemoteException;

    /**
     *  Returns a selected subset of the results.
     *
     **/

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
     **/

    public int masterQuery(String title, String author, Object publisherPK) throws RemoteException;

    /**
     *  Queries on books owned by a given person, sorted by title.
     *
     **/

    public int ownerQuery(Integer ownerPK) throws RemoteException;

    /**
     *  Queries on books held by a given person, sorted by title.
     *
     **/

    public int holderQuery(Integer holderPK) throws RemoteException;

    /**
     *  Queries the list of books held by the borrower but not owned by the borrower..
     *
     **/

    public int borrowerQuery(Integer borrowerPK) throws RemoteException;
}