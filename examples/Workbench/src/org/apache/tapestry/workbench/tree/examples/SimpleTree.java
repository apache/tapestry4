/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2000-2004 The Apache Software Foundation.  All rights
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
