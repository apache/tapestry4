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

import org.apache.tapestry.contrib.table.model.ITablePagingState;

/**
 * A minimal implementation of 
 * {@link org.apache.tapestry.contrib.table.model.ITablePagingState}.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTablePagingState implements ITablePagingState, Serializable
{
    private final static int DEFAULT_PAGE_SIZE = 10;

    private int m_nPageSize;
    private int m_nCurrentPage;

    public SimpleTablePagingState()
    {
        m_nPageSize = DEFAULT_PAGE_SIZE;
        m_nCurrentPage = 0;
    }

    /**
     * Returns the pageSize.
     * @return int
     */
    public int getPageSize()
    {
        return m_nPageSize;
    }

    /**
     * Sets the pageSize.
     * @param pageSize The pageSize to set
     */
    public void setPageSize(int pageSize)
    {
        m_nPageSize = pageSize;
    }

    /**
     * Returns the currentPage.
     * @return int
     */
    public int getCurrentPage()
    {
        return m_nCurrentPage;
    }

    /**
     * Sets the currentPage.
     * @param currentPage The currentPage to set
     */
    public void setCurrentPage(int currentPage)
    {
        m_nCurrentPage = currentPage;
    }

}
