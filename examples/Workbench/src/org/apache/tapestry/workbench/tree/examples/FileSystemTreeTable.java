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

package org.apache.tapestry.workbench.tree.examples;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.contrib.tree.components.INodeRenderFactory;
import org.apache.tapestry.contrib.tree.components.table.TreeTable;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystem;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystemDataModel;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FileSystemStateManager;
import org.apache.tapestry.workbench.tree.examples.fsmodel.FolderObject;
import org.apache.tapestry.workbench.tree.examples.fsmodel.NodeRenderFactory;
import org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode;
import org.apache.tapestry.contrib.tree.model.TreeRowObject;
import org.apache.tapestry.contrib.tree.model.TreeStateEvent;
import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeNode;
import org.apache.tapestry.contrib.tree.model.ITreeSessionStateManager;
import org.apache.tapestry.contrib.tree.model.ITreeStateListener;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeModel;
import org.apache.tapestry.html.BasePage;

public class FileSystemTreeTable extends BasePage implements ISelectedFolderSource, ITreeStateListener{
    private ITreeSessionStateManager m_objTreeSessionStateManager = null;
    private ITreeDataModel m_objDataModel;
    private ITreeModel m_objModel;
    private Object m_objValue;
	private ArrayList m_arrTableColumns = null;

    public FileSystemTreeTable() {
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
        if (m_objTreeSessionStateManager == null) {
            String strRootDir = getRequestCycle().getRequestContext().getServlet().getInitParameter("TreeRootDir");
//            System.out.println("strRootDir = " + strRootDir);

            m_objTreeSessionStateManager =
                new FileSystemStateManager(strRootDir);
        }
        return m_objTreeSessionStateManager;
    }
	
	/**
	 * @return
	 */
	public ArrayList getTableColumns() {
		if(m_arrTableColumns == null){
			m_arrTableColumns = new ArrayList();
			m_arrTableColumns.add(new SimpleTableColumn ("Date", true) 
				{ 
					public Object getColumnValue(Object objValue) {
						TreeRowObject objRowObject = (TreeRowObject)objValue;
						//SFObject objSFObject = (SFObject)objRowObject.getTreeNode();
						IFileSystemTreeNode objFileSystemTreeNode = (IFileSystemTreeNode)objRowObject.getTreeNode();
						return objFileSystemTreeNode.getDate();
					}
				});
		}
		return m_arrTableColumns;
	}

	/**
	 * @see org.apache.tapestry.workbench.tree.examples.ISelectedFolderSource#getSelectedFolder()
	 */
	public Collection getSelectedFolderChildren() {
		TreeTable objTreeTable = (TreeTable)getComponent("tree");
		ITreeModelSource objTreeModelSource = objTreeTable.getTreeModelSource();
		ITreeStateModel objTreeStateModel = objTreeModelSource.getTreeModel().getTreeStateModel();
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
		TreeTable objTreeTable = (TreeTable)getComponent("tree");
		ITreeModelSource objTreeModelSource = objTreeTable.getTreeModelSource();
		ITreeStateModel objTreeStateModel = objTreeModelSource.getTreeModel().getTreeStateModel();
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
