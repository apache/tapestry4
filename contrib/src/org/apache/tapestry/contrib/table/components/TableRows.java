/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableRowSource;

/**
 * A low level Table component that generates the rows of the current page in the table.
 * This component must be wrapped by {@link org.apache.tapestry.contrib.table.components.TableView}.
 * 
 * <p>
 * The component iterates over the rows of the current page in the table. 
 * The rows are wrapped in 'tr' tags by default. 
 * You can define columns manually within, or
 * you can use {@link org.apache.tapestry.contrib.table.components.TableValues} 
 * to generate the columns automatically.
 * 
 * <p>
 * <table border=1 align="center">
 * <tr>
 *    <th>Parameter</th>
 *    <th>Type</th>
 *    <th>Direction </th>
 *    <th>Required</th>
 *    <th>Default</th>
 *    <th>Description</th>
 * </tr>
 *
 * <tr>
 *  <td>row</td>
 *  <td>Object</td>
 *  <td>out</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The value object of the current row.</td> 
 * </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>tr</td>
 *  <td align="left">The tag to use to wrap the rows in.</td> 
 * </tr>
 *
 * </table> 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class TableRows extends AbstractTableViewComponent implements ITableRowSource
{
    // Binding
    private IBinding m_objElementBinding = null;
    private IBinding m_objRowBinding = null;
    private IBinding m_objValueBinding = null;

    // Transient
    private Object m_objTableRow;

    public Iterator getTableRowsIterator()
    {
        ITableModel objTableModel = getTableModelSource().getTableModel();
        return objTableModel.getCurrentPageRows();
    }

    /**
     * Returns the tableRow.
     * @return Object
     */
    public Object getTableRow()
    {
        return m_objTableRow;
    }

    /**
     * Sets the tableRow.
     * @param tableRow The tableRow to set
     */
    public void setTableRow(Object tableRow)
    {
        m_objTableRow = tableRow;

        IBinding objRowBinding = getRowBinding();
        if (objRowBinding != null)
            objRowBinding.setObject(tableRow);

        IBinding objValueBinding = getValueBinding();
        if (objValueBinding != null)
            objValueBinding.setObject(tableRow);
    }

    /**
     * Returns the valueBinding.
     * @return IBinding
     */
    public IBinding getRowBinding()
    {
        return m_objRowBinding;
    }

    /**
     * Sets the valueBinding.
     * @param valueBinding The valueBinding to set
     */
    public void setRowBinding(IBinding valueBinding)
    {
        m_objRowBinding = valueBinding;
    }

    /**
     * Returns the valueBinding.
     * @return IBinding
     */
    public IBinding getValueBinding()
    {
        return m_objValueBinding;
    }

    /**
     * Sets the valueBinding.
     * @param valueBinding The valueBinding to set
     */
    public void setValueBinding(IBinding valueBinding)
    {
        m_objValueBinding = valueBinding;
    }

    /**
     * Returns the elementBinding.
     * @return IBinding
     */
    public IBinding getElementBinding()
    {
        return m_objElementBinding;
    }

    /**
     * Sets the elementBinding.
     * @param elementBinding The elementBinding to set
     */
    public void setElementBinding(IBinding elementBinding)
    {
        m_objElementBinding = elementBinding;
    }

    /**
     * Returns the element.
     * @return String
     */
    public String getElement()
    {
        IBinding objElementBinding = getElementBinding();
        if (objElementBinding == null || objElementBinding.getObject() == null)
            return "tr";
        return objElementBinding.getString();
    }

    /**
     * @see org.apache.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object objOldValue = cycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, this);

        super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, objOldValue);
    }

}
