// Copyright 2004, 2005 The Apache Software Foundation
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

package org.apache.tapestry.contrib.table.components.inserted;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.contrib.table.components.Table;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.util.ComponentAddress;

/**
 * A component that renders a sorting link - to be used with contrib:Table when 
 * customizing a column's header.
 * 
 * 
 * @author andyhot
 */
public abstract class SimpleTableColumnSortLink extends BaseComponent
        implements ITableRendererListener, PageDetachListener
{
    public abstract Table getTable();
    
    // transient
    private ITableColumn m_objColumn;
    private ITableModelSource m_objModelSource;

    public SimpleTableColumnSortLink()
    {
        init();
    }

    /**
     * @see org.apache.tapestry.event.PageDetachListener#pageDetached(PageEvent)
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
    
     public void prepareForRender(IRequestCycle cycle)
     {
         if (getTable()==null)
            throw Tapestry.createRequiredParameterException(this, "table");
         m_objModelSource = getTable();
         m_objColumn = getTable().getTableColumn();         
     }

    /**
     * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(IRequestCycle,
     *      ITableModelSource, ITableColumn, Object)
     */
    public void initializeRenderer(IRequestCycle objCycle,
            ITableModelSource objSource, ITableColumn objColumn, Object objRow)
    {
        m_objModelSource = objSource;
        m_objColumn = objColumn;
    }

    public Object[] getColumnSelectedParameters()
    {
        return new Object[] { new ComponentAddress(m_objModelSource),
                m_objColumn.getColumnName() };
    }

    public void columnSelected(IRequestCycle objCycle)
    {
        Object[] arrArgs = objCycle.getListenerParameters();
        ComponentAddress objAddr = (ComponentAddress) arrArgs[0];
        String strColumnName = (String) arrArgs[1];

        ITableModelSource objSource = (ITableModelSource) objAddr
                .findComponent(objCycle);
        objSource.storeTableAction(new TableActionColumnSort(strColumnName));
    }

}
