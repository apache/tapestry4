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
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.Tapestry;
import org.apache.tapestry.contrib.table.components.Table;
import org.apache.tapestry.contrib.table.components.TableColumns;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;
import org.apache.tapestry.contrib.table.model.ITableSortingState;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * A component that renders the proper sort image for the current column - to be 
 * used with contrib:Table when customizing a column's header.
 * 
 * @author Andreas Andreou
 */
public abstract class SimpleTableColumnSortImage extends BaseComponent
        implements PageDetachListener, ITableRendererListener
{
    // transient
    private ITableModelSource m_objModelSource;
    private ITableColumn m_objColumn;
    
    public SimpleTableColumnSortImage()
    {
        init();
    }

    public abstract Table getTable();
    
    /**
     * @see org.apache.tapestry.event.PageDetachListener#pageDetached(PageEvent)
     */
    public void pageDetached(PageEvent arg0)
    {
        init();
    }

    private void init()
    {
        m_objModelSource = null;
        m_objColumn = null;
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
    
     public void prepareForRender(IRequestCycle cycle)
     {         
         if (getTable()==null)
            throw Tapestry.createRequiredParameterException(this, "table");
         
         m_objModelSource = getTable();
         m_objColumn = getTable().getTableColumn();
                  
     }
         

    public ITableModel getTableModel()
    {
        return m_objModelSource.getTableModel();
    }

    public IAsset getSortImage()
    {
        IAsset objImageAsset;

        IRequestCycle objCycle = getPage().getRequestCycle();
        ITableSortingState objSortingState = getTableModel().getSortingState();
        if (objSortingState.getSortOrder() == ITableSortingState.SORT_ASCENDING)
        {
            objImageAsset = (IAsset) objCycle
                    .getAttribute(TableColumns.TABLE_COLUMN_ARROW_UP_ATTRIBUTE);
            if (objImageAsset == null) objImageAsset = getAsset("sortUp");
        }
        else
        {
            objImageAsset = (IAsset) objCycle
                    .getAttribute(TableColumns.TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE);
            if (objImageAsset == null) objImageAsset = getAsset("sortDown");
        }

        return objImageAsset;
    }
    
    public boolean getIsSorted()
    {
        ITableSortingState objSortingState = getTableModel().getSortingState();
        String strSortColumn = objSortingState.getSortColumn();
        return m_objColumn.getColumnName().equals(strSortColumn);
    }    
}
