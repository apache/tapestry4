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
 * @author tsveltin ?
 */
public abstract class TreeDataView extends BaseComponent implements ITreeRowSource
{
    private TreeRowObject m_objTreeRowObject = null;

    private int m_nTreeDeep = -1;

    public TreeDataView()
    {
        super();
        initialize();
    }

    public void initialize()
    {
        m_objTreeRowObject = null;
        m_nTreeDeep = -1;
    }

    public abstract TreeView getTreeView();

    public void renderComponent(IMarkupWriter writer, IRequestCycle cycle)
    {
        // render data
        Object objExistedTreeModelSource = cycle
                .getAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE);
        cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, this);

        TreeView objView = getTreeView();
        ITreeModel objTreeModel = objView.getTreeModel();
        ITreeDataModel objTreeDataModel = objTreeModel.getTreeDataModel();

        Object objRoot = objTreeDataModel.getRoot();
        Object objRootUID = objTreeDataModel.getUniqueKey(objRoot, null);
        if (getShowRootNode())
        {
            walkTree(
                    objRoot,
                    objRootUID,
                    0,
                    objTreeModel,
                    writer,
                    cycle,
                    TreeRowObject.FIRST_LAST_ROW,
                    new int[0],
                    true);
        }
        else
        {
            boolean bFirst = true;
            int nChildenCount = objTreeModel.getTreeDataModel().getChildCount(objRoot);
            int nRowPossiotionType = nChildenCount == 1 ? TreeRowObject.FIRST_LAST_ROW
                    : TreeRowObject.FIRST_ROW;
            for (Iterator iter = objTreeModel.getTreeDataModel().getChildren(objRoot); iter
                    .hasNext();)
            {
                Object objChild = iter.next();
                Object objChildUID = objTreeModel.getTreeDataModel()
                        .getUniqueKey(objChild, objRoot);
                boolean bChildLast = !iter.hasNext();
                if (!bFirst)
                {
                    if (bChildLast)
                        nRowPossiotionType = TreeRowObject.LAST_ROW;
                    else
                        nRowPossiotionType = TreeRowObject.MIDDLE_ROW;
                }

                walkTree(
                        objChild,
                        objChildUID,
                        0,
                        objTreeModel,
                        writer,
                        cycle,
                        nRowPossiotionType,
                        new int[0],
                        bChildLast);

                bFirst = false;
            }
        }

        cycle.setAttribute(ITreeRowSource.TREE_ROW_SOURCE_ATTRIBUTE, objExistedTreeModelSource);
    }

    public void walkTree(Object objParent, Object objParentUID, int nDepth,
            ITreeModel objTreeModel, IMarkupWriter writer, IRequestCycle cycle,
            int nRowPossiotionType, int[] arrConnectImages, boolean bLast)
    {
        m_nTreeDeep = nDepth;
        int nNumberOfChildren = objTreeModel.getTreeDataModel().getChildCount(objParent);
        boolean bLeaf = (nNumberOfChildren == 0) ? true : false;
        m_objTreeRowObject = new TreeRowObject(objParent, objParentUID, nDepth, bLeaf,
                nRowPossiotionType, arrConnectImages);

        super.renderComponent(writer, cycle);

        boolean bContain = objTreeModel.getTreeStateModel().isUniqueKeyExpanded(objParentUID);
        if (bContain)
        {
            int[] arrConnectImagesNew = new int[arrConnectImages.length + 1];
            System.arraycopy(arrConnectImages, 0, arrConnectImagesNew, 0, arrConnectImages.length);
            if (bLast)
                arrConnectImagesNew[arrConnectImagesNew.length - 1] = TreeRowObject.EMPTY_CONN_IMG;
            else
                arrConnectImagesNew[arrConnectImagesNew.length - 1] = TreeRowObject.LINE_CONN_IMG;

            for (Iterator iter = objTreeModel.getTreeDataModel().getChildren(objParent); iter
                    .hasNext();)
            {
                Object objChild = iter.next();
                Object objChildUID = objTreeModel.getTreeDataModel().getUniqueKey(
                        objChild,
                        objParentUID);
                boolean bChildLast = !iter.hasNext();
                if (bChildLast)
                    nRowPossiotionType = TreeRowObject.LAST_ROW;
                else
                    nRowPossiotionType = TreeRowObject.MIDDLE_ROW;
                walkTree(
                        objChild,
                        objChildUID,
                        nDepth + 1,
                        objTreeModel,
                        writer,
                        cycle,
                        nRowPossiotionType,
                        arrConnectImagesNew,
                        bChildLast);
            }
        }
    }

    public int getTreeDeep()
    {
        return m_nTreeDeep;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeRowSource#getTreeRow()
     */
    public TreeRowObject getTreeRow()
    {
        return getTreeRowObject();
    }

    public TreeRowObject getTreeRowObject()
    {
        return m_objTreeRowObject;
    }

    public void setTreeRowObject(TreeRowObject object)
    {
        m_objTreeRowObject = object;
    }

    public abstract boolean getShowRootNode();

}