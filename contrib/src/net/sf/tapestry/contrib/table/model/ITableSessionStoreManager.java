package net.sf.tapestry.contrib.table.model;

import java.io.Serializable;

import net.sf.tapestry.IRequestCycle;

/**
 * An interface responsible for determining <b>where</b> the session state 
 * will be saved between requests.
 *  
 * @version $Id$
 * @author mindbridge
 */
public interface ITableSessionStoreManager
{
	/**
	 * Method saveState saves the session sate
	 * @param objCycle the current request cycle
	 * @param objState the session state to be saved
	 */
	void saveState(IRequestCycle objCycle, Serializable objState);
	/**
	 * Method loadState loads the session state
	 * @param objCycle the current request cycle
	 * @return Object the loaded sessions state
	 */
	Serializable loadState(IRequestCycle objCycle);
}
