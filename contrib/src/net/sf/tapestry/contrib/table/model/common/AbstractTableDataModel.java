package net.sf.tapestry.contrib.table.model.common;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.tapestry.contrib.table.model.CTableDataModelEvent;
import net.sf.tapestry.contrib.table.model.ITableDataModel;
import net.sf.tapestry.contrib.table.model.ITableDataModelListener;

/**
 * An implementation of the listener support in the ITableDataModel interface
 * 
 * @version $Id$
 * @author mindbridge
 */
public abstract class AbstractTableDataModel implements ITableDataModel
{
	private List m_arrListeners;

	public AbstractTableDataModel()
	{
		m_arrListeners = new ArrayList();
	}

	/**
	 * Method fireTableDataModelEvent.
	 * Fires a change event to all listeners
	 * @param objEvent the event to pass to the listeners
	 */
	protected void fireTableDataModelEvent(CTableDataModelEvent objEvent)
	{
		for (Iterator it = m_arrListeners.iterator(); it.hasNext();)
		{
			ITableDataModelListener objListener =
				(ITableDataModelListener) it.next();
			objListener.tableDataChanged(objEvent);
		}
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#addTableDataModelListener(ITableDataModelListener)
	 */
	public void addTableDataModelListener(ITableDataModelListener objListener)
	{
		m_arrListeners.add(objListener);
	}

	/**
	 * @see net.sf.tapestry.contrib.table.model.ITableDataModel#removeTableDataModelListener(ITableDataModelListener)
	 */
	public void removeTableDataModelListener(ITableDataModelListener objListener)
	{
		m_arrListeners.remove(objListener);
	}

}
