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

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;
import org.apache.tapestry.IPage;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class EngineMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(EngineMessages.class,
            "EngineStrings");

    public static String serviceNoParameter(IEngineService service)
    {
        return _formatter.format("service-no-parameter", service.getName());
    }

    public static String wrongComponentType(IComponent component, Class expectedType)
    {
        return _formatter.format("wrong-component-type", component.getExtendedId(), expectedType
                .getName());
    }

    public static String requestStateSession(IComponent component)
    {
        return _formatter.format("request-stale-session", component.getExtendedId());
    }

    public static String pageNotCompatible(IPage page, Class expectedType)
    {
        return _formatter.format("page-not-compatible", page.getPageName(), expectedType.getName());
    }
}