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

package org.apache.tapestry.contrib.table.model;

import org.apache.tapestry.IComponent;

/**
 * A Tapestry component that provides the current table model.
 * This interface is used for obtaining the table model source by
 * components wrapped by it, as well as by external renderers,
 * such as those provided by the column implementations
 * 
 * @version $Id$
 * @author mindbridge
 */
public interface ITableModelSource extends IComponent
{
    final static String TABLE_MODEL_SOURCE_ATTRIBUTE = "org.apache.tapestry.contrib.table.model.ITableModelSource";

	/**
	 * Returns the table model currently used
	 * @return ITableModel the current table model
	 */
	ITableModel getTableModel();

	/**
	 * Notifies the model source that the model state has changed, and 
     * that it should consider saving it.<p>
     * This method was added to allow using the table within a Block when 
     * the pageBeginRender() listener of the implementation will not be called
     * and automatic state storage will therefore be hard to implement.
	 */
    void fireObservedStateChange();
}
