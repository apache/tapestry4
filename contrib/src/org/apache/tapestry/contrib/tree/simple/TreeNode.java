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

package org.apache.tapestry.contrib.tree.simple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.tapestry.contrib.tree.model.IMutableTreeNode;
import org.apache.tapestry.contrib.tree.model.ITreeNode;

/**
 * @author ceco
 * @version $Id$
 */
public class TreeNode implements IMutableTreeNode {

	protected Set m_setChildren;
	protected IMutableTreeNode m_objParentNode;
	
	/**
	 * Constructor for TreeNode.
	 */
	public TreeNode() {
		this(null);
	}
	public TreeNode(IMutableTreeNode parentNode) {
		super();
		m_objParentNode = parentNode;
		m_setChildren = new HashSet();
	}


	public int getChildCount() {
		return m_setChildren.size();
	}

	public ITreeNode getParent() {
		return m_objParentNode;
	}

	public boolean getAllowsChildren() {
		return true;
	}

	public boolean isLeaf() {
		return m_setChildren.size() == 0 ? true:false;
	}

	public Collection children() {
		return m_setChildren;
	}


	public void insert(IMutableTreeNode child) {
		child.setParent(this);
		m_setChildren.add(child);
	}

	public void remove(IMutableTreeNode node) {
		m_setChildren.remove(node);
	}

	public void removeFromParent() {
		m_objParentNode.remove(this);
		m_objParentNode = null;
	}

	public void setParent(IMutableTreeNode newParent) {
		m_objParentNode = newParent;
	}

	public void insert(Collection colChildren){
		for (Iterator iter = colChildren.iterator(); iter.hasNext();) {
			IMutableTreeNode element = (IMutableTreeNode) iter.next();
			element.setParent(this);
			m_setChildren.add(element);
		}
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#containsChild(ITreeNode)
	 */
	public boolean containsChild(ITreeNode node) {
		return m_setChildren.contains(node);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildren()
	 */
	public Collection getChildren() {
		return m_setChildren;
	}

}
