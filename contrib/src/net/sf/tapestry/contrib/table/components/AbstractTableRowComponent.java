package net.sf.tapestry.contrib.table.components;

import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableRowSource;

/**
 * @author mindbridge
 *
 */
public class AbstractTableRowComponent extends AbstractTableViewComponent
{
	public ITableRowSource getTableRowSource() throws RequestCycleException
	{
		IRequestCycle objCycle = getPage().getRequestCycle();

		Object objSourceObj =
			objCycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
		ITableRowSource objSource = (ITableRowSource) objSourceObj;

		if (objSource == null)
			throw new RequestCycleException(
				"The component "
					+ getId()
					+ " must be contained within an ITableRowSource component, such as TableRows",
				this);

		return objSource;
	}

}
