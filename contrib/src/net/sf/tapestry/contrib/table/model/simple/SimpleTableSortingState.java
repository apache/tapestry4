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

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableSortingState;

/**
 * A minimal implementation of ITableSortingState
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSortingState implements ITableSortingState, Serializable {
    private String m_strSortColumn;
    private boolean m_bSortOrder;

    public SimpleTableSortingState()
    {
        m_strSortColumn = null;                              // no sorting
        m_bSortOrder = ITableSortingState.SORT_ASCENDING;   // irrelevant, but anyway
    }

	/**
	 * Returns the SortOrder.
	 * @return boolean
	 */
	public boolean getSortOrder() {
		return m_bSortOrder;
	}

	/**
	 * Returns the SortColumn.
	 * @return int
	 */
	public String getSortColumn() {
		return m_strSortColumn;
	}

	/**
	 * Sets the SortColumn.
	 * @param SortColumn The SortColumn to set
	 */
	public void setSortColumn(String strSortColumn, boolean bSortOrder) {
		m_strSortColumn = strSortColumn;
        m_bSortOrder = bSortOrder;
	}

}
