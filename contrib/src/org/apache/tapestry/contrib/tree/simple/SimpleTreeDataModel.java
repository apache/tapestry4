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

import java.io.Serializable;
import java.util.Iterator;

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeNode;

/**
 * @author ceco
 * @version $Id$
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
	 * @see org.apache.tapestry.contrib.tree.model.ITreeDataModel#getParentUniqueKey
	 */
	public Object getParentUniqueKey(Object objChildUniqueKey) {
		TreePath objChildPath = (TreePath)objChildUniqueKey;
		TreePath objParentPath = objChildPath.getParentPath();
		if(objParentPath == null)
			return null;
		return objParentPath.getLastPathComponent();
	}

}
