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

package org.apache.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ITableDataModel;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableSessionStateManager;

/**
 * A {@link org.apache.tapestry.contrib.table.model.ITableSessionStateManager} 
 * implementation that saves only the paging and sorting state of the table model 
 * into the session.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSessionStateManager
	implements ITableSessionStateManager
{
	private ITableDataModel m_objDataModel;
	private ITableColumnModel m_objColumnModel;

	public SimpleTableSessionStateManager(
		ITableDataModel objDataModel,
		ITableColumnModel objColumnModel)
	{
		m_objDataModel = objDataModel;
		m_objColumnModel = objColumnModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(ITableModel)
	 */
	public Serializable getSessionState(ITableModel objModel)
	{
		SimpleTableModel objSimpleModel = (SimpleTableModel) objModel;
		return objSimpleModel.getState();
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(Serializable)
	 */
	public ITableModel recreateTableModel(Serializable objState)
	{
		if (objState == null)
			return null;
		SimpleTableState objSimpleState = (SimpleTableState) objState;
		return new SimpleTableModel(
			m_objDataModel,
			m_objColumnModel,
			objSimpleState);
	}

}
