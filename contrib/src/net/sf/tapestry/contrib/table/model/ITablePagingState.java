package net.sf.tapestry.contrib.table.model;

/**
 * An interface defining the management of the table's paging state.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITablePagingState
{
	/**
	 * Method getPageSize provides the size of a page in a number of records.
	 * This value may be meaningless if the model uses a different method for pagination.
	 * @return int the current page size
	 */
	int getPageSize();

	/**
	 * Method setPageSize updates the size of a page in a number of records.
	 * This value may be meaningless if the model uses a different method for pagination.
	 * @param nPageSize the new page size
	 */
	void setPageSize(int nPageSize);

	/**
	 * Method getCurrentPage.
	 * @return int the current active page
	 */
	int getCurrentPage();

	/**
	 * Method setCurrentPage.
	 * @param nPage the new active page
	 */
	void setCurrentPage(int nPage);
}
