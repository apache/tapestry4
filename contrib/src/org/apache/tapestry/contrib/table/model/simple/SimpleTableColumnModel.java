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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.common.ArrayIterator;

/**
 * A minimal implementation of the 
 * {@link org.apache.tapestry.contrib.table.model.ITableColumnModel} interface
 * that stores columns as an array.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableColumnModel implements ITableColumnModel, Serializable
{

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

    public int getColumnCount()
    {
        return m_arrColumns.length;
    }

    public ITableColumn getColumn(int nColumn)
    {
        if (nColumn < 0 || nColumn >= m_arrColumns.length)
        {
            // error message
            return null;
        }
        return m_arrColumns[nColumn];
    }

    public ITableColumn getColumn(String strColumn)
    {
        return (ITableColumn) m_mapColumns.get(strColumn);
    }

    public Iterator getColumns()
    {
        return new ArrayIterator(m_arrColumns);
    }

}
