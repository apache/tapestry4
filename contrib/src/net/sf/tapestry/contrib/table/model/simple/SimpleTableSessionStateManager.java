package net.sf.tapestry.contrib.table.model.simple;

import java.io.Serializable;

import net.sf.tapestry.contrib.table.model.ITableColumnModel;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;

/**
 * A {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager} 
 * implementation that saves only the paging and sorting state of the table model 
 * into the session.
 * 
 * @version $Id$
 * @author mindbridge
 */
public class SimpleTableSessionStateManager
	implements ITableSessionStateManager
{
	private ITableDataModel m_objDataModel;
	private ITableColumnModel m_objColumnModel;

	public SimpleTableSessionStateManager(
		ITableDataModel objDataModel,
		ITableColumnModel objColumnModel)
	{
		m_objDataModel = objDataModel;
		m_objColumnModel = objColumnModel;
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#getSessionState(ITableModel)
	 */
	public Serializable getSessionState(ITableModel objModel)
	{
		SimpleTableModel objSimpleModel = (SimpleTableModel) objModel;
		return objSimpleModel.getState();
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableSessionStateManager#recreateTableModel(Serializable)
	 */
	public ITableModel recreateTableModel(Serializable objState)
	{
		if (objState == null)
			return null;
		SimpleTableState objSimpleState = (SimpleTableState) objState;
		return new SimpleTableModel(
			m_objDataModel,
			m_objColumnModel,
			objSimpleState);
	}

}
