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

package org.apache.tapestry.contrib.table.components;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;

/**
 * The facade component in the Table family. Table allows you to present 
 * a sortable and pagable table simply and easily by using only this one component.
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.Table.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class Table extends BaseComponent implements ITableModelSource
{
    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModelSource#getTableModel()
     */
    public ITableModel getTableModel()
    {
        return getTableViewComponent().getTableModel();
    }

    /**
     * Indicates that the table model has changed and it may need to saved.
     * This method has to be invoked if modifications are made to the model.
     *  
     * @see org.apache.tapestry.contrib.table.model.ITableModelSource#fireObservedStateChange()
     */
    public void fireObservedStateChange()
    {
        getTableViewComponent().fireObservedStateChange();
    }

    /**
     * Resets the state of the component and forces it to load a new
     * TableModel from the tableModel binding the next time it renders.
     */
    public void reset()
    {
        getTableViewComponent().reset();
    }

    /**
     * Returns the currently rendered table column. 
     * You can call this method to obtain the current column.
     *  
     * @return ITableColumn the current table column
     */
    public ITableColumn getTableColumn()
    {
        Object objCurrentRow = getTableRow();

        // if the current row is null, then we are most likely rendering TableColumns
        if (objCurrentRow == null)
            return getTableColumnsComponent().getTableColumn();
        else
            return getTableValuesComponent().getTableColumn();
    }

    /**
     * Returns the currently rendered table row or null 
     * if the rows are not rendered at the moment.
     * You can call this method to obtain the current row.
     *  
     * @return Object the current table row 
     */
    public Object getTableRow()
    {
        return getTableRowsComponent().getTableRow();
    }

    protected TableView getTableViewComponent()
    {
        return (TableView) getComponent("tableView");
    }

    protected TableColumns getTableColumnsComponent()
    {
        return (TableColumns) getComponent("tableColumns");
    }

    protected TableRows getTableRowsComponent()
    {
        return (TableRows) getComponent("tableRows");
    }

    protected TableValues getTableValuesComponent()
    {
        return (TableValues) getComponent("tableValues");
    }
}
