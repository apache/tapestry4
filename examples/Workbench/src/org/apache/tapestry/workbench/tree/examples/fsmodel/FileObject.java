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

package org.apache.tapestry.workbench.tree.examples.fsmodel;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public class FileObject extends SFObject {
    private long m_lSize;

    public FileObject(ITreeNode objParent, File objFile) {
        super(objParent, objFile);
        init();
    }

    protected void init() {
        super.init();
        m_lSize = m_objFile.length();
    }

    public long getSize() {
        return m_lSize;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#containsChild(ITreeNode)
     */
    public boolean containsChild(ITreeNode node) {
        return false;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getAllowsChildren()
     */
    public boolean getAllowsChildren() {
        return false;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildCount()
     */
    public int getChildCount() {
        return 0;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildren()
     */
    public Collection getChildren() {
        return new ArrayList();
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return true;
    }

    /**
     * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAssets()
     */
    public AssetsHolder getAssets() {
        if (m_objAssetsHolder == null) {
            final String a = "/org/apache/tapestry/workbench/tree/examples/fsmodel/file.gif";
            m_objAssetsHolder = new AssetsHolder(a, a);
        }
        return m_objAssetsHolder;
    }
}
