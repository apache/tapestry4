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
 * A model of the table's data
 * This model need not be used. Implementations may choose to
 * access data via an abstraction.
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
	 * Iterates over all of the rows in the model
	 * @return Iterator the iterator for access to the data
	 */
	Iterator getRows();
    
	/**
	 * Method addTableDataModelListener
     * Adds a listener that is notified when the data in the model is changed
	 * @param objListener the listener to add
	 */
    void addTableDataModelListener(ITableDataModelListener objListener);

	/**
	 * Method removeTableDataModelListener.
     * Removes a listener that is notified when the data in the model is changed
	 * @param objListener the listener to remove
	 */
    void removeTableDataModelListener(ITableDataModelListener objListener);
    
}
