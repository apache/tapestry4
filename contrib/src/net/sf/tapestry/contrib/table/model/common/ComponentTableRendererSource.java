package net.sf.tapestry.contrib.table.model.common;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableRendererListener;
import net.sf.tapestry.contrib.table.model.ITableRendererSource;

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
	 * @see net.sf.tapestry.contrib.table.model.column.ITableValueRendererSource#getValueRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
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
