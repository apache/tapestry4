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

package org.apache.tapestry.contrib.tree.simple;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry.contrib.tree.model.ITreeStateModel;

/**
 * @author ceco
 * @version $Id$
 */
public class SimpleTreeStateModel implements ITreeStateModel, Serializable{

	private Set m_setExpanded;
	private Object m_objSelectedNodeUID = null;
	
	/**
	 * Constructor for SimpleTreeStateModel.
	 */
	public SimpleTreeStateModel() {
		super();
		initialize();
	}
	private void initialize(){
		m_setExpanded = new HashSet();
		m_objSelectedNodeUID = null;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#getExpandSelection()
	 */
	public Set getExpandSelection() {
		return m_setExpanded;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#expand(Object)
	 */
	public void expand(Object objUniqueKey) {
		m_setExpanded.add(objUniqueKey);
		setSelectedNode(objUniqueKey);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#expandPath(Object)
	 */
	public void expandPath(Object objUniqueKey) {
		m_setExpanded.add(objUniqueKey);
		setSelectedNode(objUniqueKey);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#collapse(Object)
	 */
	public void collapse(Object objUniqueKey) {
		m_setExpanded.remove(objUniqueKey);
		setSelectedNode(objUniqueKey);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#collapsePath(Object)
	 */
	public void collapsePath(Object objUniqueKey) {
		m_setExpanded.remove(objUniqueKey);
		setSelectedNode(objUniqueKey);
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#isUniqueKeyExpanded(Object)
	 */
	public boolean isUniqueKeyExpanded(Object objUniqueKey) {
		return m_setExpanded.contains(objUniqueKey);
	}
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#getSelectedNode()
	 */
	public Object getSelectedNode() {
		return m_objSelectedNodeUID;
	}
	private void setSelectedNode(Object objUniqueKey){
		if(m_objSelectedNodeUID == null || !m_objSelectedNodeUID.equals(objUniqueKey))
			m_objSelectedNodeUID = objUniqueKey;
	}
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeStateModel#resetState()
	 */
	public void resetState() {
		initialize();
	}

}
