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
import java.util.Collection;
import java.util.Date;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public abstract class SFObject implements IFileSystemTreeNode{
    protected File m_objFile;
    protected ITreeNode m_objParent;
    private Date m_objDate;
    protected transient AssetsHolder m_objAssetsHolder = null;

    public SFObject(ITreeNode objParent, File objFile) {
        m_objParent = objParent;
        m_objFile = objFile;
//        init();
    }

    protected void init() {
		if(m_objFile.isFile() || m_objFile.isDirectory())
        	m_objDate = new Date(m_objFile.lastModified());
    }

    public String getName() {
        if (m_objFile.getName().equals("")) {
            return m_objFile.toString();
        }
        return m_objFile.getName();
    }

    public Date getDate() {
        return m_objDate;
    }

    public Object getAttributes() {
        return null;
    }

    protected File getFile() {
        return m_objFile;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getParent()
     */
    public ITreeNode getParent() {
        return m_objParent;
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
        return null;
    }

    /**
     * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
     */
    public boolean isLeaf() {
        return false;
    }

    /**
     * @see java.lang.Object#equals(Object)
     */
    public boolean equals(Object arg0) {
        if (!(arg0 instanceof SFObject)) {
            return false;
        }
        SFObject objSF = (SFObject)arg0;
        if (getFile().equals(objSF.getFile())) {
            return true;
        }
        return false;
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return m_objFile.hashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return getName();
    }

    /**
     * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAbsolutePath()
     */
    public String getAbsolutePath() {
        return getFile().getAbsolutePath();
    }
}
