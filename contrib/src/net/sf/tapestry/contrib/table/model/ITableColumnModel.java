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
 * Defines a list model of ITableColumn objects
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableColumnModel
{
	/**
	 * Method getColumnCount.
	 * @return int the number of columns in the model
	 */
	int getColumnCount();

	/**
	 * Method getColumn.
	 * @param strName the name of the requested column
	 * @return ITableColumn the column with the given name. null if no such column exists.
	 */
	ITableColumn getColumn(String strName);

	/**
	 * Method getColumns.
	 * @return Iterator an iterator of all columns in the model
	 */
	Iterator getColumns();
}
