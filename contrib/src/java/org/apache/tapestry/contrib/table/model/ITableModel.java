//  Copyright 2004 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.table.model;

import java.util.Iterator;

/**
 * The main interface defining the abstraction containing the table data and state
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModel
{
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
