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

package org.apache.tapestry.contrib.tree.components.table;

import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableColumn;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.ITableRendererListener;

/**
 * @author ceco
 * @version $Id$
 */
public class TreeTableNodeViewDelegator extends BaseComponent implements ITableRendererListener{

	/**
	 * 
	 */
	public TreeTableNodeViewDelegator() {
		super();
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.ITableRendererListener#initializeRenderer(org.apache.tapestry.IRequestCycle, org.apache.tapestry.contrib.table.model.ITableModelSource, org.apache.tapestry.contrib.table.model.ITableColumn, java.lang.Object)
	 */
	public void initializeRenderer(
		IRequestCycle arg0,
		ITableModelSource arg1,
		ITableColumn arg2,
		Object arg3) {
	}

}
