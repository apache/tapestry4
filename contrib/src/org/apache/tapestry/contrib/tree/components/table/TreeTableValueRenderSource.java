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

package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.contrib.table.model.common.ComponentTableRendererSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * @author ceco
 * @version $Id$
 */
public class TreeTableValueRenderSource implements ITableRendererSource
{

	private ComponentTableRendererSource m_objComponentRenderer;
	private ComponentAddress m_objComponentAddress = null;

	public TreeTableValueRenderSource()
	{
		m_objComponentRenderer = null;
	}

	public TreeTableValueRenderSource(ComponentAddress objComponentAddress)
	{
		m_objComponentAddress = objComponentAddress;
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
					
					ComponentAddress objAddress = m_objComponentAddress;
					if(m_objComponentAddress == null)
						objAddress = new ComponentAddress(
							"contrib:TreeTableNodeViewPage",
							"treeTableNodeViewDelegator");
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
