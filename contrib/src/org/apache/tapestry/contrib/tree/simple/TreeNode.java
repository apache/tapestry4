package org.apache.tapestry.contrib.tree.simple;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.tapestry.contrib.tree.model.IMutableTreeNode;
import org.apache.tapestry.contrib.tree.model.ITreeNode;

/**
 * @author ceco
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


	/**
	 * @see com.rushmore.components.tree.ITreeNode#getChildCount()
	 */
	public int getChildCount() {
		return m_setChildren.size();
	}

	/**
	 * @see com.rushmore.components.tree.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {
		return m_objParentNode;
	}

	/**
	 * @see com.rushmore.components.tree.ITreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * @see com.rushmore.components.tree.ITreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return m_setChildren.size() == 0 ? true:false;
	}

	/**
	 * @see com.rushmore.components.tree.ITreeNode#children()
	 */
	public Collection children() {
		return m_setChildren;
	}


	/**
	 * @see com.rushmore.components.tree.IMutableTreeNode#insert(IMutableTreeNode, int)
	 */
	public void insert(IMutableTreeNode child) {
		child.setParent(this);
		m_setChildren.add(child);
	}

	/**
	 * @see com.rushmore.components.tree.IMutableTreeNode#remove(IMutableTreeNode)
	 */
	public void remove(IMutableTreeNode node) {
		m_setChildren.remove(node);
	}

	/**
	 * @see com.rushmore.components.tree.IMutableTreeNode#removeFromParent()
	 */
	public void removeFromParent() {
		m_objParentNode.remove(this);
		m_objParentNode = null;
	}

	/**
	 * @see com.rushmore.components.tree.IMutableTreeNode#setParent(IMutableTreeNode)
	 */
	public void setParent(IMutableTreeNode newParent) {
		m_objParentNode = newParent;
	}

	/**
	 * @see com.rushmore.components.tree.IMutableTreeNode#insert(Collection)
	 */
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
