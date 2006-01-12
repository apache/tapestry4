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

package org.apache.tapestry.engine;

import java.util.List;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public final class EngineMessages
{

    private final static Messages MESSAGES = new MessageFormatter(EngineMessages.class);

    /** @since 4.1 */
    private EngineMessages()
    {
    }

    public static String serviceNoParameter(IEngineService service)
    {
        return MESSAGES.format("service-no-parameter", service.getName());
    }

    public static String wrongComponentType(IComponent component, Class expectedType)
    {
        return MESSAGES.format("wrong-component-type", component.getExtendedId(), expectedType.getName());
    }

    public static String requestStateSession(IComponent component)
    {
        return MESSAGES.format("request-stale-session", component.getExtendedId());
    }

    public static String pageNotCompatible(IPage page, Class expectedType)
    {
        return MESSAGES.format("page-not-compatible", page.getPageName(), expectedType.getName());
    }

    static String exceptionDuringCleanup(Throwable cause)
    {
        return MESSAGES.format("exception-during-cleanup", cause);
    }

    static String validateCycle(List pageNames)
    {
        StringBuffer buffer = new StringBuffer();
        int count = pageNames.size();

        for(int i = 0; i < count; i++)
        {
            if (i > 0) buffer.append("; ");

            buffer.append(pageNames.get(i));
        }

        return MESSAGES.format("validate-cycle", buffer);
    }
}
