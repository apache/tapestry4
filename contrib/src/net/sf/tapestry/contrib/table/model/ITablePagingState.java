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
 * An interface defining the management of the table's paging state.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITablePagingState
{
	/**
	 * Method getPageSize provides the size of a page in a number of records.
	 * This value may be meaningless if the model uses a different method for pagination.
	 * @return int the current page size
	 */
	int getPageSize();

	/**
	 * Method setPageSize updates the size of a page in a number of records.
	 * This value may be meaningless if the model uses a different method for pagination.
	 * @param nPageSize the new page size
	 */
	void setPageSize(int nPageSize);

	/**
	 * Method getCurrentPage.
	 * @return int the current active page
	 */
	int getCurrentPage();

	/**
	 * Method setCurrentPage.
	 * @param nPage the new active page
	 */
	void setCurrentPage(int nPage);
}
