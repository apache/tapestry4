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
import org.apache.tapestry.BaseComponent;
import org.apache.tapestry.IRequestCycle;
import org.apache.tapestry.contrib.table.model.ITableModelSource;

/**
 * The base implementation for a component that is wrapped by 
 * the TableView component. Provides a utility method for getting 
 * a pointer to TableView. 
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public class AbstractTableViewComponent extends BaseComponent
{
    public ITableModelSource getTableModelSource()
    {
        IRequestCycle objCycle = getPage().getRequestCycle();

        ITableModelSource objSource =
            (ITableModelSource) objCycle.getAttribute(
                ITableModelSource.TABLE_MODEL_SOURCE_ATTRIBUTE);

        if (objSource == null)
            throw new ApplicationRuntimeException(
                "The component "
                    + getId()
                    + " must be contained within an ITableModelSource component, such as TableView",
                this,
                null,
                null);

        return objSource;
    }

}
