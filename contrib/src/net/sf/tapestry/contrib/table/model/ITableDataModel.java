package net.sf.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * A model containing the data within the table.
 * This model is not necessary to be used. Implementations may choose to 
 * access its data in a way that would provide an abstraction as to the
 * true source of data.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableDataModel
{
	/**
	 * Method getRowCount.
	 * @return int the number of rows in the model
	 */
	int getRowCount();

	/**
	 * Iterates over all of the rows in the model
	 * @return Iterator the iterator for access to the data
	 */
	Iterator getRows();
    
	/**
	 * Method addTableDataModelListener
     * Adds a listener that is notified when the data in the model is changed
	 * @param objListener the listener to add
	 */
    void addTableDataModelListener(ITableDataModelListener objListener);

	/**
	 * Method removeTableDataModelListener.
     * Removes a listener that is notified when the data in the model is changed
	 * @param objListener the listener to remove
	 */
    void removeTableDataModelListener(ITableDataModelListener objListener);
    
}
