package net.sf.tapestry.contrib.table.model;

import java.io.Serializable;

import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;

/**
 * This interface provides a renderer to present the data in a table column.
 * It is usually used by the {@link net.sf.tapestry.contrib.table.model.ITableColumn} 
 * implementations via aggregation.
 * 
 * @see net.sf.tapestry.contrib.table.model.column.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public interface ITableRendererSource extends Serializable
{
	/**
	 * Returns a renderer to present the data of the row in the given column. <p>
	 * This method can also be used to return a renderer to present the
	 * heading of the column. In such a case the row passed would be null.
	 * 
	 * @see net.sf.tapestry.contrib.table.model.ITableColumn#getValueRenderer(IRequestCycle, ITableModelSource, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow);

}
