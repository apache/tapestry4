//  Copyright 2004 The Apache Software Foundation
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

package org.apache.tapestry.contrib.tree.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.tree.model.ISessionStoreManager;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;
import org.apache.tapestry.contrib.tree.model.ITreeStateListener;
import org.apache.tapestry.contrib.tree.simple.FullTreeSessionStateManager;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.event.PageRenderListener;
import org.apache.tapestry.util.ComponentAddress;

/**
 * @version $Id$
 */
public class TreeView extends BaseComponent
    implements PageDetachListener, PageRenderListener, ITreeModelSource {

    private static final Log LOG = LogFactory.getLog(TreeView.class);

    private IBinding m_objSessionStoreManagerBinding;
    private IBinding m_objTreeModelBinding;
    private IBinding m_objSessionStateManagerBinding;

    private ITreeModel m_objTreeModel;
    private ITreeSessionStateManager m_objTreeSessionStateManager;
    private ISessionStoreManager m_objSessionStoreManager;
    private Object m_objTreeSessionState;
    private ComponentAddress m_objComponentAddress;

    public TreeView(){
        super();
        initialize();
    }

    private void initialize(){
        m_objTreeModel = null;
        m_objTreeSessionStateManager = null;
        m_objSessionStoreManager = null;
        m_objTreeSessionState = null;
        m_objComponentAddress = null;
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#finishLoad()
     */
    protected void finishLoad() {
        super.finishLoad();
        getPage().addPageRenderListener(this);
        getPage().addPageDetachListener(this);
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    /*	protected void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
        throws RequestCycleException
	{
    	renderWrapped(writer, cycle);
	}
    */

    /**
     * @see org.apache.tapestry.event.PageDetachListener#pageDetached(PageEvent)
     */
    public void pageDetached(PageEvent arg0) {
        initialize();
    }

    /**
     * @see org.apache.tapestry.event.PageRenderListener#pageBeginRender(PageEvent)
     */
    public void pageBeginRender(PageEvent arg0) {
        if(arg0.getRequestCycle().isRewinding()) {
            return;
        }
        storeSesion();
    }

    /**
     * @see org.apache.tapestry.event.PageRenderListener#pageEndRender(PageEvent)
     */
    public void pageEndRender(PageEvent arg0) {
    }

    /**
     * Returns the treeModelBinding.
     * @return IBinding
     */
    public IBinding getTreeModelBinding() {
        return m_objTreeModelBinding;
    }

    /**
     * Sets the treeModelBinding.
     * @param treeModelBinding The treeModelBinding to set
     */
    public void setTreeModelBinding(IBinding treeModelBinding) {
        m_objTreeModelBinding = treeModelBinding;
    }

    /**
     * Returns the SessionStoreManagerBinding.
     * @return IBinding
     */
    public IBinding getSessionStoreManagerBinding() {
        return m_objSessionStoreManagerBinding;
    }

    /**
     * Returns the sessionStateManagerBinding.
     * @return IBinding
     */
    public IBinding getSessionStateManagerBinding() {
        return m_objSessionStateManagerBinding;
    }

    /**
     * Sets the SessionStoreManagerBinding.
     * @param sessionStoreManagerBinding The SessionStoreManagerBinding to set
     */
    public void setSessionStoreManagerBinding(IBinding
                                              sessionStoreManagerBinding) {
        m_objSessionStoreManagerBinding = sessionStoreManagerBinding;
    }

    /**
     * Sets the sessionStateManagerBinding.
     * @param sessionStateManagerBinding The sessionStateManagerBinding to set
     */
    public void setSessionStateManagerBinding(IBinding
                                              sessionStateManagerBinding) {
        m_objSessionStateManagerBinding = sessionStateManagerBinding;
    }

    private void extractTreeModel(){
        if (LOG.isDebugEnabled()) {
            LOG.debug("TreeView.extractTreeModel()");
        }

        ISessionStoreManager objHolder = getSessionStoreManager();
        ITreeSessionStateManager objSessionManager = getTreeSessionStateMgr();
        Object objSessionState;
        if (objHolder == null) {
            objSessionState = getTreeSessionState();
        } else {
            objSessionState = objHolder.getSessionState(this.getPage(),
                                                        "treeSessionState");
        }

        if (objSessionState != null) {
            m_objTreeModel = objSessionManager.getModel(objSessionState);
        } else {
            if (LOG.isDebugEnabled()) {
                LOG.debug("TreeView.extractTreeModel() from BINDING");
            }

            m_objTreeModel = (ITreeModel)getTreeModelBinding().getObject();
        }

    }

    private void storeSesion(){
        if (LOG.isDebugEnabled()) {
            LOG.debug("TreeView.storeSesion()");
        }

        ITreeSessionStateManager objSessionManager = getTreeSessionStateMgr();
        Object objSessionState =
            objSessionManager.getSessionState(getTreeModel());

        store(objSessionState);
    }

    private void store(Object objSessionState){
        ISessionStoreManager objHolder = getSessionStoreManager();

        if (objHolder == null) {
            fireObservedChange("treeSessionState", objSessionState);
        } else {
            //String strPath = "treeSessionState";
            String strPath = getExtendedId();
            if (LOG.isDebugEnabled())
                LOG.debug("store(): setting state with: " + strPath);
            objHolder.setSessionState(this.getPage(), strPath,
                                      objSessionState);
        }
    }

    /**
     * @see ITreeComponent#resetState()
     */
    public void resetState() {
        if (LOG.isDebugEnabled()) {
            LOG.debug("TreeView.resetState()");
        }
        initialize();
        store(null);
    }

    /**
     * Returns the SessionStoreManager.
     * @return ISessionStoreManager
     */
    public ISessionStoreManager getSessionStoreManager() {
        if (m_objSessionStoreManager == null
            && getSessionStoreManagerBinding() != null) {
            m_objSessionStoreManager =
                (ISessionStoreManager)getSessionStoreManagerBinding().getObject();
        }

        return m_objSessionStoreManager;
    }

    /**
     * Returns the wizardSessionStateMgr.
     * @return IWizardSessionStateManager
     */
    public ITreeSessionStateManager getTreeSessionStateMgr() {
        if (m_objTreeSessionStateManager == null) {
            IBinding objBinding = getSessionStateManagerBinding();
            if(objBinding != null){
                Object objManager = objBinding.getObject();
                m_objTreeSessionStateManager =
                    (ITreeSessionStateManager) objManager;
            } else {
                m_objTreeSessionStateManager =
                    new FullTreeSessionStateManager();
            }
        }
        return m_objTreeSessionStateManager;
    }

    public ComponentAddress getComponentPath() {
        if (m_objComponentAddress == null) {
            m_objComponentAddress = new ComponentAddress(this);
        }
        return m_objComponentAddress;
    }

    /**
     * Returns the treeModel.
     * @return ITreeModel
     */
    public ITreeModel getTreeModel() {
        if (m_objTreeModel == null) {
            extractTreeModel();
        }
        return m_objTreeModel;
    }

    /**
     * Sets the treeModel.
     * @param treeModel The treeModel to set
     */
    public void setTreeModel(ITreeModel treeModel) {
        m_objTreeModel = treeModel;
    }

    /**
     * Returns the treeSessionState.
     * @return Object
     */
    public Object getTreeSessionState() {
        return m_objTreeSessionState;
    }

    /**
     * Sets the treeSessionState.
     * @param treeSessionState The treeSessionState to set
     */
    public void setTreeSessionState(Object treeSessionState) {
        m_objTreeSessionState = treeSessionState;
    }

    public String getSelectedNodeStyleID(){
        return getId() + ":selected";
    }
    
    /**
	 * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter arg0, IRequestCycle arg1) {
		Object objExistedTreeModelSource = arg1.getAttribute(ITreeModelSource.TREE_MODEL_SOURCE_ATTRIBUTE);
		arg1.setAttribute(ITreeModelSource.TREE_MODEL_SOURCE_ATTRIBUTE, this);
		
		super.renderComponent(arg0, arg1);
		arg1.setAttribute(ITreeModelSource.TREE_MODEL_SOURCE_ATTRIBUTE, objExistedTreeModelSource);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeModelSource#getTreeStateListener()
	 */
	public ITreeStateListener getTreeStateListener() {
		ITreeStateListener objListener = null;
		IBinding objBinding = getBinding("treeStateListener");
		if(objBinding != null){
			objListener = (ITreeStateListener) objBinding.getObject();
		}
		return objListener;
	}

}
