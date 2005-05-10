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

package org.apache.tapestry.engine;

import java.util.List;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
public class EngineMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(EngineMessages.class,
            "EngineStrings");

    public static String serviceNoParameter(IEngineService service)
    {
        return _formatter.format("service-no-parameter", service.getName());
    }

    static String wrongComponentType(IComponent component, Class expectedType)
    {
        return _formatter.format("wrong-component-type", component.getExtendedId(), expectedType
                .getName());
    }

    static String requestStateSession(IComponent component)
    {
        return _formatter.format("request-stale-session", component.getExtendedId());
    }

    static String pageNotCompatible(IPage page, Class expectedType)
    {
        return _formatter.format("page-not-compatible", page.getPageName(), expectedType.getName());
    }

    static String exceptionDuringCleanup(Throwable cause)
    {
        return _formatter.format("exception-during-cleanup", cause);
    }

    static String validateCycle(List pageNames)
    {
        StringBuffer buffer = new StringBuffer();
        int count = pageNames.size();

        for (int i = 0; i < count; i++)
        {
            if (i > 0)
                buffer.append("; ");

            buffer.append(pageNames.get(i));
        }

        return _formatter.format("validate-cycle", buffer);
    }
}