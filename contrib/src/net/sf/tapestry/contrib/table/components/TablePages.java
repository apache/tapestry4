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

package net.sf.tapestry.contrib.table.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;

/**
 * @author mindbridge
 * @version $Id$
 *
 */
public class TablePages extends BaseComponent
{
	private final static int DEFAULT_PAGE_COUNT = 7;

	// Bindings    
	private IBinding m_objTableViewBinding = null;
	private IBinding m_objPagesDisplayedBinding = null;

	// Transient
	private int m_nDisplayPage;

	/**
	 * Returns the tableViewBinding.
	 * @return IBinding
	 */
	public IBinding getTableViewBinding()
	{
		return m_objTableViewBinding;
	}

	/**
	 * Sets the tableViewBinding.
	 * @param tableViewBinding The tableViewBinding to set
	 */
	public void setTableViewBinding(IBinding tableViewBinding)
	{
		m_objTableViewBinding = tableViewBinding;
	}

	public TableView getTableView()
	{
		return (TableView) getTableViewBinding().getObject();
	}

	/**
	 * Returns the PagesDisplayedBinding.
	 * @return IBinding
	 */
	public IBinding getPagesDisplayedBinding()
	{
		return m_objPagesDisplayedBinding;
	}

	/**
	 * Sets the PagesDisplayedBinding.
	 * @param PagesDisplayedBinding The PagesDisplayedBinding to set
	 */
	public void setPagesDisplayedBinding(IBinding PagesDisplayedBinding)
	{
		m_objPagesDisplayedBinding = PagesDisplayedBinding;
	}

	public int getPagesDisplayed()
	{
		IBinding objBinding = getPagesDisplayedBinding();
		if (objBinding == null || objBinding.getObject() == null)
			return DEFAULT_PAGE_COUNT;
		return objBinding.getInt();
	}

	/**
	 * Returns the displayPage.
	 * @return int
	 */
	public int getDisplayPage()
	{
		return m_nDisplayPage;
	}

	/**
	 * Sets the displayPage.
	 * @param displayPage The displayPage to set
	 */
	public void setDisplayPage(int displayPage)
	{
		m_nDisplayPage = displayPage;
	}

	public int getCurrentPage()
	{
		return getTableView().getTableModel().getPagingState().getCurrentPage()
			+ 1;
	}

	public void setCurrentPage(int nPage)
	{
		getTableView().getTableModel().getPagingState().setCurrentPage(
			nPage - 1);
	}

	public int getPageCount()
	{
		return getTableView().getTableModel().getPageCount();
	}

	public boolean getCondBack()
	{
		return getCurrentPage() > 1;
	}

	public boolean getCondFwd()
	{
		return getCurrentPage() < getPageCount();
	}

	public boolean getCondCurrent()
	{
		return getDisplayPage() == getCurrentPage();
	}

	public int getStartPage()
	{
		int nCurrent = getCurrentPage();
		int nPagesDisplayed = getPagesDisplayed();

		int nRightMargin = nPagesDisplayed / 2;
		int nStop = nCurrent + nRightMargin;
		int nLastPage = getPageCount();

		int nLeftAddon = 0;
		if (nStop > nLastPage)
			nLeftAddon = nStop - nLastPage;

		int nLeftMargin = (nPagesDisplayed - 1) / 2 + nLeftAddon;
		int nStart = nCurrent - nLeftMargin;
		int nFirstPage = 1;
		if (nStart < nFirstPage)
			nStart = nFirstPage;
		return nStart;
	}

	public int getStopPage()
	{
		int nCurrent = getCurrentPage();
		int nPagesDisplayed = getPagesDisplayed();

		int nLeftMargin = (nPagesDisplayed - 1) / 2;
		int nStart = nCurrent - nLeftMargin;
		int nFirstPage = 1;

		int nRightAddon = 0;
		if (nStart < nFirstPage)
			nRightAddon = nFirstPage - nStart;

		int nRightMargin = nPagesDisplayed / 2 + nRightAddon;
		int nStop = nCurrent + nRightMargin;
		int nLastPage = getPageCount();
		if (nStop > nLastPage)
			nStop = nLastPage;
		return nStop;
	}

	public Integer[] getPageList()
	{
		int nStart = getStartPage();
		int nStop = getStopPage();

		Integer[] arrPages = new Integer[nStop - nStart + 1];
		for (int i = nStart; i <= nStop; i++)
			arrPages[i - nStart] = new Integer(i);

		return arrPages;
	}

	public int getFirstPage()
	{
		return 1;
	}

	public int getLastPage()
	{
		return getPageCount();
	}

	public int getBackPage()
	{
		return getCurrentPage() - 1;
	}

	public int getFwdPage()
	{
		return getCurrentPage() + 1;
	}

	public void changePage(IRequestCycle objCycle)
	{
		Object[] arrParamenters = objCycle.getServiceParameters();
		if (arrParamenters.length != 1
			&& !(arrParamenters[0] instanceof Integer))
		{
			// error
			return;
		}

		setCurrentPage(((Integer) arrParamenters[0]).intValue());
	}
}
