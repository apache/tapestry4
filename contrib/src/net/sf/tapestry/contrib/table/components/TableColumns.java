package net.sf.tapestry.contrib.table.components;

import java.util.Iterator;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IAsset;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRender;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableColumn;
import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;

/**
 * A low level Table component that renders the column headers in the table.
 * This component must be wrapped by {@link net.sf.tapestry.contrib.table.components.TableView}.
 * <p>
 * The component iterates over all column objects in the
 * {@link net.sf.tapestry.contrib.table.model.ITableColumnModel} and renders
 * a header for each one of them using the renderer provided by the
 * getColumnRender() method in {@link net.sf.tapestry.contrib.table.model.ITableColumn}.
 * The headers are wrapped in 'th' tags by default.
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
 *  <td>column</td>
 *  <td>{@link net.sf.tapestry.contrib.table.model.ITableColumn}</td>
 *  <td>out</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The object representing the current column.</td> 
 * </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>th</td>
 *  <td align="left">The tag to use to wrap the column headers.</td> 
 * </tr>
 *
 * <tr>
 *  <td>arrowUpAsset</td>
 *  <td>{@link net.sf.tapestry.IAsset}</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The image to use to describe a column sorted in an ascending order.</td> 
 * </tr>
 *
 * <tr>
 *  <td>arrowDownAsset</td>
 *  <td>{@link net.sf.tapestry.IAsset}</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The image to use to describe a column sorted in a descending order.</td> 
 * </tr>
 *
 * </table> 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class TableColumns extends AbstractTableViewComponent implements PageDetachListener
{
    public static final String TABLE_COLUMN_ARROW_UP_ATTRIBUTE = 
        "net.sf.tapestry.contrib.table.components.TableColumns.arrowUp";

    public static final String TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE = 
        "net.sf.tapestry.contrib.table.components.TableColumns.arrowDown";
    
    // Bindings (custom)
    private IBinding m_objColumnBinding = null;
    private IBinding m_objElementBinding = null;

    // Bindings (in)
    private IAsset m_objArrowUpAsset;
    private IAsset m_objArrowDownAsset;

	// Transient
	private ITableColumn m_objTableColumn;

    public TableColumns()
    {
        initialize();
    }

    /**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
	}

    /**
	 * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent event)
	{
        initialize();
	}
    
    protected void initialize()
    {
        m_objArrowUpAsset = null;
        m_objArrowDownAsset = null;
    }

	public Iterator getTableColumnIterator() throws RequestCycleException
	{
		ITableColumnModel objColumnModel =
			getTableModelSource().getTableModel().getColumnModel();
		return objColumnModel.getColumns();
	}

	/**
	 * Returns the tableColumn.
	 * @return ITableColumn
	 */
	public ITableColumn getTableColumn()
	{
		return m_objTableColumn;
	}

	/**
	 * Sets the tableColumn.
	 * @param tableColumn The tableColumn to set
	 */
	public void setTableColumn(ITableColumn tableColumn)
	{
		m_objTableColumn = tableColumn;

        IBinding objColumnBinding = getColumnBinding();
        if (objColumnBinding != null)
            objColumnBinding.setObject(tableColumn);
	}

	public IRender getTableColumnRenderer() throws RequestCycleException
	{
		return getTableColumn().getColumnRenderer(
			getPage().getRequestCycle(),
			getTableModelSource());
	}

    /**
     * Returns the valueBinding.
     * @return IBinding
     */
    public IBinding getColumnBinding()
    {
        return m_objColumnBinding;
    }

    /**
     * Sets the valueBinding.
     * @param valueBinding The valueBinding to set
     */
    public void setColumnBinding(IBinding valueBinding)
    {
        m_objColumnBinding = valueBinding;
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
            return "th";
        return objElementBinding.getString();
    }

	/**
	 * Returns the arrowDownAsset.
	 * @return IAsset
	 */
	public IAsset getArrowDownAsset()
	{
		return m_objArrowDownAsset;
	}

	/**
	 * Returns the arrowUpAsset.
	 * @return IAsset
	 */
	public IAsset getArrowUpAsset()
	{
		return m_objArrowUpAsset;
	}

	/**
	 * Sets the asset to use to render an image describing a column 
     * sorted in a descending order.
	 * @param arrowDownAsset The asset of a 'down' arrow image
	 */
	public void setArrowDownAsset(IAsset arrowDownAsset)
	{
		m_objArrowDownAsset = arrowDownAsset;
	}

	/**
     * Sets the asset to use to render an image describing a column 
     * sorted in an ascending order.
	 * @param arrowUpAsset The asset of an 'up' arrow image
	 */
	public void setArrowUpAsset(IAsset arrowUpAsset)
	{
		m_objArrowUpAsset = arrowUpAsset;
	}


    /**
	 * @see net.sf.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
        Object oldValueUp = cycle.getAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE);
        Object oldValueDown = cycle.getAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE);

        cycle.setAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE, getArrowUpAsset());
        cycle.setAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE, getArrowDownAsset());

		super.renderComponent(writer, cycle);
        
        cycle.setAttribute(TABLE_COLUMN_ARROW_UP_ATTRIBUTE, oldValueUp);
        cycle.setAttribute(TABLE_COLUMN_ARROW_DOWN_ATTRIBUTE, oldValueDown);
        
	}

}
