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

package net.sf.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * The main interface defining the abstraction containing the table data and state
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModel {
	/**
	 * Method getColumnModel.
	 * @return ITableColumnModel the column model of the table
	 */
    ITableColumnModel getColumnModel();

	/**
	 * Method getSortingState.
	 * @return ITableSortingState the sorting state of the table
	 */
    ITableSortingState getSortingState();
	/**
	 * Method getPagingState.
	 * @return ITablePagingState the paging state of the table
	 */
    ITablePagingState getPagingState();
    
	/**
	 * Method getPageCount.
	 * @return int the number of pages this table would have given the current data and paging state
	 */
    int getPageCount();
	/**
	 * Method getCurrentPageRows.
	 * @return Iterator the rows in the current table page given the current data, sorting, and paging state
	 */
    Iterator getCurrentPageRows();
}
