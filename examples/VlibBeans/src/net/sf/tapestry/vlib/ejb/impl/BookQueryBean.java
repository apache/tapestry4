//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.vlib.ejb.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.tapestry.contrib.ejb.XEJBException;
import net.sf.tapestry.contrib.jdbc.IStatement;
import net.sf.tapestry.contrib.jdbc.StatementAssembly;
import net.sf.tapestry.vlib.ejb.Book;

/**
 *  Implementation of a stateful session bean used to query the 
 *  {@link net.sf.tapestry.vlib.ejb.IBook}
 *  entity and cache the results.  It can then download the results, in chunks,
 *  to the client ... this is used to support clients that which to display
 *  the results a page at a time (with random access to the pages of results).
 *
 *  <p>To avoid a lot of duplicate code for things like finding the JDBC {@link
 *  Connection} and querying the IBook entity, we subclass from
 *  {@link OperationsBean}.
 *
 *  @see net.sf.tapestry.vlib.ejb.IBookQuery
 *  @see net.sf.tapestry.vlib.ejb.IBookQueryHome
 *
 *  @version $Id$
 *  @author Howard Lewis Ship
 *
 **/

public class BookQueryBean extends OperationsBean
{

    /**
     *  Stores the results from the most recent query.
     *
     **/

    private Book[] results;

    /**
     *  Releases any results.
     *
     **/

    public void ejbRemove()
    {
        results = null;
    }

    // Business methods

    /**
     *  Returns the number of results from the most recent query.
     *
     **/

    public int getResultCount()
    {
        if (results == null)
            return 0;

        return results.length;
    }

    /**
     *  Gets a subset of the results from the query.
     *
     **/

    public Book[] get(int offset, int length)
    {
        Book[] result;

        if (offset < 0)
            return null;

        int finalLength = Math.min(length, results.length - offset);

        if (finalLength < 0)
            return null;

        // Create a new array and copy the requested
        // results into it.

        result = new Book[finalLength];
        System.arraycopy(results, offset, result, 0, finalLength);

        return result;
    }

    /**
     *  The master query is for querying by some mixture of title, author
     *  and publisher.
     *
     **/

    public int masterQuery(String title, String author, Object publisherPK)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildMasterQuery(connection, title, author, publisherPK);
            }
            catch (SQLException ex)
            {
                throw new XEJBException("Unable to create query statement.", ex);
            }

            processQuery(statement);

        }
        finally
        {
            close(connection, statement, null);
        }

        return getResultCount();
    }

    /**
     *  Queries on books owned by a given person, sorted by title.
     *
     **/

    public int ownerQuery(Integer ownerPK)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildPersonQuery(connection, "owner.PERSON_ID", ownerPK);
            }
            catch (SQLException e)
            {
                throw new XEJBException("Unable to create query statement.", e);
            }

            processQuery(statement);

        }
        finally
        {
            close(connection, statement, null);
        }

        return getResultCount();
    }

    /**
     *  Queries on books held (borrowed) by a given person, sorted by title.
     *
     **/

    public int holderQuery(Integer holderPK)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildPersonQuery(connection, "holder.PERSON_ID", holderPK);
            }
            catch (SQLException ex)
            {
                throw new XEJBException("Unable to create query statement.", ex);
            }

            processQuery(statement);

        }
        finally
        {
            close(connection, statement, null);
        }

        return getResultCount();
    }

    public int borrowerQuery(Integer borrowerPK)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildBorrowerQuery(connection, borrowerPK);
            }
            catch (SQLException ex)
            {
                throw new XEJBException("Unable to create query statement.", ex);
            }

            processQuery(statement);

        }
        finally
        {
            close(connection, statement, null);
        }

        return getResultCount();
    }

    private void processQuery(IStatement statement)
    {
        ResultSet set = null;

        try
        {
            set = statement.executeQuery();
        }
        catch (SQLException ex)
        {
            throw new XEJBException("Unable to execute query.", ex);
        }

        try
        {
            processQueryResults(set);
        }
        catch (SQLException ex)
        {
            throw new XEJBException("Unable to process query results.", ex);
        }
        finally
        {
            close(null, null, set);
        }
    }

    private void processQueryResults(ResultSet set) throws SQLException
    {
        List list;
        Object[] columns;
        Book book;

        list = new ArrayList();
        columns = new Object[Book.N_COLUMNS];

        while (set.next())
        {
            book = convertRowToBook(set, columns);

            list.add(book);
        }

        results = new Book[list.size()];
        results = (Book[]) list.toArray(results);
    }

    private IStatement buildMasterQuery(
        Connection connection,
        String title,
        String author,
        Object publisherPK)
        throws SQLException
    {
        StatementAssembly assembly;

        assembly = buildBaseBookQuery();

        addSubstringSearch(assembly, "book.TITLE", title);
        addSubstringSearch(assembly, "book.AUTHOR", author);

        // Hide books that are not visible to the master query.

        assembly.addSep(" AND ");
        assembly.add("book.HIDDEN = 0");

        if (publisherPK != null)
        {
            assembly.addSep(" AND ");
            assembly.addParameter("book.PUBLISHER_ID = ?", publisherPK);
        }

        assembly.newLine("ORDER BY book.TITLE");

        return assembly.createStatement(connection);
    }

    private IStatement buildPersonQuery(Connection connection, String personColumn, Integer personPK)
        throws SQLException
    {
        StatementAssembly assembly;

        assembly = buildBaseBookQuery();

        assembly.addSep(" AND ");
        assembly.add(personColumn);
        assembly.addParameter(" = ?", personPK);

        assembly.newLine("ORDER BY book.TITLE");

        return assembly.createStatement(connection);
    }

    private IStatement buildBorrowerQuery(Connection connection, Integer borrowerPK)
        throws SQLException
    {
        StatementAssembly assembly;

        assembly = buildBaseBookQuery();

        // Get books held by the borrower but not owned by the borrower.

        assembly.addSep(" AND ");
        assembly.addParameter("book.HOLDER_ID = ?", borrowerPK);
        assembly.addSep(" AND ");
        assembly.add("book.HOLDER_ID <> book.OWNER_ID");

        assembly.newLine("ORDER BY book.TITLE");

        return assembly.createStatement(connection);
    }

}