package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import javax.naming.*;
import com.primix.foundation.jdbc.*;
import com.primix.foundation.ejb.*;

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
 *  Implementation of the KeyAllocator stateless session bean.
 *
 *  <p>We're cheating a little; they KeyAllocator does have
 *  state, it just doesn't get persisted ever.  Since the
 *  operation on it is atomic ("gimme a key") it doesn't
 *  need to have conversational state with its clients.
 *
 *  <p>The KeyAllocator records in the database the "next key
 *  to allocate".  When it needs a key, it allocates a block
 *  of keys (by advancing the next key by a some number).
 *
 *  <p>If the KeyAllocator instance is purged from the pool,
 *  then some number of keys that it has allocated will
 *  be lost.  Big deal.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class BookQueryBean extends OperationsBean
{
	
	/**
	 *  Stores the results from the most recent query.
	 *
	 */
	 
	private Book[] results;
		
	/**
	 *  Releases any results.
	 *
	 */
	 
	public void ejbRemove()
	{
		results = null;
	}

		
	// Business methods
	
	public int getResultCount()
	{
		if (results == null)
			return 0;
		
		return results.length;
	}	
	
	public Book[] get(int offset, int length)
	{
		Book[] result;
		
		// Create a new array and copy the requested
		// results into it.
		
		result = new Book[length];
		System.arraycopy(results, offset, result, 0, length);
		
		return result;
	}
	
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
	 *  Queries on books owned by a given person, sorted by title.
	 *
	 */
	 
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
	 *  Queries on books owned by a given person, sorted by title.
	 *
	 */
	 
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


	private void processQuery(IStatement statement)
	{
		ResultSet set = null;
		
		try
		{
			set = statement.executeQuery();
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to execute query.", e);
		}
		
		try
		{
			processQueryResults(set);
		}
		catch (SQLException e)
		{
			throw new XEJBException("Unable to process query results.", e);
		}
		finally
		{
			close(null, null, set);
		}
	}
	
	private void processQueryResults(ResultSet set)
	throws SQLException
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
		results = (Book[])list.toArray(results);	
	}
	
	
	private IStatement buildMasterQuery(Connection connection,
		String title, String author, Object publisherPK)
	throws SQLException
	{
		StatementAssembly assembly;
		int i;
		IStatement result;
		
		assembly = buildBaseBookQuery();
				
		addSubstringSearch(assembly, "book.TITLE", title);
		addSubstringSearch(assembly, "book.AUTHOR", author);
				
		if (publisherPK != null)
		{
			assembly.addSep(" AND ");
			assembly.addParameter("book.PUBLISHER_ID = ?", publisherPK);
		}
		
		assembly.newLine("ORDER BY book.TITLE");

		result = assembly.createStatement(connection);
		
		return result;
	}
	

	
	private IStatement buildPersonQuery(Connection connection,
		String personColumn, Integer personPK)
	throws SQLException
	{
		StatementAssembly assembly;
		int i;
		IStatement result;
		String trimmedTitle;
		
		assembly = buildBaseBookQuery();
		
		assembly.addSep(" AND ");
		assembly.addParameter(personColumn + "= ?", personPK);
	
		assembly.newLine("ORDER BY book.TITLE");

		result = assembly.createStatement(connection);
		
		return result;
	}
	
}  