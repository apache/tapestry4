package net.sf.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * Defines a list model of ITableColumn objects
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableColumnModel
{
	/**
	 * Method getColumnCount.
	 * @return int the number of columns in the model
	 */
	int getColumnCount();

	/**
	 * Method getColumn.
	 * @param strName the name of the requested column
	 * @return ITableColumn the column with the given name. null if no such column exists.
	 */
	ITableColumn getColumn(String strName);

	/**
	 * Method getColumns.
	 * @return Iterator an iterator of all columns in the model
	 */
	Iterator getColumns();
}
