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
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;

/**
 * A low level Table component that generates the columns in the current row in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableRows}.
 * 
 * <p>
 * The component iterates over the columns in the table and 
 * automatically renders the column values for the current table row. 
 * The columns are wrapped in 'td' tags by default. <br>
 * The column values are rendered using the renderer returned by the 
 * getValueRenderer() method in {@link org.apache.tapestry.contrib.table.model.ITableColumn}.
 * 
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableValues.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableValues extends AbstractTableRowComponent
{
    public static final String TABLE_VALUE_CSS_CLASS_SUFFIX = "ColumnValue";

    // Bindings
    public abstract IBinding getColumnBinding();
    public abstract IBinding getClassBinding();

	// Transient
	private ITableColumn m_objTableColumn;

    /**
     * Get the list of all table columns to be displayed.
     * 
     * @return an iterator of all table columns
     */
	public Iterator getTableColumnIterator()
	{
		ITableColumnModel objColumnModel =
			getTableModelSource().getTableModel().getColumnModel();
		return objColumnModel.getColumns();
	}

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
     * Returns the renderer to be used to generate the appearance of the current column
     * 
     * @return the value renderer of the current column
     */
	public IRender getTableValueRenderer()
	{
		Object objRow = getTableRowSource().getTableRow();
		return getTableColumn().getValueRenderer(
			getPage().getRequestCycle(),
			getTableModelSource(),
			objRow);
	}

    /**
     * Returns the CSS class of the generated table cell.
     * It uses the class parameter if it has been bound, or
     * the default value of "[column name]ColumnValue" otherwise.
     * 
     * @return the CSS class of the cell
     */
    public String getValueClass()
    {
        IBinding objClassBinding = getClassBinding();
        if (objClassBinding != null)
            return objClassBinding.getString();
        else
            return getTableColumn().getColumnName() + TABLE_VALUE_CSS_CLASS_SUFFIX;
    }

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderComponent(writer, cycle);

        // set the current column to null when the component is not active
        m_objTableColumn = null;
    }

}
