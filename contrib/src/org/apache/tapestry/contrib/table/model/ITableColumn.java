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

import java.util.Comparator;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * The interface defining a table column. 
 * 
 * A column is responsible for presenting a particular part of the data
 * from the objects in the table. This is done via the getValueRender() method.
 * 
 * A column may be sortable, in which case it defines the way in which the
 * objects in the table must be sorted by providing a Comparator.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableColumn
{
	/**
	 * Method getColumnName provides the name of the column. 
	 *
	 * The column name must be unique and is generally used for the identification 
	 * of the column. It does not have to be the same as the display name 
	 * via which the column is identified to the user (see the getColumnRender() method).
	 * @return String the name of the column
	 */
	String getColumnName();

	/**
	 * Method getSortable declares whether the column allows sorting.
	 * If the column allows sorting, it must also return a valid Comparator
	 * via the getComparator() method.
	 * @return boolean whether the column is sortable or not
	 */
	boolean getSortable();

	/**
	 * Method getComparator returns the Comparator to be used to sort 
	 * the data in the table according to this column. The Comparator must
	 * accept two different rows, compare them according to this column, 
	 * and return the appropriate value.
	 * @return Comparator the Comparator used to sort the table data
	 */
	Comparator getComparator();

	/**
	 * Method getColumnRenderer provides a renderer that takes care of rendering 
	 * the column in the table header. If the column is sortable, the renderer
	 * may provide a mechanism to sort the table in an ascending or descending 
	 * manner.
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @return IRender the renderer to present the column header
	 */
	IRender getColumnRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource);

	/**
	 * Method getValueRenderer provides a renderer for presenting the value of a 
	 * particular row in the current column.
	 * 
	 * @param objCycle the current request cycle
	 * @param objSource a component that can provide the table model (typically TableView)
	 * @param objRow the row data
	 * @return IRender the renderer to present the value of the row in this column
	 */
	IRender getValueRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		Object objRow);
}
