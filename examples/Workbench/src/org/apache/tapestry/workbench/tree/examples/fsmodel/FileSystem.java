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
