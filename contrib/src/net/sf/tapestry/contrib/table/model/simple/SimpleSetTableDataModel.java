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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.common.AbstractTableDataModel;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * @author mindbridge
 *
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
     * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRows(int, int)
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
