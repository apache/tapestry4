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
