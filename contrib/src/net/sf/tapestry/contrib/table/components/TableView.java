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

import java.io.Serializable;

import net.sf.tapestry.BaseComponent;
import net.sf.tapestry.IBinding;
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableModelSource;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;
import net.sf.tapestry.contrib.table.model.ITableSessionStoreManager;
import net.sf.tapestry.contrib.table.model.common.FullTableSessionStateManager;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;

/**
 * A low level Table component that wraps all other low level Table components.
 * This component carries the {@link net.sf.tapestry.contrib.table.model.ITableModel}
 * that is used by the other Table components. Please see the documentation of
 * {@link net.sf.tapestry.contrib.table.model.ITableModel} if you need to know more
 * about how a table is represented.
 * <p>
 * This component also handles the saving of the state of the model using an 
 * {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager}
 * to determine what part of the model is to be saved and an 
 * {@link  net.sf.tapestry.contrib.table.model.ITableSessionStoreManager}
 * to determine how to save it.
 * <p>
 * Upon the beginning of a new request cycle when the table model is first needed,
 * the model is obtained using the following process:
 * <ul>
 * <li>The persistent state of the table is loaded.
 * If the tableSessionStoreManager binding has not been bound, the state is loaded 
 * from a persistent property within the component (it is null at the beginning). 
 * Otherwise the supplied
 * {@link  net.sf.tapestry.contrib.table.model.ITableSessionStoreManager} is used
 * to load the persistent state.
 * <li>The table model is recreated using the 
 * {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager} that
 * could be supplied using the tableSessionStateManager binding 
 * (but has a default value and is therefore not required).
 * <li>If the {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager}
 * returns null, then a table model is taken from the tableModel binding. Thus, if
 * the {@link net.sf.tapestry.contrib.table.model.common.NullTableSessionStateManager}
 * is used, the table model would be taken from the tableModel binding every time.
 * </ul>
 * Just before the rendering phase the persistent state of the model is saved in
 * the session. This process occurs in reverse:
 * <ul>
 * <li>The persistent state of the model is taken via the 
 * {@link net.sf.tapestry.contrib.table.model.ITableSessionStateManager}.
 * <li>If the tableSessionStoreManager binding has not been bound, the persistent
 * state is saved as a persistent page property. Otherwise the supplied
 * {@link  net.sf.tapestry.contrib.table.model.ITableSessionStoreManager} is used
 * to save the persistent state. Use of the 
 * {@link  net.sf.tapestry.contrib.table.model.ITableSessionStoreManager} 
 * is usually necessary when tables with the same model have to be used across 
 * multiple pages, and hence the state has to be saved in the Visit, rather than
 * in a persistent component property.
 * </ul>
 * <p>
 * 
 * @author mindbridge
 * @version $Id$
 */
public class TableView
	extends BaseComponent
	implements PageDetachListener, PageRenderListener, ITableModelSource
{
	private static ITableSessionStateManager DEFAULT_SESSION_STATE_MANAGER =
		new FullTableSessionStateManager();

	// Custom bindings
	private IBinding m_objTableModelBinding = null;
	private IBinding m_objTableSessionStateManagerBinding = null;
	private IBinding m_objTableSessionStoreManagerBinding = null;
	private IBinding m_objElementBinding = null;

	// Persistent properties
	private Serializable m_objSessionState;

	// Transient objects
	private ITableModel m_objTableModel;

	public TableView()
	{
		init();
	}

	/**
	 * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
	 */
	public void pageDetached(PageEvent objEvent)
	{
		init();
	}

	private void init()
	{
		m_objSessionState = null;
		m_objTableModel = null;
	}

	/**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad()
	{
		super.finishLoad();
		getPage().addPageDetachListener(this);
		getPage().addPageRenderListener(this);
	}

	public void reset()
	{
		m_objTableModel = null;
		storeSessionState(null);
	}

	/**
	 * Returns the tableModelBinding.
	 * @return IBinding
	 */
	public IBinding getTableModelBinding()
	{
		return m_objTableModelBinding;
	}

	/**
	 * Sets the tableModelBinding.
	 * @param tableModelBinding The tableModelBinding to set
	 */
	public void setTableModelBinding(IBinding tableModelBinding)
	{
		m_objTableModelBinding = tableModelBinding;
	}

	/**
	 * Returns the tableModel.
	 * @return ITableModel
	 */
	public ITableModel getTableModel()
	{
		// if null, first try to recreate the model from the session state
		if (m_objTableModel == null)
		{
			Serializable objState = loadSessionState();
			m_objTableModel =
				getTableSessionStateManager().recreateTableModel(objState);
		}

		// if the session state does not help, get the model from the binding
		if (m_objTableModel == null)
		{
			IBinding objBinding = getTableModelBinding();
			m_objTableModel = (ITableModel) objBinding.getObject();
		}

		return m_objTableModel;
	}

    /**
     * @see net.sf.tapestry.contrib.table.model.ITableModelSource#fireObservedStateChange()
     */
    public void fireObservedStateChange()
    {
        saveSessionState();
    }

	/**
	 * Returns the tableSessionStateManagerBinding.
	 * @return IBinding
	 */
	public IBinding getTableSessionStateManagerBinding()
	{
		return m_objTableSessionStateManagerBinding;
	}

	/**
	 * Sets the tableSessionStateManagerBinding.
	 * @param tableSessionStateManagerBinding The tableSessionStateManagerBinding to set
	 */
	public void setTableSessionStateManagerBinding(IBinding tableSessionStateManagerBinding)
	{
		m_objTableSessionStateManagerBinding = tableSessionStateManagerBinding;
	}

	public ITableSessionStateManager getTableSessionStateManager()
	{
		IBinding objBinding = getTableSessionStateManagerBinding();
		if (objBinding == null || objBinding.getObject() == null)
			return DEFAULT_SESSION_STATE_MANAGER;
		return (ITableSessionStateManager) objBinding.getObject();
	}

	/**
	 * Returns the tableSessionStoreManagerBinding.
	 * @return IBinding
	 */
	public IBinding getTableSessionStoreManagerBinding()
	{
		return m_objTableSessionStoreManagerBinding;
	}

	/**
	 * Sets the tableSessionStoreManagerBinding.
	 * @param tableSessionStoreManagerBinding The tableSessionStoreManagerBinding to set
	 */
	public void setTableSessionStoreManagerBinding(IBinding tableSessionStoreManagerBinding)
	{
		m_objTableSessionStoreManagerBinding = tableSessionStoreManagerBinding;
	}

	public ITableSessionStoreManager getTableSessionStoreManager()
	{
		IBinding objBinding = getTableSessionStoreManagerBinding();
		if (objBinding == null)
			return null;
		return (ITableSessionStoreManager) objBinding.getObject();
	}

	/**
	 * Returns the sessionState.
	 * @return Object
	 */
	public Serializable getSessionState()
	{
		return m_objSessionState;
	}

	/**
	 * Sets the sessionState.
	 * @param sessionState The sessionState to set
	 */
	public void setSessionState(Serializable sessionState)
	{
		m_objSessionState = sessionState;
	}

	public void updateSessionState(Serializable sessionState)
	{
		setSessionState(sessionState);
		fireObservedChange("sessionState", sessionState);
	}

	protected Serializable loadSessionState()
	{
		ITableSessionStoreManager objManager = getTableSessionStoreManager();
		if (objManager != null)
			return objManager.loadState(getPage().getRequestCycle());
		return getSessionState();
	}

    protected void saveSessionState()
    {
        ITableModel objModel = getTableModel();
        Serializable objState =
            getTableSessionStateManager().getSessionState(objModel);
        storeSessionState(objState);
    }
    
	protected void storeSessionState(Serializable objState)
	{
		ITableSessionStoreManager objManager = getTableSessionStoreManager();
		if (objManager != null)
			objManager.saveState(getPage().getRequestCycle(), objState);
		else
			updateSessionState(objState);
	}

	/**
	 * @see net.sf.tapestry.event.PageRenderListener#pageBeginRender(PageEvent)
	 */
	public void pageBeginRender(PageEvent objEvent)
	{
		// ignore if rewinding
		if (objEvent.getRequestCycle().isRewinding())
			return;

		// Save the session state of the table model
		// This is the moment after changes and right before committing
        saveSessionState();
	}

	/**
	 * @see net.sf.tapestry.event.PageRenderListener#pageEndRender(PageEvent)
	 */
	public void pageEndRender(PageEvent objEvent)
	{
	}

    /**
     * Returns the elementBinding.
     * @return IBinding
     */
    public IBinding getElementBinding() {
        return m_objElementBinding;
    }

    /**
     * Sets the elementBinding.
     * @param elementBinding The elementBinding to set
     */
    public void setElementBinding(IBinding elementBinding) {
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
			return "table";
		return objElementBinding.getString();
	}

    /**
	 * @see net.sf.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
		throws RequestCycleException
	{
        Object objOldValue = cycle.getAttribute(ITableModelSource.TABLE_MODEL_SOURCE_PROPERTY);
        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_PROPERTY, this);

		super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_PROPERTY, objOldValue);
	}


}
