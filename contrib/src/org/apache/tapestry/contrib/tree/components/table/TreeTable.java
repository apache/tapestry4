package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.contrib.tree.components.ITreeComponent;
import org.apache.tapestry.contrib.tree.components.TreeView;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 2, 2003
 * 
 * @author ceco
 */
public class TreeTable extends BaseComponent implements ITreeComponent{

	/**
	 * 
	 */
	public TreeTable() {
		super();
	}

	public ITreeModelSource getTreeModelSource(){
		return (ITreeModelSource) getComponent("treeView");
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.components.ITreeComponent#resetState()
	 */
	public void resetState() {
		TreeView objTreeView = (TreeView)getComponent("treeView");
		objTreeView.resetState();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.components.ITreeComponent#getComponentPath()
	 */
	public ComponentAddress getComponentPath() {
		return new ComponentAddress(this);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.components.ITreeComponent#getTreeRowSource()
	 */
	public ITreeRowSource getTreeRowSource() {
		TreeTableDataView objTreeDataView = (TreeTableDataView)getComponent("treeTableDataView");
		return objTreeDataView;
	}
}
