package net.sf.tapestry.contrib.table.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableModelSource;

/**
 * @author mindbridge
 *
 */
public class AbstractTableViewComponent extends BaseComponent
{
	public ITableModelSource getTableModelSource() throws RequestCycleException
	{
		IRequestCycle objCycle = getPage().getRequestCycle();

		ITableModelSource objSource =
			(ITableModelSource) objCycle.getAttribute(
				ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE);

		if (objSource == null)
			throw new RequestCycleException(
				"The component "
					+ getId()
					+ " must be contained within an ITableModelSource component, such as TableView",
				this);

		return objSource;
	}

}
