package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.common.AbstractTableDataModel;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal set implementation of the 
 * {@link net.sf.tapestry.contrib.table.model.ITableDataModel} interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleSetTableDataModel extends AbstractTableDataModel implements Serializable
{
    private Set m_setRows;

    public SimpleSetTableDataModel(Set setRows)
    {
        m_setRows = setRows;
    }

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRowCount()
     */
    public int getRowCount()
    {
        return m_setRows.size();
    }

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRows()
     */
    public Iterator getRows()
    {
        return m_setRows.iterator();
    }

    /**
     * Method addRow.
     * Adds a row object to the model at its end
     * @param objRow the row object to add
     */
    public void addRow(Object objRow)
    {
        if (m_setRows.contains(objRow)) return;
        m_setRows.add(objRow);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

    public void addRows(Collection arrRows)
    {
        m_setRows.addAll(arrRows);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

    /**
     * Method removeRow.
     * Removes a row object from the model
     * @param objRow the row object to remove
     */
    public void removeRow(Object objRow)
    {
        if (!m_setRows.contains(objRow)) return;
        m_setRows.remove(objRow);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

    public void removeRows(Collection arrRows)
    {
        m_setRows.removeAll(arrRows);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

}
