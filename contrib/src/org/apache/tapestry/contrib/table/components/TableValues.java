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

import java.util.Iterator;

import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;

/**
 * A low level Table component that generates the columns in the current row in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableRows}.
 * 
 * <p>
 * The component iterates over the columns in the table and 
 * automatically renders the column values for the current table row. 
 * The columns are wrapped in 'td' tags by default. <br>
 * The column values are rendered using the renderer returned by the 
 * getValueRenderer() method in {@link org.apache.tapestry.contrib.table.model.ITableColumn}.
 * 
 * <p> 
 * Please see the Component Reference for details on how to use this component. 
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.TableValues.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class TableValues extends AbstractTableRowComponent
{
    // Bindings
    public abstract IBinding getColumnBinding();

	// Transient
	private ITableColumn m_objTableColumn;

    /**
     * Get the list of all table columns to be displayed.
     * 
     * @return an iterator of all table columns
     */
	public Iterator getTableColumnIterator()
	{
		ITableColumnModel objColumnModel =
			getTableModelSource().getTableModel().getColumnModel();
		return objColumnModel.getColumns();
	}

    /**
     * Returns the currently rendered table column. 
     * You can call this method to obtain the current column.
     *  
     * @return ITableColumn the current table column
     */
	public ITableColumn getTableColumn()
	{
		return m_objTableColumn;
	}

    /**
     * Sets the currently rendered table column. 
     * This method is for internal use only.
     * 
     * @param tableColumn The current table column
     */
	public void setTableColumn(ITableColumn tableColumn)
	{
		m_objTableColumn = tableColumn;
        
        IBinding objColumnBinding = getColumnBinding();
        if (objColumnBinding != null)
            objColumnBinding.setObject(tableColumn);
	}

    /**
     * Returns the renderer to be used to generate the appearance of the current column
     * 
     * @return the value renderer of the current column
     */
	public IRender getTableValueRenderer()
	{
		Object objRow = getTableRowSource().getTableRow();
		return getTableColumn().getValueRenderer(
			getPage().getRequestCycle(),
			getTableModelSource(),
			objRow);
	}

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        super.renderComponent(writer, cycle);

        // set the current column to null when the component is not active
        m_objTableColumn = null;
    }

}
