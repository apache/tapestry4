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

import org.apache.tapestry.contrib.table.model.ITableModelSource;

/**
 * A modified version of the facade component in the Table family. 
 * FormTable allows you to present a sortable and pagable table 
 * within a form by using only this one component.
 * 
 *  [<a href="../../../../../../../ComponentReference/contrib.FormTable.html">Component Reference</a>]
 * 
 * @author mindbridge
 * @version $Id$
 *
 */
public abstract class FormTable extends Table implements ITableModelSource
{
    // parameters
    public abstract Object getColumns();

    /**
     *  If the columns are defined via a String, make sure they use 
     *  the form-specific column headers.
     */
    public Object getFormColumns()
    {
        Object objColumns = getColumns();
        if (objColumns instanceof String)
            objColumns = "*" + objColumns;
        return objColumns;
    }

}
