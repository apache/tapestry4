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

package org.apache.tapestry.contrib.table.components;

import org.apache.tapestry.ApplicationRuntimeException;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableRowSource;

/**
 * The base implementation for a component that is wrapped by 
 * the TableRows component. Provides a utility method for getting 
 * a pointer to TableRows. 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class AbstractTableRowComponent extends AbstractTableViewComponent
{
    public ITableRowSource getTableRowSource()
    {
        IRequestCycle objCycle = getPage().getRequestCycle();

        Object objSourceObj = objCycle.getAttribute(ITableRowSource.TABLE_ROW_SOURCE_ATTRIBUTE);
        ITableRowSource objSource = (ITableRowSource) objSourceObj;

        if (objSource == null)
            throw new ApplicationRuntimeException(
                "The component "
                    + getId()
                    + " must be contained within an ITableRowSource component, such as TableRows",
                this,
                null,
                null);

        return objSource;
    }

}
