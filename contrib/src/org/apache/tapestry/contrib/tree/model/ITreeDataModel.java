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
