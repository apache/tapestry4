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

import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 * @version $Id$
 * @author mindbridge
 */
public class BasicTableModelWrap extends AbstractTableModel 
{
    private IBasicTableModel m_objBasicTableModel;
    private ITableColumnModel m_objTableColumnModel;

    public BasicTableModelWrap(IBasicTableModel objBasicTableModel, ITableColumnModel objColumnModel)
    {
        this(objBasicTableModel, objColumnModel, new SimpleTableState());
    }

    public BasicTableModelWrap(IBasicTableModel objBasicTableModel, ITableColumnModel objColumnModel, SimpleTableState objState)
    {
        super(objState);
        m_objBasicTableModel = objBasicTableModel;
        m_objTableColumnModel = objColumnModel;
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModel#getColumnModel()
     */
    public ITableColumnModel getColumnModel()
    {
        return m_objTableColumnModel;
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.common.AbstractTableModel#getRowCount()
     */
    protected int getRowCount()
    {
        return m_objBasicTableModel.getRowCount();
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModel#getCurrentPageRows()
     */
    public Iterator getCurrentPageRows()
    {
        int nPageSize = getPagingState().getPageSize();
        if (nPageSize <= 0)
            nPageSize = getRowCount();

        int nCurrentPage = getPagingState().getCurrentPage();
        int nFrom = nCurrentPage * nPageSize;
        
        String strSortColumn = getSortingState().getSortColumn();
        ITableColumn objSortColumn = getColumnModel().getColumn(strSortColumn); 
        boolean bSortOrder = getSortingState().getSortOrder();
        
        return m_objBasicTableModel.getCurrentPageRows(nFrom, nPageSize, objSortColumn, bSortOrder);
    }

}
