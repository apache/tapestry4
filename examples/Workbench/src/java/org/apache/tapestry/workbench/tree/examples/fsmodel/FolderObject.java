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
import java.util.Vector;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public class FolderObject extends SFObject{

    /**
     * @associates <{File}>
     * @supplierCardinality 0..*
     * @link aggregation
     */
    private Vector m_vFiles = null;

    /**
     * @associates <{FolderObject}>
     * @supplierCardinality 0..*
     * @link aggregation
     */
    private Vector m_vFolders = null;
    private boolean m_bShared;

    public FolderObject(ITreeNode objParent, File objFile, boolean bInvokeInit) {
        super(objParent, objFile);
        if(bInvokeInit)
        	init();
    }

    public void reload() {
        m_vFolders = new Vector();
        m_vFiles = new Vector();

        File[] arrFiles = getFile().listFiles();

        if (arrFiles == null) {
            return;
        }

        for (int i=0; i<arrFiles.length; i++) {
            if (arrFiles[i].isDirectory()) {
                m_vFolders.addElement(new FolderObject(this, arrFiles[i], true));
            } else {
                m_vFiles.addElement(new FileObject(this, arrFiles[i]));
            }
        }
    }

    public boolean isShared() {
        return m_bShared;
    }

    public Vector getFolders() {
        if (m_vFolders == null) {
            reload();
        }
        return m_vFolders;
    }

    public Vector getFiles() {
        if (m_vFiles == null) {
            reload();
        }
        return m_vFiles;
    }

    public int getChildNumber(Object objChild) {
        for(int i = 0; i < m_vFolders.size(); i++) {
            Object objChildFolder = m_vFolders.elementAt(i);
            if (objChildFolder.equals(objChild)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#containsChild(ITreeNode)
     */
    public boolean containsChild(ITreeNode node) {
        return true;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
        return true;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildCount()
     */
    public int getChildCount() {
        return getFolders().size() + getFiles().size();
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildren()
     */
    public Collection getChildren() {
        ArrayList arrChildrens = new ArrayList();
        arrChildrens.addAll(getFolders());
        arrChildrens.addAll(getFiles());
        return arrChildrens;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return false;
    }

    private final static String openImage =
        "/org/apache/tapestry/workbench/tree/examples/fsmodel/TreeOpen.gif";

    private final static String closedImage =
        "/org/apache/tapestry/workbench/tree/examples/fsmodel/TreeClosed.gif";

    public AssetsHolder getAssets() {
        if (m_objAssetsHolder == null) {
            m_objAssetsHolder = new AssetsHolder(openImage, closedImage);
        }
        return m_objAssetsHolder;
    }
}
