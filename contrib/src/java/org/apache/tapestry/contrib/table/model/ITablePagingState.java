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
	 * Gets the currently selected page. The page number is counted from 0.
	 * @return int the current active page
	 */
	int getCurrentPage();

	/**
	 * Sets the newly selected page. The page number is counted from 0.
	 * @param nPage the new active page
	 */
	void setCurrentPage(int nPage);
}
