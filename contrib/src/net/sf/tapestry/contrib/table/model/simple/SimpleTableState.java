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

import net.sf.tapestry.contrib.table.model.ITablePagingState;
import net.sf.tapestry.contrib.table.model.ITableSortingState;

/**
 * A container holding all of the table model states.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableState implements Serializable {
    private ITablePagingState m_objPagingState;
    private ITableSortingState m_objSortingState;

    public SimpleTableState()
    {
        this(new SimpleTablePagingState(), new SimpleTableSortingState());
    }
    
    public SimpleTableState(ITablePagingState objPagingState, ITableSortingState objSortingState)
    {
        m_objPagingState = objPagingState;
        m_objSortingState = objSortingState;
    }
    
	/**
	 * Returns the pagingState.
	 * @return ITablePagingState
	 */
	public ITablePagingState getPagingState() {
		return m_objPagingState;
	}

	/**
	 * Returns the sortingState.
	 * @return ITableSortingState
	 */
	public ITableSortingState getSortingState() {
		return m_objSortingState;
	}

}
