package net.sf.tapestry.contrib.table.components;

import java.util.Iterator;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableRowSource;

/**
 * A low level Table component that generates the rows of the current page in the table.
 * This component must be wrapped by {@link net.sf.tapestry.contrib.table.components.TableView}.
 * 
 * <p>
 * The component iterates over the rows of the current page in the table. 
 * The rows are wrapped in 'tr' tags by default. 
 * You can define columns manually within, or
 * you can use {@link net.sf.tapestry.contrib.table.components.TableValues} 
 * to generate the columns automatically.
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
 *  <td>value</td>
 *  <td>Object</td>
 *  <td>out</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The value object of the current row.</td> 
 * </tr>
 *
 * <tr>
 *  <td>element</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>tr</td>
 *  <td align="left">The tag to use to wrap the rows in.</td> 
 * </tr>
 *
 * </table> 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class TableRows extends AbstractTableViewComponent implements ITableRowSource
{
    // Binding
    private IBinding m_objElementBinding = null;
    private IBinding m_objValueBinding = null;

	// Transient
	private Object m_objTableRow;


	public Iterator getTableRowsIterator() throws RequestCycleException
	{
		ITableModel objTableModel = getTableModelSource().getTableModel();
		return objTableModel.getCurrentPageRows();
	}

	/**
	 * Returns the tableRow.
	 * @return Object
	 */
	public Object getTableRow()
	{
		return m_objTableRow;
	}

	/**
	 * Sets the tableRow.
	 * @param tableRow The tableRow to set
	 */
	public void setTableRow(Object tableRow)
	{
		m_objTableRow = tableRow;
        
        IBinding objValueBinding = getValueBinding();
        if (objValueBinding != null)
            objValueBinding.setObject(tableRow);
	}

    /**
     * Returns the valueBinding.
     * @return IBinding
     */
    public IBinding getValueBinding()
    {
        return m_objValueBinding;
    }

    /**
     * Sets the valueBinding.
     * @param valueBinding The valueBinding to set
     */
    public void setValueBinding(IBinding valueBinding)
    {
        m_objValueBinding = valueBinding;
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
            return "tr";
        return objElementBinding.getString();
    }

    /**
	 * @see net.sf.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
        Object objOldValue = cycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, this);

		super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE, objOldValue);
	}


}
