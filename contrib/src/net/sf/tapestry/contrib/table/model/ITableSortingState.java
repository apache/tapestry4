package net.sf.tapestry.contrib.table.model;

/**
 * An interface defining the management of the table's sorting state.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSortingState
{
	static final boolean SORT_ASCENDING = false;
	static final boolean SORT_DESCENDING = true;

	/**
	 * Method getSortColumn defines the column that the table should be sorted upon
	 * @return String the name of the sorting column or null if the table is not sorted
	 */
	String getSortColumn();

	/**
	 * Method getSortOrder defines the direction of the table sorting 
	 * @return boolean the sorting order (see constants)
	 */
	boolean getSortOrder();

	/**
	 * Method setSortColumn updates the table sorting column and order
	 * @param strName the name of the column to sort by
	 * @param bOrder the sorting order (see constants)
	 */
	void setSortColumn(String strName, boolean bOrder);
}
