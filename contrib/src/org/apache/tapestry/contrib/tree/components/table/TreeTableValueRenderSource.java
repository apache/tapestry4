package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererSource;
import org.apache.tapestry.contrib.table.model.common.ComponentTableRendererSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 8, 2003
 * 
 * @author ceco
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
		if (m_objComponentRenderer == null)
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
		}

		return m_objComponentRenderer.getRenderer(
			objCycle,
			objSource,
			objColumn,
			objRow);
	}

}
