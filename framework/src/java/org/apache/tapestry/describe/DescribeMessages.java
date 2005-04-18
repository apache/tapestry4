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

package org.apache.tapestry.describe;

import org.apache.hivemind.impl.MessageFormatter;

/**
 * @author Howard M. Lewis Ship
 * @since 4.0
 */
class DescribeMessages
{
    private static final MessageFormatter _formatter = new MessageFormatter(DescribeMessages.class,
            "DescribeStrings");

    public static String mustSetTitleBeforeSection()
    {
        return _formatter.getMessage("must-set-title-before-section");
    }

    public static String setTitleOnce()
    {
        return _formatter.getMessage("set-title-once");
    }

    public static String mustSetTitleBeforeProperty()
    {
        return _formatter.getMessage("must-set-title-before-property");
    }
}