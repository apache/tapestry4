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

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.valid.RenderString;

/**
 * This is a simple implementation of 
 * {@link org.apache.tapestry.contrib.table.model.ITableRendererSource} 
 * that returns a standard renderer of a column value.
 * 
 * This implementation requires that the column passed is of type SimpleTableColumn
 * 
 * @see org.apache.tapestry.contrib.table.model.common.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class SimpleTableValueRendererSource implements ITableRendererSource
{
    /** 
     *  The representation of null values. This is geared towards HTML, but will
     *  work for some other *ML languages as well. In any case, changing the 
     *  column's value renderer allows selecting fully custom rendering behaviour. 
     **/ 
    private static final String EMPTY_REPRESENTATION = "&nbsp;";

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererSource#getRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow)
	{
		SimpleTableColumn objSimpleColumn = (SimpleTableColumn) objColumn;

		Object objValue = objSimpleColumn.getColumnValue(objRow);
		if (objValue == null)
			return new RenderString(EMPTY_REPRESENTATION, true);

		return new RenderString(objValue.toString());
	}

}
