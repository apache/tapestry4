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

import org.apache.tapestry.IComponent;
import org.apache.tapestry.contrib.table.model.ITableColumnModel;

/**
 * Service used to generate a {@link org.apache.tapestry.contrib.table.model.ITableColumnModel}from
 * a string description.
 * 
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public interface TableColumnModelSource
{
    /**
     * Generate a table column model out of the description string provided. Entries in the
     * description string are separated by commas. Each column entry is of the format name,
     * name:expression, or name:displayName:expression. An entry prefixed with ! represents a
     * non-sortable column. If the whole description string is prefixed with *, it represents
     * columns to be included in a Form.
     * 
     * @param strDesc
     *            the description of the column model to be generated
     * @param objComponent
     *            the component ordering the generation
     * @param objColumnSettingsContainer
     *            the component containing the column settings
     * @return a table column model based on the provided parameters
     */
    public ITableColumnModel generateTableColumnModel(String strDesc, IComponent objComponent,
            IComponent objColumnSettingsContainer);
}