package net.sf.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * The main interface defining the abstraction containing the table data and state
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModel
{
	/**
	 * Method getColumnModel.
	 * @return ITableColumnModel the column model of the table
	 */
	ITableColumnModel getColumnModel();

	/**
	 * Method getSortingState.
	 * @return ITableSortingState the sorting state of the table
	 */
	ITableSortingState getSortingState();
	/**
	 * Method getPagingState.
	 * @return ITablePagingState the paging state of the table
	 */
	ITablePagingState getPagingState();

	/**
	 * Method getPageCount.
	 * @return int the number of pages this table would have given the current data and paging state
	 */
	int getPageCount();
	/**
	 * Method getCurrentPageRows.
	 * @return Iterator the rows in the current table page given the current data, sorting, and paging state
	 */
	Iterator getCurrentPageRows();
}
