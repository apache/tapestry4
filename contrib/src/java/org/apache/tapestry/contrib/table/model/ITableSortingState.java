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

/**
 * An interface defining the management of the table's sorting state.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSortingState
{
	static final boolean SORT_ASCENDING = false;
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
