package net.sf.tapestry.contrib.table.model.common;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;

/**
 * A simple ITableSessionStateManager implementation 
 * that saves the entire table model into the session.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class FullTableSessionStateManager implements ITableSessionStateManager
{

    public final static FullTableSessionStateManager FULL_STATE_MANAGER =
        new FullTableSessionStateManager();

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(ITableModel)
	 */
	public Serializable getSessionState(ITableModel objModel)
	{
		return (Serializable) objModel;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(Serializable)
	 */
	public ITableModel recreateTableModel(Serializable objState)
	{
		return (ITableModel) objState;
	}

}
