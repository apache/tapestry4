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

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.tree.components.INodeRenderFactory;
import org.apache.tapestry.contrib.tree.model.ITreeModelSource;
import org.apache.tapestry.valid.RenderString;

/**
 * @author ceco
 * @version $Id$
 */
public class SimpleNodeRenderFactory implements INodeRenderFactory {

	/**
	 * Constructor for SimpleNodeRenderFactory.
	 */
	public SimpleNodeRenderFactory() {
		super();
	}

	/**
	 * @see INodeRenderFactory#getRender
	 */
	public IRender getRenderByID(
		Object objUniqueKey,
		ITreeModelSource objTreeModelSource,
		IRequestCycle cycle)
	{
		Object objValue = objTreeModelSource.getTreeModel().getTreeDataModel().getObject(objUniqueKey);
		return getRender(objValue, objTreeModelSource, cycle);
	}

	/**
	 * @see INodeRenderFactory#getRender
	 */
	public IRender getRender(Object objValue, ITreeModelSource objTreeModelSource, IRequestCycle objCycle) {
		return new RenderString(objValue.toString());
	}

}
