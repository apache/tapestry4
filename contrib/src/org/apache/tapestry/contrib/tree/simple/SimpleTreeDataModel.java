package org.apache.tapestry.contrib.tree.simple;

import java.io.Serializable;
import java.util.Iterator;

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeNode;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * Created on Sep 25, 2002
 *
 * @author ceco
 *
 */
public class SimpleTreeDataModel implements ITreeDataModel, Serializable {

	protected ITreeNode m_objRootNode;
	/**
	 * Constructor for SimpleTreeDataModel.
	 */
	public SimpleTreeDataModel(ITreeNode objRootNode) {
		super();
		m_objRootNode = objRootNode;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getRoot()
	 */
	public Object getRoot() {
		return m_objRootNode;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getChildCount(Object)
	 */
	public int getChildCount(Object objParent) {
		ITreeNode objParentNode = (ITreeNode)objParent;
		
		return objParentNode.getChildCount();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getChildren(Object)
	 */
	public Iterator getChildren(Object objParent) {
		ITreeNode objParentNode = (ITreeNode)objParent;
		return objParentNode.getChildren().iterator();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getObject(Object)
	 */
	public Object getObject(Object objUniqueKey) {
		if(objUniqueKey != null) {
			TreePath objPath = (TreePath)objUniqueKey;
			return objPath.getLastPathComponent();
		}
		return null;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getUniqueKey(Object, Object)
	 */
	public Object getUniqueKey(Object objTarget, Object objParentUniqueKey) {
		TreePath objPath = (TreePath)objParentUniqueKey;
		Object objTargetUID = null;
		if(objPath != null){
			objTargetUID = objPath.pathByAddingChild(objTarget);
		}else{
			objTargetUID = new TreePath(objTarget);
		}
		return objTargetUID;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#isAncestorOf(Object, Object)
	 */
	public boolean isAncestorOf(Object objTargetUniqueKey, Object objParentUniqueKey) {
		TreePath objParentPath = (TreePath)objParentUniqueKey;
		TreePath objTargetPath = (TreePath)objTargetUniqueKey;
		boolean bResult = objParentPath.isDescendant(objTargetPath);
		return bResult;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getParentID(Object)
	 */
	public Object getParentUniqueKey(Object objChildUniqueKey) {
		TreePath objChildPath = (TreePath)objChildUniqueKey;
		TreePath objParentPath = objChildPath.getParentPath();
		if(objParentPath == null)
			return null;
		return objParentPath.getLastPathComponent();
	}

}
