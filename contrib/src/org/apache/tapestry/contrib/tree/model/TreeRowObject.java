package org.apache.tapestry.contrib.tree.model;

/**
 * All right reserved.
 * Copyright (c) by Rushmore Digital Ltd.
 * 
 * Created on Sep 9, 2003
 * 
 * @author ceco
 */
public class TreeRowObject {
	private Object m_objTreeNode = null;
	private Object m_objTreeNodeUID = null;
	private int m_nTreeRowDepth;

	/**
	 * 
	 */
	public TreeRowObject(Object objTreeNode, Object objTreeNodeUID, int nTreeRowDepth) {
		super();
		m_objTreeNode = objTreeNode;
		m_objTreeNodeUID = objTreeNodeUID;
		m_nTreeRowDepth = nTreeRowDepth;
	}

	/**
	 * @return
	 */
	public Object getTreeNode() {
		return m_objTreeNode;
	}

	/**
	 * @return
	 */
	public Object getTreeNodeUID() {
		return m_objTreeNodeUID;
	}

	/**
	 * @return
	 */
	public int getTreeRowDepth() {
		return m_nTreeRowDepth;
	}

}
