package com.primix.tapestry.components;

import com.primix.tapestry.spec.ComponentSpecification;
import com.primix.tapestry.*;
import java.sql.*;
import java.util.*;

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
 *  A wrapper around a JDBC query. Acts something like a {@link Foreach}
 *  in that it executes the query then iterates through the results, creating a
 *  {@link Map} for each row that is fetched.
 *
 * <p>The {@link Map} stores each non-null value under one or
 *  two keys.  The first key is a <code>java.lang.Integer</code> for the column number, and
 * is always present.  The second key is a {@link String} retrieved from
 * the <code>ResultSetMetaData</code>.  In some cases, a column has no name, so the column
 * number key acts as a fallback.
 *
 * <p>Once the row is loaded into a page (or other component) property, it can be
 * accessed by name:
 * <pre>
 *  &lt;component&gt;
 *	  &lt;id&gt;insertTitle&lt;/id&gt;
 *	  &lt;type&gt;insert&lt;/type&gt;
 *
 *	  &lt;binding&gt;
 *		&lt;name&gt;value&lt;/name&gt;
 *		&lt;property-path&gt;currentRow.Title&lt;/property-path&gt;
 *	  &lt;/binding&gt;
 * &lt;/component&gt;
 * </pre>
 *
 *  <p>Use of stateful actions within or after a <code>DatabaseQuery</code> should be
 *  avoided, since it will require the query to be re-run to perform the page rewind.
 *  Immediate actions can be used to avoid this.  Alternately, the the query can be performed
 *  by the containing page and the results stored as persistent state.
 *
 *
 * <table border=1>
 * <tr> 
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Read / Write</th>
 *    <th>Required</th> 
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *		<td>connection</td>
 *		<td>java.sql.Connection</td>
 *		<td>R</td>
 *		<td>No</td>
 *		<td>&nbsp;</td>
 *		<td>The JDBC Connection to execute the query on.  If not specified, then the
 *     <code>URL</code> property is used to forge a temporary connection. </td> </tr>
 *
 * <tr>
 *		<td>URL</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>No</td>
 *		<td>&nbsp;</td>
 *		<td>A JDBC URL used to open a connection, if the <code>connection</code> property
 *  is not specified.</td> </tr>
 *
 * <tr>
 *		<td>query</td>
 *		<td>java.lang.String</td>
 *		<td>R</td>
 *		<td>Yes</td>
 *		<td>&nbsp;</td>
 *		<td>A SQL query to execute.</td> </tr>
 *
 * <tr>
 *		<td>row</td>
 *		<td>java.util.Map</td>
 *		<td>W</td>
 *		<td>no</td>
 *		<td>&nbsp;</td>
 *		<td>A property to update with each row from the query.  Alternately,
 *  components wrapped by the DatabaseQuery can access its {@link #getRow() row property}.</td> </tr>
 * </table>
 *
 * <p>Informal parameters are not allowed.
 *
 * <p>Does <code>DatabaseQuery</code> violate the spirit of seperating the Model, the View
 * and the Controller?  I would generally put database access into the Model (business logic)
 * or perhaps the Controller (harder to justify), here it's edging its way into the View.
 * This is the kind of feature that isn't fundamentally necessary, but would be
 * noticable in its absense when compared to PHP or ColdFusion.
 * 
 *  @author Howard Ship
 *  @version $Id$
 */


public class DatabaseQuery extends AbstractComponent
{
	private IBinding connectionBinding;
	private IBinding URLBinding;
	private IBinding queryBinding;
	private String queryValue;
	private IBinding rowBinding;
	private Map row;
	private boolean rendering;

	public DatabaseQuery(IPage page, IComponent container, String id, 
		ComponentSpecification specification)
	{
		super(page, container, id, specification);
	}

	public IBinding getConnectionBinding()
	{
		return connectionBinding;
	}

	public IBinding getQueryBinding()
	{
		return queryBinding;
	}

	public IBinding getRowBinding()
	{
		return rowBinding;
	}

	public IBinding getURLBinding()
	{
		return URLBinding;
	}

	public void render(IResponseWriter writer, IRequestCycle cycle) throws RequestCycleException
	{
		Connection jdbcConnection = null;
		boolean closeConnection = false;
		String jdbcURL;
		Statement statement = null;
		String query = null;
		boolean errorState = false;
		Map currentRow = null;
		int i;
		ResultSet set;
		ResultSetMetaData meta;
		int columns;
		Integer intKey[] = null;
		String columnKey[]= null;
		Object value = null;
		int rowIndex = 0;

		if (queryValue != null)
			query = queryValue;
		else
			query = queryBinding.getString();

		if (query == null)
			throw new RequiredParameterException(this, "query", cycle);

		if (connectionBinding != null)
		{
			try
			{
				jdbcConnection = (Connection)connectionBinding.getValue();
			}
			catch (ClassCastException e)
			{
				throw new RequestCycleException(
					"The connection binding is not a java.sql.Connection object.",
					this, cycle, e);
			}
		}

		if (jdbcConnection == null)
		{
			if (URLBinding == null)
				throw new RequiredParameterException(this, "URL", cycle);

			jdbcURL = URLBinding.getString();

			try
			{
				jdbcConnection = DriverManager.getConnection(jdbcURL);

				closeConnection = true;
			}
			catch (SQLException e)
			{
				throw new RequestCycleException(e.getMessage(), this, cycle, e);
			}
		}

		if (jdbcConnection == null)
			throw new RequiredParameterException(this, "connection", cycle);

		try
		{
			rendering = true;

			statement = jdbcConnection.createStatement();

			set = statement.executeQuery(query);

			meta = set.getMetaData();
			columns = meta.getColumnCount();

			while (set.next())
			{

				// On the first row, create a reusable quick-access set of keys

				if (currentRow == null)
				{
					currentRow = new HashMap(13);
					
					row = Collections.unmodifiableMap(currentRow);

					intKey = new Integer[columns + 1];
					columnKey = new String[columns + 1];

					for (i = 1; i <= columns; i++)
					{
						intKey[i] = new Integer(i);
						columnKey[i] = meta.getColumnName(i);
					}
				}
				else
					currentRow.clear();

				// Convert the ResultSet to a Map

				for (i = 1; i <= columns; i++)
				{
					value = set.getObject(i);

					if (value != null)
					{
						currentRow.put(intKey[i], value);

						if (columnKey[i] != null)
							currentRow.put(columnKey[i], value);
					}
				}

				// Update the row (as a Map) back through the binding

				rowBinding.setValue(currentRow);

				renderWrapped(writer, cycle);

				rowIndex++;		

			}  // while

		}
		catch (SQLException e)
		{
			// Don't throw exceptions when closing statement and/or connection

			errorState = true;

			throw new DatabaseQueryException(e.getMessage(), this, cycle, queryValue, e);
		}
		finally
		{
			rendering = false;
			row = null;
			
			try
			{
				if (statement != null)
					statement.close();

				if (closeConnection)
					jdbcConnection.close();
			}
			catch (SQLException e)
			{
				if (!errorState)
					throw new RequestCycleException(e.getMessage(), this, cycle, e);
			}
		}

	}

	public void setConnectionBinding(IBinding value)
	{
		connectionBinding = value;
	}

	public void setQueryBinding(IBinding value)
	{
		queryBinding = value;

		if (value.isStatic())
			queryValue = value.getString();
	}

	public void setRowBinding(IBinding value)
	{
		rowBinding = value;
	}

	public void setURLBinding(IBinding value)
	{
		URLBinding = value;
	}
	
	/**
	 *  Returns the most recently fetched row of the query as an unmodifiable
	 *  {@link Map}.
	 *
	 *  @throws RenderOnlyPropertyException if the component is not rendering.
	 *
	 */
	 
	public Map getRow()
	{
		if (!rendering)
			throw new RenderOnlyPropertyException(this, "row");
		
		return row;
	}	
}

