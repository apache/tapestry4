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

package org.apache.tapestry.contrib.table.model.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.contrib.table.model.ITablePagingState;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleSqlTableDataSource implements ISqlTableDataSource
{
	private static final Log LOG =
		LogFactory.getLog(SimpleSqlTableDataSource.class);

	private ISqlConnectionSource m_objConnSource;
	private String m_strTableName;
	private String m_strWhereClause;

	public SimpleSqlTableDataSource(
		ISqlConnectionSource objConnSource,
		String strTableName)
	{
		this(objConnSource, strTableName, null);
	}

	public SimpleSqlTableDataSource(
		ISqlConnectionSource objConnSource,
		String strTableName,
		String strWhereClause)
	{
		setConnSource(objConnSource);
		setTableName(strTableName);
		setWhereClause(strWhereClause);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.sql.ISqlTableDataSource#getRowCount()
	 */
	public int getRowCount() throws SQLException
	{
		String strQuery = generateCountQuery();
		LOG.trace("Invoking query to count rows: " + strQuery);

		Connection objConn = getConnSource().obtainConnection();
		try
		{
			Statement objStmt = objConn.createStatement();
			try
			{
				ResultSet objRS = objStmt.executeQuery(strQuery);
				objRS.next();
				return objRS.getInt(1);
			}
			finally
			{
				objStmt.close();
			}
		}
		finally
		{
			getConnSource().returnConnection(objConn);
		}
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.sql.ISqlTableDataSource#getCurrentRows(SqlTableColumnModel, SimpleTableState)
	 */
	public ResultSet getCurrentRows(
		SqlTableColumnModel objColumnModel,
		SimpleTableState objState)
		throws SQLException
	{
		String strQuery = generateDataQuery(objColumnModel, objState);
		LOG.trace("Invoking query to load current rows: " + strQuery);

		Connection objConn = getConnSource().obtainConnection();
		Statement objStmt = objConn.createStatement();
		return objStmt.executeQuery(strQuery);
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.sql.ISqlTableDataSource#closeResultSet(ResultSet)
	 */
	public void closeResultSet(ResultSet objResultSet)
	{
		try
		{
			Statement objStmt = objResultSet.getStatement();
			Connection objConn = objStmt.getConnection();
			try
			{
				objResultSet.close();
				objStmt.close();
			}
			catch (SQLException e)
			{
				// ignore
			}
			getConnSource().returnConnection(objConn);
		}
		catch (SQLException e)
		{
			LOG.warn("Error while closing the result set", e);
		}
	}

	protected String quoteObjectName(String strObject)
	{
		return strObject;
	}

	/**
	 * Returns the tableName.
	 * @return String
	 */
	public String getTableName()
	{
		return m_strTableName;
	}

	/**
	 * Sets the tableName.
	 * @param tableName The tableName to set
	 */
	public void setTableName(String tableName)
	{
		m_strTableName = tableName;
	}

	/**
	 * Returns the connSource.
	 * @return ISqlConnectionSource
	 */
	public ISqlConnectionSource getConnSource()
	{
		return m_objConnSource;
	}

	/**
	 * Sets the connSource.
	 * @param connSource The connSource to set
	 */
	public void setConnSource(ISqlConnectionSource connSource)
	{
		m_objConnSource = connSource;
	}

	/**
	 * Returns the whereClause.
	 * @return String
	 */
	public String getWhereClause()
	{
		return m_strWhereClause;
	}

	/**
	 * Sets the whereClause.
	 * @param whereClause The whereClause to set
	 */
	public void setWhereClause(String whereClause)
	{
		m_strWhereClause = whereClause;
	}

	protected String generateColumnList(SqlTableColumnModel objColumnModel)
	{
		// build the column selection
		StringBuffer objColumnBuf = new StringBuffer();
		for (int i = 0; i < objColumnModel.getColumnCount(); i++)
		{
			SqlTableColumn objColumn = objColumnModel.getSqlColumn(i);
			if (i > 0)
				objColumnBuf.append(", ");
			objColumnBuf.append(quoteObjectName(objColumn.getColumnName()));
		}

		return objColumnBuf.toString();
	}

	protected String generateWhereClause()
	{
		String strWhereClause = getWhereClause();
		if (strWhereClause == null || strWhereClause.equals(""))
			return "";
		return "WHERE " + strWhereClause + " ";
	}

	protected String generateOrderByClause(ITableSortingState objSortingState)
	{
		// build the sorting clause
		StringBuffer objSortingBuf = new StringBuffer();
		if (objSortingState.getSortColumn() != null)
		{
			objSortingBuf.append("ORDER BY ");
			objSortingBuf.append(objSortingState.getSortColumn());
			if (objSortingState.getSortOrder()
				== ITableSortingState.SORT_ASCENDING)
				objSortingBuf.append(" ASC ");
			else
				objSortingBuf.append(" DESC ");
		}

		return objSortingBuf.toString();
	}

	protected String generateLimitClause(ITablePagingState objPagingState)
	{
		int nPageSize = objPagingState.getPageSize();
		int nStart = objPagingState.getCurrentPage() * nPageSize;
		String strPagingBuf = "LIMIT " + nPageSize + " OFFSET " + nStart + " ";
		return strPagingBuf;
	}

	protected String generateDataQuery(
		SqlTableColumnModel objColumnModel,
		SimpleTableState objState)
	{
		String strQuery =
			"SELECT "
				+ generateColumnList(objColumnModel)
				+ " FROM "
				+ getTableName()
				+ " "
				+ generateWhereClause()
				+ generateOrderByClause(objState.getSortingState())
				+ generateLimitClause(objState.getPagingState());

		return strQuery;
	}

	protected String generateCountQuery()
	{
		String strQuery =
			"SELECT COUNT(*) FROM "
				+ getTableName()
				+ " "
				+ generateWhereClause();

		return strQuery;
	}

}
