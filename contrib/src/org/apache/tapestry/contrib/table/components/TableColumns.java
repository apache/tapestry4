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

import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;

/**
 * A low level Table component that renders the column headers in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * <p>
 * The component iterates over all column objects in the
 * {@link org.apache.tapestry.contrib.table.model.ITableColumnModel} and renders
 * a header for each one of them using the renderer provided by the
 * getColumnRender() method in {@link org.apache.tapestry.contrib.table.model.ITableColumn}.
 * The headers are wrapped in 'th' tags by default.
 * <p>
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableColumns.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableColumns extends AbstractTableViewComponent
{
    public static final String TABLE_COLUMN_ARROW_UP_ATTRIBUTE =
        "org.apache.tapestry.contrib.table.components.TableColumns.arrowUp";

    public static final String TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE =
        "org.apache.tapestry.contrib.table.components.TableColumns.arrowDown";

    public static final String TABLE_COLUMN_CSS_CLASS_SUFFIX = "ColumnHeader";

    // Bindings
    public abstract IBinding getColumnBinding();
    public abstract IBinding getClassBinding();
    public abstract IAsset getArrowDownAsset();
    public abstract IAsset getArrowUpAsset();

    // Transient
    private ITableColumn m_objTableColumn = null;

    /**
     * Returns the currently rendered table column. 
     * You can call this method to obtain the current column.
     *  
     * @return ITableColumn the current table column
     */
    public ITableColumn getTableColumn()
    {
        return m_objTableColumn;
    }

    /**
     * Sets the currently rendered table column. 
     * This method is for internal use only.
     * 
     * @param tableColumn The current table column
     */
    public void setTableColumn(ITableColumn tableColumn)
    {
        m_objTableColumn = tableColumn;

        IBinding objColumnBinding = getColumnBinding();
        if (objColumnBinding != null)
            objColumnBinding.setObject(tableColumn);
    }

    /**
     * Get the list of all table columns to be displayed.
     * 
     * @return an iterator of all table columns
     */
    public Iterator getTableColumnIterator()
    {
        ITableColumnModel objColumnModel = getTableModelSource().getTableModel().getColumnModel();
        return objColumnModel.getColumns();
    }

    /**
     * Returns the renderer to be used to generate the header of the current column
     * 
     * @return the header renderer of the current column
     */
    public IRender getTableColumnRenderer()
    {
        return getTableColumn().getColumnRenderer(
            getPage().getRequestCycle(),
            getTableModelSource());
    }

    /**
     * Returns the CSS class of the generated table cell.
     * It uses the class parameter if it has been bound, or
     * the default value of "[column name]ColumnHeader" otherwise.
     * 
     * @return the CSS class of the cell
     */
    public String getColumnClass()
    {
        IBinding objClassBinding = getClassBinding();
        if (objClassBinding != null)
            return objClassBinding.getString();
        else
            return getTableColumn().getColumnName() + TABLE_COLUMN_CSS_CLASS_SUFFIX;
    }

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object oldValueUp = cycle.getAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE);
        Object oldValueDown = cycle.getAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE);

        cycle.setAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE, getArrowUpAsset());
        cycle.setAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE, getArrowDownAsset());

        super.renderComponent(writer, cycle);

        cycle.setAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE, oldValueUp);
        cycle.setAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE, oldValueDown);

        // set the current column to null when the component is not active
        m_objTableColumn = null;
    }

}
