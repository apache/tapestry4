// Copyright 2005, 2006 The Apache Software Foundation
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

package org.apache.tapestry.markup;

import java.util.List;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.util.ContentType;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class MarkupMessages
{

    private final static Messages MESSAGES = new MessageFormatter(MarkupMessages.class, "MarkupStrings");

    /** @since 4.1 */
    private MarkupMessages()
    {
    }

    static String tagNotOpen()
    {
        return MESSAGES.getMessage("tag-not-open");
    }

    static String elementNotOnStack(String name, List activeElementStack)
    {
        StringBuffer buffer = new StringBuffer();

        int count = activeElementStack.size();

        for(int i = 0; i < count; i++)
        {
            if (i > 0) buffer.append(", ");

            buffer.append(activeElementStack.get(i));
        }

        return MESSAGES.format("element-not-on-stack", name, buffer.toString());
    }

    static String endWithEmptyStack()
    {
        return MESSAGES.getMessage("end-with-empty-stack");
    }

    static String noFilterMatch(ContentType contentType)
    {
        return MESSAGES.format("no-filter-match", contentType);
    }

    static String closeOnce()
    {
        return MESSAGES.getMessage("close-once");
    }
}
