// Copyright 2004 The Apache Software Foundation
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

import org.apache.hivemind.Location;
import org.apache.hivemind.impl.MessageFormatter;
import org.apache.hivemind.service.ClassFabUtils;

/**
 * Container of static methods to format logging and exception messages, used
 * within the org.apache.tapesty.test package (and a few sub-packages).
 * 
 * <p>Technically, these are messages for the test package, and this class
 * should be called TestMessages ... but that's always a bad idea (it makes
 * the class look like a JUnit test suite).
 * 
 * <p>This class is public, not package private, because some related
 * sub-packages make use of it as well.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public final class ScriptMessages
{
    private static final MessageFormatter _formatter =
        new MessageFormatter(ScriptMessages.class, "ScriptStrings");

    public static String expectedSubstringMissing(String substring, Location location)
    {
        return _formatter.format("expected-substring-missing", substring, location);
    }

    public static String expectedRegexpMissing(String regexp, Location location)
    {
        return _formatter.format("expected-regexp-missing", regexp, location);
    }

    public static String unexpectedAttributeInElement(String attributeName, String elementName)
    {
        return _formatter.format("unexpected-attribute-in-element", attributeName, elementName);
    }

    public static String missingRequiredAttribute(String attributeName, String elementName)
    {
        return _formatter.format("missing-required-attribute", attributeName, elementName);
    }

    public static String invalidIntAttribute(
        String attributeName,
        String elementName,
        Location location,
        String attributeValue)
    {
        return _formatter.format(
            "invalid-int-attribute",
            new Object[] { attributeName, elementName, location, attributeValue });
    }

    public static String incorrectRegexpMatch(
        String expectedMatch,
        Location location,
        String actualMatch)
    {
        return _formatter.format("incorrect-regexp-match", expectedMatch, location, actualMatch);
    }

    public static String incorrectRegexpMatchCount(
        String pattern,
        Location location,
        int expectedCount,
        int actualCount)
    {
        return _formatter.format(
            "incorrect-regexp-match-count",
            new Object[] {
                pattern,
                location,
                new Integer(expectedCount),
                new Integer(actualCount)});
    }

    public static String wrongTypeForEnhancement(Class type)
    {
        return _formatter.format(
            "wrong-type-for-enhancement",
            ClassFabUtils.getJavaClassName(type));
    }

    public static String classNotAbstract(Class type)
    {
        return _formatter.format("class-not-abstract", type.getName());
    }

    public static String unableToIntrospect(Class type, Throwable cause)
    {
        return _formatter.format("unable-to-introspect", type.getName(), cause);
    }

    public static String unableToInstantiate(Class abstractClass, Throwable cause)
    {
        return _formatter.format("unable-to-instantiate", abstractClass.getName(), cause);
    }
}
