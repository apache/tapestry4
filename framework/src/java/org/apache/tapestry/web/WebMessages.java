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

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.tapestry.util.ContentType;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class WebMessages
{

    private static final Messages MESSAGES = new MessageFormatter(WebMessages.class);

    /** @since 4.1 */
    private WebMessages()
    {
    }

    static String streamOpenError(ContentType contentType, Throwable cause)
    {
        return MESSAGES.format("stream-open-error", contentType, cause);
    }

    static String errorGettingResource(String path, Throwable ex)
    {
        return MESSAGES.format("error-getting-resource", path, ex);
    }

    static String unableToFindDispatcher(String url)
    {
        return MESSAGES.format("unable-to-find-dispatcher", url);
    }

    static String unableToForward(String url, Throwable cause)
    {
        return MESSAGES.format("unable-to-forward", url, cause);
    }

    static String unableToRedirect(String url, Throwable cause)
    {
        return MESSAGES.format("unable-to-redirect", url, cause);
    }

    static String writerOpenError(ContentType contentType, Throwable cause)
    {
        return MESSAGES.format("writer-open-error", contentType, cause);
    }

    static String resetFailed(Throwable cause)
    {
        return MESSAGES.format("reset-failed", cause);
    }

    static String contentTypeUnchanged(ContentType existing, ContentType requested)
    {
        return MESSAGES.format("content-type-unchanged", existing, requested);
    }
}
