package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableDataModelListener;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITablePagingState;
import net.sf.tapestry.contrib.table.model.ITableSortingState;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;
import net.sf.tapestry.contrib.table.model.common.ReverseComparator;

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
        return (nRowCount - 1) / getPagingState().getPageSize() + 1;
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
