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
