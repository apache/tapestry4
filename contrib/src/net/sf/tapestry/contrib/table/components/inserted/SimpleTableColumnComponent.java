package net.sf.tapestry.contrib.table.components.inserted;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.components.TableColumns;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableSortingState;
import net.sf.tapestry.contrib.table.model.simple.ISimpleTableColumnRenderer;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public class SimpleTableColumnComponent
	extends BaseComponent
	implements ISimpleTableColumnRenderer, PageDetachListener
{
	// transient
	private SimpleTableColumn m_objColumn;
	private ITableModelSource m_objModelSource;

	public SimpleTableColumnComponent()
	{
		init();
	}

	/**
	 * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent arg0)
	{
		init();
	}

	private void init()
	{
		m_objColumn = null;
		m_objModelSource = null;
	}

	/**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
		getPage().addPageDetachListener(this);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.simple.ISimpleTableColumnRenderer#initializeColumnRenderer(SimpleTableColumn, ITableModelSource)
	 */
	public void initializeColumnRenderer(
		SimpleTableColumn objColumn,
		ITableModelSource objSource)
	{
		m_objColumn = objColumn;
		m_objModelSource = objSource;
	}

	public ITableModel getTableModel()
	{
		return m_objModelSource.getTableModel();
	}

	public boolean getColumnSorted()
	{
		return m_objColumn.getSortable();
	}

	public String getDisplayName()
	{
		return m_objColumn.getDisplayName();
	}

	public boolean getIsSorted()
	{
		ITableSortingState objSortingState = getTableModel().getSortingState();
		String strSortColumn = objSortingState.getSortColumn();
		return m_objColumn.getColumnName().equals(strSortColumn);
	}

	public IAsset getSortImage()
	{
		IAsset objImageAsset;

		IRequestCycle objCycle = getPage().getRequestCycle();
		ITableSortingState objSortingState = getTableModel().getSortingState();
		if (objSortingState.getSortOrder()
			== ITableSortingState.SORT_ASCENDING)
		{
			objImageAsset =
				(IAsset) objCycle.getAttribute(
					TableColumns.TABLE_COLUMN_ARROW_UP_ATTRIBUTE);
			if (objImageAsset == null)
				objImageAsset = getAsset("sortUp");
		}
		else
		{
			objImageAsset =
				(IAsset) objCycle.getAttribute(
					TableColumns.TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE);
			if (objImageAsset == null)
				objImageAsset = getAsset("sortDown");
		}

		return objImageAsset;
	}

	public Object[] getColumnSelectedParameters()
	{
		return new Object[] {
			new ComponentAddress(m_objModelSource),
			m_objColumn.getColumnName()};
	}

	public void columnSelected(IRequestCycle objCycle)
	{
		Object[] arrArgs = objCycle.getServiceParameters();
		ComponentAddress objAddr = (ComponentAddress) arrArgs[0];
		String strColumnName = (String) arrArgs[1];

		ITableModelSource objSource =
			(ITableModelSource) objAddr.findComponent(objCycle);
		ITableModel objModel = objSource.getTableModel();

		ITableSortingState objState = objModel.getSortingState();
		if (strColumnName.equals(objState.getSortColumn()))
			objState.setSortColumn(strColumnName, !objState.getSortOrder());
		else
			objState.setSortColumn(
				strColumnName,
				ITableSortingState.SORT_ASCENDING);

		// ensure that the change is saved
		objSource.fireObservedStateChange();
	}

}
