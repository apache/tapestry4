package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITablePagingState;

/**
 * A minimal implementation of ITablePagingState
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
