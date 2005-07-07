// Copyright 2004, 2005 The Apache Software Foundation
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
 * @author mindbridge
 */
public class SimpleTableSortingState
	implements ITableSortingState, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String m_strSortColumn;
	private boolean m_bSortOrder;

	public SimpleTableSortingState()
	{
		this(null, ITableSortingState.SORT_ASCENDING); 
	}

	public SimpleTableSortingState(String strSortColumn, boolean bSortOrder)
	{
		m_strSortColumn = strSortColumn; 
		m_bSortOrder = bSortOrder;
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
