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

package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.contrib.tree.components.ITreeComponent;
import org.apache.tapestry.contrib.tree.components.TreeView;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.contrib.tree.model.ITreeRowSource;
import org.apache.tapestry.util.ComponentAddress;

/**
 * @author ceco
 * @version $Id$
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
