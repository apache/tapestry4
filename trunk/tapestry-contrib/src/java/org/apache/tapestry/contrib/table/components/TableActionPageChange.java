// Copyright 2004, 2005 The Apache Software Foundation
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

import org.apache.tapestry.contrib.table.model.ITableAction;
import org.apache.tapestry.contrib.table.model.ITableModel;


/**
 * <p>A table action that changes the current table page</p>.
 *
 * @author teo
 */
public class TableActionPageChange implements ITableAction
{
    private int _page;
    
    public TableActionPageChange(int page)
    {
        super();
        _page = page;
    }

    /**
     * {@inheritDoc}
     */
    public void executeTableAction(ITableModel objTableModel)
    {
        objTableModel.getPagingState().setCurrentPage(_page - 1);
    }
}
