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
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public class FileObject extends SFObject {
    private long m_lSize;

    public FileObject(ITreeNode objParent, File objFile) {
        super(objParent, objFile);
        init();
    }

    protected void init() {
        super.init();
        m_lSize = m_objFile.length();
    }

    public long getSize() {
        return m_lSize;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#containsChild(ITreeNode)
     */
    public boolean containsChild(ITreeNode node) {
        return false;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildCount()
     */
    public int getChildCount() {
        return 0;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildren()
     */
    public Collection getChildren() {
        return new ArrayList();
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return true;
    }

    /**
     * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAssets()
     */
    public AssetsHolder getAssets() {
        if (m_objAssetsHolder == null) {
            final String a = "/org/apache/tapestry/workbench/tree/examples/fsmodel/file.gif";
            m_objAssetsHolder = new AssetsHolder(a, a);
        }
        return m_objAssetsHolder;
    }
}
