package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITablePagingState;
import net.sf.tapestry.contrib.table.model.ITableSortingState;

/**
 * A container holding all of the table model states.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableState implements Serializable
{
	private ITablePagingState m_objPagingState;
	private ITableSortingState m_objSortingState;

	public SimpleTableState()
	{
		this(new SimpleTablePagingState(), new SimpleTableSortingState());
	}

	public SimpleTableState(
		ITablePagingState objPagingState,
		ITableSortingState objSortingState)
	{
		m_objPagingState = objPagingState;
		m_objSortingState = objSortingState;
	}

	/**
	 * Returns the pagingState.
	 * @return ITablePagingState
	 */
	public ITablePagingState getPagingState()
	{
		return m_objPagingState;
	}

	/**
	 * Returns the sortingState.
	 * @return ITableSortingState
	 */
	public ITableSortingState getSortingState()
	{
		return m_objSortingState;
	}

}
