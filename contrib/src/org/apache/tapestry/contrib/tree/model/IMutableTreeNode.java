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

import java.util.Collection;

/**
 * Defines the requirements for a tree node object that can change --
 * by adding or removing child nodes, or by changing the contents
 * of a user object stored in the node.
 *
 * @see javax.swing.tree.DefaultMutableTreeNode
 * @see javax.swing.JTree
 *
 * @author ceco
 * @version $Id$
 */

public interface IMutableTreeNode extends ITreeNode
{
    /**
     * Adds collection of<code>children</code> to the receiver.
     * <code>Child</code> will be messaged with <code>setParent</code>.
     */
    void insert(Collection colChildren);

    /**
     * Removes <code>node</code> from the receiver. <code>setParent</code>
     * will be messaged on <code>node</code>.
     */
    void remove(IMutableTreeNode node);

    /**
     * Removes the receiver from its parent.
     */
    void removeFromParent();

    /**
     * Sets the parent of the receiver to <code>newParent</code>.
     */
    void setParent(IMutableTreeNode newParent);
}
