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

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;

/**
 * The facade component in the Table family. Table allows you to present 
 * a sortable and pagable table simply and easily by using only this one component.
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.Table.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class Table extends BaseComponent implements ITableModelSource
{
    /**
     * @see org.apache.tapestry.contrib.table.model.ITableModelSource#getTableModel()
     */
    public ITableModel getTableModel()
    {
        return getTableViewComponent().getTableModel();
    }

    /**
     * Indicates that the table model has changed and it may need to saved.
     * This method has to be invoked if modifications are made to the model.
     *  
     * @see org.apache.tapestry.contrib.table.model.ITableModelSource#fireObservedStateChange()
     */
    public void fireObservedStateChange()
    {
        getTableViewComponent().fireObservedStateChange();
    }

    /**
     * Resets the state of the component and forces it to load a new
     * TableModel from the tableModel binding the next time it renders.
     */
    public void reset()
    {
        getTableViewComponent().reset();
    }

    /**
     * Returns the currently rendered table column. 
     * You can call this method to obtain the current column.
     *  
     * @return ITableColumn the current table column
     */
    public ITableColumn getTableColumn()
    {
        Object objCurrentRow = getTableRow();

        // if the current row is null, then we are most likely rendering TableColumns
        if (objCurrentRow == null)
            return getTableColumnsComponent().getTableColumn();
        else
            return getTableValuesComponent().getTableColumn();
    }

    /**
     * Returns the currently rendered table row or null 
     * if the rows are not rendered at the moment.
     * You can call this method to obtain the current row.
     *  
     * @return Object the current table row 
     */
    public Object getTableRow()
    {
        return getTableRowsComponent().getTableRow();
    }

    protected TableView getTableViewComponent()
    {
        return (TableView) getComponent("tableView");
    }

    protected TableColumns getTableColumnsComponent()
    {
        return (TableColumns) getComponent("tableColumns");
    }

    protected TableRows getTableRowsComponent()
    {
        return (TableRows) getComponent("tableRows");
    }

    protected TableValues getTableValuesComponent()
    {
        return (TableValues) getComponent("tableValues");
    }
}
