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
 * A model containing the data within the table.
 * This model is not necessary to be used. Implementations may choose to 
 * access its data in a way that would provide an abstraction as to the
 * true source of data.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableDataModel
{
	/**
	 * Method getRowCount.
	 * @return int the number of rows in the model
	 */
	int getRowCount();

	/**
	 * Method getRow.
	 * @param nRow the requested row index
	 * @return Object the row with the given index
	 */
	Object getRow(int nRow);

	/**
	 * Iterates over a subset of the model
	 * @param nFrom the inital index of the model for iterating (inclusive)
	 * @param nTo the final index of the model for iterating (exclusive)
	 * @return Iterator the iterator for access to the data
	 */
	Iterator getRows(int nFrom, int nTo);

	/**
	 * Iterates over all of the rows in the model
	 * @return Iterator the iterator for access to the data
	 */
	Iterator getRows();
}
