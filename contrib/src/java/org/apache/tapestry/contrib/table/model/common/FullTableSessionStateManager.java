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

package org.apache.tapestry.contrib.table.model.common;

import java.io.Serializable;

import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableSessionStateManager;

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
	 * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(ITableModel)
	 */
	public Serializable getSessionState(ITableModel objModel)
	{
		return (Serializable) objModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(Serializable)
	 */
	public ITableModel recreateTableModel(Serializable objState)
	{
		return (ITableModel) objState;
	}

}
