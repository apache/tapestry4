/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
package net.sf.tapestry.contrib.table.components.inserted;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.contrib.table.components.TableColumns;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableRendererListener;
import net.sf.tapestry.contrib.table.model.ITableSortingState;
import net.sf.tapestry.contrib.table.model.simple.SimpleTableColumn;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableColumnComponent
	extends BaseComponent
	implements ITableRendererListener, PageDetachListener
{
	// transient
	private ITableColumn m_objColumn;
	private ITableModelSource m_objModelSource;

	public SimpleTableColumnComponent()
	{
		init();
	}

	/**
	 * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent arg0)
	{
		init();
	}

	private void init()
	{
		m_objColumn = null;
		m_objModelSource = null;
	}

	/**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
		getPage().addPageDetachListener(this);
	}

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(IRequestCycle, ITableModelSource, ITableColumn, Object)
     */
    public void initializeRenderer(
        IRequestCycle objCycle,
        ITableModelSource objSource,
        ITableColumn objColumn,
        Object objRow)
    {
        m_objModelSource = objSource;
        m_objColumn = objColumn;
    }

	public ITableModel getTableModel()
	{
		return m_objModelSource.getTableModel();
	}

	public boolean getColumnSorted()
	{
		return m_objColumn.getSortable();
	}

	public String getDisplayName()
	{
        if (m_objColumn instanceof SimpleTableColumn) {
            SimpleTableColumn objSimpleColumn = (SimpleTableColumn) m_objColumn;
    		return objSimpleColumn.getDisplayName();
        }
        return m_objColumn.getColumnName();
	}

	public boolean getIsSorted()
	{
		ITableSortingState objSortingState = getTableModel().getSortingState();
		String strSortColumn = objSortingState.getSortColumn();
		return m_objColumn.getColumnName().equals(strSortColumn);
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

	public Object[] getColumnSelectedParameters()
	{
		return new Object[] {
			new ComponentAddress(m_objModelSource),
			m_objColumn.getColumnName()};
	}

	public void columnSelected(IRequestCycle objCycle)
	{
		Object[] arrArgs = objCycle.getServiceParameters();
		ComponentAddress objAddr = (ComponentAddress) arrArgs[0];
		String strColumnName = (String) arrArgs[1];

		ITableModelSource objSource =
			(ITableModelSource) objAddr.findComponent(objCycle);
		ITableModel objModel = objSource.getTableModel();

		ITableSortingState objState = objModel.getSortingState();
		if (strColumnName.equals(objState.getSortColumn()))
			objState.setSortColumn(strColumnName, !objState.getSortOrder());
		else
			objState.setSortColumn(
				strColumnName,
				ITableSortingState.SORT_ASCENDING);

		// ensure that the change is saved
		objSource.fireObservedStateChange();
	}

}
