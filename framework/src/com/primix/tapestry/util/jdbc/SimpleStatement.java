/*
 * Tapestry Web Application Framework
 * Copyright (c) 2000, 2001 by Howard Ship and Primix
 *
 * Primix
 * 311 Arsenal Street
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
 *  A wrapper around {@link Statement} or {@link PreparedStatement}.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

package com.primix.tapestry.util.jdbc;

import java.util.*;
import java.sql.*;

public class SimpleStatement implements IStatement
{
	private String SQL;
	private Statement statement;
	
	public SimpleStatement(String SQL, Connection connection)
	throws SQLException
	{
		this.SQL = SQL;
		this.statement = connection.createStatement();
	}
	
	/**
	 * Returns the SQL associated with this statement.
	 *
	 */
	 
	public String getSQL()
	{
		return SQL;
	}
	
	/**
	 *  Returns the underlying {@link Statement}.
	 *
	 */
	 
	public Statement getStatement()
	{
		return statement;
	}
	
	/**
	 *  Closes the underlying statement, and nulls the reference to it.
	 *
	 */
	 
	public void close()
	throws SQLException
	{
		statement.close();
		
		statement = null;
		SQL = null;
	}

	/**
	 *  Executes the statement as a query, returning a {@link ResultSet}.
	 *
	 */
	 
	public ResultSet executeQuery()
	throws SQLException
	{
		return statement.executeQuery(SQL);
	}
	
	/**
	 *  Executes the statement as an update, returning the number of rows
	 *  affected.
	 *
	 */
	 
	public int executeUpdate()
	throws SQLException
	{
		return statement.executeUpdate(SQL);
	}
	
	public String toString()
	{
		StringBuffer buffer;
		
		buffer = new StringBuffer(super.toString());
		
		buffer.append("[SQL=<\n");
		buffer.append(SQL);
		buffer.append("\n>]");
		
		return buffer.toString();
	}
	
}