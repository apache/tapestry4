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
import java.util.Vector;

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public class FileSystem implements IFileSystemTreeNode
{

  private transient AssetsHolder m_objAssetsHolder = null;
    /** @associates <{Drive}>
     * @supplierCardinality 0..*
     * @link aggregation*/
    
  private Vector m_vDrives;

  public FileSystem()
  {
    //initDrives();
  }

  private void initDrives()
  {
    m_vDrives = new Vector();
    File[] arrFile = File.listRoots();

    if (arrFile != null)
      for(int i=0; i<arrFile.length; i++)
      {
        m_vDrives.addElement(new Drive(this, arrFile[i]));
      }
  }

  public Vector getDrives()
  {
  	if(m_vDrives == null){
  		initDrives();
  	}
    return m_vDrives;
  }
  public int getChildNumber(Object objChild)
  {
    for(int i=0;i<m_vDrives.size();i++)
    {
      Object objChildDrive = m_vDrives.elementAt(i);
      if(objChildDrive.equals(objChild))
      {
        return i;
      }
    }
    return -1;
  }
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#containsChild(ITreeNode)
	 */
	public boolean containsChild(ITreeNode node) {
		return true;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getAllowsChildren()
	 */
	public boolean getAllowsChildren() {
		return true;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildCount()
	 */
	public int getChildCount() {
		return getDrives().size();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getChildren()
	 */
	public Collection getChildren() {
		return getDrives();
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#getParent()
	 */
	public ITreeNode getParent() {
		return null;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeNode#isLeaf()
	 */
	public boolean isLeaf() {
		return false;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getName();
	}
	public String getName(){
		return "FileSystem";
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object arg0) {
		if(!(arg0 instanceof FileSystem))
			return false;
		FileSystem objFileSystem = (FileSystem)arg0;
		if(getName().equals(objFileSystem.getName()))
			return true;
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getName().hashCode();
	}

	/**
	 * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAbsolutePath()
	 */
	public String getAbsolutePath() {
		return "";
	}

	/**
	 * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getAssets()
	 */
	public AssetsHolder getAssets() {
		if(m_objAssetsHolder == null){
			m_objAssetsHolder = new AssetsHolder("/org/apache/tapestry/workbench/tree/examples/fsmodel/computer.gif", "/org/apache/tapestry/workbench/tree/examples/fsmodel/computer.gif");
		}
		return m_objAssetsHolder;
	}

	/**
	 * @see org.apache.tapestry.workbench.tree.examples.fsmodel.IFileSystemTreeNode#getObjectDate()
	 */
	public Date getDate() {
		return null;
	}

}
