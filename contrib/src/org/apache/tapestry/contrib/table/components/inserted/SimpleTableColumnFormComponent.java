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

package org.apache.tapestry.contrib.table.components.inserted;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.components.TableColumns;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;

/**
 * A component that renders the default column header in a form.
 * 
 * If the current column is sortable, it renders the header as a link.
 * Clicking on the link causes the table to be sorted on that column.
 * Clicking on the link again causes the sorting order to be reversed.
 * 
 * This component renders links that cause the form to be submitted. 
 * This ensures that the updated data in the other form fields is preserved. 
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class SimpleTableColumnFormComponent
	extends BaseComponent
	implements ITableRendererListener
{

    public abstract ITableColumn getTableColumn();
    public abstract void setTableColumn(ITableColumn objColumn);

    public abstract ITableModelSource getTableModelSource();
    public abstract void setTableModelSource(ITableModelSource objSource);

    public abstract String getSelectedColumnName();

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
     */
    public void initializeRenderer(
        IRequestCycle objCycle,
        ITableModelSource objSource,
        ITableColumn objColumn,
        Object objRow)
    {
        setTableModelSource(objSource);
        setTableColumn(objColumn);
    }

	public ITableModel getTableModel()
	{
		return getTableModelSource().getTableModel();
	}

	public boolean getColumnSorted()
	{
		return getTableColumn().getSortable();
	}

	public String getDisplayName()
	{
        ITableColumn objColumn = getTableColumn();
        
        if (objColumn instanceof SimpleTableColumn) {
            SimpleTableColumn objSimpleColumn = (SimpleTableColumn) objColumn;
    		return objSimpleColumn.getDisplayName();
        }
        return objColumn.getColumnName();
	}

	public boolean getIsSorted()
	{
		ITableSortingState objSortingState = getTableModel().getSortingState();
		String strSortColumn = objSortingState.getSortColumn();
		return getTableColumn().getColumnName().equals(strSortColumn);
	}

	public IAsset getSortImage()
	{
		IAsset objImageAsset;

		IRequestCycle objCycle = getPage().getRequestCycle();
		ITableSortingState objSortingState = getTableModel().getSortingState();
		if (objSortingState.getSortOrder()
			== ITableSortingState.SORT_ASCENDING)
		{
			objImageAsset =
				(IAsset) objCycle.getAttribute(
					TableColumns.TABLE_COLUMN_ARROW_UP_ATTRIBUTE);
			if (objImageAsset == null)
				objImageAsset = getAsset("sortUp");
		}
		else
		{
			objImageAsset =
				(IAsset) objCycle.getAttribute(
					TableColumns.TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE);
			if (objImageAsset == null)
				objImageAsset = getAsset("sortDown");
		}

		return objImageAsset;
	}

	public void columnSelected(IRequestCycle objCycle)
	{
        String strColumnName = getSelectedColumnName();
		ITableSortingState objState = getTableModel().getSortingState();
		if (strColumnName.equals(objState.getSortColumn()))
			objState.setSortColumn(strColumnName, !objState.getSortOrder());
		else
			objState.setSortColumn(
				strColumnName,
				ITableSortingState.SORT_ASCENDING);

		// ensure that the change is saved
		getTableModelSource().fireObservedStateChange();
	}

}
