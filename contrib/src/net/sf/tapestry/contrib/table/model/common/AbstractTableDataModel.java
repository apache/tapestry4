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

package net.sf.tapestry.contrib.table.model.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableDataModelListener;

/**
 * An implementation of the listener support in the ITableDataModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class AbstractTableDataModel implements ITableDataModel
{
	private List m_arrListeners;

	public AbstractTableDataModel()
	{
		m_arrListeners = new ArrayList();
	}

	/**
	 * Method fireTableDataModelEvent.
	 * Fires a change event to all listeners
	 * @param objEvent the event to pass to the listeners
	 */
	protected void fireTableDataModelEvent(CTableDataModelEvent objEvent)
	{
		for (Iterator it = m_arrListeners.iterator(); it.hasNext();)
		{
			ITableDataModelListener objListener =
				(ITableDataModelListener) it.next();
			objListener.tableDataChanged(objEvent);
		}
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#addTableDataModelListener(ITableDataModelListener)
	 */
	public void addTableDataModelListener(ITableDataModelListener objListener)
	{
		m_arrListeners.add(objListener);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#removeTableDataModelListener(ITableDataModelListener)
	 */
	public void removeTableDataModelListener(ITableDataModelListener objListener)
	{
		m_arrListeners.remove(objListener);
	}

}
