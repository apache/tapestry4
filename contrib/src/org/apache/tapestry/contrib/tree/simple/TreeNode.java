/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2003 The Apache Software Foundation.  All rights
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
