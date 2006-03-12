// Copyright 2004, 2005 The Apache Software Foundation
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.apache.tapestry.contrib.table.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.IAdvancedTableColumnSource;
import org.apache.tapestry.contrib.table.model.IBasicTableModel;
import org.apache.tapestry.contrib.table.model.ITableAction;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;
import org.apache.tapestry.contrib.table.model.ITableDataModel;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITablePagingState;
import org.apache.tapestry.contrib.table.model.ITableSessionStateManager;
import org.apache.tapestry.contrib.table.model.ITableSessionStoreManager;
import org.apache.tapestry.contrib.table.model.common.BasicTableModelWrap;
import org.apache.tapestry.contrib.table.model.simple.SimpleListTableDataModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableState;
import org.apache.tapestry.event.PageBeginRenderListener;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * A low level Table component that wraps all other low level Table components. This component
 * carries the {@link org.apache.tapestry.contrib.table.model.ITableModel}that is used by the other
 * Table components. Please see the documentation of
 * {@link org.apache.tapestry.contrib.table.model.ITableModel}if you need to know more about how a
 * table is represented.
 * <p>
 * This component also handles the saving of the state of the model using an
 * {@link org.apache.tapestry.contrib.table.model.ITableSessionStateManager}to determine what part
 * of the model is to be saved and an
 * {@link  org.apache.tapestry.contrib.table.model.ITableSessionStoreManager}to determine how to
 * save it.
 * <p>
 * Upon the beginning of a new request cycle when the table model is first needed, the model is
 * obtained using the following process:
 * <ul>
 * <li>The persistent state of the table is loaded. If the tableSessionStoreManager binding has not
 * been bound, the state is loaded from a persistent property within the component (it is null at
 * the beginning). Otherwise the supplied
 * {@link  org.apache.tapestry.contrib.table.model.ITableSessionStoreManager}is used to load the
 * persistent state.
 * <li>The table model is recreated using the
 * {@link org.apache.tapestry.contrib.table.model.ITableSessionStateManager}that could be supplied
 * using the tableSessionStateManager binding (but has a default value and is therefore not
 * required).
 * <li>If the {@link org.apache.tapestry.contrib.table.model.ITableSessionStateManager}returns
 * null, then a table model is taken from the tableModel binding. Thus, if the
 * {@link org.apache.tapestry.contrib.table.model.common.NullTableSessionStateManager}is used, the
 * table model would be taken from the tableModel binding every time.
 * </ul>
 * Just before the rendering phase the persistent state of the model is saved in the session. This
 * process occurs in reverse:
 * <ul>
 * <li>The persistent state of the model is taken via the
 * {@link org.apache.tapestry.contrib.table.model.ITableSessionStateManager}.
 * <li>If the tableSessionStoreManager binding has not been bound, the persistent state is saved as
 * a persistent page property. Otherwise the supplied
 * {@link  org.apache.tapestry.contrib.table.model.ITableSessionStoreManager}is used to save the
 * persistent state. Use of the
 * {@link  org.apache.tapestry.contrib.table.model.ITableSessionStoreManager}is usually necessary
 * when tables with the same model have to be used across multiple pages, and hence the state has to
 * be saved in the Visit, rather than in a persistent component property.
 * </ul>
 * <p>
 * <p>
 * Please see the Component Reference for details on how to use this component. [ <a
 * href="../../../../../../../ComponentReference/contrib.TableView.html">Component Reference </a>]
 * 
 * @author mindbridge
 */
public abstract class TableView extends BaseComponent implements PageDetachListener,
        PageBeginRenderListener, ITableModelSource
{
    /** @since 4.0 */
    public abstract TableColumnModelSource getModelSource();

    /** @since 4.0 */
    public abstract IAdvancedTableColumnSource getColumnSource();
    
    // Component properties
    private ITableSessionStateManager m_objDefaultSessionStateManager = null;

    private ITableColumnModel m_objColumnModel = null;

    // Transient objects
    private ITableModel m_objTableModel;

    private ITableModel m_objCachedTableModelValue;

    // enhanced parameter methods
    public abstract ITableModel getTableModelValue();

    public abstract Object getSource();

    public abstract Object getColumns();

    public abstract int getInitialPage();

    public abstract String getInitialSortColumn();

    public abstract boolean getInitialSortOrder();

    public abstract ITableSessionStateManager getTableSessionStateManager();

    public abstract ITableSessionStoreManager getTableSessionStoreManager();

    public abstract IComponent getColumnSettingsContainer();

    public abstract int getPageSize();

    public abstract String getPersist();

    // enhanced property methods
    public abstract Serializable getSessionState();

    public abstract void setSessionState(Serializable sessionState);

    public abstract Serializable getClientState();

    public abstract void setClientState(Serializable sessionState);

    public abstract Serializable getClientAppState();

    public abstract void setClientAppState(Serializable sessionState);

    public abstract List getTableActions();
    
    public abstract void setTableActions(List actions); 
    
    /**
     * The component constructor. Invokes the component member initializations.
     */
    public TableView()
    {
        initialize();
    }

    /**
     * Invokes the component member initializations.
     * 
     * @see org.apache.tapestry.event.PageDetachListener#pageDetached(PageEvent)
     */
    public void pageDetached(PageEvent objEvent)
    {
        initialize();
    }

    /**
     * Initialize the component member variables.
     */
    private void initialize()
    {
        m_objTableModel = null;
        m_objCachedTableModelValue = null;
    }

    /**
     * Resets the table by removing any stored table state. This means that the current column to
     * sort on and the current page will be forgotten and all data will be reloaded.
     */
    public void reset()
    {
        initialize();
        storeSessionState(null);
    }

    public ITableModel getCachedTableModelValue()
    {
        if (m_objCachedTableModelValue == null)
            m_objCachedTableModelValue = getTableModelValue();
        return m_objCachedTableModelValue;
    }

    /**
     * Returns the tableModel.
     * 
     * @return ITableModel the table model used by the table components
     */
    public ITableModel getTableModel()
    {
        // if null, first try to recreate the model from the session state
        if (m_objTableModel == null)
        {
            Serializable objState = loadSessionState();
            ITableSessionStateManager objStateManager = getTableSessionStateManager();
            m_objTableModel = objStateManager.recreateTableModel(objState);
        }

        // if the session state does not help, get the model from the binding
        if (m_objTableModel == null)
            m_objTableModel = getCachedTableModelValue();

        // if the model from the binding is null, build a model from source and columns
        if (m_objTableModel == null)
            m_objTableModel = generateTableModel(null);

        if (m_objTableModel == null)
            throw new ApplicationRuntimeException(TableMessages.missingTableModel(this));

        return m_objTableModel;
    }

    /**
     * Generate a table model using the 'source' and 'columns' parameters.
     * 
     * @return the newly generated table model
     */
    protected ITableModel generateTableModel(SimpleTableState objState)
    {
        // create a new table state if none is passed
        if (objState == null)
        {
            objState = new SimpleTableState();
            objState.getSortingState().setSortColumn(getInitialSortColumn(), getInitialSortOrder());
            objState.getPagingState().setCurrentPage(getInitialPage());
        }

        // update the page size if set in the parameter
        if (isParameterBound("pageSize"))
            objState.getPagingState().setPageSize(getPageSize());

        // get the column model. if not possible, return null.
        ITableColumnModel objColumnModel = getTableColumnModel();
        if (objColumnModel == null)
            return null;

        Object objSourceValue = getSource();
        if (objSourceValue == null)
            return null;

        // if the source parameter is of type {@link IBasicTableModel},
        // create and return an appropriate wrapper
        if (objSourceValue instanceof IBasicTableModel)
            return new BasicTableModelWrap((IBasicTableModel) objSourceValue, objColumnModel,
                    objState);

        // otherwise, the source parameter must contain the data to be displayed
        ITableDataModel objDataModel = null;
        if (objSourceValue instanceof Object[])
            objDataModel = new SimpleListTableDataModel((Object[]) objSourceValue);
        else if (objSourceValue instanceof List)
            objDataModel = new SimpleListTableDataModel((List) objSourceValue);
        else if (objSourceValue instanceof Collection)
            objDataModel = new SimpleListTableDataModel((Collection) objSourceValue);
        else if (objSourceValue instanceof Iterator)
            objDataModel = new SimpleListTableDataModel((Iterator) objSourceValue);

        if (objDataModel == null)
            throw new ApplicationRuntimeException(TableMessages.invalidTableSource(
                    this,
                    objSourceValue));

        return new SimpleTableModel(objDataModel, objColumnModel, objState);
    }

    /**
     * Returns the table column model as specified by the 'columns' binding. If the value of the
     * 'columns' binding is of a type different than ITableColumnModel, this method makes the
     * appropriate conversion.
     * 
     * @return The table column model as specified by the 'columns' binding
     */
    protected ITableColumnModel getTableColumnModel()
    {
        Object objColumns = getColumns();

        if (objColumns == null)
            return null;

        if (objColumns instanceof ITableColumnModel)
        {
            return (ITableColumnModel) objColumns;
        }

        if (objColumns instanceof Iterator)
        {
            // convert to List
            Iterator objColumnsIterator = (Iterator) objColumns;
            List arrColumnsList = new ArrayList();
            addAll(arrColumnsList, objColumnsIterator);
            objColumns = arrColumnsList;
        }

        if (objColumns instanceof List)
        {
            // validate that the list contains only ITableColumn instances
            List arrColumnsList = (List) objColumns;
            int nColumnsNumber = arrColumnsList.size();
            for (int i = 0; i < nColumnsNumber; i++)
            {
                if (!(arrColumnsList.get(i) instanceof ITableColumn))
                    throw new ApplicationRuntimeException(TableMessages.columnsOnlyPlease(this));
            }
            //objColumns = arrColumnsList.toArray(new ITableColumn[nColumnsNumber]);
            return new SimpleTableColumnModel(arrColumnsList);
        }

        if (objColumns instanceof ITableColumn[])
        {
            return new SimpleTableColumnModel((ITableColumn[]) objColumns);
        }

        if (objColumns instanceof String)
        {
            String strColumns = (String) objColumns;
            if (getBinding("columns").isInvariant())
            {
                // if the binding is invariant, create the columns only once
                if (m_objColumnModel == null)
                    m_objColumnModel = generateTableColumnModel(strColumns);
                return m_objColumnModel;
            }

            // if the binding is not invariant, create them every time
            return generateTableColumnModel(strColumns);
        }

        throw new ApplicationRuntimeException(TableMessages.invalidTableColumns(this, objColumns));
    }

    private void addAll(List arrColumnsList, Iterator objColumnsIterator)
    {
        while (objColumnsIterator.hasNext())
            arrColumnsList.add(objColumnsIterator.next());
    }

    /**
     * Generate a table column model out of the description string provided. Entries in the
     * description string are separated by commas. Each column entry is of the format name,
     * name:expression, or name:displayName:expression. An entry prefixed with ! represents a
     * non-sortable column. If the whole description string is prefixed with *, it represents
     * columns to be included in a Form.
     * 
     * @param strDesc
     *            the description of the column model to be generated
     * @return a table column model based on the provided description
     */
    protected ITableColumnModel generateTableColumnModel(String strDesc)
    {
        IComponent objColumnSettingsContainer = getColumnSettingsContainer();
        IAdvancedTableColumnSource objColumnSource = getColumnSource();
        
        return getModelSource().generateTableColumnModel(objColumnSource, strDesc, this, objColumnSettingsContainer);
    }

    /**
     * The default session state manager to be used in case no such manager is provided by the
     * corresponding parameter.
     * 
     * @return the default session state manager
     */
    public ITableSessionStateManager getDefaultTableSessionStateManager()
    {
        if (m_objDefaultSessionStateManager == null)
            m_objDefaultSessionStateManager = new TableViewSessionStateManager(this);
        return m_objDefaultSessionStateManager;
    }

    /**
     * Invoked when there is a modification of the table state and it needs to be saved
     * 
     * @see org.apache.tapestry.contrib.table.model.ITableModelSource#fireObservedStateChange()
     */
    public void fireObservedStateChange()
    {
        saveSessionState();
    }

    /**
     * Ensures that the table state is saved before the render phase begins in case there are
     * modifications for which {@link #fireObservedStateChange()}has not been invoked.
     * 
     * @see org.apache.tapestry.event.PageBeginRenderListener#pageBeginRender(org.apache.tapestry.event.PageEvent)
     */
    public void pageBeginRender(PageEvent event)
    {
        executeTableActions();
        
        // 'suspenders': save the table model if it has been already loaded.
        // this means that if a change has been made explicitly in a listener,
        // it will be saved. this is the last place before committing the changes
        // where a save can occur
        if (m_objTableModel != null)
            saveSessionState();
    }

    /**
     * Saves the table state using the SessionStateManager to determine what to save and the
     * SessionStoreManager to determine where to save it.
     */
    protected void saveSessionState()
    {
        ITableModel objModel = getTableModel();
        Serializable objState = getTableSessionStateManager().getSessionState(objModel);
        storeSessionState(objState);
    }

    /**
     * Loads the table state using the SessionStoreManager.
     * 
     * @return the stored table state
     */
    protected Serializable loadSessionState()
    {
        ITableSessionStoreManager objManager = getTableSessionStoreManager();
        if (objManager != null)
            return objManager.loadState(getPage().getRequestCycle());
    	String strPersist = getPersist();
    	if (strPersist.equals("client") || strPersist.equals("client:page"))
    		return getClientState();
    	else if (strPersist.equals("client:app"))
    		return getClientAppState();
    	else
    		return getSessionState();
    }

    /**
     * Stores the table state using the SessionStoreManager.
     * 
     * @param objState
     *            the table state to store
     */
    protected void storeSessionState(Serializable objState)
    {
        ITableSessionStoreManager objManager = getTableSessionStoreManager();
        if (objManager != null)
            objManager.saveState(getPage().getRequestCycle(), objState);
        else {
        	String strPersist = getPersist();
        	if (strPersist.equals("client") || strPersist.equals("client:page"))
        		setClientState(objState);
        	else if (strPersist.equals("client:app"))
        		setClientAppState(objState);
        	else 
        		setSessionState(objState);
        }
    }

    /**
     * Make sure that the values stored in the model are useable and correct. The changes made here
     * are not saved.
     */
    protected void validateValues()
    {
        ITableModel objModel = getTableModel();

        // make sure current page is within the allowed range
        ITablePagingState objPagingState = objModel.getPagingState();
        int nCurrentPage = objPagingState.getCurrentPage();
        int nPageCount = objModel.getPageCount();
        if (nCurrentPage >= nPageCount)
        {
            // the current page is greater than the page count. adjust.
            nCurrentPage = nPageCount - 1;
            objPagingState.setCurrentPage(nCurrentPage);
        }
        if (nCurrentPage < 0)
        {
            // the current page is before the first page. adjust.
            nCurrentPage = 0;
            objPagingState.setCurrentPage(nCurrentPage);
        }
    }

    /**
     * Stores a pointer to this component in the Request Cycle while rendering so that wrapped
     * components have access to it.
     * 
     * @see org.apache.tapestry.BaseComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        Object objOldValue = cycle.getAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE, this);

        initialize();
        validateValues();
        super.renderComponent(writer, cycle);

        cycle.setAttribute(ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE, objOldValue);
    }

    /**
     * Stores the provided table action
     */
    public void storeTableAction(ITableAction action)
    {
        List actions = getTableActions();
        if (actions == null) {
            actions = new ArrayList(5);
            setTableActions(actions);
        }
        
        actions.add(action);
    }
    
    /**
     * Executes the stored table actions
     */
    public void executeTableActions()
    {
        List actions = getTableActions();
        if (actions == null || actions.isEmpty())
            return;

        // save the actions and clear the list
        List savedActions = new ArrayList(actions);
        actions.clear();

        ITableModel objTableModel = getTableModel();
        for(Iterator it = savedActions.iterator(); it.hasNext();)
        {
            ITableAction action = (ITableAction) it.next();
            action.executeTableAction(objTableModel);
        }
        
        // ensure that the changes are saved
        fireObservedStateChange();
    }
    
}
