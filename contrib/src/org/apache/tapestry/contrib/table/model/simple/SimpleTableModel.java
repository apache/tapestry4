/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ITableDataModel;
import org.apache.tapestry.contrib.table.model.ITableDataModelListener;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITablePagingState;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.common.ArrayIterator;
import org.apache.tapestry.contrib.table.model.common.ReverseComparator;

/**
 * A simple generic table model implementation.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableModel implements ITableModel, ITableDataModelListener, Serializable
{
    private ITableDataModel m_objDataModel;
    private Object[] m_arrRows;
    private ITableColumnModel m_objColumnModel;
    private SimpleTableState m_objState;

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
        m_arrRows = null;
        m_objColumnModel = objColumnModel;
        m_objState = objState;
        m_objLastSortingState = new SimpleTableSortingState();

        m_objDataModel = objDataModel;
        m_objDataModel.addTableDataModelListener(this);
    }

    public SimpleTableState getState()
    {
        return m_objState;
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

    public ITablePagingState getPagingState()
    {
        return m_objState.getPagingState();
    }

    public ITableSortingState getSortingState()
    {
        return m_objState.getSortingState();
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
        m_objDataModel = dataModel;
        m_arrRows = null;
    }

}
