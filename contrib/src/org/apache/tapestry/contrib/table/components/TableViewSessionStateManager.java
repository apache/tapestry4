/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry" 
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache" 
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their 
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

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
