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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.tapestry.Tapestry;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal implementation of the ITableColumnModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableColumnModel implements ITableColumnModel, Serializable {

    private ITableColumn[] m_arrColumns;
    private Map m_mapColumns;

    public SimpleTableColumnModel(ITableColumn[] arrColumns)
    {
        m_arrColumns = arrColumns;

        m_mapColumns = new HashMap();
        for (int i = 0; i < m_arrColumns.length; i++) 
            m_mapColumns.put(m_arrColumns[i].getColumnName(), m_arrColumns[i]);
    }
    
    public SimpleTableColumnModel(List arrColumns)
    {
        this((ITableColumn[]) arrColumns.toArray(new ITableColumn[arrColumns.size()]));
    }

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableColumnModel#getColumnCount()
     */
    public int getColumnCount() {
        return m_arrColumns.length;
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableColumnModel#getColumn(int)
	 */
	public ITableColumn getColumn(int nColumn) {
        if (nColumn < 0 || nColumn >= m_arrColumns.length) {
            // error message
            return null;
        }
		return m_arrColumns[nColumn];
	}

    public ITableColumn getColumn(String strColumn) {
        return (ITableColumn) m_mapColumns.get(strColumn);
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableColumnModel#getColumns()
	 */
	public Iterator getColumns() {
		return new ArrayIterator(m_arrColumns);
	}

}
