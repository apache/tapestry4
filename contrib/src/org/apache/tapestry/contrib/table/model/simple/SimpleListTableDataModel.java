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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.common.AbstractTableDataModel;
import org.apache.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal list implementation of the 
 * {@link org.apache.tapestry.contrib.table.model.ITableDataModel} interface
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

    public SimpleListTableDataModel(Collection arrRows)
    {
        m_arrRows = new ArrayList(arrRows);
    }

    public SimpleListTableDataModel(Iterator objRows)
    {
        m_arrRows = new ArrayList();
        CollectionUtils.addAll(m_arrRows, objRows);
    }

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRowCount()
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
	 * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRows()
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
