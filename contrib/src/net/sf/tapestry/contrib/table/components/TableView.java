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
import net.sf.tapestry.IMarkupWriter;
import net.sf.tapestry.IRequestCycle;
import net.sf.tapestry.RequestCycleException;
import net.sf.tapestry.contrib.table.model.*;
import net.sf.tapestry.contrib.table.model.ITableModel;
import net.sf.tapestry.contrib.table.model.ITableSessionStateManager;
import net.sf.tapestry.contrib.table.model.ITableSessionStoreManager;
import net.sf.tapestry.contrib.table.model.common.FullTableSessionStateManager;
import net.sf.tapestry.event.PageDetachListener;
import net.sf.tapestry.event.PageEvent;
import net.sf.tapestry.event.PageRenderListener;

/**
 * @version $Id$
 * @author mindbridge
 *
 */
public class TableView extends BaseComponent 
    implements PageDetachListener, PageRenderListener, ITableModelSource
{
    private static ITableSessionStateManager DEFAULT_SESSION_STATE_MANAGER = 
        new FullTableSessionStateManager();

    // Custom bindings
    private IBinding m_objTableModelBinding = null;
    private IBinding m_objTableSessionStateManagerBinding = null;
    private IBinding m_objTableSessionStoreManagerBinding = null;

    // In bindings
    private String m_strElement;

    // Persistent properties
    private Object m_objSessionState;    
    
    // Transient objects
    private ITableModel m_objTableModel;

    public TableView() {
        init();
    }
    
    /**
     * @see net.sf.tapestry.event.PageDetachListener#pageDetached(PageEvent)
     */
    public void pageDetached(PageEvent objEvent) {
        init();
    }

    private void init() {
        m_objSessionState = null;
        m_objTableModel = null;
        m_strElement = "table";
    }

    /**
	 * @see net.sf.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad() {
		super.finishLoad();
        getPage().addPageDetachListener(this);
        getPage().addPageRenderListener(this);
	}

    public void reset() {
        m_objTableModel = null;
        saveSessionState(null);
    }

	/**
	 * Returns the tableModelBinding.
	 * @return IBinding
	 */
	public IBinding getTableModelBinding() {
		return m_objTableModelBinding;
	}

	/**
	 * Sets the tableModelBinding.
	 * @param tableModelBinding The tableModelBinding to set
	 */
	public void setTableModelBinding(IBinding tableModelBinding) {
		m_objTableModelBinding = tableModelBinding;
	}

    /**
     * Returns the tableModel.
     * @return ITableModel
     */
    public ITableModel getTableModel() {
        // if null, first try to recreate the model from the session state
        if (m_objTableModel == null) {
            Object objState = loadSessionState();
            m_objTableModel = getTableSessionStateManager().recreateTableModel(objState);
        }

        // if the session state does not help, get the model from the binding
        if (m_objTableModel == null) {
            IBinding objBinding = getTableModelBinding();
            m_objTableModel = (ITableModel) objBinding.getObject();
        }

        return m_objTableModel;
    }

	/**
	 * Returns the tableSessionStateManagerBinding.
	 * @return IBinding
	 */
	public IBinding getTableSessionStateManagerBinding() {
		return m_objTableSessionStateManagerBinding;
	}

	/**
	 * Sets the tableSessionStateManagerBinding.
	 * @param tableSessionStateManagerBinding The tableSessionStateManagerBinding to set
	 */
	public void setTableSessionStateManagerBinding(IBinding tableSessionStateManagerBinding) {
		m_objTableSessionStateManagerBinding = tableSessionStateManagerBinding;
	}

    public ITableSessionStateManager getTableSessionStateManager() {
        IBinding objBinding = getTableSessionStateManagerBinding();
        if (objBinding == null || objBinding.getObject() == null)
            return DEFAULT_SESSION_STATE_MANAGER;
        return (ITableSessionStateManager) objBinding.getObject();
    }

	/**
	 * Returns the tableSessionStoreManagerBinding.
	 * @return IBinding
	 */
	public IBinding getTableSessionStoreManagerBinding() {
		return m_objTableSessionStoreManagerBinding;
	}

	/**
	 * Sets the tableSessionStoreManagerBinding.
	 * @param tableSessionStoreManagerBinding The tableSessionStoreManagerBinding to set
	 */
	public void setTableSessionStoreManagerBinding(IBinding tableSessionStoreManagerBinding) {
		m_objTableSessionStoreManagerBinding = tableSessionStoreManagerBinding;
	}

    public ITableSessionStoreManager getTableSessionStoreManager() {
        IBinding objBinding = getTableSessionStoreManagerBinding();
        if (objBinding == null) return null;
        return (ITableSessionStoreManager) objBinding.getObject();
    }
    
	/**
	 * Returns the sessionState.
	 * @return Object
	 */
	public Object getSessionState() {
		return m_objSessionState;
	}

	/**
	 * Sets the sessionState.
	 * @param sessionState The sessionState to set
	 */
	public void setSessionState(Object sessionState) {
		m_objSessionState = sessionState;
	}
    
    public void updateSessionState(Object sessionState) {
        setSessionState(sessionState);
        fireObservedChange("sessionState", sessionState);
    }

    protected Object loadSessionState() {
        ITableSessionStoreManager objManager = getTableSessionStoreManager();
        if (objManager != null)
            return objManager.loadState(getPage().getRequestCycle());
        return getSessionState();
    }

    protected void saveSessionState(Object objState) {
        ITableSessionStoreManager objManager = getTableSessionStoreManager();
        if (objManager != null)
            objManager.saveState(getPage().getRequestCycle(), objState);
        else
            updateSessionState(objState);
    }
    
    
    
	/**
	 * @see net.sf.tapestry.event.PageRenderListener#pageBeginRender(PageEvent)
	 */
	public void pageBeginRender(PageEvent objEvent) {
        // ignore if rewinding
        if (objEvent.getRequestCycle().isRewinding()) return;
        
        // Save the session state of the table model
        // This is the moment after changes and right before committing
        ITableModel objModel = getTableModel();
        Object objState = getTableSessionStateManager().getSessionState(objModel);
        saveSessionState(objState);
	}

	/**
	 * @see net.sf.tapestry.event.PageRenderListener#pageEndRender(PageEvent)
	 */
	public void pageEndRender(PageEvent objEvent) {
	}

	/**
	 * Returns the element.
	 * @return String
	 */
	public String getElement() {
		return m_strElement;
	}

	/**
	 * Sets the element.
	 * @param element The element to set
	 */
	public void setElement(String element) {
		m_strElement = element;
	}

}
