//
// Tapestry Web Application Framework
// Copyright (c) 2000-2002 by Howard Lewis Ship
//
// Howard Lewis Ship
// http://sf.net/projects/tapestry
// mailto:hship@users.sf.net
//
// This library is free software.
//
// You may redistribute it and/or modify it under the terms of the GNU
// Lesser General Public License as published by the Free Software Foundation.
//
// Version 2.1 of the license should be included with this distribution in
// the file LICENSE, as well as License.html. If the license is not
// included with this distribution, you may find a copy at the FSF web
// site at 'www.gnu.org' or 'www.fsf.org', or you may write to the
// Free Software Foundation, 675 Mass Ave, Cambridge, MA 02139 USA.
//
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied waranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//

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
