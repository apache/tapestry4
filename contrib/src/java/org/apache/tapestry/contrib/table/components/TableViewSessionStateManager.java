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

package org.apache.tapestry.contrib.table.components;

import java.io.Serializable;

import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableSessionStateManager;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;

/**
 *  Acts like {@link org.apache.tapestry.contrib.table.model.common.FullTableSessionStateManager} 
 *  if the model is provided via the tableModel parameter; 
 *  saves only the model state otherwise. 
 * 
 *  @author mindbridge
 *  @version $Id$
 */
public class TableViewSessionStateManager implements ITableSessionStateManager
{
    private TableView m_objView;

    public TableViewSessionStateManager(TableView objView)
    {
        m_objView = objView;
    }
    
    /**
     * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(org.apache.tapestry.contrib.table.model.ITableModel)
     */
    public Serializable getSessionState(ITableModel objModel)
    {
        // if the model is provided using the 'tableModel' parameter, 
        // emulate FullTableSessionStateManager and save everything
        // (backward compatibility)
        if (m_objView.getCachedTableModelValue() != null)
            return (Serializable) objModel;
            
        // otherwise save only the state
        return new SimpleTableState(objModel.getPagingState(), objModel.getSortingState());
    }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(java.io.Serializable)
     */
    public ITableModel recreateTableModel(Serializable objState)
    {
        // if the state implements ITableModel, return itself
        // (backward compatibility)
        if (objState instanceof ITableModel)
            return (ITableModel) objState;
            
        // otherwise have the component re-generate the model using the provided state
        return m_objView.generateTableModel((SimpleTableState) objState);
    }

}
