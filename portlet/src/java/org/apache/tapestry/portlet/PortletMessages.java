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

package org.apache.tapestry.portlet;

import org.apache.hivemind.Messages;
import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
final class PortletMessages
{

    private final static Messages MESSAGES = new MessageFormatter(PortletMessages.class);

    /** @since 4.1 */
    private PortletMessages()
    {
    }

    static String unsupportedMethod(String methodName)
    {
        return MESSAGES.format("unsupported-method", methodName);
    }

    static String errorGettingResource(String path, Throwable cause)
    {
        return MESSAGES.format("error-getting-resource", path, cause);
    }

    static String errorProcessingAction(Throwable cause)
    {
        return MESSAGES.format("error-processing-action", cause);
    }

    static String errorProcessingRender(Throwable cause)
    {
        return MESSAGES.format("error-processing-render", cause);
    }

    static String errorReportingException(Throwable cause)
    {
        return MESSAGES.format("error-reporting-exception", cause);
    }

    static String staleSession()
    {
        return MESSAGES.getMessage("stale-session");
    }
}
