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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry.contrib.ejb.XEJBException;
import org.apache.tapestry.contrib.jdbc.IStatement;
import org.apache.tapestry.contrib.jdbc.StatementAssembly;
import org.apache.tapestry.vlib.ejb.Book;
import org.apache.tapestry.vlib.ejb.MasterQueryParameters;
import org.apache.tapestry.vlib.ejb.SortOrdering;

/**
 *  Implementation of a stateful session bean used to query the 
 *  {@link org.apache.tapestry.vlib.ejb.IBook}
 *  entity and cache the results.  It can then download the results, in chunks,
 *  to the client ... this is used to support clients that which to display
 *  the results a page at a time (with random access to the pages of results).
 *
 *  <p>To avoid a lot of duplicate code for things like finding the JDBC {@link
 *  Connection} and querying the IBook entity, we subclass from
 *  {@link OperationsBean}.
 *
 *  @see org.apache.tapestry.vlib.ejb.IBookQuery
 *  @see org.apache.tapestry.vlib.ejb.IBookQueryHome
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

    private Book[] _results;

    /**
     *  Releases any results.
     *
     **/

    public void ejbRemove()
    {
        _results = null;
    }

    // Business methods

    /**
     *  Returns the number of results from the most recent query.
     *
     **/

    public int getResultCount()
    {
        if (_results == null)
            return 0;

        return _results.length;
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

        int finalLength = Math.min(length, _results.length - offset);

        if (finalLength < 0)
            return null;

        // Create a new array and copy the requested
        // results into it.

        result = new Book[finalLength];
        System.arraycopy(_results, offset, result, 0, finalLength);

        return result;
    }

    /**
     *  The master query is for querying by some mixture of title, author
     *  and publisher.
     *
     **/

    public int masterQuery(MasterQueryParameters parameters, SortOrdering sortOrdering)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        _results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildMasterQuery(connection, parameters, sortOrdering);
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

    public int ownerQuery(Integer ownerId, SortOrdering sortOrdering)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        _results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildPersonQuery(connection, "owner.PERSON_ID", ownerId, sortOrdering);
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
     *  Queries on books held (borrowed) by a given person, sorted by title.
     *
     **/

    public int holderQuery(Integer holderId, SortOrdering sortOrdering)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        _results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement =
                    buildPersonQuery(connection, "holder.PERSON_ID", holderId, sortOrdering);
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

    public int borrowerQuery(Integer borrowerId, SortOrdering sortOrdering)
    {
        IStatement statement = null;
        Connection connection = null;

        // Forget any current results.

        _results = null;

        try
        {
            connection = getConnection();

            try
            {
                statement = buildBorrowerQuery(connection, borrowerId, sortOrdering);
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
        List list = new ArrayList();
        Object[] columns = new Object[Book.N_COLUMNS];

        while (set.next())
        {
            Book book = convertRowToBook(set, columns);

            list.add(book);
        }

        _results = new Book[list.size()];
        _results = (Book[]) list.toArray(_results);
    }

    private IStatement buildMasterQuery(
        Connection connection,
        MasterQueryParameters parameters,
        SortOrdering ordering)
        throws SQLException
    {
        String title = parameters.getTitle();
        String author = parameters.getAuthor();
        Integer publisherId = parameters.getPublisherId();
        Integer ownerId = parameters.getOwnerId();
	
        StatementAssembly assembly = buildBaseBookQuery();

        addSubstringSearch(assembly, "book.TITLE", title);
        addSubstringSearch(assembly, "book.AUTHOR", author);

        // Hide books that are not visible to the master query.

        assembly.addSep(" AND ");
        assembly.add("book.HIDDEN = 0");

        if (publisherId != null)
        {
            assembly.addSep(" AND ");
            assembly.add("book.PUBLISHER_ID = ");
            assembly.addParameter(publisherId);
        }
        
        if (ownerId != null)
        {
        	assembly.addSep(" AND ");
        	assembly.add("book.OWNER_ID = ");
        	assembly.addParameter(ownerId);
        }

        addSortOrdering(assembly, ordering);

        return assembly.createStatement(connection);
    }

    private IStatement buildPersonQuery(
        Connection connection,
        String personColumn,
        Integer personId,
        SortOrdering sortOrdering)
        throws SQLException
    {
        StatementAssembly assembly = buildBaseBookQuery();

        assembly.addSep(" AND ");
        assembly.add(personColumn);
        assembly.add(" = ");
        assembly.addParameter(personId);

        addSortOrdering(assembly, sortOrdering);

        return assembly.createStatement(connection);
    }

    private IStatement buildBorrowerQuery(
        Connection connection,
        Integer borrowerId,
        SortOrdering sortOrdering)
        throws SQLException
    {
        StatementAssembly assembly = buildBaseBookQuery();

        // Get books held by the borrower but not owned by the borrower.

        assembly.addSep(" AND ");
        assembly.add("book.HOLDER_ID = ");
        assembly.addParameter(borrowerId);
        assembly.addSep(" AND ");
        assembly.add("book.HOLDER_ID <> book.OWNER_ID");

        addSortOrdering(assembly, sortOrdering);

        return assembly.createStatement(connection);
    }

}