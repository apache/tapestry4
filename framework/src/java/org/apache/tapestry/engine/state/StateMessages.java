// Copyright 2005 The Apache Software Foundation
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

package org.apache.tapestry.engine.state;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class StateMessages
{

    private static final Messages MESSAGES = new MessageFormatter(StateMessages.class);

    /** @since 4.1 */
    private StateMessages()
    {
    }

    static String unknownStateObjectName(String objectName)
    {
        return MESSAGES.format("unknown-state-object-name", objectName);
    }

    static String unableToInstantiateObject(Class objectClass, Throwable cause)
    {
        return MESSAGES.format("unable-to-instantiate-object", objectClass, cause);
    }

    static String unableToInstantiateObject(String className, Throwable cause)
    {
        return MESSAGES.format("unable-to-instantiate-object", className, cause);
    }

    static String unknownScope(String objectName, String scope)
    {
        return MESSAGES.format("unknown-scope", objectName, scope);
    }

}
