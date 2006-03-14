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
import org.apache.tapestry.contrib.table.model.common.ComponentTableRendererSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * This is a simple implementation of 
 * {@link org.apache.tapestry.contrib.table.model.ITableRendererSource} 
 * that returns a standard renderer of a column header. <p>
 * 
 * This implementation requires that the column passed is of type SimpleTableColumn
 * 
 * @see org.apache.tapestry.contrib.table.model.common.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class SimpleTableColumnFormRendererSource implements ITableRendererSource
{
	private ComponentTableRendererSource m_objComponentRenderer;

	public SimpleTableColumnFormRendererSource()
	{
		m_objComponentRenderer = null;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererSource#getRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow)
	{
			synchronized (this)
			{
				if (m_objComponentRenderer == null)
				{
					ComponentAddress objAddress =
						new ComponentAddress(
							objSource.getNamespace(),
							"SimpleTableColumnPage",
							"tableColumnFormComponent");
					m_objComponentRenderer =
						new ComponentTableRendererSource(objAddress);
				}
			}

		return m_objComponentRenderer.getRenderer(
			objCycle,
			objSource,
			objColumn,
			objRow);
	}
}
