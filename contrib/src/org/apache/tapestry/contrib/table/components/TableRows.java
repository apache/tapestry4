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

import java.util.Iterator;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableRowSource;

/**
 * A low level Table component that generates the rows of the current page in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * 
 * <p>
 * The component iterates over the rows of the current page in the table. 
 * The rows are wrapped in 'tr' tags by default. 
 * You can define columns manually within, or
 * you can use {@link org.apache.tapestry.contrib.table.components.TableValues} 
 * to generate the columns automatically.
 * 
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableRows.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableRows extends AbstractTableViewComponent implements ITableRowSource
{
    // Parameters
    public abstract IBinding getRowBinding();

    // Transient
    private Object m_objTableRow = null;

    /**
     * Returns the currently rendered table row.
     * You can call this method to obtain the current row.
     *  
     * @return Object the current table row
     */
    public Object getTableRow()
    {
        return m_objTableRow;
    }

    /**
     * Sets the currently rendered table row. 
     * This method is for internal use only.
     * 
     * @param tableRow The current table row
     */
    public void setTableRow(Object tableRow)
    {
        m_objTableRow = tableRow;

        IBinding objRowBinding = getRowBinding();
        if (objRowBinding != null)
            objRowBinding.setObject(tableRow);
    }

    /**
     * Get the list of all table rows to be displayed on this page.
     * 
     * @return an iterator of all table rows
     */
    public Iterator getTableRowsIterator()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        return objTableModel.getCurrentPageRows();
    }

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object objOldValue = cycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, this);

        super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, objOldValue);

        // set the current row to null when the component is not active
        m_objTableRow = null;
    }

}
