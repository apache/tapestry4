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

import java.util.Iterator;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IBinding;
import org.apache.tapestry.IMarkupWriter;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;
import org.apache.tapestry.contrib.tree.model.TreeRowObject;

/**
 * @version $Id$
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

    public int getTreeDeep() {
        return m_nTreeDeep;
    }
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeRowSource#getTreeRow()
	 */
	public TreeRowObject getTreeRow() {
		return getTreeRowObject();
	}

	public TreeRowObject getTreeRowObject() {
		return m_objTreeRowObject;
	}

	public void setTreeRowObject(TreeRowObject object) {
		m_objTreeRowObject = object;
	}

}
