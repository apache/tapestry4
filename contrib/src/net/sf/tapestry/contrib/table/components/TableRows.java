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
import net.sf.tapestry.contrib.table.model.ITableModel;

/**
 * @author mindbridge
 * @version $Id$
 *
 */
public class TableRows extends BaseComponent
{

	// Bindings    
	private IBinding m_objTableViewBinding = null;

	// Transient
	private Object m_objTableRow;

	/**
	 * Returns the tableViewBinding.
	 * @return IBinding
	 */
	public IBinding getTableViewBinding()
	{
		return m_objTableViewBinding;
	}

	/**
	 * Sets the tableViewBinding.
	 * @param tableViewBinding The tableViewBinding to set
	 */
	public void setTableViewBinding(IBinding tableViewBinding)
	{
		m_objTableViewBinding = tableViewBinding;
	}

	public TableView getTableView()
	{
		return (TableView) getTableViewBinding().getObject();
	}

	public Iterator getTableRowsIterator()
	{
		ITableModel objTableModel = getTableView().getTableModel();
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
	}

}
