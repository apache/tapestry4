/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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

package org.apache.tapestry.workbench.tree.examples;

import java.io.File;
import java.util.Collection;
import java.util.Date;

import org.apache.tapestry.contrib.tree.components.INodeRenderFactory;
import org.apache.tapestry.contrib.tree.components.TreeView;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystem;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystemDataModel;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystemStateManager;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FolderObject;
import org.apache.tapestry.workbench.tree.examples.fsmodel.NodeRenderFactory;
import org.apache.tapestry.contrib.tree.model.TreeStateEvent;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeNode;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;
import org.apache.tapestry.contrib.tree.model.ITreeStateListener;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeModel;

import org.apache.tapestry.html.BasePage;

public class FileSystemTree extends BasePage implements ISelectedFolderSource, ITreeStateListener{
    private ITreeSessionStateManager m_objTreeSessionStateManager = null;
    private ITreeDataModel m_objDataModel;
    private ITreeModel m_objModel;
    private Object m_objValue;

    public FileSystemTree() {
        super();
    }

	protected void initialize() {
		super.initialize();
        m_objDataModel = null;
        m_objValue = null;
        m_objTreeSessionStateManager = null;
    }

    public void initTableModel() {
        ITreeNode objParent;
        String strRootDir = getRequestCycle().getRequestContext().getServlet().getInitParameter("TreeRootDir");

        System.out.println("strRootDir = " + strRootDir);

        if (strRootDir == null || "".equals(strRootDir)) {
            objParent = new FileSystem();
        } else{
            FolderObject objFolder = new FolderObject(null, new File(strRootDir), true);
            objFolder.reload();
            objParent = objFolder;
        }

        m_objDataModel = new FileSystemDataModel(objParent);
        m_objModel = new SimpleTreeModel(m_objDataModel);
    }

    public Date getCurrentTime() {
        return new Date();
    }

    public ITreeModel getTreeModel() {
        if (m_objDataModel == null) {
            initTableModel();
        }
        return m_objModel;
    }

    /**
     * Returns the value.
     * @return Object
     */
    public Object getValue() {
        return m_objValue;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(Object value) {
        m_objValue = value;
    }

    public INodeRenderFactory getRenderFactory() {
        return new NodeRenderFactory();
    }

    public ITreeSessionStateManager getSessionStateManager() {
		//IPage objPage = getRequestCycle().getPage("contrib:TreeNodeViewPage");
		//System.out.println("TreeNodeViewPage NamespaceId : "+objPage.getNamespace().getNamespaceId());
        
        if (m_objTreeSessionStateManager == null) {
            String strRootDir = getRequestCycle().getRequestContext().getServlet().getInitParameter("TreeRootDir");
            //System.out.println("strRootDir = " + strRootDir);

            m_objTreeSessionStateManager =
                new FileSystemStateManager(strRootDir);
        }
        return m_objTreeSessionStateManager;
    }
	/**
	 * @see org.apache.tapestry.workbench.tree.examples.ISelectedFolderSource#getSelectedFolder()
	 */
	public Collection getSelectedFolderChildren() {
		TreeView objTreeView = (TreeView)getComponent("treeView");
		ITreeStateModel objTreeStateModel = objTreeView.getTreeModel().getTreeStateModel();
		Object objSelectedNodeUID = objTreeStateModel.getSelectedNode();
		ITreeNode objSelectedNode = null;
		if(objSelectedNodeUID != null)
			objSelectedNode = (ITreeNode)getTreeModel().getTreeDataModel().getObject(objSelectedNodeUID);
		else{
			objSelectedNode = (ITreeNode)getTreeModel().getTreeDataModel().getRoot();
		}
		return objSelectedNode.getChildren();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateListener#treeStateChanged(org.apache.tapestry.contrib.tree.model.TreeStateEvent)
	 */
	public void treeStateChanged(TreeStateEvent objEvent) {
		DirectoryTableView objDirectoryTableView = (DirectoryTableView)getComponent("directoryTableView");
		objDirectoryTableView.resetState();
	}

	public ITreeStateListener getTreeStateListener(){
		return this;
	}

	public ISelectedFolderSource getSelectedFolderSource(){
		return this;
	}
	
	/**
	 * @see org.apache.tapestry.workbench.tree.examples.ISelectedFolderSource#getSelectedNodeName()
	 */
	public String getSelectedNodeName() {
		TreeView objTreeView = (TreeView)getComponent("treeView");
		ITreeStateModel objTreeStateModel = objTreeView.getTreeModel().getTreeStateModel();
		Object objSelectedNodeUID = objTreeStateModel.getSelectedNode();
		ITreeNode objSelectedNode = null;
		if(objSelectedNodeUID != null)
			objSelectedNode = (ITreeNode)getTreeModel().getTreeDataModel().getObject(objSelectedNodeUID);
		else{
			objSelectedNode = (ITreeNode)getTreeModel().getTreeDataModel().getRoot();
		}
		return objSelectedNode.toString();
	}

}
