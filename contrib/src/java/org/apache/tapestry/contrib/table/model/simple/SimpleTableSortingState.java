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

package org.apache.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import org.apache.tapestry.contrib.table.model.ITableSortingState;

/**
 * A minimal implementation of 
 * {@link org.apache.tapestry.contrib.table.model.ITableSortingState}
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSortingState
	implements ITableSortingState, Serializable
{
	private String m_strSortColumn;
	private boolean m_bSortOrder;

	public SimpleTableSortingState()
	{
		m_strSortColumn = null; // no sorting
		m_bSortOrder = ITableSortingState.SORT_ASCENDING;
		// irrelevant, but anyway
	}

	/**
	 * Returns the SortOrder.
	 * @return boolean
	 */
	public boolean getSortOrder()
	{
		return m_bSortOrder;
	}

	/**
	 * Returns the SortColumn.
	 * @return int
	 */
	public String getSortColumn()
	{
		return m_strSortColumn;
	}

	/**
	 * Sets the SortColumn.
	 * @param strSortColumn The SortColumn to set
	 */
	public void setSortColumn(String strSortColumn, boolean bSortOrder)
	{
		m_strSortColumn = strSortColumn;
		m_bSortOrder = bSortOrder;
	}

}
