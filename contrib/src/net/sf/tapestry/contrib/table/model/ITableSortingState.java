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

/**
 * An interface defining the management of the table's sorting state.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSortingState {
    static final boolean SORT_ASCENDING  = false;
    static final boolean SORT_DESCENDING = true;

	/**
	 * Method getSortColumn defines the column that the table should be sorted upon
	 * @return String the name of the sorting column or null if the table is not sorted
	 */
    String getSortColumn();

	/**
	 * Method getSortOrder defines the direction of the table sorting 
	 * @return boolean the sorting order (see constants)
	 */
    boolean getSortOrder();
    
	/**
	 * Method setSortColumn updates the table sorting column and order
	 * @param strName the name of the column to sort by
	 * @param bOrder the sorting order (see constants)
	 */
    void setSortColumn(String strName, boolean bOrder);
}
