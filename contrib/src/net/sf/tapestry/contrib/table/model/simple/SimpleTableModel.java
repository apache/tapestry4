//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITablePagingState;
import net.sf.tapestry.contrib.table.model.ITableSortingState;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;
import net.sf.tapestry.contrib.table.model.common.ReverseComparator;

/**
 * A simple generic table model implementation 
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableModel implements ITableModel, Serializable 
{
    private Object[] m_arrRows;
    private ITableColumnModel m_objColumnModel;
    private SimpleTableState m_objState;

    private SimpleTableSortingState m_objLastSortingState;


    public SimpleTableModel(Object[] arrData, ITableColumn[] arrColumns)
    {
        this(new SimpleTableDataModel(arrData), new SimpleTableColumnModel(arrColumns));
    }

    public SimpleTableModel(ITableDataModel objDataModel, ITableColumnModel objColumnModel)
    {
        this(objDataModel, objColumnModel, new SimpleTableState());
    }

    public SimpleTableModel(ITableDataModel objDataModel, ITableColumnModel objColumnModel, SimpleTableState objState)
    {
        extractRows(objDataModel);
        m_objColumnModel = objColumnModel;
        m_objState = objState;
        m_objLastSortingState = new SimpleTableSortingState();
    }

    public SimpleTableState getState() {
        return m_objState;
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getColumnModel()
	 */
	public ITableColumnModel getColumnModel() {
		return m_objColumnModel;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getCurrentPageRows()
	 */
	public Iterator getCurrentPageRows() {
        sortRows();
        
        int nPageSize = getPagingState().getPageSize();
        int nCurrentPage = getPagingState().getCurrentPage();

        int nFrom = nCurrentPage * nPageSize;
        int nTo = (nCurrentPage + 1) * nPageSize;
        
		return new ArrayIterator(m_arrRows, nFrom, nTo);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModel#getPageCount()
	 */
	public int getPageCount() {
        int nRowCount = getRowCount();
        if (nRowCount == 0) return 1;
		return (nRowCount - 1) / getPagingState().getPageSize() + 1;
	}

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableModel#getPagingState()
     */
    public ITablePagingState getPagingState() {
        return m_objState.getPagingState();
    }

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableModel#getSortingState()
     */
    public ITableSortingState getSortingState() {
        return m_objState.getSortingState();
    }

    public int getRowCount()
    {
        return m_arrRows.length;
    }

    private void extractRows(ITableDataModel objDataModel)
    {
        int nRowCount = objDataModel.getRowCount();
        Object[] arrRows = new Object[nRowCount];

        int i = 0;
        for (Iterator it = objDataModel.getRows(0, nRowCount); it.hasNext();)
            arrRows[i++] = it.next();
            
        m_arrRows = arrRows;
    }

    protected void sortRows()
    {
        ITableSortingState objSortingState = getSortingState();
        
        String strSortColumn = objSortingState.getSortColumn();
        if (strSortColumn == null) return;

        boolean bSortOrder = objSortingState.getSortOrder();

        // See if the table is already sorted this way. If so, return.
        if (strSortColumn.equals(m_objLastSortingState.getSortColumn()) &&
            m_objLastSortingState.getSortOrder() == bSortOrder)
            return;
        
        ITableColumn objColumn = getColumnModel().getColumn(strSortColumn);
        if (objColumn == null || !objColumn.getSortable()) return;

        Comparator objCmp = objColumn.getComparator();
        if (objCmp == null) return;
        
        if (bSortOrder == ITableSortingState.SORT_DESCENDING)
            objCmp = new ReverseComparator(objCmp);
            
        Arrays.sort(m_arrRows, objCmp);
        
        m_objLastSortingState.setSortColumn(strSortColumn, bSortOrder);
    }

}
