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

import org.apache.tapestry.contrib.table.model.ITablePagingState;
import org.apache.tapestry.contrib.table.model.ITableSortingState;

/**
 * A container holding all of the table model states.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableState implements Serializable
{
	private ITablePagingState m_objPagingState;
	private ITableSortingState m_objSortingState;

	public SimpleTableState()
	{
		this(new SimpleTablePagingState(), new SimpleTableSortingState());
	}

	public SimpleTableState(
		ITablePagingState objPagingState,
		ITableSortingState objSortingState)
	{
		m_objPagingState = objPagingState;
		m_objSortingState = objSortingState;
	}

	/**
	 * Returns the pagingState.
	 * @return ITablePagingState
	 */
	public ITablePagingState getPagingState()
	{
		return m_objPagingState;
	}

	/**
	 * Returns the sortingState.
	 * @return ITableSortingState
	 */
	public ITableSortingState getSortingState()
	{
		return m_objSortingState;
	}

}
