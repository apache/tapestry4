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

package org.apache.tapestry.contrib.tree.model;

/**
 * @author ceco
 */
public class TreeRowObject {
    public static final int FIRST_ROW		= 1;
    public static final int MIDDLE_ROW		= 2;
    public static final int LAST_ROW		= 3;
    public static final int FIRST_LAST_ROW	= 4;

    public static final int EMPTY_CONN_IMG	= 1;
    public static final int LINE_CONN_IMG	= 2;

    private Object m_objTreeNode = null;
	private Object m_objTreeNodeUID = null;
	private int m_nTreeRowDepth;
	private boolean m_bLeaf = false;
	private int m_nTreeRowPossiotionType = MIDDLE_ROW;
	private int[] m_nLineConnImages;

	public TreeRowObject(Object objTreeNode, Object objTreeNodeUID, int nTreeRowDepth, boolean bLeaf, int nTreeRowPossiotionType, int[] nLineConnImages) {
		super();
		m_objTreeNode = objTreeNode;
		m_objTreeNodeUID = objTreeNodeUID;
		m_nTreeRowDepth = nTreeRowDepth;
		m_bLeaf = bLeaf;
		m_nTreeRowPossiotionType = nTreeRowPossiotionType;
		m_nLineConnImages = nLineConnImages;
	}

	public Object getTreeNode() {
		return m_objTreeNode;
	}

	public Object getTreeNodeUID() {
		return m_objTreeNodeUID;
	}

	public int getTreeRowDepth() {
		return m_nTreeRowDepth;
	}

	/**
	 * @return Returns the leaf.
	 */
	public boolean getLeaf() {
		return m_bLeaf;
	}
	/**
	 * @return Returns the treeRowPossiotionType.
	 */
	public int getTreeRowPossiotionType() {
		return m_nTreeRowPossiotionType;
	}
	/**
	 * @return Returns the lineConnImages.
	 */
	public int[] getLineConnImages() {
		return m_nLineConnImages;
	}
}
