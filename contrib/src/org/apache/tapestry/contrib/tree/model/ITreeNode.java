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

import java.io.Serializable;
import java.util.Collection;

/**
 * @author ceco
 * @version $Id$
 */

public interface ITreeNode extends Serializable
{
	
    /**
     * Returns the <code>Collection</code> of children. 
     */
    Collection getChildren();

    /**
     * Returns the number of children <code>ITreeNode</code>s the receiver
     * contains.
     */
    int getChildCount();

    /**
     * Returns the parent <code>ITreeNode</code> of the receiver.
     */
    ITreeNode getParent();

    /**
     * Returns the true if current node contains received children, otherwise return false;
     */
    boolean containsChild(ITreeNode node);

    /**
     * Returns true if the receiver allows children.
     */
    boolean getAllowsChildren();

    /**
     * Returns true if the receiver is a leaf.
     */
    boolean isLeaf();

}
