package net.sf.tapestry.contrib.table.model.simple;

import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableRendererSource;
import net.sf.tapestry.valid.RenderString;

/**
 * This is a simple implementation of 
 * {@link net.sf.tapestry.contrib.table.model.column.ITableValueRendererSource} 
 * that returns a standard column value renderer.
 * 
 * This implementation requires that the column passed is of type SimpleTableColumn
 * 
 * @see net.sf.tapestry.contrib.table.model.column.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class SimpleTableValueRendererSource implements ITableRendererSource
{

	/**
	 * @see net.sf.tapestry.contrib.table.model.column.ITableValueRendererSource#getValueRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow)
	{
		SimpleTableColumn objSimpleColumn = (SimpleTableColumn) objColumn;

		Object objValue = objSimpleColumn.getColumnValue(objRow);
		if (objValue == null)
			objValue = "";

		return new RenderString(objValue.toString());
	}

}
