package net.sf.tapestry.contrib.table.model.simple;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableRendererSource;
import net.sf.tapestry.contrib.table.model.common.ComponentTableRendererSource;

/**
 * This is a simple implementation of 
 * {@link net.sf.tapestry.contrib.table.model.ITableRendererSource} 
 * that returns a standard renderer of a column header. <p>
 * 
 * This implementation requires that the column passed is of type SimpleTableColumn
 * 
 * @see net.sf.tapestry.contrib.table.model.common.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public class SimpleTableColumnRendererSource implements ITableRendererSource
{
	private ComponentTableRendererSource m_objComponentRenderer;

	public SimpleTableColumnRendererSource()
	{
		m_objComponentRenderer = null;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableRendererSource#getRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
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
					ComponentAddress objAddress =
						new ComponentAddress(
							objSource.getNamespace(),
							"SimpleTableColumnPage",
							"tableColumnComponent");
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
