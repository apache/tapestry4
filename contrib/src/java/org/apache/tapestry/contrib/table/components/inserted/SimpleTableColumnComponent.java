//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.table.components.inserted;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.components.TableColumns;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.util.ComponentAddress;

/**
 * A component that renders the default column header.
 * 
 * If the current column is sortable, it renders the header as a link.
 * Clicking on the link causes the table to be sorted on that column.
 * Clicking on the link again causes the sorting order to be reversed.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableColumnComponent
	extends BaseComponent
	implements ITableRendererListener, PageDetachListener
{
	// transient
	private ITableColumn m_objColumn;
	private ITableModelSource m_objModelSource;

	public SimpleTableColumnComponent()
	{
		init();
	}

	/**
	 * @see org.apache.tapestry.event.PageDetachListener#pageDetached(PageEvent)
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
     * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
     */
    public void initializeRenderer(
        IRequestCycle objCycle,
        ITableModelSource objSource,
        ITableColumn objColumn,
        Object objRow)
    {
        m_objModelSource = objSource;
        m_objColumn = objColumn;
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
        if (m_objColumn instanceof SimpleTableColumn) {
            SimpleTableColumn objSimpleColumn = (SimpleTableColumn) m_objColumn;
    		return objSimpleColumn.getDisplayName();
        }
        return m_objColumn.getColumnName();
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
