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
import org.apache.tapestry.components.Block;
import org.apache.tapestry.components.BlockRenderer;
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
public class BlockTableRendererSource implements ITableRendererSource
{
	private ComponentAddress m_objBlockAddress;
	private ComponentAddress m_objListenerAddress;

	public BlockTableRendererSource(Block objBlock)
	{
		this(new ComponentAddress(objBlock));
	}

	public BlockTableRendererSource(
		Block objBlock,
		ITableRendererListener objListener)
	{
		this(new ComponentAddress(objBlock), new ComponentAddress(objListener));
	}

	public BlockTableRendererSource(ComponentAddress objBlockAddress)
	{
		this(objBlockAddress, null);
	}

	public BlockTableRendererSource(
		ComponentAddress objBlockAddress,
		ComponentAddress objListenerAddress)
	{
		setBlockAddress(objBlockAddress);
		setListenerAddress(objListenerAddress);
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
		ComponentAddress objListenerAddress = getListenerAddress();
		if (objListenerAddress != null)
		{
			ITableRendererListener objListener =
				(ITableRendererListener) objListenerAddress.findComponent(
					objCycle);
			objListener.initializeRenderer(
				objCycle,
				objSource,
				objColumn,
				objRow);
		}

		Block objBlock = (Block) getBlockAddress().findComponent(objCycle);
		return new BlockRenderer(objBlock);
	}

	/**
	 * Returns the blockAddress.
	 * @return ComponentAddress
	 */
	public ComponentAddress getBlockAddress()
	{
		return m_objBlockAddress;
	}

	/**
	 * Sets the blockAddress.
	 * @param blockAddress The blockAddress to set
	 */
	public void setBlockAddress(ComponentAddress blockAddress)
	{
		m_objBlockAddress = blockAddress;
	}

	/**
	 * Returns the listenerAddress.
	 * @return ComponentAddress
	 */
	public ComponentAddress getListenerAddress()
	{
		return m_objListenerAddress;
	}

	/**
	 * Sets the listenerAddress.
	 * @param listenerAddress The listenerAddress to set
	 */
	public void setListenerAddress(ComponentAddress listenerAddress)
	{
		m_objListenerAddress = listenerAddress;
	}

}
