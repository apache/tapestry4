/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.contrib.table.model.sql;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITablePagingState;
import net.sf.tapestry.contrib.table.model.ITableSortingState;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SqlTableModel implements ITableModel, Serializable
{
	private static final Log LOG = LogFactory.getLog(SqlTableModel.class);

	private ISqlTableDataSource m_objDataSource;
	private SqlTableColumnModel m_objColumnModel;
	private SimpleTableState m_objTableState;

	public SqlTableModel(
		ISqlTableDataSource objDataSource,
		SqlTableColumnModel objColumnModel)
	{
		this(objDataSource, objColumnModel, new SimpleTableState());
	}

	public SqlTableModel(
		ISqlTableDataSource objDataSource,
		SqlTableColumnModel objColumnModel,
		SimpleTableState objState)
	{
		m_objDataSource = objDataSource;
		m_objColumnModel = objColumnModel;
		m_objTableState = objState;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getColumnModel()
	 */
	public ITableColumnModel getColumnModel()
	{
		return m_objColumnModel;
	}

	public SqlTableColumnModel getSqlColumnModel()
	{
		return m_objColumnModel;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getCurrentPageRows()
	 */
	public Iterator getCurrentPageRows()
	{
		try
		{
			ResultSet objResultSet =
				getSqlDataSource().getCurrentRows(
					getSqlColumnModel(),
					getState());

			return new ResultSetIterator(objResultSet)
			{
				protected void notifyEnd()
				{
					getSqlDataSource().closeResultSet(getResultSet());
				}
			};
		}
		catch (SQLException e)
		{
			LOG.error("Cannot get current page rows", e);
			return new ResultSetIterator(null);
		}
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getPageCount()
	 */
	public int getPageCount()
	{
		try
		{
			int nPageCount =
				(m_objDataSource.getRowCount() - 1)
					/ getPagingState().getPageSize()
					+ 1;
			if (nPageCount < 1)
				nPageCount = 1;
			return nPageCount;
		}
		catch (SQLException e)
		{
			LOG.error("Cannot get row count", e);
			return 1;
		}
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getPagingState()
	 */
	public ITablePagingState getPagingState()
	{
		return getState().getPagingState();
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getSortingState()
	 */
	public ITableSortingState getSortingState()
	{
		return getState().getSortingState();
	}

	/**
	 * Returns the tableState.
	 * @return SimpleTableState
	 */
	public SimpleTableState getState()
	{
		return m_objTableState;
	}

	/**
	 * Returns the dataSource.
	 * @return ISqlTableDataSource
	 */
	public ISqlTableDataSource getSqlDataSource()
	{
		return m_objDataSource;
	}

}
