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

package org.apache.tapestry.listener;

import java.lang.reflect.Method;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.Tapestry;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class ListenerMessages
{
    private static final Messages MESSAGES = new MessageFormatter(ListenerMessages.class);

    /** @since 4.1 */
    private ListenerMessages() {}
    
    static String objectMissingMethod(Object target, String name)
    {
        return MESSAGES.format("object-missing-method", target, name);
    }

    static String unableToInvokeMethod(Method method, Object target, Throwable ex)
    {
        return MESSAGES.format("unable-to-invoke-method", method.getName(), target, ex);
    }

    static String listenerMethodFailure(Method m, Object target, Throwable cause)
    {
        return MESSAGES.format("listener-method-failure", m, target, cause);
    }

    static String noListenerMethodFound(String name, Object[] serviceParameters, Object target)
    {
        return MESSAGES.format("no-listener-method-found", name, new Integer(Tapestry
                .size(serviceParameters)), target);
    }
}