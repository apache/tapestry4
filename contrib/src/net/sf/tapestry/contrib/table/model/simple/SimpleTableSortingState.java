package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableSortingState;

/**
 * A minimal implementation of 
 * {@link net.sf.tapestry.contrib.table.model.ITableSortingState}
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSortingState
	implements ITableSortingState, Serializable
{
	private String m_strSortColumn;
	private boolean m_bSortOrder;

	public SimpleTableSortingState()
	{
		m_strSortColumn = null; // no sorting
		m_bSortOrder = ITableSortingState.SORT_ASCENDING;
		// irrelevant, but anyway
	}

	/**
	 * Returns the SortOrder.
	 * @return boolean
	 */
	public boolean getSortOrder()
	{
		return m_bSortOrder;
	}

	/**
	 * Returns the SortColumn.
	 * @return int
	 */
	public String getSortColumn()
	{
		return m_strSortColumn;
	}

	/**
	 * Sets the SortColumn.
	 * @param SortColumn The SortColumn to set
	 */
	public void setSortColumn(String strSortColumn, boolean bSortOrder)
	{
		m_strSortColumn = strSortColumn;
		m_bSortOrder = bSortOrder;
	}

}
