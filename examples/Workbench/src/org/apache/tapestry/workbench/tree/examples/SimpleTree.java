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

import java.util.Date;

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeModel;
import org.apache.tapestry.contrib.tree.simple.SimpleTreeDataModel;

import org.apache.tapestry.IEngine;
import org.apache.tapestry.html.BasePage;

/**
 * Simple example page.
 *
 * @version $Revision$
 */
public class SimpleTree extends BasePage {
    private ITreeDataModel treeDataModel;
    private ITreeModel treeModel;
    private Object value;

    public SimpleTree() {
    }

    public void attach(IEngine value) {
        super.attach(value);
    }

    public void detach() {
        super.detach();
        treeDataModel = null;
        value = null;
    }

    public void init() {
        TestTreeNode objParent = new TestTreeNode("Parent");

        TestTreeNode objChild1 = new TestTreeNode("Child1");
        TestTreeNode objChild2 = new TestTreeNode("Child2");
        TestTreeNode objChild3 = new TestTreeNode("Child3");

        objParent.insert(objChild3);
        objParent.insert(objChild2);
        objParent.insert(objChild1);

        TestTreeNode objChild31 = new TestTreeNode("Child31");
        TestTreeNode objChild32 = new TestTreeNode("Child32");

        objChild3.insert(objChild32);
        objChild3.insert(objChild31);

        treeDataModel = new SimpleTreeDataModel(objParent);
        treeModel = new SimpleTreeModel(treeDataModel);
    }

    public Date getCurrentTime() {
        return new Date();
    }

    public ITreeModel getTreeModel() {
        if(treeModel == null) {
            init();
        }
        return treeModel;
    }

    /**
     * Returns the value.
     * @return Object
     */
    public Object getValue() {
        return value;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(Object value) {
        this.value = value;
    }
}
