/*
 *  ====================================================================
 *  The Apache Software License, Version 1.1
 *
 *  Copyright (c) 2002 The Apache Software Foundation.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Apache Software Foundation (http://www.apache.org/)."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Apache" and "Apache Software Foundation" and
 *  "Apache Tapestry" must not be used to endorse or promote products
 *  derived from this software without prior written permission. For
 *  written permission, please contact apache@apache.org.
 *
 *  5. Products derived from this software may not be called "Apache",
 *  "Apache Tapestry", nor may "Apache" appear in their name, without
 *  prior written permission of the Apache Software Foundation.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of the Apache Software Foundation.  For more
 *  information on the Apache Software Foundation, please see
 *  <http://www.apache.org/>.
 */
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
 *  <td>element</td>
 *  <td>String</td>
 *  <td>in</td>
 *  <td>no</td>
 *  <td>"table"</td>
 *  <td align="left">The tag that will be used to wrap the inner components.
 *      If no binding is given, the tag that will be generated is 'table'. If you 
 *      would like to place the bounds of the table elsewhere, you can make the
 *      element 'span' or another neutral tag and manually define the table.
 *  </td> 
 * </tr>
 *
 * </table> 
 * 
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
        Object objOldValue = cycle.getAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE, this);

		super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE, objOldValue);
	}


}
