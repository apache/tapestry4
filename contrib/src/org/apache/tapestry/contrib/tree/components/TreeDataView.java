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
package org.apache.tapestry.contrib.tree.components;

import java.util.Iterator;

import org.apache.tapestry.contrib.tree.model.TreeRowObject;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;

/**
 * @version $Revision$
 */
public class TreeDataView extends BaseComponent implements ITreeRowSource{
    private IBinding m_objTreeViewBinding;

	private TreeRowObject m_objTreeRowObject = null;
    private int m_nTreeDeep = -1;

    public TreeDataView(){
        super();
        initialize();
    }

    public void initialize(){
		m_objTreeRowObject = null;
        m_nTreeDeep = -1;
    }

    /**
     * Returns the treeViewBinding.
     * @return IBinding
     */
    public IBinding getTreeViewBinding() {
        return m_objTreeViewBinding;
    }

    /**
     * Sets the treeViewBinding.
     * @param treeViewBinding The treeViewBinding to set
     */
    public void setTreeViewBinding(IBinding treeViewBinding) {
        m_objTreeViewBinding = treeViewBinding;
    }

    public TreeView getTreeView() {
        return (TreeView) m_objTreeViewBinding.getObject();
    }

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle) {
        // render data
		Object objExistedTreeModelSource = cycle.getAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE);
		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, this);
	
        TreeView objView = getTreeView();
        ITreeModel objTreeModel = objView.getTreeModel();
        ITreeDataModel objTreeDataModel = objTreeModel.getTreeDataModel();
        Object objValue = objTreeDataModel.getRoot();
        Object objValueUID = objTreeDataModel.getUniqueKey(objValue, null);

        // Object objSelectedNode = objTreeModel.getTreeStateModel().getSelectedNode();
        //if(objSelectedNode == null)
        //  objTreeModel.getTreeStateModel().expand(objValueUID);

        walkTree(objValue, objValueUID, 0, objTreeModel, writer, cycle);

		cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, objExistedTreeModelSource);
    }

    public void walkTree(Object objParent, Object objParentUID, int nDepth,
                         ITreeModel objTreeModel, IMarkupWriter writer,
                         IRequestCycle cycle) {
		m_objTreeRowObject = new TreeRowObject(objParent, objParentUID, nDepth);
        m_nTreeDeep = nDepth;

        super.renderComponent(writer, cycle);

        boolean bContain = objTreeModel.getTreeStateModel().isUniqueKeyExpanded(objParentUID);
        if (bContain) {
            for (Iterator iter = objTreeModel.getTreeDataModel().getChildren(objParent); iter.hasNext();) {
                Object objChild = iter.next();
                Object objChildUID = objTreeModel.getTreeDataModel().getUniqueKey(objChild, objParentUID);
                walkTree(objChild, objChildUID, nDepth+1, objTreeModel, writer, cycle);
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
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeRowSource#getTreeRow()
	 */
	public TreeRowObject getTreeRow() {
		return getTreeRowObject();
	}

	/**
	 * @return
	 */
	public TreeRowObject getTreeRowObject() {
		return m_objTreeRowObject;
	}

	/**
	 * @param object
	 */
	public void setTreeRowObject(TreeRowObject object) {
		m_objTreeRowObject = object;
	}

}
