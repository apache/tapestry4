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

/**
 * A component that renders the default column header in a form.
 * 
 * If the current column is sortable, it renders the header as a link.
 * Clicking on the link causes the table to be sorted on that column.
 * Clicking on the link again causes the sorting order to be reversed.
 * 
 * This component renders links that cause the form to be submitted. 
 * This ensures that the updated data in the other form fields is preserved. 
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class SimpleTableColumnFormComponent
	extends BaseComponent
	implements ITableRendererListener
{

    public abstract ITableColumn getTableColumn();
    public abstract void setTableColumn(ITableColumn objColumn);

    public abstract ITableModelSource getTableModelSource();
    public abstract void setTableModelSource(ITableModelSource objSource);

    public abstract String getSelectedColumnName();

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
     */
    public void initializeRenderer(
        IRequestCycle objCycle,
        ITableModelSource objSource,
        ITableColumn objColumn,
        Object objRow)
    {
        setTableModelSource(objSource);
        setTableColumn(objColumn);
    }

	public ITableModel getTableModel()
	{
		return getTableModelSource().getTableModel();
	}

	public boolean getColumnSorted()
	{
		return getTableColumn().getSortable();
	}

	public String getDisplayName()
	{
        ITableColumn objColumn = getTableColumn();
        
        if (objColumn instanceof SimpleTableColumn) {
            SimpleTableColumn objSimpleColumn = (SimpleTableColumn) objColumn;
    		return objSimpleColumn.getDisplayName();
        }
        return objColumn.getColumnName();
	}

	public boolean getIsSorted()
	{
		ITableSortingState objSortingState = getTableModel().getSortingState();
		String strSortColumn = objSortingState.getSortColumn();
		return getTableColumn().getColumnName().equals(strSortColumn);
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

	public void columnSelected(IRequestCycle objCycle)
	{
        String strColumnName = getSelectedColumnName();
		ITableSortingState objState = getTableModel().getSortingState();
		if (strColumnName.equals(objState.getSortColumn()))
			objState.setSortColumn(strColumnName, !objState.getSortOrder());
		else
			objState.setSortColumn(
				strColumnName,
				ITableSortingState.SORT_ASCENDING);

		// ensure that the change is saved
		getTableModelSource().fireObservedStateChange();
	}

}
