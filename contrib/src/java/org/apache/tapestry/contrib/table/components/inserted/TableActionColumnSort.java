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
package org.apache.tapestry.contrib.table.components.inserted;

import org.apache.tapestry.contrib.table.model.ITableAction;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableSortingState;


/**
 * <p>A table action that configures the table sorting to use a given column</p>
 *
 * @author teo
 */
public class TableActionColumnSort implements ITableAction
{
    private String m_strColumnName;
    
    public TableActionColumnSort(String name)
    {
        m_strColumnName = name;
    }

    /**
     * {@inheritDoc}
     */
    public void executeTableAction(ITableModel objTableModel)
    {
        ITableSortingState objState = objTableModel.getSortingState();
        if (m_strColumnName.equals(objState.getSortColumn()))
            objState.setSortColumn(m_strColumnName, !objState.getSortOrder());
        else objState.setSortColumn(m_strColumnName, ITableSortingState.SORT_ASCENDING);
    }
}
