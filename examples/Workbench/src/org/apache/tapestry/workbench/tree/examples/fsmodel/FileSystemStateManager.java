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

import java.io.File;

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeNode;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeModel;

/**
 * @author ceco
 */
public class FileSystemStateManager implements ITreeSessionStateManager {
    private String m_strRootDir;

    /**
     * Constructor for FileSystemStateManager.
     */
    public FileSystemStateManager(String strRootDir) {
        super();
        m_strRootDir = strRootDir;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getSessionState(ITreeModel)
     */
    public Object getSessionState(ITreeModel objModel) {
        return objModel.getTreeStateModel();
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager#getModel(Object)
     */
    public ITreeModel getModel(Object objSessionState) {
        ITreeStateModel objStateModel = (ITreeStateModel) objSessionState;

        ITreeNode objParent;

        if (m_strRootDir == null || "".equals(m_strRootDir)) {
            objParent = new FileSystem();
        } else {
            FolderObject objFolder = new FolderObject(null, new File(m_strRootDir), true);
            objFolder.reload();
            objParent = objFolder;
        }

        ITreeDataModel objDataModel = new FileSystemDataModel(objParent);
        ITreeModel objModel = new SimpleTreeModel(objDataModel,
                                                    objStateModel);

        return objModel;
    }
}
