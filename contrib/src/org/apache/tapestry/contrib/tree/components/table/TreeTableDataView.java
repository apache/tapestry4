/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation", "Tapestry"
 *    must not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    or "Tapestry", nor may "Apache" or "Tapestry" appear in their
 *    name, without prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE TAPESTRY CONTRIBUTOR COMMUNITY
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
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
	        // render data
			ITreeModelSource objTreeModelSource = getTreeModelSource();
	        ITreeModel objTreeModel = objTreeModelSource.getTreeModel();
	        ITreeDataModel objTreeDataModel = objTreeModel.getTreeDataModel();
	        Object objValue = objTreeDataModel.getRoot();
	        Object objValueUID = objTreeDataModel.getUniqueKey(objValue, null);
	
	        // Object objSelectedNode = objTreeModel.getTreeStateModel().getSelectedNode();
	        //if(objSelectedNode == null)
	        //  objTreeModel.getTreeStateModel().expand(objValueUID);
			ArrayList arrAllExpandedNodes = new ArrayList();
			walkTree(arrAllExpandedNodes, objValue, objValueUID, 0, objTreeModel);
			m_arrAllExpandedNodes = arrAllExpandedNodes;
		}
		
		
		return m_arrAllExpandedNodes;
    }

    public void walkTree(ArrayList arrAllExpandedNodes, Object objParent, Object objParentUID, int nDepth,
                         ITreeModel objTreeModel) {
        m_nTreeDeep = nDepth;

		TreeRowObject objTreeRowObject = new TreeRowObject(objParent, objParentUID, nDepth);
		arrAllExpandedNodes.add(objTreeRowObject);

        boolean bContain = objTreeModel.getTreeStateModel().isUniqueKeyExpanded(objParentUID);
        if (bContain) {
			Iterator colChildren = objTreeModel.getTreeDataModel().getChildren(objParent);
            for (Iterator iter = colChildren; iter.hasNext();) {
                Object objChild = iter.next();
                Object objChildUID = objTreeModel.getTreeDataModel().getUniqueKey(objChild, objParentUID);
                walkTree(arrAllExpandedNodes, objChild, objChildUID, nDepth+1, objTreeModel);
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

}
