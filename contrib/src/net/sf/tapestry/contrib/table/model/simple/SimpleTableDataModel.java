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
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.Tapestry;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal implementation of the ITableDataModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableDataModel implements ITableDataModel, Serializable {
    private Object[] m_arrRows;

    public SimpleTableDataModel(Object[] arrRows)
    {
        m_arrRows = arrRows;
    }
    
    public SimpleTableDataModel(List arrRows)
    {
        this(arrRows.toArray());
    }

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRowCount()
     */
    public int getRowCount() {
        return m_arrRows.length;
    }

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRow(int)
	 */
	public Object getRow(int nRow) {
        if (nRow < 0 || nRow >= m_arrRows.length) {
            // error message
            return null;
        }
        return m_arrRows[nRow];
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRows(int, int)
	 */
	public Iterator getRows(int nFrom, int nTo) {
		return new ArrayIterator(m_arrRows, nFrom, nTo);
	}

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableDataModel#getRows(int, int)
     */
    public Iterator getRows() {
        return getRows(0, m_arrRows.length);
    }

}
