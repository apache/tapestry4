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

package net.sf.tapestry.util.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 *  A wrapper around {@link PreparedStatement}.
 *
 *  @version $Id$
 *  @author Howard Ship
 */

public class ParameterizedStatement implements IStatement
{
	private String SQL;
	private PreparedStatement statement;
	private Object[] parameters;

	public ParameterizedStatement(
		String SQL,
		Connection connection,
		List parameters)
		throws SQLException
	{
		this.SQL = SQL;

		statement = connection.prepareStatement(SQL);

		setupParameters(parameters);
	}

	/** @since 1.0.7 **/

	public ParameterizedStatement(
		String SQL,
		Connection connection,
		List parameters,
		int resultSetType,
		int resultSetConcurrency)
		throws SQLException
	{
		this.SQL = SQL;

		statement =
			connection.prepareStatement(SQL, resultSetType, resultSetConcurrency);

		setupParameters(parameters);
	}

	private void setupParameters(List list) throws SQLException
	{
		int i;

		parameters = (Object[]) list.toArray();

		for (i = 0; i < parameters.length; i++)
		{
			statement.setObject(i + 1, parameters[i]);
		}
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
	 *  Returns the underlying or {@link PreparedStatement}.
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

	public void close() throws SQLException
	{
		statement.close();

		statement = null;
		SQL = null;
	}

	/**
	 *  Executes the statement as a query, returning a {@link ResultSet}.
	 *
	 */

	public ResultSet executeQuery() throws SQLException
	{
		return statement.executeQuery();
	}

	/**
	 *  Executes the statement as an update, returning the number of rows
	 *  affected.
	 *
	 */

	public int executeUpdate() throws SQLException
	{
		return statement.executeUpdate();
	}

	public String toString()
	{
		StringBuffer buffer;
		int i;
		Object parameter;

		buffer = new StringBuffer(super.toString());

		buffer.append("[SQL=\n<");
		buffer.append(SQL);
		buffer.append("\n>");

		for (i = 0; i < parameters.length; i++)
		{
			parameter = parameters[i];

			buffer.append(" ?");
			buffer.append(i + 1);
			buffer.append('=');

			if (parameter == null)
				buffer.append("null");
			else
			{
				buffer.append('(');
				buffer.append(parameter.getClass().getName());
				buffer.append(") ");
				buffer.append(parameter);
			}
		}

		buffer.append(']');

		return buffer.toString();
	}

}