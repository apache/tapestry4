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
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.apache.tapestry.contrib.table.model.CTableDataModelEvent;
import org.apache.tapestry.contrib.table.model.common.AbstractTableDataModel;

/**
 * A minimal set implementation of the 
 * {@link org.apache.tapestry.contrib.table.model.ITableDataModel} interface
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
     * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRowCount()
     */
    public int getRowCount()
    {
        return m_setRows.size();
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableDataModel#getRows()
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
