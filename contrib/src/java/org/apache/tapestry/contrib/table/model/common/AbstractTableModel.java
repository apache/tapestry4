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

package org.apache.tapestry.contrib.table.model.common;

import java.io.Serializable;

import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITablePagingState;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 * A base table model that implements the handling of the model state.
 * Used by most standard ITableModel implementations.
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class AbstractTableModel implements ITableModel, Serializable
{
    private SimpleTableState m_objTableState;

    protected AbstractTableModel()
    {
        this(new SimpleTableState());
    }

    protected AbstractTableModel(SimpleTableState objTableState)
    {
        m_objTableState = objTableState;
    }
    
    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModel#getPagingState()
     */
    public ITablePagingState getPagingState()
    {
        return getState().getPagingState();
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModel#getSortingState()
     */
    public ITableSortingState getSortingState()
    {
        return getState().getSortingState();
    }

    /**
     * Returns the tableState.
     * @return SimpleTableState
     */
    public SimpleTableState getState()
    {
        return m_objTableState;
    }

    protected abstract int getRowCount();
    
    public int getPageCount()
    {
        int nRowCount = getRowCount();
        if (nRowCount == 0)
            return 1;

        int nPageSize = getPagingState().getPageSize();
        if (nPageSize <= 0)
            return 1;

        return (nRowCount - 1) / nPageSize + 1;
    }
    
}
