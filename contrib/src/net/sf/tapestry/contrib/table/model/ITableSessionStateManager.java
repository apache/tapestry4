package net.sf.tapestry.contrib.table.model;

import java.io.Serializable;

/**
 * An  interface responsible for determining <b>what</b> data would be stored 
 * in the session between requests. 
 * It could be only the table state, it could be entire table including the data,
 * or it could be nothing at all. 
 * It is all determined by the implemention of this interface.
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSessionStateManager
{

	/**
	 * Method getSessionState extracts the "persistent" portion of the table model
	 * @param objModel the table model to extract the session state from
	 * @return Object the session state to be saved between the requests
	 */
	Serializable getSessionState(ITableModel objModel);

	/**
	 * Method recreateTableModel recreates a table model from the saved session state
	 * @param objState the saved session state
	 * @return ITableModel the recreated table model
	 */
	ITableModel recreateTableModel(Serializable objState);
}
