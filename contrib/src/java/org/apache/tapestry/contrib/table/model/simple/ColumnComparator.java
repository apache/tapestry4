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

import java.util.Comparator;

/**
 * In order to provide more generic behaviour, ITableColumn 
 * has no "column value" concept. The comparator it returns 
 * compares two table rows, rather than values specific to the column. 
 * <p>
 * SimpleTableColumn introduces the concept of "column value" and 
 * allows one to extract that "column value" from the row using 
 * the getColumnValue() method. In practice comparisons are also typically 
 * done between these values rather than the full row objects.
 * <p>
 * This comparator extracts the column values from the rows passed 
 * and uses the provided comparator to compare the values.
 * It therefore allows a comparator designed for comparing column values to be
 * quickly wrapped and used as a comparator comparing rows, which is what
 * ITableColumn is expected to return.
 * <p>
 * Example:
 * <p>
 * objColumn.setComparator(new ColumnComparator(objColumn, objBeanComparator));    
 * 
 * @version $Id$
 * @author mindbridge
 *
 */
public class ColumnComparator implements Comparator
{
    private SimpleTableColumn m_objColumn;
	private Comparator m_objComparator;

	public ColumnComparator(SimpleTableColumn objColumn, Comparator objComparator)
	{
        m_objColumn = objColumn;
		m_objComparator = objComparator;
	}

	/**
	 * @see java.util.Comparator#compare(Object, Object)
	 */
	public int compare(Object objRow1, Object objRow2)
	{
        Object objValue1 = m_objColumn.getColumnValue(objRow1);
        Object objValue2 = m_objColumn.getColumnValue(objRow2);
                
		return m_objComparator.compare(objValue1, objValue2);
	}

}
