package net.sf.tapestry.contrib.table.components;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;

/**
 * The facade component in the Table family. Table allows you to present 
 * a sortable and pagable table simply and easily by using only this one component.
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.Table.html">Component Reference</a>]
 * 
 * <p>
 * The Table component allows you to manipulate its appearance by allowing you 
 * to define the 'class' attributes of its internal elements. 
 * If you want to change the structure of the table, however,
 * you can instead build your own using the lower level components
 * {@link TableView}, {@link TablePages}, {@link TableColumns}, {@link TableRows},
 * and {@link TableValues}.
 * <p>
 * The Table component delegates the handling of the table model and related
 * activities to the {@link TableView}, and more detailed information about the
 * process can be found in the documentation of that class.
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
 *  <td>tableModel</td>
 *  <td>{@link net.sf.tapestry.contrib.table.model.ITableModel}</td>
 *  <td>in</td>
 *  <td>yes</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The TableModel to be used to render the table. 
 *      This binding is typically used only once at the beginning and then the 
 *      component stores the model in the session state. 
 *      <p>If you want the Table to read the model every time you can use
 *      a session state manager such as 
 *      {@link net.sf.tapestry.contrib.table.model.common.NullTableSessionStateManager}
 *      that will force it to get the TableModel from this binding every time.
 *      If you do this, however, you will be responsible for saving the state of 
 *      the table yourself.
 *      <p> You can also call the reset() method to force the Table to abandon
 *      its old model and reload a new one.
 *  </td> 
 * </tr>
 *
 * <tr>
 *  <td>tableSessionStateManager</td>
 *  <td>{@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager}</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>{@link net.sf.tapestry.contrib.table.model.common.FullTableSessionStateManager}</td>
 *  <td align="left">This is the session state manager that will control what part of the 
 *      table model will be saved in the session state. 
 *      It is then used to recreate the table model from
 *      using what was saved in the session. By default, the 
 *      {@link net.sf.tapestry.contrib.table.model.common.FullTableSessionStateManager}
 *      is used, which just saves the entire model into the session.
 *      This behaviour may not be appropriate when the data is a lot or it is not
 *      {@link java.io.Serializable}.
 *      <p> You can use one of the stock implementations of  
 *      {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager}
 *      to determine the session state behaviour, or you can just define your own.
 *  </td> 
 * </tr>
 *
 * <tr>
 *  <td>tableSessionStoreManager</td>
 *  <td>{@link net.sf.tapestry.contrib.table.model.ITableSessionStoreManager}</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>null</td>
 *  <td align="left">Determines how the session state (returned by the session state manager)
 *      will be saved in the session. If this parameter is null, then the state
 *      will be saved as a persistent property. If it is not null, then the methods
 *      of the interface will be used to save and load the state.
 *  </td> 
 * </tr>
 *
 * <tr>
 *  <td>row</td>
 *  <td>Object</td>
 *  <td>out</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The value object of the current row.</td> 
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
 * <tr>
 *  <td>pagesClass</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The CSS class of the table pages.</td> 
 * </tr>
 *
 * <tr>
 *  <td>columnsClass</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The CSS class of the table columns.</td> 
 * </tr>
 *
 * <tr>
 *  <td>rowsClass</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The CSS class of the table rows.</td> 
 * </tr>
 *
 * <tr>
 *  <td>valuesClass</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>&nbsp;</td>
 *  <td align="left">The CSS class of the table values.</td> 
 * </tr>
 *
 * </table> 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class Table extends BaseComponent implements ITableModelSource
{
	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableModelSource#getTableModel()
	 */
	public ITableModel getTableModel()
	{
		return getTableView().getTableModel();
	}

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableModelSource#fireObservedStateChange()
     */
    public void fireObservedStateChange()
    {
        getTableView().fireObservedStateChange();
    }

	/**
	 * Resets the state of the component and forces it to load a new
     * TableModel from the tableModel binding the next time it renders.
	 */
	public void reset()
	{
		getTableView().reset();
	}

	private TableView getTableView()
	{
		return (TableView) getComponent("tableView");
	}
}
