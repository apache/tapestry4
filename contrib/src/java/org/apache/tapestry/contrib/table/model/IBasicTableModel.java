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
 * A simplified version of the table model that concerns itself only with
 * providing the data on the current page. 
 * 
 * @version $Id$
 * @author mindbridge
 * @since 3.0
 */
public interface IBasicTableModel
{
    /**
     *  Returns the number of all records
     *  @return the number of all rows
     **/
    int getRowCount();

    /** 
     *  Returns the rows on the current page.
     *  @param nFirst the index of the first item to be dispayed
     *  @param nPageSize the number of items to be displayed
     *  @param objSortColumn the column to sort by or null if there is no sorting
     *  @param bSortOrder determines the sorting order (ascending or descending)
     **/
    Iterator getCurrentPageRows(int nFirst, int nPageSize, ITableColumn objSortColumn, boolean bSortOrder);
}
