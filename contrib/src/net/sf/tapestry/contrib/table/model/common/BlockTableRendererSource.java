package net.sf.tapestry.contrib.table.model.common;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.components.Block;
import net.sf.tapestry.components.BlockRenderer;
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
	 * @see net.sf.tapestry.contrib.table.model.column.ITableValueRendererSource#getValueRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
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
