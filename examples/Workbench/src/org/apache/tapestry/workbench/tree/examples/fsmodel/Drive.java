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

import org.apache.tapestry.contrib.tree.model.ITreeNode;

public class Drive extends FolderObject {
	private String m_strType;
	private String m_strLabel;
	private long m_lSize;

	public Drive(ITreeNode objParent, File objFile) {
		super(objParent, objFile, false);
	}
	public long getSize() {
		return m_lSize;
	}
	public String getType() {
		return m_strType;
	}
	public String getLabel() {
		return m_strLabel;
	}

	public AssetsHolder getAssets() {
		if (m_objAssetsHolder == null) {
			m_objAssetsHolder =
				new AssetsHolder(
					"/org/apache/tapestry/workbench/tree/examples/fsmodel/harddrive.gif",
					"/org/apache/tapestry/workbench/tree/examples/fsmodel/harddrive.gif");
		}
		return m_objAssetsHolder;
	}
}
