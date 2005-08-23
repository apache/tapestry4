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

package org.apache.tapestry.callback;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.IComponent;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class CallbackMessages
{
    private final static MessageFormatter _formatter = new MessageFormatter(CallbackMessages.class);

    static String pageNotExternal(String pageName)
    {
        return _formatter.format("page-not-external", pageName);
    }

    static String componentNotDirect(IComponent component)
    {
        return _formatter.format("component-not-direct", component.getExtendedId());
    }
}
