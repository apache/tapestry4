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

package net.sf.tapestry.contrib.table.model.simple;

import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;

/**
 * A ITableSessionStateManager implementation that saves 
 * only the paging and sorting state of the table model into the session.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSessionStateManager implements ITableSessionStateManager {
    private ITableDataModel m_objDataModel;
    private ITableColumnModel m_objColumnModel;

    public SimpleTableSessionStateManager(ITableDataModel objDataModel, ITableColumnModel objColumnModel) {
        m_objDataModel = objDataModel;
        m_objColumnModel = objColumnModel;
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionManager#getSessionState(ITableModel)
	 */
	public Object getSessionState(ITableModel objModel) {
        SimpleTableModel objSimpleModel = (SimpleTableModel) objModel;
		return objSimpleModel.getState();
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionManager#recreateTableModel(Object)
	 */
	public ITableModel recreateTableModel(Object objState) {
        if (objState == null) return null;
        SimpleTableState objSimpleState = (SimpleTableState) objState;
		return new SimpleTableModel(m_objDataModel, m_objColumnModel, objSimpleState);
	}

}
