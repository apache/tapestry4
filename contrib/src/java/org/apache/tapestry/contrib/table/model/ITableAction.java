// Copyright 2004, 2005, 2006 The Apache Software Foundation
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


/**
 * <p>A Table action that needs to be executed at a later time. 
 * The action may be sorting a given column, or it may be selecting a given page</p>
 * <ul>
 * <li></li> 
 * </ul>
 *
 * @author teo
 * @since 4.1
 */
public interface ITableAction
{
    /**
     * Executes the desired action.
     * 
     * @param objTableModel the table model on which the action should be executed
     */
    void executeTableAction(ITableModel objTableModel);
}
