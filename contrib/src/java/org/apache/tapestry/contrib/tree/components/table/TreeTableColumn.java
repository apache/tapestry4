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

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModelSource;
import org.apache.tapestry.contrib.table.model.simple.SimpleTableColumn;
import org.apache.tapestry.util.ComponentAddress;

/**
 * @author ceco
 * @version $Id$
 */
public class TreeTableColumn extends SimpleTableColumn {

	/**
	 * @param arg0
	 * @param arg1
	 */
	public TreeTableColumn(String arg0, boolean arg1, ComponentAddress objComponentAddress) {
		super(arg0, arg1);
		setValueRendererSource(new TreeTableValueRenderSource(objComponentAddress));
	}

	/**
	 * @see org.apache.tapestry.contrib.table.model.common.AbstractTableColumn#getValueRenderer(org.apache.tapestry.IRequestCycle, org.apache.tapestry.contrib.table.model.ITableModelSource, java.lang.Object)
	 */
	public IRender getValueRenderer(IRequestCycle arg0, ITableModelSource arg1, Object arg2) {
		return super.getValueRenderer(arg0, arg1, arg2);
	}

}
