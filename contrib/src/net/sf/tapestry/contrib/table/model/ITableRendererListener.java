package net.sf.tapestry.contrib.table.model;

import net.sf.tapestry.IComponent;
import net.sf.tapestry.IRequestCycle;

/**
 * 
 * @see net.sf.tapestry.contrib.table.model.column.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public interface ITableRendererListener extends IComponent
{
	void initializeRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow);
}
