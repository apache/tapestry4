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

package com.primix.tapestry.util.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Class for creating and executing JDBC statements.
 *
 *  @version $Id$
 *  @author Howard Ship
 *
 */

public class StatementAssembly
{
	private StringBuffer buffer = new StringBuffer();
	private List parameters;

	private int lineLength;
	private int maxLineLength = 80;
	private int indent = 5;

	/**
	 *  Default constructor; uses a maximum line length of 80 and an indent of 5.
	 *
	 */

	public StatementAssembly()
	{
	}

	public StatementAssembly(int maxLineLength, int indent)
	{
		this.maxLineLength = maxLineLength;
		this.indent = indent;
	}

	/**
	 *  Clears the assembly, preparing it for re-use.
	 * 
	 *  @since 1.0.7
	 * 
	 **/

	public void clear()
	{
		buffer.setLength(0);
		lineLength = 0;

		if (parameters != null)
			parameters.clear();
	}

	/**
	 *  Maximum length of a line.
	 *
	 */

	public int getMaxLineLength()
	{
		return maxLineLength;
	}

	/**
	 *  Number of spaces to indent continuation lines by.
	 *
	 */

	public int getIndent()
	{
		return indent;
	}

	/**
	 *  Adds text to the current line, unless that would make the line too long, in
	 *  which case a new line is started (and indented) before adding the text.
	 *
	 *  <p>Text is added as-is, with no concept of quoting.  To add arbitrary strings
	 *  (such as in a where clause), use addParameter().
	 *
	 *
	 */

	public void add(String text)
	{
		int textLength;

		textLength = text.length();

		if (lineLength + textLength > maxLineLength)
		{
			buffer.append('\n');

			for (int i = 0; i < indent; i++)
				buffer.append(' ');

			lineLength = indent;
		}

		buffer.append(text);
		lineLength += textLength;
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void add(int value)
	{
		add(Integer.toString(value));
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void add(short value)
	{
		add(Short.toString(value));
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void add(float value)
	{
		add(Float.toString(value));
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void add(double value)
	{
		add(Double.toString(value));
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void add(long value)
	{
		add(Long.toString(value));
	}

	/**
	 *  Adds an arbitrary object to the SQL by invoking {@link Object#toString()}.
	 *  This is typically used with {@link Integer}, {@link Double}, etc.  Note that
	 *  this will not work well with {@link Boolean} ... invoke {@link #add(boolean)}
	 *  instead.
	 *
	 *  @since 0.2.10
	 */

	public void add(Object value)
	{
		add(value.toString());
	}

	/**
	 * Adds a boolean value as either '0' or '1'.
	 *
	 * @since 0.2.10
	 */

	public void add(boolean value)
	{
		add(value ? "1" : "0");
	}

	/** 
	 *  Adds a seperator (usually a comma and a space) to the current line, regardless
	 *  of line length.  "this is purely aesthetic ... it just looks odd if a seperator
	 *  gets wrapped to a new line by itself.
	 *
	 */

	public void addSep(String text)
	{
		buffer.append(text);
		lineLength += text.length();
	}

	/**
	 *  Starts a new line, without indenting.
	 *
	 */

	public void newLine()
	{
		if (buffer.length() != 0)
			buffer.append('\n');

		lineLength = 0;
	}

	/**
	 * Starts a new line, then adds the given text.
	 *
	 */

	public void newLine(String text)
	{
		if (buffer.length() != 0)
			buffer.append('\n');

		buffer.append(text);

		lineLength = text.length();
	}

	public void addList(String[] items, String seperator)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0)
				addSep(seperator);

			add(items[i]);
		}
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void addList(Object[] items, String seperator)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0)
				addSep(seperator);

			add(items[i].toString());
		}
	}

	/**
	 *  @since 0.2.10
	 *
	 */

	public void addList(int[] items, String seperator)
	{
		for (int i = 0; i < items.length; i++)
		{
			if (i > 0)
				addSep(seperator);

			add(items[i]);
		}
	}

	/**
	 *  Adds a parameter to the statement.  Adds a question mark to the SQL and stores
	 *  the value for later.
	 *
	 */

	public void addParameter(Object value)
	{
		if (parameters == null)
			parameters = new ArrayList();

		parameters.add(value);

		add("?");
	}

	/**
	 *  Adds a parameter with some associated text, which should include the question
	 *  mark used to represent the parameter in the SQL.
	 *
	 */

	public void addParameter(String text, Object value)
	{
		if (parameters == null)
			parameters = new ArrayList();

		parameters.add(value);

		add(text);
	}

	/**
	 *  Creates and returns a {@link IStatement} based on the SQL and parameters
	 *  acquired.
	 *
	 */

	public IStatement createStatement(Connection connection) throws SQLException
	{
		String sql = buffer.toString();

		if (parameters == null || parameters.isEmpty())
			return new SimpleStatement(sql, connection);

		return new ParameterizedStatement(sql, connection, parameters);
	}

	/**
	 * 
	 * Creates a statement that supports JDBC 2.0 ResultSet features.
	 * 
	 *  @since 1.0.7
	 * 
	 **/

	public IStatement createStatement(
		Connection connection,
		int resultSetType,
		int resultSetConcurrency)
		throws SQLException
	{
		String sql = buffer.toString();

		if (parameters == null || parameters.isEmpty())
			return new SimpleStatement(
				sql,
				connection,
				resultSetType,
				resultSetConcurrency);

		return new ParameterizedStatement(
			sql,
			connection,
			parameters,
			resultSetType,
			resultSetConcurrency);

	}

	public String toString()
	{
		StringBuffer description;

		description = new StringBuffer(super.toString());

		description.append("[SQL=\n<");
		description.append(buffer);
		description.append("\n>");

		if (parameters != null)
		{
			int count = parameters.size();
			for (int i = 0; i < count; i++)
			{
				Object parameter = parameters.get(i);

				description.append(" ?");
				description.append(i + 1);
				description.append('=');

				if (parameter == null)
					description.append("null");
				else
				{
					description.append('(');
					description.append(parameter.getClass().getName());
					description.append(") ");
					description.append(parameter);
				}
			}
		}

		description.append(']');

		return description.toString();
	}

}