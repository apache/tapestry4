package net.sf.tapestry.contrib.table.model.common;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;

/**
 * A simple ITableSessionStateManager implementation 
 * that saves nothing at all into the session.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class NullTableSessionStateManager implements ITableSessionStateManager
{
    
    public final static NullTableSessionStateManager NULL_STATE_MANAGER =
        new NullTableSessionStateManager();

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(ITableModel)
	 */
	public Serializable getSessionState(ITableModel objModel)
	{
		return null;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(Serializable)
	 */
	public ITableModel recreateTableModel(Serializable objState)
	{
		return null;
	}

}
