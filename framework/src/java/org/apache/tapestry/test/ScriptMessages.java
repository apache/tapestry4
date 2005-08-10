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

package org.apache.tapestry.test;

import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;

/**
 * Container of static methods to format logging and exception messages, used within the
 * org.apache.tapesty.test package (and a few sub-packages).
 * <p>
 * Technically, these are messages for the test package, and this class should be called
 * TestMessages ... but that's always a bad idea (it makes the class look like a JUnit test suite).
 * <p>
 * This class is public, not package private, because some related sub-packages make use of it as
 * well.
 * 
 * @author Howard Lewis Ship
 * @since 4.0
 */
public class ScriptMessages
{
    protected static MessageFormatter _formatter = new MessageFormatter(ScriptMessages.class,
            "ScriptStrings");

    static String wrongTypeForEnhancement(Class type)
    {
        return _formatter
                .format("wrong-type-for-enhancement", ClassFabUtils.getJavaClassName(type));
    }

    static String unableToInstantiate(Class abstractClass, Throwable cause)
    {
        return _formatter.format("unable-to-instantiate", abstractClass.getName(), cause);
    }
}