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
import java.util.Collection;
import java.util.Date;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public abstract class SFObject implements IFileSystemTreeNode{
    protected File m_objFile;
    protected ITreeNode m_objParent;
    private Date m_objDate;
    protected transient AssetsHolder m_objAssetsHolder = null;

    public SFObject(ITreeNode objParent, File objFile) {
        m_objParent = objParent;
        m_objFile = objFile;
//        init();
    }

    protected void init() {
		if(m_objFile.isFile() || m_objFile.isDirectory())
        	m_objDate = new Date(m_objFile.lastModified());
    }

    public String getName() {
        if (m_objFile.getName().equals("")) {
            return m_objFile.toString();
        }
        return m_objFile.getName();
    }

    public Date getDate() {
        return m_objDate;
    }

    public Object getAttributes() {
        return null;
    }

    protected File getFile() {
        return m_objFile;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getParent()
     */
    public ITreeNode getParent() {
        return m_objParent;
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
        return null;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return false;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof SFObject)) {
            return false;
        }
        SFObject objSF = (SFObject)arg0;
        if (getFile().equals(objSF.getFile())) {
            return true;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return m_objFile.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }

    /**
     * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAbsolutePath()
     */
    public String getAbsolutePath() {
        return getFile().getAbsolutePath();
    }
}
