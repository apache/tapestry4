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

package org.apache.tapestry.contrib.table.model.common;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * 
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class ComponentTableRendererSource implements ITableRendererSource
{
	private ComponentAddress m_objComponentAddress;

	public ComponentTableRendererSource(ITableRendererListener objComponent)
	{
		this(new ComponentAddress(objComponent));
	}

	public ComponentTableRendererSource(ComponentAddress objComponentAddress)
	{
		setComponentAddress(objComponentAddress);
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
		ITableRendererListener objComponent =
			(ITableRendererListener) getComponentAddress().findComponent(
				objCycle);

		objComponent.initializeRenderer(objCycle, objSource, objColumn, objRow);

		return objComponent;
	}

	/**
	 * Returns the listenerAddress.
	 * @return ComponentAddress
	 */
	public ComponentAddress getComponentAddress()
	{
		return m_objComponentAddress;
	}

	/**
	 * Sets the listenerAddress.
	 * @param listenerAddress The listenerAddress to set
	 */
	public void setComponentAddress(ComponentAddress listenerAddress)
	{
		m_objComponentAddress = listenerAddress;
	}

}
