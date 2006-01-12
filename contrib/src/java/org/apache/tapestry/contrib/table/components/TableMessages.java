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

package org.apache.tapestry.contrib.table.components;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public final class TableMessages
{

    private static final Messages MESSAGES = new MessageFormatter(TableMessages.class);

    /** @since 4.1 */
    private TableMessages()
    {
    }

    static String notAColumn(IComponent component, String expression)
    {
        return MESSAGES.format("not-a-column", component.getExtendedId(), expression);
    }

    static String invalidTableSource(IComponent component, Object sourceValue)
    {
        return MESSAGES.format("invalid-table-source", component.getExtendedId(), ClassFabUtils
                .getJavaClassName(sourceValue.getClass()));
    }

    static String invalidTableColumns(IComponent component, Object columnSource)
    {
        return MESSAGES.format("invalid-table-column", component.getExtendedId(), ClassFabUtils
                .getJavaClassName(columnSource.getClass()));
    }

    static String missingTableModel(IComponent component)
    {
        return MESSAGES.format("missing-table-model", component.getExtendedId());
    }

    static String columnsOnlyPlease(IComponent component)
    {
        return MESSAGES.format("columns-only-please", component.getExtendedId());
    }

    public static String invalidTableStateFormat(String value)
    {
        return MESSAGES.format("invalid-table-state-format", value);
    }
}
