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

package org.apache.tapestry.contrib.tree.model;

/**
 * @author ceco
 * @version $Id$
 */
public class TreeStateEvent {
	public static final int SELECTED_NODE_CHANGED 	= 1;
	public static final int NODE_EXPANDED 			= 2;
	public static final int NODE_COLLAPSED 			= 4;
	
	private int m_nEventType;
	private transient ITreeStateModel m_objTreeStateModel = null;
	private transient Object m_objNodeUID = null;

	/**
	 * Constructor for TreeStateEvent.
	 */
	public TreeStateEvent(int nEventType, Object objNodeUID, ITreeStateModel objTreeStateModel) {
		super();
		m_nEventType = nEventType;
		m_objNodeUID = objNodeUID;
		m_objTreeStateModel = objTreeStateModel;
	}

	/**
	 * Returns the EventType.
	 * @return int
	 */
	public int getEventType() {
		return m_nEventType;
	}

    public boolean isEvent(int nEventType){
		return (getEventType() & nEventType) > 0;
	}

	public Object getNodeUID() {
		return m_objNodeUID;
	}

	public ITreeStateModel getTreeStateModel() {
		return m_objTreeStateModel;
	}
}
