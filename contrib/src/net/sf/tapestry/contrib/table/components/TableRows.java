//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.table.components;

import java.util.Iterator;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableRowSource;

/**
 * @author mindbridge
 * @version $Id$
 *
 */
public class TableRows extends AbstractTableViewComponent implements ITableRowSource
{
    // Binding
    private IBinding m_objValueBinding = null;

	// Transient
	private Object m_objTableRow;


	public Iterator getTableRowsIterator() throws RequestCycleException
	{
		ITableModel objTableModel = getTableModelSource().getTableModel();
		return objTableModel.getCurrentPageRows();
	}

	/**
	 * Returns the tableRow.
	 * @return Object
	 */
	public Object getTableRow()
	{
		return m_objTableRow;
	}

	/**
	 * Sets the tableRow.
	 * @param tableRow The tableRow to set
	 */
	public void setTableRow(Object tableRow)
	{
		m_objTableRow = tableRow;
        
        IBinding objValueBinding = getValueBinding();
        if (objValueBinding != null)
            objValueBinding.setObject(tableRow);
	}

    /**
     * Returns the valueBinding.
     * @return IBinding
     */
    public IBinding getValueBinding()
    {
        return m_objValueBinding;
    }

    /**
     * Sets the valueBinding.
     * @param valueBinding The valueBinding to set
     */
    public void setValueBinding(IBinding valueBinding)
    {
        m_objValueBinding = valueBinding;
    }

    /**
	 * @see net.sf.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
        Object objOldValue = cycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_PROPERTY);
        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_PROPERTY, this);

		super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_PROPERTY, objOldValue);
	}


}
