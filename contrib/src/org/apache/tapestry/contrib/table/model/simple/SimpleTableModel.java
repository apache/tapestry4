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

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ITableDataModel;
import org.apache.tapestry.contrib.table.model.ITableDataModelListener;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.common.AbstractTableModel;
import org.apache.tapestry.contrib.table.model.common.ArrayIterator;
import org.apache.tapestry.contrib.table.model.common.ReverseComparator;

/**
 * A simple generic table model implementation.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableModel extends AbstractTableModel implements ITableDataModelListener
{
    private ITableDataModel m_objDataModel = null;
    private Object[] m_arrRows = null;
    private ITableColumnModel m_objColumnModel = null;

    private SimpleTableSortingState m_objLastSortingState;

    public SimpleTableModel(Object[] arrData, ITableColumn[] arrColumns)
    {
        this(new SimpleListTableDataModel(arrData), new SimpleTableColumnModel(arrColumns));
    }

    public SimpleTableModel(Object[] arrData, ITableColumnModel objColumnModel)
    {
        this(new SimpleListTableDataModel(arrData), objColumnModel);
    }

    public SimpleTableModel(ITableDataModel objDataModel, ITableColumnModel objColumnModel)
    {
        this(objDataModel, objColumnModel, new SimpleTableState());
    }

    public SimpleTableModel(ITableDataModel objDataModel, ITableColumnModel objColumnModel, SimpleTableState objState)
    {
        super(objState);
        
        m_arrRows = null;
        m_objColumnModel = objColumnModel;
        m_objLastSortingState = new SimpleTableSortingState();

        setDataModel(objDataModel);
    }

    public ITableColumnModel getColumnModel()
    {
        return m_objColumnModel;
    }

    public Iterator getCurrentPageRows()
    {
        sortRows();

        int nPageSize = getPagingState().getPageSize();
        if (nPageSize <= 0)
            return new ArrayIterator(m_arrRows);

        int nCurrentPage = getPagingState().getCurrentPage();
        int nFrom = nCurrentPage * nPageSize;
        int nTo = (nCurrentPage + 1) * nPageSize;

        return new ArrayIterator(m_arrRows, nFrom, nTo);
    }

    public int getRowCount()
    {
        updateRows();
        return m_arrRows.length;
    }

    private void updateRows()
    {
        // If it is not null, then there is no need to extract the data
        if (m_arrRows != null)
            return;

        // Extract the data from the model
        m_objLastSortingState = new SimpleTableSortingState();

        int nRowCount = m_objDataModel.getRowCount();
        Object[] arrRows = new Object[nRowCount];

        int i = 0;
        for (Iterator it = m_objDataModel.getRows(); it.hasNext();)
            arrRows[i++] = it.next();

        m_arrRows = arrRows;
    }

    protected void sortRows()
    {
        updateRows();

        ITableSortingState objSortingState = getSortingState();

        // see if there is sorting required
        String strSortColumn = objSortingState.getSortColumn();
        if (strSortColumn == null)
            return;

        boolean bSortOrder = objSortingState.getSortOrder();

        // See if the table is already sorted this way. If so, return.
        if (strSortColumn.equals(m_objLastSortingState.getSortColumn())
            && m_objLastSortingState.getSortOrder() == bSortOrder)
            return;

        ITableColumn objColumn = getColumnModel().getColumn(strSortColumn);
        if (objColumn == null || !objColumn.getSortable())
            return;

        Comparator objCmp = objColumn.getComparator();
        if (objCmp == null)
            return;

        // Okay, we have everything in place. Sort the rows.
        if (bSortOrder == ITableSortingState.SORT_DESCENDING)
            objCmp = new ReverseComparator(objCmp);

        Arrays.sort(m_arrRows, objCmp);

        m_objLastSortingState.setSortColumn(strSortColumn, bSortOrder);
    }

    public void tableDataChanged(CTableDataModelEvent objEvent)
    {
        m_arrRows = null;
    }

    /**
     * Returns the dataModel.
     * @return ITableDataModel
     */
    public ITableDataModel getDataModel()
    {
        return m_objDataModel;
    }

    /**
     * Sets the dataModel.
     * @param dataModel The dataModel to set
     */
    public void setDataModel(ITableDataModel dataModel)
    {
        if (m_objDataModel != null)
            m_objDataModel.removeTableDataModelListener(this);
            
        m_objDataModel = dataModel;
        m_objDataModel.addTableDataModelListener(this);
        
        m_arrRows = null;
    }

}
