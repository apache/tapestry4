package net.sf.tapestry.contrib.table.components;

import net.sf.tapestry.ComponentAddress;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableModelSource;

/**
 * A low level Table component that renders the pages in the table.
 * This component must be wrapped by {@link net.sf.tapestry.contrib.table.components.TableView}.
 * <p>
 * The component generates a list of pages in the Table centered around the 
 * current one and allows you to navigate to other pages.
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
 *  <td>pagesDisplayed</td>
 *  <td>int</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>7</td>
 *  <td align="left">Determines the maximum number of pages to be displayed in the page list
 *      when the table has more than one page.
 *      <p>For example, if the table has 20 pages, and 10 is the current page,
 *      pages from 7 to 13 in the page list will be shown if this parameter has 
 *      a value of 7.
 *  </td> 
 * </tr>
 *
 * </table> 
 * 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class TablePages extends AbstractTableViewComponent
{
	private final static int DEFAULT_PAGE_COUNT = 7;

	// Bindings    
	private IBinding m_objPagesDisplayedBinding = null;

	// Transient
	private int m_nDisplayPage;

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

	public int getCurrentPage() throws RequestCycleException
	{
		return getTableModelSource().getTableModel().getPagingState().getCurrentPage()
			+ 1;
	}

	public int getPageCount() throws RequestCycleException
	{
		return getTableModelSource().getTableModel().getPageCount();
	}

	public boolean getCondBack() throws RequestCycleException
	{
		return getCurrentPage() > 1;
	}

	public boolean getCondFwd() throws RequestCycleException
	{
		return getCurrentPage() < getPageCount();
	}

	public boolean getCondCurrent() throws RequestCycleException
	{
		return getDisplayPage() == getCurrentPage();
	}

	public int getStartPage() throws RequestCycleException
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

	public int getStopPage() throws RequestCycleException
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

	public Integer[] getPageList() throws RequestCycleException
	{
		int nStart = getStartPage();
		int nStop = getStopPage();

		Integer[] arrPages = new Integer[nStop - nStart + 1];
		for (int i = nStart; i <= nStop; i++)
			arrPages[i - nStart] = new Integer(i);

		return arrPages;
	}

	public Object[] getFirstPageContext() throws RequestCycleException
	{
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
		return new Object[] { objAddress, new Integer(1) };
	}

	public Object[] getLastPageContext() throws RequestCycleException
	{
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getPageCount()) };
	}

	public Object[] getBackPageContext() throws RequestCycleException
	{
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getCurrentPage() - 1) };
	}

	public Object[] getFwdPageContext() throws RequestCycleException
	{
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(getCurrentPage() + 1) };
	}

    public Object[] getDisplayPageContext() throws RequestCycleException
    {
        ComponentAddress objAddress = new ComponentAddress(getTableModelSource());
        return new Object[] { objAddress, new Integer(m_nDisplayPage) };
    }

	public void changePage(IRequestCycle objCycle) throws RequestCycleException
	{
		Object[] arrParameters = objCycle.getServiceParameters();
		if (arrParameters.length != 2
            && !(arrParameters[0] instanceof ComponentAddress)
			&& !(arrParameters[1] instanceof Integer))
		{
			// error
			return;
		}

        ComponentAddress objAddress = (ComponentAddress) arrParameters[0];
        ITableModelSource objSource = (ITableModelSource) objAddress.findComponent(objCycle);
		setCurrentPage(objSource, ((Integer) arrParameters[1]).intValue());

        // ensure that the change is saved
        objSource.fireObservedStateChange();
	}

    public void setCurrentPage(ITableModelSource objSource, int nPage)  throws RequestCycleException
    {
        objSource.getTableModel().getPagingState().setCurrentPage(
            nPage - 1);
    }

}
