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

package org.apache.tapestry.workbench.tree.examples;

import org.apache.tapestry.contrib.tree.model.IMutableTreeNode;
import org.apache.tapestry.contrib.tree.simple.TreeNode;

/**
 * @author ceco
 */
public class TestTreeNode extends TreeNode {

    private String m_strValue;

    /**
     * Constructor for TestTreeNode.
     */
    public TestTreeNode(String strValue) {
        this(null, strValue);
    }

    /**
     * Constructor for TestTreeNode.
     * @param parentNode
     */
    public TestTreeNode(IMutableTreeNode parentNode, String strValue) {
        super(parentNode);
        m_strValue = strValue;
    }

    public String toString(){
        return m_strValue;
    }

    public int hashCode(){
        return m_strValue.hashCode();
    }

    public boolean equals(Object objTarget){
        if(objTarget == this)
            return true;
        if(! (objTarget instanceof TestTreeNode))
            return false;

        TestTreeNode objTargetNode = (TestTreeNode)objTarget;
        return this.getValue().equals(objTargetNode.getValue());
    }

    /**
     * Returns the value.
     * @return String
     */
    public String getValue() {
        return m_strValue;
    }

}
