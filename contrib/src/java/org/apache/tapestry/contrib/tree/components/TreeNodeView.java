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

package org.apache.tapestry.contrib.tree.components;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IAsset;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;
import org.apache.tapestry.contrib.tree.model.ITreeStateListener;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;
import org.apache.tapestry.contrib.tree.model.TreeRowObject;
import org.apache.tapestry.contrib.tree.model.TreeStateEvent;
import org.apache.tapestry.contrib.tree.simple.SimpleNodeRenderFactory;
import org.apache.tapestry.engine.IPageLoader;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;
import org.apache.tapestry.spec.ComponentSpecification;
import org.apache.tapestry.util.ComponentAddress;

/**
 * @author tsveltin?
 */
public abstract class TreeNodeView extends BaseComponent implements PageDetachListener
{
    private static final Log LOG = LogFactory.getLog(TreeNodeView.class);

    private IBinding m_objNodeRenderFactoryBinding;

    private IBinding m_objShowNodeImagesBinding;

    private IBinding m_objMakeNodeDirectBinding;

    private Boolean m_objNodeState;

    private Boolean m_objShowNodeImages;

    private Boolean m_objMakeNodeDirect;

    private INodeRenderFactory m_objNodeRenderFactory;

    private IAsset m_objOpenNodeImage;

    private IAsset m_objCloseNodeImage;

    private int m_CurrentForeachConnectImageValue = TreeRowObject.LINE_CONN_IMG;

    public TreeNodeView()
    {
        super();
        initialize();
    }

    private void initialize()
    {
        m_objNodeState = null;
        m_objShowNodeImages = null;
        m_objNodeRenderFactory = null;
        m_objMakeNodeDirect = null;
        m_CurrentForeachConnectImageValue = TreeRowObject.LINE_CONN_IMG;
    }

    public IRender getCurrentRenderer()
    {
        INodeRenderFactory objRenderFactory = getNodeRenderFactory();
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        return objRenderFactory.getRender(
                objTreeRowSource.getTreeRow().getTreeNode(),
                getTreeModelSource(),
                getPage().getRequestCycle());
    }

    public Object[] getNodeContext()
    {
        ITreeModelSource objModelSource = getTreeModelSource();
        ComponentAddress objModelSourceAddress = new ComponentAddress(objModelSource);
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
        Object objValueUID = objTreeRowObject.getTreeNodeUID();
        if (LOG.isDebugEnabled())
        {
            LOG.debug("getNodeContext objValueUID = " + objValueUID);
        }

        return new Object[]
        { objValueUID, new Boolean(isNodeOpen()), objModelSourceAddress };
    }

    /**
     * Called when a node in the tree is clicked by the user. If the node is expanded, it will be
     * collapsed, and vice-versa, that is, the tree state model is retrieved, and it is told to
     * collapse or expand the node.
     * 
     * @param cycle
     *            The Tapestry request cycle object.
     */
    public void nodeExpandCollaps(IRequestCycle cycle)
    {
        Object context[] = cycle.getListenerParameters();
        Object objValueUID = null;
        if (context != null && context.length > 0)
        {
            objValueUID = context[0];
        }
        ComponentAddress objModelSourceAddress = (ComponentAddress) context[2];
        ITreeModelSource objTreeModelSource = (ITreeModelSource) objModelSourceAddress
                .findComponent(cycle);
        //ITreeModelSource objTreeModelSource = getTreeModelSource();
        ITreeStateModel objStateModel = objTreeModelSource.getTreeModel().getTreeStateModel();
        boolean bState = objStateModel.isUniqueKeyExpanded(objValueUID);

        if (bState)
        {
            objStateModel.collapse(objValueUID);
            fireNodeCollapsed(objValueUID, objTreeModelSource);
        }
        else
        {
            objStateModel.expandPath(objValueUID);
            fireNodeExpanded(objValueUID, objTreeModelSource);
        }
    }

    /**
     * Called when a node in the tree is selected by the user. the tree state model is retrieved,
     * and it is told to select the node.
     * 
     * @param cycle
     *            The Tapestry request cycle object.
     */
    public void nodeSelect(IRequestCycle cycle)
    {
        Object context[] = cycle.getListenerParameters();
        Object objValueUID = null;
        if (context != null && context.length > 0)
        {
            objValueUID = context[0];
        }
        ComponentAddress objModelSourceAddress = (ComponentAddress) context[2];
        ITreeModelSource objTreeModelSource = (ITreeModelSource) objModelSourceAddress
                .findComponent(cycle);
        //ITreeModelSource objTreeModelSource = getTreeModelSource();
        ITreeStateModel objStateModel = objTreeModelSource.getTreeModel().getTreeStateModel();
        Object objSelectedNodeInState = objStateModel.getSelectedNode();

        if (objValueUID.equals(objSelectedNodeInState))
        {
            //do nothing, the selected node in UI is the same as the selected in
            //state model. The user should use refresh of back button.
            return;
        }

        objStateModel.setSelectedNode(objValueUID);
        fireNodeSelected(objValueUID, objTreeModelSource);
    }

    private void fireNodeSelected(Object objValueUID, ITreeModelSource objTreeModelSource)
    {
        deliverEvent(TreeStateEvent.SELECTED_NODE_CHANGED, objValueUID, objTreeModelSource);
    }

    private void fireNodeCollapsed(Object objValueUID, ITreeModelSource objTreeModelSource)
    {
        deliverEvent(TreeStateEvent.NODE_COLLAPSED, objValueUID, objTreeModelSource);
    }

    private void fireNodeExpanded(Object objValueUID, ITreeModelSource objTreeModelSource)
    {
        deliverEvent(TreeStateEvent.NODE_EXPANDED, objValueUID, objTreeModelSource);
    }

    private void deliverEvent(int nEventUID, Object objValueUID, ITreeModelSource objTreeModelSource)
    {
        ITreeStateListener objListener = objTreeModelSource.getTreeStateListener();
        if (objListener != null)
        {
            TreeStateEvent objEvent = new TreeStateEvent(nEventUID, objValueUID, objTreeModelSource
                    .getTreeModel().getTreeStateModel());
            objListener.treeStateChanged(objEvent);
        }

    }

    public void pageDetached(PageEvent arg0)
    {
        initialize();
    }

    public void finishLoad(IRequestCycle objCycle, IPageLoader arg0, ComponentSpecification arg1)
    {
        super.finishLoad(objCycle, arg0, arg1);
        getPage().addPageDetachListener(this);

        m_objOpenNodeImage = getAsset("_openNodeImage");
        m_objCloseNodeImage = getAsset("_closeNodeImage");
    }

    public boolean isNodeOpen()
    {
        if (m_objNodeState == null)
        {
            ITreeRowSource objTreeRowSource = getTreeRowSource();
            TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
            Object objValueUID = objTreeRowObject.getTreeNodeUID();
            ITreeModelSource objTreeModelSource = getTreeModelSource();
            ITreeStateModel objStateModel = objTreeModelSource.getTreeModel().getTreeStateModel();
            boolean bState = objStateModel.isUniqueKeyExpanded(objValueUID);
            m_objNodeState = new Boolean(bState);
        }
        return m_objNodeState.booleanValue();
    }

    /**
     * Returns the openNodeImage.
     * 
     * @return IAsset
     */
    public IAsset getNodeImage()
    {
        IAsset objResult = null;
        ITreeRowSource objRowSource = getTreeRowSource();
        boolean bLeaf = objRowSource.getTreeRow().getLeaf();
        int nRowType = objRowSource.getTreeRow().getTreeRowPossiotionType();
        if (!bLeaf)
        {
            if (isNodeOpen())
            {
                switch (nRowType)
                {
                    case TreeRowObject.FIRST_LAST_ROW:
                    {
                        objResult = getAsset("_topLastOpenNodeImage");
                        break;
                    }

                    case TreeRowObject.FIRST_ROW:
                    {
                        objResult = getAsset("_topOpenNodeImage");
                        break;
                    }

                    case TreeRowObject.MIDDLE_ROW:
                    {
                        objResult = getAsset("_middleOpenNodeImage");
                        break;
                    }

                    case TreeRowObject.LAST_ROW:
                    {
                        objResult = getAsset("_bottomOpenNodeImage");
                        break;
                    }

                    default:
                    {
                        objResult = getAsset("_openNodeImage");
                        break;
                    }
                }
            }
            else
            {
                switch (nRowType)
                {
                    case TreeRowObject.FIRST_LAST_ROW:
                    {
                        objResult = getAsset("_topLastCloseNodeImage");
                        break;
                    }

                    case TreeRowObject.FIRST_ROW:
                    {
                        objResult = getAsset("_topCloseNodeImage");
                        break;
                    }

                    case TreeRowObject.MIDDLE_ROW:
                    {
                        objResult = getAsset("_middleCloseNodeImage");
                        break;
                    }

                    case TreeRowObject.LAST_ROW:
                    {
                        objResult = getAsset("_bottomCloseNodeImage");
                        break;
                    }

                    default:
                    {
                        objResult = getAsset("_closeNodeImage");
                        break;
                    }
                }
            }
        }
        else
        {
            switch (nRowType)
            {
                case TreeRowObject.FIRST_LAST_ROW:
                {
                    objResult = getAsset("_topLineImage");
                    break;
                }

                case TreeRowObject.FIRST_ROW:
                {
                    objResult = getAsset("_topLineImage");
                    break;
                }

                case TreeRowObject.MIDDLE_ROW:
                {
                    objResult = getAsset("_middleCrossLineImage");
                    break;
                }

                case TreeRowObject.LAST_ROW:
                {
                    objResult = getAsset("_bottomLineImage");
                    break;
                }

                default:
                {
                    objResult = getAsset("_bottomLineImage");
                    break;
                }
            }

        }
        return objResult;
    }

    public IAsset getNodeImageOld()
    {
        if (isNodeOpen())
        {
            if (m_objOpenNodeImage == null)
            {
                m_objOpenNodeImage = getAsset("_openNodeImage");
            }
            return m_objOpenNodeImage;
        }

        if (m_objCloseNodeImage == null)
        {
            m_objCloseNodeImage = getAsset("_closeNodeImage");
        }
        return m_objCloseNodeImage;
    }

    /**
     * Returns the closeNodeImage.
     * 
     * @return IAsset
     */
    public IAsset getCloseNodeImage()
    {
        return m_objCloseNodeImage;
    }

    /**
     * Returns the openNodeImage.
     * 
     * @return IAsset
     */
    public IAsset getOpenNodeImage()
    {
        return m_objOpenNodeImage;
    }

    /**
     * Sets the closeNodeImage.
     * 
     * @param closeNodeImage
     *            The closeNodeImage to set
     */
    public void setCloseNodeImage(IAsset closeNodeImage)
    {
        m_objCloseNodeImage = closeNodeImage;
    }

    /**
     * Sets the openNodeImage.
     * 
     * @param openNodeImage
     *            The openNodeImage to set
     */
    public void setOpenNodeImage(IAsset openNodeImage)
    {
        m_objOpenNodeImage = openNodeImage;
    }

    /**
     * @see org.apache.tapestry.AbstractComponent#renderComponent(IMarkupWriter, IRequestCycle)
     */
    protected void renderComponent(IMarkupWriter arg0, IRequestCycle arg1)
    {
        super.renderComponent(arg0, arg1);
        m_objNodeState = null;
    }

    /**
     * Returns the ShowNodeImagesBinding.
     * 
     * @return IBinding
     */
    public IBinding getShowNodeImagesBinding()
    {
        return m_objShowNodeImagesBinding;
    }

    /**
     * Sets the ShowNodeImagesBinding.
     * 
     * @param ShowNodeImagesBinding
     *            The ShowNodeImagesBinding to set
     */
    public void setShowNodeImagesBinding(IBinding ShowNodeImagesBinding)
    {
        m_objShowNodeImagesBinding = ShowNodeImagesBinding;
    }

    /**
     * Returns the ShowNodeImages.
     * 
     * @return Boolean
     */
    public Boolean isShowNodeImages()
    {
        if (m_objShowNodeImages == null)
        {
            if (getNodeRenderFactoryBinding() == null)
            {
                m_objShowNodeImages = Boolean.TRUE;
            }
            else
            {
                if (m_objShowNodeImagesBinding != null)
                {
                    m_objShowNodeImages = (Boolean) m_objShowNodeImagesBinding.getObject();
                }
                else
                {
                    m_objShowNodeImages = Boolean.TRUE;
                }
            }
        }
        return m_objShowNodeImages;
    }

    public boolean getShowImages()
    {
        boolean bResult = isShowNodeImages().booleanValue();
        return bResult;
    }

    public boolean getShowWithoutImages()
    {
        boolean bResult = !isShowNodeImages().booleanValue();
        return bResult;
    }

    public String getOffsetStyle()
    {
        //return "width: " + getTreeDataView().getTreeDeep() * 15;
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
        int nTreeRowDepth = 0;
        if (objTreeRowObject != null)
        {
            nTreeRowDepth = objTreeRowObject.getTreeRowDepth();
            if (nTreeRowDepth != 0)
                nTreeRowDepth = nTreeRowDepth - 1;
        }
        return "padding-left: " + nTreeRowDepth * 19 + "px";
    }

    /**
     * Returns the nodeRenderFactoryBinding.
     * 
     * @return IBinding
     */
    public IBinding getNodeRenderFactoryBinding()
    {
        return m_objNodeRenderFactoryBinding;
    }

    /**
     * Sets the nodeRenderFactoryBinding.
     * 
     * @param nodeRenderFactoryBinding
     *            The nodeRenderFactoryBinding to set
     */
    public void setNodeRenderFactoryBinding(IBinding nodeRenderFactoryBinding)
    {
        m_objNodeRenderFactoryBinding = nodeRenderFactoryBinding;
    }

    public INodeRenderFactory getNodeRenderFactory()
    {
        if (m_objNodeRenderFactory == null)
        {
            IBinding objBinding = getNodeRenderFactoryBinding();
            if (objBinding != null)
            {
                m_objNodeRenderFactory = (INodeRenderFactory) objBinding.getObject();
            }
            else
            {
                m_objNodeRenderFactory = new SimpleNodeRenderFactory();
            }
        }
        return m_objNodeRenderFactory;
    }

    /**
     * Returns the makeNodeDirectBinding.
     * 
     * @return IBinding
     */
    public IBinding getMakeNodeDirectBinding()
    {
        return m_objMakeNodeDirectBinding;
    }

    /**
     * Sets the makeNodeDirectBinding.
     * 
     * @param makeNodeDirectBinding
     *            The makeNodeDirectBinding to set
     */
    public void setMakeNodeDirectBinding(IBinding makeNodeDirectBinding)
    {
        m_objMakeNodeDirectBinding = makeNodeDirectBinding;
    }

    /**
     * Returns the makeNodeDirect.
     * 
     * @return Boolean
     */
    public boolean getMakeNodeDirect()
    {
        if (m_objMakeNodeDirect == null)
        {
            IBinding objBinding = getMakeNodeDirectBinding();
            if (objBinding != null)
            {
                m_objMakeNodeDirect = (Boolean) objBinding.getObject();
            }
            else
            {
                m_objMakeNodeDirect = Boolean.TRUE;
            }
        }
        return m_objMakeNodeDirect.booleanValue();
    }

    public boolean getMakeNodeNoDirect()
    {
        return !getMakeNodeDirect();
    }

    public String getCleanSelectedID()
    {
        return getSelectedNodeID();
    }

    public String getSelectedID()
    {
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        ITreeModelSource objTreeModelSource = getTreeModelSource();
        TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
        Object objNodeValueUID = objTreeRowObject.getTreeNodeUID();
        Object objSelectedNode = objTreeModelSource.getTreeModel().getTreeStateModel()
                .getSelectedNode();
        if (objNodeValueUID.equals(objSelectedNode))
        {
            return getSelectedNodeID();
        }
        return "";
    }

    private String getSelectedNodeID()
    {
        //return getTreeDataView().getTreeView().getSelectedNodeID();
        return "tree";
    }

    public String getNodeStyleClass()
    {
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        ITreeModelSource objTreeModelSource = getTreeModelSource();
        TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
        boolean bResult = false;
        if (objTreeRowObject != null)
        {
            Object objNodeValueUID = objTreeRowObject.getTreeNodeUID();
            Object objSelectedNode = objTreeModelSource.getTreeModel().getTreeStateModel()
                    .getSelectedNode();
            bResult = objNodeValueUID.equals(objSelectedNode);
        }
        if (bResult)
        {
            return "selectedNodeViewClass";
        }

        return "notSelectedNodeViewClass";
    }

    public ITreeRowSource getTreeRowSource()
    {
        ITreeRowSource objSource = (ITreeRowSource) getPage().getRequestCycle().getAttribute(
                ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE);
        return objSource;
    }

    public ITreeModelSource getTreeModelSource()
    {
        ITreeModelSource objSource = (ITreeModelSource) getPage().getRequestCycle().getAttribute(
                ITreeModelSource.TREE_MODEL_SOURCE_ATTRIBUTE);
        return objSource;
    }

    public boolean getShowConnectImage()
    {
        ITreeRowSource objRowSource = getTreeRowSource();
        int nRowType = objRowSource.getTreeRow().getTreeRowPossiotionType();
        if (TreeRowObject.MIDDLE_ROW == nRowType)
            return true;
        return false;
    }

    public int[] getForeachConnectImageList()
    {
        ITreeRowSource objTreeRowSource = getTreeRowSource();
        TreeRowObject objTreeRowObject = objTreeRowSource.getTreeRow();
        return objTreeRowObject.getLineConnImages();
    }

    public boolean getDisableLink()
    {
        ITreeRowSource objRowSource = getTreeRowSource();
        boolean bLeaf = objRowSource.getTreeRow().getLeaf();
        return bLeaf;
    }

    /**
     * Returns the openNodeImage.
     * 
     * @return IAsset nevalidno neshto
     */
    public IAsset getConnectImage()
    {
        IAsset objResult = null;
        int nConnectImageType = getCurrentForeachConnectImageValue();
        switch (nConnectImageType)
        {
            case TreeRowObject.EMPTY_CONN_IMG:
            {
                objResult = getAsset("_whiteSpaceImage");
                break;
            }

            case TreeRowObject.LINE_CONN_IMG:
            {
                objResult = getAsset("_middleLineImage");
                break;
            }

            default:
            {
                objResult = getAsset("_whiteSpaceImage");
                break;
            }
        }
        return objResult;
    }

    /**
     * @return Returns the m_CurrentForeachConnectImageValue.
     */
    public int getCurrentForeachConnectImageValue()
    {
        return m_CurrentForeachConnectImageValue;
    }

    /**
     * @param currentForeachConnectImageValue
     *            The m_CurrentForeachConnectImageValue to set.
     */
    public void setCurrentForeachConnectImageValue(int currentForeachConnectImageValue)
    {
        m_CurrentForeachConnectImageValue = currentForeachConnectImageValue;
    }
}