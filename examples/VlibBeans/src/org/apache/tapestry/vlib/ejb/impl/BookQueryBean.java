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