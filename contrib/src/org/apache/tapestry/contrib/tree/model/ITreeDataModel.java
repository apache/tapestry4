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
package org.apache.tapestry.contrib.tree.model;

import java.util.Iterator;

/**
 * The interface that defines a suitable data model for a <code>TreeView component</code>. 
 * 
 * @author ceco
 * @version $Id$
 */
public interface ITreeDataModel
{
	/**
	 * Returns the root node of the tree
	 */
	Object getRoot();

	/**
	 * Returns the number of children of parent node.
	 * @param objParent is the parent object whose nr of children are sought
	 */
	int getChildCount(Object objParent);

	/**
	 * Get an iterator to the Collection of children belonging to the parent node object
	 * @param objParent is the parent object whose children are requested
	 */
	Iterator getChildren(Object objParent);

	/**
	 * Get the actual node object based on some identifier (for example an UUID)
	 * @param objUniqueKey is the unique identifier of the node object being retrieved
	 * @return the instance of the node object identified by the key
	 */
	Object getObject(Object objUniqueKey);

	/** 
	 * Get the unique identifier (UUID) of the node object with a certain parent node
	 * @param objTarget is the Object whose identifier is required
	 * @param objParentUniqueKey is the unique id of the parent of objTarget
	 * @return the unique identifier of objTarget
	 */
	Object getUniqueKey(Object objTarget, Object objParentUniqueKey);

	/**
	 * Get the unique identifier of the parent of an object
	 * @param objChildUniqueKey is the identifier of the Object for which the parent identifier is sought
	 * @return the identifier (possibly UUID) of the parent of objChildUniqueKey
	 */
	Object getParentUniqueKey(Object objChildUniqueKey);

	/**
	 * Check to see (on the basis of some node object identifier) whether the parent node is indeed the parent of the object
	 * @param objChildUniqueKey is the identifier of the object whose parent is being checked
	 * @param objParentUniqueKey is the identifier of the parent which is to be checked against
	 */
	boolean isAncestorOf(Object objChildUniqueKey, Object objParentUniqueKey);
	
}
