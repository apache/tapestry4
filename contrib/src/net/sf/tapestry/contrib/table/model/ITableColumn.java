package net.sf.tapestry.contrib.table.model;

import java.util.Comparator;

import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;

/**
 * The interface defining a table column. 
 * 
 * A column is responsible for presenting a particular part of the data
 * from the objects in the table. The is done via the getValueRender() method.
 * 
 * A column man be sortable, in which case it defines the way in which the 
 * objects in the table must be sorted by providing a Comparator.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableColumn
{
	/**
	 * Method getColumnName provides the name of the column. 
	 *
	 * The column name must be unique and is generally used for the identification 
	 * of the column. It does not have to be the same as the display name 
	 * via which the column is identified to the user (see the getColumnRender() method).
	 * @return String the name of the column
	 */
	String getColumnName();

	/**
	 * Method getSortable declares whether the column allows sorting.
	 * If the column allows sorting, it must also return a valid Comparator
	 * via the getComparator() method.
	 * @return boolean whether the column is sortable or not
	 */
	boolean getSortable();

	/**
	 * Method getComparator returns the Comparator to be used to sort 
	 * the data in the table according to this column. The Comparator must
	 * accept two different rows, compare them according to this column, 
	 * and return the appropriate value.
	 * @return Comparator the Comparator to sort the table data by
	 */
	Comparator getComparator();

	/**
	 * Method getColumnRenderer provides a renderer that takes care of rendering 
	 * the column in the table header. If the column is sortable, the renderer
	 * may provide a mechanism to sort the table in an ascending or descending 
	 * manner.
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @return IRender the renderer to present the column header
	 */
	IRender getColumnRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource);

	/**
	 * Method getValueRenderer provides a renderer for presenting the value of a 
	 * particular row in the current column.
	 * 
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @param objRow the row data
	 * @return IRender the renderer to present the value of the row in this column
	 */
	IRender getValueRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		Object objRow);
}
