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
 * A Tapestry component that provides the current row value.
 * This interface is used for obtaining the row source by components 
 * wrapped by the row source
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableRowSource
{
    final static String TABLE_ROW_SOURCE_ATTRIBUTE = "org.apache.tapestry.contrib.table.model.ITableRowSource";

	/**
	 * Method getTableRow
	 * @return Object the current table row object
	 */
    Object getTableRow();
}
