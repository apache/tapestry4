package com.primix.vlib.ejb;

import javax.ejb.*;
import java.rmi.*;
import java.util.*;
import javax.sql.*;
import java.sql.*;
import javax.naming.*;
import com.primix.foundation.jdbc.*;

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

public class BookQueryBean implements SessionBean
{
	private SessionContext context;
		
	/**
	 *  Data source, retrieved from the ENC property 
	 *  "jdbc/dataSource".
	 *
	 */
	 
	private DataSource dataSource;
	
	/**
	 *  Stores the results from the most recent query.
	 *
	 */
	 
	private BookQueryResult[] results;
	
	/**
	 *  Activates the bean.  Gets the DataSource from the environment.
	 *
	 */
	 
	public void ejbCreate()
	{
		Context initial;
		Context environment;
		
		try
		{
			initial = new InitialContext();
			environment = (Context)initial.lookup("java:comp/env");
		}
		catch (NamingException e)
		{
			throw new EJBException("Could not lookup environment: " + e);
		}
			
		try
		{
			dataSource = (DataSource)environment.lookup("jdbc/dataSource");
		}
		catch (NamingException e)
		{
			throw new EJBException("Could not lookup data source: " + e);
		}

	}
	
	/**
	 *  Does nothing.
	 */
	 
	public void ejbPassivate()
	{
	}
	
	public void setSessionContext(SessionContext value)
	{
		context = value;
	}
	
	/**
	 *  Does nothing.
	 *
	 */
	 
	public void ejbActivate()
	{
	}
	
	/**
	 *  Releases any results.
	 *
	 */
	 
	public void ejbRemove()
	{
		results = null;
	}

	
	/**
	 *  Gets a database connection from the pool.
	 *
	 *  @throws EJBException if a <code>SQLException</code>
	 *  is thrown.
	 *
	 */
	 
	private Connection getConnection()
	{
		try
		{
			return dataSource.getConnection();
		}
		catch (SQLException e)
		{
			throw new EJBException("Unable to get database connection from pool: " + e);
		}
	}
	
	// Business methods
	
	public int getResultCount()
	{
		if (results == null)
			return 0;
		
		return results.length;
	}	
	
	public BookQueryResult[] get(int offset, int length)
	{
		BookQueryResult[] result;
		
		// Create a new array and copy the requested
		// results into it.
		
		result = new BookQueryResult[length];
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
				throw new EJBException("Unable to create query statement: " + e);
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
				throw new EJBException("Unable to create query statement: " + e);
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
				throw new EJBException("Unable to create query statement: " + e);
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
			throw new EJBException("Unable to execute query: " + e);
		}
		
		try
		{
			processQueryResults(set);
		}
		catch (SQLException e)
		{
			throw new EJBException("Unable to process query results: " + e);
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
		int column;
		
		list = new ArrayList();
		columns = new Object[BookQueryResult.N_COLUMNS];
		
		while (set.next())
		{
			// Translate the selected columns into the columns of
			// the result set (we fold two first name/last name pairs into simple
			// names).
			
			column = 1;
			
			columns[BookQueryResult.PRIMARY_KEY_COLUMN] =
				set.getObject(column++);
			columns[BookQueryResult.TITLE_COLUMN] = set.getString(column++);
			columns[BookQueryResult.DESCRIPTION_COLUMN] = set.getString(column++);
			columns[BookQueryResult.ISBN_COLUMN] = set.getString(column++);
			columns[BookQueryResult.LEND_COUNT_COLUMN] = set.getObject(column++);
			columns[BookQueryResult.OWNER_PK_COLUMN] = set.getObject(column++);
			columns[BookQueryResult.OWNER_NAME_COLUMN] = 
				buildName(set.getString(column++), set.getString(column++));
			columns[BookQueryResult.HOLDER_PK_COLUMN] = set.getObject(column++);
			columns[BookQueryResult.HOLDER_NAME_COLUMN] =
				buildName(set.getString(column++), set.getString(column++));
			columns[BookQueryResult.PUBLISHER_PK_COLUMN] = set.getObject(column++);
			columns[BookQueryResult.PUBLISHER_NAME_COLUMN] = set.getString(column++);
			columns[BookQueryResult.AUTHOR_COLUMN] = set.getString(column++);
			columns[BookQueryResult.RATING_COLUMN] = set.getObject(column++);
			
			list.add(new BookQueryResult(columns));
		}
		
		results = new BookQueryResult[list.size()];
		results = (BookQueryResult[])list.toArray(results);	
	}
	
	private String buildName(String firstName, String lastName)
	{
		if (firstName == null)
			return lastName;
		
		return firstName + " " + lastName;
	}	
	
	/**
	 *  All queries must use this exact set of select columns, so that
	 *  {@link #processQueryResults(ResultSet)} can build
	 *  the correct {@link BookQueryResult} from each row.
	 *
	 */
	 	
	private static final String[] selectColumns =
	{
		"book.BOOK_ID", "book.TITLE", "book.DESCRIPTION", "book.ISBN",
		"book.LEND_COUNT",
		"owner.PERSON_ID", "owner.FIRST_NAME", "owner.LAST_NAME",
		"holder.PERSON_ID", "holder.FIRST_NAME", "holder.LAST_NAME",
		"publisher.PUBLISHER_ID", "publisher.NAME",
		"book.AUTHOR", "book.RATING"
	};
	
	private static final String[] aliasColumns =
	{
		"BOOK book", "PERSON owner", "PERSON holder", "PUBLISHER publisher"
	};
	
	private static final String[] joins =
	{
		"book.OWNER_ID = owner.PERSON_ID",
		"book.HOLDER_ID = holder.PERSON_ID",
		"book.PUBLISHER_ID = publisher.PUBLISHER_ID"
	};
	
	private IStatement buildMasterQuery(Connection connection,
		String title, String author, Object publisherPK)
	throws SQLException
	{
		StatementAssembly assembly;
		int i;
		IStatement result;
		
		assembly = new StatementAssembly();
		
		assembly.newLine("SELECT ");
		assembly.addList(selectColumns, ", ");

		assembly.newLine("FROM ");
		assembly.addList(aliasColumns, ", ");
		
		assembly.newLine("WHERE ");
		assembly.addList(joins, " AND ");
		
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
	
	private void addSubstringSearch(StatementAssembly assembly, String column, String value)
	{
		String trimmed;
		
		if (value == null)
			return;
			
		trimmed = value.trim();
		if (trimmed.length() == 0)
			return;
		
		// The is very Cloudscape dependant
		
		assembly.addSep(" AND ");
		assembly.addParameter(column + ".trim().toLowerCase() LIKE ?",
				 "%" + trimmed.toLowerCase() + "%");	
	}
	
	private IStatement buildPersonQuery(Connection connection,
		String personColumn, Integer personPK)
	throws SQLException
	{
		StatementAssembly assembly;
		int i;
		IStatement result;
		String trimmedTitle;
		
		assembly = new StatementAssembly();
		
		assembly.newLine("SELECT ");
		assembly.addList(selectColumns, ", ");

		assembly.newLine("FROM ");
		assembly.addList(aliasColumns, ", ");
		
		assembly.newLine("WHERE ");
		assembly.addList(joins, " AND ");
		
		assembly.addSep(" AND ");
		assembly.addParameter(personColumn + "= ?", personPK);
	
		assembly.newLine("ORDER BY book.TITLE");

		result = assembly.createStatement(connection);
		
		return result;
	}
	
	private void close(Connection connection, IStatement statement, ResultSet resultSet)
	{
		if (resultSet != null)
		{
			try
			{
				resultSet.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing result set.");
				e.printStackTrace();
			}
		}
		
		if (statement != null)
		{
			try
			{
				statement.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing statement.");
				e.printStackTrace();
			}
		}
		
		if (connection != null)
		{
			try
			{
				connection.close();
			}
			catch (SQLException e)
			{
				System.out.println("Exception closing connection.");
				e.printStackTrace();
			}
		}
	}
}  