//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
