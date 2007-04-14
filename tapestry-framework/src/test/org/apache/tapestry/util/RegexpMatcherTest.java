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
package org.apache.tapestry.util;

import com.javaforge.tapestry.testng.TestBase;
import org.testng.annotations.Test;


/**
 * Tests functionality of {@link RegexpMatcher}.
 */
@Test
public class RegexpMatcherTest extends TestBase
{
    RegexpMatcher _matcher = new RegexpMatcher();

    public void test_Basic_Match()
    {
        assertTrue(_matcher.contains("/\\w+.css", "/a/b/FooBar.css"));
        assertFalse(_matcher.contains("/\\w+.css", "/a/b/FooBar.gif"));
    }

    public void test_Begin_Line_Match()
    {
        assertTrue(_matcher.contains("^/org/apache/tapestry/.*.gif", "/org/apache/tapestry/util/things/wiggle.gif"));
        assertFalse(_matcher.contains("^/org/apache/tapestry/.*.gif", "apache/tapestry/util/things/wiggle.gif"));
    }

    public void test_Match_Result()
    {
        String input = "value=biff[fred's message]";
        RegexpMatch[] matches = _matcher.getMatches("^\\s*(\\$?\\w+)\\s*(=\\s*(((?!,|\\[).)*))?", input);

        assertEquals(matches.length, 1);
        assertEquals(matches[0].getMatchLength(), 10);
        assertEquals(matches[0].getGroup(1), "value");
        assertEquals(matches[0].getGroup(3), "biff");
    }
}
