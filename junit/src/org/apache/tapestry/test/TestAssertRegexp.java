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

import org.apache.hivemind.ApplicationRuntimeException;
import org.apache.tapestry.junit.TapestryTestCase;
import org.apache.tapestry.test.assertions.AssertRegexp;
import org.apache.tapestry.test.assertions.RegexpMatch;

/**
 * Tests for the {@link org.apache.tapestry.test.assertions.AssertRegexp}
 * class.
 *
 * @author Howard Lewis Ship
 * @since 3.1
 */
public class TestAssertRegexp extends TapestryTestCase
{
    public void testSuccess() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<title>.*</title>");

        ar.execute(ss);
    }

    public void testFailure() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<body>.*</body>");

        try
        {
            ar.execute(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                "Expected regular expression \\(\"<body>.*</body>\", at .*?\\) was not found in the response\\.",
                ex.getMessage());
        }
    }

    public void testMatchesSuccess() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<.*?>");

        addMatch(ar, "<title>");
        addMatch(ar, "</title>");

        ar.execute(ss);
    }

    public void testMatchesWrongCount() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<.*?>");

        addMatch(ar, "<title>");

        try
        {
            ar.execute(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                "Regular expression '<\\.\\*\\?>' \\(at .*?\\) should have generated 1 matches, but generated 2 instead\\.",
                ex.getMessage());
        }
    }
    public void testMatchesSubgroupSuccess() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<(.*?)>");
        ar.setSubgroup(1);

        addMatch(ar, "title");
        addMatch(ar, "/title");

        ar.execute(ss);
    }

    public void testMatchesFailure() throws Exception
    {
        ScriptedTestSession ss = TestScriptParser.createSession();

        AssertRegexp ar = new AssertRegexp();
        ar.setRegexp("<.*?>");

        addMatch(ar, "<little>");
        addMatch(ar, "</title>");

        try
        {
            ar.execute(ss);
            unreachable();
        }
        catch (ApplicationRuntimeException ex)
        {
            assertRegexp(
                "Expected match was '<little>' \\(at .*?\\), but actual value matched was '<title>'\\.",
                ex.getMessage());
        }
    }

    private void addMatch(AssertRegexp ar, String matchValue)
    {
        RegexpMatch m = new RegexpMatch();
        m.setExpectedString(matchValue);

        ar.addMatch(m);
    }
}
