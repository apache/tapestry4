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

package org.apache.tapestry.contrib.tree.components.table;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModel;
import org.apache.tapestry.contrib.table.model.ITableSessionStateManager;
import org.apache.tapestry.contrib.table.model.simple.SimpleListTableDataModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumnModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableModel;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableSessionStateManager;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;
import org.apache.tapestry.contrib.tree.model.TreeRowObject;
import org.apache.tapestry.event.PageDetachListener;
import org.apache.tapestry.event.PageEvent;

/**
 * @version $Id$
 */
public class TreeTableDataView extends BaseComponent implements ITreeRowSource, PageDetachListener{
    private int m_nTreeDeep = -1;
	private TreeRowObject m_objTreeRowObject = null;
	private ArrayList m_arrAllExpandedNodes = null;

    public TreeTableDataView(){
        super();
        initialize();
    }

    public void initialize(){
        m_nTreeDeep = -1;
//		m_objTableModel = null;
		m_objTreeRowObject = null;
		m_arrAllExpandedNodes = null;
    }


	/**
	 * @see org.apache.tapestry.AbstractComponent#finishLoad()
	 */
	protected void finishLoad() {
		super.finishLoad();
		getPage().addPageDetachListener(this);
	}

	/**
	 * @see org.apache.tapestry.event.PageDetachListener#pageDetached(org.apache.tapestry.event.PageEvent)
	 */
	public void pageDetached(PageEvent arg0) {
		initialize();
	}


    public ITreeModelSource getTreeModelSource() {
		ITreeModelSource objSource = (ITreeModelSource) getPage().getRequestCycle().getAttribute(ITreeModelSource.TREE_MODEL_SOURCE_ATTRIBUTE);
    	if(objSource == null){
			objSource = (ITreeModelSource) getBinding("treeView").getObject();
    	}
    	return objSource;
    }

    public ArrayList generateNodeList() {
        if(m_arrAllExpandedNodes == null){
			ArrayList arrAllExpandedNodes = new ArrayList();

			// render data
			ITreeModelSource objTreeModelSource = getTreeModelSource();
	        ITreeModel objTreeModel = objTreeModelSource.getTreeModel();
	        ITreeDataModel objTreeDataModel = objTreeModel.getTreeDataModel();

	        Object objRoot = objTreeDataModel.getRoot();
	        Object objRootUID = objTreeDataModel.getUniqueKey(objRoot, null);
	        if(getShowRootNode()){
	            walkTree(arrAllExpandedNodes, objRoot, objRootUID, 0, objTreeModel, TreeRowObject.FIRST_LAST_ROW, new int[0], true);
	        }else{
	        	int nChildenCount = objTreeModel.getTreeDataModel().getChildCount(objRoot);
	        	int nRowPossiotionType = nChildenCount == 1 ? TreeRowObject.FIRST_LAST_ROW : TreeRowObject.FIRST_ROW;
	        	boolean bFirst = true;
	            for (Iterator iter = objTreeModel.getTreeDataModel().getChildren(objRoot); iter.hasNext();) {
	                Object objChild = iter.next();
	                Object objChildUID = objTreeModel.getTreeDataModel().getUniqueKey(objChild, objRoot);
	                boolean bChildLast = !iter.hasNext();
	                if( !bFirst){
	                    if(bChildLast)
	                		nRowPossiotionType = TreeRowObject.LAST_ROW;
	                	else
	                    	nRowPossiotionType = TreeRowObject.MIDDLE_ROW;
	                }
					walkTree(arrAllExpandedNodes, objChild, objChildUID, 0, objTreeModel, nRowPossiotionType, new int[0], bChildLast);
	                bFirst = false;
	            }
	        }			

			m_arrAllExpandedNodes = arrAllExpandedNodes;
		}
		
		
		return m_arrAllExpandedNodes;
    }

    public void walkTree(ArrayList arrAllExpandedNodes, Object objParent, Object objParentUID, int nDepth,
                         ITreeModel objTreeModel, int nRowPossiotionType, int[] arrConnectImages, boolean bLast) {
        m_nTreeDeep = nDepth;

    	int nNumberOfChildren = objTreeModel.getTreeDataModel().getChildCount(objParent);
    	boolean bLeaf = (nNumberOfChildren == 0) ? true : false;
		TreeRowObject objTreeRowObject = new TreeRowObject(objParent, objParentUID, nDepth, bLeaf, nRowPossiotionType, arrConnectImages);
		arrAllExpandedNodes.add(objTreeRowObject);

        boolean bContain = objTreeModel.getTreeStateModel().isUniqueKeyExpanded(objParentUID);
        if (bContain) {
        	int[] arrConnectImagesNew = new int[arrConnectImages.length+1];
        	System.arraycopy(arrConnectImages, 0, arrConnectImagesNew, 0, arrConnectImages.length);
        	if(bLast)
        		arrConnectImagesNew[arrConnectImagesNew.length-1] = TreeRowObject.EMPTY_CONN_IMG;
        	else
        		arrConnectImagesNew[arrConnectImagesNew.length-1] = TreeRowObject.LINE_CONN_IMG;
			Iterator colChildren = objTreeModel.getTreeDataModel().getChildren(objParent);
            for (Iterator iter = colChildren; iter.hasNext();) {
                Object objChild = iter.next();
                Object objChildUID = objTreeModel.getTreeDataModel().getUniqueKey(objChild, objParentUID);
                boolean bChildLast = !iter.hasNext();
                if(!bChildLast)
            		nRowPossiotionType = TreeRowObject.LAST_ROW;
            	else
                	nRowPossiotionType = TreeRowObject.MIDDLE_ROW;
               walkTree(arrAllExpandedNodes, objChild, objChildUID, nDepth+1, objTreeModel, nRowPossiotionType, arrConnectImagesNew, bChildLast);
            }
        }
    }

    /**
     * Returns the treeDeep.
     * @return int
     */
    public int getTreeDeep() {
        return m_nTreeDeep;
    }

/*	public ITableModel getTableModel() {
		if(m_objTableModel == null){
			m_objTableModel = createTableModel();
		}
		return m_objTableModel;
	}
*/
	public ITableModel getTableModel() {
		return createTableModel();
	}

	private ITableModel createTableModel(){
		ArrayList arrAllNodes = generateNodeList();
		Object[] arrAllExpandedNodes = new Object[arrAllNodes.size()];
		arrAllNodes.toArray(arrAllExpandedNodes);

		
		SimpleTableModel objTableModel = new SimpleTableModel(arrAllExpandedNodes, getTableColunms());
		objTableModel.getPagingState().setPageSize(getEntriesPerTablePage());		
		
		return objTableModel;
	}

	public ITableColumn[] getTableColunms(){
		ArrayList arrColumnsList = new ArrayList();
		arrColumnsList.add(new TreeTableColumn ("Name", false, null)); 

		ArrayList arrTableColunms = getTableColunmsFromBinding();
		if(arrTableColunms != null)
			arrColumnsList.addAll(arrTableColunms);
		
		ITableColumn[] arrColumns = new ITableColumn[arrColumnsList.size()];
		arrColumnsList.toArray(arrColumns);

		return arrColumns;
	}

	public ArrayList getTableColunmsFromBinding(){
		IBinding objBinding = getBinding("tableColunms");
		if(objBinding != null)
			return (ArrayList)objBinding.getObject();
		return null;
	}
	
	public int getEntriesPerTablePage(){
		IBinding objBinding = getBinding("entriesPerTablePage");
		if(objBinding != null)
			return objBinding.getInt();
		return 50;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeRowSource#getTreeRow()
	 */
	public TreeRowObject getTreeRow() {
		return getTreeRowObject();
	}

	public ITableSessionStateManager getTableSessionStateManager(){
		SimpleListTableDataModel objDataModel = new SimpleListTableDataModel(generateNodeList());
		SimpleTableColumnModel objColumnModel = new SimpleTableColumnModel(getTableColunms());
		SimpleTableSessionStateManager objStateManager = new SimpleTableSessionStateManager(objDataModel, objColumnModel);
		return objStateManager;
		//return NullTableSessionStateManager.NULL_STATE_MANAGER;
	}

	/**
	 * @see org.apache.tapestry.BaseComponent#renderComponent(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	protected void renderComponent(IMarkupWriter arg0, IRequestCycle cycle) {
		Object objExistedTreeModelSource = cycle.getAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE);
		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, this);

		super.renderComponent(arg0, cycle);

		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, objExistedTreeModelSource);
	}

	/**
	 * @see org.apache.tapestry.AbstractComponent#renderBody(org.apache.tapestry.IMarkupWriter, org.apache.tapestry.IRequestCycle)
	 */
	public void renderBody(IMarkupWriter arg0, IRequestCycle cycle) {
		Object objExistedTreeModelSource = cycle.getAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE);
		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, this);

		super.renderBody(arg0, cycle);

		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, objExistedTreeModelSource);
	}


	public TreeRowObject getTreeRowObject() {
		return m_objTreeRowObject;
	}

	public void setTreeRowObject(TreeRowObject object) {
		m_objTreeRowObject = object;
	}

	public boolean getShowRootNode(){
		boolean bShowRootNode = true;
		IBinding objShowRootNodeB = getBinding("showRootNode");
		if(objShowRootNodeB != null){
			bShowRootNode = objShowRootNodeB.getBoolean();
		}
		return bShowRootNode;
	}
	
}
