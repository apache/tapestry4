//  Copyright 2004 The Apache Software Foundation
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
 * 
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
final class TapestryMessages
{
    private static final MessageFormatter _formatter =
        new MessageFormatter(TapestryMessages.class, "TapestryStrings2");

    public static String servletInitFailure(Throwable cause)
    {
        return _formatter.format("servlet-init-failure", cause);
    }

    public static String paramNotNull(String parameterName)
    {
        return _formatter.format("param-not-null", parameterName);
    }
}
