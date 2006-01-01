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

package org.apache.tapestry.util;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class UtilMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(UtilMessages.class);

    static String valueNotFound(Object value)
    {
        return _formatter.format("value-not-found", value);
    }

    static String keyAlreadyExists(Object key)
    {
        return _formatter.format("key-already-exists", key);
    }
}
