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

package org.apache.tapestry.web;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 3.1
 */
class WebMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(WebMessages.class,
            "WebStrings");

    public static String streamOpenError(String contentType, Throwable cause)
    {
        return _formatter.format("stream-open-error", contentType, cause);
    }

    public static String errorGettingResource(String path, Throwable ex)
    {
        return _formatter.format("error-getting-resource", path, ex);
    }
}