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

package org.apache.tapestry.workbench.tree.examples.fsmodel;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.tapestry.contrib.tree.model.ITreeNode;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeDataModel;

/**
 * @author ceco
 */
public class FileSystemDataModel extends SimpleTreeDataModel
    implements Serializable {
    /**
     * Constructor for FileSystemDataModel.
     * @param objRootNode
     */
    public FileSystemDataModel(ITreeNode objRootNode) {
        super(objRootNode);
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getObject(Object)
     */
    public Object getObject(Object objUniqueKey) {
        return findNode(objUniqueKey, (IFileSystemTreeNode)getRoot());
    }

    private IFileSystemTreeNode findNode(Object objUniqueKey,
                                         IFileSystemTreeNode objParentNode) {
        String strUniqueKey = (String) objUniqueKey;
        String strParentUniqueKey = objParentNode.getAbsolutePath();

        if (strUniqueKey.equals(strParentUniqueKey)) {
            return objParentNode;
        }

        IFileSystemTreeNode obj = null;

		if(strUniqueKey.startsWith(strParentUniqueKey))
		{
	        for (Iterator iter = objParentNode.getChildren().iterator(); iter.hasNext();) {
	            IFileSystemTreeNode element = (IFileSystemTreeNode) iter.next();
	            obj = findNode(objUniqueKey, element);
	            if (obj != null) {
	                break;
	            }
	        }
		}

        return obj;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getUniqueKey(Object, Object)
     */
    public Object getUniqueKey(Object objTarget, Object objParentUniqueKey) {
        IFileSystemTreeNode objNode = (IFileSystemTreeNode) objTarget;
        return objNode.getAbsolutePath();
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#isAncestorOf(Object, Object)
     */
    public boolean isAncestorOf(Object objChildUniqueKey,
                                Object objParentUniqueKey) {
        String strChildAbsolutePath = (String)objChildUniqueKey;
        String strParentAbsolutePath = (String)objParentUniqueKey;

        if("".equals(strParentAbsolutePath)) {
            return true;
        }

        return strChildAbsolutePath.lastIndexOf(strParentAbsolutePath) > -1;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getParentUniqueKey(Object)
     */
    public Object getParentUniqueKey(Object objChildUniqueKey) {
        IFileSystemTreeNode objNode =
            (IFileSystemTreeNode) getObject(objChildUniqueKey);
        return objNode.getParent();
    }

}
