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

import java.io.Serializable;

import org.apache.tapestry.IRender;
import org.apache.tapestry.IRequestCycle;

/**
 * This interface provides a renderer to present the data in a table column.
 * It is usually used by the {@link org.apache.tapestry.contrib.table.model.ITableColumn} 
 * implementations via aggregation.
 * 
 * @see org.apache.tapestry.contrib.table.model.common.AbstractTableColumn
 * @version $Id$
 * @author mindbridge
 * @since 2.3
 */
public interface ITableRendererSource extends Serializable
{
	/**
	 * Returns a renderer to present the data of the row in the given column. <p>
	 * This method can also be used to return a renderer to present the
	 * heading of the column. In such a case the row passed would be null.
	 * 
	 * @see org.apache.tapestry.contrib.table.model.ITableColumn#getValueRenderer(IRequestCycle, ITableModelSource, Object)
	 */
	public IRender getRenderer(
		IRequestCycle objCycle,
		ITableModelSource objSource,
		ITableColumn objColumn,
		Object objRow);

}
