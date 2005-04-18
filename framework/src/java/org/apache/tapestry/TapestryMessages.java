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

package org.apache.tapestry;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard Lewis Ship
 * @since 4.0
 */
final class TapestryMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(TapestryMessages.class,
            "TapestryStrings2");

    static String servletInitFailure(Throwable cause)
    {
        return _formatter.format("servlet-init-failure", cause);
    }

    static String componentIsLocked(IComponent component)
    {
        return _formatter.format("component-is-locked", component.getExtendedId());
    }

    static String servletInit(String name, long elapsedToRegistry, long elapsedOverall)
    {
        return _formatter.format("servlet-init", name, new Long(elapsedToRegistry), new Long(
                elapsedOverall));
    }

    static String nonUniqueAttribute(Object newInstance, String key, Object existingInstance)
    {
        return _formatter.format("non-unique-attribute", newInstance, key, existingInstance);
    }

    static String noPageRenderSupport(IComponent component)
    {
        return _formatter.format("no-page-render-support", component.getExtendedId());
    }

    static String providedByEnhancement(String methodName)
    {
        return _formatter.format("provided-by-enhancement", methodName);
    }
}