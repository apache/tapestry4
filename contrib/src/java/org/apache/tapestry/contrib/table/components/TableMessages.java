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

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class TableMessages
{
    protected static MessageFormatter _formatter = new MessageFormatter(TableMessages.class,
            "TableStrings");

    static String notAColumn(IComponent component, String expression)
    {
        return _formatter.format("not-a-column", component.getExtendedId(), expression);
    }

    static String invalidTableSource(IComponent component, Object sourceValue)
    {
        return _formatter.format("invalid-table-source", component.getExtendedId(), ClassFabUtils
                .getJavaClassName(sourceValue.getClass()));
    }

    static String invalidTableColumns(IComponent component, Object columnSource)
    {
        return _formatter.format("invalid-table-column", component.getExtendedId(), ClassFabUtils
                .getJavaClassName(columnSource.getClass()));
    }

    static String missingTableModel(IComponent component)
    {
        return _formatter.format("missing-table-model", component.getExtendedId());
    }

    static String columnsOnlyPlease(IComponent component)
    {
        return _formatter.format("columns-only-please", component.getExtendedId());
    }
}