package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableDataModelListener;
import net.sf.tapestry.contrib.table.model.common.AbstractTableDataModel;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal implementation of the ITableDataModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleListTableDataModel extends AbstractTableDataModel implements Serializable
{
	private List m_arrRows;

	public SimpleListTableDataModel(Object[] arrRows)
	{
		this(Arrays.asList(arrRows));
	}

	public SimpleListTableDataModel(List arrRows)
	{
		m_arrRows = arrRows;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRowCount()
	 */
	public int getRowCount()
	{
		return m_arrRows.size();
	}

	/**
	 * Returns the row element at the given position
     * @param nRow the index of the element to return
	 */
	public Object getRow(int nRow)
	{
		if (nRow < 0 || nRow >= m_arrRows.size())
		{
			// error message
			return null;
		}
		return m_arrRows.get(nRow);
	}

	/**
	 * Returns an Iterator with the elements from the given range
     * @param nFrom the start of the range (inclusive)
     * @param nTo the stop of the range (exclusive)
	 */
	public Iterator getRows(int nFrom, int nTo)
	{
		return new ArrayIterator(m_arrRows.toArray(), nFrom, nTo);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRows()
	 */
	public Iterator getRows()
	{
		return m_arrRows.iterator();
	}

	/**
	 * Method addRow.
     * Adds a row object to the model at its end
	 * @param objRow the row object to add
	 */
	public void addRow(Object objRow)
	{
		m_arrRows.add(objRow);

		CTableDataModelEvent objEvent = new CTableDataModelEvent();
		fireTableDataModelEvent(objEvent);
	}

    public void addRows(Collection arrRows)
    {
        m_arrRows.addAll(arrRows);

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
		m_arrRows.remove(objRow);

		CTableDataModelEvent objEvent = new CTableDataModelEvent();
		fireTableDataModelEvent(objEvent);
	}

    public void removeRows(Collection arrRows)
    {
        m_arrRows.removeAll(arrRows);

        CTableDataModelEvent objEvent = new CTableDataModelEvent();
        fireTableDataModelEvent(objEvent);
    }

}
