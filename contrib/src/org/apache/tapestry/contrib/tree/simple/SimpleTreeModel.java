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

import org.apache.tapestry.contrib.tree.model.ITreeDataModel;
import org.apache.tapestry.contrib.tree.model.ITreeModel;
import org.apache.tapestry.contrib.tree.model.ITreeStateModel;

/**
 * @author ceco
 * @version $Id$
 */
public class SimpleTreeModel implements ITreeModel, Serializable{

	private ITreeDataModel m_objDataModel;
	private ITreeStateModel m_objTreeStateModel;
	
	/**
	 * Constructor for SimpleTreeModel.
	 */
	public SimpleTreeModel(ITreeDataModel objDataModel) {
		this(objDataModel, new SimpleTreeStateModel());
	}

	public SimpleTreeModel(ITreeDataModel objDataModel, ITreeStateModel objTreeStateModel) {
		super();
		m_objDataModel = objDataModel;
		m_objTreeStateModel = objTreeStateModel;
	}
	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeModel#getTreeDataModel()
	 */
	public ITreeDataModel getTreeDataModel() {
		return m_objDataModel;
	}

	/**
	 * @see org.apache.tapestry.contrib.tree.model.ITreeModel#getTreeStateModel()
	 */
	public ITreeStateModel getTreeStateModel() {
		return m_objTreeStateModel;
	}

}
