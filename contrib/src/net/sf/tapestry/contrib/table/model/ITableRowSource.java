package net.sf.tapestry.contrib.table.model;

/**
 * A Tapestry component that provides the current row value.
 * This interface is used for obtaining the row source by components 
 * wrapped by the row source
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableRowSource
{
    final static String TABLE_ROW_SOURCE_ATTRIBUTE = "net.sf.tapestry.contrib.table.model.ITableRowSource";

	/**
	 * Method getTableRow
	 * @return Object the current table row object
	 */
    Object getTableRow();
}
