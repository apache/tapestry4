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

package net.sf.tapestry.contrib.table.components.inserted;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.components.*;
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
	 * @see net.sf.tapestry.contrib.table.model.simple.ISimpleTableColumnRenderer#initializeColumnRenderer(SimpleTableColumn, ITableModel)
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
    
    public boolean getIsSortedDown()
    {
        ITableSortingState objSortingState = getTableModel().getSortingState();
        String strSortColumn = objSortingState.getSortColumn();
        if (!m_objColumn.getColumnName().equals(strSortColumn)) return false;
        return objSortingState.getSortOrder() == ITableSortingState.SORT_ASCENDING;
    }

    public boolean getIsSortedUp()
    {
        ITableSortingState objSortingState = getTableModel().getSortingState();
        String strSortColumn = objSortingState.getSortColumn();
        if (!m_objColumn.getColumnName().equals(strSortColumn)) return false;
        return objSortingState.getSortOrder() == ITableSortingState.SORT_DESCENDING;
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
	}

}
