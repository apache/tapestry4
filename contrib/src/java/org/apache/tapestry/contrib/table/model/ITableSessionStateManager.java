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

package org.apache.tapestry.contrib.table.model;

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
